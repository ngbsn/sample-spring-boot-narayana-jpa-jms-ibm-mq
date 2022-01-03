package com.greenfall.jms;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.JMSException;

import static com.ibm.msg.client.jms.JmsConstants.PASSWORD;
import static com.ibm.msg.client.jms.JmsConstants.USERID;
import static com.ibm.msg.client.wmq.common.CommonConstants.*;

@Configuration
public class Config {

    @Value("${ibm.hostName}")
    private String hostName;
    @Value("${ibm.queueManager}")
    private String queueManager;
    @Value("${ibm.channel}")
    private String channel;
    @Value("${ibm.port}")
    private Integer port;
    @Value("${ibm.userName}")
    private String userName;
    @Value("${ibm.password}")
    private String password;
    @Value("${ibm.clientId}")
    private String clientId;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() throws JMSException {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(getConnectionFactory());
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setSessionTransacted(true);
        factory.setConcurrency("10");
        return factory;
    }

    @Bean
    public MQQueueConnectionFactory getConnectionFactory() throws JMSException {
        final MQQueueConnectionFactory connectionFactory = new MQQueueConnectionFactory();
        connectionFactory.setHostName(hostName);
        connectionFactory.setPort(port);
        connectionFactory.setChannel(channel);
        connectionFactory.setQueueManager(queueManager);
        connectionFactory.setAppName(clientId);
        connectionFactory.setStringProperty(USERID, userName);
        connectionFactory.setStringProperty(PASSWORD, password);
        connectionFactory.setTransportType(WMQ_CM_CLIENT);
        connectionFactory.setIntProperty(WMQ_TARGET_CLIENT, WMQ_TARGET_DEST_MQ);
        return connectionFactory;
    }

    private CachingConnectionFactory getCachingConnectionFactory() throws JMSException {
        final CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(getConnectionFactory());
        cachingConnectionFactory.setCacheConsumers(false);
        cachingConnectionFactory.setCacheProducers(true);
        cachingConnectionFactory.setSessionCacheSize(10);
        cachingConnectionFactory.setClientId("test");
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }

    @Bean
    public JmsTemplate getJmsTemplate() throws JMSException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(getCachingConnectionFactory());
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.setDeliveryPersistent(false);
        jmsTemplate.setSessionTransacted(true);
        return jmsTemplate;
    }
}
