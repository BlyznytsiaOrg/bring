package testdata.di.positive.qualifier.constructor;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Qualifier;
import io.github.blyznytsiaorg.bring.core.annotation.Service;

@Service
public class MusicPlayer {

  private Music music1;
  private Music music2;

  @Autowired
  public MusicPlayer(@Qualifier("classicMusic") Music music1, @Qualifier("rockMusic")Music music2) {
    this.music1 = music1;
    this.music2 = music2;
  }

  public String playMusic() {
    return music1.getSong() + " " + music2.getSong();
  }
}
