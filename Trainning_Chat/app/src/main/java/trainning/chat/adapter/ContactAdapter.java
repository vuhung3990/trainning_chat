package trainning.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import trainning.chat.R;
import trainning.chat.entity.ContactUser;
import trainning.chat.entity.HistoryUser;

/**
 * Created by ASUS on 12/10/2015.
 */
public class ContactAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ContactUser> users;
    private LayoutInflater mInflater;

    public ContactAdapter(Context mContext, ArrayList<ContactUser> users) {
        this.mContext = mContext;
        this.users = users;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        ContactUser user = users.get(i);
        if (view == null) {
            view = mInflater.inflate(R.layout.item_contact_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();


        }
        holder.userName.setText(user.getUsername());
        holder.tvEmail.setText(user.getEmail());
        return view;
    }

    class ViewHolder {
        TextView userName;
        TextView tvEmail;

        public ViewHolder(View view) {
            userName = (TextView) view.findViewById(R.id.tvUsername);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);


        }
    }
}
