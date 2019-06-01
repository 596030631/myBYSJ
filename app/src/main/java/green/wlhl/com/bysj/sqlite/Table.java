package green.wlhl.com.bysj.sqlite;

public class Table {
    public static final String T_RECORD_TEMP = "RECORD_TEMP";
    public static final String RECORD_TEMP =  "create table RECORD_TEMP(" +
            "id integer primary key autoincrement," +
            "bizdate text," +
            "temp text" +
            ")";


    public static final String T_RECORD_HUMI = "RECORD_HUMI";
    public static final String RECORD_HUMI =  "create table RECORD_HUMI(" +
            "id integer primary key autoincrement," +
            "bizdate text," +
            "humi text" +
            ")";


    public static final String T_RECORD_GAS = "RECORD_GAS";
    public static final String RECORD_GAS =  "create table RECORD_GAS(" +
            "id integer primary key autoincrement," +
            "bizdate text," +
            "gas text" +
            ")";

}
