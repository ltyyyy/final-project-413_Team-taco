package dto;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

public class UserDto extends BaseDto{

  private String userName;
  private String password;
  private Double balance = 0.0d;
  private List<String> contacts = new ArrayList<>();

  public List<String> getContacts() {
    return contacts;
  }

  public void setContacts(List<String> contacts) {
    this.contacts = contacts;
  }

  public UserDto() {
    super();
  }

  public UserDto(String uniqueId) {
    super(uniqueId);
  }

  public String getPassword() {
    return password;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

    @Override
    public Document toDocument() {
        
        return new Document()
                .append("userName", userName)
                .append("password", password)
                .append("balance", balance)
                .append("contacts", contacts); // add contacts
    }

    public static UserDto fromDocument(Document match) {
        var userDto = new UserDto();

        if(match.get("_id")!= null){
          userDto.loadUniqueId(match);
        }

        userDto.balance = match.getDouble("balance");
        userDto.userName = match.getString("userName");
        userDto.password = match.getString("password");
        userDto.contacts = match.getList("contacts", String.class);
        return userDto;
    }
}
