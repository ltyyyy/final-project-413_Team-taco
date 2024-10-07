package handler;

import dao.UserDao;
import dto.UserDto;
import handler.AuthFilter.AuthResult;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

public class GetContactsHandler implements BaseHandler {

  @Override
  public HttpResponseBuilder handleRequest(ParsedRequest request) {
    UserDao userDao = UserDao.getInstance();
    AuthResult authResult = AuthFilter.doFilter(request);
    
    if (!authResult.isLoggedIn) {
      return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
    }

    // Query the current user based on the username.
    UserDto userDto = userDao.query(new Document("userName", authResult.userName)).get(0);

    // Extract the contacts list from the userDto.
    userDto.getContacts();

    var res = new RestApiAppResponse<>(true, List.of(userDto), null);

    // Build the HTTP response.
    return new HttpResponseBuilder()
        .setStatus(StatusCodes.OK)
        .setBody(res);
  }
}