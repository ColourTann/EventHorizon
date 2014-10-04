package game.assets;


import util.Colours;
import util.image.Pic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;



public class Gallery {
	
	//Ships//
	
	public static Pic shipAurora=new Pic("Ship/Aurora/ship", 3);
	public static Pic auroraGenerator= new Pic("Ship/Aurora/generator", 3);
	public static Pic auroraComputer= new Pic("Ship/Aurora/computer", 3);
	
	public static Pic shipComet=new Pic("Ship/Comet/ship", 3);
	public static Pic cometGenerator= new Pic("Ship/Comet/generator", 3);
	public static Pic cometComputer= new Pic("Ship/Comet/computer", 3);
	
	public static Pic shipNova=new Pic("Ship/Nova/ship", 3);
	public static Pic novaGenerator= new Pic("Ship/Nova/generator", 3);
	public static Pic novaComputer= new Pic("Ship/Nova/computer", 3);
	
	public static Pic shipEclipse=new Pic("Ship/Eclipse/ship", 3);
	public static Pic eclipseGenerator= new Pic("Ship/Eclipse/generator", 3);
	public static Pic eclipseComputer= new Pic("Ship/Eclipse/computer", 3);
	
	public static Pic shipHornet=new Pic("Ship/Hornet/ship", 3);
	public static Pic hornetGenerator= new Pic("Ship/Hornet/generator", 3);
	public static Pic hornetComputer= new Pic("Ship/Hornet/computer", 3);
	
	//CARD STUFF//
	public static Pic cardBase=new Pic("Interface/cardbase");
	public static Pic cardBaseConsumable=new Pic("Interface/cardbaseconsumable");
	public static Pic cardOutline=new Pic("Interface/cardoutline");
	public static Pic iconEnergy=new Pic("Icon/energy");
	public static Pic iconCooldown=new Pic("Icon/cooldown");
	public static Pic iconShots=new Pic("Icon/multipleshot");
	public static Pic iconTargeted=new Pic("Icon/targeted");
	public static Pic iconJammed=new Pic("Icon/jammed");
	public static Pic iconIncreasedEffect=new Pic("Icon/increasedeffect");
	public static Pic damageIcon[] = new Pic[3];
	public static Pic shieldIcon[] = new Pic[3];
	public static Pic fiveIcon[]= new Pic[2];
	
	//WEAPONS//
	public static Pic blaster=new Pic("Module/Weapon/Blaster/module", 3);
	public static Pic[] blasterCard= new Pic[5];
	public static Pic laser = new Pic("Module/Weapon/Laser/module", 3);
	public static Pic[] laserCard= new Pic[5];
	public static Pic pulse = new Pic("Module/Weapon/Pulse/module", 3);
	public static Pic[] pulseCard= new Pic[5];
	public static Pic ray = new Pic("Module/Weapon/Ray/module", 3);
	public static Pic[] rayCard= new Pic[5];
	public static Pic tesla = new Pic("Module/Weapon/Tesla/module", 3);
	public static Pic[] teslaCard= new Pic[5];
	public static Pic swift= new Pic("Module/Weapon/Swift/module", 3);
	public static Pic[] swiftCard= new Pic[5];
	
	//SHIELDS//
	public static Pic deflector=new Pic("Module/Shield/Deflector/module", 3);
	public static Pic[] deflectorCard= new Pic[5];
	public static Pic repulsor=new Pic("Module/Shield/Repulsor/module", 3);
	public static Pic[] repulsorCard= new Pic[5];
	public static Pic repeller=new Pic("Module/Shield/Repeller/module", 3);
	public static Pic[] repellerCard= new Pic[5];
		
	//Computers//
	public static Pic[] cardComputer= new Pic[4];
	
	//Generators//
	public static Pic[] cardGenerator= new Pic[4];
	
	//UTILITY//
	
