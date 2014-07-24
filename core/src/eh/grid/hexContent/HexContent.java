package eh.grid.hexContent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.grid.hex.Hex;

public abstract class HexContent {
	Hex hex;
	public HexContent(Hex hex){
		this.hex=hex;
	}
	public abstract void render(SpriteBatch batch);
}
