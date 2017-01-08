package indi.smt.dao;

import java.util.List;

import indi.smt.entity.Blog;

public interface BlogDAO {
	
	public List<Blog> selectBlog();
	
	public Blog selectBlogById(String blog_id);
	
	public void insertBlog(Blog blog);
	
}
