package indi.smt.crawler;

import indi.smt.business.DoCrawl;

import org.junit.Before;
import org.junit.Test;

public class Test3 {
	
	private DoCrawl crl;
	
	public void setCrl(DoCrawl crl) {
		this.crl = crl;
	}

	public DoCrawl getCrl() {
		return crl;
	}
	
	@Before
	public void before() throws Exception{
		setCrl(new DoCrawl());
	}
	
	@Test
	public void demo1() throws Exception{
		crl.init("resource/capatch.jpg");
		crl.getAllBlogs("http://weibo.cn/bapiwang");
	}
	
	@Test
	public void demo2() throws Exception{
		crl.init("resource/capatch.jpg");
		String p128 = crl.getThePageBlog("http://weibo.cn/314268372?filter=1&page=128");
		System.out.println("p128--->" +p128);
		String p129 = crl.getThePageBlog("http://weibo.cn/314268372?filter=1&page=129");
		System.out.println("p129--->" +p129);
	}

}
