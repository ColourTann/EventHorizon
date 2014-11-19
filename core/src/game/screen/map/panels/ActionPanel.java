package game.screen.map.panels;

import util.Colours;
import util.Draw;
import util.TextWriter;
import util.assets.Font;
import util.maths.Pair;
import util.update.SimpleButton.Code;
import game.Main;
import game.assets.AdvancedButton;
import game.grid.hex.Hex;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ActionPanel extends SidePanel{
	static final Pair ButtonCenter=new Pair(Main.width-125, Main.height-60);
	static final Pair TitleCenter=new Pair(Main.width-125, Main.height/2+40);
	static final Pair PicCenter=new Pair(Main.width-125, Main.height/2+80);
	
	
	static final int flavourWrap=200;
	static final Pair flavourCenter=new Pair(Main.width-125, Main.height/2+190);
	AdvancedButton button;
	Hex h;
	TextWriter writer;
	public void init(final Hex h){

		this.h=h;
		if(button!=null){
			button.deactivate();
			button=null;
		}
		if(writer!=null){
			writer.smallDispose();
			writer=null;
		}
		if(h.content==null)return;
		Font.medium.setColor(Colours.light);
		
		
		writer=new TextWriter(Font.medium, h.content.getFlavour());
		writer.setWrapWidth(flavourWrap);
		writer.setMapReplacements();
		writer.setupTexture();
		if(h.content.getActionName()==null)return;
		button=new AdvancedButton(ButtonCenter, true, null, h.content.getActionName(), Font.medium, new Code() {
			
			@Override
			public void onPress() {
				h.content.action();
			}
		});
	}
	
	@Override
	public void update(float delta) {
	}

	@Override
	public void render(SpriteBatch batch) {
		if(h==null||h.content==null)return;
		Draw.drawCenteredScaled(batch, h.content.getPic().get(), PicCenter.x, PicCenter.y, 2, 2);
		Font.medium.setColor(Colours.light);
		Font.drawFontCentered(batch,  h.content.toString(), Font.medium, TitleCenter.x, TitleCenter.y);
		writer.render(batch, (int)(flavourCenter.x-writer.maxWidth/2), (int)(flavourCenter.y-writer.maxHeight/2));
		
		
		if(button!=null)button.render(batch);
	}

}
