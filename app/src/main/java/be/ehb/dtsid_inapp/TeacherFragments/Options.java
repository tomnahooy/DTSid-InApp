package be.ehb.dtsid_inapp.TeacherFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import be.ehb.dtsid_inapp.R;

public class Options extends Fragment
{
    Button studentRegistrerenBTN;
    Button lijstBTN;
    Button regioBTN;
    Button optiesBTN;
    Button syncBTN;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_options_dashboardscreen, null);

        return v;
    }
}
