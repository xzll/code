/**
 * 字符串匹配
 * 非暴力解法
 * kmp算法
 *
 */
public class IndexKMP {
    /**
     * 获得字符串对应的next[]
     * 设k=next[i],则str的前k个字符等于str的i-k到i-1的字符。
     * next[0] = -1;
     * next[1] = 0;
     * 若next[j] = k，
     * str.j == str.k 则next[j+1] = k+1 ①
     * str.j != str.k 则另k = next[k] ②，继续比str.j 和 str.k 直到 ① 或者 str.j != str.k 且k=0，此时 next[j+1] = 0
     * @param str
     * @return
     */
    public static int[] getNext(String str){
        int[] next = new int[str.length()];
        int j = 1;
        int k =0;
        next[0] = -1;
        next[1] = 0;
        while(j<=str.length()){
            if(str.charAt(j) == str.charAt(k)){
                next[j+1] = k+1;
                j++;
                k++;
            }else if(k==0){
                next[j+1]=0;
                j++;
            } else {
                k=next[k];
            }
        }
        return next;
    }

    /**
     *       a  a a a c
     * next: -1 0 1 2 3
     * k:         1 2 3
     * 改：  -1 0 0 0 3
     * @param str
     * @return
     */
    public static int[] getNextVal(String str){
        int[] next = new int[str.length()];
        int j = 1;
        int k =0;
        next[0] = -1;
        next[1] = 0;
        while(j<=str.length()){
            if(k==-1 || str.charAt(j) == str.charAt(k)){
//                next[j+1] = k+1;
                j++;
                k++;
                if(str.charAt(j)!=str.charAt(k)) {
                    next[j] = k;
                }else {//相等
                    next[j] = next[k];
                }
            }else {
                k=next[k];
            }
        }
        return next;
    }

    /**
     * 主串指针i的初值为start
     * 模式串指针j的初值为0
     * 一一比较，①
     * 如果[i] == [j] 则 i++;j++;
     * 如果[i] != [j] 则 j=next[j];
     *
     * 如果j==-1 则i++;j++; 然后①
     *
     * 循环直到j>length 或者 i>length
     * @param str
     * @param start
     * @return
     */
    public static int index_KMP(String str,String match,int start) {
        int[] next = getNextVal(match);
        int j = start;
        int i = 0;
        while (i<str.length() && j<match.length()){

            if(j==-1 || str.charAt(i)==match.charAt(j)) {
                i++;
                j++;
            }else {
                j=next[j];
            }
        }
        if(j<match.length()){
            return -1;
        }else {
            return i-match.length();
        }
    }
}
