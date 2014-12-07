package game.screen.map.stuff;

import game.assets.Gallery;
import game.assets.TextBox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.sun.org.apache.bcel.internal.classfile.Code;

import util.Draw;
import util.TextWriter.Alignment;
import util.image.Pic;
import util.maths.Pair;
import util.update.SimpleButton;
import util.update.Timer;
import util.update.Timer.Interp;
import util.update.Updater.Layer;


public class Scroller {
	
	public Pair position;
	ArrayList<Item> items= new ArrayList<Item>();
	final static float viewSpeed=.4f;
	public Timer view= new Timer();
	private Pair size;
	SimpleButton top;
	SimpleButton bot;
	int index=0;
	int numItems;
	public static int buttonHeight=Gallery.upButton.getHeight()*4;
	public static float width= Item.size.x+TextBox.gap*2;
	public Scroller(ArrayList<Item> aItems, Pair position, int numItems){
		this.numItems=numItems;
		updateLocations(true);
		this.position=position;
		
		size=new Pair(Gallery.rewardOutline.getWidth()*4,(Gallery.rewardHighlights.getHeight()-1)*4*numItems+4);
		top = new ScrollerButton(true);
		bot = new ScrollerButton(false);
		for(Item i:aItems){
			addItem(i);
			
		}
		updateMousers();
		System.out.println(items.size());
	}
	
	public void updateMousers(){
		for(int i=0;i<items.size();i++){
			Item it = items.get(i);
			
			if(i<index)it.demousectivate();
			else if(i>=index+numItems)it.demousectivate();
			else {
				it.mousectivate(null);
				
			}
			
		}
	}
	
	public void addItem(Item item){
		item.layer=Layer.ALL;
		item.activate();
		item.mousectivate(null);
		items.add(item);
		item.setY((items.indexOf(item))*Item.height);
	}
	
	public void removeItem(Item item){
		item.demousectivate();
		items.remove(item);
	}
	
	public void moveViewTo(int index){
		System.out.println(index);
		if(index<0)return;
		if(index>items.size()-numItems)return;
	//	System.out.println(items.size()-numItems);
		if(index>items.size()-numItems)return;
		this.index=index;
		view=new Timer(view.getFloat(), -index*Item.height, viewSpeed, Interp.SQUARE);
	}
	
	public void updateLocations(boolean instant){
		for(int i=0;i<items.size();i++){
			Item it = items.get(i);
			float y=i*Item.height;
			if(instant){
				it.setY(y);
			}
			else {
				it.moveY(y);
			}
		}
		
		if(index>items.size()-numItems){
			moveViewTo(index-1);
		}
		updateMousers();
	}
	
	public void render(SpriteBatch batch){
		
		TextBox.renderBox(batch, position.add(-TextBox.gap, -TextBox.gap+buttonHeight), size.x+TextBox.gap*2, size.y+TextBox.gap*2, Alignment.Left, false);
		top.render(batch);
		bot.render(batch);
		batch.flush();
		ScissorStack.pushScissors(new Rectangle(position.x, Gdx.graphics.getHeight()-position.y-size.y-buttonHeight, size.x, size.y));
		for(Item i:items){
			i.updateCollider(position, view.getFloat());
			i.render(batch, position.x, position.y+view.getFloat()+buttonHeight);
		}
		for(Item i:items){
		
			i.postRender(batch, position.x, position.y+view.getFloat()+buttonHeight);
		}
		batch.flush();
		ScissorStack.popScissors();
		
	}
	
	public class ScrollerButton extends SimpleButton{
		
		public ScrollerButton(final boolean top) {
			super(new Pair(50,50), "", top?Gallery.upButton:Gallery.upButton.getFlipped(false), new Code() {
				@Override
				public void onPress() {
					Scroller.this.moveViewTo(index+(top?-1:+1));
					Scroller.this.updateMousers();
				}
			});
			setScale(4);
			float y=Scroller.this.position.y-TextBox.gap;
			if(!top)y=Scroller.this.position.y+Scroller.this.size.y+TextBox.gap+height;
			setPosition(new Pair(Scroller.this.position.x+Scroller.this.size.x/2-width/2,y));
			
		}
		
		
	}

	public void dispose() {
		//items, top, bot
		for(Item i:items){
			i.demousectivate();
			i.deactivate();
		}
		top.demousectivate();
		top.deactivate();
		bot.demousectivate();
		bot.deactivate();
	}
}
