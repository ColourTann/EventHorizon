package eh.card;

import java.util.ArrayList;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import eh.Main;
import eh.assets.Font;
import eh.assets.Gallery;
import eh.assets.Pic;
import eh.card.CardCode.Special;
import eh.screen.battle.Battle;
import eh.screen.battle.Battle.Phase;
import eh.screen.battle.Battle.State;
import eh.screen.battle.interfaceJunk.CycleButton;
import eh.ship.module.Module.ModuleType;
import eh.ship.module.shield.Shield;
import eh.ship.module.utils.Buff;
import eh.ship.module.utils.Buff.BuffType;
import eh.ship.module.weapon.Weapon;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.Timer.Interp;
import eh.util.maths.BoxCollider;
import eh.util.maths.Pair;

public class CardGraphic extends Bonkject {
	// POSITIONS//
	public static float width = 140;
	public static float height = 250;
	public static Pair enemyPlayStartPosition = new Pair(Main.width, 350);
	public static Pair enemyPlayToPosition = new Pair(850, 350);
	public static float maxSelectedHeight = -26;
	public static float fadeSpeed = 2.5f;
	public static Interp fadeType = Interp.LINEAR;
	
	public static Pair positionPic = new Pair(43, 31);
	private static Pair positionArray[][] = new Pair[6][5];
	private static Pair positionTitle = new Pair(70, 8);
	private static Pair positionRules = new Pair(5, 78);
	private static Pair positionEnergy = new Pair(13, 38);
	private static Pair positionCooldown = new Pair(119, 38);
	private static Pair positionShots = new Pair(12, 21);
	private static Pair positionTargeted = new Pair(116, 22);
	private static Pair positionEffectStart = new Pair(5, 65);
	private static Pair positionEffectMid = new Pair(9, 67);
	private static Pair positionEffectEnd = new Pair(110, 65);
	private static Pair positionAugment = new Pair(Main.width / 2 - width / 2, 126);
	private static float cooldownWidth = Gallery.iconCooldown.get().getWidth() * 3;
	private static float cooldownGap = cooldownWidth + 6;
	private static float effectGap = 17;
	private static float gap = 7;
	private static float selectSpeed=7;
	private static float flipSpeed=7;
	//Graphic stuff//
	private float selectedHeight = 0;
	private boolean drawTopPic = true;
	private boolean showLower = true;
	private boolean still;
	float sidePositions[] = new float[] {0,0};
	public boolean override;
	public Card card;
	public static String alphabet="!\"£$%^&*()_+_1234567890-=qwertyuiop[]QWERTYUIOP{}asdfghjkl;'#ASDFGHJKL:@~zxcvbnm,.|ZXCVBNM<>?";
	String topScrambledRules=randomString(70);
	String botScrambledRules=randomString(70);
	float scrambleTicks=0;
	boolean scrambled;

	//offcuts//
	private static CardGraphic augmentPicker;

	public CardGraphic(Card c) {
		super(new BoxCollider(0, 0, width, height));
		position=new Pair(Main.width/2 - width/2, -height -10);
		this.card = c;
	}

	// HALF CARD CONSTRUCTOR//
	public CardGraphic(Card staticHalfCard, float x, float y) {
		super(null);
		this.card = staticHalfCard;
		still = true;
		showLower=false;				//Static half cards have no collider and can't be moused//
		position=new Pair(x,y);
		if (card.side == 1) position.y += height / 2;
	}



	public void setPosition(Pair s) {

		position=s.copy();

		if(collider!=null){
			collider.position=s.copy();
		}
	}

