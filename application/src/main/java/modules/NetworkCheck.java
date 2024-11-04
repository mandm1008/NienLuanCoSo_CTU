package modules;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ui.App;

public class NetworkCheck {
  private ThreadCustom threadCustom;
  private Runnable runOnInternet;
  private Runnable runOnNoInternet;
  private int countReCheck = 0;

  public NetworkCheck() {
    threadCustom = new ThreadCustom(1, 10000, 0);
  }

  public void start() {
    threadCustom.runner("networkCheck", () -> {
      if (check()) {
        if (!App.isInternet) {
          countReCheck = 0;
          App.isInternet = true;
          if (runOnInternet != null) {
            runOnInternet.run();
          }
        }
      } else {
        if (App.isInternet) {
          if (countReCheck < 2) {
            countReCheck++;
            App.getNotificationManager().notify("Không có kết nối mạng! Cố gắng thử lại...",
                NotificationManager.WARNING);
            return;
          }

          App.isInternet = false;
          if (runOnNoInternet != null) {
            runOnNoInternet.run();
          }
        }
      }
    });
  }

  public static boolean check() {
    try {
      InetAddress address = InetAddress.getByName("www.google.com");
      return !address.equals("");
    } catch (UnknownHostException e) {
      return false;
    }
  }

  public void setRunOnInternet(Runnable runOnInternet) {
    this.runOnInternet = runOnInternet;
  }

  public void setRunOnNoInternet(Runnable runOnNoInternet) {
    this.runOnNoInternet = runOnNoInternet;
  }

}
