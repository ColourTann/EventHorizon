package eh.screen.cardView;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eh.card.Card;
import eh.card.CardGraphic;
import eh.module.Module;
import eh.module.computer.Alpha;
import eh.module.computer.Beta;
import eh.module.computer.Gamma;
import eh.module.generator.Five;
import eh.module.generator.Four;
import eh.module.generator.Three;
import eh.module.shield.Deflector;
import eh.module.shield.Repeller;
import eh.module.weapon.Laser;
import eh.module.weapon.Pulse;
import eh.module.weapon.Ray;
import eh.module.weapon.Tesla;
import eh.screen.Screen;
import eh.util.maths.Pair;

public class CardViewer extends Screen{
	public ArrayList<Module> modules = new ArrayList<Module>();
	public ArrayList<Card> cards= new ArrayList<Card>();
	int tier=0;
	public CardViewer(){
		
		
		init();
	}
	
	public void init(){
		modules.clear();
		
		modules.add(new Laser(tier));
		modules.add(new Pulse(tier));
		modules.add(new Tesla(tier));
		modules.add(new Ray(tier));
		
		
		cards.clear();
		for(Module m:modules){
			System.out.println(m.tier);
			cards.addAll(m.getCardsJustForShowing());
		}
		for(int i=0;i<cards.size();i++){
			cards.get(i).getGraphic().setPosition(new Pair(i%8*CardGraphic.width, i/8*CardGraphic.height));
			cards.get(i).getGraphic().demousectivate();
		}
		
	}
	
	@Override
	public void update(float delta) {
	}

	@Override
	public void render(SpriteBatch batch) {
		for(Card c:cards){
			c.getGraphic().render(batch);
		}
	}

	@Override
	public void keyPress(int keycode) {
		switch(keycode){
		case Input.Keys.RIGHT:
			tier++;
			init();
			break;
		case Input.Keys.LEFT:
			tier--;
			init();
			break;
			
			
		}
	}

	@Override
	public void keyUp(int keycode) {
	}

	@Override
	public void mousePressed(Pair location, boolean left) {
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {
	}

	@Override
	public void postRender(SpriteBatch batch) {
	}


}
