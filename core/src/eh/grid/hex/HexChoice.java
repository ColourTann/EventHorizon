package eh.grid.hex;

import eh.ship.mapThings.mapAbility.MapAbility;

public class HexChoice {
	public Hex hex;
	public float value;
	public MapAbility source;
	boolean bad;
	
	public HexChoice(){
		bad=true;
	}
	public HexChoice(Hex hex, float value){
		this.hex=hex;
		this.value=value;
	}
	public HexChoice(Hex hex, float value, MapAbility source){
		HexChoice result= new HexChoice(hex, value);
		result.source=source;
	}
	
	public boolean isBetterThan(HexChoice hc){
		if(bad)return false;
		if(hc.bad)return true;
		return value>hc.value;
	}
}
