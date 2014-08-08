package eh.assets;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;

import eh.util.Colours;
import eh.util.maths.Pair;

public class Pic {

	String path;
	static Color checked=new Color(.5f,.1f,.99f,1);
	private Texture t;
	private Texture putline;
	private Texture glowOutline;
	private Texture monoChrome;

	public Pic(String path){
		this.path=path+".png";
	}
	public Texture get(){
		if(t==null)t=new Texture(Gdx.files.internal(path));
		return t;
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

}
