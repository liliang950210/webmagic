package com.webmagic.processor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Controller;

import com.webmagic.entity.TNews;
import com.webmagic.pipeline.NewsPipeline;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
@Controller
public class XinhuaProcessor implements PageProcessor{
	
	@Autowired
	private NewsPipeline newsPipeline;
	
	private static final String[] START_URLS = {"http://news.xinhuanet.com/politics/2017-03/27/c_1120701077.htm",
			"http://news.xinhuanet.com/city/2017-03/28/c_129520229.htm",
			"http://news.xinhuanet.com/culture/2017-03/27/c_1120698244.htm",
			"http://news.xinhuanet.com/fashion/2017-03/28/c_1120700518.htm",
			"http://news.xinhuanet.com/energy/2017-03/28/c_1120706573.htm",
			"http://news.xinhuanet.com/money/2017-03/28/c_129519969.htm",
			"http://news.xinhuanet.com/health/2017-03/28/c_1120710204.htm",
			"http://news.xinhuanet.com/food/2017-03/28/c_1120705580.htm",
			"http://news.xinhuanet.com/tw/2017-03/28/c_1120710373.htm",
			"http://news.xinhuanet.com/gangao/2017-03/28/c_129519753.htm",
			"http://news.xinhuanet.com/tech/2017-03/28/c_1120705548.htm",
			"http://news.xinhuanet.com/finance/2017-03/28/c_129520405.htm",
			"http://news.xinhuanet.com/legal/2017-03/28/c_1120712604.htm",
			"http://news.xinhuanet.com/mil/2017-03/10/c_129506319.htm"
			};
	
	private static final String BASE_URL = "http://news.xinhuanet.com";
	
	 private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	public void process(Page page) {
		// TODO Auto-generated method stub
		
		page.addTargetRequests(page.getHtml().links().regex(BASE_URL+"/[a-z]*/[0-9]*-[0-9]*/[0-9]*/[a-z]_[0-9]*.htm").all());
		String title = page.getHtml().xpath("div[@class='h-title']/text()").toString();
//    	System.out.println(formatString(page.getHtml().css(".h-title").toString()));
		if(title == null){
			page.setSkip(true);
		}
		StringBuffer content  = new StringBuffer();
    	List<String> list = page.getHtml().css("#p-detail p").all();
    	for(String s:list){
    		content.append(formatString(s));
    	}
    	
    	String category  = page.getUrl().toString().substring(BASE_URL.length()+1).split("/")[0];
//    	System.out.println(category);
    	
    	TNews news = new TNews();
    	news.setCategory(category);
    	news.setContent(content.toString());
    	news.setTitle(title);
    	page.putField("news", news);
    	
    	
	}
	private static String formatString(String s){
    	s = s.replaceAll(" ", "");
    	s = s.replaceAll("\n", "");
    	int start = 0;
    	int end = 0;
    	int nowStation = 0;
    	while(s.length()!= 0 && nowStation != s.length()){
    		if(s.charAt(nowStation) == '<'){
    			start = nowStation;
    			nowStation ++;
    		}else if(s.charAt(nowStation) == '>'){
    			end = nowStation;
    			s = s.replace(s.substring(start, end+1), "");
    			nowStation = start;
    		}else{
    			nowStation ++;
    		}
    	}
    	return s.replaceAll("　", "");
    }
	
	
	public static void main(String[] args) {
    	System.out.println("start");
    	ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:spring-mybatis.xml"); 
    	XinhuaProcessor xinhuaProcessor = ac.getBean(XinhuaProcessor.class);
//        Spider.create(xinhuaProcessor).addUrl("http://news.xinhuanet.com/politics/2017-03/27/c_1120701077.htm")
//        .addPipeline(new NewsPipeline())
//        .thread(5).run();
//    	String s = "<p>　　<strong>原标题：初中生自杀个案警醒：生命该如何“教育”</strong></p>";
//    	System.out.println(formatString(s));
    	xinhuaProcessor.start(xinhuaProcessor);
    	System.out.println("end");
    }
	
	public void start(PageProcessor process){
		 Spider.create(process).addUrl(START_URLS)
	        .addPipeline(newsPipeline)
	        .thread(5).run();
	}
	
	

}
