package com.QAPlatform.library;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import java.util.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;


public class UserFunctions {

    private JSONParser jsonParser;
    

//  /*//URL of the PHP API WORKING LOCAL ASUS WAMPSERVER
//    private static String loginURL = "http://192.168.4.110/learn2crack_login_api/";
//    private static String registerURL = "http://192.168.4.110/learn2crack_login_api/";
//    private static String forpassURL = "http://192.168.4.110/learn2crack_login_api/";
//    private static String chgpassURL = "http://192.168.4.110/learn2crack_login_api/";*/
//
//  /*//URL of the PHP API WORKING PERSONAL PC
//    private static String loginURL = "http://192.168.0.18:82/learn2crack_login_api/";
//    private static String registerURL = "http://192.168.0.18:82/learn2crack_login_api/";
//    private static String forpassURL = "http://192.168.0.18:82/learn2crack_login_api/";
//    private static String chgpassURL = "http://192.168.0.18:82/learn2crack_login_api/";*/


    
    //URL of the PHP API WORKING!!!!!!
    private static String loginURL = "http://10.0.2.2:9000/signin";
    private static String registerURL = "http://10.0.2.2:9000/signup";
    private static String forpassURL = "http://unps.comli.com/learn2crack_login_api/index.php";
    private static String chgpassURL = "http://unps.comli.com/learn2crack_login_api/index.php";
    
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";


    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
    /**
     * Function to Login
     **/

    public JSONObject loginUser(String email, String password) throws JSONException {
        // Building Parameters
        JSONObject jsonObjSend = new JSONObject();
        jsonObjSend.put("email", email.toString());
        jsonObjSend.put("password", password.toString());

        JSONObject json=jsonParser.getJSONFromUrl(loginURL, jsonObjSend);

        //System.out.println("from userFunction.login"+json);
        return json;
    }

    /**
     * Function to change password
     **/

    public JSONObject chgPass(String newpas, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));

        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL,params);

        return json;
    }





    /**
     * Function to reset the password
     **/

    public JSONObject forPass(String forgotpassword){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(forpassURL,params);
        return json;
    }






     /**
      * Function to  Register
      **/



     public JSONObject registerUser(String email, String password, String fname,String lname) throws JSONException {
         // Building Parameters
         JSONObject jsonObjectSend  = new JSONObject();
         jsonObjectSend.put("email", email);
         jsonObjectSend.put("password",password);
         jsonObjectSend.put("firstName",fname);
         jsonObjectSend.put("lastName",lname);
         JSONObject json = jsonParser.getJSONFromUrl(registerURL,jsonObjectSend);
         return json;

     }


    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}

