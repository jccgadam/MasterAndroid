package com.QAPlatform.library;

import android.database.sqlite.SQLiteDatabase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by root on 10/6/15.
 */
public class QuestionFunctions {
    private JSONParser jsonParser;
    private static String questionURL = "http://10.0.2.2:9000/questions";
    public QuestionFunctions(){
        jsonParser = new JSONParser();
    }

    public JSONObject postQuestion(String title, String content,int uid) throws JSONException {
        // Building Parameters
        JSONObject jsonObjSend = new JSONObject();
        JSONArray jsonArray =new JSONArray();
        jsonArray.put(1);
        System.out.println("title"+title+"content"+content);
        jsonObjSend.put("title", title);
        jsonObjSend.put("content", content);
        jsonObjSend.put("uId",uid);
        jsonObjSend.put("cIds",jsonArray);
        System.out.println(jsonObjSend.toString());
        JSONObject json=jsonParser.getJSONFromUrl(questionURL, jsonObjSend);
//        System.out.println("jsonObjSend"+jsonObjSend);
//        System.out.println("from userFunction.question"+json);
        return json;
    }
}
