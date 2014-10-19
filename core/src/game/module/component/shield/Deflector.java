package game.module.component.shield;


import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.junk.buff.Buff;
import game.module.junk.buff.Buff.BuffType;

public class Deflector extends Shield{
	public Deflector(int tier){
		super("Deflector",Gallery.deflector, 6, new int[]{5,9,13}, tier);
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.deflectorCard[i];
		}
		name[0]="Deflect";
		cost[0]=0;
		effect[0]=calc(0);
		rules[0]="";
		code[0].add(Special.AddShieldPoints);
		code[0].add(AI.RegularShield);
		
		name[1]="Amplify";
		cost[1]=1;
		effect[1]=0;
		rules[1]="Self boost 1: +"+calc(0)+" strngth to all shield cards";
		code[1].add(Special.BoostSelf);
		code[1].setBuff(new Buff(BuffType.BonusEffeect, true, calc(0), 1));
		code[1].add(AI.OtherCardsThisSystem,2);
		code[1].add(AI.TotalIncoming,calc(0)*4);
		code[1].setPriority(2);
		
		name[2]="Shell";
		cost[2]=3;
		effect[2]=calc(0);
		rules[2]="Shields all modules";
		code[2].add(Special.ShieldAll);
		code[2].add(AI.ShieldAll,calc(0)*4);
		code[2].setPriority(1);
		
		name[3]="Energise";
		cost[3]=0;
		effect[3]=calc(0);
		rules[3]="Shields computer. \n Absorb: draw a card";
		code[3].add(Special.ShieldComputer);
		code[3].add(Special.Absorb);
		code[3].add(Special.AbsorbDraw,1);
		code[3].add(AI.IncomingComputer,1);
		
		name[4]="Flux Field";
		cost[4]=1;
		effect[4]=calc(2);
		rules[4]="Shields only modules with major damage";
		code[4].add(Special.AddShieldPoints);
		code[4].add(Special.ShieldOnlyDamaged);
		code[4].add(AI.IncomingOnMajorDamaged,calc(2));
		code[4].add(AI.RegularShield);
	}
}
