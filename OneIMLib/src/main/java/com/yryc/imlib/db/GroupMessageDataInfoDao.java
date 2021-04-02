package com.yryc.imlib.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.GroupMessageDataInfo;

import java.util.List;

/**
 * 聊天消息
 */
@Dao
public interface GroupMessageDataInfoDao {
    //根据topicId查询聊天记录
    @Query("SELECT * FROM yryc_im_group_message_data WHERE " +
            "message_topic_id = (:topicId)" +
            " and (conversation_messages_sortNum < (:sortNum) OR 0 = (:sortNum))"+
            " ORDER BY conversation_messages_sortNum  desc LIMIT :size")
    List<GroupMessageDataInfo> selectMessageByTopicId(String topicId, long sortNum,int size);

    //替换或添加一条数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void replaceWith(GroupMessageDataInfo data);
    //更新一条数据
    @Update()

    void update(GroupMessageDataInfo data);
    //更新多条数据
    @Update()
    void update(List<GroupMessageDataInfo> data);
    //替换或添加多条数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void replaceAllWith(List<GroupMessageDataInfo> data);

    @Delete
    void delete(GroupMessageDataInfo data);

    //删除某个聊天室的消息
    @Query("DELETE FROM yryc_im_group_message_data WHERE " +
            "message_topic_id = (:topicId)")
    void deleteByUid(String topicId);

    //删除单条记录
    @Query("DELETE FROM yryc_im_group_message_data WHERE conversation_message_id =(:messageid) ")
    int deleteOnMessage(String messageid);

    //删除全部
    @Query("DELETE FROM yryc_im_group_message_data")
    void deleteConversationDtaAll();

    //根据messageid查询一条数据
    @Query("SELECT * FROM yryc_im_group_message_data WHERE conversation_message_id =(:messageid) ")
    GeneralMessageDataInfo getMessageById(String messageid);

}
