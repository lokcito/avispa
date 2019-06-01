package info.rayrojas.avispa.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import info.rayrojas.avispa.conf.Settings;
import info.rayrojas.avispa.models.Credential;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static class Columns implements BaseColumns {
        public static final String COLUMN_NAME_TOKEN = "token";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_MESSAGE = "message";
        public static final String COLUMN_NAME_EXTRA = "extra";
    }

    public static final String CREDENTIAL_TABLE_NAME = "avispa_credentials";

    private static final String CREDENTIAL_SQL_CREATE_TABLE =
            "CREATE TABLE " + CREDENTIAL_TABLE_NAME + " (" +
                    Columns._ID + " INTEGER PRIMARY KEY," +
                    Columns.COLUMN_NAME_TOKEN + " TEXT)";


    private static final String CREDENTIAL_SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CREDENTIAL_TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, Settings.DATABASE_NAME, null, Settings.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREDENTIAL_SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
