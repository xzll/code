
/**
 * 不用辅助数组
 * @author YJ
 *
 */
public class TrappingRainWater_42 {
	/**
	 * 取两指针和两变量
	 * 遍历一次
	 * @param height
	 * @return
	 */
    public int trap2(int[] height) {
    	if(height==null || height.length==0) return 0;
    	int l = 0;
    	int r = height.length-1;
    	int lmax = height[0];
    	int rmax = height[r];
    	int sum = 0;
    	while(l<r-1){//两指针相邻就可以跳出循环了
    		//选择两值较小的那一边进行计算
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
    /**
     * 先找出最大值
     * 从左遍历到最大值
     * 再从有遍历到最大值
     * @param height
     * @return
     */
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
    public static void main(String[] args) {
		int[] a = {0,1,0,2,1,0,1,3,2,1,2,1};
		System.out.println(new TrappingRainWater_42().trap(a));
	}
}
