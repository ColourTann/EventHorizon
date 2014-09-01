package util.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class SpriteSheet {


	
	public Texture[][] textures;
	public Pic[][] pics;
	String path;
	int across;
	int down;
	int scale;
	public SpriteSheet(String path, int across, int down, int scale){
		textures=new Texture[across][down];
		this.path=path;
		this.across=across;
		this.down=down;
		this.scale=scale;
	}
	public Texture[][] get(){
		Pixmap.setFilter(Filter.NearestNeighbour);
		
		if(textures[0][0]==null){
			Texture t=new Texture(Gdx.files.internal(path));
			
			int width=t.getWidth()/across;
			int height=t.getHeight()/down;
			for(int x=0;x<t.getWidth();x+=width){
				for(int y=0;y<t.getHeight();y+=height){
					
					Pixmap map = new Pixmap(width*scale, height*scale, Format.RGBA8888);
					map.drawPixmap(Pic.getPixMap(t), x, y, width, height, 0, 0, width*scale, height*scale);
					textures[x/width][y/height]=new Texture(map);
					//map.dispose();

				}
			}
		}

		return textures;
	}
	public Pic[][] getAsPics(){
		
		if(pics==null){
			get();
			pics= new Pic[textures.length][textures[0].length];
			for(int x=0;x<textures.length;x++){
				
				for(int y=0;y<textures[0].length;y++){
					pics[x][y]=new Pic(textures[x][y]);	
				}
			}

		}
		return pics;
	}
}
