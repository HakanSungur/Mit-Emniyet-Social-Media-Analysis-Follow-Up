package com.sosyalmedya.twitter;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Properties;

public class KafkaProducerApp {
    public static void main(String[] args) {

        Properties config=new Properties();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"167.177.67.77:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(config);

        ConfigurationBuilder cb = ConfigurationTwitter.getConfig();

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();

        ArrayList<Status> tweets = ConfigurationTwitter.getAdvancedSearch(twitter);
        //int count=1;
        for (Status status : tweets) {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("Screen Name", status.getUser().getScreenName());
            jsonObject.put("Tweet", status.getText());
            jsonObject.put("Created Date", status.getCreatedAt().getTime());
            jsonObject.put("Followers Count", status.getUser().getFollowersCount());
            jsonObject.put("Friends Count", status.getUser().getFriendsCount());
            jsonObject.put("Description", status.getUser().getDescription());
            jsonObject.put("Favorite Count", status.getFavoriteCount());
            jsonObject.put("Retweet Count", status.getRetweetCount());
            String email = status.getUser().getEmail();
            if (email != null) {
                jsonObject.put("Email", email);
            }

            //count++;

            ProducerRecord<String, String> rec = new ProducerRecord<String, String>(Constants.KafkaTopicName, jsonObject.toString());
            producer.send(rec);
        }
    }
}

