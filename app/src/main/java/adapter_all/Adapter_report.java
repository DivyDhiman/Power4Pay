package adapter_all;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import Api_all.Report_repsonse;
import Api_all.Soap_api;
import Functionality_Class.SharedPrefSave;
import freacharge.power4pay.com.power4pay.R;
import freacharge.power4pay.com.power4pay.Reacharge_all;

/**
 * Created by Abhay dhiman
 */

public class Adapter_report extends RecyclerView.Adapter<Adapter_report.ViewHolder> {

    private View view;
    private Context resultScreen;
    private ArrayList<HashMap<String, Object>> report_list;
    private ProgressDialog pDialog;
    private String response, status, message,username,txt_id;
    private Soap_api soap_api;
    private Report_repsonse report_repsonse;
    private SharedPrefSave sharedPrefSave;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView text_amount, text_uid, text_opening, text_closing, text_commission, text_mode, text_transtatus, text_opertaor, text_msgintime, text_customerno, text_outmsg;
        public LinearLayout img_report;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            text_amount = (TextView) view.findViewById(R.id.text_amount);
            text_uid = (TextView) view.findViewById(R.id.text_uid);
            text_opening = (TextView) view.findViewById(R.id.text_opening);
            text_closing = (TextView) view.findViewById(R.id.text_closing);
            text_commission = (TextView) view.findViewById(R.id.text_commission);
            text_mode = (TextView) view.findViewById(R.id.text_mode);
            text_transtatus = (TextView) view.findViewById(R.id.text_transtatus);
            text_opertaor = (TextView) view.findViewById(R.id.text_opertaor);
            text_msgintime = (TextView) view.findViewById(R.id.text_msgintime);
            text_customerno = (TextView) view.findViewById(R.id.text_customerno);
            text_outmsg = (TextView) view.findViewById(R.id.text_outmsg);
            img_report = (LinearLayout) view.findViewById(R.id.img_report);
        }
    }

    public Adapter_report(Context context, ArrayList<HashMap<String, Object>> report_list_all) {
        resultScreen = context;
        report_list = report_list_all;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report, parent, false);

        soap_api = new Soap_api();
        report_repsonse = (Report_repsonse) resultScreen;
        sharedPrefSave = new SharedPrefSave(resultScreen);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Adapter_report.ViewHolder holder, final int position) {

        holder.text_amount.setText(report_list.get(position).get("Amount").toString());
        holder.text_uid.setText(report_list.get(position).get("UID").toString());
        holder.text_opening.setText(report_list.get(position).get("Opening").toString());
        holder.text_closing.setText(report_list.get(position).get("Closing").toString());
        holder.text_commission.setText(report_list.get(position).get("Commission").toString());
        holder.text_mode.setText(report_list.get(position).get("Mode").toString());
        holder.text_transtatus.setText(report_list.get(position).get("TransactionStatus").toString());
        holder.text_opertaor.setText(report_list.get(position).get("Operator").toString());
        holder.text_msgintime.setText(report_list.get(position).get("MsgInTime").toString());
        holder.text_customerno.setText(report_list.get(position).get("CustomerNo").toString());
        holder.text_outmsg.setText(report_list.get(position).get("OutMsg").toString());

        holder.img_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = sharedPrefSave.getString("username");
                txt_id = report_list.get(position).get("UID").toString();
                new Report_Submit_Api().execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return report_list.size();
    }

    class Report_Submit_Api extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(resultScreen);
            pDialog.setMessage(resultScreen.getString(R.string.Pleasewait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            SoapObject request = new SoapObject("http://tempuri.org/", "RegisterComplain");
            PropertyInfo pi = new PropertyInfo();
            pi.setName("txtRefID");
            pi.setValue(txt_id);
            pi.setType(String.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("UserName");
            pi.setValue(username);
            pi.setType(String.class);
            request.addProperty(pi);

            response = soap_api.Api_Call(request, "http://tempuri.org/RegisterComplain");
            Log.e("resporesult", "resporesult" + response);

            try {

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(new StringReader(response)));
                // normalize the document
                doc.getDocumentElement().normalize();
                // get the root node
                NodeList nodeList = doc.getElementsByTagName("getresponse");
                Node node = nodeList.item(0);
                NodeList statuslist = doc.getElementsByTagName("responsecode");
                Node statusnode = statuslist.item(0);
                status = statusnode.getTextContent();
                if (status.equals("1")) {
                    Node messagenode = node.getChildNodes().item(0);
                    message = messagenode.getTextContent();
                } else {
                    Node messagenode = node.getChildNodes().item(0);
                    message = messagenode.getTextContent();
                }

            } catch (ParserConfigurationException e) {
                response = "Error";
            } catch (SAXException e2) {
                response = "Error";
            } catch (IOException e3) {
                response = "Error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (response != null) {
                if (!response.equals("Error")) {
                    if (status.equals("1")) {
                        report_repsonse.response_send(message);
                    } else {
                        report_repsonse.response_send(message);
                    }
                } else {
                    report_repsonse.response_send(resultScreen.getString(R.string.oopswrongfailed));
                }
            } else {
                report_repsonse.response_send(resultScreen.getString(R.string.internerfailed));
            }
        }
    }

}

