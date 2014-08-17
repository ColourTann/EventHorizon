package eh.util.assets;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.util.Colours;
import eh.util.Draw;
import eh.util.maths.Pair;

public class CutPic {

	private boolean[][] array;
	private Color cutColor;
	private ArrayList<Shard> shards= new ArrayList<Shard>();
	private Texture cutTexture;


	public CutPic(Pic pic, Color color){

		Texture t=pic.get();
		t.getTextureData().prepare();
		Pixmap pixMap=t.getTextureData().consumePixmap();

		cutColor=color;

		cutTexture=new Texture(pixMap);
		//pixMap.dispose();
	}

	public Texture get(){
		return cutTexture;
	}
	
	public void addShatter(){

		if(!cutTexture.getTextureData().isPrepared())cutTexture.getTextureData().prepare();
		Pixmap pixmap=cutTexture.getTextureData().consumePixmap();

		int width=cutTexture.getWidth();
		int height=cutTexture.getHeight();
		int cuts=7;

		analyseShards();


		int x=0;
		int y=0;

		if(shards.size()==1){
			x=width/3;
			y=height/3;
		}
		else{
			Shard shard=getBiggestShard();

			x=(shard.left+shard.right)/2;
			y=(shard.bottom+shard.top)/2;

			if(new Color(pixmap.getPixel(x, y)).a==0){
				x=(shard.left+shard.right)/3*2;
				y=(shard.bottom+shard.top)/3*2;
				if(new Color(pixmap.getPixel(x, y)).a==0){

					x=(shard.left+shard.right)/3;
					y=(shard.bottom+shard.top)/3;
				}
			}

		}
		Pair vector=Pair.randomUnitVector();
		for(int j=0;j<cuts;j++){
			cut(pixmap, x, y, vector, cutColor);
			vector=vector.rotate(Math.PI*2/cuts+(Math.random()-.5)*2);
		}

		Texture result=new Texture(pixmap);
		cutTexture=result;
		analyseShards();

	}

	public Shard removeCut(){
		


		array=null;



		
		Pixmap pixmap=cutTexture.getTextureData().consumePixmap();

		Color replacer=new Color(0,0,0,0);
		Pixmap.setBlending(Blending.None);
		Shard biggest=getMediumestShard();
		if(biggest==null)return null;
		shards.remove(biggest);
		int x=(int) biggest.aPixelLocation.x;
		int y=(int) biggest.aPixelLocation.y;
		Pixmap underneath=new Pixmap(cutTexture.getWidth(),cutTexture.getHeight(), Format.RGBA8888);
		underneath.setColor(0, 0, 0, 0);
		underneath.fillRectangle(0, 0, cutTexture.getWidth(), cutTexture.getHeight());
		fillShard(pixmap, x, y, replacer, null, underneath);




		Texture newCut=new Texture(pixmap);
		//pixMap.dispose();
		cutTexture=newCut;
		Pixmap aligned=new Pixmap(biggest.right-biggest.left, biggest.bottom-biggest.top, Format.RGBA8888);
		aligned.drawPixmap(underneath, 0, 0, biggest.left, biggest.top, biggest.right-biggest.left, biggest.bottom-biggest.top);

		Texture result=new Texture(aligned);
		biggest.texture=result;
		underneath.dispose();
		aligned.dispose();

		return biggest;

	}

	private void cut(Pixmap result, int startX, int startY, Pair startVector, Color col){
		Pair vector= startVector.copy();
		ArrayList<String> strings=new ArrayList<String>();
		double rotation=Math.random()*Math.random()*.005;
		float x=startX;
		float y=startY;
		strings.add((int)x+":"+(int)y);
		result.setColor(col);
		while(true){
			if(Math.abs(x-startX)+Math.abs(y-startY)<5){}//is ok!
			else if(strings.contains((int)x+":"+(int)y)){}//is ok!
			else if(new Color(result.getPixel((int)x, (int)y)).a==0){
				break;
			}
			else if(Colours.equals(col, new Color(result.getPixel((int)x, (int)y)))){
				if(Math.random()>.3)break;
			}
			strings.add((int)x+":"+(int)y);
			result.drawPixel((int)x, (int)y);
			x+=vector.x;
			y+=vector.y;

			vector=vector.add(Pair.randomUnitVector().multiply(0.05f));
			vector=vector.normalise();
			vector=vector.rotate(rotation);
			if(Math.abs(startX-x)+Math.abs(startY-y)>20&&Math.random()>.995){
				cut(result, (int)x, (int)y, Pair.randomUnitVector(), col);
				startX=(int)x;
				startY=(int)y;
			}
		}

	}

