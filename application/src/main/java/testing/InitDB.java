package testing;

public class InitDB {
  public static void main(String[] args) {
    InitUser.main(args);
    System.out.println("Admins initialized");
    InitMusic.main(args);
    System.out.println("Musics initialized");
  }
}
