package trainning.chat.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import trainning.chat.R;
import trainning.chat.entity.chatroom.Message;
import trainning.chat.entity.chatroom.OnLoadMoreListener;

/**
 * Created by ASUS on 14/10/2015.
 */
public class ChatAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Message> messages;
    private LayoutInflater mInflater;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final int VIEW_DATE = 0;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount, firstVisibleItem;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public ChatAdapter(ArrayList<Message> messages, RecyclerView recyclerView) {

        this.messages = messages;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Log.d("SCROLll", "SCRoll touch");
//                            int visibleItemCount = recyclerView.getChildCount();
//                    totalItemCount = linearLayoutManager.getItemCount();
//                    lastVisibleItem = linearLayoutManager
//                            .findLastVisibleItemPosition();
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    if (!loading
                            && firstVisibleItem <= visibleThreshold) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;

        View v;
        switch (viewType) {
            case VIEW_ITEM:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_chat_message, parent, false);

                vh = new ItemViewHolder(v);
                break;
            case VIEW_PROG:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_loadmore_dialog, parent, false);

                vh = new ProgressViewHolder(v);
                break;

        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Message message = (Message) messages.get(position);
            if (message.getId() == 1) {
                ((ItemViewHolder) holder).mTvMessage.setText(message.getMessage());
                ((ItemViewHolder) holder).mTvMessage.setBackgroundResource(R.drawable.bubble_a);
                ((ItemViewHolder) holder).mTime.setText(message.getTime());
                ((ItemViewHolder) holder).llMessage.setGravity(Gravity.RIGHT);
                if (message.getStatus() == false) {
                    ((ItemViewHolder) holder).mTvStatus.setText("Fail");
                } else {
                    ((ItemViewHolder) holder).mTvStatus.setText("Success");
                }
            } else {
                ((ItemViewHolder) holder).mTvMessage.setText(message.getMessage());
                ((ItemViewHolder) holder).mTvMessage.setBackgroundResource(R.drawable.bubble_b);
                ((ItemViewHolder) holder).mTime.setText(message.getTime());
                ((ItemViewHolder) holder).llMessage.setGravity(Gravity.LEFT);
                if (message.getStatus() == false) {
                    ((ItemViewHolder) holder).mTvStatus.setText("Fail");
                } else {
                    ((ItemViewHolder) holder).mTvStatus.setText("Success");
                }
            }
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position) != null) {
            position = VIEW_ITEM;


        } else {
//            position = VIEW_PROG;
            position=VIEW_DATE;
        }

        return position;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvMessage;
        public TextView mTime;
        public TextView mTvStatus;
        public LinearLayout llMessage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            mTime = (TextView) itemView.findViewById(R.id.tvTime);
            mTvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            llMessage = (LinearLayout) itemView.findViewById(R.id.llMessage);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public static class DateHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;

        public DateHolder(View v) {
            super(v);
            tvDate = (TextView) v.findViewById(R.id.tvDate);
        }
    }
}
