package cmu.banana.classdiscuz.exception;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import cmu.banana.classdiscuz.R;

/**
 * Created by chaoya on 11/23/15.
 */
public class NoSuchCourseException extends  Exception implements PromptDialog {
    public void promptDialog(Context context) {
        // exception handler, prompt an alert dialog
        // create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set dialog title & message, and provide Button to dismiss
        builder.setTitle(R.string.no_course_title);
        builder.setMessage(R.string.no_course_msg);
        builder.setPositiveButton(R.string.no_course_button, null);
        builder.show(); // display the Dialog
    }
}