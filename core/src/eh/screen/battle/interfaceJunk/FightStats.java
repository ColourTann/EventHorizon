package eh.screen.battle.interfaceJunk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.ship.Ship;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.assets.Font;
import eh.util.assets.Gallery;
import eh.util.assets.Pic;
import eh.util.maths.Collider;
import eh.util.maths.Pair;

public class FightStats extends Bonkject{
	private static float damageGap=23;
	private static Pair damageStart= new Pair(8,27);
	Ship ship;
	boolean player;
	public FightStats(Ship s) {
		this.ship=s;
		player=s.player;
	}

	@Override
	public void mouseDown() {
	}

	@Override
	public void mouseUp() {
	}

	@Override
	public void mouseClicked(boolean left) {
	}


	public void render(SpriteBatch batch) {
		float energyX=0;
		float damageX=0;
		float y=360;
		Color c=null;
		if(player){
			c=Colours.player2[1];
			energyX=265;
			damageX=345;
			Draw.drawTexture(batch, Gallery.playerEnergy.get(), energyX, y);
			Draw.drawTexture(batch, Gallery.majorDamagePlayer.get(), damageX, y);
		}
		else{
			c=Colours.enemy2[1];
			energyX=846;
			damageX=925;
			Draw.drawTexture(batch, Gallery.enemyEnergy.get(),energyX,y);
			Draw.drawTexture(batch, Gallery.majorDamageEnemy.get(),damageX,y);
		}
		BitmapFont current = (ship.getEnergy()>9?Font.medium:Font.big);
		current.setColor(c);
		String s=""+ship.getEnergy();
		current.draw(batch, s, energyX-current.getBounds(s).width/2+(ship.getEnergy()>9?45:47), y+20-current.getBounds(s).height/2);
		Draw.drawTextureScaled(batch, Gallery.iconEnergy.get(),energyX+5,y+6, 2,2);
		Font.medium.setColor(c);
		String inc="+"+ship.getIncome();
		Font.medium.draw(batch, inc, energyX-Font.medium.getBounds(inc).width/2+32, y+35);
		/*if(!player){
			String hs=""+ship.hand.size();
			Font.big.draw(batch, hs, energyX-Font.big.getBounds(hs).width/2+122, y+45);
		}*/
		Texture icon=(player?Gallery.greenHP[1].get():Gallery.redHP[1].get());
		for(int i=0;i<ship.getMajorDamage()&&i<5;i++){
			Draw.drawTexture(batch, icon, damageX+damageStart.x+damageGap*i, y+damageStart.y);
		}
	}

	@Override
	public void update(float delta) {
	}

}
