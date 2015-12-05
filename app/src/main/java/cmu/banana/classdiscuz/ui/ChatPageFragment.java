package cmu.banana.classdiscuz.ui;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallbackImpl;

import java.util.ArrayList;
import java.util.List;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.entities.Session;
import cmu.banana.classdiscuz.entities.User;
import cmu.banana.classdiscuz.entities.ChatMessage;
import cmu.banana.classdiscuz.entities.Course;
import cmu.banana.classdiscuz.exception.DatabaseException;
import cmu.banana.classdiscuz.util.BitmapScale;
import cmu.banana.classdiscuz.ws.local.ChatService;


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
    public static final String USR_ID = "usr_id"; // Intent extra key

    private int curCoursePosition;
    private String curDialogId;

    private OnFragmentInteractionListener mListener;

    private ListView memberListView;
    private ListView courseListView;
    private ArrayList<QBDialog> dialogs;

    public static ChatPageFragment newInstance(int param1) {
        ChatPageFragment fragment = new ChatPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
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
        }
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


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (((ChatBaseActivity)getActivity()).isSessionActive())
            getDialogs();

    }

    public void getDialogs(){
        //progressBar.setVisibility(View.VISIBLE);

        // Get dialogs
        //
        ChatService.getInstance().getDialogs(new QBEntityCallbackImpl() {
            @Override
            public void onSuccess(Object object, Bundle bundle) {
                //progressBar.setVisibility(View.GONE);
                dialogs = (ArrayList<QBDialog>) object;

                new RefreshCourses().execute((Object) null);
            }

            @Override
            public void onError(List errors) {
                //progressBar.setVisibility(View.GONE);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("get dialogs errors: " + errors).create().show();
            }
        });
    }

    private void setDefaultFragment()
    {
        QBDialog selectedDialog = null;
        for (QBDialog dialog : dialogs)
            if (dialog.getDialogId().compareTo(curDialogId) == 0){
                selectedDialog = dialog;
                break;
            }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        ChatFragment chat = ChatFragment.newInstance(selectedDialog);
        transaction.replace(R.id.chat_content, chat);
        transaction.commit();
    }

    public void showMessage(QBChatMessage message) {
        ChatFragment fragment = (ChatFragment) getFragmentManager().findFragmentById(R.id.chat_content);
        fragment.showMessage(message);
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private class RefreshCourses extends AsyncTask<Object, Object, ArrayList<Course>>{
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
            CourseAdapter courseAdapter = new CourseAdapter(courses);
            courseListView.setAdapter(courseAdapter);
            if (courses.size() > 0) {

                curCoursePosition = 0;
                curDialogId = courses.get(0).getDialogId();
                setDefaultFragment();
                new RefreshChatMembers().execute(courses.get(0));
            }
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            new DatabaseException().promptDialog(getActivity());
        }
    }

    private class RefreshChatMembers extends AsyncTask<Course, Object, ArrayList<User>>{
        @Override
        protected ArrayList<User> doInBackground(Course... arg){
            try{
                return arg[0].getStudents();
            } catch (DatabaseException e){
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<User> members){
            ChatMemberAdapter chatMemberAdapter = new ChatMemberAdapter(members);
            memberListView.setAdapter(chatMemberAdapter);
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            new DatabaseException().promptDialog(getActivity());
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

            if (chatMember.getAvatar() != null){
                ImageView imageAvatar = (ImageView) convertView.findViewById(R.id.member_list_avatar);
                byte[] imageBytes = chatMember.getAvatar();
                Bitmap pic = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                //Bitmap scaledPic = BitmapScale.bitmapScale(getActivity(), pic, imageAvatar.getMaxHeight());
                imageAvatar.setImageBitmap(pic);
            } else {
                Drawable d = getActivity().getResources().getDrawable(R.drawable.default_avatar);
                ImageView imageAvatar = (ImageView) convertView.findViewById(R.id.member_list_avatar);
                imageAvatar.setImageDrawable(d);
            }


            ((TextView)convertView.findViewById(R.id.member_list_name)).setText(chatMember.getName());

            return convertView;
        }
    }

    private class CourseAdapter extends ArrayAdapter<Course> {
        public CourseAdapter(ArrayList<Course> courses){
            super(getActivity(), android.R.layout.simple_list_item_1, courses);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_course, null);
            }

            Course course = getItem(position);

            String shownName = course.getNum() + course.getName();

            ((TextView)convertView.findViewById(R.id.course_item_name_text_view)).setText(shownName);

            return convertView;
        }
    }

    AdapterView.OnItemClickListener courseListListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
        {
            curCoursePosition = position;
            curDialogId = ((Course)courseListView.getAdapter().getItem(curCoursePosition)).getDialogId();
            setDefaultFragment();
            new RefreshChatMembers().execute((Course)courseListView.getAdapter().getItem(curCoursePosition));
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
