package sort;

import java.util.Arrays;

/**
 * 堆排序是一种选择排序，整体主要由构建初始堆+交换堆顶元素和末尾元素并重建堆两部分组成。
 * 其中构建初始堆经推导复杂度为O(n)，在交换并重建堆的过程中，需交换n-1次，
 * 而重建堆的过程中，根据完全二叉树的性质，[log2(n-1),log2(n-2)...1]逐步递减，近似为nlogn。
 * 所以堆排序时间复杂度一般认为就是O(nlogn);
 * 最坏最好都是O(nlogn);
 * 辅助空间O(1);
 */
public class HeapSort {

    public static void main(String []args){
        int []arr = {9,10,7,6,5,4,3,2,1};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
    public static void sort(int[] arr) {
        //先构建大顶堆 O(n)
        //从最后一个非叶子节点开始 length/2-1
        //从下至上，从右到左
        for(int i =arr.length/2-1;i>=0;i--) {
            adjustHeap(arr,i,arr.length);
        }
        //调整堆结构，交换堆顶元素与末尾元素
        for(int i=arr.length-1;i>0;i--) {
            swap(arr,0,i);
            adjustHeap(arr,0,i);
        }

    }
    /**
     * 调整最大堆
     * 堆大小小于等于3的可以当做是最大堆，所以构建最大堆时可以调用这个方法从下到上调整
     * 循环子层直到子节点不大于父节点。
     * @param arr 数组
     * @param i 父节点
     * @param length 要调整的数组长度，0~length-1
     */
    public static void adjustHeap(int[] arr,int i,int length){
        //如果子结点大于父结点的话，交换两者的位置，又因为交换之后又要判断下一层的父结点和子节点
        //所以可以先把当前节点存起来，等到都比较好位置调好后，再把这个数值放在可以覆盖的节点上。
        //i的左子节点是2i+1
        int temp = arr[i];
        for(int k=2*i+1;k<length;k=2*k+1) {//一层层往下判断，直到父结点大于子节点
            if(k+1<length && arr[k]<arr[k+1]){//如果左子节点小于右子结点，k指向右子结点
                k++;//k指向最大的子结点
            }
            if(arr[k]>temp) {//如果子结点大于父结点的话，将子结点赋给父结点
                arr[i]=arr[k];
                i=k;//继续下一层的判断，下一层的父结点还是temp
            }else {
                break;//如果子结点小于等于父结点的话，就不需要再调整堆了
            }
        }
        arr[i]=temp;
    }
    public static void swap(int[] arr,int a,int b) {
        int temp=arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }




}
