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
	public static void main(String[] args) {
		Node node = new Node(1);
		node.left = new Node(2);
		node.right = new Node(3);
		node.left.left = new Node(4);
		node.left.right = new Node(5);
		System.out.println(nodeNum(node));
	}
}
