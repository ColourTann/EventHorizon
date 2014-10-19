package game.card;

import util.Colours;
import util.Draw;
import util.TextWriter;
import util.assets.Font;
import util.image.Pic;
import util.maths.BoxCollider;
import util.maths.Pair;
import util.update.Mouser;
import util.update.Screen;
import util.update.Timer;
import util.update.Timer.Finisher;
import util.update.Timer.Interp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.assets.Sounds;
import game.card.CardCode.Special;
import game.module.Module.ModuleType;
import game.module.junk.buff.Buff;
import game.module.junk.buff.Buff.BuffType;
import game.screen.battle.Battle;
import game.screen.battle.Battle.Phase;
import game.screen.battle.Battle.State;
import game.screen.battle.interfaceJunk.CycleButton;
import game.screen.preBattle.PreBattle;

public class CardGraphic extends Mouser {
	// POSITIONS//
	public static float width = 140;
	public static float height = 249;
	public static Pair enemyPlayStartPosition = new Pair(Main.width, 150);
	public static Pair enemyPlayToPosition = new Pair(850, 150);
	public static float maxSelectedHeight = -26;
	public static float fadeSpeed = .4f;
	public static Interp fadeType = Interp.LINEAR;
	public static Pair startPosition=new Pair(Main.width/2 - width/2, Main.height+height+10);
	public static Pair positionPic = new Pair(43, 31);
	private static Pair positionArray[][] = new Pair[6][5];
	private static Pair positionTitle = new Pair(70, 8);
	private static Pair positionRules = new Pair(5, 76);
	private static Pair positionEnergy = new Pair(13, 38);
	
	
	private static Pair bonusesCenter = new Pair(124,45);
	private static int bonusGap= 20;

	private static Pair positionEffectStart = new Pair(5, 64);
	private static Pair positionEffectMid = new Pair(9, 66);
	private static Pair positionEffectEnd = new Pair(110, 64);
	private static Pair positionAugment = new Pair(Main.width / 2 - width / 2, 320);
	private static float cooldownWidth = Gallery.iconCooldown.get().getWidth() * 3;
	private static float cooldownGap = cooldownWidth + 6;
	private static float effectGap = 17;
	private static float gap = 7;
	private static float selectSpeed=7;
	private static float flipSpeed=7;
	private TextWriter[] textWriters = new TextWriter[2];
	//Graphic stuff//
	private float selectedHeight = 0;
	private boolean drawTopPic = true;
	private boolean showLower = true;
	private boolean still;
	float sidePositions[] = new float[] {0,0};
	public boolean override;
	public Card card;
	public static String alphabet="!$%^&*()_+1234567890-=qwertyuiop[]QWERTYUIOP{}asdfghjkl;'#ASDFGHJKL:@zxcvbnm,.|ZXCVBNM<>?";
	String topScrambledRules=randomString(70);
	String botScrambledRules=randomString(70);
	float scrambleTicks=0;
	boolean scrambled;
	private Timer hoverTimer= new Timer();
	private Timer hoverFadeTimer= new Timer();
	public static CardGraphic onTopGraphic;

	//offcuts//
	//public static CardGraphic augmentPicker;

	public CardGraphic(Card c) {
		position=startPosition.copy();
		this.card = c;
		mousectivate(new BoxCollider(0, 0, width, height));
	}

	// HALF CARD CONSTRUCTOR//
	public CardGraphic(Card staticHalfCard, float x, float y) {
		this.card = staticHalfCard;
		still = true;
		showLower=false;				//Static half cards have no collider and can't be moused//
		position=new Pair(x,y);
		if (card.side == 1) position.y += height / 2;
		deactivate();
	}

	public void setupTextWriters(){
		for(int i=0;i<2;i++){
			TextWriter tw= new TextWriter(Font.small, card.getRules(i));
			tw.setCardGraphicReplacements();
			tw.setWrapWidth(132);
			textWriters[i]=tw;
			
		}
	}

	public void setPosition(Pair s) {

		position=s.copy();
		position=position.floor();
		if(collider!=null){
			collider.position=s.copy();
		}
	}

	public void finishFlipping(){
		for(int i=0;i<2;i++){
			sidePositions[i] =  card.side == i ?0:  height / 2f ;
		}
	}

	public void update(float delta) {
		if (still)return;
		scrambled=card.mod.getBuffAmount(Buff.BuffType.Scrambled)>0;
		if(!scrambled&&card.wasScrambled&&!card.mod.ship.player)scrambled=true;
		// Shit about flipping cards//
		for (int i = 0; i < 2; i++) {
			float target = card.side == i ?0:  height / 2f ;
			sidePositions[i] += (target - sidePositions[i]) * delta * flipSpeed;
			if (Math.abs(sidePositions[i] - target) < 1f)sidePositions[i] = target;
		}
		collider.position=position.copy().add(0,selectedHeight);

		//Animating selected card//
		if(card.getShip()==null)return;
		if(card.getShip().player)selectedHeight += ((card.selected ? maxSelectedHeight : 0) - selectedHeight)* delta * selectSpeed;




		//Updating collider position//



	}

