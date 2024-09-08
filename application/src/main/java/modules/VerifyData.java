package modules;

public class VerifyData {
  private static String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
  private static String usernameRegex = "^[a-zA-Z0-9]{6,}$";
  private static String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

  public static boolean verifyEmail(String email) {
    return email.matches(emailRegex);
  }

  public static boolean verifyUsername(String username) {
    return username.matches(usernameRegex);
  }

  public static boolean verifyPassword(String password) {
    return password.matches(passwordRegex);
  }
}
