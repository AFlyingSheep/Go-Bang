import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class musicStuff {
    void playMusic(String path){
        try{
            File musicpath=new File(path);

            if(musicpath.exists()){
                AudioInputStream ai= AudioSystem.getAudioInputStream(musicpath);
                Clip clip=AudioSystem.getClip();
                clip.open(ai);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else{

            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
