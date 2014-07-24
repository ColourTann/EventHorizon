package eh.screen.battle.tutorial;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import eh.Main;
import eh.assets.Font;
import eh.assets.Gallery;
import eh.card.Card;
import eh.card.CardCode.AI;
import eh.card.CardCode.Special;
import eh.screen.battle.Battle;
import eh.screen.battle.Battle.Phase;
import eh.screen.battle.tutorial.Task.TaskType;
import eh.ship.Ship;
import eh.ship.module.Module;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Junk;
import eh.util.Bonkject.Interp;
import eh.util.maths.Collider;
import eh.util.maths.Sink;

public class Tutorial extends Bonkject{

	static float width=300;
	static float offset=6;
	float x=Main.width/2-width/2;
	float y=550;
	float height;
	String str;
	Sink target;
	Sink origin;
	Sink vector;
	float rotation;
	float distance;
	public Trigger trig;
	Effect eff;
	static Checklist currentList;
	public static boolean overrideStopFlip=false;
	public static boolean overrideStopEnd=false;
	public static boolean overrideStopClick=false;
	public static boolean overrideStopCycle=false;
	public static Ship enemy;
	public static Ship player;
	public static ArrayList<Tutorial> three= new ArrayList<Tutorial>();
	int special=0;
	public Tutorial(String message, Trigger t, Effect e) {
		super(null);
		demousectivate();
		this.str=message;
		height=Font.medium.getWrappedBounds(str, width-offset*2).height+9;
		trig=t;
		eff=e;
		y+=height/2;
	}

	public Tutorial(String message, float x){
		super(null);
		demousectivate();
		this.str=message;
		height=Font.medium.getWrappedBounds(str, width-offset*2).height+9;
		y+=height/2;
		this.x=x-width/2;
	}

	public Tutorial(String message, Sink point) {
		super(null);
		demousectivate();
		this.str=message;
		height=Font.medium.getWrappedBounds(str, width-offset*2).height+9;
		this.target=point;
		if(point.x<x){
			origin=new Sink(x,y-height/2);
		}
		else if(point.x>x+width){
			origin=new Sink(x+width,y-height/2);
		}
		else if(point.y<y){
			origin=new Sink(x+width/2,y);
		}
		else{
			origin=new Sink(x+width/2,y+height);
		}
		vector=Sink.getVector(this.origin, target);
		rotation=(float) Math.atan2(vector.y, vector.x);
		distance=vector.getDistance();
		vector=vector.normalise();
		y+=height/2;
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(1,1,1,alpha);
		if(target!=null){
			Junk.drawTextureRotatedScaled(batch, Gallery.tutPoint.get(), origin.x, origin.y, distance, 1, rotation);

		}
		Junk.drawTextureScaled(batch,Gallery.tutPanelBorder.get(), x,y, 3, 3);
		Junk.drawTextureScaled(batch, Gallery.tutPanelMain.get(), x, y-height, 3, height);
		Junk.drawTextureScaled(batch,Gallery.tutPanelBorder.get(), x,y-height, 3, -3);
		Font.medium.setColor(Colours.withAlpha(Colours.light,alpha));
		Font.medium.drawWrapped(batch, str, x+offset, y-2, width-offset*2, HAlignment.CENTER);
		
		if(special==1){
			batch.draw(Gallery.greenHP[1].get(), 752, 568);
			batch.draw(Gallery.greenHP[2].get(), 582, 550);
		}
		//756 515
		//572 756
		
		batch.setColor(1,1,1,1);
	}

	@Override
	public void mouseDown() {
	}

	@Override
	public void mouseUp() {
	}

	@Override
	public void mouseClicked(boolean left) {
	}

	@Override
	public void update(float delta) {

	}


