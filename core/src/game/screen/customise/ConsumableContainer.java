package game.screen.customise;

import game.Main;
import game.assets.Gallery;
import game.assets.Sounds;
import game.card.Card;
import game.card.CardGraphic;
import game.module.component.shield.Deflector;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.BoxCollider;
import util.maths.Pair;
import util.update.Mouser;
import util.update.Timer.Interp;

public class ConsumableContainer extends Mouser{
	ArrayList<Card> cards= new ArrayList<Card>();;
	static int width=Gallery.cardBase.getWidth()*3;
	static int height=Gallery.cardBase.getHeight()*2;
	Pair position=new Pair(Customise.energyX-width/2, 400);
	public ConsumableContainer(){
		mousectivate(new BoxCollider(position.x, position.y, width, height));
		cards.addAll(Customise.ship.getConsumables());
		for(Card c:Customise.ship.getConsumables())c.getGraphic().activate();
		updateCardPositions();
	}
	
	@Override
	public void mouseDown() {
	}

	@Override
	public void mouseUp() {
	}

	@Override
	public void mouseClicked(boolean left) {
	
		
		if(Customise.consumableSelected()){
			for(int i=0;i<Customise.selectedReward.cards.length;i++){
				Card c=Customise.selectedReward.cards[i];
				Customise.ship.addConsumableCard(c);
				cards.add(c);
				CardGraphic cg=c.getGraphic();
				//cg.override=true;
				cg.alpha=0;
				cg.fadeIn(1, Interp.LINEAR);
				cg.setPosition(position.copy());
				updateCardPositions();
				Sounds.shieldUse.play();
			}
			
			Customise.rewardChosen();
			
		}
	}

	private void updateCardPositions() {
		if(cards.size()==0)return;
		float start=position.x;
		
		float gap=width/(cards.size());
		if(cards.size()<=3){
			for(int i=0;i<cards.size();i++){
				CardGraphic c=cards.get(i).getGraphic();
				c.activate();
				c.slide(new Pair(start+gap*(i+1)-CardGraphic.width, position.y), .5f, Interp.SQUARE);
				c.finishFlipping();
			}
		}
		else{
			float funnyWidth=width-CardGraphic.width;
			gap=funnyWidth/(cards.size()-1);
			for(int i=0;i<cards.size();i++){
				CardGraphic c=cards.get(i).getGraphic();
				c.slide(new Pair(start+gap*i, position.y), .5f, Interp.SQUARE);
				c.finishFlipping();
			}
		}
		
	}

	@Override
	public void update(float delta) {
	}
	
	public void render(SpriteBatch batch){
		Font.drawFontCentered(batch, "Consumable Cards", Font.big, position.x+width/2, position.y-40);
		batch.setColor(Colours.backgrounds1[0]);
		Draw.drawScaled(batch, Gallery.bonusPool.get(), position.x-3, position.y-2, 3, 2);
		batch.setColor(Colours.make(30, 28, 49));
		if(Customise.consumableSelected()){
			batch.setColor(Reward.selectedColor);
		}
		Draw.drawScaled(batch, Gallery.bonusPool.getOutline(), position.x-3, position.y-2, 3, 2);
		for(Card c:cards){
			c.getGraphic().render(batch);
		}
		
	}

}
