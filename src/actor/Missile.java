package actor;

import graphics.Model;
import graphics.particles.*;

import math.*;

public class Missile extends Projectile {
    private static final long serialVersionUID = -8381240274687476481L;
    protected final float MISSILE_SPEED;
    protected int MISSILE_DAMAGE = 100;

    protected static final String MODEL_NAME = Model.Models.MISSILE;
    private static final String SHOOT_EFFECT = "missile_firing.wav";
    private static final String DEATH_EFFECT = "explode.wav";

    private static final float EFFECT_VOLUME = 10f;

    public Missile(Actor actor,float speed,int multiplier){
        super(actor);
        this.MISSILE_SPEED = speed;

        if(multiplier != 0) {
            MISSILE_DAMAGE = MISSILE_DAMAGE * multiplier;
        }
        damage = MISSILE_DAMAGE;
        modelName = MODEL_NAME;

        sound.Event effect = new sound.Event(actor.getPosition(), actor.getVelocity(),sound.Library.findByName(SHOOT_EFFECT));
        effect.gain = EFFECT_VOLUME;
        sound.Manager.addEvent(effect);
        velocity.timesEquals(MISSILE_SPEED);
        if(ParticleSystem.isEnabled()){
            particleFountain = new graphics.particles.ParticleFountain<PlasmaParticle>(PlasmaParticle.class,this);
            ParticleSystem.addFountain(particleFountain);
        }
    }


    /**
     * @param actor
     * @param positionOffset the offset relative to the actor
     * @param direction
     */
    public Missile(Actor actor, float speed, int damage, Vector3f positionOffset){
        this(actor,speed,damage);
        position.plusEquals(positionOffset);
    }

    public void die(){
        sound.Event effect = new sound.Event(getPosition(), getVelocity(),sound.Library.findByName(DEATH_EFFECT));
        effect.gain = EFFECT_VOLUME;
        sound.Manager.addEvent(effect);
        
        if(ParticleSystem.isEnabled()){
            for(int i = 0; i < 256; i++){
                ParticleSystem.addParticle( new FireParticle(this,Vector3f.newRandom(1)));
            }
        }
        
        ParticleSystem.removeFountain(particleFountain);
        delete();
    }
}

