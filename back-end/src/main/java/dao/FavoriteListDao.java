package dao;

import com.mongodb.client.MongoCollection;
import dto.BaseDto;
import dto.FavoriteListDto;
import dto.UserDto;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FavoriteListDao extends BaseDao<FavoriteListDto> {
    private static FavoriteListDao instance;

    protected FavoriteListDao(MongoCollection collection) {
        super(collection);
    }

    public static FavoriteListDao getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new FavoriteListDao(MongoConnection.getCollection("FavoriteListDao"));
        return instance;
    }

    public static FavoriteListDao getInstance(MongoCollection collection) {
        instance = new FavoriteListDao(collection);
        return instance;
    }

    @Override
    public void put(FavoriteListDto favoriteListDto) {
        collection.insertOne(favoriteListDto.toDocument());
    }

    @Override
    public List<FavoriteListDto> query(Document filter) {
        return collection.find(filter)
                .into(new ArrayList<>())
                .stream()
                .map(FavoriteListDto::fromDocument)
                .collect(Collectors.toList());
    }

    // query by userName and Id so that user cant add the same person twice
    public List<FavoriteListDto> queryUserNameAndFavoriteId(String userName, String favoriteListId) {
        return query(new Document("userName", userName).append("favoriteListId", favoriteListId));
    }
}
