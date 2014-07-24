package eh.card;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Gallery;
import eh.assets.Pic;
import eh.util.Bonkject;
import eh.util.Junk;
import eh.util.maths.BoxCollider;
import eh.util.maths.Sink;

public class CardIcon extends Bonkject{
	private static ArrayList<CardIcon> icons= new ArrayList<CardIcon>();
	private static int width=Gallery.blasterCard[0].get().getWidth();
	private static int height=Gallery.blasterCard[0].get().getHeight();
	private static Sink start=new Sink(89,646);
	private static int gap = 69;

	CardGraphic cg;
	Pic border;
	boolean overrideAlpha;
	public CardGraphic mousedGraphic;

	public CardIcon(Card c) {
		super(new BoxCollider(5, 5,	width,height));
		this.cg=c.getGraphic();
		x=cg.x+CardGraphic.positionPic.x;
		y=cg.y+CardGraphic.positionPic.y+CardGraphic.maxSelectedHeight+CardGraphic.height/2;
		border=c.getShip().player?Gallery.cardIconPlayer:Gallery.cardIconEnemy;
		mousectivate();
	}

	//Add icon to list and shuffle it all lovelily//
	public static void addIcon(Card c){
		if(c.wasScrambled)return;
		icons.add(new CardIcon(c));
		c.getGraphic().removeTopPic();
		while(icons.size()>14){
			icons.remove(0).fadeOut(Interp.LINEAR, 100);;
		}
		updateCardIconPositions();
	}

	private static void updateCardIconPositions(){
		for(int i=0;i<icons.size();i++)icons.get(i).lerptivate(new Sink(start.x+gap*(icons.size()-i), start.y), Interp.SQUARE, 1.5f);
	}

	@Override
	public void mouseDown() {
		mousedGraphic=new CardGraphic(cg.card,x+width/2-CardGraphic.width/2,y-270);
		overrideAlpha=true;
		cg.card.getShip().cardIconMoused(cg.card);
	}

	@Override
	public void mouseUp() {
		mousedGraphic.fadeOut(Interp.LINEAR, 3);
		overrideAlpha=false;
		cg.card.getShip().cardIconUnmoused();
	}

	@Override
	public void mouseClicked(boolean left) {
	}

	@Override
	public void update(float delta) {
		collider.x=x;
		collider.y=y;
	}

	@Override
	public void render(SpriteBatch batch) {
		if(!overrideAlpha)batch.setColor(1,1,1,.2f+.8f*(icons.indexOf(this)+14-icons.size())/14); 	//This alpha thing took me a 20 minutes...//
		Junk.drawTextureScaled(batch,cg.card.getImage().get(), x, y, 2, 2);
	
		batch.draw(border.get(),x,y);
		batch.setColor(1,1,1,1);
	}




}
