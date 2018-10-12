import java.util.*;

public class WordBreakⅡ_140 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
//        String[] words = {"apple","pan"};
        String[] words = {"cats","and","dog","cat","sand"};
        for(String w:words){
            list.add(w);
        }
        List<String> result = new WordBreakⅡ_140().wordBreak("catsanddog", list);
        for(String s:result){
            System.out.println(s);
        }
    }

    public List<String> wordBreak(String s, List<String> wordDict) {
        boolean[] segment = new boolean[s.length()+1];//dp数组，dp[i] s的0到i-1子串是否能被正确分割
        int maxLength = getMaxLength(wordDict);//单词的最大长度
        List<String>[] pos = new ArrayList[s.length()];
        for(int i=0;i<s.length();i++){
            pos[i] = new ArrayList<String>();
        }
        segment[0]=true;
        for(int i=1;i<=s.length();i++){
            segment[i]=false;
            for(int lastWordLength=1;lastWordLength<=maxLength&&lastWordLength<=i;lastWordLength++){
                if(!segment[i-lastWordLength]){
                    continue;
                }
                String word = s.substring(i-lastWordLength,i);
                if(wordDict.contains(word)){
                    segment[i]=true;
                    pos[i-1].add(word);
                }
            }
        }
        boolean isRight = segment[s.length()];
        List<String> result =  new ArrayList<>();
        if(isRight){
            dfs(pos,result,new LinkedList<String>(),pos.length-1);
        }
        return result;
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
    public void dfs(List<String>[] pos, List<String> result, LinkedList<String> curr, int i){
        if(i<0){
            StringBuilder temp = new StringBuilder();
            for(String s:curr){
                temp.append(s);
            }
            result.add(temp.toString().trim());
            return;
        }
        for(String s:pos[i]){
            curr.push(s+" ");
            dfs(pos,result,curr,i-s.length());
            curr.pop();
        }
    }



}