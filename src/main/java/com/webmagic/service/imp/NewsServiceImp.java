package com.webmagic.service.imp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.webmagic.dao.TNewsMapper;
import com.webmagic.entity.TNews;
import com.webmagic.service.NewsService;

@Service
public class NewsServiceImp implements NewsService{
	
	@Resource
	private TNewsMapper newsMapper;

	public void insert(TNews news) {
		// TODO Auto-generated method stub
		newsMapper.insertTest(news);
	}

}
