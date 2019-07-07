package menu_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import Api_all.Report_repsonse;
import Api_all.Soap_api;
import Functionality_Class.SharedPrefSave;
import adapter_all.Adapter_report;
import adapter_all.AdptnearContent;
import controller_all.Controller;
import freacharge.power4pay.com.power4pay.MainActivity;
import freacharge.power4pay.com.power4pay.R;

/**
 * Created by Abhay dhiman
 */

public class Report extends AppCompatActivity implements Report_repsonse{

    private RecyclerView report_recy;
    private Context context;
    private CoordinatorLayout coordinate_layout;
    private ArrayList<HashMap<String, Object>> reportlist;
    private HashMap<String, Object> reportlist_sub;
    private ProgressDialog pDialog;
    private Soap_api soap_api;
    private String username, password, status, message, response;
    private SharedPrefSave sharedPrefSave;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_layout);

        context = Report.this;
        soap_api = new Soap_api();
        setCustomActionBar();
        controller = new Controller();
        sharedPrefSave = new SharedPrefSave(context);
        username = sharedPrefSave.getString("username");
        password = sharedPrefSave.getString("password");

        initlilzer();
    }

    private void initlilzer() {
        coordinate_layout = (CoordinatorLayout) findViewById(R.id.coordinate_layout);
        report_recy = (RecyclerView) findViewById(R.id.report_recy);
        report_recy.setLayoutManager(new LinearLayoutManager(report_recy.getContext()));

        new Report_Api().execute();
    }

    //Custom Action Bar
    private void setCustomActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView text_coustomaction = (TextView) toolbar.findViewById(R.id.text_coustomaction);
        ImageView setting_coustomaction = (ImageView) toolbar.findViewById(R.id.setting_coustomaction);
        setting_coustomaction.setVisibility(View.GONE);
        text_coustomaction.setText(getString(R.string.report));
    }

    @Override
    public void response_send(String rep_send) {
        controller.Shackbarall(coordinate_layout, rep_send);
    }

    class Report_Api extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
            pDialog.setMessage(getString(R.string.Pleasewait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            reportlist = new ArrayList<>();

            SoapObject request = new SoapObject("http://tempuri.org/", "Report");
            PropertyInfo pi = new PropertyInfo();
            pi.setName("UserName");
            pi.setValue(username);
            pi.setType(String.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("Password");
            pi.setValue(password);
            pi.setType(String.class);
            request.addProperty(pi);

            response = soap_api.Api_Call(request, "http://tempuri.org/Report");

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
                    Element elem1 = (Element) node;
                    NodeList nodereport = elem1.getElementsByTagName("RechargeReport");
                    Node nodereport_sub = nodereport.item(0);
                    Element elem2 = (Element) nodereport_sub;
                    NodeList nodetable = elem2.getElementsByTagName("Table");

                    for (int temp = 0; temp < nodetable.getLength(); temp++) {
                        reportlist_sub = new HashMap<>();
                        Node nNode = nodetable.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            reportlist_sub.put("UID", eElement.getElementsByTagName("UID").item(0).getTextContent());
                            reportlist_sub.put("Amount", eElement.getElementsByTagName("Amount").item(0).getTextContent());
                            reportlist_sub.put("CustomerNo", eElement.getElementsByTagName("CustomerNo.").item(0).getTextContent());
                            reportlist_sub.put("Operator", eElement.getElementsByTagName("Operator").item(0).getTextContent());
                            reportlist_sub.put("TransactionStatus", eElement.getElementsByTagName("TransactionStatus").item(0).getTextContent());
                            reportlist_sub.put("MsgInTime", eElement.getElementsByTagName("MsgInTime").item(0).getTextContent());
                            reportlist_sub.put("OutMsg", eElement.getElementsByTagName("OutMsg").item(0).getTextContent());
                            reportlist_sub.put("Opening", eElement.getElementsByTagName("Opening").item(0).getTextContent());
                            reportlist_sub.put("Closing", eElement.getElementsByTagName("Closing").item(0).getTextContent());
                            reportlist_sub.put("Commission", eElement.getElementsByTagName("Commission").item(0).getTextContent());
                            reportlist_sub.put("Mode", eElement.getElementsByTagName("Mode").item(0).getTextContent());

                            reportlist.add(reportlist_sub);
                        }
                    }
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
                        Adapter_report adapter_report = new Adapter_report(report_recy.getContext(), reportlist);
                        report_recy.setAdapter(adapter_report);
                    } else {
                        controller.Shackbarall(coordinate_layout, message);
                    }
                } else {
                    controller.Shackbarall(coordinate_layout, getString(R.string.oopswrongfailed));
                }
            } else {
                controller.Shackbarall(coordinate_layout, getString(R.string.internerfailed));

            }
        }
    }
}
