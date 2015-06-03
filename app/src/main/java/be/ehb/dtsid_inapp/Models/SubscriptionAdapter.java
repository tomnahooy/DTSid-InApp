package be.ehb.dtsid_inapp.Models;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import be.ehb.dtsid_inapp.R;

/**
 * Created by doortje on 1/06/15.
 */
public class SubscriptionAdapter extends BaseAdapter {

    private ArrayList<Subscription> subscriptionArrayList;
    private LayoutInflater inflater;

    private static class ViewHolder
    {
        TextView naamVoornaamTV;
        TextView interestTV;
        TextView schoolTV;
    }

    public SubscriptionAdapter(Activity activity, ArrayList<Subscription> subscriptionArrayList) {
        this.subscriptionArrayList = subscriptionArrayList;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return subscriptionArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return subscriptionArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return subscriptionArrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.subscriptions_list_item,null);
            holder.naamVoornaamTV = (TextView) convertView.findViewById(R.id.tv_naam_voornaam_subscriptionitemlist);
            holder.interestTV = (TextView) convertView.findViewById(R.id.tv_interest_subscriptionitemlist);
            holder.schoolTV = (TextView) convertView.findViewById(R.id.tv_school_subscriptionitemlist);

            convertView.setTag(holder);

        }

        else
            holder = (ViewHolder)convertView.getTag();

        Subscription subscriptionByRow = subscriptionArrayList.get(position);

        holder.naamVoornaamTV.setText(subscriptionByRow.getLastName().toString() + subscriptionByRow.getFirstName().toString());
        holder.interestTV.setText(subscriptionByRow.getInterests().toString());
        holder.schoolTV.setText(subscriptionByRow.getSchool().toString());

        return convertView;
    }
}