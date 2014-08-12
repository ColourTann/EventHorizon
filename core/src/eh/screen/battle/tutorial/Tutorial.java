package eh.screen.battle.tutorial;

import java.util.ArrayList;






import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import eh.Main;
import eh.assets.Font;
import eh.assets.Gallery;
import eh.assets.Pic;
import eh.card.Card;
import eh.card.CardCode.AI;
import eh.card.CardCode.Special;
import eh.module.Module;
import eh.module.utils.ModuleStats;
import eh.module.weapon.Pulse;
import eh.screen.battle.Battle;
import eh.screen.battle.Battle.Phase;
import eh.screen.battle.tutorial.Task.TaskType;
import eh.ship.Ship;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.Timer;
import eh.util.Timer.Interp;
import eh.util.maths.Collider;
import eh.util.maths.Pair;

public class Tutorial extends Bonkject{
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

	static float width=300;
	static float offset=6;
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
		demousectivate();
		this.str=message;
		height=Font.medium.getWrappedBounds(str, width-offset*2).height+14;
		trig=t;
		eff=e;
		y-=height/2;
	}

	public Tutorial(String message, float x){
		demousectivate();
		this.str=message;
		height=Font.medium.getWrappedBounds(str, width-offset*2).height+14;
		y-=height/2;
		this.x=x-width/2;
	}

	public Tutorial(String message, Pair point) {
		demousectivate();
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
			Draw.drawTextureRotatedScaled(batch, Gallery.tutPoint.get(), origin.x, origin.y, distance, 1, rotation);
		}
		Draw.drawTextureScaled(batch,Gallery.tutPanelBorder.get(), x,y-6, 3, 3);
		Draw.drawTextureScaled(batch, Gallery.tutPanelMain.get(), x, y, 3, height);
		Draw.drawTextureScaled(batch,Gallery.tutPanelBorder.get(), x,y+height+6, 3, -3);
		Font.medium.setColor(Colours.withAlpha(Colours.light,alpha));
		Font.medium.drawWrapped(batch, str, x+offset, y+5, width-offset*2, HAlignment.CENTER);


		if(special==1){
			Draw.drawTexture(batch, Gallery.greenHP[1].get(), 503, 152);
			Draw.drawTexture(batch, Gallery.greenHP[2].get(), 615, 152);
		}
		if(special==2){
			Draw.drawTexture(batch, Gallery.iconTargeted.get(), 727,136);
		}
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
	public enum Effect{EnemyPlayCards, ShieldGenList, DrawFirstHand, PlayWeaponList, ShieldMajorList, hideNameWisps, TargetGeneratorList, DrawTargeted, drawTwoShields, LotsShieldList, End, UnscrambleList, DrawMoreTeslas, ShowPlayerNames, HighlightTargetedPulse}
	public static ArrayList<Tutorial> tutorials=new ArrayList<Tutorial>();
	public static void init(){

		enemy=Battle.enemy;
		player=Battle.player;
		add("This is your ship.\n(click to continue)", Gallery.shipNova, new Pair(167,90));
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
		add("And some cards have a cooldown, which means you must wait before playing another card from this module.",new Pair(579,484));
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
			p.debonktivate();
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
		System.out.println("nexting");
		if(!Battle.tutorial){
			for(Tutorial t:three){
				t.fadeOut(3, Interp.LINEAR);
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
				currentList.fadeOut(3, Interp.LINEAR);
				currentList=null;
			}
		}
		//t.fadeOut(3, Interp.LINEAR);
		index++;
		t=tutorials.get(index);

		//t.alpha=1;
		if(t.str.length()>2)t.bonktivate();


		if(!t.activated&&t.eff!=null){
			switch(t.eff){
			case End:
				Battle.tutorial=false;
				player.drawToMaximum();
				enemy.drawToMaximum();
				for(Module m:player.getRandomisedModules()){
					m.removeSramble();
				}
				tutorials.remove(0).fadeOut(3, Interp.LINEAR);
				break;
			case EnemyPlayCards:
				Battle.zSet(Phase.EnemyWeaponPhase);
				Battle.enemy.enemyPickAllCards(Battle.getPhase());
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
					targetedWeaponCard=player.getModule(0).getCard(6);
					player.drawCard(targetedWeaponCard);
				}
				for(Card c:player.hand){
					if(c.getName(1).equals("")){
						c.remakeCard(3);
					}
				}


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
				for(Module m:enemy.modules)m.getStats().showNameWisp();
				break;
			case ShieldGenList:
				currentList=new Checklist(t, new Task[]{
						new Task("Play the shield card\n(click to play)",TaskType.PlayShield, firstShieldCard, 0), 
						new Task("Use both shield points on your generator by clicking on it twice",TaskType.ShieldGen, Gallery.statsGenerator, new Pair(0,420)),
						new Task("Click the blue shield button to confirm", TaskType.EndShieldPhase, true)
				});
				break;

			case DrawFirstHand:
				firstShieldCard=Battle.player.getShield().getCard(5);
				firstWeaponCard=player.getModule(1).getCard(5);
				player.drawCard(firstWeaponCard);
				player.drawCard(player.getModule(1).getCard(5));
				Battle.player.drawCard(firstShieldCard);
				player.drawCard(player.getModule(0).getCard(5));
				player.drawCard(player.getModule(0).getCard(5));
				break;
			case PlayWeaponList:
				currentList=new Checklist(t, new Task[]{
						new Task("Play at least one weapon card",TaskType.PlayWeapon), 
						new Task("Click the red crossed swords to confirm",TaskType.EndWeaponPhase, true)
				});
				enemy.drawCard(enemy.getShield().getNextCard());
				enemy.drawCard(new Card(enemy.getModule(1),5));
				enemy.drawCard(new Card(enemy.getModule(1),6));
				break;
			case ShieldMajorList:
				currentList=new Checklist(t, new Task[]{
						new Task("Prevent all major damage [   ]",TaskType.PreventAllMajor), 
						new Task("Click the blue shield to confirm",TaskType.EndShieldPhase, true)
				});
				currentList.drawDam=true;
				Battle.player.drawCard(Battle.player.getShield().getCard(5));
				Battle.player.drawCard(Battle.player.getShield().getCard(5));
				Battle.player.drawCard(Battle.player.getShield().getCard(5));
				while(player.hand.size()<5){
					player.drawCard(player.getModule(0).getCard(5));
				}
				break;


			case hideNameWisps:
				for(Module m:player.modules)m.getStats().hideNameWisp();
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
				while(player.hand.size()<5)player.drawCard(player.modules[1].getNextCard());
				for(Module m:enemy.modules)m.getStats().hideNameWisp();
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
				break;
			case DrawMoreTeslas:
				enemy.drawCard(enemy.getModule(0).getNextCard());
				enemy.drawCard(enemy.getModule(1).getNextCard());
				break;
			case ShowPlayerNames:
				for(Module m:player.modules)m.getStats().showNameWisp();
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
		if(!Battle.tutorial)return false;
		return !overrideStopFlip;
	}
	public static boolean stopClick(){
		if(!Battle.tutorial)return false;
		if(currentList!=null)return false;
		return !overrideStopClick;
	}
	public static boolean stopEnd(){
		if(currentList!=null){
			if(!currentList.isCurrent()){
				return true;
			}
		}
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
		if(undoVisible())UndoButton.get().render(batch);

	}

	public static boolean undoVisible(){
		if(!Battle.tutorial)return false;
		if(index==0||index>=36)return false;
		
		if(currentList!=null&&currentList.isCurrent())return true;
			
		
		if(tutorials.get(index-1).str.equals("")||tutorials.get(index).str.equals(""))return false;
		
		return true;
	}
	
	public static void goBack() {
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
		glows.addAll(getCurrentTutorial().glows);
	}

	public static Tutorial getCurrentTutorial() {
		return tutorials.get(index);
	}
}
