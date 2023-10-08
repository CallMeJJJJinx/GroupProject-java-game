package scc210.group34.superhotflattened.menu.actors;


import com.badlogic.gdx.audio.Music;

public class MusicActor{
    private Music playingMusic;
    private boolean voice; //switch of voice
    private float volume;
    private Music curren_music;
    public MusicActor(Music music) {
        volume =0f;
        voice=true;
        curren_music=music;
        play(music);
    }

    public void play(Music music) {
        if(voice){
            stopMusic();
            playingMusic = music;
            music.setLooping(true);
            music.play();}
    }

    public void stopMusic() {
        if (playingMusic != null)
            playingMusic.stop();
    }

    //set voice level
    public void setVoice(float value){
        playingMusic.setVolume(value);
    }
    public float getVoice(){
        volume =playingMusic.getVolume();
        return volume;
    }

    //switch to open or close music
    public void MusicOpen(boolean bool){
        voice=bool;
        if(!voice) {
            setVoice(0);
            stopMusic();
        }else{
            curren_music.play();
        }

    }
    public boolean getswitch(){
        return voice;
    }
}