package com.yryc.imlib.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.yryc.imlib.info.ConversationListDataInfo;
import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.GroupMessageDataInfo;
import com.yryc.imlib.info.WordDataInfo;


@Database(entities = {ConversationListDataInfo.class, GeneralMessageDataInfo.class, WordDataInfo.class, GroupMessageDataInfo.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ConversationListDao getMessageListDao();

    public abstract GeneralMessageDataInfoDao getMessageDataInfoDao();

    public abstract WordDataInfoDao getWordDataInfoList();

    public abstract GroupMessageDataInfoDao getGroupMessageDataInfoDao();
}
