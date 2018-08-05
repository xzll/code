
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 排序，a升序b降序<br>
 * 最长递增子序列<br>
 * 动态规划<br>
 * 辅助数组记录i长度递增子序列的最小末尾<br>
 * 二分查找第一个大于当前元素的元素，取代它，没有就放在末尾
 * 
 * @author YJ
 *
 */
public class RussianDollEnvelopes_354 {
	class Envelope{
		private int width;
		private int height;	
		public Envelope(int width,int height) {
			this.width = width;
			this.height = height;
		}
	}
	class EnsComparator implements Comparator<Envelope>{

		@Override
		public int compare(Envelope o1, Envelope o2) {
			if(o1.width==o2.width){
				if(o1.height>o2.height){//降序
					return -1;
				}else if(o1.height<o2.height){
					return 1;
				}else {
					return 0;
				}
			}else if(o1.width<o2.width) {//升序
				return -1;
			}else{
				return 1;
			}
		}
		
	}


	public int maxEnvelopes(int[][] envelopes){
		if(envelopes==null || envelopes.length==0 ) return 0;
		Envelope[] ens = new Envelope[envelopes.length];
		for(int i=0;i<envelopes.length;i++){
			ens[i]=new Envelope(envelopes[i][0],envelopes[i][1]);
		}
		Arrays.sort(ens, new EnsComparator());//排序。归并？
		//辅助数组
		int[] ends = new int[envelopes.length];
		ends[0]=ens[0].height;
		int right = 0;
		for(int i=1;i<ens.length;i++){
			//二分查找辅助数组中第一个大于h的元素
			int h = ens[i].height;
			int l=0;
			int r=right;
			while(l<=r){//不是找相等的值所以会一直循环到只剩一个值
				int m=(l+r)/2;
				if(ends[m]<h){//找到最后只剩下一个值，如果是小于h的，l++，扩充了一位
					l=m+1;
				}else{//如果最后一个值大于等于h，就说明找到了第一个大于等于h的值，覆盖它
					r=m-1;
				}
			}//所以最后l指向的就是h的位置
			right=Math.max(right, l);//更新长度.Math.max()内部是 ？：
			ends[l]= h;
			
		}
		return right+1;
		
	}
	public static void main(String[] args) {
		int[][] a = {{5,4},{6,4},{6,7},{2,3}};
		new RussianDollEnvelopes_354().maxEnvelopes(a);
	}
	/**
	 * 为了熟悉arraylist，辅助数组类型是arraylist
	 * @param envelopes
	 * @return
	 */
	public int maxEnvelopes2(int[][] envelopes){
		if(envelopes==null || envelopes.length==0 ) return 0;
		Envelope[] ens = new Envelope[envelopes.length];
		for(int i=0;i<envelopes.length;i++){
			ens[i]=new Envelope(envelopes[i][0],envelopes[i][1]);
		}
		Arrays.sort(ens, new EnsComparator());//排序。归并？
		//辅助数组
		ArrayList<Integer> ends = new ArrayList<Integer>();
		ends.add(ens[0].height);
		for(int i=1;i<ens.length;i++){
			//二分查找辅助数组中第一个大于h的元素
			int h = ens[i].height;
//			int s = 0;
//			int e = ends.size()-1;
//			int m = (s+e)/2;
//			while()
//			if(ends.get(m)>=h){//大于
//				if(m==0 || ends.get(m-1)<h){//找到
//					//取代
//					ends.add(m, h);
//				}else{//在左半部分找
//					e=m-1;
//				}
//			}else{//小于，在右半部分找
//				s=m+1;
//			}
			int l=0;
			int r=ends.size()-1;
			while(l<=r){//不是找相等的值所以会一直循环到只剩一个值
				int m=(l+r)/2;
				if(ends.get(m)<h){//找到最后只剩下一个值，如果是小于h的，l++，扩充了一位
					l=m+1;
				}else{//如果最后一个值大于等于h，就说明找到了第一个大于等于h的值，覆盖它
					r=m-1;
				}
			}//所以最后l指向的就是h的位置
            if(l==ends.size()) ends.add(l,h);
            else ends.set(l, h);
			
		}
		return ends.size();
		
	}
}
