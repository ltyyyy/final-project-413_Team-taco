package handler;

import dao.UserDao;
import dto.UserDto;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

public class CreateUserHandler implements BaseHandler{

  @Override
  public HttpResponseBuilder handleRequest(ParsedRequest request) {
    UserDto userDto = GsonTool.gson.fromJson(request.getBody(), UserDto.class);
    UserDao userDao = UserDao.getInstance();

    var query = new Document("userName", userDto.getUserName());
    var resultQ = userDao.query(query);
    
    RestApiAppResponse<?> res;
    
    // Check if username is null or empty
    if (userDto.getUserName() == null || userDto.getUserName().trim().isEmpty()) {
      res = new RestApiAppResponse<>(false, null, "Username cannot be empty");
      return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
    }
    if (userDto.getPassword() == null || userDto.getPassword().trim().isEmpty()) {
      res = new RestApiAppResponse<>(false, null, "Password cannot be empty");
      return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
    }

    if(resultQ.size()!= 0){
      res = new RestApiAppResponse<>(false, null, "Username already taken");
    }else{
      userDto.setPassword(DigestUtils.sha256Hex(userDto.getPassword()));
      userDao.put(userDto);
      res = new RestApiAppResponse<>(true,null, "User created");
    }
    
    return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(res);
  }
}
