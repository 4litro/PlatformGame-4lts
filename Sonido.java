import javax.sound.sampled.*;
import java.io.*;

public class Sonido {

    private String ruta;
    private AudioInputStream audioStream;
    private Clip clip;
    private Long microSegundos;

    public Sonido(String ruta){
        this.ruta = ruta;
        try{
            audioStream = AudioSystem.getAudioInputStream(new File(ruta).getAbsoluteFile());
            clip = AudioSystem.getClip();
        }catch(Exception e){}
    }

    public void playM() {
        try{
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            //clip.loop(0);
        } catch(Exception e){}
    }

    public void pauseM(){
        microSegundos = clip.getMicrosecondPosition();
        clip.stop();
    }

    public void resumeM(){
        clip.close();
        try{
            audioStream = AudioSystem.getAudioInputStream (new File(ruta).getAbsoluteFile());
            playM();
            clip.setMicrosecondPosition(microSegundos);
        }catch(Exception e){}
    }
    public void stopM(){
        microSegundos = 0L;
        clip.stop();
        clip.close();
    }

}
