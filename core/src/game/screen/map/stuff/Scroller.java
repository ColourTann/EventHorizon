package game.screen.map.stuff;

import game.assets.Gallery;
import game.assets.TextBox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import util.Draw;
import util.TextWriter.Alignment;
import util.maths.Pair;
import util.update.Timer;
import util.update.Timer.Interp;


public class Scroller {
	public Pair position;
	ArrayList<Item> items;
	final float viewSpeed=.4f;
	public Timer view= new Timer();
	private Pair size;
	public Scroller(ArrayList<Item> items, Pair position, Pair size){
		this.items=items;
		updateLocations(true);
		this.position=position;
		this.size=size;
	}
	public void addItem(Item item){
		items.add(item);
		updateLocations(false);
	}
	public void removeItem(Item item){
		items.remove(item);
		updateLocations(false);
	}
	
	public void moveViewTo(int y){
		view=new Timer(view.getFloat(), y, viewSpeed, Interp.SQUARE);
	}
	
	public void updateLocations(boolean instant){
		for(int i=0;i<items.size();i++){
			Item it = items.get(i);
			float y=i*Item.height;
			if(instant)it.setY(y);
			else it.moveY(y);
		}
	}
	
	public void render(SpriteBatch batch){
		batch.flush();
		
		
		ScissorStack.pushScissors(new Rectangle(position.x, Gdx.graphics.getHeight()-position.y-size.y, size.x, size.y));
		TextBox.renderBox(batch, position, size.x, size.y, Alignment.Left);
		for(Item i:items){
			i.render(batch, position.x+TextBox.gap, position.y+view.getFloat()+TextBox.gap);
		}
		batch.flush();
		ScissorStack.popScissors();
	}
}
