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
import eh.util.maths.BoxCollider;
import eh.util.maths.Collider;
import eh.util.maths.Sink;

public class CycleButton extends Bonkject{
	public static CycleButton button;
	boolean active;
	public static ArrayList<Card> choices= new ArrayList<Card>();
	private static float gap=20+CardGraphic.width;
	private static Sink start=new Sink(Main.width/2-CardGraphic.width/2-gap,278);
	int cost=0;
	public CycleButton(Collider col) {
		super(new BoxCollider(160, 279, 45, 66));
		mousectivate();

		BoxCollider b=(BoxCollider) collider;
		x=b.x;
		y=b.y;
		lerpedRatio=1;
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

			if(Battle.player.getEnergy()<cost)return;

			Battle.player.addEnergy(-cost);
			swap();
			Battle.setState(State.CycleDiscard);
			return;
		}

		if(Battle.getState()==State.CycleDiscard){
			Battle.player.addEnergy(cost);
			swap();
			Battle.setState(State.Nothing);
			return;
		}
	}

	public void swap(){
		lerptivate(Interp.SQUARE, 0, 3f);
		active=!active;
	}

	public void setupChoices(){
		for(Weapon w:Battle.player.getWeapons()){
			if(w.destroyed)continue;
			choices.add(w.getNextCard());	
		}
		if(!Battle.player.getShield().destroyed){
			choices.add(Battle.player.getShield().getNextCard());
		}

		for(int i=0;i<choices.size();i++){
			CardGraphic cg=choices.get(i).getGraphic();
			cg.override=true;
			cg.mousectivate();
			cg.setPosition(new Sink(start.x+gap*i,start.y));
		}
	}

	public void choose(Card card) {
		Battle.player.hand.add(card);
		Battle.player.updateCardPositions();
		for(Card c:choices){
			if(c!=card){
				c.getGraphic().fadeOut(CardGraphic.fadeType, CardGraphic.fadeSpeed);
			}
		}
		card.getGraphic().override=false;
		Battle.setState(State.Nothing);
		choices.clear();
		cost++;
		swap();
	}

	@Override
	public void render(SpriteBatch batch) {
		if(active)batch.setColor(new Color(1,1,1,lerpedRatio));
		else batch.setColor(new Color(1,1,1,1-lerpedRatio));
		batch.draw(Gallery.cycleButton.get(), x, y);
		batch.setColor(new Color(1,1,1,1));
		batch.draw(Gallery.iconEnergy.get(), 168,284);
		Font.medium.setColor(Colours.dark);
		String s=cost+"";
		Font.medium.draw(batch, s, 191-Font.medium.getBounds(s).width/2, 302);
	}

	@Override
	public void update(float delta) {
	}





}
