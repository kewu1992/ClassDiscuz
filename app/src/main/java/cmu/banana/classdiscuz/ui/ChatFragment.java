package cmu.banana.classdiscuz.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.ws.local.Chat;
import cmu.banana.classdiscuz.ws.local.ChatService;
import cmu.banana.classdiscuz.ws.local.GroupChatImpl;

/**
 * Created by WK on 12/4/15.
 */
public class ChatFragment extends Fragment {
    private static final String TAG = "Charfrafment";

    public static final String EXTRA_DIALOG = "dialog";
    private final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";

    private EditText messageEditText;
    private ListView messagesContainer;
    private Button sendButton;
    private ChatAdapter adapter;


    private Chat chat;
    private QBDialog dialog;
    //private KeyboardHandleRelativeLayout keyboardHandleLayout;
    //private boolean isStickersFrameVisible;
    //private ImageView stickerButton;
    private RelativeLayout container;

    public static ChatFragment newInstance(QBDialog param1) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DIALOG, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            chat.release();
        } catch (XMPPException e) {
            Log.e(TAG, "failed to release chat", e);
        }

        ChatService.getInstance().removeConnectionListener(chatConnectionListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_chat, container, false);
        messagesContainer = (ListView) v.findViewById(R.id.messagesContainer);
        messageEditText = (EditText) v.findViewById(R.id.messageEdit);
        container = (RelativeLayout) v.findViewById(R.id.container);

        // Setup opponents info
        //
        dialog = (QBDialog) getArguments().getSerializable(EXTRA_DIALOG);

        // Send button
        //
        sendButton = (Button) v.findViewById(R.id.chatSendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }
                sendChatMessage(messageText);

            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (((ChatBaseActivity)getActivity()).isSessionActive())
            initChat();

        ChatService.getInstance().addConnectionListener(chatConnectionListener);
    }

    private void showKeyboard() {
        ((InputMethodManager) messageEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(messageEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void sendChatMessage(String messageText) {
        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody(messageText);
        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
        chatMessage.setDateSent(new Date().getTime() / 1000);

        try {
            chat.sendMessage(chatMessage);
        } catch (XMPPException e) {
            Log.e(TAG, "failed to send a message", e);
        } catch (SmackException sme) {
            Log.e(TAG, "failed to send a message", sme);
        }

        messageEditText.setText("");
    }

    public void setContentBottomPadding(int padding) {
        container.setPadding(0, 0, 0, padding);
    }

    public void initChat() {
        chat = new GroupChatImpl((HomePageActivity) getActivity());

        // Join group chat
        joinGroupChat();
    }

    private void joinGroupChat() {
        ((GroupChatImpl) chat).joinGroupChat(dialog, new QBEntityCallbackImpl() {
            @Override
            public void onSuccess() {

                // Load Chat history
                //
                loadChatHistory();
            }

            @Override
            public void onError(List list) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("error when join group chat: " + list.toString()).create().show();
            }
        });
    }

    private void loadChatHistory() {
        QBRequestGetBuilder customObjectRequestBuilder = new QBRequestGetBuilder();
        customObjectRequestBuilder.setPagesLimit(100);
        customObjectRequestBuilder.sortDesc("date_sent");

        QBChatService.getDialogMessages(dialog, customObjectRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {

                adapter = new ChatAdapter(getActivity(), new ArrayList<QBChatMessage>());
                messagesContainer.setAdapter(adapter);

                for (int i = messages.size() - 1; i >= 0; --i) {
                    QBChatMessage msg = messages.get(i);
                    showMessage(msg);
                }

            }

            @Override
            public void onError(List<String> errors) {
                if (!getActivity().isFinishing()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setMessage("load chat history errors: " + errors).create().show();
                }
            }
        });
    }

    public void showMessage(QBChatMessage message) {
        adapter.add(message);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                scrollDown();
            }
        });
    }

    private void scrollDown() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    ConnectionListener chatConnectionListener = new ConnectionListener() {
        @Override
        public void connected(XMPPConnection connection) {
            Log.i(TAG, "connected");
        }

        @Override
        public void authenticated(XMPPConnection connection) {
            Log.i(TAG, "authenticated");
        }

        @Override
        public void connectionClosed() {
            Log.i(TAG, "connectionClosed");
        }

        @Override
        public void connectionClosedOnError(final Exception e) {
            Log.i(TAG, "connectionClosedOnError: " + e.getLocalizedMessage());

            // leave active room
            //
            if (dialog.getType() == QBDialogType.GROUP) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((GroupChatImpl) chat).leave();
                    }
                });
            }
        }

        @Override
        public void reconnectingIn(final int seconds) {
            if (seconds % 5 == 0) {
                Log.i(TAG, "reconnectingIn: " + seconds);
            }
        }

        @Override
        public void reconnectionSuccessful() {
            Log.i(TAG, "reconnectionSuccessful");

            // Join active room
            //
            if (dialog.getType() == QBDialogType.GROUP) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        joinGroupChat();
                    }
                });
            }
        }

        @Override
        public void reconnectionFailed(final Exception error) {
            Log.i(TAG, "reconnectionFailed: " + error.getLocalizedMessage());
        }
    };

}
