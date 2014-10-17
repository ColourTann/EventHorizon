package util.image;
import java.util.HashMap;

import util.Colours;
import util.maths.Pair;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;



public class Pic {

	String path;
	static Color checked=new Color(.5f,.1f,.99f,1);
	private Texture t;
	private Texture putline;
	private Texture glowOutline;
	private Texture monoChrome;
	private Texture mask;
	private Pic flipped;
	private int scale=1;
	private Pic basePic;
	private Color[] replacers;
	private HashMap<String, Pic> scaled= new HashMap<String, Pic>();
	
	public Pic(String path){
		this.path=path+".png";
	}

	public Pic(Pic basePic, Color... replacers){
		this.basePic=basePic;
		this.replacers=replacers;
	}
	
	public Pic (String path, int scale){
		this.path=path+".png";
		this.scale=scale;
	}

	public Pic(Texture t){
		this.t=t;
	}
	
	public Pic getScaled(int scale){
		if(scale==1)return this;
		Pic p= scaled.get(scale+":"+scale);
		if (p==null){
			p = new Pic(upscale(get(), scale));
			scaled.put(scale+":"+scale, p);
			return scaled.get(scale+":"+scale);
		}
		return p;
	}
	
	public Texture get(){
		if(t==null){
			if(path!=null){
				t=new Texture(Gdx.files.internal(path));
			}
			if(basePic!=null){
				t=basePic.paletteSwap(replacers);
			}
			if(scale!=1){
				t=upscale(t,scale);
			}
		}
		return t;
	}
	
	public Pic getFlipped(){
		if(flipped==null){
			Pixmap flippedMap=new Pixmap(getWidth(), getHeight(), Format.RGBA8888);
			Pixmap base=getPixMap();
			for(int x=0;x<getWidth();x++){
				for(int y=0;y<getHeight();y++){
					flippedMap.drawPixmap(base, x, y, getWidth()-x-1, getHeight()-y-1, 1, 1);
				}
			}
			flipped=new Pic(new Texture(flippedMap));
		}
		return flipped;
	}
	
	public PicCut getCut(){
		return new PicCut(this, Colours.black);
	}
	
	public int getWidth(){
		return get().getWidth();
	}
	
	public int getHeight(){
		return get().getHeight();
	}
	
	private Texture upscale(Texture t, int scale){
		Pixmap.setFilter(Filter.NearestNeighbour);
		Pixmap source=getPixMap();
		Pixmap scaler=new Pixmap(t.getWidth()*scale, t.getHeight()*scale, Format.RGBA8888);
		scaler.drawPixmap(source, 0, 0, source.getWidth(), source.getHeight(), 0, 0, scaler.getWidth(), scaler.getHeight());
		Texture result=new Texture(scaler);;
		return result;
	}

	private Texture paletteSwap(Color[] replacers) {
		get();
		int width=t.getWidth();
		int height=t.getHeight();
		
		Pixmap base=getPixMap();
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
	
		Pixmap base=getPixMap();
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
		
		Pixmap base=getPixMap();
		Pixmap result=new Pixmap(width, height, Format.RGBA8888);
		for(int x=0;x<width;x++){			
			for(int y=0;y<height;y++){
				outlinePath(base, result, x, y, 0);
			}
		}
		putline= new Texture( result );
		//base.dispose();
		//result.dispose();
		return putline;
	}

	public Texture getGlow(){

		if(glowOutline!=null)return glowOutline;
		Texture temp=getOutline();
		int width=temp.getWidth();
		int height=temp.getHeight();

		Pixmap base=Pic.getPixMap(temp);
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
		
		Pixmap base=getPixMap();
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

	public Pic rescale(int scale){
		return new Pic(upscale(get(), scale));
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
			
				outlinePath(pixmap, result, x+dx, y, iteration+1);
		
		}
		for(int dy=-1;dy<=1;dy++){
			outlinePath(pixmap, result, x, y+dy, iteration+1);
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
	
	public Pixmap getPixMap(){
		get();
		if(!t.getTextureData().isPrepared()){
			t.getTextureData().prepare();
		}
		return t.getTextureData().consumePixmap();
	}
	
	public static Pixmap getPixMap(Texture t){
		if(!t.getTextureData().isPrepared()){
			t.getTextureData().prepare();
		}
		return t.getTextureData().consumePixmap();
	}

	public void dispose() {
		if(t!=null){
			t.dispose();
		}
	}


	
	

}
