package com.webmagic.pipeline;


import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.webmagic.dao.TNewsMapper;
import com.webmagic.entity.TNews;
import com.webmagic.service.NewsService;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Service
public class NewsPipeline implements Pipeline{
	
	@Autowired
	private TNewsMapper newDao;

	public void process(ResultItems resultItems, Task task) {
		// TODO Auto-generated method stub
		TNews news = resultItems.get("news");
		if(news != null){
			System.out.println("categoey:"+ news.getCategory());
			System.out.println("title:"+news.getTitle());
			System.out.println("contetn:"+news.getContent());
			try{
			newDao.insertTest(news);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		
	}

}
