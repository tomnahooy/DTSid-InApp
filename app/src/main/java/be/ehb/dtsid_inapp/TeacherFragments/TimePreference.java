package be.ehb.dtsid_inapp.TeacherFragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Dries
 * @version 1.0
 *
 *
 */

public class TimePreference extends DialogPreference
{
    private Calendar calendar;
    private TimePicker picker = null;

    public TimePreference(Context context, AttributeSet attrs)
    {
        this(context, attrs, android.R.attr.dialogPreferenceStyle);
    }

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");

        calendar = new GregorianCalendar();
    }

    @Override
    protected View onCreateDialogView()
    {
        picker = new TimePicker(getContext());
        return (picker);
    }

    @Override
    protected void onBindDialogView(View v)
    {
        super.onBindDialogView(v);
        picker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        picker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult)
    {
        super.onDialogClosed(positiveResult);

        if(positiveResult)
        {
            calendar.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
            calendar.set(Calendar.MINUTE, picker.getCurrentMinute());

            setSummary(getSummary());
            if(callChangeListener(calendar.getTimeInMillis()))
            {
                persistLong(calendar.getTimeInMillis());
                notifyChanged();
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index)
    {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue)
    {
        if(restoreValue)
        {
            if(defaultValue == null)
                calendar.setTimeInMillis(getPersistedLong(System.currentTimeMillis()));
            else
                calendar.setTimeInMillis(Long.parseLong(getPersistedString((String) defaultValue)));
        }
        else
        {
            if(defaultValue == null)
                calendar.setTimeInMillis(System.currentTimeMillis());
            else
                calendar.setTimeInMillis(Long.parseLong((String) defaultValue));
        }
        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary()
    {
        if(calendar == null)
            return null;
        else
            return DateFormat.getTimeFormat(getContext()).format(new Date(calendar.getTimeInMillis()));
    }

}
