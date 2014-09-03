package game.screen.battle.interfaceJunk;




import util.Colours;
import util.Draw;
import util.update.Timer;
import util.update.Timer.Interp;
import util.image.Pic;
import util.maths.Collider;
import util.maths.Pair;
import util.maths.PolygonCollider;
import util.update.Mouser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

import game.Main;
import game.assets.Gallery;
import game.screen.battle.Battle;
import game.screen.battle.Battle.Phase;
import game.screen.battle.tutorial.PicLoc;
import game.screen.battle.tutorial.Tutorial;

public class PhaseButton extends Mouser{
	public static Pair unClickedHeight;
	private static Pair clickedHeight;
	private float phaseChangeAlpha=0;
	private boolean fadingIn;
	public static PhaseButton button;
	static int width=Gallery.endTurnWeapon.get().getWidth();
	static int height=Gallery.endTurnWeapon.get().getHeight();
	private Pic before=Gallery.endTurnWeapon;
	private Pic after=Gallery.endTurnShield;
	Timer t;
	public PhaseButton(Collider col) {
		System.out.println(Main.width/2-width/2+":"+416);
		mousectivate(new PolygonCollider(new Polygon(new float[]{
				Main.width/2-width/2,416,
				Main.width/2+width/2,416,
				Main.width/2+width/2,356,
				Main.width/2-width/2,356,
		})));

		PolygonCollider pc=(PolygonCollider) collider;
		position=new Pair(pc.p.getBoundingRectangle().x,pc.p.getBoundingRectangle().y);
		unClickedHeight=position.add(0,-2);
		clickedHeight=position.add(0,-29);
		t=new Timer(unClickedHeight, unClickedHeight, 10, Interp.LINEAR);
	}

	@Override
	public void mouseDown() {
		if(Battle.getPhase()==Phase.ShieldPhase||Battle.getPhase()==Phase.WeaponPhase)t=new Timer(unClickedHeight, clickedHeight, 4, Interp.SQUARE);
	}

	@Override
	public void mouseUp() {
		t=new Timer(t.getPair(), unClickedHeight, 4, Interp.SQUARE);
	
	}

	@Override
	public void mouseClicked(boolean left) {
		Battle.turnButtonClicked();
	}
	
	public boolean isDown(){
		return Math.abs(t.getPair().y-unClickedHeight.y)<6;
		
	}
	
	public void nextPhase(){
		if(fadingIn)swap();
		fadingIn=true;
		demousectivate();
		mouseUp();
	}
	
	
	public static PhaseButton get(){
		if(button==null){
			button=new PhaseButton(null);
		}
		return button;
	}

	@Override
	public void update(float delta) {
		if(fadingIn){
			phaseChangeAlpha+=delta*2.5f;
			if(phaseChangeAlpha>1){
				phaseChangeAlpha=0;
				fadingIn=false;
				swap();
				mousectivate(null);
			}
		}
	}

	private void swap() {
		Pic temp=before;
		before=after;
		after=temp;
	}
	
	//horrible tutorial stuff//
	
	
	
	
	public void render(SpriteBatch batch){
		Color c = Colours.white;
		if(Battle.getPhase()!=Phase.ShieldPhase&&Battle.getPhase()!=Phase.WeaponPhase){
			c=Colours.faded;
		}
		
		batch.setColor(c);
		Draw.draw(batch, Gallery.endTurnBottom.get(),605,353);
		Draw.draw(batch, before.get(),t.getPair().x,t.getPair().y);
		
		batch.setColor(Colours.withAlpha(c,phaseChangeAlpha));
		Draw.draw(batch, after.get(),t.getPair().x,t.getPair().y);
		batch.setColor(1, 1, 1, 1);
		
		for(PicLoc pl:Tutorial.glows)pl.renderFuckingPhaseButtonStupidTutorial(batch);
	}
	

}
