package com.kimijoy;

import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class JavaHeapLeakTest {

    /*
     *  JVM config
     *
     * -Xms20m
     * -Xmx20m
     * -XX:-UseGCOverheadLimit
     * -XX:+HeapDumpOnOutOfMemoryError
     * -XX:+PrintGCDetails
     * -XX:+PrintGCDateStamps
     *
     * 写入50000数据后堆内存溢出
     * */

    @Test
    public void should_get_java_heap_oom() throws InterruptedException {
        //case
        final int times = 2000;

        //when
        Throwable thrown = catchThrowable(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                JavaHeapLeak heapLeak = new JavaHeapLeak();
                Map<String, String> rubbishBundle = heapLeak.produce(times);
                Thread.sleep(30 * 1000);
            }
        });

        //then
        assertThat(thrown).isInstanceOf(OutOfMemoryError.class);

    }

}