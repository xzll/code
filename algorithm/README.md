[左程云牛课堂](https://www.nowcoder.com/live/11/1/1)  
  
笔记  
> 1.给定一个N\*2的二维数组，看作是一个个二元组，例如[[a1,b1],[a2,b2],[a3,b3]]，规定：一个如果想把二元组甲放在二元组乙上，甲中的a值必须大于乙中的a值，甲中的b值必须大于乙中的b值。如果在二维数组中随意选择二元组，请问二元组最多可以往上摞几个？  
例如：[[5,4],[6,4],[6,7],[2,3]],最大数量可以摞3个，[2,3]=>[5,4]=>[6,7]  
要求：实现时间复杂度O(N\*logN)的解法  
  

题目要求实际上跟leetcode354俄罗斯套娃是一样的。   
 
  
不考虑时间复杂度，最先想到排序，先根据a升序排序a相同根据b排序。  
诶~问题来了，根据a再根据b排序后的序列要怎么知道最多能摞多少呢？  
如：（1，2），（2，7），（3，4），（3，7），（4，5），这个序列是根据该规则排序的那么怎么知道最多能摞多少个二元组？  
（1，2），（2，7） 2个  
（1，2），（3，4），（3，7）3个  
（1，2），（3，4），（4，5）3个  
> 算法原型：最长递增子序列，它的长度就是要的值  
  
#### O(n^2)解法：  
  
用一个辅助数组dp记录**以当前元素为子序列末尾 的 子序列的最大长度**。  
（1，2），（2，7），（3，4），（3，7），（4，5）排好序的二元组  
1   dp[]  
1，2  
1，2，2  
1，2，2，3  
1，2，2，3，3  
时间复杂度为O(n^2)，因为对于每个元素，都要遍历之前的元素，找出dp[i]最大的小于当前元素的元素。  
  
#### O(nlogn)的解法  
给辅助数组换一种意义，记录长度为i的递增子序列的**最小末尾**。  
找到**第一个大于当前的数**，有就替换，没有就放在末尾。（二分查找）  
相同呢？  
每个数都会是最小末尾只不过可能会被后来的数覆盖。  
3，5，1，4，8，6  
3  
3，5  
1，5  
1，4   
1，4，8   
1，4，6   
然后对于二元组，排列规则就出问题了，**怎么确定那个元素大呢？**  
如（2，7）（3，4）哪个小哪个大？  
如果只根据a判断的话（3，4）大  
a相同判断b，取b小的  
（1，2），（2，7），（3，4），（3，5），（4，5）-----新的序列  
（1，2），  
（1，2），（2，7）  
（1，2），（2，7），（3，4）  
（1，2），（2，7），（3，4）  
（1，2），（2，7），（3，4），（4，5）  
如果只根据b判断的话（2，7）大，（1，5）也比（3，4）大，  
a相同判断b，取b小的  
会变成这样 👇，好像没什么毛病  
（1，5），（2，7），（3，4），（3，6），（4，7）---- 新的序列  
（1，5），  
（1，5），（2，7）  
（3，4），（2，7）  
（3，4），（3，6）  
（3，4），（3，6），（4，7）  
  
  
按照**a升序，同a则b降序**来排序，并且只根据b来找出最大递增子序列就不会出各种各样的问题。  
b降序可以确保后面b大于前面的话就一定可以叠加起来。否则如（6，4）（6，7）后面的是不能叠在前面之上的。  
  
因为当前元素和前面的元素比较的话有两种情况，  
1：当前a与前面a相同，则b不可能比前面大，所以如果碰到了会取代它成为最小末尾，没碰到就当另一个子序列的最小末尾；  
2：当前a与前面不同，则当前a一定是大于前面a的，所以b大则元素大。  
因此可以直接比较b。  
  
感觉不降序也没事，只是需要在碰到相同a的时候取小b的元素。咦，这样子每次比较就都得判断a不能只把b拿出来判断了。降序666  
  
（1，2），（2，7），（3，5），（3，4），（4，6）  
2，7，5，4，6 ----b  
2，  
2，7  
2，5  
2，4  
2，4，6 ----（1，2），（3，4），（4，6）  
  
（2，5），（3，6），（3，4）  
5，6，4  
5  
5，6  
4，6  -----（3，4），（3，6）  
  
（2，3），（3，6），（3，4）  
3，6，4  
3  
3，6  
3，4  
（2，5），（3，5），（3，6）  
5  
5 （2，5）b相同的话比较a取小的  
5，6  
  
  
```java

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
				}else{//如果最后一个值大于等于h，就说明找到了第一个大于等于h的值，就是l所在位置
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

``` 
  


> 2.给定一个非负数的数组，代表一个容器。例如数组[0,1,0,2,1,0,1,3,2,1,2,1]，就是以下图形中黑色的部分。如果用这个容器接水的话，请问可以接多少水？还以这个数组为例，可以接6格水，就是以下图形中蓝色的部分。  
要求：实现时间复杂度O(N)，额外空间复杂度O(1)的解法  
  

  
首先想到从做到右减和加，遇到大值记录大值，但是有缺陷就是右边没有大值了怎么办水都漏了。  
然后想到从右到左减和加，emmm，一样啊，左边没大值水会流掉。  
既然这样的话那就先找到最大值。。然后呢，思路断了。其实可以找到最大值然后分别递归往左往右找第二大的值，找到后就不怕水漏了。不过没想出来这个方法，而且这个方法是O(n2)貌似很复杂。  
最后想到从底往上计算，每次把所有数组都减一，然后用两个指针从头尾大于等于0的地方开始计算小于0的元素的数量，直到指针相撞即所有元素都小于0。这些计算出来的数量累加起来就是结果了。假设最大值是m则O(mn)，需要遍历m次。m超大的时候就gg了。  
![](https://upload-images.jianshu.io/upload_images/9942378-8c6003576ef8757a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
上面的这些想法都是局限于题目给的规则了，其实可以想想怎么修改规则，改成便于自己计算的规则。  
  
>**拿到题先看能不能改规则，改成便于自己计算的规则。**  
  
怎么改？？随缘  
  
从找峰值改为-->看在i位置上能放多少水。  
**在i位置左右两边找最大值，取两个最大值的小值，减去arr[i]就是位置i能放的水量了。如果该值小于等于arr[i]，那么位置i不能储水,储水量为0。**  
  
将遍历找最大值改为O(1)的方法：  
1. 需要两个数组l、r分别存放从左边到当前位置的最大值和从右边到当前位置的最大值，这样arr[i]的左边最大值是l[i],右边最大值是r[i]。  
2. 用一个变量代替数组l，因为i遍历的时候就能够知道左边到i的最大值了。  
  
**不用辅助空间**的方法，设两指针l,r，l指向头部往右移动，r指向尾部往左移动。设两变量lmax，rmax分别记录l遍历过的值的最大值和r遍历过的最大值。  
  - ~~选择l r指向的下一个值中的小值，记为x~~。两指针指向的下一个值，选择lmax和rmax较小的一边的那个（最低瓶颈的一边的值能先得出储水量），记为x  
  - 让x跟min(lmax,rmax)比较，小于的话相减后的值是该位置的储水量，否则就是不能储水，x是左值更新lmax，右值更新rmax。  
  - x是左值指针l向右移一位，是右值的话r向左移一位。  
```java  
    public int trap(int[] height) {  
    	if(height==null || height.length==0) return 0;  
    	int l = 0;  
    	int r = height.length-1;  
    	int lmax = height[0];  
    	int rmax = height[r];  
    	int sum = 0;  
    	while(l<r-1){//两指针相邻就可以跳出循环了  
    		//选择两值较小的那一边  
    		if(lmax<rmax){  
    			sum += Math.max(0, lmax-height[++l]);  
    			lmax = Math.max(lmax, height[l]);  
    		}else{  
    			sum += Math.max(0, rmax-height[--r]);  
    			rmax = Math.max(rmax,height[r]);  
    		}  
    	}  
        return sum;  
    }  
```  
在leetcode上看到更简单容易理解的方法：  
- 先找出最大值的位置，遍历一次  
- 从左开始遍历到最大值的位置，lmax记录沿途的最大值，arr[i]与lmax相减就能得到位置i上的储水量。大于lmax就更新arr[i]。  
- 然后从右开始遍历到最大值的位置，rmax记录沿途的最大值，arr[i]与rmax相减就能得到位置i上的储水量。大于rmax就更新arr[i]。  
其实意思都一样都是**先计算能确定瓶颈的数值**。  

```java  
   public int trap(int[] height) {  
    	if(height==null || height.length==0) return 0;  
    	int maxIndex = 0;  
    	int max = height[0];  
    	for(int i=1;i<height.length;i++){  
    		if(height[i]>max){  
    			max = height[i];  
    			maxIndex = i;  
    		}  
    	}  
    	int sum = 0;  
    	int lmax = height[0];  
    	for(int i=1;i<maxIndex;i++){  
    		if(height[i]>lmax){  
    			lmax = height[i];  
    		}else{  
    			sum += lmax - height[i];  
    		}  
    	}  
    	int rmax = height[height.length-1];  
    	for(int i = height.length-2;i>maxIndex;i--){  
    		if(height[i]>rmax){  
    			rmax = height[i];  
    		}else{  
    			sum += rmax - height[i];  
    		}  
    	}  
    	return sum;  
    }  
```

> 3.给定一个非负数的数组，数组中的每个值代表一个柱子的高度，柱子的宽度是1。两个柱子之间可以围成一个面积，规定：面积＝两根柱子的最小值\*两根柱子之间的距离。比如数组[3,4,2,5]。3和4之间围成的面积为0，因为两个柱子是相邻的，中间没有距离。3和2之间围成的面积为2，因为两个柱子的距离为1，且2是最短的柱子，所以面积＝1\*2。
3和5之间围成的面积为6，因为两个柱子的距离为2，且3是最短的柱子，所以面积＝3\*2。求在一个数组中，哪两个柱子围成的面积最大，并返回值。  
要求：实现时间复杂度O(N)，额外空间复杂度O(1)的解法  
  
这个比[Trapping Rain Water]简单（但是我还是没想出来），两头指针，一个变量记录最大值，不纠结怎么找到，确保去掉没有意义的组合就行了。  

l，r分别指向头尾，相乘后更新最大值，然后移动较小值的指针。  
因为两个指针指向的值中较小的那个值是瓶颈，所以移动较大值指针没有意义。  
移动大值指针的话，移动后面积中的高度一定是小于等于瓶颈的而且距离又缩短了，最后相乘出来的面积一定比之前的小。  
所以要移动指向较小值的那个指针。  
  
```java
/**
 * 双头指针，一个变量记录最大值
 * @author YJ
 *
 */
public class ContainerWithMostWater_11 {
   public int maxArea(int[] height) {
        if(height==null || height.length==0) return 0;
        int l = 0;
        int r = height.length-1;
        int max = 0;
        while(l<r){
            int t = Math.min(height[l],height[r]);//小的一边为瓶颈，取瓶颈为高
            max = Math.max(max,t*(r-l));
            if(height[l]>height[r]){//移动小的一边的指针
                r--;
            }else{
                l++;
            }
        }
        return max;
    }
}

```
> 4.求两个子数组最大的累加和  
【题目】  
给定一个数组，其中当然有很多的子数组，在所有两个子数组的组合中，找到相加和最大的一组，要求两个子数组无重合的部分。最后返回累加和。  
【要求】  
时间复杂度达到O(N)  
  
```java
/**
 * 求数组中两不重合子数组能得到的最大和<br>
 * 算法原型：子数组最大和<br>
 * <br>
 * O(n)求子数组最大和，<br>
 * 只需一个变量记录最大和，<br>
 * 一个变量记录累加值，每次更改都更新最大和变量。<br>
 * 如果累加值小于0就将它归0，因为如果和为负数或0，它前面的值就没有添加进子数组中的必要了<br>
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
		int[] arr={1,3,4,-1,2,3,-6,9,3};
		System.out.println(getMax(arr));
	}
}  
```  

> 5.未排序正数数组中累加和为给定值的最长子数组长度  
【题目】  
给定一个数组arr，该数组无序，但每个值均为正数，再给定一个正数k。求arr的所有子数组中所有元素相加和为k的最长子数组长度。  
例如，arr=[1,2,1,1,1]，k=3。  
累加和为3的最长子数组为[1,1,1]，所以结果返回3。  
【要求】  
时间复杂度O(N)，额外空间复杂度O(1)  
  

只想到用一个指针s指向开始，遍历数组，cur记录s到当前位置的累加和，等于k的话记录长度，大于的话移动指针s，直到cur小于k。  
   
解法跟我想的差不多，不过更加清晰明了。可以想象成是一个滑动窗口。  
双指针，l，r中间的数累加和记为sum，sum大于k的话移动l，等于k的话 ~~两个指针都移动~~ 移动l，记录长度，小于k的话移动r。直到r到边界。因为是正数，l，r相同时sum为0，l不可能在前进一步，所以l不可能会在r右边。（实际代码是l==r-1时sum为0）  
```java
/**
 * 规定数组元素都是正数
 * 求累加和等于指定值的最长子数组长度，
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
```
> 6.未排序数组中累加和为给定值的最长子数组系列问题  
【题目】  
给定一个无序数组arr，其中元素可正、可负、可0，给定一个整数k。求arr所有的子数组中累加和为k的最长子数组长度。  
【补充题目】  
给定一个无序数组arr，其中元素可正、可负、可0。求arr所有的子数组中正数与负数个数相等的最长子数组长度。  
【补充题目】  
给定一个无序数组arr，其中元素只是1或0。求arr所有的子数组中0和1个数相等的最长子数组长度。  
【要求】  
时间复杂度O(N)  
> 7.未排序数组中累加和小于或等于给定值的最长子数组长度（看第9题）
【题目】  
给定一个无序数组arr，其中元素可正、可负、可0，给定一个整数k。求arr所有的子数组中累加和小于或等于k的最长子数组长度。  
例如：arr=[3,-2,-4,0,6]，k=-2，相加和小于或等于-2的最长子数组为{3,-2,-4,0}，所以结果返回4。
【要求】  
时间复杂度(N*logN)

> 8.给定一个无序矩阵，其中有正，有负，有0，求子矩阵的最大和。 
  
 
先想到暴力解法，以求子数组最大累加和为基础。先每一行分别按照求子数组最大累加和的方法得出max，再每两行接着求，再每三行接着求。。。最后n行一起求，就能得到最大累加和了。  
比如，数组a[4][4]。  
先求a[0]数组的最大累加和max，  
再求a[1]数组的最大累加和，和max比较，记最大的，  
...  
最后求a[3]数组；  

一行的求完了，求两行的。  
求a[0]+a[1]数组的最大累加和，和max比较，记最大的，(a[0]+a[1]数组表示第1行和第2行对应元素相加后得到的数组)  
求a[1]+a[2]数组的最大累加和，和max比较，记最大的，  
求a[2]+a[3]数组的最大累加和，和max比较，记最大的；  

两行的求完了求三行的；  
求a[0]+a[1]+a[2]数组的最大累加和，和max比较，记最大的，  
求a[1]+a[2]+a[3]数组的最大累加和，和max比较，记最大的，  
三行的求完了求四行的；  
求a[0]+a[1]+a[2]+a[3]数组的最大累加和，和max比较，记最大的。  

好了，都把所有可能的子矩阵遍历完了，最后的max就是最大累加和。  
时间复杂度：~~O(n2m) n行数，m列数。方阵的话是O(n3)~~ 复杂度算不明白。。  
  
实际解法思路跟我想的差不多，只不过优化了很多，时间复杂度是O(n3)。 
 
它不是根据行数来遍历而是从a[0]开始遍历一遍，再从a[1]开始遍历一遍。。比如a[4][4]：  
求a[0]数组最大累加和  
求a[0]+a[1] 数组最大累加和  
求a[0]+a[1]+a[2]数组最大累加和  
求a[0]+a[1]+a[2]+a[3]数组最大累加和  

a[0]的遍历完了，遍历包含a[1]的；  
求a[1]数组最大累加和  
求a[1]+a[2]数组最大累加和  
求a[1]+a[2]+a[3]数组最大累加和  

a[1]的遍历完了，遍历包含a[2]的；  
求a[2]数组最大累加和  
求a[2]+a[3]数组最大累加和  

最后求a[3]数组最大累加和。  

这样子也同样能遍历到所有子矩阵，而且还有一个好处，那就是能够用一个辅助数组来存储各行对应元素的和。这样就不用每次都要重新相加元素，时间复杂度相比我的想法会降为O(n3)。 
  
```java
/**
 * 矩阵元素有正有负有零，求子矩阵最大和。<br>
 * 用算法原型：求子数组最大累加和
 * 
 * @author YJ
 *
 */
public class SubMatrixMaxSum {
	public static int getMaxSum(int[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) {
			return 0;
		}
		int[] s = null;// 辅助数组，记录指定行的和
		int cur = 0;
		int max = matrix[0][0];
		for (int i = 0; i < matrix.length; i++) {
			// s = matrix[i];//错误：这样s就指向matrix了，s该matrix也跟着改
			s = new int[matrix[i].length];
			for (int j = i; j < matrix.length; j++) {// 行
				cur = 0;
				for (int k = 0; k < s.length; k++) {// 列,开始找数组s的最大累加和
					s[k] += matrix[j][k];
					cur += s[k];
					max = Math.max(max, cur);
					cur = cur < 0 ? 0 : cur;
				}
			}
		}
		return max;
	}
	public static void main(String[] args) {
		int[][] m1 = {{1,3,4,2},
				{-3,-2,3,-4},
				{2,9,-3,7},
				{3,-2,6,5}};
		int[][] m2 = {{1,3},
				{-3,2},
				{4,2}
				};
		System.out.println(getMaxSum(m2));
	}
}

```
> 9.给定一个无序矩阵，其中有正，有负，有0，再给定一个值k，求累加和小于等于k的最大子矩阵大小，矩阵的大小用其中的元素个数来表示。  
  
可不可以直接用 求子数组累加和等于k的最长子数组长度 的算法套进去的，用滑动窗口？  
窗口内值的累加和小于等于k，右边界向右滑动一步，否则左边界向右滑动一步。窗口大小就是累加和小于等于k的子数组长度，每次都比较窗口大小记下最大值。  
不对，滑动窗口之所以可行是因为数组中元素都是正数，确保了每次左边抛弃的值都是不需要的。  
这里题目矩阵元素中是有负数的，有这样一种情况：  
假设k=-3，数组为-1，-2，-2，3  
那么因为-1>k，会直接抛弃掉-1。同理后面的-2，-2，3也都被抛弃了，最后得出错误结果0。  
这里有个解决方法不知道可不可行，就是如果碰到窗口累加和是负数，就直接右边界右移不抛弃值。emm不可以，因为你不能确保最后的窗口累加和是小于等于k的。  
所以滑动窗口pass  
  
> 算法原型是求累加和小于等于k的最长子数组的长度。  
  
算法原型解法：  
设0到当前位置j累加和是sum[j]，如果sum[j]<=k,那么0到j就是累加和小于等于k的子数组长度；如果sum[j]>k，那么找出0到j之间累加和第一次大于等于sum[j]-k的位置i，该位置到j就是就是要求的最大长度。因为sum[i]+sum[i+1 ~ j]=sum[j]>k，所以只要sum[i]>=sum[j]-k，那么一定有sum[i+1~j]<=k。  
先算出累加和数组sum，再找出每一个位置对应的i，找i这里有个优化，用一个数组记录区域最大累加和，然后可以在这个数组上用二分查找查出第一个大于等于k的位置i。  
  
```java
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
		while(l<=r){//找第一个大于等于rk的数
			int mid = (r>>1)+(l>>1);
			if(inc[mid]>=rk){
				//r=mid;
				r=mid-1;
			}else{
//				l=mid;//数组中没有大于等于rk的数的话会陷入死循环
				l=mid+1;
			}
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

```
> 10.给定一个无序矩阵，其中只有 1 和 0 两种值，求只含有 1 的最大的子矩阵大小，矩阵的大小用其中的元素个数来表示。  
  
首先想到用求子矩阵最大和的思路。只不过cur归0的条件要改为当前元素小于压缩行数。其他操作都相同。O(m2n)  
  
O(mn)的解法：  
化为直方图，化直方图的方法：以每一行为底，向上累加，若当前值为0就直接归0。  
最后问题可以转化为求直方图的最大长方形。之后求出这些长方形中的最大长方形。  
本来想用找最大储水面积的那个方法，用双指针，但是不行，因为直方图的长方形要求中间的值必须是大于等于左右边界的，不能缺一块。  
暴力：  
一次计算包含当前位置的柱子的长方形能扩多远。  
  
重点来了，  
非暴力：  
用栈。  
当前元素小于等于栈顶的话栈顶出栈，结算当前栈顶能扩多远。  
栈顶下面的数一定是第一个比它小它的数。因为大的都出栈了。  
怎么结算当前栈顶？  
因为栈顶下面的元素一定是往左第一个小于它的数，当前元素又是往右第一个小于等于它的数，所以栈顶下面的元素可充当左边界，当前元素可充当右边界，不包括边界，两者距离就是~~当前栈顶能向左扩的最大面积~~ 包含弹出的栈顶高度的最大面积的长度（可能不是最大面积，但遍历到后面会修正）
  
```java
import java.util.Stack;

/**
 * 给定一个无序矩阵，其中只有 1 和 0 两种值，求只含有 1 的最大的子矩阵大小，矩阵的大小用其中的元素个数来表示。 化为直方图，求最大长方形面积 用栈
 * 当前元素小于等于栈顶的话栈顶出栈
 * 弹出栈顶元素，因为栈顶下面的元素一定是往左第一个小于它的数，当前元素又是往右第一个小于等于它的数，所以栈顶下面的元素可充当左边界，当前元素可充当右边界，
 * 两者间的距离乘以栈顶高度就是所求面积 所以该数和栈顶围起来的面积就是0到栈顶的最大面积
 * 
 * @author YJ
 *
 */
public class SubMatrixMaxSumOnlyOne {
	public int get(int[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return 0;
		}
		int max = 0;
		int[] h = new int[matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			Stack<Integer> stack = new Stack<Integer>();
			for (int j = 0; j < matrix[i].length; j++) {
				h[j] = matrix[i][j] == 0 ? 0 : h[j] + 1;// 以每一行为底的直方图数据
				while (!stack.isEmpty() && h[stack.peek()] >= matrix[i][j]) {
					int top = stack.pop();// 栈顶出栈
					int next = -1;// 左边界，如果栈为空默认则为-1
					if (!stack.isEmpty()) {
						next = stack.peek();
					}
					int length = j - next - 1;//当前位置j是右边界，length是包含top所指元素的面积长度
					int area = length * h[top];
					max = max < area ? area : max;
				}
				stack.push(j);
			}
			// 全部出栈
			while (!stack.isEmpty()) {
				int top = stack.pop();
				int next = stack.isEmpty() ? -1 : stack.peek();
				int length = h.length - next - 1;//
				int area = length * h[top];
				max = max < area ? area : max;
			}
		}
		return max;
	}

	public static void main(String[] args) {
		int[][] a = { { 1, 1 }, { 1, 1 }, { 0, 1 } };
		System.out.println(new SubMatrixMaxSumOnlyOne().get(a));
	}
	// 以每一行为底求出每一行对应的直方图就够了，没有必要全部折叠一遍
	// 有很多错误
	// public int get(int[][] matrix){
	// if(matrix==null || matrix.length==0 || matrix[0].length==0){
	// return 0;
	// }
	// int max=0;
	// for(int i=0;i<matrix.length;i++){
	// int[] histogram = new int[matrix[i].length];
	// for(int j=i;j<matrix.length;j++){
	// histogram[0] = matrix[j][0]==0?0:histogram[0]+1;
	// Stack<Integer> stack = new Stack<Integer>();
	// stack.push(0);//先压第一个数
	// for(int k=1;k<matrix[j].length;k++){
	// histogram[k] = matrix[j][k]==0?0:histogram[k]+1;//折叠矩阵，化为直方图
	// Integer top = null;
	// while(!stack.isEmpty() && histogram[top]>=matrix[j][k]){
	// top = stack.pop();//栈顶出栈
	// Integer next = 0;//如果栈为空默认栈顶为0
	// if(!stack.isEmpty()){
	// next = stack.peek();
	// }
	// int length = top-next+1;
	// int area = length*histogram[top];
	// max = max<area?area:max;
	// }
	// stack.push(k);
	// }
	// }
	// }
	// return max;
	// }
}

```
  
11. 给定一棵完全二叉树的头节点head，求其中的节点个数。  
只想到深度遍历所有结点，不知道还能怎么弄。O(n)  
  
O((logn)^2) ：  
高度为l的满二叉树节点数为2^l-1  
递归用公式计算。  
  
```java
package completetree;

/**
 * 给定一棵完全二叉树的头节点head，求其中的节点个数。
 * @author YJ
 *
 */
public class CompleteTreeNodeNumber {
	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int value) {
			this.value = value;
		}
	}

	public static int nodeNum(Node head) {
		if (head == null) {
			return 0;
		}
		return bs(head, 1, mostLeftLevel(head, 1));
	}

	/**
	 * 递归计算数量
	 * @param node 
	 * @param l 当前结点高度
	 * @param h 整颗树的高度
	 * @return
	 */
	public static int bs(Node node, int l, int h) {
		if (l == h) {
			return 1;
		}
		if (mostLeftLevel(node.right, l + 1) == h) {//如果右子树的最左结点高度等于树的高度，说明左子树确定叶结点高度都是h。
			return (1 << (h - l)) + bs(node.right, l + 1, h);//左子树结点个数确定，递归右结点
		} else {//如果不等说明右子树叶节点高度都是h-1，所以可以确定右子树结点个数
			return (1 << (h - l - 1)) + bs(node.left, l + 1, h);//右子树结点个数，递归左结点
		}
	}

	/**
	 * 返回子树最左边结点在整棵树上的高度
	 * @param node 子树根结点
	 * @param level 当前结点的高度
	 * @return
	 */
	public static int mostLeftLevel(Node node, int level) {
		while (node != null) {
			level++;
			node = node.left;
		}
		return level - 1;
	}
}

```  
  
12. 给定一个字符串类型的数组，其中不含有重复的字符串，如果其中某一个字符串是另一个字符串的前缀，返回true；如果没有任何一个字符串是另一个字符串的前缀，返回false。  
```java  
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

```