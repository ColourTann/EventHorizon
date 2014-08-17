package eh.util.assets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import eh.util.Colours;
import eh.util.Draw;
import eh.util.PerleyBabes;
import eh.util.maths.Pair;

public class Pic {

	String path;
	static Color checked=new Color(.5f,.1f,.99f,1);
	private Texture t;
	private Texture putline;
	private Texture glowOutline;
	private Texture monoChrome;
	private Texture mask;
	private Texture cut;
	private Color cutColor;
	private Pic basePic;
	private Color[] replacers;

	private boolean[][] array;

	private ArrayList<Shard> shards= new ArrayList<Shard>();
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

	public Pic(String path){
		this.path=path+".png";
	}

	public Pic(Pic basePic, Color... replacers){
		this.basePic=basePic;
		this.replacers=replacers;
	}

	public Texture get(){
		if(t==null){
			if(path!=null){
				t=new Texture(Gdx.files.internal(path));
			}
			if(basePic!=null){
				t=basePic.paletteSwap(replacers);
			}
		}


		return t;
	}

	private Texture paletteSwap(Color[] replacers) {
		get();
		int width=t.getWidth();
		int height=t.getHeight();
		t.getTextureData().prepare();
		Pixmap base=t.getTextureData().consumePixmap();
		Pixmap pixMap=new Pixmap(width, height, Format.RGBA8888);

		HashMap<Color, Color> map = new HashMap<Color, Color>();
		for(int i=0;i<replacers.length;i+=2)map.put(replacers[i], replacers[i+1]);

		for(int x=0;x<width;x++){			
			for(int y=0;y<height;y++){
				Color replace=map.get(new Color(base.getPixel(x, y)));

				if(replace!=null)	pixMap.setColor(replace);
				else				pixMap.setColor(base.getPixel(x, y));
				pixMap.drawPixel(x, y);
			}
		}
		Texture result=new Texture(pixMap);
		base.dispose();

		return result;
	}

	public Texture getMask(Color color){
		if(mask!=null)return mask;
		get();
		int width=t.getWidth();
		int height=t.getHeight();
		t.getTextureData().prepare();
		Pixmap base=t.getTextureData().consumePixmap();
		Pixmap pixMap=new Pixmap(width, height, Format.RGBA8888);



		pixMap.setColor(color);
		for(int x=0;x<width;x++){			
			for(int y=0;y<height;y++){
				Color c=new Color(base.getPixel(x, y));

				if(c.a!=0){
					pixMap.drawPixel(x, y);
				}
			}
		}
		Texture result=new Texture(pixMap);
		base.dispose();
		mask=result;

		return mask;
	}

	public Texture getOutline(){
		if(putline!=null)return putline;
		get();
		int width=t.getWidth();
		int height=t.getHeight();
		t.getTextureData().prepare();
		Pixmap base=t.getTextureData().consumePixmap();
		Pixmap result=new Pixmap(width, height, Format.RGBA8888);
		for(int x=0;x<width;x++){			
			for(int y=0;y<height;y++){
				outlinePath(base, result, x, y, 0);
			}
		}
		putline= new Texture( result );
		base.dispose();
		//result.dispose();
		return putline;
	}

	public Texture getGlow(){

		if(glowOutline!=null)return glowOutline;
		Texture temp=getOutline();
		int width=temp.getWidth();
		int height=temp.getHeight();

		Pixmap base=temp.getTextureData().consumePixmap();
		Pixmap result=new Pixmap(width, height, Format.RGBA8888);
		for(int x=0;x<width;x++){			
			for(int y=0;y<height;y++){
				glow(base, result, x, y, 0);
			}
		}
		glowOutline= new Texture(result);
		//base.dispose();
		//result.dispose();
		return glowOutline;
	}

