/*
    Author: Luke Collier
    Description: User errors handled here
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */
package epsilon.whygosoloapplication.activities.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Project Application
 * Created by Joker
 * Created on 03/05/16
 */
public class UserError {
    /**
     * A standard helper method for standardizing errors
     * @param errorMessage the message to display
     * @param context the context to display the error dialog
     */
    static public void showErrorDialog(String errorMessage, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(errorMessage)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
