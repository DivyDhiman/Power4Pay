package freacharge.power4pay.com.power4pay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.w3c.dom.Document;
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
import controller_all.Controller;

/**
 * Created by Abhay dhiman
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText login_emailid, login_password;
    private Button login_submit_btn;
    private Controller controller;
    private Context context;
    private CoordinatorLayout coordinatelayout;
    private String email;
    private String password;
    private Intent intent;
    private AfterSplash afterSplash;
    private Soap_api soap_api;
    private ProgressDialog pDialog;
    private String status, response, message;
    private SharedPrefSave sharedPrefSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        context = LoginActivity.this;
        afterSplash = AfterSplash.afterSplash;
        controller = new Controller();
        soap_api = new Soap_api();
        sharedPrefSave = new SharedPrefSave(context);
        initilization();
    }

    private void initilization() {
        coordinatelayout = (CoordinatorLayout) findViewById(R.id.coordinatelayout);
        login_emailid = (EditText) findViewById(R.id.login_emailid);
        login_password = (EditText) findViewById(R.id.login_password);
        login_submit_btn = (Button) findViewById(R.id.login_submit_btn);

        login_submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_submit_btn:

                if (controller.Check_all_empty(login_emailid)) {
                    login_emailid.setError(getString(R.string.emailenter));

                } else if (controller.Check_all_empty(login_password)) {
                    login_password.setError(getString(R.string.passwordenter));
                } else {
                    if (controller.InternetCheck(context)) {

                        email = login_emailid.getText().toString();
                        password = login_password.getText().toString();

                        //hit api
                        new Login_Api().execute();
                    } else {
                        controller.Shackbarall(coordinatelayout, getString(R.string.messagenointernet));
                    }
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        controller.Animationbackward(context);
        finish();
    }

    class Login_Api extends AsyncTask<String, String, String> {

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
            SoapObject request = new SoapObject("http://tempuri.org/", "LoginDetails");
            PropertyInfo pi = new PropertyInfo();
            pi.setName("UserName");
            pi.setValue(email);
            pi.setType(String.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("Password");
            pi.setValue(password);
            pi.setType(String.class);
            request.addProperty(pi);

            response = soap_api.Api_Call(request, "http://tempuri.org/LoginDetails");
            if (response != null) {
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

                    if (status.equals("0")) {
                        Node messagenode = node.getChildNodes().item(0);
                        message = messagenode.getTextContent();
                    } else {
                        sharedPrefSave.setString("username", email);
                        sharedPrefSave.setString("password", password);
                        sharedPrefSave.setBoolean("status", true);
                    }

                } catch (ParserConfigurationException e) {
                    response = "Error";
                } catch (SAXException e2) {
                    response = "Error";
                } catch (IOException e3) {
                    response = "Error";
                }
            }

            return null;

        }


        @Override
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();

            if (response != null) {
                if (!response.equals("Error")) {
                    if (status.equals("1")) {
                        intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        controller.Animationforward(context);
                        afterSplash.finish();
                        finish();

                    } else {
                        controller.Shackbarall(coordinatelayout, message);

                    }
                } else {
                    controller.Shackbarall(coordinatelayout, getString(R.string.oopswrongfailed));
                }
            } else {
                controller.Shackbarall(coordinatelayout, getString(R.string.internerfailed));

            }

        }
    }
}
