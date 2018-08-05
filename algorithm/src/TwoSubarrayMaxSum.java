/**
 * 求数组中两不重合子数组能得到的最大和<br>
 * 算法原型：子数组最大和<br>
 * <br>
 * O(n)求子数组最大和，<br>
 * 只需一个变量记录最大和，<br>
 * 一个变量记录累加值，每次更改都更新最大和变量。<br>
 * 如果累加值小于0就将它归0，因为如果和为负数或0，它**前面的值**就没有添加进子数组中的必要了<br>
 * <br>
 * 求两个不重合子数组的最大值的话，可以先求 0到i 和 i到n-1 的最大子数组<br>
 * 用两辅助数组分别记录从0到i的子数组最大和，和从i到n-1的子数组的最大和<br>
 * 然后计算l[i]+l[i+1]相加出来的最大和<br>
 * 再优化可以只用一个辅助数组
 * @author YJ
 *
 */
public class TwoSubarrayMaxSum {
	/**
	 * 可以只用一个辅助数组
	 * @param arr
	 * @return
	 */
	public static int getMax(int[] arr){
		if(arr==null || arr.length<2) {
			return 0;
		}
		int n = arr.length;
		int[] l = new int[n];
//		int max = arr[0]; //max被l数组代劳了，不需要再用它了
		int cur = arr[0];
		l[0] = arr[0];
		for(int i=1;i<n;i++){
			if(cur<0){//如果累加值小于0，归0
				cur=0;
			}
			cur += arr[i];
			l[i] = Math.max(cur, l[i-1]);
//			cur += arr[i];
//			max = Math.max(cur, max);
//			if(cur<0){//如果累加值小于0，归0
//				cur=0;
//			}
//			l[i] = max;
		}
		int rmax = arr[n-1];
		int max = rmax+l[n-2];
		cur = arr[n-1];
		for(int i=n-2;i>0;i--){
			if(cur<0) {
				cur=0;
			}
			cur += arr[i];
			rmax = Math.max(cur, rmax);
			max = Math.max(max, rmax+l[i-1]);
		}
		return max;
	}
	public static void main(String[] args){
//		int[] arr={1,3,4,-1,2,3,-6,9,3};
		int[] arr={-6,-3,-4,-1,-2,-3,-6,-9,-3};
		System.out.println(getMax(arr));
	}
}
