package game.module.component;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.image.Pic;
import util.maths.Pair;
import util.update.TextWisp;
import util.update.Timer;
import util.update.TextWisp.WispType;
import util.update.Timer.Interp;
import game.Main;
import game.assets.Gallery;
import game.assets.Sounds;
import game.card.Card;
import game.card.CardCode;
import game.card.CardCode.Special;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.weapon.Weapon;
import game.module.stuff.Buff;
import game.module.stuff.DamagePoint;
import game.module.stuff.ModuleStats;
import game.module.stuff.ShieldPoint;
import game.module.stuff.Buff.BuffType;
import game.screen.battle.Battle;
import game.screen.battle.Battle.State;
import game.ship.ShipGraphic;
import game.ship.niche.Niche;

public abstract class Component extends Module{

	public Niche niche;
	public int index=-1;
	
	int damageAtEnd;
	public boolean immune;
	public int maxHP;
	public int currentThreshold;
	private int[] baseThresholds;
	public int[] thresholds=new int[3];
	public boolean[] doubles= new boolean[18];
	
	float shieldIntensity=0;
	
	public ArrayList<DamagePoint> damage= new ArrayList<DamagePoint>();
	public ArrayList<DamagePoint> incomingDamage= new ArrayList<DamagePoint>();
	public ArrayList<DamagePoint> unshieldableIcoming= new ArrayList<DamagePoint>();
	public ArrayList<ShieldPoint> shieldPoints= new ArrayList<ShieldPoint>(); 

		private ModuleStats stats;
	
	

	
	public int targeteds;
	public Timer shieldAlphaTimer=new Timer();
	
	public ArrayList<Buff> buffs=new ArrayList<Buff>();

	private Pair center;
	private Pair barrel;
	
	
	public Component(int tier, String name, Pic modulePic, int variants, int numCards, int baseThresholds[]){
		super(tier, name, modulePic, variants, numCards);
		this.baseThresholds=baseThresholds;
	}

	public void recalculateThresholds(){
		for(int i=0;i<3;i++){
			thresholds[i]=(int)(baseThresholds[i]*ship.getArmourMultiplier());
		}
		maxHP=thresholds[2];

		for(int i=0;i<doubles.length;i++)doubles[i]=false;

		if(doubles.length>=maxHP){
			return;
		}
		int boxesLeft=18;
		for(int hpLeft=maxHP;hpLeft>=0;hpLeft--){


			boolean found=false;
			for(int thr:thresholds){
				if(hpLeft==thr||hpLeft-1==thr){

					boxesLeft--;
					found=true;
					break;
				}
			}
			if(found)continue;
			//if(doubles[boxesLeft])continue;
			if(hpLeft>boxesLeft){
				hpLeft--;
				boxesLeft--;
				doubles[boxesLeft]=true;
				continue;
			}

			boxesLeft--;
		}

	}
	
	@SuppressWarnings("unused")
	public void clicked(){
		//CLICKED ON PLAYER MODULE//
		if(destroyed)return;
		if(ship.player){

			if(Battle.getState()==State.ModuleChoose){
				CardCode code= Battle.moduleChooser.getCode();

				for(int i=0;i<Battle.moduleChooser.getEffect();i++)shield(new ShieldPoint(Battle.moduleChooser, i==0), false);
				if(code.contains(Special.GetCardFromChosenModule)){
					if(this==Battle.moduleChooser.mod){
						Sounds.error.play();
						return;
					}
					else ship.drawCard(getNextCard());
				}

				Battle.moduleChooser.moduleChosen();
				return;
			}


			if(ship.shieldPoints.size()>0&&getShieldableIncoming()>0){
				for(ShieldPoint p: ship.shieldPoints){
					if(shield(ship.shieldPoints.get(0),true)){
						ship.shieldPoints.remove(0);
						return;
					}
				}
				Sounds.error.play();

			}

		}

		//CLICKED ON ENEMY MODULE//

		if(!ship.player){
			if(Battle.getState()==State.Targeting){
				Card targeter=Battle.targetSource;
				Weapon source=(Weapon) targeter.mod;
				targeter.targetChosen();
				targeteds++;
				//Only the first shot is targeted//
				for(int i=0;i<targeter.getShots();i++){
					source.addAttack(targeter, this);
				}
			}
		}
	}
	
	public void moused(){
		moused=true;
	}

	public void unmoused(){
		moused=false;
	}
	
	public void damage(DamagePoint damagePoint) {
		if(immune)return;
		if(currentThreshold==3){
			System.out.println("overdamaging "+this+", already destroyed");
			return;
		}
		damage.add(damagePoint);
		if(getDamage()>=thresholds[currentThreshold]){
			majorDamage();

		}
		Battle.shake(ship.player,(float)(2.5f));
		ship.getGraphic().damage(niche.location);
		Sounds.damageMinor.play();
	}
	
