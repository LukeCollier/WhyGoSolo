/*
    Author: Luke Collier
    Description: A simple method for handling the Json parameters and parsing them to a HTTP request
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */
package epsilon.whygosoloapplication.activities.json;

import java.util.ArrayList;

/**
 * Created by Joker on 02/05/16.
 */
public class JsonParams {
    private final ArrayList<String[]> params = new ArrayList<>();

    public JsonParams() {}

    /**
     * Adds a param, name pair
     * @param name the name
     * @param data the data
     */
    public void addParam(String name, String data) {
        params.add(new String[] {name, data});
    }

    /**
     * Builds a HTTP request from the array list of params
     * @return a HTTP request
     */
    public String parse() {
        String out = "";
        for (int i = 0; i < params.size(); i++) {
            out+=params.get(i)[0]+"="+params.get(i)[1]+"&";
        }
        if (out.endsWith("&")) out = out.substring(0,out.length() - 1);
        return out;
    }

}
