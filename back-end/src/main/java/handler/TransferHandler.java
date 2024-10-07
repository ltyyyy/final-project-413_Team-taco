package handler;

import dao.TransactionDao;
import dao.UserDao;
import dto.TransactionDto;
import dto.TransactionType;
import dto.TransferRequestDto;
import dto.UserDto;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

public class TransferHandler implements BaseHandler {

    @Override 
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        
        TransactionDao transactionDao = TransactionDao.getInstance();
        UserDao userDao = UserDao.getInstance();

        TransferRequestDto transferRequestDto = GsonTool.gson.fromJson(request.getBody(), TransferRequestDto.class);

        AuthFilter.AuthResult authResult = AuthFilter.doFilter(request);
        if(!authResult.isLoggedIn) {
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
        }

        RestApiAppResponse<?> res;

         // Check if the toId is empty or null
        if (transferRequestDto.toId == null || transferRequestDto.toId.trim().isEmpty()) {
            res = new RestApiAppResponse<>(false, null, "Recipient username cannot be empty");
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
        }
        if(transferRequestDto.amount <= 0) {
            res = new RestApiAppResponse<>(false, null, "Invalid amount.");
            return new HttpResponseBuilder().setStatus("400 Bad Request").setBody(res);
        }


        UserDto fromUser = userDao.query(new Document("userName", authResult.userName)).iterator().next();
        if(fromUser == null) {
            res = new RestApiAppResponse<>(false, null, "Invalid from user");
            return new HttpResponseBuilder().setStatus("400 Bad Request").setBody(res);
        }

        // Check if the recipient username exists
        List<UserDto> toUserList = userDao.query(new Document("userName", transferRequestDto.toId));
        if (toUserList.isEmpty()) {
            res = new RestApiAppResponse<>(false, null, "Invalid user to transfer. User Does not exist.");
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
        }
    
        UserDto toUser = userDao.query(new Document("userName", transferRequestDto.toId)).iterator().next();
        if(toUser ==  null) {
             res = new RestApiAppResponse<>(false, null, "Invalid user to transfer.");
            return new HttpResponseBuilder().setStatus("400 Bad Request").setBody(res);
        }


        if(fromUser.getBalance() < transferRequestDto.amount){ 
             res = new RestApiAppResponse<>(false, null, "Not enough funds.");
            return new HttpResponseBuilder().setStatus("400 Bad Request").setBody(res);
        }

        if(fromUser.getUserName().equals(toUser.getUserName())) {
            res = new RestApiAppResponse<>(false, null, "Cannot transfer to yourself.");
            return new HttpResponseBuilder().setStatus("400 Bad Request").setBody(res);
        }


        fromUser.setBalance(fromUser.getBalance() - transferRequestDto.amount);
        toUser.setBalance(toUser.getBalance() + transferRequestDto.amount);
        userDao.put(fromUser);
        userDao.put(toUser);

        var transaction = new TransactionDto();
        transaction.setTransactionType(TransactionType.Transfer);
        transaction.setAmount(transferRequestDto.amount);
        transaction.setToId(transferRequestDto.toId);
        transaction.setUserId(authResult.userName);
        transaction.setUserBalance(fromUser.getBalance());
        transaction.setToUserBalance(toUser.getBalance());
        transactionDao.put(transaction);

        res = new RestApiAppResponse<>(true, List.of(fromUser, toUser), "Transfer Successful.");
        return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(res);
}
}

