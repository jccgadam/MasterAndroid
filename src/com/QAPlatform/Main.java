package com.QAPlatform;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.QAPlatform.library.DatabaseHandler;
import com.learn2crack.R;

import java.util.HashMap;

public class Main extends Activity {
    Button askQue;
    Button ansQue;




    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        askQue = (Button) findViewById(R.id.askQue);
        ansQue = (Button) findViewById(R.id.ansQue);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        /**
         * Hashmap to load data from the Sqlite database
         **/
         HashMap<String,String> user = new HashMap<String, String>();
         user = db.getUserDetails();

        /**
         * Change Password Activity Started
         **/
        askQue.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

                Intent AskQuestion = new Intent(getApplicationContext(), AskQuestion.class);

                startActivity(AskQuestion);
            }

        });

       /**
        *Logout from the User Panel which clears the data in Sqlite database
        **/
//        btnLogout.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//
//                UserFunctions logout = new UserFunctions();
//                logout.logoutUser(getApplicationContext());
//                Intent login = new Intent(getApplicationContext(), Login.class);
//                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(login);
//                finish();
//            }
//        });
/**
 * Sets user first name and last name in text view.
 **/
        final TextView login = (TextView) findViewById(R.id.textwelcome);

        final TextView lname = (TextView) findViewById(R.id.lname);
        lname.setText(user.get("fname"));
        System.out.println(lname.getText());

    }}