	private void analyseShards(){
		Pixmap pixmap=cutTexture.getTextureData().consumePixmap();
		int width=cutTexture.getWidth();
		int height=cutTexture.getHeight();
		array=new boolean[width][height];
		ArrayList<Shard> tempShards=new ArrayList<Shard>();
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				if(array[x][y])continue;
				Color c=new Color(pixmap.getPixel(x, y));
				if(!badColour(c, null)){
					Shard shard=new Shard();
					shard.left=x;
					shard.top=y;
					shard.aPixelLocation=new Pair(x,y);

					fillShard(pixmap, x, y, null, shard, null);
					tempShards.add(shard);
				}
			}
		}

		shards.clear();
		while(tempShards.size()>0){
			Shard s=tempShards.remove(0);
			boolean added=false;
			for(int i=0;i<shards.size();i++){
				if(shards.get(i).size>s.size){
					shards.add(i,s);
					added=true;
					break;
				}
			}
			if(!added)shards.add(s);
		}
 
	}

	private Shard getBiggestShard(){
		if(shards.size()==0){
			return null;
		}
		return shards.get(shards.size()-1);
	}

	private Shard getMediumestShard(){
		if(shards.size()==0){
			return null;
		}

		return shards.get(shards.size()/2);

	}

	private void fillShard(Pixmap pixmap, int startX, int startY, Color replacer, Shard store, Pixmap underneath){

		Color myCol=new Color(pixmap.getPixel(startX, startY));
		if(badColour(myCol, replacer))return;


		if(array!=null&&array[startX][startY])return;

		int width=cutTexture.getWidth();


		int leftX=0;
		int rightX=0;


		for(int x=startX;x>0;x--){
			Color col=new Color(pixmap.getPixel(x, startY));
			if(badColour(col, replacer)){
				leftX=x+1;
				break;
			}	
		}
		for(int x=startX;x<width;x++){
			Color col=new Color(pixmap.getPixel(x, startY));
			if(badColour(col, replacer)){
				rightX=x-1;
				break;
			}	
		}
		if(underneath!=null){
			underneath.drawPixmap(pixmap, leftX, startY, leftX, startY, rightX-leftX, 1);
		}
		if(replacer!=null){
			pixmap.setColor(replacer);
			pixmap.drawLine(leftX, startY, rightX, startY);	
		}
		if(store!=null){
			store.size+=(rightX-leftX);
			store.left=Math.min(store.left, leftX);
			store.right=Math.max(store.right, rightX);
			store.bottom=Math.max(store.bottom, startY);
			store.top=Math.min(store.top, startY);
			for(int x=leftX;x<=rightX;x++){
				array[x][startY]=true;
			}
		}



		for(int x=leftX;x<=rightX;x++){
			fillShard(pixmap, x, startY+1, replacer, store, underneath);
			fillShard(pixmap, x, startY-1, replacer, store, underneath);
		}

		return;
	}

	private boolean badColour(Color c, Color replacer){
		
		if(replacer!=null&&Colours.equals(c, replacer)){
		
			return true;
		}
		return c.a==0||Colours.equals(c, cutColor);
	}

	public class Shard{
		public int size;
		public int left;
		public int right;
		public int bottom;
		public int top;
		public Pair aPixelLocation;
		public Texture texture;
		Pair vector;
		float dr;
		float rotation;

		Pair position;


		public void finalise(){
			dr=(float) (Math.random()-.5*10);
			vector=Pair.randomUnitVector().multiply((float) (300+Math.random()*700));

			position=new Pair(left+((right-left)/2), top+((bottom-top)/2));
		}
		public void update(float delta){
			vector=vector.multiply((float) Math.pow(.3, delta));
			dr=(float) (dr*Math.pow(.3, delta));
			position=position.add(vector.multiply(delta));
			rotation+=dr*delta;
		}
		public String toString(){
			return "Shard size: "+size+", x:"+left+"-"+right+", y:"+top+"-"+bottom;
		}
		public void render(SpriteBatch batch) {
			if(size<5)return;
			Draw.drawTextureRotatedCentered(batch, texture, position.x, position.y, rotation);
		}
	}

}
