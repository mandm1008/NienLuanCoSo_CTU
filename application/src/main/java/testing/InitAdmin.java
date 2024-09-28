package testing;

import db.UserModel;

public class InitAdmin {
  private UserModel userModel;

  public InitAdmin(String username, String password, String email) {
    userModel = new UserModel(username, password, email);
  }

  public void save() {
    userModel.insert();
  }

  public static void main(String[] args) {
    new InitAdmin("admin", "private", "admin@private.com").save();
  }
}
