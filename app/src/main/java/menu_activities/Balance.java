package menu_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
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

import Api_all.Soap_api;
import Functionality_Class.SharedPrefSave;
import adapter_all.Adapter_report;
import controller_all.Controller;
import freacharge.power4pay.com.power4pay.R;

/**
 * Created by Abhay dhiman
 */

public class Balance extends AppCompatActivity {
    private ProgressDialog pDialog;
    private Context context;
    private String status, message, response;
    private Soap_api soap_api;
    private Controller controller;
    private CoordinatorLayout coordinate_layout;
    private TextView text_rid, text_name, text_address, text_state, text_mobilenumber, text_postid,
            text_sponserid, text_sponserpost, text_loginid, text_date, text_balancevariable,
            text_balanceflat, text_status, text_pin, text_firstname, text_email, text_lastupdate, text_spcode,
            text_spname, text_mpin, text_mstatus;
    private String rid, name, address, state, mobilenumber, postid,
            sponserid, sponserpost, loginid, date, balancevariable,
            balanceflat, status_st, pin, firstname, email, last_update, spcode,
            spname, mpin, mstatus, username, password;
    private SharedPrefSave sharedPrefSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_layout);

        context = Balance.this;
        controller = new Controller();
        soap_api = new Soap_api();

        setCustomActionBar();

        sharedPrefSave = new SharedPrefSave(context);
        username = sharedPrefSave.getString("username");
        password = sharedPrefSave.getString("password");

        initilize();

    }

    private void initilize() {
        coordinate_layout = (CoordinatorLayout) findViewById(R.id.coordinate_layout);

        text_rid = (TextView) findViewById(R.id.text_rid);
        text_name = (TextView) findViewById(R.id.text_name);
        text_address = (TextView) findViewById(R.id.text_address);
        text_state = (TextView) findViewById(R.id.text_state);
        text_mobilenumber = (TextView) findViewById(R.id.text_mobilenumber);
        text_postid = (TextView) findViewById(R.id.text_postid);
        text_sponserid = (TextView) findViewById(R.id.text_sponserid);
        text_sponserpost = (TextView) findViewById(R.id.text_sponserpost);
        text_loginid = (TextView) findViewById(R.id.text_loginid);
        text_date = (TextView) findViewById(R.id.text_date);
        text_balancevariable = (TextView) findViewById(R.id.text_balancevariable);
        text_balanceflat = (TextView) findViewById(R.id.text_balanceflat);
        text_status = (TextView) findViewById(R.id.text_status);
        text_pin = (TextView) findViewById(R.id.text_pin);
        text_firstname = (TextView) findViewById(R.id.text_firstname);
        text_email = (TextView) findViewById(R.id.text_email);
        text_lastupdate = (TextView) findViewById(R.id.text_lastupdate);
        text_spcode = (TextView) findViewById(R.id.text_spcode);
        text_spname = (TextView) findViewById(R.id.text_spname);
        text_mpin = (TextView) findViewById(R.id.text_mpin);
        text_mstatus = (TextView) findViewById(R.id.text_mstatus);

        text_rid.setVisibility(View.GONE);
        text_name.setVisibility(View.GONE);
        text_address.setVisibility(View.GONE);
        text_state.setVisibility(View.GONE);
        text_mobilenumber.setVisibility(View.GONE);
        text_postid.setVisibility(View.GONE);
        text_sponserid.setVisibility(View.GONE);
        text_sponserpost.setVisibility(View.GONE);
        text_loginid.setVisibility(View.GONE);
        text_date.setVisibility(View.GONE);
        text_balancevariable.setVisibility(View.GONE);
        text_balanceflat.setVisibility(View.GONE);
        text_status.setVisibility(View.GONE);
        text_pin.setVisibility(View.GONE);
        text_firstname.setVisibility(View.GONE);
        text_email.setVisibility(View.GONE);
        text_lastupdate.setVisibility(View.GONE);
        text_spcode.setVisibility(View.GONE);
        text_spname.setVisibility(View.GONE);
        text_mpin.setVisibility(View.GONE);
        text_mstatus.setVisibility(View.GONE);

        new Balance_Api().execute();

    }

    class Balance_Api extends AsyncTask<String, String, String> {

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
            SoapObject request = new SoapObject("http://tempuri.org/", "BalanceType");
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

            response = soap_api.Api_Call(request, "http://tempuri.org/BalanceType");

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
                    NodeList nodereport = elem1.getElementsByTagName("Registration");
                    Node nodereport_sub = nodereport.item(0);
                    Element elem2 = (Element) nodereport_sub;
                    NodeList nodetable = elem2.getElementsByTagName("Table");

                    Node nNode = nodetable.item(0);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        rid = eElement.getElementsByTagName("RId").item(0).getTextContent();
                        name = eElement.getElementsByTagName("Name").item(0).getTextContent();
                        address = eElement.getElementsByTagName("Address").item(0).getTextContent();
                        state = eElement.getElementsByTagName("State").item(0).getTextContent();
                        mobilenumber = eElement.getElementsByTagName("MobileNo").item(0).getTextContent();
                        postid = eElement.getElementsByTagName("PostId").item(0).getTextContent();
                        sponserid = eElement.getElementsByTagName("SponserID").item(0).getTextContent();
                        sponserpost = eElement.getElementsByTagName("SponserPost").item(0).getTextContent();
                        loginid = eElement.getElementsByTagName("LoginId").item(0).getTextContent();
                        date = eElement.getElementsByTagName("Date").item(0).getTextContent();
                        balancevariable = eElement.getElementsByTagName("BalanceVARIABLE").item(0).getTextContent();
                        balanceflat = eElement.getElementsByTagName("BalanceFlat").item(0).getTextContent();
                        status_st = eElement.getElementsByTagName("Status").item(0).getTextContent();
                        pin = eElement.getElementsByTagName("Pin").item(0).getTextContent();
                        firstname = eElement.getElementsByTagName("FirmName").item(0).getTextContent();
                        email = eElement.getElementsByTagName("Email").item(0).getTextContent();
                        last_update = eElement.getElementsByTagName("LastUpdate").item(0).getTextContent();
                        spcode = eElement.getElementsByTagName("SPCode").item(0).getTextContent();
                        spname = eElement.getElementsByTagName("SPName").item(0).getTextContent();
                        mpin = eElement.getElementsByTagName("M_Pin").item(0).getTextContent();
                        mstatus = eElement.getElementsByTagName("M_status").item(0).getTextContent();
                        Log.e("state", state);

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
                        text_rid.setVisibility(View.VISIBLE);
                        text_name.setVisibility(View.VISIBLE);
                        text_address.setVisibility(View.VISIBLE);
                        text_state.setVisibility(View.VISIBLE);
                        text_mobilenumber.setVisibility(View.VISIBLE);
                        text_postid.setVisibility(View.VISIBLE);
                        text_sponserid.setVisibility(View.VISIBLE);
                        text_sponserpost.setVisibility(View.VISIBLE);
                        text_loginid.setVisibility(View.VISIBLE);
                        text_date.setVisibility(View.VISIBLE);
                        text_balancevariable.setVisibility(View.VISIBLE);
                        text_balanceflat.setVisibility(View.VISIBLE);
                        text_status.setVisibility(View.VISIBLE);
                        text_pin.setVisibility(View.VISIBLE);
                        text_firstname.setVisibility(View.VISIBLE);
                        text_email.setVisibility(View.VISIBLE);
                        text_lastupdate.setVisibility(View.VISIBLE);
                        text_spcode.setVisibility(View.VISIBLE);
                        text_spname.setVisibility(View.VISIBLE);
                        text_mpin.setVisibility(View.VISIBLE);
                        text_mstatus.setVisibility(View.VISIBLE);

                        text_rid.setText(rid);
                        text_name.setText(name);
                        text_address.setText(address);
                        text_state.setText(state);
                        text_mobilenumber.setText(mobilenumber);
                        text_postid.setText(postid);
                        text_sponserid.setText(sponserid);
                        text_sponserpost.setText(sponserpost);
                        text_loginid.setText(loginid);
                        text_date.setText(date);
                        text_balancevariable.setText(balancevariable);
                        text_balanceflat.setText(balanceflat);
                        text_status.setText(status_st);
                        text_pin.setText(pin);
                        text_firstname.setText(firstname);
                        text_email.setText(email);
                        text_lastupdate.setText(last_update);
                        text_spcode.setText(spcode);
                        text_spname.setText(spname);
                        text_mpin.setText(mpin);
                        text_mstatus.setText(mstatus);

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

    //Custom Action Bar
    private void setCustomActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView text_coustomaction = (TextView) toolbar.findViewById(R.id.text_coustomaction);
        ImageView setting_coustomaction = (ImageView) toolbar.findViewById(R.id.setting_coustomaction);
        setting_coustomaction.setVisibility(View.GONE);
        text_coustomaction.setText(getString(R.string.report));
    }

}
