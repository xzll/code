package sort;

import java.util.Arrays;

/**
 * 归并排序，递归平分序列到最底层（最底层只有一个数默认是排好序的），然后归并左右序列
 * 时间复杂度是O(nlogn);
 * 最好最坏都是O(nlogn);
 * 辅助空间O(n);
 * 稳定
 */
public class MergeSort {

    public static void main(String []args){
        int []arr = {9,10,7,6,5,4,3,2,1};
//        sort(arr,0,arr.length-1);
        sortIteration(arr);
        System.out.println(Arrays.toString(arr));
    }

    //非递归实现
    public static void sortIteration(int[] arr){
        //从底到上
        int left;
        int mid;
        int right;
        for(int i=1;i<arr.length;i*=2) {//子序列大小，每轮乘2
            left = 0;
            while(left+i<arr.length) {//后一个子序列存在的话，归并两个序列
                mid = left+i-1;
                right = mid+i;
                right = right<arr.length?right:arr.length-1;
                merge(arr,left,mid,right);
                left = right+1;
            }
        }
    }
    public static void sort(int[] arr,int start,int end) {
        if(start>=end){
            return;
        }
        int h = (start+end)/2;
        sort(arr,start,h);
        sort(arr,h+1,end);
        merge(arr,start,h,end);

    }
    private static void merge(int[] arr,int start,int h,int end){
        int i = start;//左序列，start~h
        int j = h+1;//有序列，h+1~end
        int[] aux = new int[end-start+1];//辅助数组
        int k = 0;
        while (i<=h&&j<=end){
            while (i<=h&&j<=end&&arr[i]<=arr[j]) {
                aux[k++]=arr[i++];
            }
            while (i<=h&&j<=end&&arr[j]<arr[i]) {
                aux[k++]=arr[j++];
            }
        }
        while (i<=h){
            aux[k++]=arr[i++];
        }
        while (j<=end){
            aux[k++]=arr[j++];
        }
        System.arraycopy(aux,0,arr,start,aux.length);
    }

    private static void swap(int[] arr,int a,int b){
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
}
