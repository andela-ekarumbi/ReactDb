package checkpoint.andela.buffer;

public class BufferFactory {
    private static Buffer<String> stringLogBuffer;

    public static Buffer<String> getStringLogBuffer() {
        if (stringLogBuffer == null) {
            stringLogBuffer = new Buffer<>();
        }
        return stringLogBuffer;
    }
}
