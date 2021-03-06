package be.ehb.dtsid_inapp.Activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import be.ehb.dtsid_inapp.Database.DatabaseContract;
import be.ehb.dtsid_inapp.Models.Event;
import be.ehb.dtsid_inapp.Models.Subscription;
import be.ehb.dtsid_inapp.Models.Teacher;
import be.ehb.dtsid_inapp.R;
import be.ehb.dtsid_inapp.StudentFragments.PhotoGallery;
import be.ehb.dtsid_inapp.StudentFragments.StudentRegistration;

/**
 * @author Dries
 * @version 1.0
 *
 */

public class StudentActivity extends AppCompatActivity 
{
    private DatabaseContract dbc;
    private Boolean isInMainScreen = true;
    private Boolean isInSecondReg;
    private Teacher teacher;
    private Event event;
    StudentRegistration registrationFragment;
    PhotoGallery photoFragment;
    private Subscription currentSubscription = null;
    private AlertDialog dialog;
    private Boolean inLeftFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        /**
         * Create the AlertDialogBuilder.
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        builder .setMessage("Are you sure you want to log out?")
                .setTitle("Logging out");

        /**
         * Create the Dialog itself.
         */
        dialog = builder.create();

        isInSecondReg = false;

        dbc = new DatabaseContract(getApplicationContext());

        /**
         * Get current Teacher.
         * Get current Event.
         */
        teacher = dbc.getTeacherByID(getIntent().getLongExtra("Teacher_id", 0));
        event = dbc.getEventByID(getIntent().getLongExtra("Event_id", 0));

        dbc.close();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        registrationFragment = new StudentRegistration();
        photoFragment = new PhotoGallery();

        /**
         * Adding fragments to the screen.
         */
        ft.add(R.id.fragm_left_registration, registrationFragment);
        ft.add(R.id.fragm_right_images, photoFragment);

        ft.commit();
    }

    /**
     * If backbutton is pressed.
     */
    @Override
    public void onBackPressed()
    {
        if(isInMainScreen)
        {
            dialog.show();
        }
        else
        {
            if (isInSecondReg) 
            {
                isInMainScreen = false;
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragm_left_registration, registrationFragment)
                        .commit();
                registrationFragment.setEnabled(true);
                isInSecondReg = false;
                Log.d("MainScreen ", isInMainScreen.toString());
            }
            else 
            {
                inLeftFragment = false;
                changeWeightOfFragments(50, 50);
                registrationFragment.setEnabled(false);
                isInMainScreen = true;
            }
        }
    }

    /**
     * Enlarge the left fragment by changing the weight.
     */
    public void leftTouched()
    {
        if(!inLeftFragment)
        {
            inLeftFragment = true;
            isInMainScreen = false;
            changeWeightOfFragments(100, 0);
        }
    }

    /**
     * Enlarge the right fragment by changing the weight.
     */
    public void rightTouched()
    {
        isInMainScreen = false;
        changeWeightOfFragments(0, 100);
    }

    /**
     * changes the weight of the fragments
     * @param weightLeftFragment
     * @param weigthRightFragment
     */
    private void changeWeightOfFragments(final float weightLeftFragment, final float weigthRightFragment)
    {
        /**
         * Set registration weight.
         */
        final FrameLayout flRegistration = (FrameLayout) findViewById(R.id.fragm_left_registration);

        Animation lAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                LinearLayout.LayoutParams lpRegistration = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        50 + (weightLeftFragment - 50) * interpolatedTime);
                flRegistration.setLayoutParams(lpRegistration);
            }
        };

        /**
         * Set images weight.
         */
        final FrameLayout flImages = (FrameLayout) findViewById(R.id.fragm_right_images);

        Animation rAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                LinearLayout.LayoutParams lpImages = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        50 + (weightLeftFragment - 50) * interpolatedTime);
                flImages.setLayoutParams(lpImages);
            }
        };
        lAnim.setDuration(500);
        rAnim.setDuration(500);
        flRegistration.startAnimation(lAnim);
        flImages.startAnimation(rAnim);
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Event getEvent() {
        return event;
    }

    /**
     * @return The current subscription
     */
    public Subscription getCurrentSubscription() {
        return currentSubscription;
    }

    /**
     *
     * @param currentSubscription
     */
    public void setCurrentSubscription(Subscription currentSubscription) {
        this.currentSubscription = currentSubscription;
    }

    public Boolean getIsInSecondReg() {
        return isInSecondReg;
    }

    public void setIsInSecondReg(Boolean isInSecondReg) {
        this.isInSecondReg = isInSecondReg;
    }
}