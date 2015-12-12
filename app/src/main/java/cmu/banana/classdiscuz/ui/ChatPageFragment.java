package cmu.banana.classdiscuz.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallbackImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.app.ApplicationSingleton;
import cmu.banana.classdiscuz.entities.Course;
import cmu.banana.classdiscuz.entities.Session;
import cmu.banana.classdiscuz.entities.User;
import cmu.banana.classdiscuz.exception.DatabaseException;
import cmu.banana.classdiscuz.ws.local.ChatService;

/**
 * Chat room fragment in the HomepageActivity. One of the two main fragment
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
    private TextView courseNameTextView;

    private ArrayList<QBDialog> dialogs;

    private RefreshCourses refreshCoursesTask;
    private RefreshChatMembers refreshChatMembers;

    private TextView memberNumTextView;

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

        courseNameTextView = (TextView) v.findViewById(R.id.chat_course_name_text_view);

        memberNumTextView = (TextView) v.findViewById(R.id.memberNumTextView);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (((ChatBaseActivity)getActivity()).isSessionActive())
            getDialogs();

    }

    public void getDialogs(){
        // Get dialogs
        ChatService.getInstance().getDialogs(new QBEntityCallbackImpl() {
            @Override
            public void onSuccess(Object object, Bundle bundle) {
                dialogs = (ArrayList<QBDialog>) object;

                if (refreshCoursesTask != null)
                    refreshCoursesTask.cancel(true);
                refreshCoursesTask = new RefreshCourses();
                refreshCoursesTask.execute((Object) null);
            }

            @Override
            public void onError(List errors) {
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
        private int errNum;
        @Override
        protected ArrayList<Course> doInBackground(Object... arg){
            try{
                errNum = 0;
                return Session.get(getActivity()).getUser().getRegisteredCourses(getActivity(), true);
            } catch (DatabaseException e){
                errNum = 1;
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
                courseNameTextView.setText(courses.get(0).getName());

                if (refreshChatMembers != null)
                    refreshChatMembers.cancel(true);
                refreshChatMembers = new RefreshChatMembers();
                refreshChatMembers.execute(courses.get(0));
            }
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            if (errNum == 1)
                new DatabaseException().promptDialog(getActivity());
        }
    }

    private class RefreshChatMembers extends AsyncTask<Course, Object, ArrayList<User>>{
        private int errNum;
        @Override
        protected ArrayList<User> doInBackground(Course... arg){
            try{
                errNum = 0;
                return arg[0].getStudents();
            } catch (DatabaseException e){
                errNum = 1;
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<User> members){
            ChatMemberAdapter chatMemberAdapter = new ChatMemberAdapter(members);
            memberListView.setAdapter(chatMemberAdapter);

            HashMap<Integer, String> userID2Name = new HashMap<>();
            HashMap<Integer, byte[]> userID2Avatar = new HashMap<>();
            for (User user : members){
                userID2Name.put(user.getChatId(), user.getName());
                userID2Avatar.put(user.getChatId(), user.getAvatar());
            }

            ApplicationSingleton.getInstance().setUserHashMap(userID2Name);
            ApplicationSingleton.getInstance().setAvatarHashMap(userID2Avatar);

            memberNumTextView.setText("Members " + String.valueOf(members.size()));

            setDefaultFragment();
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            if (errNum == 1)
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

            String shownName = course.getNum() + " " + course.getName();

            ((TextView)convertView.findViewById(R.id.course_item_name_text_view)).setText(shownName);

            return convertView;
        }
    }

    private AdapterView.OnItemClickListener courseListListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
        {
            curCoursePosition = position;
            curDialogId = ((Course)courseListView.getAdapter().getItem(curCoursePosition)).getDialogId();
            courseNameTextView.setText(((Course)courseListView.getAdapter().getItem(curCoursePosition)).getName());

            if (refreshChatMembers != null)
                refreshChatMembers.cancel(true);
            refreshChatMembers = new RefreshChatMembers();
            refreshChatMembers.execute((Course) courseListView.getAdapter().getItem(curCoursePosition));
        }

    };

    private AdapterView.OnItemClickListener memberListListener = new AdapterView.OnItemClickListener()
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
