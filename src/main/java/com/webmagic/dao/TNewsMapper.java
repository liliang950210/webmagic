package com.webmagic.dao;

import org.apache.ibatis.annotations.Insert;

import com.webmagic.entity.TNews;



public interface TNewsMapper {
    
    
    @Insert("INSERT INTO t_news (category,title,content) VALUES(#{category},#{title},#{content})")
    int insertTest(TNews news);
}