package trainning.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import trainning.chat.R;
import trainning.chat.entity.history.HistoryUserData;

/**
 * Created by ASUS on 12/10/2015.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<HistoryUserData> users;
    private LayoutInflater mInflater;
    public static OnItemClickListener itemClickListener;
    public int flag_message = 0;


    public void setFlag_message(int flag_message) {
        this.flag_message = flag_message;
    }

    public HistoryAdapter(Context mContext, ArrayList<HistoryUserData> users) {
        this.mContext = mContext;
        this.users = users;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_history_list, parent, false);

        ViewHolder holder = new ViewHolder(view);


        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryUserData user = users.get(position);
        holder.mTvUserName.setText(user.getEmail());
        holder.mTvLastMessage.setText(user.getData());
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(user.getUpdated_at().getDate());
            String newString = new SimpleDateFormat("HH:mm:ss").format(date);

            holder.mTvTime.setText(newString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.pos = position;
        if (flag_message > 0) {
            holder.tvNotifyMessage.setVisibility(View.VISIBLE);
            holder.tvNotifyMessage.setText(flag_message + "");
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTvUserName, mTvLastMessage, mTvTime, tvNotifyMessage;
        public int pos;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvLastMessage = (TextView) itemView.findViewById(R.id.tvMessage_latter);
            mTvUserName = (TextView) itemView.findViewById(R.id.tvUsername);
            mTvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvNotifyMessage = (TextView) itemView.findViewById(R.id.tvMessageMissing);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.setonItemClick(view, pos);
        }

    }

    public interface OnItemClickListener {


        void setonItemClick(View view, int position);
    }
}
