package trainning.chat.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import trainning.chat.entity.Message;

/**
 * Created by ASUS on 14/10/2015.
 */
public class ChatDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "chat";
    private static final String KEY_ID = "id";
    public String tableName = null;
    private static final String KEY_MESSAGE = "message";

    public ChatDatabase(Context context, String tablename) {
        super(context, tablename, null, DATABASE_VERSION);
        this.tableName = tablename;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CHAT_TABLE = "CREATE TABLE " + tableName + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MESSAGE + " TEXT," + ")";
        sqLiteDatabase.execSQL(CREATE_CHAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXITS" + tableName);
    }

    public void addMessage(Message msg) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, msg.getId());
        values.put(KEY_MESSAGE, msg.getMessage());
        db.insert(tableName, null, values);
        db.close();

    }

    public void getMessage() {
        SQLiteDatabase db = getReadableDatabase();

    }

}
