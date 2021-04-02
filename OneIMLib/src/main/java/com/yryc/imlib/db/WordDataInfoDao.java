package com.yryc.imlib.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.yryc.imlib.info.WordDataInfo;


import java.util.List;

/**
 * 常用语
 */
@Dao
public interface WordDataInfoDao {

    //查询所有表
    @Query("SELECT * FROM yryc_im_word_list_data")
    List<WordDataInfo> selectAll();

    //替换或添加一条数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void replaceWith(WordDataInfo dataInfo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void replaceAllWith(List<WordDataInfo> dataInfo);

    //根据删除一条数据
    @Query("DELETE FROM yryc_im_word_list_data WHERE word_id = (:id)")
    int deleteDataById(int id);

    @Query("DELETE FROM yryc_im_word_list_data")
    void deleteAll();
}