	public void render(SpriteBatch batch) {
		if(alpha==0)return;
		//batch.end();debugRender();batch.begin();



		Color c = Colours.white;
		//Faded is used for cards from the wrong phase, cards cooling down and cards that can't be augmented//
		boolean wrongState = false;



		//Checking state//
		if(Screen.isActiveType(Battle.class)){
			if ((Battle.getPhase() == Phase.ShieldPhase&&card.type == ModuleType.WEAPON)||
					(Battle.getPhase() == Phase.WeaponPhase&& card.type == ModuleType.SHIELD)) {
				c = Colours.faded;
				wrongState = true;
			}
		}

		//Checking cooldown//
		if (!card.selected && card.mod.getBuffAmount(BuffType.Disabled) > 0)c = Colours.faded;

		//Fade out the lower side if selected//
		float lowerSideAlpha = card.selected?.3f:1;

		//Override colour due to augmenting state//
		if (Battle.getState() == State.Augmenting) {
			if (card.validAugmentTarget(Battle.augmentSource) || card == Battle.augmentSource)c = Colours.white;
			else c = Colours.faded;
		}

		//Overriding colour due to being a half-card//
		if (still||!showLower)c = Colours.white;

		//Overriding due to cycleDiscard//
		if(Battle.getState()==State.CycleDiscard){
			if(card.selected)c=Colours.faded;
			else c=Colours.white;
		}

		//Overriding colour due to mysterious circumstances//
		if(override){
			c=Colours.white;
		}

		//Draw the lower one first so the higher one overlaps when flipping//
		//batch.setColor(Colours.compCols6[3]);


		//Draw.draw(batch, Gallery.cardOutline.get(), position.x, getBaseHeight(1));
		//Draw.draw(batch, Gallery.cardOutline.get(), position.x, getBaseHeight(0));


		if (showLower)renderHalf(1 - card.side, batch,Colours.withAlpha(c, lowerSideAlpha * alpha));
		renderHalf(card.side, batch, Colours.withAlpha(c, alpha));

		//Drawing cooldown stuff//
		if (!card.selected&&showLower) {
			//Coodlown symbols drawn faded if the card couldn't be clicked anyway//
			if (wrongState)batch.setColor(Colours.withAlpha(c, 1f));	

			int number = card.mod.getBuffDuration(BuffType.Disabled);
			Texture cd = Gallery.iconCooldown.get();

			for (float i = 0; i < number; i++) {
				Draw.drawScaled(batch, cd, 
						position.x + width / 2 - cooldownWidth / 2 + cooldownGap* (i) - cooldownGap * (number - 1) / 2,
						position.y + height / 2 - cd.getHeight() / 2 * 3, 3, 3);
			}
		}
		Font.small.setColor(Colours.white);
		batch.setColor(Colours.white);
			
		if (hoverFadeTimer.getFloat()!=0) {
			CardHover.render(batch, card, getBaseHeight(1-card.side), hoverFadeTimer.getFloat());
		}
	}

	public float getBaseHeight(int part){
		return  position.y + sidePositions[part] + selectedHeight;
	}

