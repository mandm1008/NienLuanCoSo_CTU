package modules;

import java.util.HashMap;
import java.util.concurrent.*;

import javafx.application.Platform;

public class ThreadCustom {
  private static HashMap<String, ThreadCustom> theadOnRun = new HashMap<>();

  public static void stop(String name) {
    if (theadOnRun.containsKey(name)) {
      theadOnRun.get(name).stop();
      theadOnRun.remove(name);
    }
  }

  public static void stopAll() {
    for (String name : theadOnRun.keySet()) {
      theadOnRun.get(name).stop();
    }
    theadOnRun.clear();
  }

  private ScheduledExecutorService scheduled;
  private int corePoolSize = 1;
  private int period = 1000;
  private int delay = 0;

  public ThreadCustom() {
    scheduled = Executors.newScheduledThreadPool(corePoolSize);
  }

  public ThreadCustom(int corePoolSize, int period, int delay) {
    scheduled = Executors.newScheduledThreadPool(corePoolSize);
    this.corePoolSize = corePoolSize;
    this.period = period;
    this.delay = delay;
  }

  public void runner(String key, Runnable runnable) {
    theadOnRun.put(key, this);
    Runnable runner = () -> {
      Platform.runLater(runnable);
    };
    scheduled.scheduleAtFixedRate(runner, delay, period, TimeUnit.MILLISECONDS);
  }

  public void stop() {
    scheduled.shutdownNow();
  }

  public boolean isShutdown() {
    return scheduled.isShutdown();
  }
}
