import java.util.HashMap;

/**
 * 给定一个字符串类型的数组，其中不含有重复的字符串，如果其中某一个字符串是另一个字符串的前缀，返回true；如果没有任何一个字符串是另一个字符串的前缀，返回false。
 * 字符在边上，边是hashmap的key，对应的value是下一个点
 * 是前缀有两种情况：
 * 1. 当前字符串是之前字符串的前缀，递归完还在树中
 * 2. 之前字符串是当前字符串的前缀，递归时遇到其他字符串的中止点
 * @author YJ
 *
 */
public class Tries {
	private HashMap<Character,Tries> children = new HashMap<Character,Tries>();
	private boolean end = false;//当前点是否是一个字符串的终止。
	
	public boolean addAndCheck(char[] chs,int i) {//i是下标，递归用
		if(end) {//如果遇到其他字符串的中止点了，之前字符串是当前字符串的前缀，返回true
			return true;
		}
		if(i==chs.length) {//如果数组元素递归完了
			end = true;//将当前结点的end设为true，表明这个点是一个字符串的终止点
			return !children.isEmpty();//如果下一条边没有值，就说明当前字符串不是之前字符串的前缀，或者当前字符串与其中一个字符串相同，但是规定不含有重复的字符串，所以这个可能pass
		}
		if(!children.containsKey(chs[i])) {//如果当前结点的所有边上都不是该值
			children.put(chs[i], new Tries());//增加新的边
		}
		return children.get(chs[i]).addAndCheck(chs, i+1);//递归
		
	}
	public static void main(String[] args) {
		Tries root = new Tries();
		System.out.println(root.addAndCheck(new char[]{'a','b','c','d','e'}, 0));
		System.out.println(root.addAndCheck(new char[]{'a','b','c'}, 0));
		System.out.println(root.addAndCheck(new char[]{'b','c'}, 0));
	}
}
