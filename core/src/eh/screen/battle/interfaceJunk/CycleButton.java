package eh.screen.battle.interfaceJunk;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.Main;
import eh.assets.Font;
import eh.assets.Gallery;
import eh.card.Card;
import eh.card.CardGraphic;
import eh.screen.battle.Battle;
import eh.screen.battle.Battle.State;
import eh.screen.battle.tutorial.Tutorial;
import eh.ship.module.weapon.Weapon;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.Timer;
import eh.util.Timer.Interp;
import eh.util.maths.BoxCollider;
import eh.util.maths.Collider;
import eh.util.maths.Pair;

public class CycleButton extends Bonkject{
	public static CycleButton button;
	boolean active;
	public static ArrayList<Card> choices= new ArrayList<Card>();
	private static float gap=20+CardGraphic.width;
	private static Pair start=new Pair(Main.width/2-CardGraphic.width/2-gap,193);
	int cost=0;
	Timer timer=new Timer(0, 0, 1, Interp.LINEAR);
	public CycleButton(Collider col) {
		alpha=0;
		mousectivate(new BoxCollider(160, 355, 45, 66));

		BoxCollider b=(BoxCollider) collider;
		position=b.position.copy();
		
	}

	public static CycleButton get(){
		if(button==null)button=new CycleButton(null);
		return button;
	}

	@Override
	public void mouseDown() {
	}

	@Override
	public void mouseUp() {
	}

	@Override
	public void mouseClicked(boolean left) {
		
		if(Battle.tutorial&&Tutorial.stopCycle()){
			System.out.println("tut");
			return;
		}
		if(!Battle.isPlayerTurn())return;
		if(Battle.getState()==State.Nothing){
			timer=new Timer(0,1,3,Interp.SQUARE);

			if(Battle.player.getEnergy()<cost)return;

			Battle.player.addEnergy(-cost);
			
			Battle.setState(State.CycleDiscard);
			return;
		}

		if(Battle.getState()==State.CycleDiscard){
			timer=new Timer(timer.getFloat(),0,3,Interp.SQUARE);
			Battle.player.addEnergy(cost);
			
			Battle.setState(State.Nothing);
			return;
		}
	}

	

	public void setupChoices(){
		choices.clear();
		for(Weapon w:Battle.player.getWeapons()){
			if(w.destroyed)continue;
			choices.add(w.getNextCard());	
		}
		if(!Battle.player.getShield().destroyed){
			choices.add(Battle.player.getShield().getNextCard());
		}

		for(int i=0;i<choices.size();i++){
			CardGraphic cg=choices.get(i).getGraphic();
			cg.finishFlipping();
			cg.override=true;
			cg.mousectivate(new BoxCollider(0,0,CardGraphic.width,CardGraphic.height));
			cg.setPosition(new Pair(start.x+gap*i,start.y));
			cg.alpha=0;
			cg.fadeIn(3, Interp.SQUARE);
		}
	}

	public void choose(Card card) {
		Battle.player.hand.add(card);
		Battle.player.updateCardPositions();
		for(Card c:choices){
			if(c!=card){
				c.getGraphic().fadeOut(CardGraphic.fadeSpeed, CardGraphic.fadeType);
			}
		}
		card.getGraphic().override=false;
		Battle.setState(State.Nothing);
		cost++;
		timer=new Timer(timer.getFloat(),0,3,Interp.SQUARE);
	}

	public void render(SpriteBatch batch) {
	
		batch.setColor(new Color(1,1,1,timer.getFloat()));
		Draw.drawTexture(batch, Gallery.cycleButton.get(), position.x, position.y);
		batch.setColor(new Color(1,1,1,1));
		Draw.drawTexture(batch, Gallery.iconEnergy.get(), 168,402);
		Font.medium.setColor(Colours.dark);
		String s=cost+"";
		Font.medium.draw(batch, s, 191-Font.medium.getBounds(s).width/2, 403);
	}

	@Override
	public void update(float delta) {
	}





}
