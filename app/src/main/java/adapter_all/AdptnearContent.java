package adapter_all;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import freacharge.power4pay.com.power4pay.R;
import freacharge.power4pay.com.power4pay.Reacharge_all;

/**
 * Created by Abhay dhiman
 */

public class AdptnearContent extends RecyclerView.Adapter<AdptnearContent.ViewHolder> {

    private View view;
    private Context resultScreen;
    private Intent intent;
    private ArrayList<HashMap<String, Object>> dashboardlist;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView text_name;
        private RelativeLayout click_all;
        private ImageView image_logo;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            text_name = (TextView) view.findViewById(R.id.text_name);
            image_logo = (ImageView) view.findViewById(R.id.image_logo);
            click_all = (RelativeLayout) view.findViewById(R.id.click_all);
        }
    }

    public AdptnearContent(Context context, ArrayList<HashMap<String, Object>> dashboardlist_all) {
        resultScreen = context;
        dashboardlist = dashboardlist_all;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adptnear_content, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdptnearContent.ViewHolder holder, final int position) {
        holder.text_name.setText(dashboardlist.get(position).get("name").toString());
        holder.image_logo.setImageResource((Integer) dashboardlist.get(position).get("image"));

        holder.click_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(resultScreen, Reacharge_all.class);
                intent.putExtra("name", dashboardlist.get(position).get("name").toString());
                intent.putExtra("image", (Integer) dashboardlist.get(position).get("image"));
                intent.putExtra("type",dashboardlist.get(position).get("type").toString());

                resultScreen.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dashboardlist.size();
    }
}
