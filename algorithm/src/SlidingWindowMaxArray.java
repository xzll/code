import java.util.LinkedList;

/**
 *
 13.生成窗口最大值数组
 【题目】有一个整型数组arr和一个大小为w的窗口从数组的最左边滑到最右边，窗口每次向右边滑一个位置。
 例如，数组为[4,3,5,4,3,3,6,7]，窗口大小为3时：
 [4  3  5] 4  3  3  6  7  窗口中最大值为5
 4 [3  5  4] 3  3  6  7  窗口中最大值为5
 4  3 [5  4  3] 3  6  7  窗口中最大值为5
 4  3  5 [4  3  3] 6  7  窗口中最大值为4
 4  3  5  4 [3  3  6] 7  窗口中最大值为6
 4  3  5  4  3 [3  6  7] 窗口中最大值为7
 如果数组长度为n，窗口大小为w，则一共产生n-w+1个窗口的最大值。
 请实现一个函数。
 输入：整型数组arr，窗口大小为w。
 输出：一个长度为n-w+1的数组res，res[i]表示每一种窗口状态下的最大值。
 以本题为例，结果应该返回{5,5,5,4,6,7}。


 双端队列。
 l，r指针指向窗口左右边界
 先窗口不滑动只往右扩，扩进去的值放进双端队列尾部，
 如果尾部的值小于等于它的话就弹出这些值，确保尾部值大于它或者队列为空，再将它放进队列。
 这样能确保窗口往右扩时，队列头部的值就是该窗口的最大值
 然后窗口往右缩，查看队列头部的值（下标）是否过期，是的话弹出，保持队列头部是窗口的最大值。
 */
public class SlidingWindowMaxArray {
    public static int[] getMaxWindow(int[] arr,int w){
        if(arr==null || w<1 || arr.length<w){
            return null;
        }
        LinkedList<Integer> qmax = new LinkedList<Integer>();
        int[] res = new int[arr.length-w+1];
        int index=0;
        for(int i=0;i<arr.length;i++){
            while(!qmax.isEmpty()&&arr[qmax.peekLast()]<=arr[i]){//从尾部弹出小于等于arr[i]的值
                qmax.pollLast();
            }
            qmax.addLast(i);//入队列
            if(qmax.peekFirst()==i-w){//从头部弹出过期的值
                qmax.pollFirst();
            }
            if(i>=w-1){
                res[index++]=arr[qmax.peekFirst()];//处理完后头部就是该窗口的最大值
            }

        }
        return res;
    }
}
