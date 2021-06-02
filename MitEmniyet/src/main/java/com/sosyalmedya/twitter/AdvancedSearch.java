package com.sosyalmedya.twitter;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;

public class AdvancedSearch {
    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient(Constants.MONGO_HOST, Constants.MONGO_PORT);

        MongoDatabase twitterDB = mongoClient.getDatabase("twitterDB");
        MongoCollection<Document> searchCollection = twitterDB.getCollection("search");

        ConfigurationBuilder cb = ConfigurationTwitter.getConfig();

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();


        ArrayList<Status> tweets = ConfigurationTwitter.getAdvancedSearch(twitter);

        for (int i = 0; i < tweets.size(); i++) {
            Status status = tweets.get(i);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put ("Screen Name",status.getUser().getScreenName());
            jsonObject.put("Twit",status.getText());
            jsonObject.put("Created Date",status.getCreatedAt().getTime());
            jsonObject.put("Followers count",status.getUser().getFollowersCount());
            jsonObject.put("Friends Count",status.getUser().getFriendsCount());
            String userEmail=status.getUser().getEmail();
            if(userEmail!=null){
                jsonObject.put("Email",userEmail);
            }
            searchCollection.insertOne(Document.parse(jsonObject.toString()));
        }
    }
}