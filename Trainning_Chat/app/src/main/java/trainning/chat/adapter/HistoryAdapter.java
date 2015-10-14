package trainning.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import trainning.chat.R;
import trainning.chat.entity.HistoryUser;

/**
 * Created by ASUS on 12/10/2015.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<HistoryUser> users;
    private LayoutInflater mInflater;

    public HistoryAdapter(Context mContext, ArrayList<HistoryUser> users) {
        this.mContext = mContext;
        this.users = users;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_history_list, parent, false);

        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryUser user = users.get(position);
        holder.mTvUserName.setText(user.getUsername());
        holder.mTvLastMessage.setText(user.getMessage_latter());
        holder.mTvTime.setText(user.getTime());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvUserName, mTvLastMessage, mTvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvLastMessage = (TextView) itemView.findViewById(R.id.tvMessage_latter);
            mTvUserName = (TextView) itemView.findViewById(R.id.tvUsername);
            mTvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
}
