import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceWords {
	private static Logger log = Logger.getLogger(ReplaceWords.class.toString());

	public static void main(String[] args) {	
		replace("test.txt","target.txt");
	}
	/**
	 * 字符转为unicode 16进制
	 * @param c
	 * @return
	 */
	public static String charToHexStr(char c){
		String unicode = Integer.toHexString(c);
		log.info(c+" :"+unicode);
		return unicode;
	}
	/**
	 * 读文件
	 * @param name
	 * @return
	 */
	public static String readFile(String name){
		FileReader fr = null;
		StringBuilder sb = new StringBuilder();;
		try{
			File file = new File(name); 
			if(!file.exists()){
				log.info("文件不存在");
				return null;
			}
			fr = new FileReader(file);
			char[] buf = new char[1024];
			int len = 0;
			while((len = fr.read(buf)) != -1){
				sb.append(new String(buf,0,len));
			}
			return sb.toString();//读文件
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(fr!=null){
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * 写文件
	 * @param content
	 * @param name
	 */
	public static void writeFile(String content,String name){
		FileWriter fw = null;
		try{
			File file = new File(name);
			if(!file.exists()){
				file.createNewFile();
			}
			fw = new FileWriter(file);
			fw.write(content);//写文件
			fw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(fw!=null){
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 正则匹配并进行替换
	 */
	public static void replace(String sourceFileName,String targetFileName){
		String str = readFile(sourceFileName);
		if(str==null||str.isEmpty()){ 
			log.info("文件为空或不存在");
			return;
		}
		StringBuilder sb = new StringBuilder();
		String reg = "[\u7b2c][0-9]*[\u7ae0]";//
		Matcher m = Pattern.compile(reg).matcher(str);
		int start = 0;
		while(m.find()){
			log.info(m.group());//m.group() 是匹配到的字符串
			log.info(m.start()+"..."+m.end()); //m.start()开始位置，m.end()结束的位置，不包括它
			String t = m.group();
			t = "诶嘿"+t.substring(2, t.length()-1)+"哈";//替换
			sb.append(str.substring(start, m.start())).append(t);
			start = m.end();
		}
		if(start != str.length()) {
			sb.append(str.substring(start));
		}
		
		writeFile(sb.toString(),targetFileName);
		
	}
	
}