	public static Pic[] arcSocket= new Pic[2];
	public static Pic[] exploiter= new Pic[2];
	public static Pic[] furnace= new Pic[2];
	public static Pic[] particleCore= new Pic[2];
	public static Pic[] phaseArray= new Pic[2];
	public static Pic[] repeater= new Pic[2];
	//public static Pic armour=new Pic("Module/Utility/cycle");
	
	//Armour//
	public static Pic basicArmour=new Pic("Module/Utility/Armour/basicarmour");
	public static Pic chargedHull= new Pic("Module/Utility/Armour/chargedhull");
	public static Pic repair=new Pic("Module/Utility/Armour/repair");
	public static Pic crystalLattice =new Pic("Module/Utility/Armour/crystallattice");
	public static Pic galvanicSkin =new Pic("Module/Utility/Armour/galvanicskin");
	public static Pic organicShell =new Pic("Module/Utility/Armour/organicshell");
	public static Pic voltaicCarapce =new Pic("Module/Utility/Armour/voltaiccarapace");
	
	//Consumables//
	
	public static Pic phase= new Pic("ConsumableCard/phase");
	public static Pic flow= new Pic("ConsumableCard/flow");
	public static Pic inverseSort= new Pic("ConsumableCard/inversesort");
	public static Pic radialSort= new Pic("ConsumableCard/radialsort");
	public static Pic consume= new Pic("ConsumableCard/consume");
	public static Pic ignite= new Pic("ConsumableCard/ignite");
	public static Pic refresh= new Pic("ConsumableCard/refresh");
	public static Pic kindle= new Pic("ConsumableCard/kindle");
	public static Pic swarm= new Pic("ConsumableCard/swarm");
	public static Pic replicate= new Pic("ConsumableCard/replicate");
	public static Pic bomb= new Pic("ConsumableCard/bomb");
	public static Pic magnify= new Pic("ConsumableCard/magnify");
	
	//MOD STAT STUFF//
	public static Pic baseModuleStats= new Pic("Interface/statsbase");
	public static Pic statsGenerator= new Pic("Interface/statsgenerator");
	public static Pic statsWeapon= new Pic("Interface/statsweapon");
	public static Pic statsShield= new Pic("Interface/statsshield");
	public static Pic statsComputer= new Pic("Interface/statscomputer");
	public static Pic statsMoused= new Pic("Interface/statsmoused");
	public static Pic statsTargeted= new Pic("Interface/statstargeted");
	public static Pic statsImmune= new Pic("Interface/statsimmune");
	public static Pic mousedHP=new Pic("Icon/hpmoused");
	public static Pic greenHP[]= new Pic[5];
	public static Pic orangeHP[]= new Pic[6];
	public static Pic redHP[]= new Pic[6];
	public static Pic blueHP[]= new Pic[6];
	public static Pic greyHP[]= new Pic[5];
	public static Pic baseHP[]= new Pic[5];

	private static Color[] greenHPReplace= new Color[]{Colours.baseReplacers[0], Colours.greenHPCols[0], Colours.baseReplacers[1], Colours.greenHPCols[1], Colours.baseReplacers[2], Colours.greenHPCols[2]};
	private static Color[] orangeHPReplace= new Color[]{Colours.baseReplacers[0], Colours.orangeHPCols[0], Colours.baseReplacers[1], Colours.orangeHPCols[1], Colours.baseReplacers[2], Colours.orangeHPCols[2]};
	private static Color[] redHPReplace= new Color[]{Colours.baseReplacers[0], Colours.redHPCols[0], Colours.baseReplacers[1], Colours.redHPCols[1], Colours.baseReplacers[2], Colours.redHPCols[2]};
	private static Color[] greyHPReplace= new Color[]{Colours.baseReplacers[0], Colours.greyHPCols[0], Colours.baseReplacers[1], Colours.greyHPCols[1], Colours.baseReplacers[2], Colours.greyHPCols[2]};
	private static Color[] blueHPReplace= new Color[]{Colours.baseReplacers[0], Colours.blueHPCols[0], Colours.baseReplacers[1], Colours.blueHPCols[1], Colours.baseReplacers[2], Colours.blueHPCols[2]};
	
