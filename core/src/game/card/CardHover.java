package game.card;

import game.assets.Gallery;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;

import java.util.ArrayList;
import java.util.HashMap;

import util.Colours;
import util.Draw;
import util.TextWriter;
import util.assets.Font;
import util.image.PairPic;
import util.maths.Pair;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CardHover {

	static final int xOffset=5;
	static final int yOffset=8;
	static final int yGap=10;

	static final int wrapWidth=(int) (CardGraphic.width-xOffset*2);
	static Card card;
	public static Pair enemyHoverPosition = new Pair();
	TextWriter tw;
	public static HashMap<ReminderType, TextWriter> buffWriters = new HashMap<CardHover.ReminderType, TextWriter>();
	public enum ReminderType{Targeted, Multishot, Augment, Absorb, Boost, Drain}

	public static ArrayList<ReminderType> getReminderTypes(Card c){
		ArrayList<ReminderType> result = new ArrayList<CardHover.ReminderType>();
		CardCode code = c.getCode();
		if(code.contains(Special.Augment)) result.add(ReminderType.Augment);
		if(code.contains(Special.Targeted)) result.add(ReminderType.Targeted);
		if(code.contains(Augment.AugmentTargeted)) result.add(ReminderType.Targeted);
		if(c.getShots()>1) result.add(ReminderType.Multishot);
		if(code.contains(Special.Absorb)) result.add(ReminderType.Absorb);
		if(code.contains(Special.BoostSelf)) result.add(ReminderType.Boost);
		if(code.contains(Special.DrainSelf)) result.add(ReminderType.Drain);
		if(code.contains(Special.DrainTarget)) result.add(ReminderType.Drain);

		return result;
	}





	public static void render(SpriteBatch batch, Card c, float baseY, float alpha) {
		int bonusY=yOffset+125;
		card=c;
		if(CardHover.getReminderTypes(card).size()==0){
			return;
		}


		Pair position = card.getGraphic().position;
		if(!card.getShip().player) {
			position=enemyHoverPosition.copy();
			baseY=position.y;
			bonusY=yOffset;
		}

		batch.setColor(1,1,1,alpha);
		Draw.draw(batch, Gallery.cardBaseHover.get(), position.x, baseY);
		Font.small.setColor(Colours.withAlpha(Colours.dark, alpha));
		for(ReminderType t:getReminderTypes(card)){
			TextWriter tw=buffWriters.get(t);
			tw.drawText(batch, position.x+xOffset, position.y+bonusY);
			bonusY+=tw.maxHeight;
			bonusY+=yGap;
		}

	}

	public static void init() {
		for(ReminderType type: ReminderType.values()){
			TextWriter tw=null;
			switch(type){
			case Absorb:
				tw=new TextWriter(Font.small, "Absorb Effect - Gain the effect if all shield points are used to block damage");
				buffWriters.put(type, tw);
				break;
			case Augment:
				tw=new TextWriter(Font.small, "Augment Type: Effect - Upgrades a card of the right type to gain the effect");
				buffWriters.put(type, tw);
				break;
			case Multishot:
				tw=new TextWriter(Font.small, "x shot - Fires x times");
				tw.replace("shot", Gallery.iconShots.get());
				buffWriters.put(type, tw);
				break;
			case Targeted:
				tw=new TextWriter(Font.small, "Choose a target instead of it being chosen randomly");
				tw.addPairPic(new PairPic(Gallery.iconTargeted, new Pair(0,-2)));
				tw.addObstacleTopLeft(20, 12);
				buffWriters.put(type, tw);
				break;
			case Boost:
				tw=new TextWriter(Font.small, "Boost X Effect - Affected module gains the beneficial effect for X turns");
				buffWriters.put(type, tw);
				break;
			case Drain:
				tw=new TextWriter(Font.small, "Drain X Effect - Affected module gains the negative effect for X turns");
				buffWriters.put(type, tw);
				break;
			}
			tw.setWrapWidth(wrapWidth);
		}
	}

}
