package game.card;

import java.util.ArrayList;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import util.Draw;
import util.image.Pic;
import game.assets.Sounds;
import game.card.CardCode.AI;
import game.card.CardCode.AIclass;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.Component;
import game.module.component.shield.Shield;
import game.module.component.weapon.Weapon;
import game.module.junk.Buff;
import game.module.junk.DamagePoint;
import game.module.junk.ShieldPoint;
import game.module.junk.Buff.BuffType;
import game.screen.battle.Battle;
import game.screen.battle.Battle.Phase;
import game.screen.battle.Battle.State;
import game.screen.battle.interfaceJunk.CycleButton;
import game.screen.battle.interfaceJunk.HelpPanel;
import game.screen.battle.tutorial.Tutorial;
import game.ship.Ship;

public class Card {
	public Module mod;
	public Component component;
	//Card Stats//
	private String[] name = new String[2];
	private Pic[] cardPic = new Pic[2];
	private int[] baseCost = new int[2];
	private int[] baseEffect = new int[2];
	private int bonusEffect=0;
	private int[] baseCooldown = new int[2];
	private String[] rules = new String[2];
	private int[] shots = new int[2];
	private int bonusShots=0;
	private CardCode[] code=new CardCode[2];
	public int specialSide=-1;
	public ModuleType type;
	public boolean consumable;
	//Random Fields//
	public int side=0;
	public boolean selected;
	private CardGraphic cg;
	private int previousIndexInHand;
	public static ArrayList<CardGraphic> extraCardsToRender=new ArrayList<CardGraphic>();
	private Component chosenModule;
	public boolean wasScrambled;
	public boolean[] augmented=new boolean[]{false, false};
	public boolean active;
	public boolean addToDeck;


	//Setting up card//
	public Card(Module m){
		mod=m;

		if(m instanceof Component)component=(Component) m;
		/*specialSide=side;
		for(int i=0;i<2;i++){
			name[i]=mod.getName(i*specialSide);
			cardPic[i]=mod.getPic(i*specialSide);
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
		type=m.type;*/

	}

	public Card(String[] names, Pic[] cardpics, int[] baseCosts, int[] baseEffects, int[] baseCooldown, int[] shots, String[] rules, CardCode[] codes, ModuleType type){
		//mod=s.getSpecialComponent();
		this.name=names;
		this.cardPic=cardpics;
		this.baseCost=baseCosts;
		this.baseEffect=baseEffects;
		this.baseCooldown=baseCooldown;
		this.rules=rules;
		this.code=codes;
		this.type=type;
		this.shots=shots;
		consumable=true;
	}

	public void finaliseSide(){
		if(consumable)remakeCard(1);
		remakeCard(mod.getNextCardSide());
	}

	public void remakeCard(int side){
		if(consumable)return;
		specialSide=side;
		for(int i=0;i<2;i++){
			name[i]=mod.getName(i*specialSide);
			cardPic[i]=mod.getPic(i*specialSide);
			baseCost[i]=mod.getCost(i*specialSide);
			baseEffect[i]=mod.getEffect(i*specialSide);
			baseCooldown[i]=mod.getCooldown(i*specialSide);
			rules[i]=mod.getRules(i*specialSide);
			code[i]=mod.getCode(i*specialSide);
		}
		if(type== ModuleType.WEAPON){
			for(int i=0;i<2;i++){
				shots[i]=mod.getShots(i*specialSide);	
			}
		}
		type=mod.cardType;
	}

	//Checking whose card clicked on//
	public void click(){
		if(getShip().player){
			System.out.println("is player card");
			playerClick();
		}
	}

	public void playerClick(){
		if(Tutorial.stopClick())return;
		//First check if you're in a playable phase//
		if(Battle.getPhase()==Phase.End||Battle.getPhase()!=Phase.ShieldPhase&&Battle.getPhase()!=Phase.WeaponPhase){
			Sounds.error.play();
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
				Sounds.error.play();
				return;
			}
			getShip().discard(this);
			Battle.help.done();
			Battle.setState(State.CycleGet);
			Sounds.cardDeselect.play();
			return;
		}

