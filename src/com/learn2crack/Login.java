package com.learn2crack;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import com.learn2crack.library.JSONParser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.learn2crack.library.DatabaseHandler;
import com.learn2crack.library.UserFunctions;

import java.io.UnsupportedEncodingException;
import java.util.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import User.User;
public class Login extends Activity {

    Button btnLogin;
    Button Btnregister;
    Button passreset;
    EditText inputEmail;
    EditText inputPassword;
    private TextView loginErrorMsg;
    /**
     * Called when the activity is first created.
     */
    private static String KEY_SUCCESS = "login successfully";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "uname";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    private static String loginURL = "http://localhost:9000/signin";
    private JSONParser jsonParser =new JSONParser();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pword);
        Btnregister = (Button) findViewById(R.id.registerbtn);
        btnLogin = (Button) findViewById(R.id.login);
        passreset = (Button)findViewById(R.id.passres);
        loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);

        passreset.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        Intent myIntent = new Intent(view.getContext(), PasswordReset.class);
        startActivityForResult(myIntent, 0);
        finish();
        }});


        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Register.class);
                startActivityForResult(myIntent, 0);
                finish();
             }});

/**
 * Login button click event
 * A Toast is set to alert when the Email and Password field is empty
 **/
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (  ( !inputEmail.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) )
                {
                    NetAsync(view);
                }
                else if ( ( !inputEmail.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                }
                else if ( ( !inputPassword.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


/**
 * Async Task to check whether internet connection is working.
 **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Login.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
        **/
        @Override
        protected Boolean doInBackground(String... args){



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
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
                loginErrorMsg.setText("Error in Network Connection");
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String email, password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            inputEmail = (EditText) findViewById(R.id.email);
            inputPassword = (EditText) findViewById(R.id.pword);
            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String...params) {

            UserFunctions userFunction = new UserFunctions();

            JSONObject json = null;
            try {
                json = userFunction.loginUser(email, password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return json;
        }


        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if(json.getString("uId")!=null){

                    pDialog.setMessage("Loading User Space");
                    pDialog.setTitle("Getting Data");
                    User loginUser =new User();
                    loginUser.setEmail(json.getString("email"));
                    loginUser.setFirstName(json.getString("firstName"));
                    loginUser.setLastName(json.get("lastName").toString());
                    UserFunctions logout = new UserFunctions();
                    logout.logoutUser(getApplicationContext());

                    Intent upanel = new Intent(getApplicationContext(), Main.class);
                    upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pDialog.dismiss();
                    startActivity(upanel);
                    /**
                     * Close Login Screen
                     **/
                    finish();
                }else{

                    pDialog.dismiss();
                    loginErrorMsg.setText("Incorrect username/password");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    public void NetAsync(View view){

        new NetCheck().execute();
    }





}
//    private static JSONObject getJsonObjectFromMap(Map params) throws JSONException {
//
//        //all the passed parameters from the post request
//        //iterator used to loop through all the parameters
//        //passed in the post request
//        Iterator iter = params.entrySet().iterator();
//
//        //Stores JSON
//        JSONObject holder = new JSONObject();
//
//        //using the earlier example your first entry would get email
//        //and the inner while would get the value which would be 'foo@bar.com'
//        //{ fan: { email : 'foo@bar.com' } }
//
//        //While there is another entry
//        while (iter.hasNext())
//        {
//            //gets an entry in the params
//            Map.Entry pairs = (Map.Entry)iter.next();
//
//            //creates a key for Map
//            String key = (String)pairs.getKey();
//
//            //Create a new map
//            Map m = (Map)pairs.getValue();
//
//            //object for storing Json
//            JSONObject data = new JSONObject();
//
//            //gets the value
//            Iterator iter2 = m.entrySet().iterator();
//            while (iter2.hasNext())
//            {
//                Map.Entry pairs2 = (Map.Entry)iter2.next();
//                data.put((String)pairs2.getKey(), (String)pairs2.getValue());
//            }
//
//            //puts email and 'foo@bar.com'  together in map
//            holder.put(key, data);
//        }
//        return holder;
//    }
//}
