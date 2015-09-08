package com.learn2crack.library;

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

import javax.net.ssl.SSLContext;


public class UserFunctions {

    private JSONParser jsonParser;
    


    
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

    private static JSONObject getJsonObjectFromMap(Map params) throws JSONException {

        //all the passed parameters from the post request
        //iterator used to loop through all the parameters
        //passed in the post request
        Iterator iter = params.entrySet().iterator();

        //Stores JSON
        JSONObject holder = new JSONObject();

        //using the earlier example your first entry would get email
        //and the inner while would get the value which would be 'foo@bar.com'
        //{ fan: { email : 'foo@bar.com' } }

        //While there is another entry
        while (iter.hasNext())
        {
            //gets an entry in the params
            Map.Entry pairs = (Map.Entry)iter.next();

            //creates a key for Map
            String key = (String)pairs.getKey();

            //Create a new map
            Map m = (Map)pairs.getValue();

            //object for storing Json
            JSONObject data = new JSONObject();

            //gets the value
            Iterator iter2 = m.entrySet().iterator();
            while (iter2.hasNext())
            {
                Map.Entry pairs2 = (Map.Entry)iter2.next();
                data.put((String)pairs2.getKey(), (String)pairs2.getValue());
            }

            //puts email and 'foo@bar.com'  together in map
            holder.put(key, data);
        }
        return holder;
    }
    /**
     * Function to Login
     **/

    public JSONObject loginUser(String email, String password) throws JSONException {
        // Building Parameters
        JSONObject jsonObjSend = new JSONObject();
        jsonObjSend.put("email",email.toString());
        jsonObjSend.put("password",password.toString());

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
//    public JSONObject registerUser(String fname, String lname, String email,String password){
//        // Building Parameters
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("fname", fname));
//        params.add(new BasicNameValuePair("lname", lname));
//        params.add(new BasicNameValuePair("email", email));
//        params.add(new BasicNameValuePair("password", password));
//        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
//        System.out.print(json);
//        return json;
//    }

    public JSONObject registerUser(String email, String password,String fname, String lname) throws JSONException {
        // Building Parameters
        JSONObject jsonObjectSend  = new JSONObject();
        jsonObjectSend.put("email",email);
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

