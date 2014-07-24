package eh.util;

import com.badlogic.gdx.graphics.Color;

public class Colours {
	// Event Horizon Colour palette
	public static Color white=new Color(1,1,1,1);
	public static Color black=new Color(0,0,0,1);
	public static Color faded = new Color(.4f, .4f, .4f, 1);
	
	public static Color dark=Colours.make(7, 0, 22);
	public static Color light=Colours.make(236,250,212);
	public static Color grey=Colours.make(92,107,110);
	
	public static Color[] player2 = new Color[]{make(0,128,78),make(43,171,120)};
	public static Color[] enemy2 = new Color[]{make(144,62,54),make(191,94,79)};
	
	public static Color[] genCols5= new Color[]{make(98,43,79), make(138, 43, 49), make(191,94,79), make(254,184,114), light};
	public static Color[] compCols6= new Color[]{make(67,94,107),make(47,137,131),make(47,186,131),make(86,224,147),make(165,230,161),make(196,224,204)};
	public static Color[] weaponCols8= new Color[]{make(112,19,53),make(157,26,27),make(245,25,37),make(200,37,46),make(238,67,73),make(253,101,38),make(248,154,32),make(255,178,72)};
	public static Color[] shieldCols6 = new Color[]{make(131,226,226),make(79,211,213),make(107,202,230),make(31,162,206),make(37,114,200),make(44,68,157)};
	
	public static Color[] redWeaponCols4 = new Color[]{make(157,26,27),make(245,25,37),make(253,101,38), make(248,154,32)};
	public static Color[] blueWeaponCols4 = new Color[]{make(47,137,131),make(86,224,147),make(165,230,161), make(196,224,204)};
	public static Color shiftedTowards(Color source, Color target, float amount){
		if(amount>1) amount=1;
		if(amount<0) amount=0;
		System.out.println(amount);
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
}