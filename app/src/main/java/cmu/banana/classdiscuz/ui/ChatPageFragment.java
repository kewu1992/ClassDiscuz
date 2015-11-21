package cmu.banana.classdiscuz.ui;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.entities.User;
import cmu.banana.classdiscuz.entities.ChatMessage;
import cmu.banana.classdiscuz.entities.Course;


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
    private static final String ARG_PARAM2 = "userID";
    public static final String USR_ID = "usr_id"; // Intent extra key

    private int curCoursePosition;
    private int userID;

    private OnFragmentInteractionListener mListener;

    private ListView memberListView;
    private ListView courseListView;

    public static ChatPageFragment newInstance(int param1, int param2) {
        ChatPageFragment fragment = new ChatPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
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
            curCoursePosition = getArguments().getInt(ARG_PARAM1);
            userID = getArguments().getInt(ARG_PARAM2);
        }

        //new RefreshCourses().execute(userID);
        //new RefreshChatMembers().execute(((Course)courseListView.getAdapter().getItem(curCoursePosition)).getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_chat_page, container, false);

        memberListView = (ListView) v.findViewById(R.id.memberListView);
        memberListView.setOnItemClickListener(memberListListener);

        courseListView = (ListView) v.findViewById(R.id.courseListView);
        courseListView.setOnItemClickListener(courseListListener);

        return v;
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private class RefreshCourses extends AsyncTask<Object, Object, ArrayList<Course>>{
        @Override
        protected ArrayList<Course> doInBackground(Object... arg){
            return BackendConnector.getCourses(arg[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Course> courses){
            courseListView.setAdapter(new ArrayAdapter<Course>(getActivity(), android.R.layout.simple_list_item_1, courses));
        }
    }

    private class RefreshChatMembers extends AsyncTask<Integer, Object, ArrayList<User>>{
        @Override
        protected ArrayList<User> doInBackground(Integer... arg){
            return BackendConnector.getMembersByCourse(arg[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<User> members){
            ChatMemberAdapter chatMemberAdapter = new ChatMemberAdapter(members);
            memberListView.setAdapter(chatMemberAdapter);
        }
    }

    private class ChatMemberAdapter extends ArrayAdapter<User> {
        public ChatMemberAdapter(ArrayList<User> members){
            super(getActivity(), android.R.layout.simple_list_item_1, members);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_chat_member, null);
            }

            User chatMember = getItem(position);

            byte[] imageBytes = Base64.decode(chatMember.getAvatar(), Base64.DEFAULT);
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

    AdapterView.OnItemClickListener courseListListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
        {
            curCoursePosition = position;
            new RefreshChatMembers().execute(userID, ((Course)courseListView.getAdapter().getItem(curCoursePosition)).getId());
        }

    };

    AdapterView.OnItemClickListener memberListListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
        {
            Intent viewContact = new Intent(getActivity(), ViewOthersProfileActivity.class);
            viewContact.putExtra(USR_ID, ((User)memberListView.getAdapter().getItem(position)).getId());
            startActivity(viewContact); // start the ViewContact Activity
        }

    };

}
