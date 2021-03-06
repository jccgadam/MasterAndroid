package com.QAPlatform;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.QAPlatform.library.DatabaseHandler;
import com.learn2crack.R;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.QAPlatform.library.UserFunctions;

//import static com.learn2crack.R.id.register_error;

public class Register extends Activity {


    /**
     *  JSON Response node names.
     **/


    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_FIRSTNAME = "firstName";
    private static String KEY_LASTNAME = "lastName";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_ERROR = "error";

    /**
     * Defining layout items.
     **/

    EditText inputFirstName;
    EditText inputLastName;
    EditText inputUsername;
    EditText inputEmail;
    EditText inputPassword;
    Button btnRegister;
    TextView registerErrorMsg;
    public String emailstr;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        /**
         * Defining all layout items
         **/
        inputFirstName = (EditText) findViewById(R.id.fname);
        inputLastName = (EditText) findViewById(R.id.lname);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pword);
        btnRegister = (Button) findViewById(R.id.register);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);


/**
 * Button which Switches back to the login screen on clicked
 **/

        Button login = (Button) findViewById(R.id.bktologin);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Login.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });

        /**
         * Register Button click event.
         * A Toast is set to alert when the fields are empty.
         * Another toast is set to alert Username must be 5 characters.
         **/

        btnRegister.setOnClickListener(new View

                .OnClickListener() {
            @Override
            public void onClick(View view) {

                if (  (!inputPassword.getText().toString().equals("")) && ( !inputFirstName.getText().toString().equals("")) && ( !inputLastName.getText().toString().equals("")) && ( !inputEmail.getText().toString().equals("")) )
                {

                    if(isValidEmail(inputEmail.getText())){
                        NetAsync(view);

                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Write a valid Email", Toast.LENGTH_SHORT).show();


                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    /**
     * Async Task to check whether internet connection is working
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Register.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){


/**
 * Gets current device state and checks for working internet connection by trying Google.
 **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }

    /*public boolean isEmailValid(String email)
    {
         String regExpn =
             "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                 +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                   +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                   +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
     CharSequence inputStr = email;
     Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
     Matcher matcher = pattern.matcher(inputStr);
     if(matcher.matches())
        return true;
     else
        return false;
}*/

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {
        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;

        String email,password,fname,lname;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            inputPassword = (EditText) findViewById(R.id.pword);
            fname = inputFirstName.getText().toString();
            lname = inputLastName.getText().toString();
            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


            UserFunctions userFunction = new UserFunctions();
            JSONObject json = null;
            try {
                json = userFunction.registerUser(email,password,fname,lname);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("in do in background"+json);
            return json;


        }
        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/

                if(1==1){
//                        if(Integer.parseInt(res) == 1){
                    pDialog.setTitle("Getting Data");
                    pDialog.setMessage("Loading Info");
                    registerErrorMsg.setText("Successfully Registered");
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    JSONObject json_user = json;


                    /**
                     * Removes all the previous data in the SQlite database
                     **/

                    UserFunctions logout = new UserFunctions();
                    logout.logoutUser(getApplicationContext());
                    try {
                        db.addUser(json_user.getString("firstName"),json_user.getString("lastName"),json_user.getString("email"),"100","0");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /**
                     * Stores registered data in SQlite Database
                     * Launch Registered screen
                     **/

                    Intent registered = new Intent(getApplicationContext(), Registered.class);

                    /**
                     * Close all views before launching Registered screen
                     **/
                    registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pDialog.dismiss();
                    startActivity(registered);


                    finish();
                }
//check user sign up status
//                        else if (Integer.parseInt(red) ==2){
//                            pDialog.dismiss();
//                            registerErrorMsg.setText("User already exists");
//                        }
//                        else if (Integer.parseInt(red) ==3){
//                            pDialog.dismiss();
//                            registerErrorMsg.setText("Invalid Email id");
//                        }


            else{
                pDialog.dismiss();

                registerErrorMsg.setText("Error occured in registration");
            }

        }}
    public void NetAsync(View view){
        new NetCheck().execute();
    }}

