package trainning.chat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import trainning.chat.MySharePreferences;
import trainning.chat.R;
import trainning.chat.activity.LogInActivity;

/**
 * Created by ASUS on 10/10/2015.
 */
public class SettingFragment extends Fragment {
    private TextView tvLogOut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        tvLogOut = (TextView) view.findViewById(R.id.tvLogOut);
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MySharePreferences.setValue(getActivity(), "email", null);
                MySharePreferences.setValue(getActivity(), "token", null);
                MySharePreferences.setValue(getActivity(), "checked", false);
                startActivity(new Intent(getActivity(), LogInActivity.class));
                getActivity().finish();
            }
        });
        return view;

    }
}
