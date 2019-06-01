package green.wlhl.com.bysj.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import green.wlhl.com.bysj.adapter.GasAdapter;
import green.wlhl.com.bysj.adapter.HumiAdapter;
import green.wlhl.com.bysj.adapter.TempAdapter;

public class Action {
    public static void insertRecordTemp(Context context, String temp,String bizdate){

        SQLiteDatabase db = CreateDB.GetDB(context);
        ContentValues values = new ContentValues();
        values.put("bizdate",bizdate);
        values.put("temp",temp);
        db.insert(Table.T_RECORD_TEMP,null,values);
        values.clear();
    }

    public static void insertRecordHumi(Context context, String humi,String bizdate){

        SQLiteDatabase db = CreateDB.GetDB(context);
        ContentValues values = new ContentValues();
        values.put("bizdate",bizdate);
        values.put("humi",humi);
        db.insert(Table.T_RECORD_HUMI,null,values);
        values.clear();
    }


    public static void insertRecordGas(Context context, String gas,String bizdate){

        SQLiteDatabase db = CreateDB.GetDB(context);
        ContentValues values = new ContentValues();
        values.put("bizdate",bizdate);
        values.put("gas",gas);
        db.insert(Table.T_RECORD_GAS,null,values);
        values.clear();
    }



    //查询

    public static List<TempAdapter.TEMP> selectRecordTemp(Context context, List<TempAdapter.TEMP> list){

        SQLiteDatabase db = CreateDB.GetDB(context);
        Cursor cursor = db.rawQuery("select  *  from  RECORD_TEMP",null);
        if(cursor.moveToFirst()) {
            do {
                String bizdate = cursor.getString(cursor.getColumnIndex("bizdate"));
                String temp = cursor.getString(cursor.getColumnIndex("temp"));
                Log.e("TAG",temp+"---");
                list.add(new TempAdapter.TEMP());
                list.get(list.size()-1).setBizdate(bizdate);
                list.get(list.size()-1).setTemp(temp);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static List<HumiAdapter.HUMI> selectRecordHumi(Context context, List<HumiAdapter.HUMI> list){

        SQLiteDatabase db = CreateDB.GetDB(context);
        Cursor cursor = db.rawQuery("select  *  from  RECORD_HUMI",null);
        if(cursor.moveToFirst()) {
            do {
                String bizdate = cursor.getString(cursor.getColumnIndex("bizdate"));
                String humi = cursor.getString(cursor.getColumnIndex("humi"));
                Log.e("TAG",humi+"---");
                list.add(new HumiAdapter.HUMI());
                list.get(list.size()-1).setBizdate(bizdate);
                list.get(list.size()-1).setHumi(humi);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static List<GasAdapter.GAS> selectRecordGas(Context context, List<GasAdapter.GAS> list){

        SQLiteDatabase db = CreateDB.GetDB(context);
        Cursor cursor = db.rawQuery("select  *  from  RECORD_GAS",null);
        if(cursor.moveToFirst()) {
            do {
                String bizdate = cursor.getString(cursor.getColumnIndex("bizdate"));
                String gas = cursor.getString(cursor.getColumnIndex("gas"));
                Log.e("TAG",gas+"---");
                list.add(new GasAdapter.GAS());
                list.get(list.size()-1).setBizdate(bizdate);
                list.get(list.size()-1).setGas(gas);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
