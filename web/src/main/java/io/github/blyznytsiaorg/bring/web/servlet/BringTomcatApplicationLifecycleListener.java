package io.github.blyznytsiaorg.bring.web.servlet;

import io.github.blyznytsiaorg.bring.core.context.impl.BringApplicationContext;
import lombok.AllArgsConstructor;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

@AllArgsConstructor
public class BringTomcatApplicationLifecycleListener implements LifecycleListener {

  private final static String BEFORE_DESTROY_EVENT = "before_destroy";

  private final BringApplicationContext bringApplicationContext;

  @Override
  public void lifecycleEvent(LifecycleEvent lifecycleEvent) {
    if (lifecycleEvent.getType().equals(BEFORE_DESTROY_EVENT)) {
      bringApplicationContext.close();
    }
  }
}
