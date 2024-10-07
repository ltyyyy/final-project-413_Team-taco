package handler;

import dao.FavoriteListDao;
import dao.UserDao;
import dto.BaseDto;
import dto.FavoriteListDto;
import dto.UserDto;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

public class FavoriteListHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request){
        //FavoriteList Dto
        FavoriteListDto favoriteListDto = GsonTool.gson.fromJson(request.getBody(), dto.FavoriteListDto.class);

        //Favorite Dao
        FavoriteListDao favoriteListDao = FavoriteListDao.getInstance();

        //User dao
        UserDao userDao = UserDao.getInstance();

        // checking User is logged in
        AuthFilter.AuthResult authResult = AuthFilter.doFilter(request);
        if(!authResult.isLoggedIn){
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED); // check status codes
        }

        // check if the user to add to Favorite List is already in the database
        if (userDao.query(new Document("userName", favoriteListDto.getFavoriteListId())).size() == 0){
            var res = new RestApiAppResponse<>(false, null,
                    "Adding Unkown User");
            return new HttpResponseBuilder().setStatus("200 OK").setBody(res);
        }

        // check if user is already in Favoprite List
        if(favoriteListDao.queryUserNameAndFavoriteId(authResult.userName, favoriteListDto.getFavoriteListId()).size() !=0){
            var res = new RestApiAppResponse<>(false, null,
                    "user was already added");
            return new HttpResponseBuilder().setStatus("200 OK").setBody(res);
        }

        // adding user to Favorite List
        favoriteListDto.setUserName(authResult.userName);
        favoriteListDao.put(favoriteListDto);

        var res = new RestApiAppResponse<>(true, List.of(favoriteListDto), null);
        return new HttpResponseBuilder().setStatus("200 OK").setBody(res);

    }
}