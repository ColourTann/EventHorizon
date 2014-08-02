package eh.card;

import java.util.ArrayList;

import javax.smartcardio.CardPermission;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Clip;
import eh.assets.Pic;
import eh.card.CardCode.AI;
import eh.card.CardCode.AIclass;
import eh.card.CardCode.Augment;
import eh.card.CardCode.Special;
import eh.screen.battle.Battle;
import eh.screen.battle.Battle.Phase;
import eh.screen.battle.Battle.State;
import eh.screen.battle.interfaceJunk.CycleButton;
import eh.screen.battle.tutorial.Tutorial;
import eh.ship.Ship;
import eh.ship.module.Module;
import eh.ship.module.Module.ModuleType;
import eh.ship.module.utils.Buff;
import eh.ship.module.utils.DamagePoint;
import eh.ship.module.utils.ShieldPoint;
import eh.ship.module.utils.Buff.BuffType;
import eh.ship.module.weapon.Weapon;
import eh.util.Draw;
import eh.util.Timer.Interp;

public class Card {
	public Module mod;

	//Card Stats//
	private String[] names = new String[2];
	private Pic[] cardPics = new Pic[2];
	private int[] baseCost = new int[2];
	private int[] baseEffect = new int[2];
	public int bonusEffect=0;
	private int[] baseCooldown = new int[2];
	private String[] rules = new String[2];
	private int[] shots = new int[2];
	private CardCode[] code=new CardCode[2];
	int specialSide;
	public ModuleType type;

	//Random Fields//
	public int side=0;
	public boolean selected;
	private CardGraphic cg;
	private int previousIndexInHand;
	public static ArrayList<CardGraphic> enemyCardsToRender=new ArrayList<CardGraphic>();
	
	public boolean wasScrambled;
	
	//Setting up card//
	public Card(Module m, int side){
		mod=m;
		specialSide=side;
		for(int i=0;i<2;i++){
			names[i]=mod.getName(i*specialSide);
			cardPics[i]=mod.getPic(i*specialSide);
			baseCost[i]=mod.getCost(i*specialSide);
			baseEffect[i]=mod.getEffect(i*specialSide);
			baseCooldown[i]=mod.getCooldown(i*specialSide);
			rules[i]=mod.getRules(i*specialSide);
			code[i]=mod.getCode(i*specialSide);
		}
		if(mod instanceof Weapon){
			for(int i=0;i<2;i++){
				shots[i]=((Weapon)mod).getShots(i*specialSide);	
			}
		}
		type=m.type;
	}

	//Checking whose card clicked on//
	public void click(){
		if(getShip().player){
			playerClick();
		}
		else{
			enemyClick();
		}
	}

	public void playerClick(){
		if(Tutorial.stopClick())return;
		//First check if you're in a playable phase//
		if(Battle.getPhase()==Phase.End||Battle.getPhase()!=Phase.ShieldPhase&&Battle.getPhase()!=Phase.WeaponPhase){
			Clip.error.play();
			return;	
		}

		//Then see if you're supposed to be augmenting//
		if(Battle.getState()==State.Augmenting){
			if(this==Battle.augmentSource){
				deAugment();return;		//You can undo augments!//
			}			
			if(validAugmentTarget(Battle.augmentSource)){
				augmentThis(Battle.augmentSource);return;	//Only augment if it's a valid target//			
			}
			return;						//But you can't play cards while to augment//
		}

		//You can undo targets too//
		if(this==Battle.targetSource){deTarget();return;}

		if(Battle.getState()==State.CycleDiscard){
			if(selected){
				Clip.error.play();
				return;
			}
			getShip().discard(this);
			Battle.help.done();
			Battle.setState(State.CycleGet);
			Clip.cardDeselect.play();
			return;
		}

		if(Battle.getState()==State.CycleGet){
			if(CycleButton.choices.contains(this)){
				CycleButton.get().choose(this);
				Clip.cardSelect.play();
				return;
			}
			Clip.error.play();
			return;
		}

		if(Battle.getState()==State.ModuleChoose){
			if(this==Battle.moduleChooser){
				deModuleChoose();
				return;
			}

		}
		
		if(selected&&wasScrambled){
			scrambDeselect();
			return;
		}

		if((Battle.getPhase()==Phase.WeaponPhase&&mod.type==ModuleType.SHIELD)||(Battle.getPhase()==Phase.ShieldPhase&&mod.type==ModuleType.WEAPON)){
			Clip.error.play();
			System.out.println("Wrong state"); return;						//Wrong phase//
		}
		
		if(mod.getBuffAmount(BuffType.Scrambled)>0){
			Clip.cardSelect.play();
			scrambSelect();
			return;
		}

		//If it's an augment, must be valid//
		if(getCode().contains(Special.Augment)){
			if(!validAugmentPlay()){
				Clip.error.play();
				return;
			}
		}

		if(Battle.getState()!=State.Nothing){
			Clip.error.play();
			return;
		}



		//And deselect cards//
		if(selected){deselect(true);return;}

		//Then check to see if you are unable to play the card//
		if(getShip().getEnergy()<getCost()){
			Clip.error.play();
			System.out.println("Not enough energy to play "+this); return;	//Not enough Energy//			
		}	
		if(mod.getCurrentCooldown()>0){
			Clip.error.play();
			System.out.println(this+" is cooling down"); return;			//Cooling down//
		}
		
		playerSelect();														//You did it!//

	}



