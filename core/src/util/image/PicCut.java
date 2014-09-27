package util.image;

import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.maths.Pair;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public class PicCut {

	private boolean[][] array;
	public Color cutColor;
	public ArrayList<Shard> shards= new ArrayList<Shard>();
	private Texture cutTexture;
	private ArrayList<Pair> shatterPoints=new ArrayList<Pair>();
	int width;
	int height;
	public PicCut(Pic pic, Color color){
		Pixmap pixMap=Pic.getPixMap(pic.get());
		cutColor=color;
		cutTexture=new Texture(pixMap);
		width=cutTexture.getWidth();
		height=cutTexture.getHeight();
	}

	public Texture get(){
		return cutTexture;
	}

	public void prepareShatter(){
		if(!cutTexture.getTextureData().isPrepared())cutTexture.getTextureData().prepare();
		analyseShards();
	}

	public Pair addShatter(){
		Pixmap.setBlending(Blending.None);
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

			int startX=shard.left;
			int startY=shard.top;
			int across=shard.right-shard.left;
			int down=shard.bottom-shard.top;


			x=(startX+across/2);
			y=(startY+down/2);

			//BAD CODE//
			if(!testPoint(x, y, pixmap)){
				x=(startX+across/3);
				y=(startY+down/3);
				if(!testPoint(x, y, pixmap)){
					x=(startX+across/3*2);
					y=(startY+down/3);
					if(!testPoint(x, y, pixmap)){
						x=(startX+across/3);
						y=(startY+down/3*2);
						if(!testPoint(x, y, pixmap)){
							x=(startX+across/3*2);
							y=(startY+down/3*2);
							if(!testPoint(x, y, pixmap)){
								x=(startX+across/5);
								y=(startY+down/5);
								if(!testPoint(x, y, pixmap)){
									x=(startX+across/5*4);
									y=(startY+down/5*4);
									if(!testPoint(x, y, pixmap)){
										x=(startX+across/5*4);
										y=(startY+down/5);
										if(!testPoint(x, y, pixmap)){
											x=(startX+across/5);
											y=(startY+down/5*4);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		shatterPoints.add(new Pair(x,y));
		Pair vector=Pair.randomUnitVector();
		for(int j=0;j<cuts;j++){
			
			cut(pixmap, x, y, vector, cutColor);
			vector=vector.rotate(Math.PI*2/cuts+(Math.random()-.5)*2);
		}
		
		Texture result=new Texture(pixmap);
		cutTexture=result;
		analyseShards();
		
		return new Pair(x,y);

	}

	public boolean testPoint(int x, int y, Pixmap pixmap){
		if(new Color(pixmap.getPixel(x, y)).a==0)return false;
		for(Pair p:shatterPoints){

			if(p.getDistance(new Pair(x,y))<10){

				return false;
			}
		}
		return true;
	}

	public Shard removeCut(){



		array=null;




		Pixmap pixmap=Pic.getPixMap(cutTexture);
		Color replacer=Colours.transparent;
		Pixmap.setBlending(Blending.None);
		Shard biggest=getUpperMediumestShard();
		if(biggest==null)return null;
		shards.remove(biggest);
		int x=(int) biggest.aPixelLocation.x;
		int y=(int) biggest.aPixelLocation.y;
		Pixmap underneath=new Pixmap(cutTexture.getWidth(),cutTexture.getHeight(), Format.RGBA8888);
		underneath.setColor(replacer);
		underneath.fillRectangle(0, 0, cutTexture.getWidth(), cutTexture.getHeight());
		fillShard(pixmap, x, y, replacer, null, underneath);


		

		Texture newCut=new Texture(pixmap);
		cutTexture.dispose();
		//pixMap.dispose();
		cutTexture=newCut;
		Pixmap aligned=new Pixmap(biggest.right-biggest.left, biggest.bottom-biggest.top, Format.RGBA8888);
		aligned.drawPixmap(underneath, 0, 0, biggest.left, biggest.top, biggest.right-biggest.left, biggest.bottom-biggest.top);

		Texture result=new Texture(aligned);
		biggest.texture=result;
		underneath.dispose();
		aligned.dispose();
		biggest.finalise();
		return biggest;

	}

	public Shard replaceSection(Pair location, Texture mask){
		Pixmap.setBlending(Blending.SourceOver);
		if(checkSection(mask, (int)location.x, (int)location.y)){
			Shard s=fillSection(mask, (int)location.x, (int)location.y);
			s.aPixelLocation=new Pair(location.x,location.y);
			shards.add(s);
			cutTexture=new Texture(Pic.getPixMap(cutTexture));
			return s;
		}

		return null;

	}

	private boolean checkSection(Texture mask, int startX, int startY){

		Pixmap maskMap=Pic.getPixMap(mask);
		Pixmap baseMap=Pic.getPixMap(cutTexture);

		for(int x=startX;x<startX+mask.getWidth();x++){
			for(int y=startY;y<startY+mask.getHeight();y++){

				if(new Color(maskMap.getPixel(x-startX, y-startY)).a==0)continue;

				Color baseColor=new Color(baseMap.getPixel(x, y));
				boolean okCol=false;
				for(Color c:Colours.shipHull7){
					if(Colours.equals(c, baseColor)){

						okCol=true;
						break;
					}

				}
				if(!okCol){

					return false;
				}

			}	
		}



		return true;
	}

	private Shard fillSection(Texture mask, int startX, int startY){
		Shard shard=new Shard();
		Pixmap base=Pic.getPixMap(get());
		Pixmap maskMap=Pic.getPixMap(mask);
		Pixmap shardMap=new Pixmap(mask.getWidth(), mask.getHeight(), Format.RGBA8888);

		Pixmap.setBlending(Blending.SourceOver);
		base.setColor(1, 1, 1, 1);
		for(int x=0;x<mask.getWidth();x++){
			for(int y=0;y<mask.getWidth();y++){
				if(new Color(maskMap.getPixel(x, y)).a==0)continue;
				Color col=new Color(base.getPixel(x+startX, y+startY));

				shardMap.setColor(col);
				shardMap.drawPixel(x, y);
			}
		}



		base.drawPixmap(maskMap, startX, startY);
		shard.left=startX;
		shard.right=startX+mask.getWidth();
		shard.top=startY;
		shard.bottom=startY+mask.getHeight();
		shard.finalise();
		shard.texture=new Texture(shardMap);
		shardMap.dispose();
		//	shard.texture=Gallery.shipEclipse.get();
		return shard;
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

	private Shard getUpperMediumestShard(){
		if(shards.size()==0){
			return null;
		}
		
		return shards.get(shards.size()/3*2);

	}

	private void fillShard(Pixmap pixmap, int startX, int startY, Color replacer, Shard store, Pixmap underneath){

		Color myCol=new Color(pixmap.getPixel(startX, startY));
		if(badColour(myCol, replacer))return;


		if(array!=null&&array[startX][startY])return;

		int width=cutTexture.getWidth();


		int leftX=0;
		int rightX=width-1;


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

	public void miniDispose(){
		cutTexture.dispose();
	}
	
	public void dispose() {
		for(Shard s:shards){
			s.dispose();
		}
		cutTexture.dispose();
	}
	
	public class Shard{
		public int size;
		public int left;
		public int right;
		public int bottom;
		public int top;
		public Pair aPixelLocation;
		public Texture texture;
		public Pair vector;
		public float dr;
		float rotation;
		boolean enemy;
		public Pair position;



		public void finalise(){
			position=new Pair(left+((right-left)/2), top+((bottom-top)/2));

		}
		public void update(float delta){
			vector=vector.multiply((float) Math.pow(.95, delta));
			dr=(float) (dr*Math.pow(.9, delta));
			position=position.add(vector.multiply(delta));
			rotation+=dr*delta;

		}
		public String toString(){
			return "Shard size: "+size+", x:"+left+"-"+right+", y:"+top+"-"+bottom;
		}
		public void render(SpriteBatch batch) {
			//if(size<5)return;
			Draw.drawCenteredRotated(batch, texture, position.x, position.y, rotation);
		}
		public void estimatePosition() {
			position=new Pair(left+((right-left)/2f), top+((bottom-top)/2));
		}
		public void dispose() {
			if(texture!=null)texture.dispose();
		}



	}

	

}
