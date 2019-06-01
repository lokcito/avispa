package info.rayrojas.avispa.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

import info.rayrojas.avispa.conf.Settings;

public class Channel {
    private int id;
    public String name;
    public static Channel.ChannelDbHelper dbInstance;


    /* LocalStorage */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class ChannelTable implements BaseColumns {
        public static final String TABLE_NAME = "avispa_channels";
        public static final String COLUMN_NAME_NAME = "name";
    }

    public class ChannelDbHelper extends SQLiteOpenHelper {

        private static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + Channel.ChannelTable.TABLE_NAME + " (" +
                        Channel.ChannelTable._ID + " INTEGER PRIMARY KEY," +
                        Channel.ChannelTable.COLUMN_NAME_NAME + " TEXT)";


        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Channel.ChannelTable.TABLE_NAME;

        public ChannelDbHelper(Context context) {
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


    public Channel.ChannelDbHelper getDbInstance(Context _context) {
        if ( Channel.dbInstance == null ) {
            Channel.dbInstance = new Channel.ChannelDbHelper(_context);
            return Channel.dbInstance;
        }
        return Channel.dbInstance;
    }

    public void getOne(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getReadableDatabase();

        String[] fields = new String[] {Channel.ChannelTable._ID, Channel.ChannelTable.COLUMN_NAME_NAME};
        String[] args = new String[] {this.getId() + ""};

        Cursor c = db.query(Channel.ChannelTable.TABLE_NAME, fields,
                null, null,  null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {
                this.setId(c.getInt(0));
                this.setName(c.getString(1));
            } while(c.moveToNext());
        }
    }

    public ArrayList<Channel> getAll(Context _context) {
        ArrayList<Channel> rows = new ArrayList<>();
        SQLiteDatabase db = this.getDbInstance(_context).getReadableDatabase();

        String[] fields = new String[] {Channel.ChannelTable._ID, ChannelTable.COLUMN_NAME_NAME};
        String[] args = new String[] {this.getId() + ""};

        Cursor c = db.query(Channel.ChannelTable.TABLE_NAME, fields,
                null, null,  null, null,
                Channel.ChannelTable._ID+" DESC");

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {
                Channel n = new Channel();
                n.setId(c.getInt(0));
                n.setName(c.getString(1));
                rows.add(n);
            } while(c.moveToNext());
        }
        return rows;
    }
    public void unsetLocal(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getWritableDatabase();
        String[] args = new String[] {this.getId() + ""};
        db.delete(Channel.ChannelTable.TABLE_NAME, "_id = ?", args);
    }
    public void setLocal(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ChannelTable.COLUMN_NAME_NAME, this.getName());

        String[] args = new String[] {this.getId() + ""};

        if (this.id == 0) {
            this.id = this.getAll(_context).size() + 1;
        }
        long newRowId = db.insert(ChannelTable.TABLE_NAME, null, values);
    }
}
