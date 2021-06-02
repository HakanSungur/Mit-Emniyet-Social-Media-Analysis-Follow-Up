package com.sosyalmedya.twitter;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;

public class MongoSink {
    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient(Constants.MONGO_HOST, Constants.MONGO_PORT);

        MongoDatabase twitterDB = mongoClient.getDatabase("twitterDB");
        MongoCollection<Document> searchCollection = twitterDB.getCollection("search");

        ConfigurationBuilder configurationBuilder = ConfigurationTwitter.getConfig();

        Twitter twitter = new TwitterFactory(configurationBuilder.build()).getInstance();

        ArrayList<Status> tweets = ConfigurationTwitter.getAdvancedSearch(twitter);

        for (int i = 0; i < tweets.size(); i++) {
            Status st = (Status) tweets.get(i);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Screen Name", st.getUser().getScreenName());
            jsonObject.put("Tweet", st.getText());
            jsonObject.put("Created Date", st.getCreatedAt().getTime());
            jsonObject.put("Followers Count", st.getUser().getFollowersCount());
            jsonObject.put("Friends Count", st.getUser().getFriendsCount());
            jsonObject.put("Description", st.getUser().getDescription());
            jsonObject.put("Favorite Count", st.getFavoriteCount());
            jsonObject.put("Retweet Count", st.getRetweetCount());
            String email = st.getUser().getEmail();
            if (email != null)
                jsonObject.put("Email", email);

            searchCollection.insertOne(Document.parse(jsonObject.toString()));
        }
    }
}
