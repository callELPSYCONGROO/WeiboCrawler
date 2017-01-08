import indi.smt.business.DoCrawl;

public class Start {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		try {
			DoCrawl doCrawl = new DoCrawl();
			doCrawl.init("resource/capatch.jpg");
			doCrawl.getAllBlogs("http://weibo.cn/314268372");
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		long time = end - start;
		long day = time/(1000*60*60*24);
		time -= day*(1000*60*60*24);
		long hour = time/(1000*60*60);
		time -= hour*(1000*60*60);
		long miute = time/(1000*60);
		time -= hour*(1000*60);
		long second = time/(1000);
		time -= second*1000;
		String str = "共耗时：";
		if(day != 0){
			str += day + "天";
		}
		if(hour != 0){
			str += hour + "时";
		}
		if(miute != 0){
			str += miute + "分";
		}
		if(second != 0){
			str += second + "秒";
		}
		if(time != 0){
			str += time + "毫秒";
		}
		System.out.println(str);
	}
}