		if(Battle.getState()==State.CycleGet){
			if(CycleButton.choices.contains(this)){
				CycleButton.get().choose(this);
				Sounds.cardSelect.play();
				return;
			}
			Sounds.error.play();
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

		if((Battle.getPhase()==Phase.WeaponPhase&&type==ModuleType.SHIELD)||(Battle.getPhase()==Phase.ShieldPhase&&type==ModuleType.WEAPON)){
			Sounds.error.play();
			System.out.println("Wrong state"); return;						//Wrong phase//
		}

		if(mod.getBuffAmount(BuffType.Scrambled)>0){
			Sounds.cardSelect.play();
			scrambSelect();
			return;
		}

		//If it's an augment, must be valid//
		if(getCode().contains(Special.Augment)){
			if(!validAugmentPlay()){
				Sounds.error.play();
				return;
			}
		}

		if(Battle.getState()!=State.Nothing){
			Sounds.error.play();
			return;
		}



		//And deselect cards//
		if(selected){deselect(true);return;}

		//Then check to see if you are unable to play the card//
		if(getShip().getEnergy()<getCost()){
			Sounds.error.play();
			System.out.println("Not enough energy to play "+this); return;	//Not enough Energy//			
		}	
		if(mod.getCurrentCooldown()>0){
			Sounds.error.play();
			System.out.println(this+" is cooling down"); return;			//Cooling down//
		}

		playerSelect();														//You did it!//

	}




	//General Select method for player and enemy//
	private void select() {
		//Code stuff//
		CardCode code=getCode();
		Ship ship=getShip();


		//Paying costs//
		ship.addEnergy(-getCost(), false); 
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
		if(code.contains(Special.IncreaseEffect))component.addBuff(new Buff(BuffType.BonusEffeect, code.getAmount(Special.IncreaseEffect), this, false));
		if(code.contains(Special.PermanentIncreaseEffect))component.addBuff(new Buff(BuffType.BonusEffeect, code.getAmount(Special.PermanentIncreaseEffect), this, true));
		if(code.contains(Special.ReduceCost))component.addBuff(new Buff(BuffType.ReduceCost, code.getAmount(Special.ReduceCost), this, false));
		if(code.contains(Special.BonusShots))component.addBuff(new Buff(BuffType.BonusShot, code.getAmount(Special.BonusShots), this, false));
		ship.addIncome(code.getAmount(Special.EnergyIncome));

		ship.drawCard(code.getAmount(Special.DrawCard));
		ship.addEnergy(code.getAmount(Special.GainEnergy), false);
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
		if(code.contains(Special.ShieldAll))getShip().shieldAll(this,getEffect());
		if(code.contains(Special.ShieldShield))for(int i=0;i<getEffect();i++)getShip().getShield().shield(new ShieldPoint(this,i==0),false);
		if(code.contains(Special.ShieldComputer))for(int i=0;i<getEffect();i++)getShip().getComputer().shield(new ShieldPoint(this,i==0),false);
		if(code.contains(Special.ShieldGenerator))for(int i=0;i<getEffect();i++)getShip().getGenerator().shield(new ShieldPoint(this,i==0),false);
		if(code.contains(Special.ShieldWeapons))for(Weapon w:ship.getWeapons())for(int i=0;i<getEffect();i++)w.shield(new ShieldPoint(this, i==0),false);




		//Weapon stuff//
		if(type==ModuleType.WEAPON&&!code.contains(Special.Targeted)){
			for(int i=0;i<getShots();i++){
				ship.addAttack(this);
			}
		}
	}

