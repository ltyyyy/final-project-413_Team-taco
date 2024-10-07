package handler;

import request.ParsedRequest;

public class HandlerFactory {
  // routes based on the path. Add your custom handlers here
  public static BaseHandler getHandler(ParsedRequest request) {
    return switch (request.getPath()) {
      case "/createUser" -> new CreateUserHandler();
      case "/login" -> new LoginHandler();
      case "/getTransactions" -> new GetTransactionsHandler();
      case "/createDeposit" -> new CreateDepositHandler();
      case "/withdraw" -> new WithdrawHandler();
      case "/split" -> new SplitHandler();
      case "/transfer" -> new TransferHandler();
      case "/FavoriteList" -> new FavoriteListHandler(); // By: Guillermo Alcnatara
      case "/publicTransactionFeed" -> new PublicTransactionFeedHandler();
      case "/createContact" -> new CreateContactHandler();
      case "/getContacts" -> new GetContactsHandler();
      case "/deleteContact" -> new DeleteContactHandler();
      // Add more cases for other paths/handlers
      default -> new FallbackHandler();
    };
  }

}
