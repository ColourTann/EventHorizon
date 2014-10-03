package game.screen.battle.tutorial;

import util.Colours;
import util.Draw;
import util.image.Pic;
import util.maths.Pair;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.assets.Gallery;
import game.card.Card;
import game.screen.battle.Battle;
import game.screen.battle.interfaceJunk.PhaseButton;

public class PicLoc {


	Pic pic;
	Task task;

	Pair location;

	Card card;
	int side;

	boolean phaseButton;

	public PicLoc(Pic pic, Pair location, Task task){
		this.pic=pic;
		this.location=location;
		this.task=task;
	}

	public PicLoc(Card card, int side, Task task){
		this.task=task;
		this.card=card;
		this.side=side;
	}

	public PicLoc(Task phaseButtonTask){
		this.task=phaseButtonTask;
		this.phaseButton=true;
	}
	public void render(SpriteBatch batch){

		batch.setColor(Colours.withAlpha(Colours.tutorialHighlightColour, (float)Math.sin(Battle.ticks*4)/5+.3f));
		for(int x=-2;x<=2;x++){
			for(int y=-2;y<=2;y++){
				if(location!=null)Draw.draw(batch, pic.getOutline(), location.x+x, location.y+y);
				if(card!=null){
					Draw.draw(batch, Gallery.cardBase.getOutline(), card.getGraphic().position.x+x, card.getGraphic().getBaseHeight(side)+y);
				}

			}
		}
	}
	public void renderFuckingPhaseButtonStupidTutorial(SpriteBatch batch){
		batch.setColor(Colours.withAlpha(Colours.tutorialHighlightColour, (float)Math.sin(Battle.ticks*4)/5+.3f));
		for(int x=-2;x<=2;x++){
			for(int y=-2;y<=2;y++){
				if(phaseButton)if(PhaseButton.get().isDown())Draw.draw(batch, Gallery.endTurnWeapon.getOutline(), 605+x,356+y);	
			}
		}
	}
}
