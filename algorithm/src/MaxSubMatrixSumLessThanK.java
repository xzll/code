/**
 * 给定一个无序矩阵，其中有正，有负，有0，再给定一个值k，求累加和小于等于k的最大子矩阵大小，矩阵的大小用其中的元素个数来表示。
 * 算法原型：有正负0的数组中，找出累加和小于等于k的最大子数组长度
 * 先计算出0到每个位置的累加和，记为数组sum
 * 因为sum[i]+sum[i+1 ~ j]=sum[j]>k，所以只要sum[i]>=sum[j]-k，那么一定有sum[i+1~j]<=k（sum[j]>k）
 * 就是只要找出0到当前位置j中第一个累加和大于sum[j]-k的位置就可以了，这个位置到j的距离就是累加和小于等于k的子数组长度。
 * 如果sum[j]<=k,那么0到j就是累加和小于等于k的子数组长度
 * 
 * 优化，用一个数组记录区域最大累加和，然后用二分查找在这个数组中找第一个累加和大于sum[j]-k的位置。
 * @author YJ
 *
 */
public class MaxSubMatrixSumLessThanK {
	public int get(int[][] arr,int k){
		if(arr==null || arr.length==0){
			return 0;
		}
		int maxLength = 0;
		for(int i=0;i<arr.length;i++){
			int[] folk=new int[arr[0].length];//矩阵部分折叠后的数组
			for(int j=i;j<arr.length;j++){//行
				int[] sum = new int[arr[j].length];//数组累加和
				int[] inc = new int[arr[j].length];//数组区域最大累加和
				folk[0]+=arr[j][0];
				sum[0] = folk[0]; 
				inc[0] = sum[0];
				for(int x=1;x<arr[j].length;x++){//列
					folk[x]+=arr[j][x];
					sum[x] = sum[x-1]+folk[x];
					inc[x] = sum[x]>inc[x-1]?sum[x]:inc[x-1];
 					int length = 0;
					if(sum[x]<=k){
						length = x+1;
					}else{
						length = x-search(inc,x,sum[x]-k);
					}
					length *= (j-i+1);//长度乘以折叠的行数
					maxLength = maxLength<length?length:maxLength;
				}
			}
		}
		
		return maxLength;
		
	}
	private int search(int[] inc,int end,int rk){
		int l=0;
		int r=end;
		while(l<r){//找第一个大于等于rk的数
			int mid = (r>>1)-(l>>1);
			if(inc[mid]>=rk){
				r=mid;
			}else{
//				l=mid;//数组中没有大于等于rk的数的话会陷入死循环
				l=mid+1;
			}//l==r时跳出循环
		}
		return l;
	}
	public static void main(String[] args) {
		int[][] a = { { 1, 3 }, 
				{ -3, 2 }, 
				{ 4, 2 } };
		System.out.println(new MaxSubMatrixSumLessThanK().get(a, 4));
	}
}
