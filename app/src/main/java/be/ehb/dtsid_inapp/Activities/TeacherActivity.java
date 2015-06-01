package be.ehb.dtsid_inapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import static be.ehb.dtsid_inapp.JSONTasks.JSONContract.*;

import be.ehb.dtsid_inapp.JSONTasks.GetJSONTask;
import be.ehb.dtsid_inapp.JSONTasks.JSONContract;
import be.ehb.dtsid_inapp.R;
import be.ehb.dtsid_inapp.TeacherFragments.DepartmentLogin;
import be.ehb.dtsid_inapp.TeacherFragments.Options;
import be.ehb.dtsid_inapp.TeacherFragments.TeacherLogin;

public class TeacherActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        //Start first fragment
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.teacherContainer, new DepartmentLogin())
                .commit();
        String url = BASEURL + ALL_SCHOOLS;
        GetJSONTask jsonTask = new GetJSONTask();
        jsonTask.execute(url);
    }

    public void goToOtherFragment(View v)
    {
        Button goToButton = (Button) v;

        if(goToButton.getId() == R.id.btn_department_login)
        {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.teacherContainer, new TeacherLogin())
                    .commit();
        }

        else if(goToButton.getId() == R.id.btn_teacher_login)
        {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.teacherContainer, new Options())
                    .commit();
        }

        else if(goToButton.getId() == R.id.btn_goto_studentactivity)
        {
            Intent studentIntent = new Intent(getApplicationContext(), StudentActivity.class);
            startActivity(studentIntent);
        }
    }
}