	//Clicking on an enemy card to speed it up//
	public void enemyClick(){
		enemyPlay();				//Causes the enemy to play//
		getShip().enemyPickCard();	//And pick another card//
		mod.moused=false;
	}

	//General Select method for player and enemy//
	private void select() {
		//Code stuff//
		CardCode code=getCode();
		Ship ship=getShip();


		//Paying costs//
		ship.addEnergy(-getCost()); 
		mod.increaseCooldown(getCoolDown());

		//Adding to list of ordered cards//
		ship.playList.add(this);

		
		if(ship.player){
			if(code.contains(Special.ModuleChooser))moduleChoose();
			if(code.contains(Special.Targeted)){
				targetSelect();		
				return;
			}
		}
		
		
		//General stuff//
		if(code.contains(Special.IncreaseEffect))mod.addBuff(new Buff(BuffType.BonusEffeect, code.getAmount(Special.IncreaseEffect), this, false));
		if(code.contains(Special.PermanentIncreaseEffect))mod.addBuff(new Buff(BuffType.BonusEffeect, code.getAmount(Special.PermanentIncreaseEffect), this, true));
		if(code.contains(Special.ReduceCost))mod.addBuff(new Buff(BuffType.ReduceCost, code.getAmount(Special.ReduceCost), this, false));
		if(code.contains(Special.BonusShots))mod.addBuff(new Buff(BuffType.BonusShot, code.getAmount(Special.BonusShots), this, false));
		ship.addIncome(code.getAmount(Special.EnergyIncome));

		ship.drawCard(code.getAmount(Special.DrawCard));
		ship.addEnergy(code.getAmount(Special.GainEnergy));
		ship.addToEnergyAtEndOfPhase(code.getAmount(Special.EnergyIfEmpty));


		if(code.contains(Augment.AugmentAll)){
			for(Card c:ship.hand){
				if(c!=this)c.augmentThis(this);
			}
		}

		//Enemies play cards normally, players play discardwhenclickeds immediately//
		if(code.contains(Special.DiscardWhenPlayed)&&getShip().player){
			getShip().discard(this);
			CardIcon.addIcon(this);
			mod.moused=false;
		}

		//Shield stuff//
		if(code.contains(Special.AddShieldPoints))getShip().addShield(this,getEffect());
		if(code.contains(Special.Bubble))getShip().shieldAll(this,getEffect());
		if(code.contains(Special.ShieldComputer))for(int i=0;i<getEffect();i++)getShip().getComputer().shield(new ShieldPoint(this,i==0),false);
		if(code.contains(Special.ShieldGenerator))for(int i=0;i<getEffect();i++)getShip().getGenerator().shield(new ShieldPoint(this,i==0),false);
		if(code.contains(Special.ShieldWeapons))for(Weapon w:ship.getWeapons())for(int i=0;i<getEffect();i++)w.shield(new ShieldPoint(this, i==0),false);
		if(code.contains(Special.ThisInvuln))mod.immune=true;



		//Weapon stuff//
		if(type==ModuleType.WEAPON&&!code.contains(Special.Targeted)){
			Weapon weapon=(Weapon) mod;
			for(int i=0;i<getShots();i++){
				weapon.addAttack(this);
			}
		}
	}

