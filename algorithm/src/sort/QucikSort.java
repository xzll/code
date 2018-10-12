package sort;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

/**
 * 快速排序每次递归取一个标准值，根据它来划分序列，划分总的再递归划分左右序列。
 * 时间复杂度是O(nlogn);
 * 最好是O(nlogn);
 * 最坏是O(n^2);倒序，此时需要随机取标准值p。
 * 因为递归划分序列，所以辅助空间O(logn)？？
 */
public class QucikSort {

    public static void main(String []args){
        int []arr = {9,10,7,6,5,4,3,2,1};
        sort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 从上到下递归，先划分总的，再划分左右序列
     * @param arr
     * @param start
     * @param end
     */
    public static void sort(int[] arr,int start,int end) {
        if(start>=end){
            return;
        }
        int i = partition(arr,start,end);
        sort(arr,start,i-1);
        sort(arr,i+1,end);

    }
    //非递归实现，用栈存start和end
    public static void sortStack(int[] arr){
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(0);
        stack.push(arr.length-1);
        while (!stack.isEmpty()){
            int end = stack.pop();
            int start = stack.pop();
            int i = partition(arr,start,end);
            if(start<i-1){
                stack.push(start);
                stack.push(i-1);
            }
            if(end>i+1){
                stack.push(i+1);
                stack.push(end);
            }
        }
    }

    /**
     * 根据选定的标准p来划分序列，小于p的放左边，大于p的放右边，p在中间
     * 头尾指针实现
     * @param arr 待划分数组
     * @param start 要开始划分的下标
     * @param end 结束划分的下标
     * @return 返回最后p所在的下标
     */
    private static int partition(int[] arr,int start,int end){
        if(start>=end){
            return start;
        }
        int ran = (int)(Math.random()*(end-start+1))+start;//随机取标准值p
        swap(arr,ran,start);

        int p = arr[start];
        int i = start; // 两个指针，右边指针先开始移动，碰到小于p的数时把它放到左边指针的位置；然后开始移动左指针，操作相反；两指针轮流移动直到i>=j。
        int j = end;
        while(i<j){
            while (i<j&&arr[j]>=p){
                j--;
            }
            arr[i]=arr[j];
            while (i<j&&arr[i]<=p){
                i++;
            }
            arr[j]=arr[i];
        }
        arr[i]=p;
        return i;
    }
    private static void swap(int[] arr,int a,int b){
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
}
