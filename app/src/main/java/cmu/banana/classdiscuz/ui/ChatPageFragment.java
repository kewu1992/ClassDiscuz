package cmu.banana.classdiscuz.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.model.ChatMember;
import cmu.banana.classdiscuz.model.ChatMessage;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatPageFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "currentSelectCourse";

    private int curCourse;

    private OnFragmentInteractionListener mListener;

    private ListView memberListView;

    public static ChatPageFragment newInstance(String param1) {
        ChatPageFragment fragment = new ChatPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curCourse = Integer.parseInt(getArguments().getString(ARG_PARAM1));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_chat_page, container, false);

        memberListView = (ListView) v.findViewById(R.id.memberListView);

        return v;
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private class RefreshChatMembers extends AsyncTask<Integer, Object, ArrayList<ChatMember>>{
        @Override
        protected ArrayList<ChatMember> doInBackground(Integer... arg){
            return ChatMember.getMembers(curCourse);
        }

        @Override
        protected void onPostExecute(ArrayList<ChatMember> members){
            ChatMemberAdapter chatMemberAdapter = new ChatMemberAdapter(members);
            memberListView.setAdapter(chatMemberAdapter);
        }
    }

    private class ChatMemberAdapter extends ArrayAdapter<ChatMember> {
        public ChatMemberAdapter(ArrayList<ChatMember> members){
            super(getActivity(), android.R.layout.simple_list_item_1, members);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_chat_member, null);
            }

            ChatMember chatMember = getItem(position);

            byte[] imageBytes = null;//Base64.decode(song.getImageBytes(), Base64.DEFAULT);
            Bitmap pic = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            ((ImageView)convertView.findViewById(R.id.member_list_avatar)).setImageBitmap(pic);

            ((TextView)convertView.findViewById(R.id.member_list_name)).setText("");

            return convertView;
        }
    }

    private class ChatMsgAdapter extends ArrayAdapter<ChatMessage> {
        public ChatMsgAdapter(ArrayList<ChatMessage> messages){
            super(getActivity(), android.R.layout.simple_list_item_1, messages);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_chat_msg, null);
            }

            ChatMessage chatMessage = getItem(position);

            return convertView;
        }
    }

}