	//Special select method for player, about states//
	public void playerSelect(){
		CardCode code=getCode();
		Ship ship=getShip();
		//special case//
		if(code.contains(Special.EnergyIfEmpty)&&ship.getEnergy()!=0){
			Clip.error.play();
			return;
		}
		if(code.contains(Special.MustBeMajorDamaged)&&mod.currentThreshold==0){
			Clip.error.play();
			return;
		}
		if(code.contains(Special.MustBeUndamaged)&&mod.getDamage()>0){
			Clip.error.play();
			return;
		}
		//Complicated bit about deselecting cards. First you have to deselect all cards that rely on this card. Currently only for reducecost//
		Special unplaySpecial=null;
		if(code.contains(Special.ReduceCost))unplaySpecial=Special.ReduceCost;
		if(code.contains(Special.IncreaseEffect))unplaySpecial=Special.IncreaseEffect;
		if(code.contains(Special.BonusShots))unplaySpecial=Special.BonusShots;
		if(unplaySpecial!=null){
			for(int i=0;i<getShip().playList.size();i++){
				Card c=getShip().playList.get(i);
				CardCode checkCode=c.getCode();
				if(c!=this&&c.mod==mod){
					if(checkCode.contains(unplaySpecial))continue;
					if(c.wasScrambled)continue;
					c.deselect(false);
					i--;
				}
			}
		}
		/*if(code.contains(Special.ReduceCost)){
			for(int i=0;i<ship.playList.size();i++){
				Card c=ship.playList.get(i);
				if(c.mod==mod&&!c.getCode().contains(AI.ReduceCost)){
					c.deselect(false);
					i--;
				}
			}
		}*/
		if(!(type==ModuleType.SHIELD&&getEffect()>0&&!getCode().contains(Special.AddShieldPoints)))Clip.cardSelect.play();
		if(code.contains(Special.ShieldChosenModule))Clip.cardSelect.play();
		selected=true;


		//Enemy doesn't need to access augment state//
		if(code.contains(Special.Augment))augmentSelect();



		
		select();


	}



	//Special select method for enenmy. Just deals with graphical junk and targeting//
	public void enemySelect() {
		
		enemyCardsToRender.add(getGraphic());
		
		CardCode code = getCode();
		Ship ship=getShip();
		getGraphic().hideLower();
		getGraphic().moveToTop();
		getGraphic().setPosition(CardGraphic.enemyPlayStartPosition);
		getGraphic().slide(CardGraphic.enemyPlayToPosition, 1.5f, Interp.SQUARE);
		getGraphic().finishFlipping();
		if(mod.getBuffAmount(BuffType.Scrambled)>0){
			scrambSelect();
			return;
		}

		if(code.contains(Special.Targeted)){
			//pick a target nicely//
			Ship enemy=getShip().getEnemy();
			System.out.println("checking targeted stuff");

			Module target=enemy.getRandomUndestroyedModule();
			int required=1000;

			//Override target due to almost destroying//
			for(Module m:enemy.getRandomisedModules()){
				if(m.isDead())continue;
				int thisReq=m.getDamageUntilMajor();
				if(thisReq<required){
					target=m;
					required=thisReq;
				}
			}

			//Override due to focus target//
			if(ship.focusTarget!=null){
				target=ship.focusTarget; 
			}

			//Override target due to bonus damage vs specific things//
			for(Module m:enemy.getRandomisedModules()){
				if(code.contains(Special.BonusVsPristine)&&m.getDamage()==0){
					System.out.println("overriding due to pristine found");
					target=m;
					break;
				}
				if(code.contains(Special.BonusVsWeapon)&&m.type==ModuleType.WEAPON){
					System.out.println("overriding due to weapon found");
					target=m;
					break;
				}
				if(code.contains(Special.BonusVsGenerator)&&m.type==ModuleType.GENERATOR){
					System.out.println("overriding due to generator found");
					target=m;
					break;
				}
				if(code.contains(Special.BonusVsComputer)&&m.type==ModuleType.COMPUTER){
					System.out.println("overriding due to generator found");
					target=m;
					break;
				}
				//Tutorishit//
				if(code.contains(Special.BonusVsModule0)&&m.index==0){
					System.out.println("overriding due to tutorishit");
					target=m;
					break;
				}
				if(code.contains(Special.BonusVsModule1)&&m.index==1){
					System.out.println("overriding due to tutorishit");
					target=m;
					break;
				}
			}

			if(code.contains(AI.OtherTargeted)){
				ship.focusTarget=target;	
			}
			((Weapon)mod).addAttack(this, target);
			
			for(int i=0;i<getShots()-1;i++){
				((Weapon)mod).addAttack(this);
			}
		}
	

		if(code.contains(Special.Augment)){
			//First get the whole list of available targets//
			ArrayList<Card> possible=new ArrayList<Card>();
			for(Card c:ship.hand){
				if(c.validAugmentTarget(this))possible.add(c);
			}
			if(possible.size()>0){
				Card c=Draw.getRandom(possible);
				c.augmentThis(this);
			}
			else{
				System.out.println("No augment targets");
				ship.drawCard(code.getAmount(Augment.AugmentDrawCard));
			}
		}

		if(code.contains(Special.ShieldChosenModule)){
			for(Module m:ship.getRandomisedModules()){
				if(m.getShieldableIncoming()>=getEffect()-1){
					for(int i=0;i<getEffect();i++)m.shield(new ShieldPoint(this, i==0), false);
				}
			}
		}

		select();

		//if(!(mod.type==ModuleType.SHIELD&&getEffect()>0))Clip.cardSelect.play();

	}



