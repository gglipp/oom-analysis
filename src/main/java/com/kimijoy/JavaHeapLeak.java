package com.kimijoy;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * java heap leak creator
 *
 * @author panlijun
 */
public class JavaHeapLeak {
    private final Logger log = getLogger(getClass());

    public static final int TEN_THOUS = 10000;

    private Map<String, String> baseLoop() {
        HashMap<String, String> rubbish = new HashMap<>(TEN_THOUS);
        for (int i = 0; i < TEN_THOUS>>2; i++) {
            String article = "乱七八糟的文章" + "更多的乱七八糟的文章" + "还要多的乱七八糟的文章" + ThreadLocalRandom.current().nextInt();
            rubbish.put("key" + ThreadLocalRandom.current().nextInt(), article);
        }
        return rubbish;
    }

    private Map<String, String> loop(int times) throws InterruptedException {
        HashMap<String, String> rubbishBundle = new HashMap<>(5 * TEN_THOUS * 10);
        for (int i = 1; i <= times; i++) {
            rubbishBundle.putAll(baseLoop());
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

}
