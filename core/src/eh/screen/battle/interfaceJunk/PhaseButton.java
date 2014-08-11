package eh.screen.battle.interfaceJunk;



import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

import eh.Main;
import eh.assets.Gallery;
import eh.assets.Pic;
import eh.screen.battle.Battle;
import eh.screen.battle.Battle.Phase;
import eh.screen.battle.tutorial.PicLoc;
import eh.screen.battle.tutorial.Tutorial;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.Timer;
import eh.util.Timer.Interp;
import eh.util.maths.Collider;
import eh.util.maths.PolygonCollider;
import eh.util.maths.Pair;

public class PhaseButton extends Bonkject{
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
	private boolean glow;
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
	public void setGlow(boolean on){
		glow=on;
	}
	
	
	
	public void render(SpriteBatch batch){
		Color c = Colours.white;
		if(Battle.getPhase()!=Phase.ShieldPhase&&Battle.getPhase()!=Phase.WeaponPhase){
			c=Colours.faded;
		}
		
		batch.setColor(c);
		Draw.drawTexture(batch, Gallery.endTurnBottom.get(),605,353);
		Draw.drawTexture(batch, before.get(),t.getPair().x,t.getPair().y);
		
		batch.setColor(Colours.withAlpha(c,phaseChangeAlpha));
		Draw.drawTexture(batch, after.get(),t.getPair().x,t.getPair().y);
		batch.setColor(1, 1, 1, 1);
		
		for(PicLoc pl:Tutorial.glows)pl.renderFuckingPhaseButtonStupidTutorial(batch);
	}
	

}
