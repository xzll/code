package avltree;

import java.nio.file.Paths;

/**
 * 高度平衡的二叉搜索树，任何节点的两个子树的最大高度差为1
 * 失去平衡的情况：
 * ll、rr、lr、rl
 * ll和rr均只需要一次旋转就可以恢复平衡
 * lr先左旋成ll再右旋
 * rl先右旋成rr再左旋
 */
public class AVLTree<T extends Comparable<T>> {
    private AVLTreeNode<T> mRoot;
    /**
     * 空的二叉树高度是0
     * @param <T>
     */
    class AVLTreeNode<T extends Comparable<T>>{
        private T key;
        private AVLTreeNode<T> left;
        private AVLTreeNode<T> right;
        private int height;
        public AVLTreeNode(T key,AVLTreeNode<T> left,AVLTreeNode<T> right){
            this.key = key;
            this.left = left;
            this.right = right;
            this.height = 1; //

        }
    }
    public int height(AVLTreeNode<T> tree){
        if(tree!=null){
            return tree.height;
        } else {
            return 0;
        }
    }
    public int height(){
        return height(mRoot);
    }
    private int max(int a,int b){
        return a>b?a:b;
    }
    /**
     * ll情况下的旋转
     * k1左节点是k2，k2旋转当父结点
     * k2右节点变成k1左节点，k1变成k2右节点
     * @param k1
     * @return k2
     */
    private AVLTreeNode<T> llRotation(AVLTreeNode<T> k1) {
        AVLTreeNode<T> k2;
        //旋转
        k2 = k1.left;
        k1.left = k2.right;
        k2.right = k1;
        //修改高度
        k1.height = max(height(k1.left),height(k1.right))+1; //必须先改k1再改k2
        k2.height = max(height(k2.left),height(k2.right))+1;

        return k2;

    }
    /**
     * rr情况下的旋转
     * k1右节点是k2，k2旋转当父结点
     * k2左节点变成k1右节点，k1变成k2左节点
     * @param k1
     * @return k2
     */
    private AVLTreeNode<T> rrRotation(AVLTreeNode<T> k1) {
        AVLTreeNode<T> k2;
        //旋转
        k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        //修改高度
        k1.height = max(height(k1.left),height(k1.right))+1; //必须先改k1再改k2
        k2.height = max(k1.height,height(k2.right))+1;

        return k2;

    }
    /**
     * lr情况下的旋转
     * k3为顶部节点，左节点是k1，k1右节点是k2
     * 先k1左旋成ll，k3再右旋
     * @param k3
     * @return
     */
    private AVLTreeNode<T> lrRotation(AVLTreeNode<T> k3) {
        k3.left  = rrRotation(k3.left);
        return llRotation(k3);
    }
    /**
     * rl情况下的旋转
     * k3为顶部节点，右节点是k1，k1左节点是k2
     * 先k1右旋成rr，k3再左旋
     * @param k3
     * @return
     */
    private AVLTreeNode<T> rlRotation(AVLTreeNode<T> k3) {
        k3.right  = rrRotation(k3.right);
        return llRotation(k3);
    }

    /**
     * 将节点插入树中
     * @param tree
     * @param key
     * @return 根节点
     */
    private AVLTreeNode<T> insert(AVLTreeNode<T> tree, T key) {
        if(tree == null) {//如果要插入的树是空的，直接新建节点
            tree = new AVLTreeNode<T>(key,null,null);
        } else {
            int cmp = key.compareTo(tree.key);
            if(cmp<0) { // 应该插入左子树的情况
                tree.left = insert(tree.left,key);
                if(height(tree.left) - height(tree.right)>1) { //插入节点后如果avl树失去平衡，则应该进行相应的调节
                    //怎么判断是哪种非平衡状态?
                    //比较大小确定节点是插入了左子树中的哪一个子树里
                    if(key.compareTo(tree.left.key) < 0) {
                        tree = llRotation(tree);
                    }else {
                        tree = lrRotation(tree);
                    }
                }
            }else if(cmp>0){// 应该插入右子树的情况
                tree.right = insert(tree.right,key);
                if(height(tree.right) - height(tree.left)>1) {
                    if(key.compareTo(tree.right.key) < 0) {
                        tree = rlRotation(tree);
                    }else {
                        tree = rrRotation(tree);
                    }
                }
            }else {
                System.out.println("不允许插入相同的节点");
            }
        }
        //最后添加完成，更新树的高度
        tree.height = max(height(tree.left),height(tree.right))+1;
        return tree;
    }

