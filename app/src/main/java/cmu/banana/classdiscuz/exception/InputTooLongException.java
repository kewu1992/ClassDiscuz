package cmu.banana.classdiscuz.exception;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import cmu.banana.classdiscuz.R;

/**
 * Created by WK on 11/13/15.
 */
public class InputTooLongException extends Exception implements PromptDialog {
    public void promptDialog(Context context) {
        // exception handler, prompt an alert dialog
        // create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set dialog title & message, and provide Button to dismiss
        builder.setTitle(R.string.errorTitle);
        builder.setMessage(R.string.errorMessage);
        builder.setPositiveButton(R.string.errorButton, null);
        builder.show(); // display the Dialog
    }
}