	public void renderHalf(int part, SpriteBatch batch, Color c) {
		//Setting height and colours//
		float baseHeight = getBaseHeight(part);
		Color lightText = Colours.withAlpha(Colours.multiply(Colours.light, c),c.a);
		Color darkText = Colours.withAlpha(Colours.dark, c.a);
		if(override) {
			c=new Color(1,1,1,alpha);
			lightText.a=alpha;
			darkText.a=alpha;
		}
		batch.setColor(c);

		//Card base//
		if(card.consumable) Draw.draw(batch, Gallery.cardBaseConsumable.get(), position.x, baseHeight);
		else Draw.draw(batch, Gallery.cardBase.get(), position.x, baseHeight);

		//Augment colouring//
		if(!still&&card.isAugmented(part)){
			float glowAlpha=(float)Math.sin(Main.ticks*5);
			glowAlpha+=2.3f;
			glowAlpha/=4;
			Color prevCol=batch.getColor();
			batch.setColor(Colours.withAlpha(Colours.orangeHPCols[0], glowAlpha));
			Draw.draw(batch, Gallery.cardOutline.get(), position.x, baseHeight);
			batch.setColor(prevCol);
		}

		if(moused&&Screen.isActiveType(PreBattle.class)){
			batch.setColor(Colours.genCols5[1]);
			Draw.draw(batch, Gallery.cardOutline.get(), position.x, baseHeight);
			batch.setColor(1,1,1,1);
		}
		
		if(card.addToDeck){
			batch.setColor(Colours.light);
			Draw.draw(batch, Gallery.cardOutline.get(), position.x, baseHeight);
			batch.setColor(1,1,1,1);
		}

		//Card image//
		if (drawTopPic || part != card.side){
			Draw.drawScaled(batch, card.getImage(part).get(), position.x + positionPic.x, baseHeight+ positionPic.y, 2, 2);		
		}

		//Name//
		Font.small.setColor(darkText);
		String name = card.getName(part);
		if(scrambled)name="Scrambled!";
		if(card.wasScrambled&&card.mod.ship.player)name=card.mod.getBuffAmount(BuffType.Scrambled)>0?"Unscrambling":"Unscrambled";
		Font.small.draw(batch, name,
				position.x + positionTitle.x - Font.small.getBounds(name).width / 2,
				baseHeight + positionTitle.y+2);

		//Rules//
		Font.small.setColor(lightText);
		if(scrambled){
			String rules=part==0?topScrambledRules:botScrambledRules;
			Font.small.drawWrapped(batch, rules, position.x + positionRules.x, baseHeight+ positionRules.y+3, 132, HAlignment.LEFT);
		}
		else{
			if(textWriters[part]==null)setupTextWriters();
			textWriters[part].drawText(batch, position.x + positionRules.x, baseHeight+ positionRules.y+3);
		}
		

		//Effect//
		Pic[] effectPics=card.getShots(0)>0?Gallery.damageIcon:Gallery.shieldIcon;
		int effect = card.getEffect(part);
		if(card.wasScrambled)effect=0;

		for (int i = 0; i < effect; i++) {
			if (i == 0) {
				Draw.draw(batch, effectPics[0].get(), position.x+ positionEffectStart.x, baseHeight+ positionEffectStart.y);
				if(effect>7){
					Draw.draw(batch, Gallery.fiveIcon[0].get(), position.x+ positionEffectStart.x, baseHeight+ positionEffectStart.y);
					effect-=4;
				}
				continue;
			}
			if (i == 6) {
				Draw.draw(batch, effectPics[2].get(), position.x+ positionEffectEnd.x, baseHeight+ positionEffectEnd.y);

				continue;
			}
			Draw.draw(batch, effectPics[1].get(), position.x + positionEffectMid.x+ effectGap * i - 1, baseHeight + positionEffectMid.y);
			if(effect>7){
				Draw.draw(batch, Gallery.fiveIcon[1].get(), position.x + positionEffectMid.x+ effectGap * i - 1, baseHeight + positionEffectMid.y);
				effect-=4;
			}
		}


		// Weapon Junk//
		int shots = card.getShots(part);
		int numSpecials= 0;
		if(card.hasSpecial(Special.Targeted, part)) numSpecials++;
		if(shots>1)numSpecials++;
		
		//MultiShots
		if (shots > 1) {
			
			Draw.drawCentered(batch, Gallery.iconShots.get(), position.x + bonusesCenter.x+4, baseHeight + bonusesCenter.y+(numSpecials==1?0:bonusGap/2));
			Font.small.setColor(darkText);
			Font.drawFontCentered(batch, ""+shots, Font.small, position.x + bonusesCenter.x-6,   baseHeight + bonusesCenter.y+(numSpecials==1?0:bonusGap/2));
		}
		//Targeted//
		if (card.hasSpecial(Special.Targeted, part)){
			Draw.drawCentered(batch, Gallery.iconTargeted.get(), position.x + bonusesCenter.x,  baseHeight + bonusesCenter.y-(numSpecials==1?0:bonusGap/5));
		}



		if(scrambled){
			if(!card.wasScrambled&&card.mod.ship.player){
				Draw.draw(batch, Gallery.iconScrambled.get(),position.x+positionEnergy.x+positionArray[1][0].x-4,baseHeight+positionEnergy.y+positionArray[1][0].y-3);
			}
		}


		if(scrambled){
			if(!card.wasScrambled&&card.mod.ship.player){
				Draw.draw(batch, Gallery.iconScrambled.get(),position.x+positionEnergy.x+positionArray[1][0].x-4,baseHeight+positionEnergy.y+positionArray[1][0].y-3);
			}
		}

		//Cost//
		else{
			int cost = card.getCost(part);
			if(card.wasScrambled)cost=0;
			Font.medium.setColor(darkText);
			if (cost < 5) {
				for (int i = 0; i < cost; i++)	Draw.draw(batch, Gallery.iconEnergy.get(), position.x + positionEnergy.x+ positionArray[cost][i].x, baseHeight+ positionEnergy.y + positionArray[cost][i].y);
			} 
			else {
				BitmapFont font = Font.medium;
				if(cost>9)font=Font.small;
				font.setColor(darkText);
				Draw.draw(batch, Gallery.iconEnergy.get(), position.x + positionEnergy.x+ positionArray[5][0].x-1, baseHeight + positionEnergy.y+ positionArray[5][0].y);
				Font.drawFontCentered(batch, "" + cost, font, position.x + positionEnergy.x + 14,baseHeight + positionEnergy.y + positionArray[5][0].y+7);
				//	font.draw(batch, "" + cost, position.x + positionEnergy.x + 6,baseHeight + positionEnergy.y + positionArray[5][0].y);
			}
		}

		batch.setColor(Colours.white);
		if(part==card.side&&moused&&Screen.isActiveType(Battle.class)){
			Draw.draw(batch, Gallery.cardBase.getOutline(), position.x, baseHeight);
		}
	}
	