	public Texture getMonochrome(){
		get();
		if(monoChrome!=null)return monoChrome;
		int width=t.getWidth();
		int height=t.getHeight();
		t.getTextureData().prepare();
		Pixmap base=t.getTextureData().consumePixmap();
		Pixmap result=new Pixmap(width, height, Format.RGBA8888);
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				Color c = new Color(base.getPixel(x, y));
				c=Colours.monochrome(c);
				result.setColor(c);
				result.drawPixel(x, y);
			}
		}
		monoChrome= new Texture( result );
		//base.dispose();
		//result.dispose();
		return monoChrome;
	}

	public Texture getCut(Color col){
		if(cut!=null)return cut;
		this.cutColor=col;
		get();

		t.getTextureData().prepare();
		Pixmap pixMap=t.getTextureData().consumePixmap();







		Texture result=new Texture(pixMap);

		//pixMap.dispose();
		cut=result;



		return cut;
	}

	public void addShatter(){
		getCut(cutColor);

		Pixmap pixmap=cut.getTextureData().consumePixmap();

		int width=t.getWidth();
		int height=t.getHeight();
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
		cut=result;
		analyseShards();

	}

	public Shard removeCut(){
		long time=System.currentTimeMillis();
		
		
		
		array=null;

	
		
		time=System.currentTimeMillis();
		Pixmap pixmap=cut.getTextureData().consumePixmap();
	
		Color replacer=new Color(0,0,0,0);
		Pixmap.setBlending(Blending.None);
		Shard biggest=getMediumestShard();
		if(biggest==null)return null;
		shards.remove(biggest);
		int x=(int) biggest.aPixelLocation.x;
		int y=(int) biggest.aPixelLocation.y;
		Pixmap underneath=new Pixmap(t.getWidth(),t.getHeight(), Format.RGBA8888);
		underneath.setColor(0, 0, 0, 0);
		underneath.fillRectangle(0, 0, t.getWidth(), t.getHeight());
		fillShard(pixmap, x, y, replacer, null, underneath);

		
	

		Texture newCut=new Texture(pixmap);
		//pixMap.dispose();
		cut=newCut;
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

	public void analyseShards(){
		Pixmap pixmap=cut.getTextureData().consumePixmap();
		int width=t.getWidth();
		int height=t.getHeight();
		array=new boolean[width][height];
		ArrayList<Shard> tempShards=new ArrayList<Pic.Shard>();
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
		for(Shard s:shards)System.out.println(s.size);
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
		Color boundaryColor=cutColor;
		Color myCol=new Color(pixmap.getPixel(startX, startY));
		if(badColour(myCol, replacer))return;


		if(array!=null&&array[startX][startY])return;

		int width=t.getWidth();


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
		if(replacer!=null&&Colours.equals(c, replacer))return true;
		return c.a==0||Colours.equals(c, cutColor);
	}

	private void outlinePath(Pixmap pixmap, Pixmap result, int x, int y, int iteration){
		if(x<0||y<0||x>pixmap.getWidth()||y>pixmap.getHeight())return;
		int col=pixmap.getPixel(x, y);
		if(getAlpha(col)!=0){
			if(iteration>0||x==0||y==0||x==pixmap.getWidth()-1||y==pixmap.getHeight()-1){
				result.setColor(Colours.white);
				result.drawPixel(x, y);
			}
			return;
		}
		if(iteration>0)return;
		for(int dx=-1;dx<=1;dx++){	
			for(int dy=-1;dy<=1;dy++){
				outlinePath(pixmap, result, x+dx, y+dy, iteration+1);
			}
		}
	}

	private void glow(Pixmap pixmap, Pixmap result, int x, int y, int iteration){
		Pixmap.setBlending(Blending.SourceOver);
		if(x<0||y<0||x>pixmap.getWidth()||y>pixmap.getHeight())return;
		int col=pixmap.getPixel(x, y);
		if(getAlpha(col)==0)return;


		float dist=2;
		new Pair(dist,dist).getDistance();
		for(float dx=-dist;dx<=dist;dx++){	
			for(float dy=-dist;dy<=dist;dy++){
				Color setter=Colours.withAlpha(Colours.white,.25f);
				col=result.getPixel((int)(x+dx), (int)(y+dy));

				//if(getAlpha(col)<=getAlpha(Color.rgba8888(setter))){

				result.setColor(setter);
				result.drawPixel((int)(x+dx), (int)(y+dy));
				//}


			}
		}


	}



	static float getAlpha(int col){
		return (col& 0x000000ff)/ 255f;
	}

	public void reset() {
		cut=null;
	}

}
