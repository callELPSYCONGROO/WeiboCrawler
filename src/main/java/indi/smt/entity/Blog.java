package indi.smt.entity;

import java.util.Date;

public class Blog {
	
	@Override
	public String toString() {
		return "Blog [bl=" + bl + ", blog_id=" + blog_id + ", blog_text="
				+ blog_text + ", \ncomment_num=" + comment_num + ", create_date="
				+ create_date + ", device=" + device + ", \nimg_num=" + img_num
				+ ", like_num=" + like_num + ", place=" + place
				+ ", \nrepeat_num=" + repeat_num + "]";
	}
	private String blog_id;
	private String blog_text;
	private String device;
	private String place;
	private String bl;
	private Date create_date; 
	private Integer img_num;
	private Integer like_num;
	private Integer repeat_num;
	private Integer comment_num;
	
	
	
	public Blog(String blogId, String blogText, String device, String place,
			String bl, Date createDate, Integer imgNum, Integer likeNum,
			Integer repeatNum, Integer commentNum) {
		super();
		this.blog_id = blogId;
		this.blog_text = blogText;
		this.device = device;
		this.place = place;
		this.bl = bl;
		this.create_date = createDate;
		this.img_num = imgNum;
		this.like_num = likeNum;
		this.repeat_num = repeatNum;
		this.comment_num = commentNum;
	}
	
	public Blog(){}
	
	public String getBlog_id() {
		return blog_id;
	}
	public void setBlog_id(String blogId) {
		blog_id = blogId;
	}
	public String getBlog_text() {
		return blog_text;
	}
	public void setBlog_text(String blogText) {
		blog_text = blogText;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getBl() {
		return bl;
	}
	public void setBl(String bl) {
		this.bl = bl;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date createDate) {
		create_date = createDate;
	}
	public Integer getImg_num() {
		return img_num;
	}
	public void setImg_num(Integer imgNum) {
		img_num = imgNum;
	}
	public Integer getLike_num() {
		return like_num;
	}
	public void setLike_num(Integer likeNum) {
		like_num = likeNum;
	}
	public Integer getRepeat_num() {
		return repeat_num;
	}
	public void setRepeat_num(Integer repeatNum) {
		repeat_num = repeatNum;
	}
	public Integer getComment_num() {
		return comment_num;
	}
	public void setComment_num(Integer commentNum) {
		comment_num = commentNum;
	}
}
