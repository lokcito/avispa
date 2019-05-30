package info.rayrojas.avispa.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

import info.rayrojas.avispa.conf.Settings;

public class Notify {
    private int id;
    private String title;
    private String message;
    private String token;
    private String extra;
    public static NotifyDbHelper dbInstance;

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


    /* LocalStorage */

    public static class NotifyTable implements BaseColumns {
        public static final String TABLE_NAME = "avispa_notifications";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TOKEN = "token";
        public static final String COLUMN_NAME_MESSAGE = "message";
        public static final String COLUMN_NAME_EXTRA = "extra";
    }

    public class NotifyDbHelper extends SQLiteOpenHelper {

        private static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + NotifyTable.TABLE_NAME + " (" +
                        NotifyTable._ID + " INTEGER PRIMARY KEY," +
                        NotifyTable.COLUMN_NAME_TITLE + " TEXT," +
                        NotifyTable.COLUMN_NAME_MESSAGE + " TEXT," +
                        NotifyTable.COLUMN_NAME_EXTRA + " TEXT," +
                        NotifyTable.COLUMN_NAME_TOKEN + " TEXT)";


        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + NotifyTable.TABLE_NAME;

        public NotifyDbHelper(Context context) {
            super(context, Settings.DATABASE_NAME, null, Settings.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE);
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
    }

    /* End LocalStorage */

    public NotifyDbHelper getDbInstance(Context _context) {
        if ( Notify.dbInstance == null ) {
            Notify.dbInstance = new NotifyDbHelper(_context);
            return Notify.dbInstance;
        }
        return Notify.dbInstance;
    }

    public void getOne(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getReadableDatabase();

        String[] fields = new String[] {NotifyTable._ID, NotifyTable.COLUMN_NAME_TITLE,
            NotifyTable.COLUMN_NAME_MESSAGE, NotifyTable.COLUMN_NAME_EXTRA};
        String[] args = new String[] {this.getId() + ""};

        Cursor c = db.query(NotifyTable.TABLE_NAME, fields,
                null, null,  null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {
                this.setId(c.getInt(0));
                this.setTitle(c.getString(1));
                this.setMessage(c.getString(2));
                this.setExtra(c.getString(3));
            } while(c.moveToNext());
        }
    }

    public ArrayList<Notify> getAll(Context _context) {
        ArrayList<Notify> rows = new ArrayList<>();
        SQLiteDatabase db = this.getDbInstance(_context).getReadableDatabase();

        String[] fields = new String[] {NotifyTable._ID, NotifyTable.COLUMN_NAME_TITLE,
                NotifyTable.COLUMN_NAME_MESSAGE, NotifyTable.COLUMN_NAME_EXTRA};
        String[] args = new String[] {this.getId() + ""};

        Cursor c = db.query(NotifyTable.TABLE_NAME, fields,
                null, null,  null, null, NotifyTable._ID+" DESC");

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {
                Notify n = new Notify();
                n.setId(c.getInt(0));
                n.setTitle(c.getString(1));
                n.setMessage(c.getString(2));
                n.setExtra(c.getString(3));
                rows.add(n);
            } while(c.moveToNext());
        }
        return rows;
    }

//    public void syncLocal(Context _context) {
//        SQLiteDatabase db = this.getDbInstance(_context).getReadableDatabase();
//
//        String[] fields = new String[] {UserTable.COLUMN_NAME_FIRST_NAME};
//        String[] args = new String[] {this.id + ""};
//
//        this.localSynced = false;
//
//        Cursor c = db.query(UserTable.TABLE_NAME, fields, "_id=?", args,  null, null, null);
//
//        //Nos aseguramos de que existe al menos un registro
//        if (c.moveToFirst()) {
//            do {
//                this.first_name = c.getString(0);
//                this.localSynced = true;
//            } while(c.moveToNext());
//        }
//    }

    public void setLocal(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(NotifyTable._ID, this.id);
        values.put(NotifyTable.COLUMN_NAME_TITLE, this.getTitle());
        values.put(NotifyTable.COLUMN_NAME_MESSAGE, this.getMessage());
        values.put(NotifyTable.COLUMN_NAME_EXTRA, this.getExtra());
        values.put(NotifyTable.COLUMN_NAME_TOKEN, this.getToken());

        String[] args = new String[] {this.getId() + ""};

        //this.syncLocal(_context);

////        if ( this.localSynced ) {//
//            db.update(NotifyTable.TABLE_NAME, values, "_id = ?", args);
////        } else {
        if (this.id == 0) {
            this.id = this.getAll(_context).size() + 1;
        }
        long newRowId = db.insert(NotifyTable.TABLE_NAME, null, values);
//        }

    }


}
