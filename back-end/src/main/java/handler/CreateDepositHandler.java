package handler;

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

public class CreateDepositHandler implements BaseHandler {

  @Override
  public HttpResponseBuilder handleRequest(ParsedRequest request) {
      TransactionDto transactionDto = GsonTool.gson.fromJson(request.getBody(), TransactionDto.class);
      TransactionDao transactionDao = TransactionDao.getInstance();
      UserDao userDao = UserDao.getInstance();

      AuthFilter.AuthResult authResult = AuthFilter.doFilter(request);
      if(!authResult.isLoggedIn) {
          return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
      }

      UserDto userDto = userDao.query(new Document("userName", authResult.userName)).get(0);

      
      if(transactionDto.getAmount() <=0){
        var res = new RestApiAppResponse<>(false, null, "Invalid amount.");
        return new HttpResponseBuilder().setStatus("400 Bad Request").setBody(res);
    }
    

      transactionDto.setTransactionType(TransactionType.Deposit);
      transactionDto.setUserId(authResult.userName);
      transactionDto.setUserBalance(userDto.getBalance() + transactionDto.getAmount());
      transactionDao.put(transactionDto);

      // Deduct the amount from the user's balance
      userDto.setBalance(userDto.getBalance() + transactionDto.getAmount());
      userDao.put(userDto);

      

      var res = new RestApiAppResponse<>(true, List.of(transactionDto), "Deposit successful");
      return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(res);
  }
}