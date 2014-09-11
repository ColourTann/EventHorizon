package game.screen.cardView;

import java.util.ArrayList;

import util.maths.Pair;
import util.update.Screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import game.card.Card;
import game.card.CardGraphic;
import game.module.Module;
import game.module.component.shield.Deflector;
import game.module.component.shield.Repeller;
import game.module.component.shield.Repulsor;
import game.module.component.weapon.Laser;

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
		modules.add(new Repeller(tier));
		modules.add(new Repulsor(tier));
		modules.add(new Deflector(tier));
		
		
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

	@Override
	public void scroll(int amount) {
	}

	@Override
	public void dispose() {
	}


}
