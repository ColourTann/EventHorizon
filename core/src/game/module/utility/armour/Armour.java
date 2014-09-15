package game.module.utility.armour;

import util.image.Pic;
import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.utility.Utility;

public abstract class Armour extends Utility{
	private float multiplier;
	public Armour(float multiplier, int tier, String modName, String passive, Pic modulePic, int variants, int numCards){
		super(tier, modName, passive, modulePic, variants, numCards);
		this.multiplier=multiplier;
		
		name[1]="Cycle";
		cost[1]=0;
		cooldown[1]=0;
		effect[1]=0;
		rules[1]="Draw a card";
		cardPic[1]=Gallery.armour;
		code[1].add(Special.DrawCard, 1);
		code[1].add(Special.DiscardWhenPlayed);
		code[1].add(AI.Ignore);
		
		
	}
	public float getMultuplier(){
		return multiplier;
	}

	
}