	private void majorDamage() {
		currentThreshold++;
		ship.majorDamage();
		if(currentThreshold==3){
			destroy();
			return;
		}
		new TextWisp("Scrambled", Font.medium, getCenter().add(new Pair(0,-40)), WispType.Regular); 
		scramble();
		for(int i=0;i<5;i++)ship.getGraphic().damage(niche.location);
		ship.checkDefeat();
	}
	
	private void destroy(){
		destroyed=true;
		new TextWisp("Destroyed", Font.medium, getCenter().add(new Pair(0,-40)), WispType.Regular);
		for(int i=0;i<ship.hand.size();i++){
			Card c=ship.hand.get(i);
			if(c.mod==this){
				ship.discard(c);
				i--;
			}
		}
		for(int i=0;i<ship.deck.size();i++){
			Module m= ship.deck.get(i);
			if(m==this){
				ship.deck.remove(this);
				i--;
			}
		}
		ship.getGraphic().drawMap();
	}
	
	public void calculateDamage(int damage, boolean unshieldable) {
		if(unshieldable){
			for(int i=0;i<damage;i++){
				DamagePoint p = unshieldableIcoming.remove(0);
				damage(p);
			}
		}
		if(!unshieldable){
			for(int i=0;i<damage;i++){
				DamagePoint p = incomingDamage.remove(0);
				if(shieldPoints.size()>0){
					Sounds.shieldActivate.play();
					activateShield(shieldPoints.remove(0));
					continue;
				}
				damage(p);
			}
		}
	}
	
	private void activateShield(ShieldPoint shield) {
		if(immune)return;
		if(!shield.firstAdded)return;
		CardCode code=shield.card.getCode();
		ship.drawCard(code.getAmount(Special.AbsorbDraw));
		ship.addEnergy(code.getAmount(Special.AbsorbEnergy));
		shieldAlphaTimer=new Timer(1,0,1,Interp.SQUARE);
	}
	
	public boolean shield(ShieldPoint s, boolean overlapSound){
		//#1Reason why not to shield//
		System.out.println("shielding "+this);
		if(s.card!=null&&s.card.getCode().contains(Special.ShieldOnlyDamaged))if(currentThreshold==0)return false;
		shieldPoints.add(s);
		if(overlapSound)Sounds.shieldUse.overlay();
		else Sounds.shieldUse.play();
		return true;
	}

	public void clearShields(){
		shieldPoints.clear();
	}

	public void unshield(Card card) {
		for(int i=0;i<shieldPoints.size();i++){
			ShieldPoint sp=shieldPoints.get(i);
			if(sp.card==card){
				shieldPoints.remove(sp);
				i--;
			}
		}
	}
	
	public void removeIncoming(Card card) {
		for(int i=0;i<incomingDamage.size();i++){
			DamagePoint p=incomingDamage.get(i);
			if(p.card!=null&&p.card==card){
				incomingDamage.remove(p);
				i--;
			}
		}
	}
	
	public void cardIconMoused(Card card) {
		for(DamagePoint p:damage)p.checkMoused(card);
		for(DamagePoint p:unshieldableIcoming)p.checkMoused(card);
		for(DamagePoint p:incomingDamage)p.checkMoused(card);
	}
	public void cardIconUnmoused() {
		for(DamagePoint p:damage)p.reset();
		for(DamagePoint p:unshieldableIcoming)p.reset();
		for(DamagePoint p:incomingDamage)p.reset();
	}
	
	//Getters and setters//
	
	public int getIndex(){
		if(index==-1)System.out.println(this+" isn't installed on a ship");
		return index;
	}
	
	public int getDamage(){
		return damage.size();
	}
	public int getShieldableIncoming() {
		if(immune)return 0;
		return incomingDamage.size()-shieldPoints.size();
	}
	public int getSimpleIncoming(){
		return incomingDamage.size();
	}
	public int getTotalIncoming(){
		if(immune)return 0;
		return incomingDamage.size()+unshieldableIcoming.size();
	}
	public int getUnshieldableIncoming(){
		return unshieldableIcoming.size();
	}
	public int getShield(){
		return shieldPoints.size();
	}
	public void addIncoming(DamagePoint d){
		if(immune)return;
		incomingDamage.add(d);
	}

	public void addUnshieldable(DamagePoint d){
		if(immune)return;
		unshieldableIcoming.add(d);
	}
	public Pair getBarrel(){
		if(barrel==null){
			float offset=12+niche.width/2;
			float x=getCenter().x+(ship.player?offset:-offset);
			float y=getCenter().y;
			barrel=new Pair(x,y);
		}

		return barrel;
	}

