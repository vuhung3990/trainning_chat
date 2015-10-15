package trainning.chat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import trainning.chat.entity.User;

/**
 * Created by ASUS on 09/10/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userManager";
    private static final String TABLE_USER = "user";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_NAME + " TEXT," + KEY_EMAIL + " TEXT," + KEY_PASSWORD + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXITS" + TABLE_USER);
        onCreate(sqLiteDatabase);
    }

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getUserName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassWord());
        db.insert(TABLE_USER, null, values);
        db.close();


    }

    /**
     * get user saved in database
     *
     * @param email email you need get
     */
    public User getUser(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_ID, KEY_USER_NAME, KEY_EMAIL, KEY_PASSWORD}, KEY_EMAIL + "=?", new String[]{email}, null, null, null);

        User user = null;
        if (cursor != null) {
            cursor.moveToFirst();
            user = new User(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))), cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)), cursor.getString(cursor.getColumnIndex(KEY_EMAIL)), cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
        }
        return user;
    }

}
