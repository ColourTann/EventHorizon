package eh.screen.battle.interfaceJunk;



import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

import eh.Main;
import eh.assets.Gallery;
import eh.assets.Pic;
import eh.screen.battle.Battle;
import eh.screen.battle.Battle.Phase;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.maths.Collider;
import eh.util.maths.PolygonCollider;
import eh.util.maths.Sink;

public class PhaseButton extends Bonkject{
	private static Sink unClickedHeight;
	private static Sink clickedHeight;
	private float phaseChangeAlpha=0;
	private boolean fadingIn;
	public static PhaseButton button;
	static int width=Gallery.endTurnWeapon.get().getWidth();
	static int height=Gallery.endTurnWeapon.get().getHeight();
	private Pic before=Gallery.endTurnWeapon;
	private Pic after=Gallery.endTurnShield;
	public PhaseButton(Collider col) {
		super(new PolygonCollider(new Polygon(new float[]{
				Main.width/2-width/2,284,
				Main.width/2+width/2,284,
				Main.width/2+width/2,344,
				Main.width/2-width/2,344,
		})));
		mousectivate();
		System.out.println("mousectivating");
		PolygonCollider pc=(PolygonCollider) collider;
		x=pc.p.getBoundingRectangle().x;
		y=pc.p.getBoundingRectangle().y;
		unClickedHeight=new Sink(x,y);
		clickedHeight=new Sink(x,y+26);
	}

	@Override
	public void mouseDown() {
		System.out.println("moused??");
		if(Battle.getPhase()==Phase.ShieldPhase||Battle.getPhase()==Phase.WeaponPhase)lerptivate(clickedHeight, Interp.SQUARE, 4);
	}

	@Override
	public void mouseUp() {
		lerptivate(unClickedHeight, Interp.SQUARE, 4);
	}

	@Override
	public void mouseClicked(boolean left) {
		Battle.turnButtonClicked();
	}
	
	public void nextPhase(){
		if(fadingIn)swap();
		fadingIn=true;
		demousectivate();
		mouseUp();
	}
	
	public void render(SpriteBatch sb){
		Color c = Colours.white;
		if(Battle.getPhase()!=Phase.ShieldPhase&&Battle.getPhase()!=Phase.WeaponPhase){
			c=Colours.faded;
		}
		
		sb.setColor(c);
		sb.draw(Gallery.endTurnBottom.get(),605,277);
		sb.draw(before.get(),x,y);
		
		sb.setColor(Colours.withAlpha(c,phaseChangeAlpha));
		sb.draw(after.get(),x,y);
		sb.setColor(1, 1, 1, 1);
		//debugRender();
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
				mousectivate();
			}
		}
	}

	private void swap() {
		Pic temp=before;
		before=after;
		after=temp;
	}

}
