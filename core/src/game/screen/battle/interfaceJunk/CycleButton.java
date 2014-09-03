package game.screen.battle.interfaceJunk;

import java.util.ArrayList;


import util.Colours;
import util.Draw;
import util.update.Timer;
import util.update.Timer.Interp;
import util.assets.Font;
import util.maths.BoxCollider;
import util.maths.Collider;
import util.maths.Pair;
import util.update.Mouser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.card.Card;
import game.card.CardGraphic;
import game.module.weapon.Weapon;
import game.screen.battle.Battle;
import game.screen.battle.Battle.State;
import game.screen.battle.tutorial.Tutorial;

public class CycleButton extends Mouser{
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
		
		if(Battle.isTutorial()&&Tutorial.stopCycle()){
			System.out.println("tut");
			return;
		}
		if(!Battle.isPlayerTurn())return;
		if(Battle.getState()==State.Nothing){
			timer=new Timer(0,1,3,Interp.SQUARE);

			if(Battle.getPlayer().getEnergy()<cost)return;

			Battle.getPlayer().addEnergy(-cost);
			
			Battle.setState(State.CycleDiscard);
			return;
		}

		if(Battle.getState()==State.CycleDiscard){
			timer=new Timer(timer.getFloat(),0,3,Interp.SQUARE);
			Battle.getPlayer().addEnergy(cost);
			
			Battle.setState(State.Nothing);
			return;
		}
	}

	

	public void setupChoices(){
		choices.clear();
		for(Weapon w:Battle.getPlayer().getWeapons()){
			if(w.destroyed)continue;
			choices.add(w.getNextCard());	
		}
		if(!Battle.getPlayer().getShield().destroyed){
			choices.add(Battle.getPlayer().getShield().getNextCard());
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
		Battle.getPlayer().hand.add(card);
		Battle.getPlayer().updateCardPositions();
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
		Draw.draw(batch, Gallery.cycleButton.get(), position.x, position.y);
		batch.setColor(new Color(1,1,1,1));
		Draw.draw(batch, Gallery.iconEnergy.get(), 168,402);
		Font.medium.setColor(Colours.dark);
		String s=cost+"";
		Font.medium.draw(batch, s, 191-Font.medium.getBounds(s).width/2, 403);
		
	}

	@Override
	public void update(float delta) {
	}





}