	private void deselect(boolean playSound){
		CardCode code=getCode();
		Ship ship=getShip();
		
		//Reasons not to deselect//
		//if(wasScrambled)return;
		if(code.getAmount(Special.GainEnergy)>ship.getEnergy()){
			Clip.error.play();
			return;
		}
		
		
		//ok unplaying!
		selected=false;
		
		//Complicated bit about deselecting cards. First you have to deselect all cards that rely on this card. Currently only for reducecost//
		Special unplaySpecial=null;
		System.out.println("ra");
		if(code.contains(Special.ReduceCost))unplaySpecial=Special.ReduceCost;
		if(code.contains(Special.IncreaseEffect))unplaySpecial=Special.IncreaseEffect;
		if(code.contains(Special.BonusShots))unplaySpecial=Special.BonusShots;
		if(unplaySpecial!=null){
			for(int i=0;i<getShip().playList.size();i++){
				Card c=getShip().playList.get(i);
				CardCode checkCode=c.getCode();
				if(c!=this&&c.mod==mod){
					if(checkCode.contains(unplaySpecial))continue;
				
					if(c.wasScrambled)continue;
					c.deselect(false);
					i--;
				}
			}
		}
		if(getCost()!=0){
			for(Card c:ship.hand){
				if(c.selected&&c.getCode().contains(Special.EnergyIfEmpty)){
					
					c.deselect(false);
				}
			}
		}

		//Resetting code stuff//
		mod.removeBuffs(this);

		ship.addIncome(-code.getAmount(Special.EnergyIncome));
		ship.addEnergy(-code.getAmount(Special.GainEnergy));

		if(code.contains(Special.ThisInvuln))mod.immune=false;
		//Clearing shields//
		if(type==ModuleType.SHIELD)ship.unShield(this);
		mod.removeIncoming(this);

		//Uncharing weapons//
		if(type==ModuleType.WEAPON){
			Weapon weapon=(Weapon) mod;
			if(getShots()>0){
				weapon.removeAttack(this);
			}
		}

		//Getting resources back
		mod.increaseCooldown(-getCoolDown());
		ship.addEnergy(getCost());

		ship.playList.remove(this);
		
		if(playSound)Clip.cardDeselect.play();
	}

	//General Play method//
	public void play() {
		CardCode code=getCode();
		Ship ship = getShip();
		ship.hand.remove(this);
		getGraphic().fadeOut(CardGraphic.fadeSpeed, CardGraphic.fadeType);

		//Enemies play cards normally, players play discardwhenclickeds immediately//
		for(int i=0;i<code.getAmount(Special.SelfScramble);i++)mod.scramble();
		for(int i=0;i<code.getAmount(Special.selfDamage);i++)mod.damage(new DamagePoint(this));

		if(((getCode().contains(Special.DiscardWhenPlayed)||getCode().contains(Special.Augment))&&getShip().player))
			return;
		CardIcon.addIcon(this);
	}

	//The Player plays when you end your turn//
	public void playerPlay(){
		play();
	}

	//The enemy plays when you click on their card or after a period(not in yet)//
	public void enemyPlay(){
		play();
	}



	private void scrambSelect() {
		wasScrambled=true;
		selected=true;
		mod.removeSramble();
		getShip().playList.add(this);
	}

	private void scrambDeselect() {
		mod.scramble();
		for(Card c:getShip().hand){
			if(c.selected&&!c.wasScrambled&&c.mod==mod){
				c.deselect(false);
			}
		}
		selected=false;
		wasScrambled=false;
		Clip.cardDeselect.play();
		getShip().playList.remove(this);
	}	

	//State and graphics stuff when you select a targeted card//
	private void targetSelect(){

		previousIndexInHand=getShip().hand.indexOf(this);
		Battle.setState(State.Targeting);
		Battle.targetSource=this;
		getShip().hand.remove(this);
		getShip().updateCardPositions();
		getGraphic().moveUp();
		getGraphic().hideLower();
		CardGraphic.setAugmentOrTarget(getGraphic());
	}


