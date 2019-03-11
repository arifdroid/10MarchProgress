package com.example.a6march_firestorefinalize.mock_firebaseload_sqlitesave;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.a6march_firestorefinalize.mock_firebaseload_sqlitesave.FSToSQLite_Contract.*;

public class FS_SQLite_DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FS_SQLiteDB";
    public static final int DATABASE_VERSION=1;

    private static FS_SQLite_DBHelper instance;

    private SQLiteDatabase db;

    private static ArrayList<FireStoreSQLiteClass> liss;

    public FS_SQLite_DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //this.liss=liss;
        Log.i("checkk flow SQ","sqlite constructor");
    }

    public static synchronized FS_SQLite_DBHelper getInstance(Context context, ArrayList<FireStoreSQLiteClass> liss){
        if(instance==null){
            instance = new FS_SQLite_DBHelper(context);
        }
       FS_SQLite_DBHelper.liss=liss;
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;
        Log.i("checkk flow SQ","oncreate");

        final String SQL_CREATE_FSTOSQLITE_TABLE = "CREATE TABLE "+
            FSToSQLite_Table.TABLE_NAME +" ( "+
            FSToSQLite_Table._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            FSToSQLite_Table.COLUMN_NAME + " TEXT, "+
            FSToSQLite_Table.COLUMN_PHONE + " TEXT, "+
            FSToSQLite_Table.COLUMN_IMAGE_URL + " TEXT, "+
            FSToSQLite_Table.COLUMN_SCORE_CARD_REF + " TEXT "+
            " ) ";

        db.execSQL(SQL_CREATE_FSTOSQLITE_TABLE);

        Log.i("checkk flow SQ","h :");
        addToTableFuntion(liss);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ FSToSQLite_Table.TABLE_NAME);
        onCreate(db);

    }

    private void addToTableFuntion(ArrayList<FireStoreSQLiteClass> liss){
        int h =0;
        Log.i("checkk flow SQ","h :"+h+" : sqlite count");
        Log.i("checkk flow SQ","liss :"+liss.size()+" : liss size");

        for(int i=0; i<liss.size();i++) {
            h++;
            Log.i("checkk flow SQ","h :"+h+" : sqlite count");

            FireStoreSQLiteClass fireStoreSQLiteClass = liss.get(i);

            ContentValues cv = new ContentValues();

            cv.put(FSToSQLite_Table.COLUMN_NAME, fireStoreSQLiteClass.getName());
            cv.put(FSToSQLite_Table.COLUMN_PHONE, fireStoreSQLiteClass.getPhone());
            cv.put(FSToSQLite_Table.COLUMN_IMAGE_URL, fireStoreSQLiteClass.getImage_url());
            cv.put(FSToSQLite_Table.COLUMN_SCORE_CARD_REF, fireStoreSQLiteClass.getMy_score_ref());

            db.insert(FSToSQLite_Table.TABLE_NAME, null, cv);
        }
        Log.i("checkk flow SQ","h :"+h+" : sqlite count");

        Log.i("checkk flow SQ","liss :"+liss.size()+" : liss size");
    }

    //4th this is where we getting back our value.

    public List<FireStoreSQLiteClass> getAll_FSToSQLite(){

        Log.i("checkk flow SQ","sqlite called");
        List<FireStoreSQLiteClass> returnList =new ArrayList<>();
        db = getReadableDatabase();
        Cursor c =db.rawQuery("SELECT *FROM "+ FSToSQLite_Table.TABLE_NAME, null);

        if(c.moveToFirst()){

            do{

                FireStoreSQLiteClass fireStoreSQLiteClass = new FireStoreSQLiteClass();

                fireStoreSQLiteClass.setName(c.getString(c.getColumnIndex(FSToSQLite_Table.COLUMN_NAME)));
                fireStoreSQLiteClass.setPhone(c.getString(c.getColumnIndex(FSToSQLite_Table.COLUMN_PHONE)));
                fireStoreSQLiteClass.setImage_url(c.getString(c.getColumnIndex(FSToSQLite_Table.COLUMN_IMAGE_URL)));
                fireStoreSQLiteClass.setMy_score_ref(c.getString(c.getColumnIndex(FSToSQLite_Table.COLUMN_SCORE_CARD_REF)));


                returnList.add(fireStoreSQLiteClass);

            }while (c.moveToNext());


        }

        c.close();

        return returnList;
    }


}
