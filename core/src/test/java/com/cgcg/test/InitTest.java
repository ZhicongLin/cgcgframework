package com.cgcg.test;

import lombok.extern.slf4j.Slf4j;
import org.cgcgframework.core.annotation.CBean;
import org.cgcgframework.core.annotation.CInit;
import org.cgcgframework.core.event.Async;
import org.cgcgframework.core.event.EventProcessor;
import org.cgcgframework.core.event.EventPublisher;

@Slf4j
@CBean
public class InitTest {

    @CInit
    public void handle() {
        final EventTest2 event = new EventTest2();
        event.setId(1L);
        EventPublisher.push(event);
        final Integer execute = EventPublisher.execute(event, Integer.class);
        System.out.println("execute = " + execute);
    }

    @Async
    @EventProcessor
    public void processor(EventTest eventTest) {
        log.info(eventTest.toString());
    }

    @EventProcessor
    public long processor2(EventTest2 eventTest) {
        return eventTest.getId();
    }

}
