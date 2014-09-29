package game.screen.battle.interfaceJunk;


import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import util.update.Mouser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.assets.Gallery;
import game.module.component.Component;
import game.module.utility.armour.CrystalLattice;
import game.ship.Ship;

public class FightStats extends Mouser{
	private static float damageGap=23;
	private static Pair damageStart= new Pair(8,27);
	Ship ship;
	boolean player;
	public static Pair playerEnergy=new Pair(265, 360);
	public static Pair enemyEnergy=new Pair(846, 360);
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
			Draw.draw(batch, Gallery.playerEnergy.get(), energyX, y);
			if(ship.getArmour() instanceof CrystalLattice) Draw.draw(batch, Gallery.majorDamagePlayerPlus.get(), damageX, y);
			else Draw.draw(batch, Gallery.majorDamagePlayer.get(), damageX, y);
		}
		else{
			c=Colours.enemy2[1];
			energyX=846;
			damageX=925;
			Draw.draw(batch, Gallery.enemyEnergy.get(),energyX,y);
			if(ship.getArmour() instanceof CrystalLattice) Draw.draw(batch, Gallery.majorDamageEnemyPlus.get(), damageX, y);
			else Draw.draw(batch, Gallery.majorDamageEnemy.get(),damageX,y);
		}
		BitmapFont current = (ship.getEnergy()>9?Font.medium:Font.big);
		current.setColor(c);
		String s=""+ship.getEnergy();
		current.draw(batch, s, energyX-current.getBounds(s).width/2+(ship.getEnergy()>9?45:47), y+19-current.getBounds(s).height/2);
		Draw.drawScaled(batch, Gallery.iconEnergy.get(),energyX+5,y+6, 2,2);
		Font.medium.setColor(c);
		String inc="+"+ship.getIncome();
		Font.medium.draw(batch, inc, energyX-Font.medium.getBounds(inc).width/2+32, y+35);

		/*Texture icon=(player?Gallery.greenHP[1].get():Gallery.redHP[1].get());
		for(int i=0;i<ship.getMajorDamage()&&
				(i<5  || (i<6&&ship.getArmour() instanceof CrystalLattice)  );i++){
			Draw.draw(batch, icon, damageX+damageStart.x+damageGap*i, y+damageStart.y);
		}*/
		
		//Horrible algorithm for determining about to takedness, woof!//
		int reds=0;
		int greys=0;
		int oranges=0;
		int blues=0;
		reds=ship.getMajorDamage();
		for(Component cmp: ship.components){
			for(int thr:cmp.thresholds){
				if(cmp.getDamage()>=thr) continue; //already damaged : ( //
				if(cmp.getDamage()+cmp.getUnshieldableIncoming()>=thr){
					greys++; //Unshieldable incoming//
					continue;
				}
				if(cmp.getDamage()+cmp.getTotalIncoming()>=thr){
					if(cmp.getDamage()+cmp.getTotalIncoming()-Math.min(cmp.incomingDamage.size(), cmp.getShield())<thr){
						blues++; //Shielded, good job!//
						continue;
					}
					oranges++; //Not shielded, get to work!//
					continue;
				}	
			}
		}
		
		int totalDrawn=0;
		int max=ship.getArmour() instanceof CrystalLattice?6:5;
		
		for(int i=0;i<reds&&totalDrawn<max;i++){
			Draw.draw(batch, ship.player?Gallery.greenHP[1].get():Gallery.redHP[1].get(), damageX+damageStart.x+damageGap*totalDrawn, y+damageStart.y);
			totalDrawn++;
		}
		for(int i=0;i<greys&&totalDrawn<max;i++){
			Draw.draw(batch, Gallery.greyHP[1].get(), damageX+damageStart.x+damageGap*totalDrawn, y+damageStart.y);
			totalDrawn++;
		}
		for(int i=0;i<oranges&&totalDrawn<max;i++){
			Draw.draw(batch, Gallery.orangeHP[1].get(), damageX+damageStart.x+damageGap*totalDrawn, y+damageStart.y);
			totalDrawn++;
		}
		for(int i=0;i<blues&&totalDrawn<max;i++){
			Draw.draw(batch, Gallery.blueHP[1].get(), damageX+damageStart.x+damageGap*totalDrawn, y+damageStart.y);
			totalDrawn++;
		}
	}

	@Override
	public void update(float delta) {
	}

}