	//Deselecting a targeted card//
	private void deTarget() {

		getShip().hand.add(previousIndexInHand, this);
		Battle.setState(State.Nothing);
		Battle.targetSource=null;
		getShip().updateCardPositions();
		getGraphic().showLower();
		deselect(true);
	}

	//Method gets called from clicking an enemy module//
	public void targetChosen() {

		getShip().hand.add(previousIndexInHand, this);
		Battle.setState(State.Nothing);
		Battle.targetSource=null;
		getShip().updateCardPositions();
		getGraphic().showLower();
	}

	private void moduleChoose() {
		previousIndexInHand=getShip().hand.indexOf(this);
		Battle.setState(State.ModuleChoose);
		Battle.moduleChooser=this;
		getShip().hand.remove(this);
		getShip().updateCardPositions();
		getGraphic().moveUp();
		getGraphic().hideLower();
		
	}

	public void moduleChosen() {
		CardCode code=getCode();
		Ship ship=getShip();

		if(code.contains(Special.DiscardWhenChosen)){
			ship.discard(this);
		}
		else{
			ship.hand.add(previousIndexInHand, this);
			ship.updateCardPositions();
			getGraphic().showLower();
		}

		Battle.setState(State.Nothing);

	}

	public void deModuleChoose(){
		getShip().hand.add(previousIndexInHand, this);
		Battle.setState(State.Nothing);
		getShip().updateCardPositions();
		getGraphic().showLower();
		deselect(true);
	}

	public boolean validAugmentPlay(){
		/*if(getShip().player&&(Fight.getState()!=State.Augmenting||Fight.augmentSource==null)){
			System.out.println("trying to check valid augment when not in right state");
			return false;			
		}*/
		for(Card c:getShip().hand){
			if(c.validAugmentTarget(this))return true;
		}
		return false;
	}


	public boolean validAugmentTarget(Card augmenter){
		CardCode code=augmenter.getCode();
		boolean checkForWeapon=code.contains(Augment.AugmentWeapon);
		boolean checkForSameSystem=code.contains(Augment.AugmentThis);

		if(augmenter==this)return false; //Same card
		if(checkForWeapon&&type==ModuleType.WEAPON){
			if(code.contains(Augment.AugmentTargeted)){
				if(getCode(0).contains(Special.Targeted))return false;
			}
			return true;
		}
		if(checkForSameSystem&&mod==augmenter.mod)return true;

		return false;
	}

	//Method gets called from clicking on a valid augment target//
	private void augmentSelect() {
		previousIndexInHand=getShip().hand.indexOf(this);
		Battle.setState(State.Augmenting);
		Battle.augmentSource=this;
		getShip().hand.remove(this);
		getShip().updateCardPositions();
		getGraphic().moveUp();
		getGraphic().hideLower();
		if(mod.ship.player)CardGraphic.setAugmentOrTarget(getGraphic());
	}

	//Graphical and state stuff from choosing not to augment//
	private void deAugment() {
		getShip().hand.add(previousIndexInHand, this);
		Battle.setState(State.Nothing);
		Battle.augmentSource=null;
		getShip().updateCardPositions();
		getGraphic().showLower();
		deselect(true);
	}

	//The chosen card to augment//
	private void augmentThis(Card augmenter) {
		CardCode augCode=augmenter.getCode();
		if(augmenter==Battle.augmentSource){
			Battle.augmentSource.getGraphic().fadeOut(CardGraphic.fadeSpeed, CardGraphic.fadeType);
			Battle.setState(State.Nothing);
			Battle.augmentSource=null;
			CardIcon.addIcon(augmenter);
		}

		//General bonuses//
		bonusEffect+=augCode.getAmount(Augment.AugmentDamage);
		if(augCode.contains(Augment.AugmentDrawCard))getShip().drawCard(1);

		//Per-side bonuses//
		for(int i=0;i<2;i++){
			//This is generall for weapon augments that affect shooting so non-combat abilities don't get boosted//
			if(getShots(i)>0){
				if(augCode.contains(Augment.AugmentTargeted)){
					code[i].add(Special.Targeted);
				}
			}
		}

		//Making the card icon of the augmenting card//

	}



	public void rightClick(){
		if(Battle.getPhase()==Phase.End||Battle.getState()!=State.Nothing)return;
		if(selected)return;
		if(Tutorial.stopFlip())return;
		Clip.cardFlip.play();
		flip();
	}

	public CardGraphic getGraphic(){
		if(cg==null){
			cg=new CardGraphic(this);
		}
		return cg;
	}
	
