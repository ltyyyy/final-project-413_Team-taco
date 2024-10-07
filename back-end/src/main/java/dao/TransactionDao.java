package dao;

import com.mongodb.client.MongoCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dto.TransactionDto;
import org.bson.Document;

// TODO fill this out
public class TransactionDao extends BaseDao<TransactionDto> {

  private static TransactionDao instance;

  private TransactionDao(MongoCollection<Document> collection) {
    super(collection);
  }

  public static TransactionDao getInstance() {
    if (instance != null) {
      return instance;
    }
    instance = new TransactionDao(MongoConnection.getCollection("TransactionDao"));
    return instance;
  }

  public static TransactionDao getInstance(MongoCollection<Document> collection) {
    instance = new TransactionDao(collection);
    return instance;
  }

  public List<TransactionDto> query(Document filter) {
    // TODO please use find and into
    // List<TransactionDto> transactionDtos = new ArrayList<>();
    // List<Document> documents = collection.find(filter).into(new ArrayList<>());

    // for (Document document : documents) {
    // TransactionDto transactionDto = TransactionDto.fromDocument(document);
    // transactionDtos.add(transactionDto);
    // }
    // return transactionDtos;
    return collection.find(filter)
        .into(new ArrayList<>())
        .stream()
        .map(TransactionDto::fromDocument)
        .collect(Collectors.toList());
  }

  public List<TransactionDto> getAllTransactions() {
    return collection.find()
        .into(new ArrayList<>())
        .stream()
        .map(TransactionDto::fromDocument)
        .collect(Collectors.toList());
  }

}
