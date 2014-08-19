package eh.util.assets;

import com.badlogic.gdx.graphics.Color;

import eh.util.Colours;

public class Gallery {
	
	//SHIPS//
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
	
	//CARD STUFF//
	public static Pic cardBase=new Pic("Interface/cardbase");
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
	
	//SHIELDS//
	public static Pic deflector=new Pic("Module/Shield/Deflector/module", 3);
	public static Pic[] deflectorCard= new Pic[5];
	public static Pic repulsor=new Pic("Module/Shield/Repulsor/module", 3);
	public static Pic[] repulsorCard= new Pic[5];
	public static Pic repeller=new Pic("Module/Shield/Repeller/module", 3);
	public static Pic[] repellerCard= new Pic[5];
	
	//COMPUTERS//
	public static Pic[] cardComputer= new Pic[4];
	
	//Generators//
	public static Pic[] cardGenerator= new Pic[4];
	
	//MOD STAT STUFF//
	public static Pic baseModuleStats= new Pic("Interface/basestats");
	public static Pic statsGenerator= new Pic("Interface/statsgenerator");
	public static Pic statsWeapon= new Pic("Interface/statsweapon");
	public static Pic statsShield= new Pic("Interface/statsshield");
	public static Pic statsComputer= new Pic("Interface/statscomputer");
	public static Pic statsMoused= new Pic("Interface/statsmoused");
	public static Pic statsTargeted= new Pic("Interface/statstargeted");
	public static Pic statsImmune= new Pic("Interface/statsimmune");
	public static Pic mousedHP=new Pic("Icon/hpmoused");
	public static Pic greenHP[]= new Pic[3];
	public static Pic orangeHP[]= new Pic[4];
	public static Pic redHP[]= new Pic[3];
	public static Pic blueHP[]= new Pic[4];
	public static Pic greyHP[]= new Pic[3];
	public static Pic baseHP[]= new Pic[3];

	private static Color[] greenHPReplace= new Color[]{Colours.baseReplacers[0], Colours.greenHPCols[0], Colours.baseReplacers[1], Colours.greenHPCols[1], Colours.baseReplacers[2], Colours.greenHPCols[2]};
	private static Color[] orangeHPReplace= new Color[]{Colours.baseReplacers[0], Colours.orangeHPCols[0], Colours.baseReplacers[1], Colours.orangeHPCols[1], Colours.baseReplacers[2], Colours.orangeHPCols[2]};
	private static Color[] redHPReplace= new Color[]{Colours.baseReplacers[0], Colours.redHPCols[0], Colours.baseReplacers[1], Colours.redHPCols[1], Colours.baseReplacers[2], Colours.redHPCols[2]};
	private static Color[] greyHPReplace= new Color[]{Colours.baseReplacers[0], Colours.greyHPCols[0], Colours.baseReplacers[1], Colours.greyHPCols[1], Colours.baseReplacers[2], Colours.greyHPCols[2]};
	private static Color[] blueHPReplace= new Color[]{Colours.baseReplacers[0], Colours.blueHPCols[0], Colours.baseReplacers[1], Colours.blueHPCols[1], Colours.baseReplacers[2], Colours.blueHPCols[2]};
	
	

	
	//INTERFACE STUFF//
	public static Pic battleScreen= new Pic("Interface/mainscreen");
	public static Pic endTurnWeapon= new Pic("Interface/endturnweapon");
	public static Pic endTurnShield= new Pic("Interface/endturnshield");
	public static Pic endTurnBottom= new Pic("Interface/endturnbottom");
	
	public static Pic cycleButton= new Pic("Interface/cyclebutton");
	public static Pic playerEnergy = new Pic("Interface/playerenergy");
	public static Pic enemyEnergy = new Pic("Interface/enemyenergy");
	public static Pic majorDamagePlayer= new Pic("Interface/majordamageplayer");
	public static Pic majorDamageEnemy= new Pic("Interface/majordamageenemy");
	public static Pic cardIconPlayer= new Pic("Interface/cardiconplayer");
	public static Pic cardIconEnemy= new Pic("Interface/cardiconenemy");
	public static Pic helpPanel = new Pic("Interface/helppanel");
	
	public static Pic tutPanelBorder= new Pic("Interface/tutpanelborder");
	public static Pic tutPanelMain= new Pic("Interface/tutpanelmain");
	public static Pic tutPoint= new Pic("Interface/tutpoint");
	public static Pic tutUndo= new Pic("Interface/undo");
	
	public static Pic nothing= new Pic("Interface/nothing");
	public static Pic darkDot= new Pic("Interface/darkdot");
	
	public static Pic shitButton= new Pic("Interface/shitbutton"); 
	
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
	public static Pic square1=new Pic("Particle/square1");
	public static Pic square2=new Pic("Particle/square2");
	public static Pic fuzzBall=new Pic("Particle/fuzz");
	public static Pic blackSmoke= new Pic("Particle/smoke");
	public static Pic greySmoke= new Pic("Particle/greysmoke");
	public static Pic[] laserBody=new Pic[3];
	public static Pic lightning= new Pic("Particle/lightning");
	public static Pic lightningEnd= new Pic("Particle/lightningend");
	public static Pic[][] debris = new Pic[2][5];
	
	//Pause stuff//
	public static Pic pauseBase= new Pic("Interface/pausebase");
	
	//Shit//
	public static Pic tomato= new Pic("Shit/tomato");
	public static Pic holes=new Pic("Shit/holes");
	
	public static void init(){
		for(int i=0;i<2;i++){
			fiveIcon[i]=new Pic("Icon/fiveiconoverlay"+i);
			
		}
		for(int i=0;i<3;i++){
			baseHP[i]=new Pic("Icon/hpbase"+i);
			greenHP[i]=new Pic(baseHP[i], greenHPReplace);
			redHP[i]=new Pic(baseHP[i], redHPReplace);
			greyHP[i]=new Pic(baseHP[i], greyHPReplace);
			orangeHP[i]=new Pic(baseHP[i], orangeHPReplace);
			blueHP[i]=new Pic(baseHP[i], blueHPReplace);
			
			laserBody[i]=new Pic("Particle/laserbody"+i);
			damageIcon[i]=new Pic("Icon/damageicon"+i);
			shieldIcon[i]=new Pic("Icon/shieldicon"+i);
		}
		
		for(int i=0;i<4;i++){
			cardComputer[i]= new Pic("Module/Computer/card"+i);
			cardGenerator[i]= new Pic("Module/Generator/card"+i);
		}
		
		for(int i=0;i<5;i++){
			pulseCard[i]=new Pic("Module/Weapon/Pulse/card"+i);
			rayCard[i]=new Pic("Module/Weapon/Ray/card"+i);
			laserCard[i]=new Pic("Module/Weapon/Laser/card"+i);
			blasterCard[i]=new Pic("Module/Weapon/Blaster/card"+i);
			teslaCard[i]=new Pic("Module/Weapon/Tesla/card"+i);
			
			deflectorCard[i]=new Pic("Module/Shield/Deflector/card"+i);
			repulsorCard[i]=new Pic("Module/Shield/Repulsor/card"+i);
			repellerCard[i]=new Pic("Module/Shield/Repeller/card"+i);
			
			debris[0][i]=new Pic("Ship/Debris/s"+i);
			debris[1][i]=new Pic("Ship/Debris/b"+i);
		}
		
		blueHP[3]=new Pic("Icon/hpblue3");
		orangeHP[3]=new Pic("Icon/hporange3");
		redHP[2]=new Pic("Icon/hpred2");
		
	}
	
	
	
}

