package checkpoint.andela.buffer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SynchronizedBufferTest {

    Runnable testRunnable;

    @Before
    public void beforeTestAddListToBuffer() {
        testRunnable = new Runnable() {
            @Override
            public void run() {
                Buffer<String> stringBuffer
                        = BufferSingletons.getStringLogBuffer();

                List<String> testList = new ArrayList<>();
                testList.add("Lorem");
                testList.add("ipsum");
                testList.add("dolor");

                stringBuffer.addListToBuffer(testList);
            }
        };

        (new Thread(testRunnable)).start();
    }


    @Test
    public void testAddListToBuffer() throws Exception {
        Thread.sleep(1000);

        Buffer<String> buffer = BufferSingletons.getStringLogBuffer();

        String trackingKey = buffer.registerClientForTracking();
        assertTrue(buffer.isThereNewData(trackingKey));

        List<String> newData = buffer.getLatestData(trackingKey);
        assertNotNull(newData);
        assertTrue(newData.size() > 0);

        assertFalse(buffer.isThereNewData(trackingKey));
    }
}