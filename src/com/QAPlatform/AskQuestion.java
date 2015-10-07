package com.QAPlatform;

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
import com.QAPlatform.library.JSONParser;
import com.QAPlatform.library.QuestionFunctions;
import com.learn2crack.R;
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

import com.QAPlatform.library.DatabaseHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class AskQuestion extends Activity {

    Button postQuestion;
    EditText inputTitle;
    EditText inputContent;
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
    private JSONParser jsonParser = new JSONParser();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.askquestion);

//        inputEmail = (EditText) findViewById(R.id.email);
//        inputPassword = (EditText) findViewById(R.id.pword);
//        Btnregister = (Button) findViewById(R.id.registerbtn);
          postQuestion = (Button) findViewById(R.id.postQuestion);
//        passreset = (Button) findViewById(R.id.passres);
//        loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);
          inputTitle = (EditText)findViewById(R.id.questionTitle);
          inputContent  = (EditText)findViewById(R.id.questionContent);
//        passreset.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent myIntent = new Intent(view.getContext(), AskQuestion.class);
//                startActivityForResult(myIntent, 0);
//                finish();
//            }
//        });


/**
 * Login button click event
 * A Toast is set to alert when the Email and Password field is empty
 **/
        postQuestion.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if ((!inputContent.getText().toString().equals("")) && (!inputTitle.getText().toString().equals(""))) {
                    NetAsync(view);
                } else if ((inputTitle.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Question Title cannot be empty", Toast.LENGTH_SHORT).show();
                } else if ((inputContent.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Question Content cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please fill in Question Title and Content", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    /**
     * Async Task to check whether internet connection is working.
     **/

    private class NetCheck extends AsyncTask<String, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(AskQuestion.this);
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
        protected Boolean doInBackground(String... args) {


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
        protected void onPostExecute(Boolean th) {

            if (th == true) {
                nDialog.dismiss();
                new ProcessPostQuestion().execute();
            } else {
                nDialog.dismiss();
                loginErrorMsg.setText("Error in Network Connection");
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class ProcessPostQuestion extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String questionTitle, questionContent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            inputTitle = (EditText)findViewById(R.id.questionTitle);
//            inputContent = (EditText)findViewById(R.id.questionContent);
//            questionTitle = inputTitle.getText().toString();
//            questionContent =  inputContent.getText().toString();
            pDialog = new ProgressDialog(AskQuestion.this);
            pDialog.setMessage("posting question ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            QuestionFunctions questionFunction = new QuestionFunctions();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            /**
             * Hashmap to load data from the Sqlite database
             **/
            questionTitle = inputTitle.getText().toString();
            questionContent = inputContent.getText().toString();

            HashMap<String,String> user = new HashMap<String, String>();
            user = db.getUserDetails();
            String uid = user.get("uId");
//            System.out.println("uid"+uid);
            JSONObject jsonObject = new JSONObject();
            try {
                 jsonObject = questionFunction.postQuestion(questionTitle,questionContent,21);
            } catch (JSONException e) {
                e.printStackTrace();
            }
             System.out.println("callback post"+jsonObject);
            return jsonObject;
        }


        @Override
        protected void onPostExecute(JSONObject json) {


            try {
                 json.getString("uId");
//                if (json.getString("uId") != null) {
//
//                    pDialog.setMessage("Loading User Space");
//                    pDialog.setTitle("Getting Data");
////                    User loginUser = new User();
////                    loginUser.setEmail(json.getString("email"));
////                    loginUser.setFirstName(json.getString("firstName"));
////                    loginUser.setLastName(json.get("lastName").toString());
////                    UserFunctions logout = new UserFunctions();
////                    logout.logoutUser(getApplicationContext());
//                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//                    try {
//                        db.resetTables();
//                        db.addUser(json.getString("firstName"), json.getString("lastName"), json.getString("email"), json.getString("credit"),json.getString("exp"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    Intent upanel = new Intent(getApplicationContext(), Main.class);
                    upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pDialog.dismiss();
                    startActivity(upanel);
                    /**
                     * Close Login Screen
                     **/
                    finish();
//                }
            } catch (JSONException e) {
                e.printStackTrace();

//                if (1 == 1) {
//                    pDialog.setMessage("Loading User Space");
//                    pDialog.setTitle("Getting Data");
////                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
////                        JSONObject json_user = json.getJSONObject("user");
//                    /**
//                     * Clear all previous data in SQlite database.
//                     **/
//                    UserFunctions logout = new UserFunctions();
//                    logout.logoutUser(getApplicationContext());
////                        db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
//                    /**
//                     *If JSON array details are stored in SQlite it launches the User Panel.
//                     **/
//                    Intent upanel = new Intent(getApplicationContext(), Main.class);
//                    upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    pDialog.dismiss();
//                    startActivity(upanel);
//                    /**
//                     * Close Login Screen
//                     **/
//                    finish();
//                } else {
//
//                    pDialog.dismiss();
//                    loginErrorMsg.setText("Incorrect username/password");
//
//                }
//
//
//            }
            }
        }

    }
    public void NetAsync(View view){

        new NetCheck().execute();
    }


}

