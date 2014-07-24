package eh.screen.cardView;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eh.card.Card;
import eh.card.CardGraphic;
import eh.screen.Screen;
import eh.ship.module.computer.Alpha;
import eh.ship.module.computer.Beta;
import eh.ship.module.computer.Gamma;
import eh.ship.module.generator.Five;
import eh.ship.module.generator.Four;
import eh.ship.module.generator.Three;
import eh.ship.module.shield.Deflector;
import eh.ship.module.shield.Repeller;
import eh.ship.module.weapon.Laser;
import eh.ship.module.weapon.Pulse;
import eh.ship.module.weapon.Ray;
import eh.ship.module.weapon.Tesla;
import eh.util.maths.Sink;

public class CardViewer extends Screen{
	public ArrayList<Card> cards= new ArrayList<Card>();
	public CardViewer(){
		cards.addAll(new Deflector().getCardsJustForShowing());
		cards.addAll(new Repeller().getCardsJustForShowing());
		cards.addAll(new Alpha().getCardsJustForShowing());
		cards.addAll(new Beta().getCardsJustForShowing());
		cards.addAll(new Gamma().getCardsJustForShowing());
		cards.addAll(new Three().getCardsJustForShowing());
		cards.addAll(new Four().getCardsJustForShowing());
		cards.addAll(new Five().getCardsJustForShowing());
		for(int i=0;i<cards.size();i++){
			cards.get(i).getGraphic().setPosition(new Sink(i%8*CardGraphic.width, i/8*CardGraphic.height));
			
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
	}

	@Override
	public void keyUp(int keycode) {
	}

	@Override
	public void mousePressed(Sink location, boolean left) {
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {
	}


}
