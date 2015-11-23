package cmu.banana.classdiscuz.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Iterator;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.entities.Course;
import cmu.banana.classdiscuz.entities.Session;
import cmu.banana.classdiscuz.exception.DatabaseException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SchedulePageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SchedulePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchedulePageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String EXTRA_MESSAGE = "com.banana.classdiscuz.SchedulePageFragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<Course> coursesList = new ArrayList<Course>();
    private View mView;
    private RelativeLayout[] layout = new RelativeLayout[7];

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulePageFragment.
     */
    public static SchedulePageFragment newInstance(String param1, String param2) {
        SchedulePageFragment fragment = new SchedulePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SchedulePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log log = null;
        log.i("chaoyal", "onCrV");
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_schedule_page, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), AddCourseActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        new RefreshCourses().execute((Object) null);

        layout[1] =  (RelativeLayout) v.findViewById(R.id.mondayRelativeLayout);
        layout[2] =  (RelativeLayout) v.findViewById(R.id.tuesdayRelativeLayout);
        layout[3] =  (RelativeLayout) v.findViewById(R.id.wednesdayRelativeLayout);
        layout[4] =  (RelativeLayout) v.findViewById(R.id.thursdayRelativeLayout);
        layout[5] =  (RelativeLayout) v.findViewById(R.id.fridayRelativeLayout);

        mView = v;

        return v;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private class RefreshCourses extends AsyncTask<Object, Object, ArrayList<Course>> {
        @Override
        protected ArrayList<Course> doInBackground(Object... arg){
            try{
                return Session.get(getActivity()).getUser().getRegisteredCourses();
            } catch (DatabaseException e){
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Course> courses){
            coursesList = courses;
            drawAllCourse(coursesList);
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            new DatabaseException().promptDialog(getActivity());
        }
    }

    private void drawAllCourse(ArrayList<Course> list) {
        Course course;
        for (int i = 0; i < list.size(); i++) {
            course = list.get(i);
            drawCourse(mView, course.getName(), course.getTime(), course);
        }
    }

    //M T W R F
    //relation between margin and time: 120dp = 60min
    private void drawCourse(View v, String courseName, String time, Course course) {
        int buttonHeight;
        int marginTop;
        int startMinute = 0;
        int endMinute = 0;

        //split the time string using "-" and " "
        String[] tokens = time.split("[ -]");

        //split the start time using ":"
        String[] mTime = tokens[1].split("[:]");
        startMinute = (Integer.parseInt(mTime[0]) - 7) * 60 + Integer.parseInt(mTime[1]);

        //split the end time using ":"
        String[] mTime2 = tokens[2].split("[:]");
        endMinute = (Integer.parseInt(mTime2[0]) - 7)* 60 + Integer.parseInt(mTime2[1]);

        //carculate the marginTop and buttonHeight
        marginTop = startMinute * 2;
        buttonHeight = (endMinute - startMinute) * 2;
        String buttonName = courseName + "\n" + time;

        //draw the button
        if (tokens[0].contains("M")) {
            drawButton(layout[1], marginTop, buttonHeight, buttonName, course);
        }
        if (tokens[0].contains("T")) {
            drawButton(layout[2], marginTop, buttonHeight, buttonName, course);
        }
        if (tokens[0].contains("W")) {
            drawButton(layout[3], marginTop, buttonHeight, buttonName, course);
        }
        if (tokens[0].contains("R")) {
            drawButton(layout[4], marginTop, buttonHeight, buttonName, course);
        }
        if (tokens[0].contains("F")) {
            drawButton(layout[5], marginTop, buttonHeight, buttonName, course);
        }

    }

    //draw button.
    private void drawButton(RelativeLayout rl, int marginTop, int buttonHeight, String buttonName, final Course course) {
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                buttonHeight
        );
        p.setMargins(0, marginTop, 0, 0);

        Button button = new Button(getActivity());
        button.setLayoutParams(p);
        button.setText(buttonName);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent myIntent = new Intent(getActivity(), CourseInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_MESSAGE, course);
                myIntent.putExtras(bundle);
                startActivity(myIntent);

 //               startActivityForResult(myIntent, 0);
            }
        });
        rl.addView(button);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log log = null;
        log.i("chaoyal", "onPause");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log log = null;
        log.i("chaoyal", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log log = null;
        log.i("chaoyal", "onDisV");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log log = null;
        log.i("chaoyal", "onResume");
    }


//    public void on
}
