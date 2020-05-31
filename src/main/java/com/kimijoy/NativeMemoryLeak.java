package com.kimijoy;

import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * native memory leak creator
 *
 * @author panlijun
 */
public class NativeMemoryLeak {
    private final Logger log = getLogger(getClass());

    public static final int TEN_THOUS = 10000;

    private Map<String, String> baseLoop() {
        HashMap<String, String> rubbish = new HashMap<>(TEN_THOUS);
        for (int i = 0; i < TEN_THOUS >> 2; i++) {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100000);
            byteBuffer.put(("乱七八糟的文章" + "更多的乱七八糟的文章" + "还要多的乱七八糟的文章" + ThreadLocalRandom.current().nextInt()).getBytes());
            byteBuffer.flip();
            byte[] value = new byte[byteBuffer.limit()];

            ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(100000);
            byteBuffer1.put(("key" + ThreadLocalRandom.current().nextInt()).getBytes());
            byteBuffer1.flip();
            byte[] key = new byte[byteBuffer1.limit()];

            rubbish.put(new String(key), new String(value));
        }
        return rubbish;
    }

    private Map<String, String> loop(int times) throws InterruptedException {
        HashMap<String, String> rubbishBundle = new HashMap<>();
        for (int i = 1; i <= times; i++) {
            baseLoop();
//            rubbishBundle.putAll(baseLoop());
            Thread.sleep(10);
            if (i % 10 == 0) {
                log.info("已生成{}批数据。size:{}", i, rubbishBundle.size());
            }
        }
        return rubbishBundle;

    }

    Map<String, String> produce(int times) throws InterruptedException {
        assert times > 0;
        Map<String, String> rubbishBundle = loop(times);
        log.info("批量垃圾生成结束。");
        return rubbishBundle;
    }


    //用于linux上执行
    public static void main(String[] args) throws InterruptedException {
        //case
        final int times = 2000;
        //when
        NativeMemoryLeak nativeMemoryLeak = new NativeMemoryLeak();
        Map<String, String> rubbishBundle = nativeMemoryLeak.produce(times);
    }

}
