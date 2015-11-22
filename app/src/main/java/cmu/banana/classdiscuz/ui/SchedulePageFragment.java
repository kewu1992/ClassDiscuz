package cmu.banana.classdiscuz.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<Course> coursesList;
    private boolean isGetCourseDone = false;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulePageFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        while (!isGetCourseDone);

        drawCourse(v);

        return v;
    }

    private void drawCourse(View v) {
        int buttonHeight = 240;//1 hour = 120
        int marginTop = 120;//1 hour = 120

        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.sundayRelativeLayout);

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                buttonHeight
        );
        p.setMargins(0, marginTop, 0, 0);

        Button button = new Button(getActivity());
        button.setLayoutParams(p);
        button.setText("Button");

        rl.addView(button);
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
        // TODO: Update argument type and name
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
            isGetCourseDone = true;
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            new DatabaseException().promptDialog(getActivity());
        }
    }

}
