package com.kimijoy;

import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.slf4j.LoggerFactory.getLogger;

public class NativeMemoryLeakTest {

    private final Logger log = getLogger(getClass());

    /*
     *  JVM config
     *
     * -Xms20m
     * -Xmx20m
     * -XX:-UseGCOverheadLimit
     * -XX:MaxDirectMemorySize=1m
     * -XX:+HeapDumpOnOutOfMemoryError
     * -XX:+PrintGCDetails
     * -XX:+PrintGCDateStamps
     *
     * 写入50000数据后堆内存溢出
     * */

    @Test
    public void should_not_throw_any_exception() throws InterruptedException {
        //case
        final int times = 2000;

        //when
        Throwable thrown = catchThrowable(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                NativeMemoryLeak nativeMemoryLeak = new NativeMemoryLeak();
                Map<String, String> rubbishBundle = nativeMemoryLeak.produce(times);
            }
        });

        //then
        assertThat(thrown).isNull();

    }

    @Test
    public void should_getter_and_setter_by_direct_memory(){
        //case
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(100);
        log.info("info before init : {}", directBuffer);
        directBuffer.put("直接内存信息。".getBytes());
        //when
        log.info("info after init : {}", directBuffer.toString());
        directBuffer.flip();
        log.info("info after flip : {}", directBuffer.toString());
        
        byte[] dst = new byte[directBuffer.limit()];
        directBuffer.get(dst);
        log.info("info after get : {}, data : {}", directBuffer.toString(), new String(dst));

        directBuffer.clear();
        log.info("info after reset : {}", directBuffer);
        //then

    }

}