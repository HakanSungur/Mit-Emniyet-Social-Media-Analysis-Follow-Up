package com.sosyalmedya.twitter;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;

public class ConfigurationTwitter {

    public static ConfigurationBuilder getConfig(){
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(Constants.OAuthConsumerKey);
        configurationBuilder.setOAuthConsumerSecret(Constants.OAuthConsumerSecretKey);
        configurationBuilder.setOAuthAccessToken(Constants.OAuthConsumerAccessToken);
        configurationBuilder.setOAuthAccessTokenSecret(Constants.OAuthConsumerAccessTokenSecret);
        return configurationBuilder;
    }

    public static ArrayList<Status> getAdvancedSearch(Twitter twitter){
        Query query = new Query("toplanıyoruz");
        query.setSince("2021-06-01");
        query.setUntil("2021-06-02");
        int numberOfTweets = 100000;
        long lastID = Long.MAX_VALUE;
        ArrayList<Status> tweets = new ArrayList<Status>();
        while (tweets.size() < numberOfTweets) {
            if (numberOfTweets - tweets.size() > 100)
                query.setCount(100);
            else
                query.setCount(numberOfTweets - tweets.size());
            try {
                QueryResult result = twitter.search(query);
                tweets.addAll(result.getTweets());
                System.out.println(tweets.size() + " adet toplandı");
                for (Status t : tweets)
                    if (t.getId() < lastID) lastID = t.getId();

            } catch (TwitterException te) {
                System.out.println("Couldn't connect: " + te);
            }
            ;
            query.setMaxId(lastID - 1);
        }

        return tweets;
    }
}