	//Special select method for player, about states//
	public void playerSelect(){
		CardCode code=getCode();
		Ship ship=getShip();
		//special case//
		if(code.contains(Special.EnergyIfEmpty)&&ship.getEnergy()!=0){
			Sounds.error.play();
			return;
		}
		if(code.contains(Special.MustBeMajorDamaged)&&component.currentThreshold==0){
			Sounds.error.play();
			return;
		}
		if(code.contains(Special.MustBeUndamaged)&&component.getDamage()>0){
			Sounds.error.play();
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
		if(!(type==ModuleType.SHIELD&&getEffect()>0&&!getCode().contains(Special.AddShieldPoints)))Sounds.cardSelect.play();
		if(code.contains(Special.ShieldChosenModule))Sounds.cardSelect.play();
		selected=true;


		//Enemy doesn't need to access augment state//
		if(code.contains(Special.Augment))augmentSelect();
		if(code.contains(Special.DiscardOthers))discardSelect();





		select();


	}



	//Special select method for enenmy. Just deals with graphical junk and targeting//
	public void enemySelectAndPlay() {

		extraCardsToRender.add(getGraphic());

		CardCode code = getCode();
		Ship ship=getShip();
		getGraphic().hideLower();
		getGraphic().moveToTop();
		getGraphic().setPosition(CardGraphic.enemyPlayStartPosition);
		getGraphic().finishFlipping();
		if(mod.getBuffAmount(BuffType.Scrambled)>0){
			scrambSelect();
			return;
		}

		if(code.contains(Special.Targeted)){
			//pick a target nicely//
			Ship enemy=getShip().getEnemy();


			Component target=enemy.getRandomUndestroyedModule();
			int required=1000;

			//Override target due to almost destroying//
			for(Component c:enemy.getRandomisedModules()){
				if(c.isDead())continue;
				int thisReq=c.getDamageUntilMajor();
				if(thisReq<required){
					target=c;
					required=thisReq;
				}
			}

			//Override due to focus target//
			if(ship.focusTarget!=null){
				target=ship.focusTarget; 
			}

			//Override target due to bonus damage vs specific things//
			for(Component c:enemy.getRandomisedModules()){
				if(code.contains(Special.BonusVsPristine)&&c.getDamage()==0){
					System.out.println("overriding due to pristine found");
					target=c;
					break;
				}
				if(code.contains(Special.BonusVsWeapon)&&c.type==ModuleType.WEAPON){
					System.out.println("overriding due to weapon found");
					target=c;
					break;
				}
				if(code.contains(Special.BonusVsGenerator)&&c.type==ModuleType.GENERATOR){
					System.out.println("overriding due to generator found");
					target=c;
					break;
				}
				if(code.contains(Special.BonusVsComputer)&&c.type==ModuleType.COMPUTER){
					System.out.println("overriding due to generator found");
					target=c;
					break;
				}
				//Tutorishit//
				if(code.contains(Special.BonusVsModule0)&&c.index==0){
					System.out.println("overriding due to tutorishit");
					target=c;
					break;
				}
				if(code.contains(Special.BonusVsModule1)&&c.index==1){
					System.out.println("overriding due to tutorishit");
					target=c;
					break;
				}
			}

			if(code.contains(AI.OtherTargeted)){
				ship.focusTarget=target;	
			}


			for(int i=0;i<getShots();i++){
				ship.addAttack(this, target);
				//TODO attacks from single cards
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
			for(Component c:ship.getRandomisedModules()){
				if(c.getShieldableIncoming()>=getEffect()-1){
					for(int i=0;i<getEffect();i++)c.shield(new ShieldPoint(this, i==0), false);
				}
			}
		}

		select();
		play();
		//if(!(mod.type==ModuleType.SHIELD&&getEffect()>0))Clip.cardSelect.play();

	}



	private void deselect(boolean playSound){
		CardCode code=getCode();
		Ship ship=getShip();

		//Reasons not to deselect//
		//if(wasScrambled)return;
		if(code.getAmount(Special.GainEnergy)>ship.getEnergy()){
			Sounds.error.play();
			return;
		}


		//ok unplaying!
		selected=false;

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
		if(getCost()!=0){
			for(Card c:ship.hand){
				if(c.selected&&c.getCode().contains(Special.EnergyIfEmpty)){

					c.deselect(false);
				}
			}
		}

		//Resetting code stuff//


		ship.addIncome(-code.getAmount(Special.EnergyIncome));
		ship.addEnergy(-code.getAmount(Special.GainEnergy), false);

		//Clearing shields//
		if(type==ModuleType.SHIELD)ship.unShield(this);
		if(component!=null){
			component.removeIncoming(this);
			component.removeBuffs(this);
		}

		//Uncharing weapons//
		if(type==ModuleType.WEAPON){

			if(getShots()>0){
				ship.removeAttack(this);
			}
			//TODO single attacks
		}

		//Getting resources back
		mod.increaseCooldown(-getCoolDown());
		ship.addEnergy(getCost(), false);

		ship.playList.remove(this);

		//deselecting modchoose stuff//
		if(code.contains(Special.ModuleChooser)){
			
			if(code.contains(Special.ImmuneChosenModule)){
				chosenModule.immune=false;
			}
			
			
		}
		
		if(playSound)Sounds.cardDeselect.play();
	}

	//General Play method//
	public void play() {
		CardCode code=getCode();
		Ship ship = getShip();
		ship.hand.remove(this);


		//Enemies play cards normally, players play discardwhenclickeds immediately//
		for(int i=0;i<code.getAmount(Special.SelfScramble);i++)component.scramble();
		for(int i=0;i<code.getAmount(Special.selfDamage);i++)component.damage(new DamagePoint(this));

		if(((getCode().contains(Special.DiscardWhenPlayed)||getCode().contains(Special.Augment))&&getShip().player))
			return;

		if(getCode().contains(Special.DestroyEnemyShield)){
			Component enemyShield=getShip().getEnemy().getShield();
			for(int i=0;i<enemyShield.maxHP;i++){
				enemyShield.damage(new DamagePoint(this));
			}
		}

	}

	public void fadeAndAddIcon(){
		extraCardsToRender.add(getGraphic());
		getGraphic().fadeOut(CardGraphic.fadeSpeed, CardGraphic.fadeType);
		CardIcon.addIcon(this);
	}

	//The Player plays when you end your turn//
	public void playerPlay(){
		play();
		fadeAndAddIcon();
	}




	private void scrambSelect() {
		wasScrambled=true;
		selected=true;
		component.removeSramble();
		getShip().playList.add(this);
	}

	private void scrambDeselect() {
		component.scramble();
		for(Card c:getShip().hand){
			if(c.selected&&!c.wasScrambled&&c.mod==mod){
				c.deselect(false);
			}
		}
		selected=false;
		wasScrambled=false;
		Sounds.cardDeselect.play();
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

	public void moduleChosen(Component component2) {
		chosenModule=component2;
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
		if(code.contains(Augment.AugmentAny))return true;
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
		Battle.help=new HelpPanel("Pick a card to augment",false);
	}

	public void discardSelect(){
		augmentSelect();
		Battle.help=new HelpPanel("Choose a card to discard",false);
	}



	//Graphical and state stuff from choosing not to augment//
	private void deAugment() {
		getShip().hand.add(previousIndexInHand, this);
		Battle.setState(State.Nothing);
		Battle.augmentSource=null;
		getShip().updateCardPositions();
		getGraphic().showLower();
		deselect(true);
		mod.ship.removeAttack(this);
	}

	//The chosen card to augment//
	private void augmentThis(Card augmenter) {
		CardCode augCode=augmenter.getCode();
		if(augCode.contains(Augment.AugmentDiscard))mod.ship.discard(this);
		if(augmenter==Battle.augmentSource){
			Battle.augmentSource.getGraphic().fadeOut(CardGraphic.fadeSpeed, CardGraphic.fadeType);
			Battle.setState(State.Nothing);
			Battle.augmentSource=null;
			CardIcon.addIcon(augmenter);
		}

		//General bonuses//
		mod.ship.addEnergy(augCode.getAmount(Augment.AugmentGainEnergy), false);
		bonusEffect+=augCode.getAmount(Augment.AugmentDamage);
		bonusShots+=augCode.getAmount(Augment.AugmentAddShot);
		mod.ship.getComputer().addBonusCards(augCode.getAmount(Augment.AugmentAddBonusHandSize));

		if(augCode.contains(Augment.AugmentDrawCard))getShip().drawCard(1);

		//Per-side bonuses//
		for(int i=0;i<2;i++){
			//This is generall for weapon augments that affect shooting so non-combat abilities don't get boosted//
			if(augCode.contains(Augment.AugmentTargeted)){
				if(getShots(i)>0){

					code[i].add(Special.Targeted);
					augmented[i]=true;

				}
			}
			else{
				if(getEffect(i)>0){
					augmented[i]=true;
				}
			}
		}



		//Making the card icon of the augmenting card//

	}



	public void rightClick(){
		if(Battle.getPhase()==Phase.End||Battle.getState()!=State.Nothing)return;
		if(selected)return;
		if(Tutorial.stopFlip())return;
		Sounds.cardFlip.play();
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
	public Ship getShip(){
		return mod.ship;
		//TODO - single cards	
	}



	//Big list of getter methods//
	public String getName(){return getName(side);}
	public String getName(int pick){{
		return name[pick];}
	}

	public Pic getImage(){return getImage(side);}
	public Pic getImage(int pick){return cardPic[pick];}

	public int getCost(){return getCost(side);}
	public int getCost(int pick){
		if(getCode(pick).contains(Special.ReduceCost)){
			return baseCost[pick];
		}
		return Math.max(0, baseCost[pick]-mod.getBuffAmount(BuffType.ReduceCost));}

	public int getEffect(){return getEffect(side);}
	public int getEffect(int pick){
		if(baseEffect[pick]==0)return 0;

		int effect= baseEffect[pick]+bonusEffect+mod.getBuffAmount(BuffType.BonusEffeect);
		if(mod.ship!=null&&active)effect+=mod.ship.getBonusEffect(this, pick, effect);

		return effect;
	}

	public int getCoolDown(){return getCoodlown(side);}
	public int getCoodlown(int pick){return baseCooldown[pick];}

	public String getRules(){return getRules(side);}
	public String getRules(int pick){return rules[pick];}

	public CardCode getCode(){return getCode(side);}
	public CardCode getCode(int pick){return code[pick];}

	public int getShots(){return getShots(side);}
	public int getShots(int pick){

		if(type==ModuleType.WEAPON){
			//TODO - single card
			int effect=mod.getShots(pick*specialSide)+bonusShots;
			if(active)effect+=mod.ship.getBonusShots(this, pick, effect);
			return effect;
		}
		return 0;
	}

	public boolean hasSpecial(Special test){return code[side].contains(test);}
	public boolean hasSpecial(Special test, int pick){return code[pick].contains(test);}

	public String toString(){
		return getName();
	}

	@SuppressWarnings("incomplete-switch")
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


		if(code.contains(AI.OverrideIfOtherSideIgnore)){
			if(getCode(1-side).contains(AI.Ignore)){
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
		if(code.contains(Special.MustBeMajorDamaged)&&component.currentThreshold==0){
			no("must be damaged");
			return false;
		}
		if(code.contains(Special.MustBeUndamaged)&&component.getDamage()!=0){
			no("must be pristine");
			return false;
		}

		//Checking stuff if augment//
		if(code.contains(Special.Augment)){
			if(!validAugmentPlay()||augmented[0]||augmented[1]){
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
				if(component.getDamageUntilMajor()<=aiclass.number){
					no(aiclass, "damage until: "+component.getDamageUntilMajor());
					return false;
				}
				ok(aiclass, "damage until: "+component.getDamageUntilMajor());
				break;
			case DamagedModules:
				int damagedModules=0;
				for(Component c:ship.getRandomisedModules()){
					if(c.currentThreshold>0)damagedModules++;
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
				for(Component c: ship.getEnemy().getRandomisedModules()){
					if(c.getDamage()==0)pristines++;
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
				for(Component c:ship.getEnemy().getRandomisedModules()){
					if(c.currentThreshold>0)majorDamagedEnemySystems++;
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
				for(Component c:ship.getRandomisedModules()){
					if(c.getShieldsRequiredToAvoidMajor()<=getEffect()&&side==0){
						ok(aiclass, "Can stop major on"+c+" even though it would be inefficient");
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
				for(Component c:ship.getRandomisedModules()){
					if(c.currentThreshold>0){
						totalShieldable+=c.getShieldableIncoming();
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
				for(Component c:ship.getRandomisedModules()){
					incomingAllShielded+=Math.min(c.getShieldableIncoming(), getEffect());
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
				for(Component c:ship.getRandomisedModules()){
					finc+=c.getShieldableIncoming();
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
				int thisInc=component.getTotalIncoming();
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
				for(Component c:ship.getRandomisedModules()){
					totalShielded+=Math.min(c.getShieldableIncoming(), getEffect());
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
				for(Component c:ship.getRandomisedModules()){
					if(c.getShieldableIncoming()>=aiclass.number){
						ok(aiclass, c+"");
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
		return true;
	}

	public void ok(AIclass aiclass, String addition){

		//System.out.println("ok "+aiclass.ai+" "+addition+(aiclass.number==-1?"":(" ("+aiclass.number+")")));
	}
	public void ok(String addition){

		//System.out.println("ok "+addition);
	}
	public void no(AIclass aiclass, String addition){

		//System.out.println("NO!! "+aiclass.ai+" "+addition+(aiclass.number==-1?"":(" ("+aiclass.number+")")));
	}
	public void no(String addition){

		//System.out.println("NO!! "+" "+addition);
	}
	public boolean sameAs(Card c){
		return c.getName(1).equals(getName(1));
	}
	public void zSetEffect(int eff){
		baseEffect[0]=eff;
		baseEffect[1]=eff;
	}

	public boolean isAugmented(int checkSide) {
		if(selected&&side!=checkSide)return false;
		if(code[checkSide].contains(Special.ReduceCost))return false;
		if(code[checkSide].contains(Special.IncreaseEffect))return false;
		if(code[checkSide].contains(Special.BonusShots))return false;
		if(bonusEffect>0&&getEffect(checkSide)>0)return true;
		return augmented[checkSide]||(component!=null&&component.isAugmented());
	}

	public void resetGraphic() {
		cg.stopFading();
		cg.alpha=0;
		cg=null;
	}


}
