package util;

public class Option {
	/*
	 * For using with volume and stuff
	 */
	private float amount=.75f;
	public Option(float start){
		amount=start;
	}
	public void setFloat(float amount){
		amount=Math.max(0, amount);
		amount=Math.min(1, amount);
		this.amount=amount;
	}
	public float getFloat(){
		return amount;
	}
}
