package cmu.banana.classdiscuz.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quickblox.chat.model.QBChatMessage;
import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.app.ApplicationSingleton;
import cmu.banana.classdiscuz.ws.local.ChatService;
import cmu.banana.classdiscuz.util.TimeUtils;
import com.quickblox.users.model.QBUser;

import java.util.List;

/**
 * Reference: ApplicationSingleton.java from http://quickblox.com/developers/Android#Download_Android_SDK
 */
public class ChatAdapter extends BaseAdapter {

    private final List<QBChatMessage> chatMessages;
    private Activity context;

    private enum ChatItemType {
        Message,
        Sticker
    }

    public ChatAdapter(Activity context, List<QBChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public QBChatMessage getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getViewTypeCount() {
        return ChatItemType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return ChatItemType.Message.ordinal();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        QBChatMessage chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.list_item_message, parent, false);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        QBUser currentUser = ChatService.getInstance().getCurrentUser();
        boolean isOutgoing = chatMessage.getSenderId() == null || chatMessage.getSenderId().equals(currentUser.getId());
        setAlignment(holder, isOutgoing);
        if (holder.txtMessage != null) {
            holder.txtMessage.setText(chatMessage.getBody());
        }
        if (chatMessage.getSenderId() != null) {
            String name = ApplicationSingleton.getInstance().getName(chatMessage.getSenderId());
            holder.txtName.setText(name);
            holder.txtInfo.setText(getTimeText(chatMessage));
        } else {
            holder.txtName.setText("");
            holder.txtInfo.setText(getTimeText(chatMessage));
        }

        byte[] imageBytes = ApplicationSingleton.getInstance().getAvatar(chatMessage.getSenderId());
        if (imageBytes != null) {
            Bitmap pic = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.avatar.setImageBitmap(pic);
        }
        return convertView;
    }

    public void add(QBChatMessage message) {
        chatMessages.add(message);
    }

    private void setAlignment(ViewHolder holder, boolean isOutgoing) {
        if (!isOutgoing) {
            holder.content.removeAllViews();
            holder.content.addView(holder.text);
            holder.content.addView(holder.avatar);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtName.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtName.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.avatar.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.avatar.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.text.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.text.setLayoutParams(layoutParams);

            if (holder.txtMessage != null) {
                holder.contentWithBG.setBackgroundResource(R.drawable.chat_others_bubble);
                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.RIGHT;
                holder.txtMessage.setLayoutParams(layoutParams);
            } else {
                holder.contentWithBG.setBackgroundResource(android.R.color.transparent);
            }
        } else {
            holder.content.removeAllViews();
            holder.content.addView(holder.avatar);
            holder.content.addView(holder.text);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtName.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtName.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.avatar.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.avatar.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.text.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.text.setLayoutParams(layoutParams);

            if (holder.txtMessage != null) {
                holder.contentWithBG.setBackgroundResource(R.drawable.chat_mine_bubble);
                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtMessage.setLayoutParams(layoutParams);
            } else {
                holder.contentWithBG.setBackgroundResource(android.R.color.transparent);
            }
        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        holder.txtName = (TextView) v.findViewById(R.id.txtName);
        holder.avatar = (ImageView) v.findViewById(R.id.chat_avatar);
        holder.text = (LinearLayout) v.findViewById(R.id.chat_text);
        return holder;
    }

    private String getTimeText(QBChatMessage message) {
        return TimeUtils.millisToLongDHMS(message.getDateSent() * 1000);
    }

    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtName;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
        public ImageView avatar;
        public LinearLayout text;
    }
}