	public CardGraphic getHalfGraphic(boolean botSide){
		if(botSide)side=1;
		else side=0;
		if(cg==null){
			cg=new CardGraphic(this, 0, 0);
		}
		return cg;
	}
	
	public void flip(){side=1-side;}
	public Ship getShip(){return mod.ship;}



	//Big list of getter methods//
	public String getName(){return getName(side);}
	public String getName(int pick){return names[pick];}

	public Pic getImage(){return getImage(side);}
	public Pic getImage(int pick){return cardPics[pick];}

	public int getCost(){return getCost(side);}
	public int getCost(int pick){
		if(getCode(pick).contains(Special.ReduceCost)){
			return baseCost[pick];
		}
		return Math.max(0, baseCost[pick]-mod.getBuffAmount(BuffType.ReduceCost));}

	public int getEffect(){return getEffect(side);}
	public int getEffect(int pick){
		if(baseEffect[pick]==0)return 0;
		return baseEffect[pick]+bonusEffect+mod.getBuffAmount(BuffType.BonusEffeect);
	}

	public int getCoolDown(){return getCoodlown(side);}
	public int getCoodlown(int pick){return baseCooldown[pick];}

	public String getRules(){return getRules(side);}
	public String getRules(int pick){return rules[pick];}

	public CardCode getCode(){return getCode(side);}
	public CardCode getCode(int pick){return code[pick];}

	public int getShots(){return getShots(side);}
	public int getShots(int pick){
		if(mod instanceof Weapon){
			return ((Weapon)mod).getShots(pick*specialSide);
		}
		return 0;
	}

	public boolean hasSpecial(Special test){return code[side].contains(test);}
	public boolean hasSpecial(Special test, int pick){return code[pick].contains(test);}

	public String toString(){
		return getName();
	}

