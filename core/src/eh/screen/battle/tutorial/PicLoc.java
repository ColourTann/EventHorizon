package eh.screen.battle.tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Gallery;
import eh.assets.Pic;
import eh.card.Card;
import eh.screen.battle.Battle;
import eh.screen.battle.interfaceJunk.PhaseButton;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.Timer;
import eh.util.Timer.Interp;
import eh.util.maths.Pair;

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

		batch.setColor(Colours.withAlpha(Colours.light, (float)Math.sin(Battle.ticks*4)/5+.3f));
		for(int x=-2;x<=2;x++){
			for(int y=-2;y<=2;y++){
				if(location!=null)Draw.drawTexture(batch, pic.getOutline(), location.x+x, location.y+y);
				if(card!=null)Draw.drawTexture(batch, Gallery.cardBase.getOutline(), card.getGraphic().position.x+x, card.getGraphic().getBaseHeight(side)+y);
				if(phaseButton){
					
					if(PhaseButton.get().isDown()){
						System.out.println(PhaseButton.get().position.subtract(PhaseButton.unClickedHeight).getDistance());
		
						Draw.drawTexture(batch, Gallery.endTurnWeapon.getOutline(), 605+x,356+y);
					}
				}
			}
		}


	}
}
