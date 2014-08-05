package eh.card;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Gallery;
import eh.assets.Pic;
import eh.util.Bonkject;
import eh.util.Draw;
import eh.util.Timer.Interp;
import eh.util.maths.BoxCollider;
import eh.util.maths.Pair;

public class CardIcon extends Bonkject{
	public static ArrayList<CardIcon> icons= new ArrayList<CardIcon>();
	private static int width=Gallery.blasterCard[0].get().getWidth();
	private static int height=Gallery.blasterCard[0].get().getHeight();
	private static Pair start=new Pair(89,22);
	private static int gap = 69;
	CardGraphic cg;
	Pic border;
	boolean overrideAlpha;
	public CardGraphic mousedGraphic;

	public CardIcon(Card c) {
	
		this.cg=c.getGraphic();
		
		position=new Pair(cg.position.x+CardGraphic.positionPic.x,
				cg.position.y+CardGraphic.positionPic.y+CardGraphic.maxSelectedHeight+CardGraphic.height/2);
		border=c.getShip().player?Gallery.cardIconPlayer:Gallery.cardIconEnemy;
		
		mousedGraphic=new CardGraphic(cg.card, position.x+width/2-CardGraphic.width/2,position.y-270);
		mousedGraphic.alpha=0;
		mousectivate(new BoxCollider(5, 5,	width,height));
	}

	//Add icon to list and shuffle it all lovelily//
	public static void addIcon(Card c){
		if(c.wasScrambled)return;
		icons.add(new CardIcon(c));
		c.getGraphic().removeTopPic();
		while(icons.size()>14){
			icons.remove(0).fadeOut(100, Interp.LINEAR);;
		}
		updateCardIconPositions();
	}

	private static void updateCardIconPositions(){
		for(int i=0;i<icons.size();i++)icons.get(i).slide(new Pair(start.x+gap*(icons.size()-i), start.y), 1.5f, Interp.SQUARE);
	}

	@Override
	public void mouseDown() {
		System.out.println(mousedGraphic.position);
		overrideAlpha=true;
		mousedGraphic.setPosition(position.add(width/2-CardGraphic.width/2,start.y+16));
		
		mousedGraphic.stopFading();
		mousedGraphic.alpha=1;
		cg.card.getShip().cardIconMoused(cg.card);
	}

	@Override
	public void mouseUp() {
		mousedGraphic.bonktivate();
		mousedGraphic.fadeOut(3, Interp.LINEAR);
		overrideAlpha=false;
		cg.card.getShip().cardIconUnmoused();
	}

	@Override
	public void mouseClicked(boolean left) {
	}

	@Override
	public void update(float delta) {
		collider.position=position.copy();
	}

	public void render(SpriteBatch batch) {
		if(!overrideAlpha)batch.setColor(1,1,1,.2f+.8f*(icons.indexOf(this)+14-icons.size())/14); 	//This alpha thing took me a 20 minutes...//
		Draw.drawTextureScaled(batch,cg.card.getImage().get(), position.x, position.y, 2, 2);
	
		Draw.drawTexture(batch, border.get(),position.x,position.y);
		batch.setColor(1,1,1,1);
	}




}
