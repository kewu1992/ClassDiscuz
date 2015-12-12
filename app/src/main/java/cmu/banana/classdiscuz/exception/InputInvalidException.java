package cmu.banana.classdiscuz.exception;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import cmu.banana.classdiscuz.R;

/**
 * Exception for invalid input
 */
public class InputInvalidException extends Exception implements PromptDialog{
    public void promptDialog(Context context){
        // exception handler, prompt an alert dialog \
        // create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set dialog title & message, and provide Button to dismiss
        builder.setTitle(R.string.invalid_input_title);
        builder.setMessage(R.string.invalid_input_msg);
        builder.setPositiveButton(R.string.invalid_input_button, null);
        builder.show(); // display the Dialog
    }
}
