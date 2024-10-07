package dto;


public class SplitRequestDto {
    public final double amount;
    public final String fromId;
    public final String toId;


    // Constructors, getters, and setters

    public SplitRequestDto(double amount, String fromId, String toId) {
        this.amount = amount;
        this.fromId = fromId;
        this.toId = toId;
    }

    // public double getAmountPerPerson() {
    //     return amountPerPerson;
    // }

    // public void setAmountPerPerson(double amountPerPerson) {
    //     this.amountPerPerson = amountPerPerson;
    // }

    // public int getNumOfPeople() {
    //     return numOfPeople;
    // }

    // public void setNumOfPeople(int numOfPeople) {
    //     this.numOfPeople = numOfPeople;
    // }

    // public List<String> getRecipientIds() {
    //     return recipientIds;
    // }

    // public void setRecipientIds(List<String> recipientIds) {
    //     this.recipientIds = recipientIds;
    // }
}
