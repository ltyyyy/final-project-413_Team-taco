package handler;

import dao.AuthDao;
import dao.UserDao;
import dto.AuthDto;
import java.time.Instant;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

class LoginDto{
  String userName;
  String password;
}

public class LoginHandler implements BaseHandler{

  @Override
  public HttpResponseBuilder handleRequest(ParsedRequest request) {

    // todo
    LoginDto userDto = GsonTool.gson.fromJson(request.getBody(), LoginDto.class);
    UserDao userDao = UserDao.getInstance();
    AuthDao authDao = AuthDao.getInstance();

    // if the given username exists
    var userQuery = new Document("userName", userDto.userName);
    var users = userDao.query(userQuery);


    // If no user is found
    if (users.isEmpty()) {
      var res = new RestApiAppResponse<>(false, null, "User not found");
        return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
    }

    //  check if the password matches
    userQuery.append("password", DigestUtils.sha256Hex(userDto.password));
    var userWithCorrectPassword = userDao.query(userQuery);

    //incorrect password
    if (userWithCorrectPassword.isEmpty()) {
      var res = new RestApiAppResponse<>(false, null, "Incorrect password");
      return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
    }

    // login
    AuthDto authDto = new AuthDto();
    authDto.setExpireTime(Instant.now().getEpochSecond() + 30000); 
    String hash = DigestUtils.sha256Hex(userDto.userName + authDto.getExpireTime());
    authDto.setHash(hash);
    authDto.setUserName(userDto.userName);
    authDao.put(authDto); 

    // login success
      var res = new RestApiAppResponse<>(true, null, "Login successful");
      return new HttpResponseBuilder().setStatus(StatusCodes.OK).setHeader("Set-Cookie", "auth=" + hash).setBody(res);

  }
}
