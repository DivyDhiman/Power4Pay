package freacharge.power4pay.com.power4pay;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import Api_all.Data_Pass;
import Api_all.Soap_api;
import Functionality_Class.SharedPrefSave;
import adapter_all.AdapterSpinner;
import adapter_all.Adapter_report;
import controller_all.Controller;

/**
 * Created by Abhay dhiman
 */

public class Reacharge_all extends AppCompatActivity implements View.OnClickListener, Data_Pass, AdapterView.OnItemSelectedListener {

    private Context context;
    private EditText et_customer_number, et_amount,et_extra;
    private Button btn_recharge;
    private CoordinatorLayout coordinate_layout;
    private Controller controller;
    private String customer_number, amount, type, opcode, operator_type,extra;
    private ImageView img_logo;
    private int logo;
    private String name, response, status, message, username, password;
    private boolean api_hit;
    private ProgressDialog pDialog;
    private Soap_api soap_api;
    private SharedPrefSave sharedPrefSave;
    private ArrayList<HashMap<String, String>> operatorlist;
    private HashMap<String, String> operatorlist_sub;
    private Spinner spinneroperator, spinnertype;
    private List<String> type_all;
    private SoapObject request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recaharge_all);
        context = Reacharge_all.this;
        controller = new Controller();
        soap_api = new Soap_api();
        sharedPrefSave = new SharedPrefSave(context);

        username = sharedPrefSave.getString("username");
        password = sharedPrefSave.getString("password");

        name = getIntent().getExtras().getString("name");
        logo = getIntent().getExtras().getInt("image");
        type = getIntent().getExtras().getString("type");

        setCustomActionBar();

        initilize();
    }

    private void initilize() {
        coordinate_layout = (CoordinatorLayout) findViewById(R.id.coordinate_layout);
        img_logo = (ImageView) findViewById(R.id.img_logo);
        et_customer_number = (EditText) findViewById(R.id.et_customer_number);
        et_amount = (EditText) findViewById(R.id.et_amount);
        et_extra = (EditText) findViewById(R.id.et_extra);
        btn_recharge = (Button) findViewById(R.id.btn_recharge);
        spinneroperator = (Spinner) findViewById(R.id.spinneroperator);
        spinnertype = (Spinner) findViewById(R.id.spinnertype);
        spinnertype.setOnItemSelectedListener(this);
        btn_recharge.setOnClickListener(this);
        img_logo.setImageResource(logo);
        api_hit = false;
        et_extra.setVisibility(View.GONE);

        if (type.equals(getString(R.string.dth_type))) {
            et_customer_number.setHint(getString(R.string.SubscriberNumber));
        }else if (type.equals(getString(R.string.insurance_type))) {
            et_extra.setVisibility(View.VISIBLE);
            et_extra.setHint(getString(R.string.DOB));
            et_customer_number.setHint(getString(R.string.PolicyApplicationNumber));
        }else if (type.equals(getString(R.string.gas_type))) {
            et_customer_number.setHint(getString(R.string.ConsumerNumber));
        }else if (type.equals(getString(R.string.electricity_type))) {
            et_extra.setVisibility(View.VISIBLE);
            et_extra.setHint(getString(R.string.CycleNumber));
            et_customer_number.setHint(getString(R.string.ConsumerNumber));
        }else if (type.equals(getString(R.string.datacard_type))) {
            et_customer_number.setHint(getString(R.string.DataCardNumber));
        }else if (type.equals(getString(R.string.postpaid_type))) {
            et_customer_number.setHint(getString(R.string.Number));
        }else if (type.equals(getString(R.string.prepaid_type))) {
            et_customer_number.setHint(getString(R.string.MobileNumber));
        }else {
            et_customer_number.setHint(getString(R.string.Number));
        }

        new GetOperatorList_Api().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharge:
                if (controller.Check_all_empty(et_customer_number)) {
                    et_customer_number.setError(getString(R.string.entervalue));
                } else if (controller.Check_all_empty(et_amount)) {
                    et_amount.setError(getString(R.string.enteramount));
                } else {
                    if (controller.InternetCheck(context)) {
                        if(et_extra.getVisibility() == View.GONE){
                            customer_number = et_customer_number.getText().toString();
                            amount = et_amount.getText().toString();
                            api_hit = true;

                            new GetOperatorList_Api().execute();

                        }else {
                            if (controller.Check_all_empty(et_extra)) {
                                et_amount.setError(getString(R.string.entervalue));
                            }else {
                                customer_number = et_customer_number.getText().toString();
                                amount = et_amount.getText().toString();
                                extra = et_extra.getText().toString();
                                api_hit = true;

                                new GetOperatorList_Api().execute();
                            }

                        }
                        //hit api
                    } else {
                        controller.Shackbarall(coordinate_layout, getString(R.string.messagenointernet));
                    }

                    break;
                }
        }
    }

    @Override
    public void data_send(String opcode) {
        this.opcode = opcode;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        operator_type = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class GetOperatorList_Api extends AsyncTask<String, String, String> {

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

            if (!api_hit) {
                operatorlist = new ArrayList<>();

                request = new SoapObject("http://tempuri.org/", "OperatorList");
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
                pi = new PropertyInfo();
                pi.setName("OperatorType");
                pi.setValue(type);
                pi.setType(String.class);
                request.addProperty(pi);

                response = soap_api.Api_Call(request, "http://tempuri.org/OperatorList");

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
                        NodeList nodereport = elem1.getElementsByTagName("Incentive");
                        Node nodereport_sub = nodereport.item(0);
                        Element elem2 = (Element) nodereport_sub;
                        NodeList nodetable = elem2.getElementsByTagName("Table");

                        for (int temp = 0; temp < nodetable.getLength(); temp++) {
                            operatorlist_sub = new HashMap<>();
                            Node nNode = nodetable.item(temp);

                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element eElement = (Element) nNode;
                                operatorlist_sub.put("Operator", eElement.getElementsByTagName("Operator").item(0).getTextContent());
                                operatorlist_sub.put("OPCODE", eElement.getElementsByTagName("OPCODE").item(0).getTextContent());
                                operatorlist.add(operatorlist_sub);
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

            } else {

                if (type.equals(getString(R.string.dth_type))) {
                    request = new SoapObject("http://tempuri.org/", "RechargeDTH");
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
                    pi = new PropertyInfo();
                    pi.setName("OPCode");
                    pi.setValue(opcode);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("Amount");
                    pi.setValue(amount);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("SubscriberNumber");
                    pi.setValue(customer_number);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("BalType");
                    pi.setValue(operator_type);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    response = soap_api.Api_Call(request, "http://tempuri.org/RechargeDTH");

                }else if (type.equals(getString(R.string.insurance_type))) {
                    request = new SoapObject("http://tempuri.org/", "Insurance");
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
                    pi = new PropertyInfo();
                    pi.setName("OPCode");
                    pi.setValue(opcode);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("Amount");
                    pi.setValue(amount);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("PolicyApplicationNumber");
                    pi.setValue(customer_number);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("BalType");
                    pi.setValue(operator_type);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("DOB");
                    pi.setValue(extra);
                    pi.setType(String.class);
                    request.addProperty(pi);

                    response = soap_api.Api_Call(request, "http://tempuri.org/Insurance");

                }else if (type.equals(getString(R.string.gas_type))) {
                    request = new SoapObject("http://tempuri.org/", "Gas");
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
                    pi = new PropertyInfo();
                    pi.setName("OPCode");
                    pi.setValue(opcode);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("Amount");
                    pi.setValue(amount);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("ConsumerNumber");
                    pi.setValue(customer_number);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("BalType");
                    pi.setValue(operator_type);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    response = soap_api.Api_Call(request, "http://tempuri.org/Gas");
                }else if (type.equals(getString(R.string.electricity_type))) {
                    request = new SoapObject("http://tempuri.org/", "Electricity");
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
                    pi = new PropertyInfo();
                    pi.setName("OPCode");
                    pi.setValue(opcode);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("Amount");
                    pi.setValue(amount);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("ConsumerNumber");
                    pi.setValue(customer_number);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("BalType");
                    pi.setValue(operator_type);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("CycleNumber");
                    pi.setValue(extra);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    response = soap_api.Api_Call(request, "http://tempuri.org/Electricity");
                }else if (type.equals(getString(R.string.datacard_type))) {
                    request = new SoapObject("http://tempuri.org/", "RechargeDatacard");
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
                    pi = new PropertyInfo();
                    pi.setName("OPCode");
                    pi.setValue(opcode);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("Amount");
                    pi.setValue(amount);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("DataCardNumber");
                    pi.setValue(customer_number);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("BalType");
                    pi.setValue(operator_type);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    response = soap_api.Api_Call(request, "http://tempuri.org/RechargeDatacard");
                }else if (type.equals(getString(R.string.postpaid_type))) {
                    request = new SoapObject("http://tempuri.org/", "RechargePostpaid");
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
                    pi = new PropertyInfo();
                    pi.setName("OPCode");
                    pi.setValue(opcode);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("Amount");
                    pi.setValue(amount);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("Number");
                    pi.setValue(customer_number);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("BalType");
                    pi.setValue(operator_type);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    response = soap_api.Api_Call(request, "http://tempuri.org/RechargePostpaid");
                }else if (type.equals(getString(R.string.prepaid_type))) {
                    request = new SoapObject("http://tempuri.org/", "RechargePrepaid");
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
                    pi = new PropertyInfo();
                    pi.setName("OPCode");
                    pi.setValue(opcode);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("Amount");
                    pi.setValue(amount);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("MobileNumber");
                    pi.setValue(customer_number);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    pi = new PropertyInfo();
                    pi.setName("BalType");
                    pi.setValue(operator_type);
                    pi.setType(String.class);
                    request.addProperty(pi);
                    response = soap_api.Api_Call(request, "http://tempuri.org/RechargePrepaid");
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (!api_hit) {
                if (response != null) {
                    if (!response.equals("Error")) {
                        if (status.equals("1")) {
                            AdapterSpinner adapterSpinner = new AdapterSpinner(context, R.layout.custom_spinner_item, operatorlist);
                            spinneroperator.setAdapter(adapterSpinner);
                            spinneroperator.setPrompt(getString(R.string.spinnerlable));
                            if (type.equals(getString(R.string.connection_type))) {
                                type_all = new ArrayList<String>();
                                type_all.add(getString(R.string.flat));
                            }else if (type.equals(getString(R.string.dth_type))) {
                                type_all = new ArrayList<String>();
                                type_all.add(getString(R.string.variable));
                                type_all.add(getString(R.string.flat));
                            }else if (type.equals(getString(R.string.prepaid_type))) {
                                type_all = new ArrayList<String>();
                                type_all.add(getString(R.string.variable));
                                type_all.add(getString(R.string.flat));
                            }else if (type.equals(getString(R.string.datacard_type))) {
                                type_all = new ArrayList<String>();
                                type_all.add(getString(R.string.variable));
                                type_all.add(getString(R.string.flat));
                            } else {
                                type_all = new ArrayList<String>();
                                type_all.add(getString(R.string.variable));
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                                    context,R.layout.custom_textview_to_spinner,type_all );

                            dataAdapter.setDropDownViewResource(R.layout.custom_textview_to_spinner);
                            // attaching data adapter to spinner
                            spinnertype.setAdapter(dataAdapter);

                        } else {
                            controller.Shackbarall(coordinate_layout, message);
                        }
                    } else {
                        controller.Shackbarall(coordinate_layout, getString(R.string.oopswrongfailed));
                    }
                } else {
                    controller.Shackbarall(coordinate_layout, getString(R.string.internerfailed));

                }

            }else {
                controller.Shackbarall(coordinate_layout, response);
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
        text_coustomaction.setText(name);
    }
}
