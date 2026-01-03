//package com.kutay.exchange.config;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@ConfigurationProperties(prefix = "event-topics") // bind config property "event-topics" to this file
//@Component
//@Getter
//@Setter
//public class EventTopicConfig {
//    private Map<String, String> mappings;
//}

// currently not needed since we use Topics class inside kafka folder for topic definitions