package green.wlhl.com.bysj.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CreateDB extends SQLiteOpenHelper {

    public CreateDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Table.RECORD_TEMP);
        db.execSQL(Table.RECORD_HUMI);
        db.execSQL(Table.RECORD_GAS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists RECORD_TEMP");
        db.execSQL("drop table if exists RECORD_HUMI");
        db.execSQL("drop table if exists RECORD_GAS");
        onCreate(db);
    }

    public static SQLiteDatabase GetDB(Context context){
        CreateDB dbHelper = new CreateDB(context,"shuaijun.db",null,2);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db;
    }
}
