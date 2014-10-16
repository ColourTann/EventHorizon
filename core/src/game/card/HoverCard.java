package game.card;

import game.assets.Gallery;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;

import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HoverCard {
	
	static final int xOffset=5;
	static final int yOffset=8;
	static final int yGap=10;
	
	static final int wrapWidth=(int) (CardGraphic.width-xOffset*2);
	static Card card;
	static int bonusY=0;
	public static Pair enemyHoverPosition = new Pair();
	
	public enum ReminderType{WordTargeted, Targeted, Multishot, Augment, Absorb, Boost, Drain}
	
	public static ArrayList<ReminderType> getReminderTypes(Card c){
		ArrayList<ReminderType> result = new ArrayList<HoverCard.ReminderType>();
		CardCode code = c.getCode();
		if(code.contains(Special.Augment)) result.add(ReminderType.Augment);
		if(code.contains(Special.Targeted)) result.add(ReminderType.Targeted);
		if(code.contains(Augment.AugmentTargeted)) result.add(ReminderType.WordTargeted);
		if(c.getShots()>1) result.add(ReminderType.Multishot);
		if(code.contains(Special.Absorb)) result.add(ReminderType.Absorb);
		if(code.contains(Special.BoostSelf)) result.add(ReminderType.Boost);
		if(code.contains(Special.DrainSelf)) result.add(ReminderType.Drain);
		if(code.contains(Special.DrainTarget)) result.add(ReminderType.Drain);
		
		return result;
	}
	
	public static void drawHoverText(SpriteBatch batch, ReminderType type, float x, float y){
		String text;
		switch (type){
		case Absorb:
			text="Absorb Effect - Gain the effect if all shield points are used to block damage";
			Font.small.drawWrapped(batch, text, x, y, wrapWidth);
			bonusY+= Font.small.getWrappedBounds(text, wrapWidth).height;
			break;
		case Augment:
			text="Augment Type: Effect - Upgrades a card of the right type to gain the effect";
			Font.small.drawWrapped(batch, text, x, y, wrapWidth);
			bonusY+= Font.small.getWrappedBounds(text, wrapWidth).height;
			break;
		case Multishot:
			text=card.getShots()+"     - Fires "+card.getShots()+ " times";
			Font.small.drawWrapped(batch, text, x, y, wrapWidth);
			Draw.draw(batch, Gallery.iconShots.get(), x+8, y-3);
			bonusY+= Font.small.getWrappedBounds(text, wrapWidth).height;
			break;
		case Targeted:
			text="Choose a target instead"; 
			Draw.draw(batch, Gallery.iconTargeted.get(), x+1, y-2);
			Font.small.drawWrapped(batch, text, x+20, y, wrapWidth-20);
			float bonusBonusY= Font.small.getWrappedBounds(text, wrapWidth-20).height+2;
			text="of it being picked randomly";
			Font.small.drawWrapped(batch, text, x, y+bonusBonusY, wrapWidth);
			bonusY+= Font.small.getWrappedBounds("a\na\na\na\n", wrapWidth).height;
			break;
		case WordTargeted:
			text="Targeted - Choose a target instead of it being picked randomly"; 
			Font.small.drawWrapped(batch, text, x, y, wrapWidth);
			bonusY+= Font.small.getWrappedBounds(text, wrapWidth).height;
			break;
		case Boost:
			text="Boost X Effect - Affected module gains the beneficial effect for X turns"; 
			Font.small.drawWrapped(batch, text, x, y, wrapWidth);
			bonusY+= Font.small.getWrappedBounds(text, wrapWidth).height;
			break;
		case Drain:
			text="Drain X Effect - Affected module gains the negative effect for X turns"; 
			Font.small.drawWrapped(batch, text, x, y, wrapWidth);
			bonusY+= Font.small.getWrappedBounds(text, wrapWidth).height;
			break;
		}
		bonusY+=yGap;
	}
	


	public static void render(SpriteBatch batch, Card c, float baseY, float alpha) {
		bonusY=yOffset+125;
		card=c;
		if(HoverCard.getReminderTypes(card).size()==0){
			return;
		}
		

		Pair position = card.getGraphic().position;
		if(!card.getShip().player) {
			System.out.println(enemyHoverPosition);
			position=enemyHoverPosition.copy();
			baseY=position.y;
			bonusY=yOffset;
			
		}
	
		batch.setColor(1,1,1,alpha);
		Draw.draw(batch, Gallery.cardBaseHover.get(), position.x, baseY);
		Font.small.setColor(Colours.withAlpha(Colours.dark, alpha));
		for(ReminderType t:getReminderTypes(card)){
		
			drawHoverText(batch, t, position.x+xOffset, position.y+bonusY);
		}
		
	}
	
}
