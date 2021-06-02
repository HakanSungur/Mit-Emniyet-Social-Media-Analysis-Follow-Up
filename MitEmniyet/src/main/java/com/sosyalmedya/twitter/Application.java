package com.sosyalmedya.twitter;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.sql.SQLOutput;
import java.util.List;

public class Application {
    public static void main(String[] args) throws TwitterException {

        MongoClient mongoClient = new MongoClient(Constants.MONGO_HOST, Constants.MONGO_PORT);
        MongoDatabase twitterDB=mongoClient.getDatabase("twitterDB");
        MongoCollection<Document> searchCollection = twitterDB.getCollection("search");
        //Ben bu tokenları sildim. Siz kendiniz oluşturmanız gerekmektedir.
        ConfigurationBuilder configurationBuilder = ConfigurationTwitter.getConfig();

        TwitterFactory twitterFactory=new TwitterFactory(configurationBuilder.build());
        Twitter factoryInstance = twitterFactory.getInstance();

        //kullanıcı adına göre sorgulama yapılacağında kullanılacak kod.
        /*
        List<Status> responseTimeLineList = factoryInstance.getUserTimeline("mugeanli", new Paging(1,200));

        for(Status status:responseTimeLineList){
        //null pointer hatası fırlatmasın diye if bloğu içine almamız gerekiyor.
            GeoLocation geoLocation=status.getGeoLocation();
            if(geoLocation!=null) {
                System.out.println(status.getGeoLocation().getLatitude());
                System.out.println(status.getCreatedAt());
                System.out.println(status.getPlace());
                System.out.println(status.getLang());
                System.out.println(status.getText());
                System.out.println(status.getSource());

            }
        } */

        Query query=new Query();
        query.setQuery("toplanıyoruz");
        query.setCount(10100);
        QueryResult queryResult = factoryInstance.search(query);
        List<Status> tweets = queryResult.getTweets();
        for(Status status:tweets){
            JSONObject jsonObject=new JSONObject();
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

            //System.out.println(status.getGeoLocation()+""+status.getCreatedAt()+""+status.getText()+""+status.getUser().getScreenName());

        }

    }
}
