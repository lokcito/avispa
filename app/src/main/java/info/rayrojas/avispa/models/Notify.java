package info.rayrojas.avispa.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

import info.rayrojas.avispa.conf.Settings;
import info.rayrojas.avispa.helpers.DatabaseHelper;

public class Notify {
    private int id;
    private String title;
    private String message;
    private String token;
    public static DatabaseHelper dbInstance;
    private String extra;
    private String event;
    private String channel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }


//    /* LocalStorage */
//
//    public static class NotifyTable implements BaseColumns {
//        public static final String TABLE_NAME = "avispa_notifications";
//
//    }
//
//    public class NotifyDbHelper extends SQLiteOpenHelper {
//
//        private static final String SQL_CREATE_TABLE =
//                "CREATE TABLE " + NotifyTable.TABLE_NAME + " (" +
//                        NotifyTable._ID + " INTEGER PRIMARY KEY," +
//                        NotifyTable.COLUMN_NAME_TITLE + " TEXT," +
//                        NotifyTable.COLUMN_NAME_MESSAGE + " TEXT," +
//                        NotifyTable.COLUMN_NAME_EXTRA + " TEXT," +
//                        NotifyTable.COLUMN_NAME_TOKEN + " TEXT)";
//
//
//        private static final String SQL_DELETE_ENTRIES =
//                "DROP TABLE IF EXISTS " + NotifyTable.TABLE_NAME;
//
//        public NotifyDbHelper(Context context) {
//            super(context, Settings.DATABASE_NAME, null, Settings.DATABASE_VERSION);
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            db.execSQL(SQL_CREATE_TABLE);
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            db.execSQL(SQL_DELETE_ENTRIES);
//            onCreate(db);
//        }
//
//        @Override
//        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            onUpgrade(db, oldVersion, newVersion);
//        }
//    }
//
//    /* End LocalStorage */

    public DatabaseHelper getDbInstance(Context _context) {
        if ( Notify.dbInstance == null ) {
            Notify.dbInstance = new DatabaseHelper(_context);
            return Notify.dbInstance;
        }
        return Notify.dbInstance;
    }

    public Notify getOne(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getReadableDatabase();

        String[] fields = new String[] {DatabaseHelper.Columns._ID,
                DatabaseHelper.Columns.COLUMN_NAME_TITLE,
                DatabaseHelper.Columns.COLUMN_NAME_MESSAGE,
                DatabaseHelper.Columns.COLUMN_NAME_EXTRA,
                DatabaseHelper.Columns.COLUMN_NAME_CHANNEL,
                DatabaseHelper.Columns.COLUMN_NAME_EVENT,
                DatabaseHelper.Columns.COLUMN_NAME_TOKEN};
        String[] args = new String[] {this.getId() + ""};

        Cursor c = db.query(DatabaseHelper.NOTIFY_TABLE_NAME, fields,
                null, null,  null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {
                this.setId(c.getInt(0));
                this.setTitle(c.getString(1));
                this.setMessage(c.getString(2));
                this.setExtra(c.getString(3));
                this.setChannel(c.getString(4));
                this.setEvent(c.getString(5));
                this.setToken(c.getString(6));
            } while(c.moveToNext());
            return this;
        }
        return null;
    }

    public ArrayList<Notify> getAll(Context _context) {
        ArrayList<Notify> rows = new ArrayList<>();
        SQLiteDatabase db = this.getDbInstance(_context).getReadableDatabase();

        String[] fields = new String[] {DatabaseHelper.Columns._ID,
                DatabaseHelper.Columns.COLUMN_NAME_TITLE,
                DatabaseHelper.Columns.COLUMN_NAME_MESSAGE,
                DatabaseHelper.Columns.COLUMN_NAME_EXTRA,
                DatabaseHelper.Columns.COLUMN_NAME_TOKEN,
                DatabaseHelper.Columns.COLUMN_NAME_EVENT,
                DatabaseHelper.Columns.COLUMN_NAME_CHANNEL};
        String[] args = new String[] {this.getId() + ""};

        Cursor c = db.query(DatabaseHelper.NOTIFY_TABLE_NAME, fields,
                null, null,  null, null,
                DatabaseHelper.Columns._ID+" DESC");

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {
                Notify n = new Notify();
                n.setId(c.getInt(0));
                n.setTitle(c.getString(1));
                n.setMessage(c.getString(2));
                n.setExtra(c.getString(3));
                n.setToken(c.getString(4));
                n.setEvent(c.getString(5));
                n.setChannel(c.getString(6));
                rows.add(n);
            } while(c.moveToNext());
        }
        return rows;
    }

    public void unsetLocal(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getWritableDatabase();
        String[] args = new String[] {this.getId() + ""};
        db.delete(DatabaseHelper.NOTIFY_TABLE_NAME, "_id = ?", args);
    }

    public void setLocal(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Columns.COLUMN_NAME_TITLE, this.getTitle());
        values.put(DatabaseHelper.Columns.COLUMN_NAME_MESSAGE, this.getMessage());
        values.put(DatabaseHelper.Columns.COLUMN_NAME_EXTRA, this.getExtra());
        values.put(DatabaseHelper.Columns.COLUMN_NAME_TOKEN, this.getToken());
        values.put(DatabaseHelper.Columns.COLUMN_NAME_EVENT, this.getEvent());
        values.put(DatabaseHelper.Columns.COLUMN_NAME_CHANNEL, this.getChannel());

        String[] args = new String[] {this.getId() + ""};

        if (this.id == 0) {
            this.id = this.getAll(_context).size() + 1;
        }
        long newRowId = db.insert(DatabaseHelper.NOTIFY_TABLE_NAME, null, values);
    }


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
