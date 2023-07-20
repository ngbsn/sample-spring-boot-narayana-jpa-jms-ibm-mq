package com.ngbsn.service;

import com.ngbsn.domain.entity.Entry;
import com.ngbsn.domain.repository.EntryRepository;
import com.ngbsn.jms.JMSProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TestService {
    private static final Logger logger
            = LoggerFactory.getLogger(TestService.class);

    @Autowired
    EntryRepository entryRepository;
    @Autowired
    JMSProducer jmsProducer;

    @Transactional
    public void process(final String message){
        Entry entry = Entry.builder()
                .message(message)
                .build();
        entryRepository.save(entry);
        jmsProducer.publish(message + "-PROCESSED");
        logger.info("Successfully processed message {}", message);
    }
}
