package com.example.pr_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "UserInfo";
    public DatabaseHelper(Context context){
        super(context, "Userdata.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Запрос на создание таблицы в БД
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "name TEXT PRIMARY KEY, " +
                "phone TEXT, " +
                "date_of_birth TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Условие на удаление таблицы UserInfo
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    /**
     * Метод добавления данных пользователя в таблицу UserInfo
     * @param name - Имя
     * @param phone - Номер телефона
     * @param date_of_birth - Дата рождения
     * @return - Возвращает true/false (зависит от результата)
     */
    public Boolean insert(String name, String phone, String date_of_birth){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("date_of_birth", date_of_birth);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    /**
     * Выполняет запрос Select для выборки всех данных из БД
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    /**
     * Выполняет удаление юзера по его имени
     * @param elementName - Имя пользователя
     */
    public void removeAt(String elementName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "name = '" + elementName + "'",null);
    }

    /**
     * Выполняет обновление записи в БД
     * @param elementName - Имя пользователя, у которого будем менять данные
     * @param name - Новое имя
     * @param phone - Новый номер телефона
     * @param date_of_birth - Новая дата рождения
     */
    public void update(String elementName, String name, String phone, String date_of_birth){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("date_of_birth", date_of_birth);

        db.update(TABLE_NAME, contentValues , "name = '" + elementName + "'",null);
    }
}