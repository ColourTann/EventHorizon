package game.screen.battle.tutorial;

import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.update.Timer.Interp;
import util.assets.Font;
import util.image.Pic;
import util.maths.Pair;
import util.update.Updater;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.component.Component;
import game.module.component.weapon.Pulse;
import game.module.junk.ModuleStats;
import game.screen.battle.Battle;
import game.screen.battle.Battle.Phase;
import game.screen.battle.tutorial.Task.TaskType;
import game.ship.Ship;

public class Tutorial extends Updater{
	public static Checklist currentList;
	public static boolean overrideStopFlip=false;
	public static boolean overrideStopEnd=false;
	public static boolean overrideStopClick=false;
	public static boolean overrideStopCycle=false;
	public static Ship enemy;
	public static Ship player;
	public static ArrayList<Tutorial> three= new ArrayList<Tutorial>();
	public static int index;
	public static Card firstShieldCard;
	public static Card firstWeaponCard;
	public static Card targetedWeaponCard;

	static float width=350;
	static float offset=8;
	float x=Main.width/2-width/2;
	float y=173;
	float height;
	String str;
	Pair target;
	Pair origin;
	Pair vector;
	float rotation;
	float distance;
	public Trigger trig;
	Effect eff;
	int special=0;
	ArrayList<PicLoc> myGlows= new ArrayList<PicLoc>();
	boolean activated;
	private boolean triggered;
	
	//Highlight stuff//
	public static ArrayList<PicLoc> glows= new ArrayList<PicLoc>();

	public Tutorial(String message, Trigger t, Effect e) {
		this.str=message;
		height=Font.medium.getWrappedBounds(str, width-offset*2).height+14;
		trig=t;
		eff=e;
		y-=height/2;
	}

	public Tutorial(String message, float x){
		this.str=message;
		height=Font.medium.getWrappedBounds(str, width-offset*2).height+14;
		y-=height/2;
		this.x=x-width/2;
	}

	public Tutorial(String message, Pair point) {
		this.str=message;
		height=Font.medium.getWrappedBounds(str, width-offset*2).height+14;
		this.target=point;
		if(point.x<x){
			origin=new Pair(x+10,y+height/2+5);
		}
		else if(point.x>x+width){
			origin=new Pair(x+width,y+height/2);
		}
		else if(point.y<y){
			origin=new Pair(x+width/2,y);
		}
		else{
			origin=new Pair(x+width/2,y+height/2);
		}
		vector=Pair.getVector(this.origin, target);
		rotation=(float) Math.atan2(vector.y, vector.x);
		distance=vector.getDistance();
		vector=vector.normalise();
		y-=height/2;
	}


	public void render(SpriteBatch batch) {

		if(str.equals(""))return;

		batch.setColor(1,1,1,alpha);
		if(target!=null){
			Draw.drawRotatedScaled(batch, Gallery.tutPoint.get(), origin.x, origin.y, distance, 1, rotation);
		}
		Draw.drawScaled(batch,Gallery.tutPanelBorder.get(), x,y-6, width/100f, 3);
		Draw.drawScaled(batch, Gallery.tutPanelMain.get(), x, y, width/100f, height);
		Draw.drawScaled(batch,Gallery.tutPanelBorder.get(), x,y+height+6, width/100f, -3);
		Font.medium.setColor(Colours.withAlpha(Colours.light,alpha));
		Font.medium.drawWrapped(batch, str, x+offset, y+5, width-offset*2, HAlignment.LEFT);


		if(special==1){
			Draw.draw(batch, Gallery.greenHP[1].get(), 491, 161);
			Draw.draw(batch, Gallery.greenHP[2].get(), 603, 161);
		}
		if(special==2){
			Draw.draw(batch, Gallery.iconTargeted.get(), 591,154);
		}
		batch.setColor(1,1,1,1);
	}



	@Override
	public void update(float delta) {

	}