	private static Color[] playerToEnemyColours=new Color[]{Colours.player2[0], Colours.enemy2[0], Colours.player2[1], Colours.enemy2[1]};

	
	//INTERFACE STUFF//
	public static Pic battleScreen= new Pic("Interface/mainscreen");
	public static Pic endTurnWeapon= new Pic("Interface/endturnweapon");
	public static Pic endTurnShield= new Pic("Interface/endturnshield");
	public static Pic endTurnBottom= new Pic("Interface/endturnbottom");
	
	public static Pic cycleButton= new Pic("Interface/cyclebutton");
	public static Pic playerEnergy = new Pic("Interface/playerenergy");
	public static Pic enemyEnergy = new Pic("Interface/enemyenergy");
	
	public static Pic majorDamagePlayer= new Pic("Interface/majordamageplayer");
	public static Pic majorDamageEnemy= new Pic(majorDamagePlayer, playerToEnemyColours);
	public static Pic majorDamagePlayerPlus= new Pic("Interface/majordamageplayerplus");
	public static Pic majorDamageEnemyPlus= new Pic(majorDamagePlayerPlus, playerToEnemyColours);
	
	public static Pic cardIconPlayer= new Pic("Interface/cardiconplayer");
	public static Pic cardIconEnemy= new Pic("Interface/cardiconenemy");
	public static Pic helpPanelMid = new Pic("Interface/helppanelmid");
	public static Pic helpPanelEdge = new Pic("Interface/helppaneledge");
	
	public static Pic tutPanelBorder= new Pic("Interface/tutpanelborder");
	public static Pic tutPanelMain= new Pic("Interface/tutpanelmain");
	public static Pic tutPoint= new Pic("Interface/tutpoint");
	public static Pic tutUndo= new Pic("Interface/undo");
	
	public static Pic nothing= new Pic("Interface/nothing");
	public static Pic darkDot= new Pic("Interface/darkdot");
	
	public static Pic shitButton= new Pic("Interface/shitbutton");
	public static Pic tickButton= new Pic("Interface/tickbutton");
	public static Pic leftButton= new Pic("Interface/leftbutton");
	public static Pic rewardOutline= new Pic("Interface/rewardoutline");
	public static Pic rewardHighlights= new Pic("Interface/rewardhighlights");
	public static Pic threeCards= new Pic("Interface/threecards");
	public static Pic energyDial= new Pic("Interface/energydial");
	public static Pic energyMeter= new Pic("Interface/energymeter");
	public static Pic bonusPool= new Pic("Interface/bonuspool");
	
	
	public static Pic difficultyMeter= new Pic("Interface/difficultymeter");
	public static Pic difficultyDial= new Pic("Interface/difficultydial");
	public static Pic fightButton= new Pic("Interface/fightbutton");
	
	//Map stuff//
	public static Pic mapslice= new Pic("Interface/mapslice");
	public static Pic mapsliceRight= new Pic("Interface/mapsliceright");
	public static Pic star=new Pic("Map/star");
	
	//Map Abilities//
	//Gen//
	public static Pic mapAbilityTeleport=new Pic("Map/Abilities/teleport");
	public static Pic mapAbilityDoubleMove=new Pic("Map/Abilities/doublemove");
	public static Pic mapAbilityDiagonalMove=new Pic("Map/Abilities/diagonalmove");
	//Com//
	public static Pic mapAbilityForceField=new Pic("Map/Abilities/forcefield");
	public static Pic mapAbilityPull=new Pic("Map/Abilities/pull");
	public static Pic mapAbilityPush=new Pic("Map/Abilities/push");
	public static Pic mapAbilityCloak=new Pic("Map/Abilities/cloak");
	
