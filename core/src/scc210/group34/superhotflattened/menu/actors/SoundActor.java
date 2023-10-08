package scc210.group34.superhotflattened.menu.actors;

import com.badlogic.gdx.audio.Sound;

public class SoundActor {
    private boolean voice; //switch of voice
    private float volume;
    public SoundActor() {
        volume =20f;//default
        voice=true;
    }

    public void play(Sound sound) {
        if(voice){
            sound.play(volume);
        }
    }


    //set voice level
    public void setVoice(float value){
        volume=value;
    }
    public float getVoice(){
        return volume;
    }

    //switch to open or close music
    public void MusicOpen(boolean bool){
        voice=bool;
    }
    public boolean getswitch(){
        return voice;
    }
}
