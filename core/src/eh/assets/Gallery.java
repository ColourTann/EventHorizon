package eh.assets;

public class Gallery {
	
	//SHIPS//
	public static Pic shipAurora=new Pic("Ship/Aurora/ship");
	public static Pic auroraGenerator= new Pic("Ship/Aurora/generator");
	public static Pic auroraComputer= new Pic("Ship/Aurora/computer");
	
	public static Pic shipComet=new Pic("Ship/Comet/ship");
	public static Pic cometGenerator= new Pic("Ship/Comet/generator");
	public static Pic cometComputer= new Pic("Ship/Comet/computer");
	
	public static Pic shipNova=new Pic("Ship/Nova/ship");
	public static Pic novaGenerator= new Pic("Ship/Nova/generator");
	public static Pic novaComputer= new Pic("Ship/Nova/computer");
	
	public static Pic shipEclipse=new Pic("Ship/Eclipse/ship");
	public static Pic eclipseGenerator= new Pic("Ship/Eclipse/generator");
	public static Pic eclipseComputer= new Pic("Ship/Eclipse/computer");
	
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
	
	//WEAPONS//
	public static Pic blaster=new Pic("Module/Weapon/Blaster/module");
	public static Pic[] blasterCard= new Pic[5];
	public static Pic laser = new Pic("Module/Weapon/Laser/module");
	public static Pic[] laserCard= new Pic[5];
	public static Pic pulse = new Pic("Module/Weapon/Pulse/module");
	public static Pic[] pulseCard= new Pic[5];
	public static Pic ray = new Pic("Module/Weapon/Ray/module");
	public static Pic[] rayCard= new Pic[5];
	public static Pic tesla = new Pic("Module/Weapon/Tesla/module");
	public static Pic[] teslaCard= new Pic[5];
	
	//SHIELDS//
	public static Pic deflector=new Pic("Module/Shield/Deflector/module");
	public static Pic[] deflectorCard= new Pic[5];
	public static Pic repulsor=new Pic("Module/Shield/Repulsor/module");
	public static Pic[] repulsorCard= new Pic[5];
	public static Pic repeller=new Pic("Module/Shield/Repeller/module");
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
	
	
	//INTERFACE STUFF//
	public static Pic battleScreen= new Pic("Interface/mainscreen");
	public static Pic endTurnButton= new Pic("Interface/endturnbutton");
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
	
	public static Pic nothing= new Pic("Interface/nothing");
	public static Pic darkDot= new Pic("Interface/darkdot");
	
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
	public static Pic mapAbilityTractorBeam=new Pic("Map/Abilities/tractor");
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

	
	public static void init(){
		for(int i=0;i<3;i++){
			damageIcon[i]=new Pic("Icon/damageicon"+i);
			shieldIcon[i]=new Pic("Icon/shieldicon"+i);
			greenHP[i]=new Pic("Icon/hpgreen"+i);
			redHP[i]=new Pic("Icon/hpred"+i);
			greyHP[i]=new Pic("Icon/hpgrey"+i);
			laserBody[i]=new Pic("Particle/laserbody"+i);
		}
		
		for(int i=0;i<4;i++){
			cardComputer[i]= new Pic("Module/Computer/card"+i);
			cardGenerator[i]= new Pic("Module/Generator/card"+i);
			orangeHP[i]=new Pic("Icon/hporange"+i);
			blueHP[i]=new Pic("Icon/hpblue"+i);
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
		}
		
	}
	
}

