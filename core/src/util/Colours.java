package util;



import com.badlogic.gdx.graphics.Color;

public class Colours {
	// Event Horizon Colour palette
	public static final Color white=new Color(1,1,1,1);
	public static final Color black=new Color(0,0,0,1);
	public static final Color faded = new Color(.4f, .4f, .4f, 1);
	
	
	public static final Color dark=Colours.make(7, 0, 22);
	public static final Color light=Colours.make(236,250,212);
	public static Color grey=Colours.make(92,107,110);
	
	public static final Color[] baseReplacers =new Color[]{make(0,0,0), make(127,127,127), make(255,255,255)};
	public static final Color[] greenHPCols =new Color[]{make(47,186,131), make(86,224,147), make(165,230,161)};
	public static final Color[] blueHPCols =new Color[]{make(36,113,203), make(31,162,206), make(107,202,230)};
	
	public static final Color[] orangeHPCols =new Color[]{make(255,101,36), make(251,154,32), make(255,178,73)};
	public static final Color[] greyHPCols =new Color[]{make(77,62,54), make(150,137,126), make(182,171,154)};
	public static final Color[] redHPCols =new Color[]{make(98,43,79), make(138,43,49), make(191,94,79)};
	public static final Color tutorialHighlightColour = Colours.orangeHPCols[1];
	
	
	public static final Color[] player2 = new Color[]{make(0,128,78),make(43,171,120)};
	public static final Color[] enemy2 = new Color[]{make(144,62,54),make(191,94,79)};
	
	public static final Color[] genCols5= new Color[]{make(98,43,79), make(138, 43, 49), make(191,94,79), make(254,184,114), light};
	public static final Color[] compCols6= new Color[]{make(67,94,107),make(47,137,131),make(47,186,131),make(86,224,147),make(165,230,161),make(196,224,204)};
	public static final Color[] weaponCols8= new Color[]{make(112,19,53),make(157,26,27),make(245,25,37),make(200,37,46),make(238,67,73),make(253,101,38),make(248,154,32),make(255,178,72)};
	public static final Color[] shieldCols6 = new Color[]{make(131,226,226),make(79,211,213),make(107,202,230),make(31,162,206),make(37,114,200),make(44,68,157)};
	
	public static final Color[] redWeaponCols4 = new Color[]{make(157,26,27),make(245,25,37),make(253,101,38), make(248,154,32)};
	public static final Color[] blueWeaponCols4 = new Color[]{make(47,137,131),make(86,224,147),make(165,230,161), make(196,224,204)};
	
	public static final Color[] shipHull7=new Color[]{make(236,250,212), make(196,207,173), make(153,146,115), make(109,156,164), make(88,135,99), make(52,94,84), make(61,57,84), make(30,28,49), make(28,45,61)};
	public static final Color transparent=new Color(0,0,0,0);

	public static final Color[] backgrounds1=new Color[]{make(28,44,60)};
	
	public static Color shiftedTowards(Color source, Color target, float amount){
		if(amount>1) amount=1;
		if(amount<0) amount=0;
		float r=source.r+((target.r-source.r)*amount);
		float g=source.g+(target.g-source.g)*amount;
		float b=source.b+(target.b-source.b)*amount;
		return new Color(r, g, b, 1);
	}
	public static Color multiply(Color source, Color target){
		return new Color(source.r*target.r,source.g*target.g,source.b*target.b,1);
	}
	public static Color withAlpha(Color source, float alphaMult){
		return new Color(source.r,source.g,source.b,source.a*alphaMult);
	}
	public static Color randomColor(){
		return new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1);
	}
	public static Color make(int r, int g, int b){
		return new Color((float)(r/255f),(float)(g/255f),(float)(b/255f),1);
	}
	public static Color monochrome(Color c){
		float brightness=(c.r+c.g+c.b)/3;
		return new Color(brightness,brightness,brightness,c.a);
	}
	public static boolean equals(Color a, Color b){
		return a.a==b.a&&a.r==b.r&&a.g==b.g&&a.b==b.b;
	}
	public static boolean wigglyEquals(Color a, Color aa){
		float r=Math.abs(a.r-aa.r);
		float g=Math.abs(a.g-aa.g);
		float b=Math.abs(a.b-aa.b);
		float wiggle=.01f;
		return r<wiggle&&g<wiggle&&b<wiggle;
	}
}