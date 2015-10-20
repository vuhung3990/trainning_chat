package trainning.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import trainning.chat.R;
import trainning.chat.entity.chatroom.Message;

/**
 * Created by ASUS on 14/10/2015.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Message> messages;
    private LayoutInflater mInflater;

    public ChatAdapter(Context mContext, ArrayList<Message> messages) {
        this.mContext = mContext;
        this.messages = messages;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_chat_message, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (message.getId() == 1) {
            holder.mTvMessage.setText(message.getMessage());
            holder.llMessage.setGravity(Gravity.RIGHT);

            holder.mTvTime.setText(message.getTime());
            if (message.getStatus() == false) {
                holder.mTvStatus.setText("Fail");

            } else {
                holder.mTvStatus.setText("Success");
            }
            holder.mTvMessage.setBackgroundResource(R.drawable.bubble_a);
        } else {
            holder.mTvMessage.setText(message.getMessage());
            holder.llMessage.setGravity(Gravity.LEFT);
            holder.mTvMessage.setBackgroundResource(R.drawable.bubble_b);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvMessage;
        public LinearLayout llMessage;
        public TextView mTvTime;
        public TextView mTvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            llMessage = (LinearLayout) itemView.findViewById(R.id.llMessage);
            mTvTime = (TextView) itemView.findViewById(R.id.tvTime);
            mTvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
        }
    }
}
