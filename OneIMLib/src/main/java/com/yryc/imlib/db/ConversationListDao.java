package com.yryc.imlib.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.yryc.imlib.info.ConversationListDataInfo;

import java.util.List;

/**
 * 聊天列表
 */
@Dao
public interface ConversationListDao {

    //查询所有表
    @Query("SELECT * FROM yryc_im_message_list_data ORDER BY conversation_list_last_words_time desc")
    List<ConversationListDataInfo> selectAll();

    //替换或添加一条数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void replaceWith(ConversationListDataInfo dataInfo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void replaceAllWith(List<ConversationListDataInfo> dataInfo);

    @Delete
    void delete(ConversationListDataInfo dataInfo);

    //根据userid搜索一条数据
    @Query("SELECT * FROM yryc_im_message_list_data WHERE conversation_list_user_id = (:uid) LIMIT 1")
    ConversationListDataInfo selectByUserid(String uid);

    //根据userid删除一条数据
    @Query("DELETE FROM yryc_im_message_list_data WHERE conversation_list_user_id = (:uid)")
    int deleteByUserid(String uid);

}
