package testing;

import db.UserModel;

public class InitUser {
  private UserModel userModel;

  public InitUser(String username, String password, String email) {
    userModel = new UserModel(username, password, email);
  }

  public void save() {
    userModel.insert();
  }

  public static void main(String[] args) {
    new InitUser("unknow", "unknow", "unknow@private.com").save();
    new InitUser("admin", "private", "admin@private.com").save();
  }
}