    /**
     * 插入节点
     * @param key
     */
    public void insert(T key){
        mRoot = insert(mRoot,key);
    }

    /**
     * 从树中删除节点，必须确保该节点存在，否则会出错，把整棵树都置为空
     * @param tree
     * @param key
     * @return
     */
    private AVLTreeNode<T> remove(AVLTreeNode<T> tree,T key) {
        if(tree==null) {
            return null;
        }
        int cmp = key.compareTo(tree.key);
        if(cmp<0) {  //要删除的节点在左子树中
            tree.left = remove(tree.left,key);
            if(height(tree.right) - height(tree.left)>1) {//调整
                //知道了是左子树高度小，那么右子树又是什么情况，rr还是rl
                AVLTreeNode<T> r = tree.right;
                if(height(r.left) - height(r.right)>0) {
                    tree = rlRotation(tree);
                }else {
                    tree = rrRotation(tree);
                }
            }
        }else if(cmp>0) {  //要删除的节点在右子树中
            tree.right = remove(tree.right,key);
            if(height(tree.left) - height(tree.right)>1) {
                AVLTreeNode<T> r = tree.left;
                if(height(r.left) - height(r.right)>0) {
                    tree = llRotation(tree);
                }else {
                    tree = lrRotation(tree);
                }
            }
        }else {  //当前节点就是要删除的节点
            if(tree.left!=null && tree.right!=null) {//左右孩子都存在，找其他节点替代tree
                //1. 找出替代节点（最大或最小节点，也是叶子节点）
                //2. 把值赋给tree
                //3. 删除该替代节点
                if(height(tree.left)>height(tree.right)) {//左子树比右子树高，那么可以从中找最大节点来替代
                    AVLTreeNode<T> max = maxNode(tree.left);
                    tree.key = max.key;
                    tree.left = remove(tree.left,max.key);
                }else {
                    AVLTreeNode<T> min = minNode(tree.right);
                    tree.key = min.key;
                    tree.right = remove(tree.right,min.key);
                }
            }else {
                AVLTreeNode<T> temp = tree;
                tree = (tree.left!=null)?tree.left:tree.right;
                temp = null;
            }
        }
        //修改高度
        tree.height = max(height(tree.left),height(tree.right))+1;
        return tree;
    }

    /**
     * 删除节点
     * @param key
     */
    public void remove(T key) {
        if(search(mRoot,key)!=null){
            mRoot =remove(mRoot,key);
        }
    }
    private AVLTreeNode<T> search(AVLTreeNode<T> tree,T key) {
        if(tree==null){
            return null;
        }
        while(tree!=null){
            int cmp = key.compareTo(tree.key);
            if(cmp>0) {
                tree = tree.right;
            }else if(cmp<0) {
                tree = tree.left;
            }else {
                return tree;
            }
        }
        return null;
    }

    public AVLTreeNode<T> maxNode(AVLTreeNode<T> tree) {
        if(tree==null) {
            return null;
        }
        while(tree.right!=null){
            tree = tree.right;
        }
        return tree;
    }
    public AVLTreeNode<T> minNode(AVLTreeNode<T> tree) {
        if(tree==null) {
            return null;
        }
        while(tree.left!=null){
            tree = tree.left;
        }
        return tree;
    }
    private void inOrder(AVLTreeNode<T> tree){
        if(tree!=null) {
            inOrder(tree.left);
            System.out.println(tree.key);
            inOrder(tree.right);
        }
    }
    /**
     * 中序遍历
     */
    public void inOrder(){
        inOrder(mRoot);
    }

    public static void main(String[] args) {
        int arr[]= {3,2,1,4,5,6,7,16,15,14,13,12,11,10,8,9};
        int i;
        AVLTree<Integer> tree = new AVLTree<Integer>();
        System.out.printf("== 依次添加: ");
        for(i=0; i<arr.length; i++) {
              System.out.printf("%d ", arr[i]);
              tree.insert(arr[i]);
        }
        System.out.println("== 中序输出");
        tree.inOrder();
    }
}
