package game.grid.hexContent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.grid.hex.Hex;

public abstract class HexContent {
	Hex hex;
	public HexContent(Hex hex){
		this.hex=hex;
	}
	public abstract void render(SpriteBatch batch);
	public abstract String toString();
}
