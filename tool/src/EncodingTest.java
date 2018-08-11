import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class EncodingTest {
    public static void main(String[] args) {
        try {
//            String str = new String("测试编码集".getBytes(),"ISO-8859-1");//用iso-8859-1解码字符数组，生成对应的字符串。错误
            String str1 = new String("测试编码集".getBytes(),"utf-8");//输出的是正确的字符串，java的getBytes默认是用的utf-8来编码的吗？不是
            //Encodes this String into a sequence of bytes using the platform's default charset 是用的平台的字符集
            String csn = Charset.defaultCharset().name();//源码是这个，至于平台指的是那个平台还没去研究，好像是操作系统的？
            System.out.println(str1);
            System.out.println(csn);
//            String str2 = new String("测试编码集ISO8859-1".getBytes("ISO8859-1"),"utf-8");
            String str2 = new String(new String("测试编码集ISO8859-1".getBytes(),"ISO8859-1").getBytes("ISO-8859-1"),"utf-8");
            //之前iso一直乱码是因为如果unicode字符串先用iso编码的话，出来的bytes就是iso的，用iso解码的也只能是乱码，因为它不支持中文
            //要iso显示中文，必须借用其他的支持中文的编码集，将中文用utf-8编码，然后用iso解码成字符串，
            // 拿到这个iso字符串后再用iso编码，utf-8解码
//            String str2 = new String("测试编码集".getBytes("gbk"),"gbk");
//            String str2 = new String(new String("测试编码集".getBytes("ISO8859-1")).getBytes("ISO8859-1"),"ISO-8859-1");
            System.out.println(str2);
            System.out.println("--------------------------------");
            System.out.println("java str: 测试编码集ISO8859-1");
            byte[] bytes = "测试编码集ISO8859-1".getBytes("utf-8");
            System.out.print("--- utf-8 encode java str --- \nbytes: ");
            for (Byte b : bytes) {
                System.out.print(String.format("%x",b));

            }
            String str = new String(bytes,"ISO8859-1");
            System.out.println("\n--- iso8859-1 decode utf-8 bytes --- \nstr:"+str);

            byte[] isoBytes = str.getBytes("ISO8859-1");
            System.out.print("--- ISO8859-1 encode iso8859-1 str --- \nbytes: ");
            for (byte b : isoBytes) {
                System.out.print(String.format("%x",b));
            }

            String utf8Str = new String(isoBytes,"utf-8");
            System.out.println("\n--- utf-8 decode iso8859-1 bytes --- \nstr:"+utf8Str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
