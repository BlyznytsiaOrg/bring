package testdata.di.positive.prototype.off;

import java.util.UUID;

import lombok.Getter;

@Getter
public class SimpleClass {
  
  private final UUID uuid = UUID.randomUUID();

}
