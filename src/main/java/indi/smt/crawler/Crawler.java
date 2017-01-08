package indi.smt.crawler;

import indi.smt.entity.Blog;
import indi.smt.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 爬蟲
 * @author SwordNoTrace
 *
 */
public class Crawler {
	
	private static Logger logger = Logger.getLogger(Crawler.class.getName());
	
	/**
	 * 客户端
	 */
	private HttpClient client;
	/**
	 * 手机版微博首页
	 */
	public static final String URL = "http://weibo.cn/pub/";
	

	public HttpClient getClient() {
		return client;
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}
	
	public Crawler() throws Exception{
		this.client = HttpClients.createDefault();
	}

	/**
	 * 根据URI获取页面HTML
	 * @param url 页面URL
	 * @return
	 * @throws Exception 
	 */
	public String getHtml(String url) throws Exception{
		logger.info("开始getHtml(url)...");
		if(!StringUtils.isUrl(url)){
			throw new Exception("传入的URL格式不正确");
		}
		HttpGet get = new HttpGet(url);
		HttpResponse res = client.execute(get);
		HttpEntity entity = res.getEntity();
		String html = EntityUtils.toString(entity,"UTF-8");
		logger.info("结束getHtml(url)...");
		return html;
	}
	
	/**
	 * 下载图片
	 * @param ImgUrl 图片链接地址
	 * @param fileName 图片保存名称
	 * @throws Exception 
	 */
	public void downloadImage(String ImgUrl, String fileName) throws Exception{
		logger.info("开始downloadImage(ImgUrl,fileName)...");
		if(!StringUtils.isUrl(ImgUrl)){
			throw new Exception("传入的URL格式不正确");
		}
		HttpGet get = new HttpGet(ImgUrl);
		HttpResponse res = this.client.execute(get);
		HttpEntity entity = res.getEntity();
		InputStream is = entity.getContent();
		File f = new File(fileName);
		if(f.exists()){
			f.delete();
			f.createNewFile();
		}else{
			f.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(f);
		@SuppressWarnings("unused")
		int line = -1;
		byte[] bytes = new byte[is.available()];
		while((line = is.read(bytes)) != -1){
			fos.write(bytes);
		}
		fos.close();
		is.close();
		get.releaseConnection();
		logger.info("结束downloadImage(ImgUrl,fileName)...");
	}
	
	/**
	 * 登录获取cookies，登录之后才能爬取微博页面
	 * @param username 登录用户名
	 * @param password 登录密码
	 * @param filename 验证码图片保存地址
	 * @exception Exception
	 */
	public void login(String username, String password, String filename) throws Exception{
		logger.info("开始login(username,password,filename)...");
		if(StringUtils.isEmpty(username)){
			throw new Exception("输入的用户名为空");
		}else if(StringUtils.isEmpty(password)){
			throw new Exception("输入的密码为空");
		}else if(StringUtils.isEmpty(filename)){
			throw new Exception("输入的文件名为空");
		}
		//1.获取登录请求地址及登录参数名称
		//1.1 获取首页信息
		String homepage_html = this.getHtml(URL);
		if(StringUtils.isEmpty(homepage_html)){
			throw new Exception("获取微博首页html失败");
		}
		//1.2 获取登录页地址
		String login_url = null;
		Document doc_homepage = Jsoup.parse(homepage_html);
		Elements eles1 = doc_homepage.getElementsByAttribute("href");
		for (Element ele : eles1) {
			String text = ele.text();
			if("登录".equals(text)){
				login_url = ele.attr("href");
				break;
			}
		}
		if(StringUtils.isEmpty(login_url)){
			throw new Exception("登录url获取失败");
		}
		if(!StringUtils.isUrl(login_url)){
			throw new Exception("获取登录URL格式错误");
		}
		//1.3 访问登录页，获取登录所需参数
		String login_html = this.getHtml(login_url);
		if(StringUtils.isEmpty(login_html)){
			throw new Exception("获取登录页html失败");
		}
		Document doc_login = Jsoup.parse(login_html);
		Elements eles2 = doc_login.getElementsByTag("input");
		List<String> keys = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		for (Element ele : eles2) {
			String name = ele.attr("name");
			keys.add(name);
			String value = ele.attr("value");
			map.put(name, value);
		}
		//1.4 获取验证码
		Elements imgs = doc_login.getElementsByTag("img");
		String captcha_url = null;
		for (Element img : imgs) {
			String style = img.attr("style");
			if(style == null || style.isEmpty()){
				captcha_url = img.attr("src");
				break;
			}
		}
		if(!StringUtils.isUrl(captcha_url)){
			throw new Exception("验证码URL格式错误");
		}
		this.downloadImage(captcha_url, filename);
		System.out.println("请输入图片中的字符:");
		Scanner scan = new Scanner(System.in);
		String captcha = scan.nextLine();
		scan.close();
		System.out.println("验证码输入成功");
		//1.5 获取登录post请求地址
		Elements eles3 = doc_login.getElementsByTag("form");
		String uri = null;
		for (Element ele : eles3) {
			String action = ele.attr("action");
			if(action == null || action.isEmpty()){
				continue;
			}else{
				uri = action;
				break;
			}
		}
		//完整的请求地址
		uri = "https://login.weibo.cn/login/" + uri;
		//2.发送登录请求
		HttpPost post = new HttpPost(uri);//使用post请求
		List<NameValuePair> list = new ArrayList<NameValuePair>();//参数列表
		for (String key : keys) {
			if(key.contains("code")){
				list.add(new BasicNameValuePair(key, captcha));
			}else if(key.contains("password")){
				list.add(new BasicNameValuePair(key, password));
			}else if(key.contains("mobile")){
				list.add(new BasicNameValuePair(key, username));
			}else{
				list.add(new BasicNameValuePair(key, map.get(key)));
			}
		}
		HttpEntity entity = new UrlEncodedFormEntity(list);
		post.setEntity(entity);
		client.execute(post);
		post.releaseConnection();
		logger.info("结束login(username,password,filename)...");
	}
	
	/**
	 * 解析一条微博的div标签，获取微博信息
	 * @param div 一条微博所在的div标
	 * @throws Exception 
	 */
	public Blog crawlBlog(Element div) throws Exception{
		logger.info("开始crawlBlog(div)...");
		//1、解析微博正文及位置，都在同一个span标签下
		Elements ctt = div.getElementsByClass("ctt");
		String blog_text = null;//微博正文
		String place = null;//位置
		for (Element text : ctt) {
			blog_text = text.ownText();
			Elements as = text.getElementsByTag("a");
			for (Element a : as) {
				place = a.ownText();
			}
			if(!StringUtils.isEmpty(blog_text)){
				break;
			}
		}
		blog_text = blog_text.replaceAll("我在这里:", "").replaceAll("我在:", "");//切割一下老版本的微博显示位置的时候多余的几个字
		if(StringUtils.isEmpty(blog_text)){
			blog_text = "";
		}
		//2、经纬度
		String bl = null;//经纬度
		Elements showmap = div.getElementsContainingOwnText("显示地图");
		for (Element sm : showmap) {
			bl = sm.attr("href");
		}
		if(!StringUtils.isEmpty(bl)){//判断是否有定位信息
			if(bl.startsWith("http://place.weibo.com/imgmap")){
				bl = bl.substring(bl.indexOf("center"));
				String[] str1 = bl.split("&");
				if(!StringUtils.isEmpty(str1[0])){
					String[] str2 = str1[0].split("=");
					bl = str2[1];
				}
			}
		}
		if(StringUtils.isEmpty(bl)){
			bl = "";
		}
		//3、图片信息
		String img_num = null;
		Elements imgs = div.getElementsContainingOwnText("原图");
		for (Element img : imgs) {
			if("a".equals(img.tagName())){
				img_num = "1";
			}
		}
		Elements imgAll = div.getElementsMatchingOwnText("[组][图][共][0-9][张]");
		for (Element img : imgAll) {
			if("a".equals(img.tagName())){
				String num = img.ownText();
				img_num = num.substring(3, 4);
			}
		}
		if(StringUtils.isEmpty(img_num)){
			img_num = "0";
		}
		//TODO 获取微博所有图片
		
		//4、获取赞、转发、评论
		String like_num = null;//赞
		String comment_num = null;//评论
		String repeat_num = null;//转发
		Elements interactions = div.getElementsMatchingOwnText("[评转]?[赞发论]\\[[\\d]+\\]");
		for (Element ia : interactions) {
			String ownText = ia.ownText();
			if(StringUtils.isEmpty(ownText)){
				throw new Exception("获取互动信息失败1");
			}else if(ownText.startsWith("赞")){
				like_num = ownText.replaceAll("赞", "").replaceAll("\\[", "").replaceAll("\\]", "");
			}else if(ownText.startsWith("评论")){
				comment_num = ownText.replaceAll("评论", "").replaceAll("\\[", "").replaceAll("\\]", "");
			}else if(ownText.startsWith("转发")){
				repeat_num = ownText.replaceAll("转发", "").replaceAll("\\[", "").replaceAll("\\]", "");
			}else{
				throw new Exception("获取互动信息失败2");
			}
		}
		if (StringUtils.isEmpty(like_num)) {
			like_num = "0";
		}
		if (StringUtils.isEmpty(comment_num)) {
			comment_num = "0";
		}
		if (StringUtils.isEmpty(repeat_num)) {
			repeat_num = "0";
		}
		//5、获取时间和设备
		String create_date = null;//2013-07-29 21:17 来自
		String device = null;
		Elements cts = div.getElementsByClass("ct");
		for (Element ct : cts) {
			String ctStr = ct.text();
			String[] s = ctStr.split("来自");
			create_date = s[0].trim();
			device = s[1].trim();//有风险，有些微博没有显示“来自”的设备，导致这里数组可能没有[1]
		}
		//处理create_date，风险，在时、分上会有误差，与微博时间不符，cal对象的创建应该放在获取到页面的时候，这样误差会小一点。
		//风险比较大的是若在23：59分采集数据，则有可能会有一天的误差
		//今天、今年的微博无法获取到秒针的时刻
		if(create_date.contains("前")){//24分钟前
			create_date = create_date.replaceAll("分钟", "").replaceAll("前", "").trim();
			Calendar cal = GregorianCalendar.getInstance();
			cal.set(Calendar.MINUTE, Integer.valueOf("-"+create_date));
			create_date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.getTime());
		}else if(create_date.contains("今")){//今天 12:30
			create_date = create_date.replaceAll("今天", "").trim();
			Calendar cal = GregorianCalendar.getInstance();
			create_date = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime()) + create_date;
		}else if(create_date.contains("月")){//01月03日 09:42
			create_date = create_date.replaceAll("月", "-").replaceAll("日", "").trim();
			System.out.println("***"+create_date+"***");
			Calendar cal = GregorianCalendar.getInstance();
			create_date = new SimpleDateFormat("yyyy-").format(cal.getTime()) + create_date;
			System.out.println("***"+create_date+"***");
			Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(create_date);
			create_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
			System.out.println("***"+create_date+"***");
		}
		if (StringUtils.isEmpty(device)) {
			device = "";
		}
		//6、此微博div的id
		String blog_id = div.id();
		System.out.println("**********************插入此条微博的id为："+blog_id);
		//封装成Blog对象
		Blog blog = new Blog(blog_id, blog_text, device, place, bl, 
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(create_date), 
				Integer.valueOf(img_num), Integer.valueOf(like_num), 
				Integer.valueOf(repeat_num), Integer.valueOf(comment_num));
		logger.info("结束crawlBlog(div)...");
		return blog;
	}
	
	/**
	 * 获取html页面的下一页URL，这个URL是一个相对路径，需要加上前缀http://weibo.cn
	 * @param html
	 * @return
	 */
	public String nextPage(String html){
		logger.info("开始nextPage(html)...");
		Document doc = Jsoup.parse(html);
		Element ele = doc.getElementById("pagelist");//翻页按钮所在的div
		Elements nexts = ele.getElementsMatchingOwnText("下页");
		String nextUrl = null;
		for (Element e : nexts) {
			nextUrl = e.attr("href");
		}
		logger.info("结束nextPage(html)...");
		return nextUrl;
	}
	
}
