package handler;

import dao.UserDao;
import dto.UserDto;
import handler.AuthFilter.AuthResult;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;
import org.bson.Document;

import java.util.List;

public class DeleteContactHandler implements BaseHandler {

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


        for (String contactUserName : contactDto.getContacts()) {
            if (existingContacts.contains(contactUserName)) {
                // Remove the contact
                existingContacts.remove(contactUserName);
            } else {
                // Contact username is not in the user's contact list
                res = new RestApiAppResponse<>(false, null, "Contact not found in your contacts");
                return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
            }
        }

        userDto.setContacts(existingContacts);
        userDao.put(userDto);

        res = new RestApiAppResponse<>(true, List.of(userDto), "User deleted from your contacts");
        return new HttpResponseBuilder()
            .setStatus(StatusCodes.OK)
            .setBody(res);
    }
}