	public enum Trigger{PlayerShieldPhase, CheckList, PlayerWeaponPhase}
	public enum Effect{EnemyPlayCards, ShieldGenList, AllowedToPlay, PlayWeaponList, ShieldMajorList, FirstWeaponDraw, TargetGeneratorList, DrawTargeted, drawTwoShields, LotsShieldList, End, UnscrambleList, DrawThreeTesla}
	public static ArrayList<Tutorial> tutorials=new ArrayList<Tutorial>();
	public static void init(){

		enemy=Battle.enemy;
		player=Battle.player;

		add("This is your ship\n(click to continue)", new Sink(429,482));
		add("It has two weapons,", new Sink(130,562));
		add("a shield,", new Sink(130,342));
		add("a generator", new Sink(130,206));
		add("and a computer", new Sink(130,64));
		add("They make up a deck of cards that you use to fight!");
		add("The enemy ship is playing weapon cards to attack you! \n(click them to continue)", Trigger.PlayerShieldPhase, Effect.EnemyPlayCards);
		add("These orange blips show that you have incoming damage on your generator", new Sink(43,260));
		add("Shield cards give you shield points to spend when you play them", Effect.AllowedToPlay);
		add("This card gives two shield points", new Sink(Main.width/2, 249));
		add("", Trigger.CheckList, Effect.ShieldGenList);
		add("", Trigger.PlayerWeaponPhase);
		add("Now it's your turn to fight back", Effect.FirstWeaponDraw);
		add("Cards have an energy cost", new Sink(305,216));
		add("And some cards have a cooldown, which means you must wait before playing another card from this module",new Sink(409,216));
		add("Weapon and shield cards have bars which show how much damage or shielding they provide", new Sink(358,179));
		add("", Trigger.CheckList, Effect.PlayWeaponList);
		add("", Trigger.PlayerShieldPhase);
		add("If you take enough damage to cover a [   ] or a [   ], you take a major damage and something bad happens!", 1);
		add("If a ship takes 5 total major damage, it is defeated");
		add("You are about to take major damage, shield it all");
		add("", Trigger.CheckList, Effect.ShieldMajorList);
		add("", Trigger.PlayerWeaponPhase);
		add("All cards have a special alternate side");
		add("You can choose which side to play by right-clicking the card");
		add("The special side of this weapon is targeted, which means you can pick a target rather than it hitting a random enemy module", Effect.DrawTargeted);
		add("", Trigger.CheckList, Effect.TargetGeneratorList);
		add("", Trigger.PlayerShieldPhase);
		add("You're about to take three major damage!", Effect.drawTwoShields);
		add("", Trigger.CheckList, Effect.LotsShieldList);
		add("", Trigger.PlayerWeaponPhase, Effect.DrawThreeTesla);
		add("Your tesla module has been scrambled because it took major damage");
		add("Click on a scrambled card to repair it, that card will be free but will not fire");
		add("", Trigger.CheckList, Effect.UnscrambleList);
		add("", Trigger.PlayerShieldPhase);
		add("At the beginning of each turn, you draw to your maximum hand size");
		add("And gain your income in energy",new Sink(320,296));
		add("You're good to go! If there's anything else you don't understand, press tab");
		add("", Effect.End);
		for(Tutorial p:tutorials){
			p.derendervate();
		}
		tutorials.get(0).activate();
	}
	public static void add(String s){
		tutorials.add(new Tutorial(s,null,null));
	}
	public static void add(String s, int special){
		Tutorial t=(new Tutorial(s,null,null));
		t.special=special;
		tutorials.add(t);
	}
	public static void add(String s, Trigger t){
		tutorials.add(new Tutorial(s, t, null));
	}
	public static void add(String s, Effect e){
		tutorials.add(new Tutorial(s, null, e));
	}
	public static void add(String s, Trigger t, Effect e){
		tutorials.add(new Tutorial(s, t, e));
	}
	public static void add(String s, Sink point){
		tutorials.add(new Tutorial(s, point));
	}



