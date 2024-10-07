package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import dto.FavoriteListDto;
import dto.UserDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bson.Document;
import org.bson.types.ObjectId;

public class UserDao extends BaseDao<UserDto> {

  private static UserDao instance;

  private UserDao(MongoCollection<Document> collection){
    super(collection);
  }

  public static UserDao getInstance(){
    if(instance != null){
      return instance;
    }
    instance = new UserDao(MongoConnection.getCollection("UserDao"));
    return instance;
  }

  public static UserDao getInstance(MongoCollection<Document> collection){
    instance = new UserDao(collection);
    return instance;
  }


  public List<UserDto> query(Document filter){
    // // todo please use find and into
    // List<UserDto> userDtos = new ArrayList<>();
    // List<Document> documents = collection.find(filter).into(new ArrayList<>());

    // for (Document document: documents){
    //   UserDto userDto = UserDto.fromDocument(document);
    //   userDtos.add(userDto);
    // }
    // return userDtos;
    return collection.find(filter)
        .into(new ArrayList<>())
        .stream()
        .map(UserDto::fromDocument)
        .collect(Collectors.toList());
  }

}
