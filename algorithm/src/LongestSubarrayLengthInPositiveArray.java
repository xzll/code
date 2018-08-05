/**
 * 累加和等于指定值的最长子数组长度
 * @author YJ
 *
 */
public class LongestSubarrayLengthInPositiveArray {
	
	public static int getMaxLength(int[] arr,int k){
		if (arr == null || arr.length==0 || k<0) {
			return 0;
		}
		int l = 0;
		int r = 0;
		int sum = arr[0];//l，r所围的范围的数的累加和，包括l、r
		int len = 0;
		while (r < arr.length) {//每次循环不是r走一步就是l走一步，所以最多2n复杂度。
			//sum大于等于k，指针l前进
			//sum小于k，指针r前进
			if (sum == k) {
				len = Math.max(len, r-l+1);
				sum -= arr[l++];//有可能l走到r右边，l走到r右边一定是因为该数大于k，减掉后sum就是0了，		
			} else if (sum < k) {
				//下一个循环一定在这，r会前进一步,所以没关系。
				r++;
				if (r == arr.length) {
					break;
				}
				sum += arr[r];
			} else {
				sum -= arr[l++];
			}
		}
		return len;
	}
	public static void main(String[] args) {
		int[] arr = {5,2,3,0,2};
		int k = 4;
		System.out.println(getMaxLength(arr, k));
	}
}