	public Pair getCorner(){
		return getCenter().add(-niche.width/2, -niche.height/2);
	}

	public Pair getCenter(){
		if(center==null){

			if(ship.player){
				center=new Pair(
						niche.location.x+niche.width/2+ShipGraphic.offset.x,
						niche.location.y+niche.height/2+ShipGraphic.offset.y);
			}

			if(!ship.player){
				center=new Pair(
						500+Main.width-ShipGraphic.offset.x-niche.location.x-niche.width/2+((type==ModuleType.WEAPON||type==ModuleType.SHIELD)?-niche.width:0),
						ShipGraphic.offset.y+niche.location.y+niche.height/2);
			}
		}
		return center;
	}
	public Pair getHitLocation(){
		return getCenter().add(Pair.randomAnyVector().multiply(3));
	}
	public int getBuffAmount(BuffType check) {
		int total=0;
		for(Buff b:buffs){
			if(check==b.type)total+=1;
		}
		return total;
	}

	public void removeSramble(){
		for(Buff b:buffs){
			if(b.type==BuffType.Scrambled){
				buffs.remove(b);
				return;
			}
		}
	}

	public void scramble(){
		buffs.add(new Buff(BuffType.Scrambled, 1, null, true));
	}

	public void addBuff(Buff b){
		buffs.add(b);
	}

	public void removeBuffs(Card c){
		for(int i=0;i<buffs.size();i++){
			Buff b = buffs.get(i);
			if(b.card==c){
				buffs.remove(b);
				i--;
			}
		}
	}

	public int getDamageUntilMajor(){

		if(immune||destroyed)return 999;
		if(getDamage()+getTotalIncoming()>=thresholds[2]){
			System.out.println("going to be destroyed");
			return 999;
		}
		int keyThreshold=0;

		int damageTotal=getTotalIncoming()+getDamage();
		for(int i=2;i>=0;i--){
			if(damageTotal>=thresholds[i]){
				keyThreshold=i+1;
				break;
			}
		}

		//System.out.println(this+" requires "+(thresholds[keyThreshold]-damageTotal)+" to damage");
		return thresholds[keyThreshold]-damageTotal;

	}

	public int getShieldsRequiredToAvoidMajor(){
		if(immune)return 999;
		int keyThreshold=-1;

		int damageTotal=getTotalIncoming()+getDamage()-Math.min(shieldPoints.size(), getSimpleIncoming());
		for(int i=2;i>=0;i--){
			if(damageTotal>=thresholds[i]&&i>=currentThreshold){
				keyThreshold=i;
				break;
			}
		}
		if(keyThreshold==-1){
			return 999; //99 if not taking
		}
		if(getUnshieldableIncoming()>=damageTotal-thresholds[keyThreshold]+1){
			System.out.println("unshieldable so can't");
			return 999;

		}
		return damageTotal-thresholds[keyThreshold]+1;
	}

	public boolean isDead() {
		return currentThreshold==3;
	}
	
	public void endAdmin(){
		targeteds=0;
		currentCooldown=Math.max(0, currentCooldown-1);
		immune=false;
		for(int i=0;i<damageAtEnd;i++){
			damage(new DamagePoint(null));
		}
		damageAtEnd=0;

		//DOT stuff here//
		for(int i=0;i<buffs.size();i++){
			Buff b = buffs.get(i);
			if(!b.permanent){
				buffs.remove(b);
				i--;
			}
		}
	}

	public void drawShield(SpriteBatch batch) {


		float delta= Gdx.graphics.getDeltaTime();

		shieldIntensity+=(shieldPoints.size()-shieldIntensity)*delta*5;

		float alpha= (float) (Math.sin(Battle.ticks*3)+1)/4+.5f;
		alpha/=4;
		alpha*=Math.min(4, shieldIntensity);
		batch.setColor(Colours.withAlpha(Colours.shieldCols6[3], alpha));

		Draw.drawRotatedScaledCenteredFlipped(batch, Gallery.shieldEffect.get(), getBarrel().x, getBarrel().y, 2, 6, 0,  !ship.player, false);
		batch.setColor(1,1,1,shieldAlphaTimer.getFloat());
		Draw.drawRotatedScaledCenteredFlipped(batch, Gallery.shieldEffect.get(), getBarrel().x, getBarrel().y, 2, 6, 0,  !ship.player, false);
	}

	public ModuleStats getStats(){
		if(stats==null)stats=new ModuleStats(this);
		return stats;
	}
}
