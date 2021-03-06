package be.ehb.dtsid_inapp.StudentFragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import be.ehb.dtsid_inapp.Activities.StudentActivity;
import be.ehb.dtsid_inapp.Database.DatabaseContract;
import be.ehb.dtsid_inapp.JSONTasks.PostJSONTask;
import be.ehb.dtsid_inapp.Models.Gemeente;
import be.ehb.dtsid_inapp.Models.School;
import be.ehb.dtsid_inapp.Models.Subscription;
import be.ehb.dtsid_inapp.Models.XmlHandler;
import be.ehb.dtsid_inapp.R;
import be.ehb.dtsid_inapp.TeacherFragments.Lists;
import be.ehb.dtsid_inapp.TeacherFragments.OptionsPreferences;

/**
 * @author Dries, Dorothée, Tom
 */

public class StudentRegistrationPt2 extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Switch digxSW, multecSW, werkstudentSW, studeerNogSW;
    private TextView schoolZipTV, schoolNameTV,interestTV;
    private EditText schoolZipET, schoolNameET;
    private Button confirmBTN, backBTN, cancelBTN;
    private Animation buttonAnim;
    private Subscription currentSubscription;
    private StudentActivity activity;
    private DatabaseContract dbc;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private School currentSchool;
    private ArrayList<School> allSchools;
    private ArrayList<Gemeente> allGemeenten;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_student_registration2_2, container, false);

        activity = (StudentActivity) this.getActivity();
        dbc = new DatabaseContract(activity.getApplicationContext());
        digxSW = (Switch) v.findViewById(R.id.sw_stud_reg_digx);
        multecSW = (Switch) v.findViewById(R.id.sw_stud_reg_multec);
        werkstudentSW = (Switch) v.findViewById(R.id.sw_stud_reg_werkstudent);
        studeerNogSW = (Switch) v.findViewById(R.id.sw_ik_studeer_nog);
        schoolNameTV = (TextView) v.findViewById(R.id.tv_stud_reg_2_name_school);
        schoolZipTV = (TextView) v.findViewById(R.id.tv_stud_reg_2_zip);
        schoolZipET = (EditText) v.findViewById(R.id.et_stud_reg_2_zip);
        schoolNameET = (EditText) v.findViewById(R.id.et_stud_reg_2_name_school);
        confirmBTN = (Button) v.findViewById(R.id.btn_bevestigen_subscription2);
        backBTN = (Button) v.findViewById(R.id.btn_stud_reg_2_back);
        cancelBTN = (Button) v.findViewById(R.id.btn_annuleren_subscription2);
        interestTV = (TextView) v.findViewById(R.id.tv_stud_reg_2_interests);
        buttonAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.button_animation_large);

        Typeface myCustomFont = Typeface.createFromAsset(activity.getAssets()
                , "fonts/ehb_font.ttf");

        digxSW.setTypeface(myCustomFont);
        multecSW.setTypeface(myCustomFont);
        werkstudentSW.setTypeface(myCustomFont);
        studeerNogSW.setTypeface(myCustomFont);
        schoolNameTV.setTypeface(myCustomFont);
        schoolZipTV.setTypeface(myCustomFont);
        schoolZipET.setTypeface(myCustomFont);
        confirmBTN.setTypeface(myCustomFont);
        backBTN.setTypeface(myCustomFont);
        cancelBTN.setTypeface(myCustomFont);
        interestTV.setTypeface(myCustomFont);

        schoolZipET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4){
                    final ArrayList<School> relevantSchools = getRelevantSchools(s.toString());
                    if (relevantSchools.isEmpty()){
                        Toast.makeText(getActivity().getApplicationContext()
                                , "Geen scholen gevonden voor postcode!", Toast.LENGTH_LONG);
                        schoolZipET.setText("");
                    }
                    else {
                        final String[] relevantSchoolNames = new String[relevantSchools.size()];
                        for (int i = 0; i < relevantSchools.size(); i++){
                            relevantSchoolNames[i] = relevantSchools.get(i).getName();
                        }
                        AlertDialog.Builder schoolDialogBuilder = new AlertDialog.Builder(activity);
                        schoolDialogBuilder.setTitle("Selecteer school");
                        schoolDialogBuilder.setItems(relevantSchoolNames, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                schoolNameET.setText(relevantSchoolNames[which]);
                                currentSchool = relevantSchools.get(which);
                                dialog.dismiss();
                            }
                        });
                        schoolDialogBuilder.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                schoolZipET.setText("");
                                dialog.dismiss();
                            }
                        });
                        AlertDialog schoolDialog = schoolDialogBuilder.create();
                        schoolDialog.show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmBTN.setOnClickListener(this);
        backBTN.setOnClickListener(this);

        enableSchoolDetails(false);

        digxSW.setOnCheckedChangeListener(this);
        multecSW.setOnCheckedChangeListener(this);
        werkstudentSW.setOnCheckedChangeListener(this);
        studeerNogSW.setOnCheckedChangeListener(this);



        currentSubscription = activity.getCurrentSubscription();
        Log.d("StudReg2", "digx: " + currentSubscription.getDigx()
                + "multec: " + currentSubscription.getMultec()
                + "werkstud: " + currentSubscription.getWerkstudent()
                + currentSubscription.getFirstName()
                + currentSubscription.getSchool().getName());

        //CREATE Autocomplete school


        //Create the AlertDialogBuilder
        builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbc.createSubscription(currentSubscription);
                dbc.close();
                dialog.dismiss();
                activity.finish();
                startActivity(activity.getIntent());
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                confirmBTN.setVisibility(View.VISIBLE);
            }
        });

        return v;
    }

    /**
     * @param v
     */
    private void nagivateAfterClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_stud_reg_2_back:

                activity.onBackPressed();
                break;

            case R.id.btn_bevestigen_subscription2:
                if (studeerNogSW.isChecked() && !schoolNameET.getText().equals(""))
                {
                    currentSubscription.setSchool(currentSchool);
                }
                builder .setMessage("Name: " + currentSubscription.getFirstName() + " " + currentSubscription.getLastName() +
                                    "\nE-mail: " + currentSubscription.getEmail() +
                                    "\nZip: " + currentSubscription.getZip() +
                                    "\nCity: " + currentSubscription.getCity() +
                                    "\nStreet: " + currentSubscription.getStreet() + " " + currentSubscription.getStreetNumber() +
                                    "\nSchool: " + currentSubscription.getSchool().getName() +
                                    "\nInterested in DigX: " + currentSubscription.getDigx() +
                                    "\nInterested in MulTec: " + currentSubscription.getMultec() +
                                    "\nWorking student: " + currentSubscription.getWerkstudent())
                        .setTitle("Signing in - Is this data correct?");
                dialog = builder.create();
                dialog.show();
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        final View vf = v;
        vf.startAnimation(buttonAnim);
        buttonAnim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                vf.setVisibility(View.INVISIBLE);
                nagivateAfterClick(vf);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_stud_reg_digx:
                currentSubscription.setDigx(isChecked);
                break;
            case R.id.sw_stud_reg_multec:
                currentSubscription.setMultec(isChecked);
                break;
            case R.id.sw_stud_reg_werkstudent:
                currentSubscription.setWerkstudent(isChecked);
                break;
            case R.id.sw_ik_studeer_nog:
                enableSchoolDetails(isChecked);
                break;
        }
    }

    /**
     * @param isChecked
     */
    private void enableSchoolDetails(boolean isChecked)
    {
        schoolZipTV.setEnabled(isChecked);
        schoolZipET.setEnabled(isChecked);
        schoolNameTV.setEnabled(isChecked);
        schoolNameET.setEnabled(isChecked);
    }

    /**
     * @param postcode
     * @return ArrayList<School> relevantSchools
     */
    private ArrayList<School> getRelevantSchools(String postcode) {
        allSchools = (ArrayList<School>) dbc.getAllSchools();
        ArrayList<School> relevantSchools = new ArrayList<>();
        for (School i : allSchools)
        {
            if (String.valueOf(i.getZip()).equals(postcode))
                relevantSchools.add(i);
        }
        return relevantSchools;
    }

}
