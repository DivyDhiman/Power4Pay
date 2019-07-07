package freacharge.power4pay.com.power4pay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import adapter_all.AdptnearContent;
import controller_all.Controller;
import menu_activities.Balance;
import menu_activities.Report;

/**
 * Created by Abhay dhiman
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView dashboardlist_recy;
    private Context context;
    private CoordinatorLayout coordinate_layout;
    private AdptnearContent adptercontent;
    private ArrayList<HashMap<String, Object>> dashboardlist;
    private HashMap<String, Object> dashboardlist_sub;
    private ImageView setting_coustomaction;
    private Intent intent;
    private EditText change_password, change_confirmpassword;
    private Button dialog_cancel, dialog_change;
    private Dialog dialog;
    private ProgressDialog pDialog;
    private String response, status, message, username, oldpassword, newpassword;
    private Controller controller;
    private SharedPrefSave sharedprefsave;
    private Soap_api soap_api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        controller = new Controller();
        sharedprefsave = new SharedPrefSave(context);
        soap_api = new Soap_api();
        setCustomActionBar();
        initlilzer();
    }

    private void initlilzer() {
        coordinate_layout = (CoordinatorLayout) findViewById(R.id.coordinate_layout);
        dashboardlist_recy = (RecyclerView) findViewById(R.id.dashboardlist_recy);
        dashboardlist_recy.setLayoutManager(new GridLayoutManager(dashboardlist_recy.getContext(), 2));
        contentlist();
    }

    private void contentlist() {
        dashboardlist = new ArrayList<>();

        dashboardlist_sub = new HashMap<>();
        dashboardlist_sub.put("name", getString(R.string.dthrecharge));
        dashboardlist_sub.put("image", R.drawable.dth_recharge);
        dashboardlist_sub.put("type",getString(R.string.dth_type));
        dashboardlist.add(dashboardlist_sub);

        dashboardlist_sub = new HashMap<>();
        dashboardlist_sub.put("name", getString(R.string.datacardrecharge));
        dashboardlist_sub.put("image", R.drawable.datacard_recharge);
        dashboardlist_sub.put("type",getString(R.string.datacard_type));
        dashboardlist.add(dashboardlist_sub);

        dashboardlist_sub = new HashMap<>();
        dashboardlist_sub.put("name", getString(R.string.postpaidrecharge));
        dashboardlist_sub.put("image", R.drawable.prepost_recharge);
        dashboardlist_sub.put("type",getString(R.string.postpaid_type));
        dashboardlist.add(dashboardlist_sub);

        dashboardlist_sub = new HashMap<>();
        dashboardlist_sub.put("name", getString(R.string.prepaidrecharge));
        dashboardlist_sub.put("image", R.drawable.prepost_recharge);
        dashboardlist_sub.put("type",getString(R.string.prepaid_type));
        dashboardlist.add(dashboardlist_sub);

        dashboardlist_sub = new HashMap<>();
        dashboardlist_sub.put("name", getString(R.string.insurance));
        dashboardlist_sub.put("image", R.drawable.insurence_recharge);
        dashboardlist_sub.put("type",getString(R.string.insurance_type));
        dashboardlist.add(dashboardlist_sub);

        dashboardlist_sub = new HashMap<>();
        dashboardlist_sub.put("name", getString(R.string.electricity));
        dashboardlist_sub.put("image", R.drawable.electricity_recharge);
        dashboardlist_sub.put("type",getString(R.string.electricity_type));
        dashboardlist.add(dashboardlist_sub);

        dashboardlist_sub = new HashMap<>();
        dashboardlist_sub.put("name", getString(R.string.gas));
        dashboardlist_sub.put("image", R.drawable.gas_recharge);
        dashboardlist_sub.put("type",getString(R.string.gas_type));
        dashboardlist.add(dashboardlist_sub);

        dashboardlist_sub = new HashMap<>();
        dashboardlist_sub.put("name", getString(R.string.connection));
        dashboardlist_sub.put("image", R.drawable.dish);
        dashboardlist_sub.put("type",getString(R.string.connection_type));
        dashboardlist.add(dashboardlist_sub);

        dashboardlist_sub = new HashMap<>();
        dashboardlist_sub.put("name", getString(R.string.landline));
        dashboardlist_sub.put("image", R.drawable.phone);
        dashboardlist_sub.put("type",getString(R.string.landline_type));
        dashboardlist.add(dashboardlist_sub);

        adptercontent = new AdptnearContent(dashboardlist_recy.getContext(), dashboardlist);
        dashboardlist_recy.setAdapter(adptercontent);
    }

    //Custom Action Bar
    private void setCustomActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView text_coustomaction = (TextView) toolbar.findViewById(R.id.text_coustomaction);
        setting_coustomaction = (ImageView) toolbar.findViewById(R.id.setting_coustomaction);
        setting_coustomaction.setOnClickListener(this);
        text_coustomaction.setText(getString(R.string.app_name));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_coustomaction:
                PopupMenu popupMenu = new PopupMenu(context, v) {
                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.report:
                                if (controller.InternetCheck(context)) {
                                    intent = new Intent(context, Report.class);
                                    startActivity(intent);
                                } else {
                                    controller.Shackbarall(coordinate_layout, getString(R.string.messagenointernet));
                                }

                                return true;

                            case R.id.balance:
                                if (controller.InternetCheck(context)) {
                                    intent = new Intent(context, Balance.class);
                                    startActivity(intent);
                                } else {
                                    controller.Shackbarall(coordinate_layout, getString(R.string.messagenointernet));
                                }

                                return true;

                            case R.id.changepassword:
                                if (controller.InternetCheck(context)) {
                                    change_password();
                                } else {
                                    controller.Shackbarall(coordinate_layout, getString(R.string.messagenointernet));
                                }

                                return true;

                            case R.id.logout:

                                logoutmethod();
                                return true;

                            default:
                                return super.onMenuItemSelected(menu, item);

                        }
                    }
                };

                popupMenu.inflate(R.menu.menu);
                popupMenu.show();

                break;

            case R.id.dialog_change:

                if (controller.Check_all_empty(change_password)) {
                    change_password.setError(getString(R.string.enteroldpassword));
                } else if (controller.Check_all_empty(change_confirmpassword)) {
                    change_confirmpassword.setError(getString(R.string.enternewpassword));
                } else {
                    if (!controller.InternetCheck(context)) {
                        controller.Shackbarall(coordinate_layout, getString(R.string.messagenointernet));
                    } else {
                        oldpassword = change_password.getText().toString();
                        newpassword = change_confirmpassword.getText().toString();
                        username = sharedprefsave.getString("username");

                        new ChangePassword_Api().execute();

                    }
                }

                break;

            case R.id.dialog_cancel:
                dialog.dismiss();
                break;

        }
    }

    private void logoutmethod() {
        sharedprefsave.removesharedpreferences("username");
        sharedprefsave.removesharedpreferences("password");
        sharedprefsave.removesharedpreferences("status");

        intent = new Intent(context, AfterSplash.class);
        startActivity(intent);
        controller.Animationbackward(context);
        finish();

    }

    private void change_password() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialog_pass);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        change_password = (EditText) dialog.findViewById(R.id.change_password);
        change_confirmpassword = (EditText) dialog.findViewById(R.id.change_confirmpassword);
        dialog_cancel = (Button) dialog.findViewById(R.id.dialog_cancel);
        dialog_change = (Button) dialog.findViewById(R.id.dialog_change);
        dialog_cancel.setOnClickListener(this);
        dialog_change.setOnClickListener(this);

        dialog.show();

    }

    class ChangePassword_Api extends AsyncTask<String, String, String> {

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
            SoapObject request = new SoapObject("http://tempuri.org/", "ChangePassword");
            PropertyInfo pi = new PropertyInfo();
            pi.setName("UserName");
            pi.setValue(username);
            pi.setType(String.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("OldPassword");
            pi.setValue(oldpassword);
            pi.setType(String.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("NewPassword");
            pi.setValue(newpassword);
            pi.setType(String.class);
            request.addProperty(pi);

            response = soap_api.Api_Call(request, "http://tempuri.org/ChangePassword");

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
                        dialog.dismiss();
                        controller.Shackbarall(coordinate_layout, message);
                        logoutmethod();

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
