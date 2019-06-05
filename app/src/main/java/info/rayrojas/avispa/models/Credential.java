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

public class Credential {
    public String token;
    public String is_active;

    private int id;
    private String client;
    public static DatabaseHelper dbInstance;


    /* LocalStorage */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String name) {
        this.token = name;
    }

    public String getIsActive() {
        return is_active;
    }

    public void setIsActive(String is_active) {
        this.is_active = is_active;
    }


    /* End LocalStorage */


    public DatabaseHelper getDbInstance(Context _context) {
        if ( Credential.dbInstance == null ) {
            Credential.dbInstance = new DatabaseHelper(_context);
            return Credential.dbInstance;
        }
        return Credential.dbInstance;
    }

    public void getOne(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getReadableDatabase();

        String[] fields = new String[] {DatabaseHelper.Columns._ID,
                DatabaseHelper.Columns.COLUMN_NAME_TOKEN,
                DatabaseHelper.Columns.COLUMN_NAME_ACTIVE,
                DatabaseHelper.Columns.COLUMN_NAME_CLIENT};
        String[] args = new String[] {this.getId() + ""};

        Cursor c = db.query(DatabaseHelper.CREDENTIAL_TABLE_NAME, fields,
                null, null,  null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {
                this.setId(c.getInt(0));
                this.setToken(c.getString(1));
                this.setIsActive(c.getString(2));
                this.setClient(c.getString(3));
            } while(c.moveToNext());
        }
    }

    public void getActive(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getReadableDatabase();

        String[] fields = new String[] {DatabaseHelper.Columns._ID,
                DatabaseHelper.Columns.COLUMN_NAME_TOKEN,
                DatabaseHelper.Columns.COLUMN_NAME_ACTIVE,
                DatabaseHelper.Columns.COLUMN_NAME_CLIENT};
        String[] args = new String[] {"1"};

        Cursor c = db.query(DatabaseHelper.CREDENTIAL_TABLE_NAME, fields,
                "is_active = ?", args, null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {
                this.setId(c.getInt(0));
                this.setToken(c.getString(1));
                this.setIsActive(c.getString(2));
                this.setClient(c.getString(3));
            } while(c.moveToNext());
        }
    }

    public ArrayList<Credential> getAll(Context _context) {
        ArrayList<Credential> rows = new ArrayList<>();
        SQLiteDatabase db = this.getDbInstance(_context).getReadableDatabase();

        String[] fields = new String[] {DatabaseHelper.Columns._ID,
                DatabaseHelper.Columns.COLUMN_NAME_TOKEN,
                DatabaseHelper.Columns.COLUMN_NAME_ACTIVE,
                DatabaseHelper.Columns.COLUMN_NAME_CLIENT};
        String[] args = new String[] {this.getId() + ""};

        Cursor c = db.query(DatabaseHelper.CREDENTIAL_TABLE_NAME, fields,
                null, null,  null, null,
                DatabaseHelper.Columns._ID+" DESC");

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {
                Credential n = new Credential();
                n.setId(c.getInt(0));
                n.setToken(c.getString(1));
                n.setIsActive(c.getString(2));
                n.setClient(c.getString(3));
                rows.add(n);
            } while(c.moveToNext());
        }
        return rows;
    }

    public void unsetLocal(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getWritableDatabase();
        String[] args = new String[] {this.getId() + ""};
        db.delete(DatabaseHelper.CREDENTIAL_TABLE_NAME, "_id = ?", args);
    }

    public  void updateLocal(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Columns.COLUMN_NAME_TOKEN, this.getToken());
        values.put(DatabaseHelper.Columns.COLUMN_NAME_ACTIVE, this.getIsActive());
        values.put(DatabaseHelper.Columns.COLUMN_NAME_CLIENT, this.getIsActive());

        String[] args = new String[] {this.getId() + ""};

        long newRowId = db.update(DatabaseHelper.CREDENTIAL_TABLE_NAME, values, "_id = ?", args);
    }
    public void setActiveJustOne(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Columns.COLUMN_NAME_ACTIVE, "0");
        long newRowId = db.update(DatabaseHelper.CREDENTIAL_TABLE_NAME, values, null, null);

        ContentValues values_ = new ContentValues();
        values_.put(DatabaseHelper.Columns.COLUMN_NAME_ACTIVE, "1");

        String[] args = new String[] {this.getId() + ""};

        long newRowId_ = db.update(DatabaseHelper.CREDENTIAL_TABLE_NAME, values_, "_id = ?", args);

    }

    public boolean isNonDefined() {
        return this.id == 0;
    }

    public void setLocal(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Columns.COLUMN_NAME_TOKEN, this.getToken());
        values.put(DatabaseHelper.Columns.COLUMN_NAME_ACTIVE, "0");
        values.put(DatabaseHelper.Columns.COLUMN_NAME_CLIENT, this.getClient());

        if (this.id == 0) {
            this.id = this.getAll(_context).size() + 1;
        }
        long newRowId = db.insert(DatabaseHelper.CREDENTIAL_TABLE_NAME, null, values);
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
