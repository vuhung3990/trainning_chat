package trainning.chat.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ASUS on 12/10/2015.
 */
public class MySharePreferences {
    public static void setValue(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("my_chat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();

    }
    public static String getValue(Context context, String key, String defauseValue) {
        SharedPreferences preferences = context.getSharedPreferences("my_chat", Context.MODE_PRIVATE);
        return preferences.getString(key, defauseValue);
    }


    public static void setValue(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences("my_chat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }

    public static Boolean getValue(Context context, String key, Boolean defauseValue) {
        SharedPreferences preferences = context.getSharedPreferences("my_chat", Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defauseValue);
    }



}
