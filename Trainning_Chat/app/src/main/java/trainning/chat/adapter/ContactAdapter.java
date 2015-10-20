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

    public ContactAdapter(Context mContext, ArrayList<ContactUser> users) {
        this.mContext = mContext;
        this.users = users;
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
        holder.mTvUserName.setText(user.getUsername());
        holder.mTvEmail.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvUserName, mTvEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            mTvUserName = (TextView) itemView.findViewById(R.id.tvUsername);
        }
    }
}
