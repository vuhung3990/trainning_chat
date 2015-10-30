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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import trainning.chat.activity.SplashActivity;
import trainning.chat.gcm.GCMConfig;
import trainning.chat.util.MySharePreferences;
import trainning.chat.R;
import trainning.chat.activity.LogInActivity;

/**
 * Created by ASUS on 10/10/2015.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private TextView tvLogOut;
    private SharedPreferences mSharedPreferences;
    private RelativeLayout rlOnOffNotify, rlOnOffSound, rlOnOffMusic;
    private ImageView ivOnOffNotify, ivOnOffSound, ivOnOffMusic;
    private TextView tvTheme;
    public static boolean notify_on = true, sound_on = true, music_on = true;
    private TextView tvMyCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        mSharedPreferences = getActivity().getSharedPreferences(GCMConfig.PREFERENCE_NAME, Context.MODE_PRIVATE);
        tvLogOut = (TextView) view.findViewById(R.id.tvLogOut);
        tvTheme = (TextView) view.findViewById(R.id.tvTheme);
        rlOnOffMusic = (RelativeLayout) view.findViewById(R.id.rlOnOffMusic);
        rlOnOffNotify = (RelativeLayout) view.findViewById(R.id.rlOnOffNotify);
        rlOnOffSound = (RelativeLayout) view.findViewById(R.id.rlOnOffSound);
        ivOnOffNotify = (ImageView) view.findViewById(R.id.ivOnOffNotify);
        ivOnOffSound = (ImageView) view.findViewById(R.id.ivOnOffSound);
        ivOnOffMusic = (ImageView) view.findViewById(R.id.ivOnOffMusic);
        tvMyCount = (TextView) view.findViewById(R.id.tvMyAcount);
        tvMyCount.setText(MySharePreferences.getValue(getActivity(), "email", ""));

        rlOnOffMusic.setOnClickListener(this);
        rlOnOffNotify.setOnClickListener(this);
        rlOnOffSound.setOnClickListener(this);
        tvTheme.setOnClickListener(this);
        tvLogOut.setOnClickListener(this);


        return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLogOut:
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
                break;

            case R.id.rlOnOffNotify:
                if (notify_on) {
                    notify_on = !notify_on;
                    ivOnOffNotify.setImageResource(R.mipmap.ic_off);
                } else {
                    notify_on = !notify_on;
                    ivOnOffNotify.setImageResource(R.mipmap.ic_on);

                }

                break;

            case R.id.rlOnOffSound:
                if (sound_on) {
                    sound_on = !sound_on;
                    ivOnOffSound.setImageResource(R.mipmap.ic_on);
                } else {
                    sound_on = !sound_on;
                    ivOnOffSound.setImageResource(R.mipmap.ic_off);
                }
                break;

            case R.id.rlOnOffMusic:
                if (music_on) {
                    music_on = !music_on;
                    ivOnOffMusic.setImageResource(R.mipmap.ic_on);
                } else {
                    music_on = !music_on;
                    ivOnOffMusic.setImageResource(R.mipmap.ic_off);
                }
                break;
            case R.id.tvTheme:

                break;


        }
    }
}
