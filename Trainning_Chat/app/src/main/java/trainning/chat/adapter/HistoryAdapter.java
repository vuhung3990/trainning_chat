package trainning.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

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
        holder.mTvTime.setText(user.getUpdated_at().getDate());
        holder.pos = position;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTvUserName, mTvLastMessage, mTvTime;
        public int pos;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvLastMessage = (TextView) itemView.findViewById(R.id.tvMessage_latter);
            mTvUserName = (TextView) itemView.findViewById(R.id.tvUsername);
            mTvTime = (TextView) itemView.findViewById(R.id.tvTime);
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
