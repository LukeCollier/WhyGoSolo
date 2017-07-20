/*
    Author: Luke Collier
    Description: This will parse json files from the other teams api servers
    Date Created: 28/04/16
    Last Edited by: Luke Collier
    Date Edited: 28/04/16
    Edit Note:
 */
package epsilon.whygosoloapplication.activities.json;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Project Application
 * Created by Joker
 * Created on 28/04/16
 */
public class JsonParser {
    static final int READ_TIMEOUT = 20000;
    static final int CONNECTION_TIMEOUT = 20000;
    static final String URL_CONNECTION = "http://kylet1994.homeip.net:8080/androidApi/";

    /**
     * Submits a post to the directory of the URL_CONNECTION
     * @param directory the directory of the web to submit too
     * @param parameters the parameters for the post
     * @throws IOException
     */
    static public void post(String directory, String parameters) throws IOException {
        DataOutputStream printOut;
        // Convert the url connection to a URL.
        URL url = new URL(URL_CONNECTION + directory + "?");

        // Open up the connection between the URL and the
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        urlConnection.setReadTimeout(READ_TIMEOUT);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestMethod("POST");

        // Write to the output stream
        printOut = new DataOutputStream(urlConnection.getOutputStream());
        printOut.writeBytes(parameters);

        // Close the connection and send off our lovely data
        printOut.flush();
        printOut.close();

        // Log if it were successful
        int responseCode = urlConnection.getResponseCode();
        String responseMessage = urlConnection.getResponseMessage();

        InputStream printIn;
        if (responseCode == 200) {
            printIn = urlConnection.getInputStream();
        } else {
            printIn = urlConnection.getErrorStream();
        }
        String responseMessageBody = convertStreamToString(printIn);
        Log.v("Response",responseMessage+" : "+responseCode+"\n"+responseMessageBody);
    }

    /**
     * Submits a get to the directory of the URL_CONNECTION
     * @param directory the directory of the web to submit too
     * @param parameters the parameters for the get
     * @throws IOException
     */
    static public String get(String directory, String parameters) throws IOException {
        String result;
        URL url = new URL(URL_CONNECTION +directory+ "?" + parameters);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
        urlConnection.setReadTimeout(READ_TIMEOUT);
        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        urlConnection.setRequestMethod("GET");
        InputStream response = urlConnection.getInputStream();
        result = convertStreamToString(response);

        int responseCode = urlConnection.getResponseCode();
        String responseMessage = urlConnection.getResponseMessage();

        InputStream printIn;
        if (responseCode == 200) {
            printIn = urlConnection.getInputStream();
        } else {
            printIn = urlConnection.getErrorStream();
        }
        String responseMessageBody = convertStreamToString(printIn);
        Log.v("Response",responseMessage+" : "+responseCode+"\n"+responseMessageBody);
        return result;
    }

    /**
     * Converts a stream to a string
     * @param is the input stream to convert
     * @return a string of all the characters in the stream
     */
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
