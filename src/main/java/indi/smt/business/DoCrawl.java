package indi.smt.business;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import indi.smt.crawler.Crawler;
import indi.smt.dao.BlogDAO;
import indi.smt.entity.Blog;
import indi.smt.utils.StringUtils;


/**
 * 爬取业务
 * @author SwordNoTrace
 *
 */
public class DoCrawl {
	/** 日志 */
	private static Logger logger = Logger.getLogger(DoCrawl.class);
	/** 爬虫 */
	private Crawler crawler;
	/** 属性 */
	private Properties prop;
	/** 博客对象存取 */
	private BlogDAO blogDAO ;
	
	public DoCrawl() throws Exception{}
	
	/**
	 * 初始化，先登录博客
	 * @param filename 验证码保存文件，多线程时要分开命名
	 * @throws Exception
	 */
	public void init(String filename) throws Exception{
		logger.info("开始初始化DoCrawl...");
		this.crawler = new Crawler();
		this.prop = new Properties();
		this.prop.load(new FileInputStream(new File("target/classes/indi/smt/config/weiboInfo.properties")));
		String username = this.prop.getProperty("username");
		String password = this.prop.getProperty("password");
		crawler.login(username, password, filename);
		this.blogDAO = SqlSessionManager.newInstance(Resources.getResourceAsReader("indi/smt/config/MyBatisConfig.xml")).getMapper(BlogDAO.class);
		logger.info("初始化结束DoCrawl...");
	}
	
	/**
	 * 爬取所有微博
	 * @param url 要爬取的个人首页
	 * @throws Exception 
	 */
	public void getAllBlogs(String url) throws Exception{
		logger.info("开始getAllBlogs(url)...");
		if(!StringUtils.isUrl(url)){
			throw new Exception("传入的URL格式不正确");
		}else if(StringUtils.isUrlWithParam(url)){
			throw new Exception("传入的URL不能带'?'的参数");
		}
		//加上参数
		String originalBlog = this.prop.getProperty("originalBlog");
		String domain = this.prop.getProperty("domain");
		String urlWithParm = url + originalBlog;//爬取微博参数
		while(true){
			String uri = this.getThePageBlog(urlWithParm);
			if(StringUtils.isEmpty(uri)){
				break;
			}else{
				urlWithParm = domain + uri;
			}
		}
		logger.info("结束getAllBlogs(url)...");
	}
	
	/**
	 * 爬取url页面所有的微博
	 * @param url
	 * @return 下一页的URI
	 * @throws Exception
	 */
	public String getThePageBlog(String url) throws Exception{
		String originalBlogPage = this.crawler.getHtml(url);
		Document doc = Jsoup.parse(originalBlogPage);
		Elements eles = doc.getElementsByAttribute("id");//获取有id属性的div
		for (Element ele : eles) {
			if("pagelist".equals(ele.id()) || "viewport".equals(ele.id()) || "internalStyle".equals(ele.id())){
				continue;
			}
			Blog blog = this.crawler.crawlBlog(ele);
			blogDAO.insertBlog(blog);
		}
		String next = this.crawler.nextPage(originalBlogPage);
		return next;
	}
	
}
