package game.card;

import java.util.ArrayList;

public class CardCode {
	public enum Special{
		//General Specials//
		DrawCard, IncreaseEffect, DiscardWhenPlayed, Augment, ReduceCost, GainEnergy, EnergyIfEmpty, MustBeMajorDamaged,
		ModuleChooser, PermanentIncreaseEffect, EnergyIncome, SelfScramble, DiscardWhenChosen, BonusVsGenerator, BonusVsComputer,
		GetCardFromChosenModule, DiscardOthers, ChooseWeapon, ChooseEnemyModule,  
		ScrambleChosenModule, BonusEffectToShield,  StealEnergy,
		
		//Tutorial junk//
		BonusVsModule0, BonusVsModule1,

		//Weapon Specials//
		Targeted, Unshieldable, MakeVulnerable, BonusVsWeapon, BonusVsPristine, BonusVsMajorDamaged, BonusShots, BonusEffectPerOtherWeapon,

		//Shield Specials//
		AddShieldPoints, ShieldAll, ShieldComputer, AbsorbDraw, ShieldOnlyDamaged, selfDamage, AbsorbEnergy, MustBeUndamaged,
		ShieldWeapons, ShieldGenerator, ShieldChosenModule, ShieldShield, DestroyEnemyShield, ImmuneChosenModule, ResetCycle, 
		BonusPerMajorDamage,
	};

	public enum AI{
		//General AI
		Ignore, CheckOriginalFirst, LowEnergy, SurplusEnergy, OtherCardsThisSystem, LowChance, EvenChance, HighChance, ReduceCost, 
		DamageSelf, OtherUntargeted, Singleton, DamagedModules, OverrideIfOtherSideIgnore, WeaponCards,

		//Weapon AI
		PlayerPristineSystems, BetterAgainstSpecificSystem, MajorDamagedEnemySystems,

		//Shield AI
		RegularShield, OtherTargeted, IncomingComputer, IncomingOnMajorDamaged, IncomingAll, TotalIncoming, IncomingGenerator,
		TotalIncomingThis, IncomingWeapons, ShieldAll, SingleModuleIncoming, 
	};

	public enum Augment{
		//Augment targets//
		AugmentWeapon, AugmentThis, AugmentAll,

		//Augment Effects//
		AugmentTargeted, AugmentDrawCard, AugmentDamage, AugmentAddShot, AugmentDiscard, AugmentAny, AugmentGainEnergy, AugmentAddBonusHandSize    
	};
	private ArrayList<CardSpecial> specials= new ArrayList<CardCode.CardSpecial>();
	private ArrayList<AIclass> ais = new ArrayList<AIclass>();
	private ArrayList<AugmentEffect> augs= new ArrayList<AugmentEffect>();


	private class CardSpecial{
		public Special special;
		public int number;
		public CardSpecial(Special special, int number){
			this.special=special;
			this.number=number;
		}
	}
	private class AugmentEffect{
		public Augment aug;
		public int number;
		public AugmentEffect(Augment aug, int number){
			this.aug=aug;
			this.number=number;
		}
	}	
	public class AIclass{
		public AI ai;
		public int number;
		public AIclass(AI ai, int number){
			this.ai=ai;
			this.number=number;
		}
	}

	public void add(Augment aug, int number){augs.add(new AugmentEffect(aug, number));}
	public void add(Augment aug){add(aug, -1);}
	public void add(Special special, int number){specials.add(new CardSpecial(special, number));}
	public void add(Special special){add(special,-1);}
	public void add(AI ai, int number){ais.add(new AIclass(ai, number));}
	public void add(AI ai){add(ai,-1);}

	public ArrayList<AIclass> getAIs(){
		return ais;
	}

	public Special[] getSpecials(){
		Special[] result = new Special[specials.size()-1];
		for(int i=0;i<result.length;i++){result[i]=specials.get(i).special;}
		return result;
	}

	public int getAmount(Special s){
		int total=0;
		for(CardSpecial c: specials)if(c.special==s)total+=c.number;
		return total;
	}

	public boolean contains(Special s){
		for(CardSpecial cs:specials)if(cs.special==s)return true;
		return false;
	}

	public boolean contains(AI a){
		for(AIclass t:ais)if(t.ai==a)return true;
		return false;
	}

	public int getAmount(Augment aug){
		int total =0;
		for(AugmentEffect eff:augs){
			if(eff.aug==aug)total+=eff.number;
		}
		return total;
	}

	public boolean contains(Augment aug){
		for(AugmentEffect eff:augs)if(eff.aug==aug)return true;
		return false;
	}

	//Cloning method for making new cards from the module stats//
	public CardCode copy(){
		CardCode result= new CardCode();

		for(CardSpecial cs:specials)result.add(cs.special, cs.number);

		for(AIclass a:ais)result.add(a.ai,a.number);

		for(AugmentEffect e:augs)result.add(e.aug,e.number);

		result.setPriority(getPriority());
		return result;
	}
	
	int priority=0;
	/*Enemy AI priority 
	 * 2 is play super-first like cost-reduction, draw cards, gain energy
	 * 1 is play first like buffs 
	 * 0 is normal
	 * -1 is play last
	 */
	public int getPriority(){
		return priority;
	}
	public void setPriority(int pri){
		priority=pri;
	}
	public void clear(){
		ais.clear();
		specials.clear();
		augs.clear();
	}

}
