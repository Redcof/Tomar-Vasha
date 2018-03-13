/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bongboom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.*;

/**
 *
 * @author soumensardar
 */
class InferBundle {

    private StringBuffer Fletcher16Key;
    private StringBuffer Formal;
    private StringBuffer Actual;

    public static final int INDEX_FL16_KEY = 0;
    public static final int INDEX_FORMALL_STR = 1; //String provided by the language
    public static final int INDEX_ACTUAL_STR = 2; //actual eqv. string from JAVA

    public InferBundle(String fletcher16Key, String formal, String actual) {
        Fletcher16Key = new StringBuffer(fletcher16Key);
        Formal = new StringBuffer(formal);
        Actual = new StringBuffer(actual);

    }

    public StringBuffer getFletcher16Key() {
        return Fletcher16Key;
    }

    public StringBuffer getActual() {
        return Actual;
    }

    public StringBuffer getFormal() {
        return Formal;
    }

}

public class BoomInferJSONLoader {

    HashMap<String, InferBundle> InferBundleList = new HashMap<>();
    public static final String KEY_INFER_LIST = "infer-list";

    public BoomInferJSONLoader(File jsonPath) throws FileNotFoundException, IOException, JSONException {

        InputStream is = new FileInputStream(jsonPath);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }
        String fileAsString = sb.toString();
        System.out.println("Contents : " + fileAsString);

        /*Read more: http://javarevisited.blogspot
        .com/2015/09/how-to-read-file-into-string-
        in-java-7.html#ixzz57T4CCRxN*/
        // Convert JSON string to JSONObject
        JSONObject JSON;
        JSONArray JSONInferList;
        JSON = new JSONObject(fileAsString);
        JSONInferList = JSON.getJSONArray(KEY_INFER_LIST);
        int ctr = 0;
        JSONArray tempObj = JSONInferList.getJSONArray(ctr);
        while (true) {
            InferBundleList.put(
                    tempObj.getString(InferBundle.INDEX_FL16_KEY),
                    new InferBundle(
                            tempObj.getString(InferBundle.INDEX_FL16_KEY),
                            tempObj.getString(InferBundle.INDEX_FORMALL_STR),
                            tempObj.getString(InferBundle.INDEX_ACTUAL_STR))
            );
            ctr++;
            try {
                tempObj = JSONInferList.getJSONArray(ctr);
            } catch (org.json.JSONException ex) {
                break;
            }

        }
    }

    public synchronized InferBundle getFletcher16Value(String fletcher16Key) {
        return InferBundleList.get(fletcher16Key);
    }

}
