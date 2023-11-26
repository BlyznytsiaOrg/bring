package testdata.di.positive.prototype.onwithinterface;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SimpleClass {
  
  private final UUID uuid = UUID.randomUUID();

}
