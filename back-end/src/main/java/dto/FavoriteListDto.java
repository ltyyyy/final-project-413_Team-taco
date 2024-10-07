package dto;

import org.bson.Document;

public class FavoriteListDto extends BaseDto {
    private String userName;
    private String favoriteListId;

    public String getFavoriteListId() {
        return favoriteListId;
    }

    public FavoriteListDto setFavoriteListId(String favoriteListId) {
        this.favoriteListId = favoriteListId;
        return this;
    }

    public FavoriteListDto() {
        super();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Document toDocument(){
        return new Document()
                .append("userName", userName)
                .append("favoriteListId", favoriteListId);
    }

    public static FavoriteListDto fromDocument(Document match) {
        var favoriteListDto = new FavoriteListDto();
        favoriteListDto.setUserName(match.getString("userName"));
        favoriteListDto.setFavoriteListId(match.getString("favoriteListId"));
        return favoriteListDto;
    }
}