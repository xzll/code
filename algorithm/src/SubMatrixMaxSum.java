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
		int max = matrix[0][0];
		int row = matrix.length;// 行数
		int col = matrix[0].length;// 列数
		if (row > col) {// 如果行数大于列数，行列转换，相当于矩阵转置
			row = col;
			col = matrix.length;
		}
		if (matrix.length > matrix[0].length) {
			for (int i = 0; i < row; i++) {
				// s = matrix[i];//错误：这样s就指向matrix了，s改matrix也跟着改
				s = new int[col];
				for (int j = i; j < row; j++) {// 行
					int cur = 0;
					for (int k = 0; k < col; k++) {// 列,开始找数组s的最大累加和

						s[k] += matrix[k][j];

						cur += s[k];
						max = Math.max(max, cur);
						cur = cur < 0 ? 0 : cur;
					}
				}
			}
		} else {
			for (int i = 0; i < row; i++) {
				// s = matrix[i];//错误：这样s就指向matrix了，s改matrix也跟着改
				s = new int[col];
				for (int j = i; j < row; j++) {// 行
					int cur = 0;
					for (int k = 0; k < col; k++) {// 列,开始找数组s的最大累加和

						s[k] += matrix[j][k];

						cur += s[k];
						max = Math.max(max, cur);
						cur = cur < 0 ? 0 : cur;
					}
				}
			}
		}

		return max;
	}

	public static void main(String[] args) {
		int[][] m1 = { { 1, 3, 4, 2 }, 
				{ -3, -2, 3, -4 }, 
				{ 2, 9, -3, 7 }, 
				{ 3, -2, 6, 5 } };
		int[][] m2 = { { 1, 3 }, 
				{ -3, 2 }, 
				{ 4, 2 } };
		System.out.println(getMaxSum(m2));
	}
}
