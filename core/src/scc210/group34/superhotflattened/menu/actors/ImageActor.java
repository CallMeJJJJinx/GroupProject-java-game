package scc210.group34.superhotflattened.menu.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ImageActor extends Actor implements Poolable{
	private TextureRegion texture;

	public ImageActor(TextureRegion texture) {
		this.texture = texture;
		if(texture == null) {
			return;
		}
		setSize(texture.getRegionWidth(),texture.getRegionHeight());
	}

	public ImageActor() {
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(texture == null) {
			return;
		}
		drawActor(batch);
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public void drawActor(Batch batch) {
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	public void setTexture(TextureRegion texture) {
		this.texture = texture;
		setSize(texture.getRegionWidth(), texture.getRegionHeight());
	}

	
	public float getTopY() {
		return getY() + getHeight();
	}
	public float getRightX() {
		return getX()+getWidth();
	}
	public void setCenterX(float x) {
		setX(x-getWidth()/2);
	}
	public void setCenterY(float y) {
		setY(y-getHeight()/2);
	}
	public void setCenter(float x,float y) {
		setCenterX(x);
		setCenterY(y);
	}

	public void reset() {
	}


}
