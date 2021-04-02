package com.yryc.imlib.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

public class DataBase {
    private static volatile DataBase instance;
    private AppDatabase db;
    private  String dbName = "chat";

    private DataBase(Context context, String dbName) {
        this.dbName = dbName;
        db = Room.databaseBuilder(context,
                AppDatabase.class,
                "yryc_im_" + dbName)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                    }
                })
                .allowMainThreadQueries()//允许在主线程查询数据
                .addMigrations()//迁移数据库使用，下面会单独拿出来讲
                .fallbackToDestructiveMigration()//迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
                .build();


    }
    private DataBase(Context context ) {
        this.dbName = dbName;
        db = Room.databaseBuilder(context,
                AppDatabase.class,
                "yryc_im_" + dbName)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                    }
                })
                .allowMainThreadQueries()//允许在主线程查询数据
                .addMigrations()//迁移数据库使用，下面会单独拿出来讲
                .fallbackToDestructiveMigration()//迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
                .build();


    }

    public synchronized static DataBase getInstance(Context context, String dbName) {
        if (instance == null) {
            synchronized (DataBase.class) {
                if (instance == null) {
                    instance = new DataBase(context, dbName);
                }
            }

        } else if (!instance.dbName.equals(dbName)) {
            instance.getDb().close();
            instance = null;
            synchronized (DataBase.class) {
                if (instance == null) {
                    instance = new DataBase(context, dbName);
                }
            }
        }
        return instance;
    }
    public synchronized static DataBase getInstance(Context context) {
        if (instance == null) {
            synchronized (DataBase.class) {
                if (instance == null) {
                    instance = new DataBase(context);
                }
            }

        }
        return instance;
    }
    public AppDatabase getDb() {
        return db;
    }

    public void closeDataBase() {
        if (db != null && db.isOpen()) {
            db.close();
            db = null;

            instance = null;
        }
    }


}
