package testdata.di.positive.qualifier.constructor;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Qualifier;
import com.bobocode.bring.core.anotation.Service;

@Service
public class MusicPlayer {

  private Music music1;
  private Music music2;

  @Autowired
  public MusicPlayer(@Qualifier("ClassicMusic") Music music1, @Qualifier("RockMusic")Music music2) {
    this.music1 = music1;
    this.music2 = music2;
  }

  public String playMusic() {
    return music1.getSong() + " " + music2.getSong();
  }
}
