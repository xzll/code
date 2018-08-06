import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class CharsetTest {
    public static void main(String[] args) throws CharacterCodingException {
        Charset charset = Charset.forName("ASCII");
        //用一个charset实例来创建一个编码器
        CharsetEncoder encoder = charset.newEncoder();
        //调用encode方法进行编码
        ByteBuffer buffer = encoder.encode(CharBuffer.wrap("encoder.encode"));

        while(buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
        buffer.flip();
        //用charset实例来创建一个解码器
        CharsetDecoder decoder = charset.newDecoder();
        //调用decode方法进行编码
        CharBuffer cBuf = decoder.decode(buffer);
        System.out.println();
        while(cBuf.hasRemaining()) {
            System.out.print(cBuf.get());
        }

    }
}
