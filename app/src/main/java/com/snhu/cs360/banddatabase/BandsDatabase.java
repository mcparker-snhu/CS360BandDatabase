package com.snhu.cs360.banddatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BandsDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "bands.db";

    private static BandsDatabase instance;

    private BandsDatabase(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static BandsDatabase getInstance(Context context){
        if (instance == null){
            instance = new BandsDatabase(context);
        }

        return instance;
    }

    private static final class BandsTable{
        private static final String TABLE = "bands";
        private static final String COL_ID = "_id";
        private static final String COL_NAME = "name";
        private static final String COL_DESCRIPTION = "description";
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + BandsTable.TABLE + "( " +
                BandsTable.COL_ID + " integer primary key autoincrement, " +
                BandsTable.COL_NAME + ", " +
                BandsTable.COL_DESCRIPTION + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + BandsTable.TABLE);
        onCreate(db);
    }

    public List<Band> getBands(){
        List<Band> bands = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + BandsTable.TABLE;

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            do {
                long id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                Band band = new Band(id, name, description);
                bands.add(band);
            } while (cursor.moveToNext());
        }

        return bands;
    }

    public Band getBand(long bandId){
        Band band = null;

        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + BandsTable.TABLE + " WHERE " + BandsTable.COL_ID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{Long.toString(bandId)});

        if (cursor.moveToFirst()){

            String name = cursor.getString(1);
            String description = cursor.getString(2);
            band = new Band(bandId, name, description);
        }

        return band;
    }

    public long addBand(Band band){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BandsTable.COL_NAME, band.getName());
        values.put(BandsTable.COL_DESCRIPTION, band.getDescription());
        long newId = db.insert(BandsTable.TABLE, null, values);

        return newId;
    }

    public boolean editBand(long id, Band band){
        boolean isEdited = false;
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BandsTable.COL_ID, id);
        values.put(BandsTable.COL_NAME, band.getName());
        values.put(BandsTable.COL_DESCRIPTION, band.getDescription());

        int result = db.update(BandsTable.TABLE, values, BandsTable.COL_ID + " = " + id, null);

        return result == 1;
    }

    public boolean deleteBand(long id){
        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(BandsTable.TABLE, BandsTable.COL_ID + " = " + id, null);
        return result == 1;
    }
}
