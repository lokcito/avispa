package info.rayrojas.avispa.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

import info.rayrojas.avispa.conf.Settings;

public class Credential {
    public String token;

    private int id;
    public static Credential.CredentialDbHelper dbInstance;


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




    /* End LocalStorage */


    public Credential.CredentialDbHelper getDbInstance(Context _context) {
        if ( Credential.dbInstance == null ) {
            Credential.dbInstance = new Credential.CredentialDbHelper(_context);
            return Credential.dbInstance;
        }
        return Credential.dbInstance;
    }

    public void getOne(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getReadableDatabase();

        String[] fields = new String[] {Credential.CredentialTable._ID, CredentialTable.COLUMN_NAME_TOKEN};
        String[] args = new String[] {this.getId() + ""};

        Cursor c = db.query(Credential.CredentialTable.TABLE_NAME, fields,
                null, null,  null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {
                this.setId(c.getInt(0));
                this.setToken(c.getString(1));
            } while(c.moveToNext());
        }
    }

    public ArrayList<Credential> getAll(Context _context) {
        ArrayList<Credential> rows = new ArrayList<>();
        SQLiteDatabase db = this.getDbInstance(_context).getReadableDatabase();

        String[] fields = new String[] {Credential.CredentialTable._ID, CredentialTable.COLUMN_NAME_TOKEN};
        String[] args = new String[] {this.getId() + ""};

        Cursor c = db.query(Credential.CredentialTable.TABLE_NAME, fields,
                null, null,  null, null,
                Credential.CredentialTable._ID+" DESC");

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {
                Credential n = new Credential();
                n.setId(c.getInt(0));
                n.setToken(c.getString(1));
                rows.add(n);
            } while(c.moveToNext());
        }
        return rows;
    }

    public void unsetLocal(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getWritableDatabase();
        String[] args = new String[] {this.getId() + ""};
        db.delete(Credential.CredentialTable.TABLE_NAME, "_id = ?", args);
    }

    public void setLocal(Context _context) {
        SQLiteDatabase db = this.getDbInstance(_context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CredentialTable.COLUMN_NAME_TOKEN, this.getToken());

        String[] args = new String[] {this.getId() + ""};

        if (this.id == 0) {
            this.id = this.getAll(_context).size() + 1;
        }
        long newRowId = db.insert(CredentialTable.TABLE_NAME, null, values);
    }
}
