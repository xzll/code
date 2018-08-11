java的编码解码：  
java字符串本身是用的unicode字符集，可以看作是无编码的  
用str.getBytes("charset");可以得到用指定的charset编码的字节数组。  
new String(btyes,"charset");可以得到用指定的charset解码的字符串。  
比如，如果有个字符串str是用iso8859-1解码出来的，那么用str.getBytes("iso8859-1");可以得到正确的字节数组。  
如果这些字节是用的utf-8编码的那么就需要用utf-8解码才能得到正确的字符串。
