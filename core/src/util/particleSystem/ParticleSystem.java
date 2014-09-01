package util.particleSystem;

import java.util.ArrayList;








import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.maths.Pair;

public abstract class ParticleSystem {
	public static ArrayList<ParticleSystem> systems= new ArrayList<ParticleSystem>();
	public static ArrayList<Particle> debugDontUse=new ArrayList<Particle>();
	public float x,y,life,maxLife,ticks;
	public Pair vector;
	public ArrayList<Particle> particles = new ArrayList<Particle>();
	public boolean disabled;
	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);
	protected void updateParticles(float delta){
		for(Particle p:particles){
			p.update(delta);
		}
	}
	protected void renderParticles(SpriteBatch batch){
		for(Particle p:particles){
			p.render(batch);
		}
	}
	public static void updateAll(float delta){
	
		for(int i=0;i<systems.size();i++){
			ParticleSystem sys=systems.get(i);
			if(sys.disabled&&sys.particles.isEmpty()){
				systems.remove(sys);
				i--;
			}
		}
		for(ParticleSystem ps:systems){
			
			for(int i=0;i<ps.particles.size();i++){
				Particle p=ps.particles.get(i);
				if(p.dead){
					ps.particles.remove(p);
					i--;
				}
			}
			ps.update(delta);
			
		}
		for (Particle p:debugDontUse){
			p.update(delta);
		}
	}
	
	public static void renderAll(SpriteBatch batch){
		for(ParticleSystem ps:systems){
			ps.render(batch);
		}
		for (Particle p:debugDontUse){
			p.render(batch);
		}
	}
	public void disable(){
		disabled=true;
	}
	public static void clearAll() {
		systems.clear();
	}
	
	
}