	//Setting up Energy icon positions//
	public static void init() {
		positionArray[1][0] = new Pair(0, 0);

		positionArray[2][0] = new Pair(-gap, gap);
		positionArray[2][1] = new Pair(gap, -gap);

		positionArray[3][0] = new Pair(-gap, gap);
		positionArray[3][1] = new Pair(0, 0);
		positionArray[3][2] = new Pair(gap, -gap);

		positionArray[4][0] = new Pair(-gap, gap);
		positionArray[4][1] = new Pair(gap, -gap);
		positionArray[4][2] = new Pair(-gap, -gap);
		positionArray[4][3] = new Pair(gap, gap);

		positionArray[5][0] = new Pair(-gap, 0);
	}

	public String randomString(int length){
		String result="";
		for(int i=0;i<length;i++){
			result+=alphabet.charAt((int)(Math.random()*alphabet.length()));
		}
		return result;
	}

	public String scroll(String base){
		base=base.substring(0,base.length()-1);
		base=alphabet.charAt((int)(Math.random()*alphabet.length()))+base;
		return base;
	}

	public void removeTopPic() {
		drawTopPic = false;
	}

	public void moveUp() {
		slide(positionAugment, .5f, Interp.SQUARE);
	}

	public void hideLower() {
		showLower = false;
		((BoxCollider) collider).height=height/2;
	}

	public void showLower() {
		showLower = true;
		((BoxCollider) collider).height=height;
	}

	@Override
	public void mouseClicked(boolean left) {

		if(Screen.isActiveType(PreBattle.class)){

			card.addToDeck=!card.addToDeck;

			if(card.addToDeck){
				Sounds.cardSelect.overlay();	
			}
			else{
				Sounds.cardDeselect.overlay();
			}
			card.selected=!card.selected;
			return;
		}

		if (!left) {
			if(card.getShip().player)card.rightClick();
			return;
		}
		card.click();
	}

	@Override
	public void mouseDown() {
		hoverTimer= new Timer(0,1,.7f, Interp.LINEAR);
		hoverTimer.addFinisher(new Finisher() {
			@Override
			public void finish() {
				hoverFadeTimer=new Timer(0,1,.5f,Interp.LINEAR);
			}
		});
		if(Screen.isActiveType(Battle.class)){
			if(!Battle.isTutorial())			card.mod.mouse();

			if(Battle.getPlayer().hand.contains(card))onTopGraphic=this;
			if(card.getShip()!=null){
				card.getShip().cardOrIconMoused(card);
			}
			moveToTop();
		}
		if(Screen.isActiveType(PreBattle.class)){
			PreBattle.mousedGraphic=this;
		}
	}

	@Override
	public void mouseUp() {
		hoverFadeTimer=new Timer(hoverFadeTimer.getFloat(),0, .35f, Interp.LINEAR);
		hoverTimer.removeFinisher();
		if(Screen.isActiveType(Battle.class)){
			card.mod.unmouse();
			onTopGraphic=null;
			if(card.getShip()!=null)card.getShip().cardOrIconUnmoused();
		}
	}

	public static void renderOffCuts(SpriteBatch batch){
		if(Battle.augmentSource!=null)Battle.augmentSource.getGraphic().render(batch);
		if(Battle.moduleChooser!=null)Battle.moduleChooser.getGraphic().render(batch);
		if(Battle.targetSource!=null)Battle.targetSource.getGraphic().render(batch);
		if(onTopGraphic!=null)onTopGraphic.render(batch);
		for(Card c:CycleButton.choices)c.getGraphic().render(batch);
		for(CardGraphic cg:Battle.enemyHandList)cg.render(batch);
		//if(Card.)
	}


}
