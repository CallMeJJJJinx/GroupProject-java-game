package scc210.group34.superhotflattened.menu.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import scc210.group34.superhotflattened.menu.GameManager;
import scc210.group34.superhotflattened.menu.GameScreen;
import scc210.group34.superhotflattened.menu.actors.ImageActor;
import scc210.group34.superhotflattened.menu.utils.R;


public class PreferenceStage extends IStage {
    private Skin skin;
    private Window options;
    private CheckBox checkBoxSound;
    private CheckBox checkBoxMusic;
    private Slider sliderSound;
    private Slider sliderMusic;
    private CheckBox checkShowFpsCounter;
    private GameManager main;
    private ImageActor background;

    public PreferenceStage(GameManager gameManager, Viewport view) {
        super(gameManager, view);
        main=gameManager;
        init();
    }

    private void init() {
        TextureAtlas atlas=new TextureAtlas("image1/uiskin/uiskin.atlas");
        skin = new Skin(Gdx.files.local("image1/uiskin/uiskin.json"), atlas);
        BuildWindow();
        load(main);
        this.addActor(options);
        options.setVisible(true);
    }

    //load the initial state of setting
    private void load(GameManager main){
        checkBoxSound.setChecked(main.getSoundActor().getswitch());
        sliderSound.setValue(main.getSoundActor().getVoice());
        checkBoxMusic.setChecked(main.getMusicActor().getswitch());
        sliderMusic.setValue(main.getMusicActor().getVoice());
        checkShowFpsCounter.setChecked(main.getFPS());
    }

    private void BuildWindow() {
        options = new Window("Options", skin);
        options.setSize(getWidth()*50/100f,getHeight()*50/100f);
        options.add(AudioSetting()).row();
        options.add(FPS_Setting()).row();
        options.add(Save_Button()).pad(10, 0, 10, 0);
        options.setColor(1, 1, 1, 0.8f);
        options.setVisible(false);
        options.pack();
        options.setPosition( getWidth()/4, getHeight()/4);
        options.scaleBy(1.25f);
    }


    //slider to set voice of nmusic and sound
    private Table AudioSetting() {
        Table table = new Table();

        //title
        table.pad(10, 10, 0, 10);
        table.add(new Label("Audio", skin, "default-font", Color.RED))
                .colspan(3);
        table.row();
        table.columnDefaults(0).padRight(10);
        table.columnDefaults(1).padRight(10);


        //sound
        checkBoxSound = new CheckBox("", skin);
        table.add(checkBoxSound);
        table.add(new Label("Sound", skin));
        sliderSound = new Slider(0.0f, 1.0f, 0.1f, false, skin);
        table.add(sliderSound);
        table.row();

        //music
        checkBoxMusic = new CheckBox("", skin);
        table.add(checkBoxMusic);
        table.add(new Label("Music", skin));
        sliderMusic = new Slider(0.0f, 1.0f, 0.1f, false, skin);
        table.add(sliderMusic);
        table.row();
        table.setSize(50f,50f);
        return table;
    }

    //fps show or not with checkbox
    private Table FPS_Setting() {
        Table table = new Table();
        table.row();
        table.columnDefaults(0).padRight(10);
        table.columnDefaults(1).padRight(10);
        checkShowFpsCounter = new CheckBox("", skin);
        table.add(new Label("Show FPS Counter", skin));
        table.add(checkShowFpsCounter);
        table.row();
        return table;
    }

    //save and cancel buttons
    private Table Save_Button() {
        Table table = new Table();
        table.row();
        //save
        TextButton save = new TextButton("Save", skin);
        table.add(save).padRight(30);
        save.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                save();
            }
        });


        //cancel
        TextButton cancel = new TextButton("Cancel", skin);
        table.add(cancel);
        cancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cancel();
            }
        });
        return table;
    }


    //save the settings and apply
    public void save(){
        main.getSoundActor().MusicOpen(checkBoxSound.isChecked());
        main.getSoundActor().setVoice(sliderSound.getValue());

        main.getMusicActor().MusicOpen(checkBoxMusic.isChecked());
        main.getMusicActor().setVoice(sliderMusic.getValue());

        main.setFPS(checkShowFpsCounter.isChecked());
        cancel();
    }
    public void cancel(){
        options.setVisible(false);
        options.clear();
        getGameManager().getGameScreen().showMenu();
    }
}