	public boolean enemyDecide(int side) {
		this.side=side;
		Ship ship=getShip();
		CardCode code=getCode();
		int currentEnergy=ship.getEnergy();
		int cost=getCost();

		if(code.contains(AI.CheckOriginalFirst)){
			if(enemyDecide(0))return true;
			this.side=side;
		}

		System.out.println("Checking "+this);

		if(code.contains(AI.OverrideIfOtherSideIgnore)){
			if(getCode(1-side).contains(AI.Ignore)){
				System.out.println("Overriding because other side is ignore");
				return true;
			}
		}



		//Checking basic stuff//
		if(getCost()>currentEnergy){
			no("Not enough energy");
			return false;
		}
		if(mod.getCurrentCooldown()>0){
			no("Cooling down");
			return false;
		}

		//Checking special cases//
		if(code.contains(Special.MustBeMajorDamaged)&&mod.currentThreshold==0){
			no("must be damaged");
			return false;
		}
		if(code.contains(Special.MustBeUndamaged)&&mod.getDamage()!=0){
			no("must be pristine");
			return false;
		}

		//Checking stuff if augment//
		if(code.contains(Special.Augment)){
			if(!validAugmentPlay()){
				no("Invalid augment type");
				return false;
			}
		}

		//Special stuff for targeted cards//
		if(code.contains(Special.Targeted)){
			if(ship.focusTarget!=null){
				if(!getCode(1-side).contains(Special.Targeted)){
					ok("Focusing and other side not target so overriding");
					return true;
				}
			}
		}



		for(AIclass aiclass:code.getAIs()){
			AI ai=aiclass.ai;
			switch(ai){


			/*
			 * 
			 * GENERAL STUFF			 
			 *  
			 * 
			 */

			case SurplusEnergy:
				if(currentEnergy>=cost+aiclass.number){
					ok(aiclass, "excess energy is "+(currentEnergy-cost));
				}
				else{
					no(aiclass, "excess energy is "+(currentEnergy-cost));
					return false;
				}
				break;

			case LowEnergy:
				if(currentEnergy<cost+1){
					ok(aiclass, "excess energy is "+(currentEnergy-cost));
				}
				else{
					no(aiclass, "excess energy is "+(currentEnergy-cost));
					return false;
				}
				break;

			case OtherCardsThisSystem:
				int totalCardsThisSystem=-1; //to cancel out this card//
				for(Card c:ship.hand){
					if(c.mod==mod)totalCardsThisSystem++;
				}
				if(totalCardsThisSystem>=aiclass.number){
					ok(aiclass, "other cards of this system "+totalCardsThisSystem);
				}
				else{
					no(aiclass, "other cards of this system "+totalCardsThisSystem);
					return false;
				}
				break;

			case LowChance:
				if(Math.random()>.8f){
					ok(aiclass, "passed");
				}
				else{
					no(aiclass, "failed");
					return false;
				}
				break;

			case EvenChance:
				if(Math.random()>.5f){
					ok(aiclass, "passed");
				}
				else{
					no(aiclass, "failed");
					return false;
				}
				break;

			case HighChance:
				if(Math.random()>.2f){
					ok(aiclass, "passed");
				}
				else{
					no(aiclass, "failed");
					return false;
				}
				break;

			case ReduceCost:
				if(mod.getBuffAmount(BuffType.ReduceCost)>0){
					no(aiclass, "already reduced cost");
					return false;
				}
				ok(aiclass, "not already reduced cost");
				break;

			case DamageSelf:
				if(mod.getDamageUntilMajor()<=aiclass.number){
					no(aiclass, "damage until: "+mod.getDamageUntilMajor());
					return false;
				}
				ok(aiclass, "damage until: "+mod.getDamageUntilMajor());
				break;
			case DamagedModules:
				int damagedModules=0;
				for(Module m:ship.getRandomisedModules()){
					if(m.currentThreshold>0)damagedModules++;
				}
				if(damagedModules>=aiclass.number){
					ok(aiclass, damagedModules+" damaged modules");
				}
				else{
					no(aiclass, damagedModules+" damaged modules");
					return false;
				}

				break;

			case CheckOriginalFirst:
				break;
			case Ignore:
				no(aiclass,"");
				return false;

				/*
				 * 
				 * 
				 * Weapon Stuff
				 * 
				 * 
				 */



			case PlayerPristineSystems:
				int pristines=0;
				for(Module m: ship.getEnemy().getRandomisedModules()){
					if(m.getDamage()==0)pristines++;
				}
				if(code.contains(Special.Targeted)&&pristines>0){	
					ok(aiclass, "is targeted and pristines "+pristines);
				}
				else if(pristines>=aiclass.number){
					ok(aiclass, "pristines "+pristines);
				}
				else{
					no(aiclass, "pristines "+pristines);
					return false;
				}
				break;

			case BetterAgainstSpecificSystem:
				if(code.contains(Special.Targeted)){
					ok(aiclass, "is targeted so perfect!");
				}
				else{
					if(Math.random()>.8f){
						ok(aiclass, "not targeted but chance!");
					}
					else{
						no(aiclass,"");
						return false;
					}
				}
				break;

			case MajorDamagedEnemySystems:
				int majorDamagedEnemySystems=0;
				for(Module m:ship.getEnemy().getRandomisedModules()){
					if(m.currentThreshold>0)majorDamagedEnemySystems++;
				}
				if(majorDamagedEnemySystems>0&&code.contains(Special.Targeted)){
					ok(aiclass, "is targeted and "+majorDamagedEnemySystems+ " so perfect!");
				}
				if(majorDamagedEnemySystems>=aiclass.number){
					ok(aiclass, majorDamagedEnemySystems+" damaged enemy systems");
				}
				else{
					no(aiclass, majorDamagedEnemySystems+" damaged enemy systems");
					return false;
				}
				break;

			case OtherTargeted:
				int otherTargetedCards=0;
				for(Card c:ship.hand){
					for(int i=0;i<2;i++){
						if(c!=this&&c.getCode(i).contains(Special.Targeted)){
							if(ship.getEnergy()>=c.getCost(i)*2){
								otherTargetedCards++;
							}
							break;
						}
					}
				}
				if(otherTargetedCards>=aiclass.number){
					ok(aiclass, "other targeteds with enough energy "+otherTargetedCards);
				}
				else{
					no(aiclass, "other targeteds with enough energy "+otherTargetedCards);
					return false;
				}
				break;

			case OtherUntargeted:
				int otherUntargeted=0;
				for(Card c:ship.hand){
					for(int i=0;i<2;i++){
						if(c==this)continue;
						if(c.getShots(i)<=0)continue;
						if(c.getCode(i).contains(Special.Targeted))continue;
						otherUntargeted++;
						break;
					}
				}

				if(otherUntargeted>=aiclass.number){
					ok(aiclass, "other untargeteds "+otherUntargeted);
				}
				else{
					no(aiclass, "other untargeteds "+otherUntargeted);
					return false;
				}
				break;


				/*
				 * 
				 * 
				 * Shield stuff
				 * 
				 * 
				 */

			case RegularShield:
				int incoming=getShip().getTotalShieldableIncoming();
				if(incoming>0&&incoming>getEffect()-1){
					ok(aiclass, "incoming is "+incoming);
					break;
				}
				boolean found=false;
				for(Module m:ship.getRandomisedModules()){
					if(m.getShieldsRequiredToAvoidMajor()<=getEffect()&&side==0){
						ok(aiclass, "Can stop major on"+m+" even though it would be inefficient");
						found=true;
					}
				}
				if(!found){
					no(aiclass, "incoming is "+incoming);
					return false;
				}
				break;


			case IncomingComputer:
				if(ship.getComputer().getShieldableIncoming()>=aiclass.number){
					ok(aiclass, "incoming >0");
				}
				else{
					no(aiclass, "incomg <0");
					return false;
				}
				break;

			case IncomingOnMajorDamaged:
				int totalShieldable=0;
				for(Module m:ship.getRandomisedModules()){
					if(m.currentThreshold>0){
						totalShieldable+=m.getShieldableIncoming();
					}
				}
				if(totalShieldable>=aiclass.number){
					ok(aiclass, "total shieldable: "+totalShieldable);
				}
				else{
					no(aiclass, "total shieldable: "+totalShieldable);
					return false;
				}
				break;

			case IncomingAll:
				int incomingAllShielded=0;
				for(Module m:ship.getRandomisedModules()){
					incomingAllShielded+=Math.min(m.getShieldableIncoming(), getEffect());
				}
				if(incomingAllShielded>getEffect()*2){
					ok(aiclass, "amount to shield: "+incomingAllShielded);
				}
				else{
					no(aiclass, "amount to shield: "+incomingAllShielded);
					return false;
				}
				break;

			case TotalIncoming:
				int finc=0;
				for(Module m:ship.getRandomisedModules()){
					finc+=m.getShieldableIncoming();
				}
				if(finc>=aiclass.number){
					ok(aiclass, "incoming: "+finc);
				}
				else{
					no(aiclass, "incoming: "+finc);
					return false;
				}

				break;

			case IncomingGenerator:
				int genInc=ship.getGenerator().getShieldableIncoming();
				if(genInc>=aiclass.number){
					ok(aiclass, "incoming is "+genInc);
				}
				else{
					no(aiclass, "incoming is "+genInc);
					return false;
				}
				break;

			case TotalIncomingThis:
				int thisInc=mod.getTotalIncoming();
				if(thisInc>=aiclass.number){
					ok(aiclass, "incoming is "+thisInc);
				}
				else{
					no(aiclass, "incoming is "+thisInc);
					return false;
				}
				break;

			case IncomingWeapons:
				int weaponInc=0;
				for(Weapon w:ship.getWeapons())weaponInc+=Math.min(getEffect(), w.getShieldableIncoming());
				if(weaponInc>=aiclass.number){
					ok(aiclass, "incoming is "+weaponInc);
				}
				else{
					no(aiclass, "incoming is "+weaponInc);
					return false;
				}

			case ShieldAll:
				int totalShielded=0;
				for(Module m:ship.getRandomisedModules()){
					totalShielded+=Math.min(m.getShieldableIncoming(), getEffect());
				}
				if(totalShielded>=aiclass.number){
					ok(aiclass, "total "+totalShielded);
				}
				else{
					no(aiclass, "total "+totalShielded);
					return false;
				}
				break;

			case SingleModuleIncoming:
				for(Module m:ship.getRandomisedModules()){
					if(m.getShieldableIncoming()>=aiclass.number){
						ok(aiclass, m+"");
						break;
					}
					else{
						no(aiclass, "not found");
						return false;
					}
				}
				break;

			case Singleton:
				for(Card c:ship.hand){
					if(c!=this&&c.sameAs(this)){
						no(aiclass, c + " is same as "+this);
						return false;
					}
				}
				ok(aiclass, "");
				break;

			}	
		}
		System.out.println("Ok to play");
		return true;
	}

	public void ok(AIclass aiclass, String addition){
		System.out.println("ok "+aiclass.ai+" "+addition+(aiclass.number==-1?"":(" ("+aiclass.number+")")));
	}
	public void ok(String addition){
		System.out.println("ok "+addition);
	}
	public void no(AIclass aiclass, String addition){
		System.out.println("NO!! "+aiclass.ai+" "+addition+(aiclass.number==-1?"":(" ("+aiclass.number+")")));
	}
	public void no(String addition){
		System.out.println("NO!! "+" "+addition);
	}
	public boolean sameAs(Card c){
		return c.getName(1).equals(getName(1));
	}
	public void zSetEffect(int eff){
		baseEffect[0]=eff;
		baseEffect[1]=eff;
	}

	
	
}