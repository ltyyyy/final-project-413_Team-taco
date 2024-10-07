package handler;

import org.bson.Document;

import dao.TransactionDao;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

public class PublicTransactionFeedHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        TransactionDao transactionDao = TransactionDao.getInstance();

        // Assuming no authentication is required for public transaction feed
        var res = new RestApiAppResponse<>(true, transactionDao.query(new Document()), null);
        return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(res);
    }
}
