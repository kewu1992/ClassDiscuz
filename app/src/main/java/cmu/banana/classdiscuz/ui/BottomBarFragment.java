package cmu.banana.classdiscuz.ui;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cmu.banana.classdiscuz.R;


/**
 * create an instance of this fragment.
 */
public class BottomBarFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_bar, container, false);
    }

    public interface OnFragmentInteractionListener {
        public void onNavFragmentInteraction(String string);
    }


}
