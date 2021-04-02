package com.yryc.imlib.info;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "yryc_im_word_list_data")
public class WordDataInfo {

    @PrimaryKey
    @ColumnInfo(name = "word_id")
    private int id;//常用语id
    @ColumnInfo(name = "word_content")
    private String content;    //常用语内容

    @Ignore
    public WordDataInfo() {
    }

    public WordDataInfo(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId( int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
