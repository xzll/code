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
