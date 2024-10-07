package handler;

import dao.TransactionDao;
import dao.UserDao;
import dto.SplitRequestDto;
import dto.TransactionDto;
import dto.TransactionType;
import dto.UserDto;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

public class SplitHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
       TransactionDao transactionDao = TransactionDao.getInstance();
        UserDao userDao = UserDao.getInstance();

        SplitRequestDto splitRequestDto = GsonTool.gson.fromJson(request.getBody(), SplitRequestDto.class);

        AuthFilter.AuthResult authResult = AuthFilter.doFilter(request);
        if(!authResult.isLoggedIn) {
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
        }

        UserDto fromUser = userDao.query(new Document("userName", authResult.userName)).iterator().next();
        if(fromUser == null) {
            var res = new RestApiAppResponse<>(false, null, "Invalid from user");
            return new HttpResponseBuilder().setStatus("400 Bad Request").setBody(res);
        }

        UserDto toUser = userDao.query(new Document("userName", splitRequestDto.toId)).iterator().next();
        if(toUser == null) {
            var res = new RestApiAppResponse<>(false, null, "Invalid user to transfer.");
            return new HttpResponseBuilder().setStatus("400 Bad Request").setBody(res);
        }

        if(splitRequestDto.amount <= 0) {
            var res = new RestApiAppResponse<>(false, null, "Invalid amount.");
            return new HttpResponseBuilder().setStatus("400 Bad Request").setBody(res);
        }else if(fromUser.getBalance() < splitRequestDto.amount){ 
            var res = new RestApiAppResponse<>(false, null, "Not enough funds.");
            return new HttpResponseBuilder().setStatus("400 Bad Request").setBody(res);
        }else if(toUser.getBalance() < splitRequestDto.amount){ 
            var res = new RestApiAppResponse<>(false, null, "Not enough funds.");
            return new HttpResponseBuilder().setStatus("400 Bad Request").setBody(res);
        }

        fromUser.setBalance(fromUser.getBalance() - (splitRequestDto.amount)/2);
        toUser.setBalance(toUser.getBalance() - (splitRequestDto.amount)/2);
        userDao.put(fromUser);
        userDao.put(toUser);

        var transaction = new TransactionDto();
        transaction.setTransactionType(TransactionType.Split);
        transaction.setAmount(splitRequestDto.amount);
        transaction.setToId(splitRequestDto.toId);
        transaction.setUserBalance(fromUser.getBalance());
        transaction.setToUserBalance(toUser.getBalance());
        transaction.setUserId(authResult.userName);
        transactionDao.put(transaction);

        var res = new RestApiAppResponse<>(true, List.of(fromUser, toUser), null);
        return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(res);
    }
}