	public enum Trigger{PlayerShieldPhase, CheckList, PlayerWeaponPhase}
	public enum Effect{EnemyPlayCards, ShieldGenList, DrawFirstHand, PlayWeaponList, ShieldMajorList, hideNameWisps, TargetGeneratorList, DrawTargeted, drawTwoShields, LotsShieldList, End, UnscrambleList, DrawMoreTeslas, ShowPlayerNames, HighlightTargetedPulse}
	public static ArrayList<Tutorial> tutorials=new ArrayList<Tutorial>();
	public static void init(){

		enemy=Battle.getEnemy();
		player=Battle.getPlayer();
		add("This is your ship.\n(click to continue)");
		add("It has five modules.", Effect.ShowPlayerNames);
		add("They make up a deck of cards that you use to fight!");
		add("The enemy ship is playing a weapon card to attack you!", Trigger.PlayerShieldPhase, Effect.EnemyPlayCards);
		add("These orange blips show that you have incoming damage on your generator.", new Pair(43,440));
		add("Shield cards give you shield points to spend when you play them.", Effect.DrawFirstHand);
		add("This card gives two shield points.", new Pair(Main.width/2, 451));
		add("", Trigger.CheckList, Effect.ShieldGenList);
		add("", Trigger.PlayerWeaponPhase);
		add("Now it's your turn to fight back.", Effect.hideNameWisps);
		add("Cards have an energy cost.", new Pair(475,484));
		add("And some cards have a cooldown, which means you must wait before playing another card from that module.",new Pair(579,484));
		add("Weapon and shield cards have bars which show how much damage or shielding they provide.", new Pair(528,521));
		add("", Trigger.CheckList, Effect.PlayWeaponList);
		add("", Trigger.PlayerShieldPhase);
		add("If you take enough damage to cover a \n[   ] or a [   ], you take a major damage and something bad happens!", 1);
		add("If a ship takes 5 total major damage, it is defeated.");
		add("You are about to take major damage, shield it all.");
		add("", Trigger.CheckList, Effect.ShieldMajorList);
		add("", Trigger.PlayerWeaponPhase);
		add("All cards have a special alternate side.", Effect.DrawTargeted);
		add("You can choose which side to play by right-clicking the card.");
		add("The special side of this weapon has the target symbol [  ], which means you can pick a target rather than it hitting a random enemy module.", Effect.HighlightTargetedPulse, 2);
		add("", Trigger.CheckList, Effect.TargetGeneratorList);
		add("", Trigger.PlayerShieldPhase);
		add("You're about to take three major damage!", Effect.drawTwoShields);
		add("", Trigger.CheckList, Effect.LotsShieldList);
		add("", Trigger.PlayerWeaponPhase, Effect.DrawMoreTeslas);
		add("Your tesla module has been scrambled because it took major damage.");
		add("Click on a scrambled card to repair it, that card will be free but will not fire.");
		add("", Trigger.CheckList, Effect.UnscrambleList);
		add("", Trigger.PlayerShieldPhase);
		add("A turn is made up of a shield phase followed by a weapon phase.");
		add("At the beginning of each turn, you draw to your maximum hand size.");
		add("And gain your income in energy",new Pair(320,404));
		add("You're good to go! If there's anything else you don't understand, press tab.");
		add("", Effect.End);
		for(Tutorial p:tutorials){
			p.deactivate();
		}
		//tutorials.get(0).bonktivate();
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
	public static void add(String s, Effect e, int special){
		Tutorial tut= new Tutorial(s, null, e);
		tut.special=special;
		tutorials.add(tut);

	}
	public static void add(String s, Effect e){
		tutorials.add(new Tutorial(s, null, e));
	}
	public static void add(String s, Trigger t, Effect e){
		tutorials.add(new Tutorial(s, t, e));
	}
	public static void add(String s, Pair point){
		tutorials.add(new Tutorial(s, point));
	}
	public static void add(String s, Pic pic, Pair location){
		Tutorial t= new Tutorial(s,null,null);
		t.myGlows.add(new PicLoc(pic, location, null));
		tutorials.add(t);
	}




	public static void next(){
	
		if(!Battle.isTutorial()){
			for(Tutorial t:three){
				t.fadeOut(.33f, Interp.LINEAR);
			}
			three.clear();
			return;
		}
		glows.clear();

		System.out.println("next");
		Tutorial t= tutorials.get(index);


		if(!t.triggered&&t.trig!=null){
			switch(t.trig){
			case PlayerShieldPhase:
				
				Battle.advance();
				if(Battle.getPhase()!=Phase.ShieldPhase){

					return;
				}

				break;
			case CheckList:
				if(!currentList.tasks[currentList.tasks.length-1].isDone())return;
				break;
			case PlayerWeaponPhase:
				if(Battle.getPhase()!=Phase.WeaponPhase)return;
				break;
			}
		}
		t.triggered=true;

		if(currentList!=null){
			if(tutorials.indexOf(currentList.tutorial)<index){
				currentList.fadeOut(.33f, Interp.LINEAR);
				currentList=null;
			}
		}
		//t.fadeOut(3, Interp.LINEAR);
		index++;
		t=tutorials.get(index);

		//t.alpha=1;
		if(t.str.length()>2)t.activate();


		if(!t.activated&&t.eff!=null){
			switch(t.eff){
			case End:
				
				Battle.setTutorial(false);
				player.drawToMaximum();
				enemy.drawToMaximum();
				for(Card c:enemy.hand)c.remakeCard(1);
				for(Component c:player.getRandomisedModules()){
					c.removeSramble();
				}
				tutorials.remove(0).fadeOut(.33f, Interp.LINEAR);
				break;
			case EnemyPlayCards:
				Battle.zSet(Phase.EnemyWeaponPhase);
				Battle.getEnemy().enemyPickAllCards(Battle.getPhase());
				break;
			case DrawTargeted:

				boolean cardFound=false;
				for(Card c:player.hand){
					if(c.mod.getClass()==Pulse.class){
						c.remakeCard(6);
						targetedWeaponCard=c;
						cardFound=true;
						break;
					}
				}
				if(!cardFound){
					targetedWeaponCard=player.getComponent(0).getCard(6);
					player.drawCard(targetedWeaponCard);
				}
				for(Card c:player.hand){
					if(c.getName(1).equals("")){
						c.remakeCard(3);
					}
				}


				Card c=enemy.getComponent(1).getCard(5);
				c.getCode(1).clear();
				c.getCode(1).add(Special.Targeted);
				c.getCode(1).add(Special.BonusVsModule0);
				c.getCode(1).add(AI.BetterAgainstSpecificSystem);
				c.zSetEffect(4);
				enemy.drawCard(c);
				Card c1=enemy.getComponent(1).getCard(5);
				c1.getCode(1).clear();
				c1.getCode(1).add(Special.Targeted);
				c1.getCode(1).add(Special.BonusVsModule0);
				c1.getCode(1).add(AI.BetterAgainstSpecificSystem);
				c1.zSetEffect(4);
				enemy.drawCard(c1);
				Card c2=enemy.getComponent(1).getCard(5);
				c2.getCode(1).clear();
				c2.getCode(1).add(Special.Targeted);
				c2.getCode(1).add(Special.BonusVsModule1);
				c2.getCode(1).add(AI.BetterAgainstSpecificSystem);
				c2.zSetEffect(6);
				enemy.drawCard(c2);
				Card c3=enemy.getComponent(1).getCard(5);
				c3.getCode(1).clear();
				c3.getCode(1).add(Special.Targeted);
				c3.getCode(1).add(Special.BonusVsModule1);
				c3.getCode(1).add(AI.BetterAgainstSpecificSystem);
				c3.zSetEffect(6);
				enemy.drawCard(c3);
				overrideStopFlip=true;
				for(Component com:enemy.components)com.getStats().showNameWisp();
				break;
			case ShieldGenList:
				currentList=new Checklist(t, new Task[]{
						new Task("Play the shield card\n(click to play)",TaskType.PlayShield, firstShieldCard, 0), 
						new Task("Use both shield points to protect your generator by clicking on it twice \n(on the left)",TaskType.ShieldGen, Gallery.statsGenerator, new Pair(0,420)),
						new Task("Click the central blue shield button to confirm", TaskType.EndShieldPhase, true)
				});
				break;

			case DrawFirstHand:
				firstShieldCard=Battle.getPlayer().getShield().getCard(5);
				firstWeaponCard=player.getComponent(1).getCard(5);
				player.drawCard(firstWeaponCard);
				player.drawCard(player.getComponent(1).getCard(5));
				Battle.getPlayer().drawCard(firstShieldCard);
				player.drawCard(player.getComponent(0).getCard(5));
				player.drawCard(player.getComponent(0).getCard(5));
				break;
			case PlayWeaponList:
				currentList=new Checklist(t, new Task[]{
						new Task("Play at least one weapon card",TaskType.PlayWeapon), 
						new Task("Click the central red swords icon to confirm",TaskType.EndWeaponPhase, true)
				});
				enemy.drawCard(enemy.getShield().getNextCard());
				enemy.drawCard(enemy.getComponent(1).getCard(5));
				enemy.drawCard(enemy.getComponent(1).getCard(6));
				//enemy.drawCard(enemy.getComponent(1).getCard(6));
				break;
			case ShieldMajorList:
				currentList=new Checklist(t, new Task[]{
						new Task("Prevent all major damage [   ]",TaskType.PreventAllMajor), 
						new Task("Click the blue shield to confirm",TaskType.EndShieldPhase, true)
				});
				currentList.drawDam=true;
				Battle.getPlayer().drawCard(Battle.getPlayer().getShield().getCard(5));
				Battle.getPlayer().drawCard(Battle.getPlayer().getShield().getCard(5));
				Battle.getPlayer().drawCard(Battle.getPlayer().getShield().getCard(5));
				while(player.hand.size()<5){
					player.drawCard(player.getComponent(0).getCard(5));
				}
				break;


			case hideNameWisps:
				for(Component hnwc:player.components)hnwc.getStats().hideNameWisp();
				break;

			case HighlightTargetedPulse:
				t.myGlows.add(new PicLoc(targetedWeaponCard, 1, null));
				break;

			case TargetGeneratorList:
				currentList=new Checklist(t, new Task[]{
						new Task("Right-click to flip the pulse card",TaskType.FlipCard, targetedWeaponCard, 1),
						new Task("Play the alternate side of the pulse card",TaskType.PlayerAlternateSide, targetedWeaponCard, 1), 
						new Task("Target the enemy Generator",TaskType.TargetGenerator, Gallery.statsGenerator, new Pair(Main.width-ModuleStats.width, Main.height/5*3)),
						new Task(TaskType.EndWeaponPhase)
				});
				break;

			case drawTwoShields:
				player.drawCard(player.getShield().getCard(3));
				player.drawCard(player.getShield().getCard(4));
				while(player.hand.size()<5)player.drawCard(player.components[1].getNextCard());
				for(Component dtsc:enemy.components)dtsc.getStats().hideNameWisp();
				break;

			case LotsShieldList:
				currentList=new Checklist(t, new Task[]{
						new Task("Prevent two major damage",TaskType.WeirdPrevent), 
						new Task(TaskType.EndShieldPhase)
				});
				break;
			case UnscrambleList:
				currentList=new Checklist(t, new Task[]{
						new Task("Unscramble your tesla",TaskType.NoneScrambled), 
						new Task("Play one of the other tesla card",TaskType.WeaponPlayed),
						new Task(TaskType.EndWeaponPhase)
				});
				for(Card card:enemy.hand)card.getCode(1).add(AI.Ignore);
				break;
			case DrawMoreTeslas:
				//enemy.drawCard(enemy.getComponent(0).getNextCard());
				enemy.getComputer().maxCards=2;
				enemy.drawCard(enemy.getComponent(1).getNextCard());
				break;
			case ShowPlayerNames:
				for(Component com:player.components)com.getStats().showNameWisp();
				glows.clear();
				
				for(int i=0;i<5;i++){
					t.myGlows.add(new PicLoc(Gallery.statsComputer, new Pair(0,Main.height/5*i), null));
				}
				break;


			}
		}
		t.activated=true;
	}

	public static boolean stopFlip(){
		if(!Battle.isTutorial())return false;
		return !overrideStopFlip;
	}
	public static boolean stopClick(){
		if(!Battle.isTutorial())return false;
		if(currentList!=null)return false;
		return !overrideStopClick;
	}
	public static boolean stopEnd(){
		if(currentList!=null){
			if(!currentList.isCurrent()){
				return true;
			}
		}
		if(!Battle.isTutorial()){
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
		three.add(new Tutorial("The cycle button is for getting rid of cards you don't want to get ones you need. It costs more and more energy each time you use it", new Pair(197,365)));
		three.add(new Tutorial("Absorb: [effect] means if the shield is used, you gain the effect", Main.width/5f));
		three.add(new Tutorial("Augment [cardtype]: [effect] means you play this to add the effect to another card of the right cardtype",Main.width/5f*4f));
	}

	public static void renderAll(SpriteBatch batch) {
		for(Tutorial t:three){
			t.render(batch);
		}
		
		if(index==36)return;
		for(PicLoc pl:glows)pl.render(batch);

		if(currentList!=null){
			currentList.render(batch);
		}

		if(tutorials.size()>0){
			Tutorial t= tutorials.get(index);
			if(t.myGlows!=null){
				for(PicLoc pl:t.myGlows){
					pl.render(batch);
				}
			}
			t.render(batch);
			
		}
		if(undoVisible()){
			UndoButton.get().render(batch);
		}
		


	}

	public static boolean undoVisible(){
		if(!Battle.isTutorial())return false;
		if(index==0||index>=36)return false;
		
		if(currentList!=null&&currentList.isCurrent())return true;
			
		
		if(tutorials.get(index-1).str.equals("")||tutorials.get(index).str.equals(""))return false;
		
		return true;
	}
	
	@SuppressWarnings("incomplete-switch")
	public static void goBack() {
		System.out.println("going back");
		if(index==0)return;
		Tutorial t=tutorials.get(index-1);
		if(t==null)return;
		if(t.str.equals(""))return;
		if(t.eff!=null){
			switch(t.eff){
			case LotsShieldList:
				return;
			case PlayWeaponList:
				return;
			case ShieldGenList:
				return;
			case ShieldMajorList:
				return;
			case TargetGeneratorList:
				return;
			case UnscrambleList:
				return;

			}
		}
		glows.clear();
		
		t.alpha=1;
		index--;
		getCurrentTutorial();
		glows.addAll(Tutorial.glows);
	}

	public static Tutorial getCurrentTutorial() {
		return tutorials.get(index);
	}
}