	//PARTICLE STUFF//
	public static Pic circle32=new Pic("Particle/circle32");
	public static Pic fuzzBall=new Pic("Particle/fuzz");
	public static Pic blackSmoke= new Pic("Particle/smoke");
	public static Pic greySmoke= new Pic("Particle/greysmoke");
	public static Pic[] laserBody=new Pic[3];
	public static Pic lightning= new Pic("Particle/lightning");
	public static Pic lightningEnd= new Pic("Particle/lightningend");
	public static Pic[][] debris = new Pic[2][5];
	public static Pic[] shipDamage = new Pic[9];
	public static Pic shieldEffect= new Pic("Particle/shield");
	public static Pic swiftParticle= new Pic("Particle/swiftparticle");
	public static Pic rocket = new Pic("Particle/rocket");
	
	//Pause stuff//
	public static Pic pauseBase= new Pic("Interface/pausebase");
	
	//Shit//
	public static Pic tomato= new Pic("Shit/tomato");
	public static Pic holes=new Pic("Shit/holes");
	
	//Runtime stuff//
	public static Pic whiteSquare;
	
	
	
	public static void init(){
		for(int i=0;i<2;i++){
			fiveIcon[i]=new Pic("Icon/fiveiconoverlay"+i);
			
			arcSocket[i]=new Pic("Module/Utility/ArcSocket/card"+i);
			exploiter[i]=new Pic("Module/Utility/Exploiter/card"+i);
			furnace[i]=new Pic("Module/Utility/Furnace/card"+i);
			particleCore[i]=new Pic("Module/Utility/ParticleCore/card"+i);
			phaseArray[i]=new Pic("Module/Utility/PhaseArray/card"+i);
			repeater[i]=new Pic("Module/Utility/Repeater/card"+i);
			
		}
		for(int i=0;i<3;i++){
			
			laserBody[i]=new Pic("Particle/laserbody"+i);
			damageIcon[i]=new Pic("Icon/damageicon"+i);
			shieldIcon[i]=new Pic("Icon/shieldicon"+i);
		}
	
		
		for(int i=0;i<4;i++){
			
			cardComputer[i]= new Pic("Module/Computer/card"+i);
			cardGenerator[i]= new Pic("Module/Generator/card"+i);
		}
		
		for(int i=0;i<5;i++){
		
			baseHP[i]=new Pic("Icon/hpbase"+i);
			greenHP[i]=new Pic(baseHP[i], greenHPReplace);
			redHP[i]=new Pic(baseHP[i], redHPReplace);
			greyHP[i]=new Pic(baseHP[i], greyHPReplace);
			orangeHP[i]=new Pic(baseHP[i], orangeHPReplace);
			blueHP[i]=new Pic(baseHP[i], blueHPReplace);
			pulseCard[i]=new Pic("Module/Weapon/Pulse/card"+i);
			rayCard[i]=new Pic("Module/Weapon/Ray/card"+i);
			swiftCard[i]=new Pic("Module/Weapon/Swift/card"+i);
			laserCard[i]=new Pic("Module/Weapon/Laser/card"+i);
			blasterCard[i]=new Pic("Module/Weapon/Blaster/card"+i);
			teslaCard[i]=new Pic("Module/Weapon/Tesla/card"+i);
			
			deflectorCard[i]=new Pic("Module/Shield/Deflector/card"+i);
			repulsorCard[i]=new Pic("Module/Shield/Repulsor/card"+i);
			repellerCard[i]=new Pic("Module/Shield/Repeller/card"+i);
			
			debris[0][i]=new Pic("Ship/Debris/s"+i);
			debris[1][i]=new Pic("Ship/Debris/b"+i);
		}
		for(int i=0;i<9;i++){
			shipDamage[i]=new Pic("Ship/Damage/"+i,3);
		}
		
		blueHP[5]=new Pic("Icon/hpblue3");
		orangeHP[5]=new Pic("Icon/hporange3");
		redHP[5]=new Pic("Icon/hpred2");
		
		Pixmap map = new Pixmap(1,1,Format.RGBA8888);
		map.setColor(1, 1, 1, 1);
		map.drawPixel(0, 0);
		whiteSquare=new Pic(new Texture(map));
		
	}
	
	
	
	
}

