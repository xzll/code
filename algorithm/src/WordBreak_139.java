import java.util.*;

public class WordBreak_139 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
//        String[] words = {"apple","pan"};
        String[] words = {"a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa","aaaaaaaaa","aaaaaaaaaa"
        ,"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabb"};
//        String[] words = {"cats","and","dog"};
        for(String w:words){
            list.add(w);
        }
        boolean b =  new WordBreak_139().wordBreak("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"
                , list);
        System.out.println(b);
    }

    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] segment = new boolean[s.length()+1];//dp数组，dp[i] s的0到i-1子串是否能被正确分割
        int maxLength = getMaxLength(wordDict);//单词的最大长度
        segment[0]=true;
        for(int i=1;i<=s.length();i++){
            segment[i]=false;
            for(int lastWordLength=1;lastWordLength<=maxLength&&lastWordLength<=i;lastWordLength++){
                if(!segment[i-lastWordLength]){
                    continue;
                }
                if(wordDict.contains(s.substring(i-lastWordLength,i))){
                    segment[i]=true;
                    break;
                }
            }
        }
        return segment[s.length()];
    }
    public int getMaxLength(List<String> wordDict){
        int max = 0;
        for(String word:wordDict) {
            if(word.length()>max){
                max = word.length();
            }
        }
        return max;
    }


    /**
     * 动态规划
     * dp[i]=isInDict(s.substring(0,i+1)) 初始化
     * dp[i]=dp[j]&&isInDict(s.substring(j+1,i+1))  0<=i<n;0<=j<i; 如果先判断到dp[i]为ture则它就一直为true
     * @param s
     * @param wordDict
     * @return
     */
    public boolean wordBreak1(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length()];
        for(int i=0;i<dp.length;i++){
            dp[i] = isInDict(s.substring(0,i+1),wordDict);
            for(int j=0;j<i;j++){
                if(dp[i]){
                    break;
                }
                dp[i] = dp[j]&&isInDict(s.substring(j+1,i+1),wordDict);
            }
        }
        return dp[s.length()-1];
    }
    public boolean isInDict(String str, List<String> wordDict){
        return wordDict.contains(str);
    }
//    public boolean wordBreak(String s, List<String> wordDict) {
//        //暴力，循环遍历字典，看字符串开头是否有符合的词，有的话切割后继续遍历
//        //递归，栈内存溢出，要把状态存下来
//        Stack<Integer> spoint = new Stack<>();//字符串适配到单词的分割点从0开始
//        Stack<Integer> wordIndex = new Stack<>();//spoint分割点对应的字典未循环到的下标
//        spoint.push(0);
//        wordIndex.push(0);
//        while(!spoint.empty()){
//            int sindex = spoint.peek();
//            int windex = wordIndex.pop();
//            boolean flag = false;
//            for(int i=windex;i<wordDict.size();i++) {
//                String word = wordDict.get(i);
//                if(s.startsWith(word,sindex)){
//                    if(sindex+word.length()==s.length()){//分割点分到最后了，说明可以根据字典来分隔字符串
//                        return true;
//                    }
//                    System.out.println(word);
//                    spoint.push(sindex+word.length());
//                    wordIndex.push(i+1);//当前sindex对应的
//                    wordIndex.push(0);//刚push进去的spoint对应的
//                    flag = true;
//                    break;
//                }
//            }
//            // 没有适配的单词的话栈弹出一个继续遍历，那这个弹出的是从字典的哪开始遍历呢？这里也需要一个值来存字典遍历的位置，即wordIndex
//            if(!flag){
//                spoint.pop();
//            }
//        }
//        return false;
//
//    }


    //这个方法需要什么？切割后的字符串s，还有字典，要返回的boolean
    //怎么把递归修改成用栈实现？s可以用指针表示，把每个分割点压栈，不符合时回退把分割点吐出来就可以了
//    public boolean isStartWithWord(String s,List<String> wordDict){
//        for(String word:wordDict){
//            if(s.startsWith(word)){
//                if( isStartWithWord(s.substring(0,word.length()),wordDict) ){
//
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}