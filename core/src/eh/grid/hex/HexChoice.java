package eh.grid.hex;

public class HexChoice {
	public Hex hex;
	public float value;
	boolean bad;
	public HexChoice(){
		bad=true;
	}
	public HexChoice(Hex hex, float value){
		this.hex=hex;
		this.value=value;
	}
	public boolean isBetterThan(HexChoice hc){
		if(bad)return false;
		if(hc.bad)return true;
		return value>hc.value;
	}
}
