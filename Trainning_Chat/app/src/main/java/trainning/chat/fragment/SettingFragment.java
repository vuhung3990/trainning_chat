package trainning.chat.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import trainning.chat.activity.SplashActivity;
import trainning.chat.gcm.GCMConfig;
import trainning.chat.util.MySharePreferences;
import trainning.chat.R;
import trainning.chat.activity.LogInActivity;

/**
 * Created by ASUS on 10/10/2015.
 */
public class SettingFragment extends Fragment {
    private TextView tvLogOut;
    private SharedPreferences mSharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        mSharedPreferences = getActivity().getSharedPreferences(GCMConfig.PREFERENCE_NAME, Context.MODE_PRIVATE);
        tvLogOut = (TextView) view.findViewById(R.id.tvLogOut);
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d("REG_ID", mSharedPreferences.getString(GCMConfig.PREFERENCE_KEY_REG_ID, null) + "");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Are you sure want logOut?");

                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                        MySharePreferences.setValue(getActivity(), "email", null);
                        MySharePreferences.setValue(getActivity(), "token", null);
                        MySharePreferences.setValue(getActivity(), "checked", false);
                        mSharedPreferences.edit().putString(GCMConfig.PREFERENCE_KEY_REG_ID, null).commit();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), SplashActivity.class));
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });
        return view;

    }
}