	public void finishFlipping(){
		for(int i=0;i<2;i++){
			sidePositions[i] = card.side == i ? height / 2f : 0;
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

		//Animating selected card//
		if(card.getShip().player)selectedHeight += ((card.selected ? maxSelectedHeight : 0) - selectedHeight)* delta * selectSpeed;


		//Updating collider position//
		collider.position=position.copy().add(0,selectedHeight);

		//Compensating the collider position// (I could have the card shown in the lower part and move it there if it's wrong but I think this is simpler//
		if(!showLower){collider.position.y+=height/2;}
	}

	public void render(SpriteBatch batch) {
		if(alpha==0)return;
		//batch.end();debugRender();batch.begin();



		Color c = Colours.white;
		//Faded is used for cards from the wrong phase, cards cooling down and cards that can't be augmented//
		boolean wrongState = false;

		//Checking state//
		if (Battle.getPhase() == Phase.ShieldPhase&&card.mod.type == ModuleType.WEAPON||
				Battle.getPhase() == Phase.WeaponPhase&& card.mod.type == ModuleType.SHIELD) {
			c = Colours.faded;
			wrongState = true;
		}

		//Checking cooldown//
		if (!card.selected && card.mod.getCurrentCooldown() > 0)c = Colours.faded;

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
		if (showLower)renderHalf(1 - card.side, batch,Colours.withAlpha(c, lowerSideAlpha * alpha));
		renderHalf(card.side, batch, Colours.withAlpha(c, alpha));


		//Drawing cooldown stuff//
		if (!card.selected&&showLower) {
			//Coodlown symbols drawn faded if the card couldn't be clicked anyway//
			if (wrongState)batch.setColor(Colours.withAlpha(c, 1f));	

			int number = card.mod.getCurrentCooldown();
			Texture cd = Gallery.iconCooldown.get();

			for (float i = 0; i < number; i++) {
				Draw.drawTextureScaled(batch, cd, 
						position.x + width / 2 - cooldownWidth / 2 + cooldownGap* (i) - cooldownGap * (number - 1) / 2,
						position.y + height / 2 - cd.getHeight() / 2 * 3, 3, 3);
			}
		}
		Font.small.setColor(Colours.white);
		batch.setColor(Colours.white);
	}

	public void renderHalf(int part, SpriteBatch batch, Color c) {
		//Setting height and colours//
		float baseHeight = position.y + sidePositions[part] + selectedHeight;
		Color lightText = Colours.withAlpha(Colours.multiply(Colours.light, c),c.a);
		Color darkText = Colours.withAlpha(Colours.dark, c.a);
		batch.setColor(c);


		//Card base//
		
		Draw.drawTexture(batch, Gallery.cardBase.get(), position.x, baseHeight);

		//Card image//
		if (drawTopPic || part != card.side)
			Draw.drawTextureScaled(batch, card.getImage(part).get(), position.x + positionPic.x, baseHeight
					+ positionPic.y, 2, 2);

		//Name//
		Font.small.setColor(darkText);
		String name = card.getName(part);
		if(scrambled)name="Scrambled!";
		if(card.wasScrambled&&card.mod.ship.player)name=card.mod.getBuffAmount(BuffType.Scrambled)>0?"Unscrambling":"Unscrambled";
		Font.small.draw(batch, name,
				position.x + positionTitle.x - Font.small.getBounds(name).width / 2,
				baseHeight + positionTitle.y);

		//Rules//
		Font.small.setColor(lightText);
		String rules = card.getRules(part);
		if(scrambled)rules=part==0?topScrambledRules:botScrambledRules;
		if(card.wasScrambled&&card.mod.ship.player)rules="";
		Font.small.drawWrapped(batch, rules, position.x + positionRules.x, baseHeight+ positionRules.y, 132, HAlignment.LEFT);


		//Effect//
		Pic[] effectPics=null;
		int effect = card.getEffect(part);
		if(card.wasScrambled)effect=0;
		if (card.mod instanceof Weapon)effectPics=Gallery.damageIcon;
		if (card.mod instanceof Shield)	effectPics=Gallery.shieldIcon;
		if (effectPics!=null) {

			if(effect>7){
				for (int i = 0; i < effect-5; i++) {
					if (i == 0) {
						Draw.drawTexture(batch, effectPics[0].get(), position.x+ positionEffectStart.x, baseHeight+ positionEffectStart.y);
						continue;
					}
					if (i == 6) {
						Draw.drawTexture(batch, effectPics[2].get(), position.x+ positionEffectEnd.x, baseHeight+ positionEffectEnd.y);
						continue;
					}
					Draw.drawTexture(batch, effectPics[1].get(), position.x + positionEffectMid.x+ effectGap * i - 1, baseHeight + positionEffectMid.y);
				}
				Font.small.setColor(Colours.withAlpha(Colours.white, alpha));
				Font.small.draw(batch, "+5", position.x+ positionEffectStart.x+3, baseHeight+ positionEffectStart.y+11);
			}
			else{
				for (int i = 0; i < effect; i++) {
					if (i == 0) {
						Draw.drawTexture(batch, effectPics[0].get(), position.x+ positionEffectStart.x, baseHeight+ positionEffectStart.y);
						continue;
					}
					if (i == 6) {
						Draw.drawTexture(batch, effectPics[2].get(), position.x+ positionEffectEnd.x, baseHeight+ positionEffectEnd.y);
						continue;
					}
					Draw.drawTexture(batch, effectPics[1].get(), position.x + positionEffectMid.x+ effectGap * i - 1, baseHeight + positionEffectMid.y);
				}
			}
		}


		// Weapon Junk//
		if (card.mod instanceof Weapon&&!scrambled&&!card.wasScrambled) {
			int shots = card.getShots(part);
			if (shots > 1) {
				Draw.drawTexture(batch, Gallery.iconShots.get(), position.x + positionShots.x, baseHeight + positionShots.y-15);
				Font.small.setColor(darkText);
				Font.small.draw(batch, "" + shots, position.x + positionShots.x - 6, baseHeight + positionShots.y -13);
			}
			if (card.hasSpecial(Special.Targeted, part))Draw.drawTexture(batch, Gallery.iconTargeted.get(), position.x + positionTargeted.x, baseHeight + positionTargeted.y-17);
		}

		//Cost//
		if(scrambled){
			if(!card.wasScrambled&&card.mod.ship.player){
				Draw.drawTexture(batch, Gallery.iconJammed.get(),position.x+positionEnergy.x+positionArray[1][0].x-4,baseHeight+positionEnergy.y+positionArray[1][0].y-3);
			}
		}
		else{
			int cost = card.getCost(part);
			if(card.wasScrambled)cost=0;
			Font.medium.setColor(darkText);
			if (cost < 2) {
				for (int i = 0; i < cost; i++)	Draw.drawTexture(batch, Gallery.iconEnergy.get(), position.x + positionEnergy.x+ positionArray[cost][i].x, baseHeight+ positionEnergy.y + positionArray[cost][i].y);
			} 
			else {
				Draw.drawTexture(batch, Gallery.iconEnergy.get(), position.x + positionEnergy.x+ positionArray[5][0].x, baseHeight + positionEnergy.y+ positionArray[5][0].y);
				Font.medium.draw(batch, "" + cost, position.x + positionEnergy.x + 6,baseHeight + positionEnergy.y + positionArray[5][0].y - 5);
			}
		}

		//Cooldown//
		if(scrambled){
			if(!card.wasScrambled&&card.mod.ship.player){
				Draw.drawTexture(batch, Gallery.iconJammed.get(),position.x+positionCooldown.x+positionArray[1][0].x-4,baseHeight+positionCooldown.y+positionArray[1][0].y-3);
			}
		}
		else{
			int cooldown = card.getCoodlown(part);
			if (cooldown < 3) {
				for (int i = 0; i < cooldown; i++) Draw.drawTexture(batch, Gallery.iconCooldown.get(), position.x + positionCooldown.x+ positionArray[cooldown][i].x, baseHeight+ positionCooldown.y + positionArray[cooldown][i].y);
			} 
			else {
				Draw.drawTexture(batch, Gallery.iconCooldown.get(), position.x + positionCooldown.x+ positionArray[5][0].x, baseHeight + positionCooldown.y+ positionArray[5][0].y);
				Font.medium.draw(batch, "" + cooldown, position.x + positionCooldown.x + 6,baseHeight + positionCooldown.y + positionArray[5][0].y+ 18);
			}
		}
		batch.setColor(Colours.white);
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
		slide(positionAugment, 2, Interp.SQUARE);
	}

	public void hideLower() {
		showLower = false;
		((BoxCollider) collider).h=height/2;
	}

	public void showLower() {
		showLower = true;
		((BoxCollider) collider).h=height;
	}

	@Override
	public void mouseClicked(boolean left) {
		if (!left) {
			if(card.getShip().player)card.rightClick();
			return;
		}
		card.click();
	}

	@Override
	public void mouseDown() {
		card.mod.moused=true;
		moveToTop();
	}

	@Override
	public void mouseUp() {
		card.mod.moused=false;
	}



	public static void setAugmentOrTarget(CardGraphic augmenter){
		resetOffCuts();
		augmentPicker=augmenter;
	}

	public static void resetOffCuts(){
		augmentPicker=null;
	}

	public static void renderOffCuts(SpriteBatch batch){
		if(augmentPicker!=null)augmentPicker.render(batch);
		for(Card c:CycleButton.choices)c.getGraphic().render(batch);
	}


}