	public static void next(){
		if(!Battle.tutorial){
			for(Tutorial t:three){
				t.fadeOut(Interp.LINEAR, 3);
			}
			three.clear();
			return;
		}
		System.out.println("next");
		Tutorial t= tutorials.get(0);
		if(t.trig!=null){
			switch(t.trig){
			case PlayerShieldPhase:
				if(Battle.getPhase()!=Phase.ShieldPhase)return;
				break;
			case CheckList:
				if(!currentList.tasks[currentList.tasks.length-1].isDone())return;
				break;
			case PlayerWeaponPhase:
				if(Battle.getPhase()!=Phase.WeaponPhase)return;
				break;
			}
		}
		if(currentList!=null)currentList.fadeOut(Interp.LINEAR, 3);
		currentList=null;
		tutorials.remove(0).fadeOut(Interp.LINEAR, 3);
		t=tutorials.get(0);
		if(t.str.length()>2)t.activate();
		System.out.println(t.height);

		if(t.eff!=null){
			switch(t.eff){
			case End:
				System.out.println("ending");
				Battle.tutorial=false;
				player.drawToMaximum();
				enemy.drawToMaximum();
				for(Module m:player.getRandomisedModules()){
					m.removeSramble();
				}
				tutorials.remove(0).fadeOut(Interp.LINEAR, 3);
				break;
			case EnemyPlayCards:
				Battle.enemy.enemyPickCard();
				break;
			case DrawTargeted:
				player.drawCard(player.getModule(0).getCard(6));
				Card c=enemy.getModule(1).getCard(5);
				c.getCode(1).clear();
				c.getCode(1).add(Special.Targeted);
				c.getCode(1).add(Special.BonusVsModule0);
				c.getCode(1).add(AI.BetterAgainstSpecificSystem);
				c.zSetEffect(4);
				enemy.drawCard(c);
				Card c1=enemy.getModule(1).getCard(5);
				c1.getCode(1).clear();
				c1.getCode(1).add(Special.Targeted);
				c1.getCode(1).add(Special.BonusVsModule0);
				c1.getCode(1).add(AI.BetterAgainstSpecificSystem);
				c1.zSetEffect(4);
				enemy.drawCard(c1);
				Card c2=enemy.getModule(1).getCard(5);
				c2.getCode(1).clear();
				c2.getCode(1).add(Special.Targeted);
				c2.getCode(1).add(Special.BonusVsModule1);
				c2.getCode(1).add(AI.BetterAgainstSpecificSystem);
				c2.zSetEffect(6);
				enemy.drawCard(c2);
				Card c3=enemy.getModule(1).getCard(5);
				c3.getCode(1).clear();
				c3.getCode(1).add(Special.Targeted);
				c3.getCode(1).add(Special.BonusVsModule1);
				c3.getCode(1).add(AI.BetterAgainstSpecificSystem);
				c3.zSetEffect(6);
				enemy.drawCard(c3);
				overrideStopFlip=true;
				break;
			case ShieldGenList:
				currentList=new Checklist(new Task[]{
						new Task("Play the shield card\n(click to play)",TaskType.PlayShield), 
						new Task("Use both shield points on your generator by clicking on it twice",TaskType.ShieldGen),
						new Task("Click the blue shield button to confirm", TaskType.EndShieldPhase)
				});
				break;
			case PlayWeaponList:
				currentList=new Checklist(new Task[]{
						new Task("Play at least one weapon card",TaskType.PlayWeapon), 
						new Task("Click the red crossed swords to confirm",TaskType.EndWeaponPhase),
				});
				enemy.drawCard(enemy.getShield().getNextCard());
				enemy.drawCard(new Card(enemy.getModule(1),5));
				enemy.drawCard(new Card(enemy.getModule(1),6));
				break;
			case ShieldMajorList:
				currentList=new Checklist(new Task[]{
						new Task("Prevent all major damage [   ]",TaskType.PreventAllMajor), 
						new Task("Click the blue shield to confirm",TaskType.EndShieldPhase),
				});
				currentList.drawDam=true;
				Battle.player.discardHand();
				Battle.player.drawCard(Battle.player.getShield().getCard(5));
				Battle.player.drawCard(Battle.player.getShield().getCard(5));
				Battle.player.drawCard(Battle.player.getShield().getCard(5));
				break;
				
			case AllowedToPlay:
				Battle.player.drawCard(Battle.player.getShield().getCard(5));
				break;
				
			case FirstWeaponDraw:
				player.drawCard(player.getModule(1).getCard(5));
				player.drawCard(player.getModule(1).getCard(5));
				player.drawCard(player.getModule(0).getCard(5));
				break;

			case TargetGeneratorList:
				currentList=new Checklist(new Task[]{
						new Task("Play the alternate side of the pulse card",TaskType.PlayerAlternateSide), 
						new Task("Target the enemy Generator",TaskType.TargetGenerator),
						new Task("Click the red crossed swords to confirm", TaskType.EndWeaponPhase)
				});
				break;
				
			case drawTwoShields:
					player.drawCard(player.getShield().getCard(3));
					player.drawCard(player.getShield().getCard(4));
				break;
				
			case LotsShieldList:
				currentList=new Checklist(new Task[]{
						new Task("Prevent two major damage",TaskType.WeirdPrevent), 
						new Task("Click the blue shield to confirm",TaskType.EndShieldPhase)
				});
				break;
			case UnscrambleList:
				currentList=new Checklist(new Task[]{
						new Task("Unscramble your tesla",TaskType.NoneScrambled), 
						new Task("Play one of the other tesla card",TaskType.WeaponPlayed),
						new Task("Click the red crossed swords to confirm", TaskType.EndWeaponPhase)
				});
				break;
			case DrawThreeTesla:
				for(int i=0;i<3;i++)player.drawCard(player.getModule(1).getNextCard());
				enemy.drawCard(enemy.getModule(0).getNextCard());
				enemy.drawCard(enemy.getModule(1).getNextCard());
				break;
		
			}
		}
	}

	public static boolean stopFlip(){
		if(!Battle.tutorial)return false;
		return !overrideStopFlip;
	}
	public static boolean stopClick(){
		if(!Battle.tutorial)return false;
		if(currentList!=null)return false;
		return !overrideStopClick;
	}
	public static boolean stopEnd(){
		if(!Battle.tutorial){
			return false;
		}
		if(currentList==null)return !overrideStopEnd;
		for(Task t:currentList.tasks){
			if(t.t==TaskType.EndShieldPhase)return false;
			if(t.t==TaskType.EndWeaponPhase)return false;
			if(t.isDone())continue;
			break;
		}
		return !overrideStopEnd;
	}
	public static boolean stopCycle(){
		return !overrideStopCycle;
	}
	public static void makeThree(){
		three.add(new Tutorial("The cycle button is for getting rid of cards you don't want to get ones you need. It costs more and more energy each time you use it", new Sink(197,335)));
		three.add(new Tutorial("Absorb: [effect]- if the shield is used, you gain the effect", Main.width/5f));
		three.add(new Tutorial("Augment [cardtype]: [effect]- you play this to add the effect to another card of the right cardtype",Main.width/5f*4f));
	}
}
