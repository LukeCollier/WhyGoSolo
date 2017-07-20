/*
    Author: Luke Collier
    Description: Some generic methods for validation
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */
package epsilon.whygosoloapplication.activities.utility;

import android.util.Log;
import android.widget.EditText;

/**
 * Project Application
 * Created by Joker
 * Created on 03/05/16
 */
public class Validation {
    final static int USERNAME_MIN_LENGTH = 4;
    final static int PASSWORD_MIN_LENGTH = 4;
    final static int EMAIL_MIN_LENGTH = 3;
    final static int DESCRIPTION_MIN_LENGTH=0;
    final static int NAME_MIN_LENGTH =1;

    /**
     * Is the email an email
     * @param email the string to check
     * @return is it an e-mail
     */
    static public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Check if a text box is empty
     * @param text the text box to check
     * @return true if text box is empty
     */
    static public boolean isEmpty(EditText text) {
        return text.getText().toString().trim().length() <= 0;
    }

    /**
     * Checks multiple text boxes if one is empty returns false
     * @param texts the text boxes
     * @return if one of the boxes are empty return true
     */
    static public boolean isEmpty(EditText... texts) {
        for (EditText text : texts) {
            if (isEmpty(text)) return true;
        }
        return false;
    }

    /**
     * Get's text boxes checking the strings inside are greater than the numbers corresponding
     * @param texts the text boxes to validate
     * @return a string with if there was an error
     */
    public static String validateLengths(String... texts) {
        for (int i=0;texts.length>i;i=i+2){
            String currentType = texts[i+1];
            int currentLength = texts[i].length();
            Log.v("CurrentString", currentType);
            Log.v("CurrentType", currentType);
            Log.v("CurrentLength", String.valueOf(currentLength));
            if (texts[i].length()<getLength(currentType)) return
                    "You cannot have a "+currentType+" of length "+currentLength;
        }
        return "";
    }

    /**
     * Get the length for the type
     * @param type the type of the validation
     * @return the length to be working with
     */
    public static int getLength(String type) {
        switch(type) {
            case ("password"): return PASSWORD_MIN_LENGTH;
            case ("username"): return USERNAME_MIN_LENGTH;
            case ("email"): return EMAIL_MIN_LENGTH;
            case ("description"): return DESCRIPTION_MIN_LENGTH;
            case ("name"): return NAME_MIN_LENGTH;
            default: return 0;
        }
    }
}
