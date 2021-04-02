package com.yryc.imlib.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.yryc.imlib.info.GeneralMessageDataInfo;

import java.util.List;

/**
 * 聊天消息
 */
@Dao
public interface GeneralMessageDataInfoDao {
    //根据topicId分页查询聊天记录
    @Query("SELECT * FROM yryc_im_message_data WHERE " +
            "(message_topic_id = (:topicId)" +
            " and (conversation_messages_sortNum < (:sortNum)"+
            " OR  0= (:sortNum) ))" +
            " ORDER BY conversation_sent_time desc LIMIT :size ")
    List<GeneralMessageDataInfo> selectMessageByTopicId(String topicId,long sortNum,int size);
    //查询某聊天对象的所有聊天消息
    @Query("SELECT * FROM yryc_im_message_data WHERE " +
            "(" +
            "(conversation_who_send = (:uid) and conversation_who_receive = (:myUid))" +
            " OR " +
            "(conversation_who_receive = (:uid) and conversation_who_send = (:myUid))" +
            ")" +
            " and (conversation_messages_sortNum < (:sortNum)"+
            " OR 0= (:sortNum) )" +
            " ORDER BY conversation_sent_time  desc LIMIT :size  ")
    List<GeneralMessageDataInfo> selectAll(String uid, String myUid, long sortNum,int size);


    //查询和某人的最后一条数据
    //select top 1 from 数据库表 where 条件 order by 主ID desc
    @Query("SELECT * FROM yryc_im_message_data WHERE " +
            "(conversation_who_send = (:uid) and conversation_who_receive = (:myUid))" +
            " OR " +
            "(conversation_who_send = (:myUid) and conversation_who_receive = (:uid))" +
            " ORDER BY conversation_sent_time  desc LIMIT 1")
    GeneralMessageDataInfo selectLastMessageByUid(String uid, String myUid);

    //查询和某人的第一条数据
    //select top 1 from 数据库表 where 条件 order by 主ID desc
    @Query("SELECT * FROM yryc_im_message_data WHERE " +
            "(conversation_who_send = (:uid) and conversation_who_receive = (:myUid))" +
            " OR " +
            "(conversation_who_send = (:myUid) and conversation_who_receive = (:uid))" +
            " ORDER BY conversation_sent_time  LIMIT 1")
    GeneralMessageDataInfo selectFirstMessageByUid(String uid, String myUid);

    //替换或添加一条数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void replaceWith(GeneralMessageDataInfo data);
    //更新一条数据
    @Update()
    void update(GeneralMessageDataInfo data);
    //更新多条数据
    @Update()
    void update(List<GeneralMessageDataInfo> data);
    //替换或添加多条数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void replaceAllWith(List<GeneralMessageDataInfo> data);

    @Delete
    void delete(GeneralMessageDataInfo data);

    //删除和某人的聊天记录
    @Query("DELETE FROM yryc_im_message_data WHERE " +
            "(conversation_who_send = (:uid) and conversation_who_receive = (:myUid))" +
            " OR " +
            "(conversation_who_receive = (:uid) and conversation_who_send = (:myUid))")
    void deleteByUid(String uid, String myUid);

    //删除单条记录
    @Query("DELETE FROM yryc_im_message_data WHERE conversation_message_id =(:messageid) ")
    int deleteOnMessage(String messageid);

    //删除全部
    @Query("DELETE FROM yryc_im_message_data")
    void deleteConversationDtaAll();

    //根据messageid查询一条数据
    @Query("SELECT * FROM yryc_im_message_data WHERE conversation_message_id =(:messageid) ")
    GeneralMessageDataInfo getMessageById(String messageid);
}
