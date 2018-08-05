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
