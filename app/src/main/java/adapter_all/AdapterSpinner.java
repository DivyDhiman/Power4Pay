package adapter_all;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import Api_all.Data_Pass;
import freacharge.power4pay.com.power4pay.R;

/**
 * Created by Abhay dhiman
 */
public class AdapterSpinner extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<HashMap<String, String>> operatorList;
    private Data_Pass data_pass;

    public AdapterSpinner(Context context, int layoutResourceId, ArrayList<HashMap<String, String>> operatorList) {
        super(context, layoutResourceId,operatorList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.operatorList = operatorList;
        data_pass = (Data_Pass)context;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //return super.getView(position, convertView, parent);

        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {

            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.txtViewName = (TextView) row.findViewById(R.id.txtViewName);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.txtViewName.setText(operatorList.get(position).get("Operator"));
        data_pass.data_send(operatorList.get(position).get("OPCODE"));

        return row;
    }

    static class ViewHolder {
        TextView txtViewName;
    }


}