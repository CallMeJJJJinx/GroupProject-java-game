package scc210.group34.superhotflattened.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import scc210.group34.superhotflattened.menu.utils.R;

class fps implements Disposable {

    private SpriteBatch batch;
    private BitmapFont fpsBitmapFont;
    private static final float OCCUPY_HEIGHT_RATIO = 14.0f/480.0f;
    private static final float DISPLAY_ORIGIN_OFFSET_RATIO = 0.5f;
    private boolean show;

    private float x,y;
    public fps(AssetManager asset){
        BitmapFont font;
        font=asset.get(R.Font.FONT,BitmapFont.class);
        init(font,25);
    }

    public void init(BitmapFont BitFont,int RawSize) {
        show=true;
        this.batch = new SpriteBatch();
        this.fpsBitmapFont = BitFont;
        float height = Gdx.graphics.getHeight();
        float scale = (height * OCCUPY_HEIGHT_RATIO)/(float)RawSize;
        this.fpsBitmapFont.getData().setScale(scale);
        float scaledFontSize = RawSize * scale;
        float offset = scaledFontSize * DISPLAY_ORIGIN_OFFSET_RATIO;
        x = scaledFontSize - offset;
        y = scaledFontSize * 1.85f - offset;
    }

    public void render(){
        if(batch!=null) {
            batch.begin();
            if (show) fpsBitmapFont.draw(batch, "FPS:" + Gdx.graphics.getFramesPerSecond(), x, y);
            batch.end();
        }
    }

    public void dispose() {
        if(batch != null) {
            batch.dispose();
            batch = null;
        }
    }
    public boolean getFps(){
        return show;
    }
    public void setFps(boolean bool){
        show=bool;
        if(!show){
        }else{
            render();
        }
    }
}
