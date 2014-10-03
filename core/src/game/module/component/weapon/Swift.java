package game.module.component.weapon;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import util.image.Pic;

public class Swift extends Weapon{

	public Swift(int tier) {
		super("Swift",Gallery.swift, 6,0, new int[]{6,9,14}, tier);
		cardPic=Gallery.swiftCard;
		
		name[0]="Swift";
		cost[0]=3;
		cooldown[0]=0;
		effect[0]=calc(1);
		shots[0]=2;
		rules[0]="";
		
		name[1]="Boost";
		cost[1]=1;
		cooldown[1]=0;
		effect[1]=0;
		shots[1]=0;
		rules[1]="Augment Swift card: "+calc(0)+" damage";
		code[1].add(Special.Augment);
		code[1].add(Augment.AugmentThis);
		code[1].add(Augment.AugmentDamage, calc(0));
		code[1].add(AI.OtherCardsThisSystem,1);
		code[1].setPriority(1);
		
		name[2]="Twist";
		cost[2]=2;
		cooldown[2]=0;
		effect[2]=0;
		shots[2]=0;
		rules[2]="Augment swift card: +1 shot";
		code[2].add(Special.Augment);
		code[2].add(Augment.AugmentThis);
		code[2].add(Augment.AugmentAddShot, 1);
		code[2].add(AI.OtherCardsThisSystem,1);
		code[2].setPriority(1);
		
		name[3]="High-impact";
		cost[3]=4;
		cooldown[3]=0;
		effect[3]=calc(1);
		shots[3]=2;
		rules[3]="+"+calc(0)+" damage against undamaged modules";
		code[3].add(Special.BonusVsPristine, calc(0));
		code[3].add(AI.PlayerPristineSystems,3);
		
		
		name[4]="Rend";
		cost[4]=5;
		cooldown[4]=0;
		effect[4]=calc(1);
		shots[4]=2;
		rules[4]="Modules hit by this take +"+calc(0)+" damage from further shots this turn";
		code[4].add(Special.MakeVulnerable,calc(0));
		code[4].add(AI.SurplusEnergy,3);
		code[4].setPriority(1);

	}

}
