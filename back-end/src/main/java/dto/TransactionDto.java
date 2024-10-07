package dto;

import org.bson.Document;

import java.time.Instant;

public class TransactionDto extends BaseDto{

  private String userId;
  private String toId;
  private Double amount;
  private TransactionType transactionType;
  private Long timestamp;
  private Double userBalance;
  private Double toUserBalance;

  public TransactionDto(){
    timestamp = Instant.now().toEpochMilli();
  }

  public TransactionDto(String uniqueId) {
    super(uniqueId);
    timestamp = Instant.now().toEpochMilli();
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getToId() {
    return toId;
  }

  public void setToId(String toId) {
    this.toId = toId;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Double getUserBalance() {
    return userBalance;
  }

  public void setUserBalance(Double userBalance) {
    this.userBalance = userBalance;
  }

  public Double getToUserBalance() {
    return toUserBalance;
  }

  public void setToUserBalance(Double toUserBalance) {
    this.toUserBalance = toUserBalance;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
  }

  public Document toDocument(){
    Document document = new Document();
    document.append("userId", userId)
            .append("toId", toId)
            .append("amount", amount)
            .append("transactionType", transactionType.toString())
            .append("userBalance", userBalance)
            .append("toUserBalance", toUserBalance)
            .append("timestamp", timestamp);
    return document;
  }

  public static TransactionDto fromDocument(Document document) {
    TransactionDto transactionDto = new TransactionDto();
    transactionDto.setUserId(document.getString("userId"));
    transactionDto.setToId(document.getString("toId"));
    transactionDto.setAmount(document.getDouble("amount"));
    transactionDto.setTransactionType(TransactionType.valueOf(document.getString("transactionType")));
    transactionDto.setUserBalance(document.getDouble("userBalance"));
    transactionDto.setToUserBalance(document.getDouble("toUserBalance"));
    transactionDto.setTimestamp(document.getLong("timestamp"));
    transactionDto.setUniqueId(document.getObjectId("_id").toHexString());
    return transactionDto;
  }

  public static TransactionDto createBillTransaction(String userId, String toId, Double amount) {
    TransactionDto billTransaction = new TransactionDto();
    billTransaction.setUserId(userId);
    billTransaction.setToId(toId);
    billTransaction.setAmount(amount);
    billTransaction.setTransactionType(TransactionType.Split);
    billTransaction.setTimestamp(Instant.now().toEpochMilli());

    return billTransaction;
  }
}
