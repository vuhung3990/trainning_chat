package trainning.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import trainning.chat.R;
import trainning.chat.entity.contact.ContactUser;

/**
 * Created by ASUS on 12/10/2015.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ContactUser> users;
    private LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;


    public ContactAdapter(Context mContext, ArrayList<ContactUser> users, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.users = users;
        this.onItemClickListener = onItemClickListener;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_contact_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactUser user = users.get(position);
        holder.mTvUserName.setText(user.getName());
        holder.mTvEmail.setText(user.getEmail());
        holder.pos = position;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvUserName, mTvEmail;
        public int pos;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            mTvUserName = (TextView) itemView.findViewById(R.id.tvUsername);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.setOnItemClick(pos);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.setOnItemLongClick(pos);
                    return false;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void setOnItemClick(int position);

        void setOnItemLongClick(int position);
    }


}
