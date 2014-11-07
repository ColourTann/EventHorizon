package game.module.component.generator;

import game.assets.Gallery;
import util.image.Pic;

public class Two extends Generator{

	public Two() {
		super("Spark",null,2, new int[]{4,7,10});
		
		variants=1;
		for(int i=0;i<=variants;i++){
			cardPic[0]=Gallery.cardGenerator[0];
		}
		

	}
}
