package handler;

import com.google.gson.JsonSyntaxException;
import dao.AuthDao;
import dao.TransactionDao;
import dao.UserDao;
import dto.TransactionDto;
import dto.TransactionType;
import dto.UserDto;
import handler.AuthFilter.AuthResult;

import java.util.List;

import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

public class CreateContactHandler implements BaseHandler {

    @Override
  public HttpResponseBuilder handleRequest(ParsedRequest request) {
    UserDto contactDto = GsonTool.gson.fromJson(request.getBody(), UserDto.class);
    UserDao userDao = UserDao.getInstance();

    AuthResult authResult = AuthFilter.doFilter(request);
    if (!authResult.isLoggedIn) {
      return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
    }

    UserDto userDto = userDao.query(new Document("userName", authResult.userName)).get(0);
    List<String> existingContacts = userDto.getContacts(); // Get existing contacts

    RestApiAppResponse<?> res;

    // Loop through each contact that the user wants to add
    for (String contactUserName : contactDto.getContacts()) {
      // Check if the contact is not the user themselves
      if (authResult.userName.equals(contactUserName)) {
        res = new RestApiAppResponse<>(false, null, "You cannot add yourself to your contacts");
        return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
      }
      // Check if the contact is not already in the user's contact list
      if (!existingContacts.contains(contactUserName)) {
        // Check if the contact username exists in the system
        if (userDao.query(new Document("userName", contactUserName)).size() != 0) {
          // The contact exists, add it to the user's contacts
          existingContacts.add(contactUserName);
        } else {
          // The contact username does not exist, return an error
          res = new RestApiAppResponse<>(false, null, "Username does not exist");
          return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
        }
      } else {
        // The contact username is already in the user's contact list
        res = new RestApiAppResponse<>(false, null, "User already exists in contacts");
        return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
      }
    }

    // If this point is reached, new contacts have been added
    userDto.setContacts(existingContacts);
    userDao.put(userDto);
    res = new RestApiAppResponse<>(true, List.of(userDto), "User added to contacts");

    // Return a success response
    return new HttpResponseBuilder()
        .setStatus(StatusCodes.OK)
        .setBody(res);
  }
}