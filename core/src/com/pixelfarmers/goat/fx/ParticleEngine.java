package com.pixelfarmers.goat.fx;


import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pixelfarmers.goat.constants.MessageCode;

import java.util.ArrayList;
import java.util.List;

public class ParticleEngine implements Telegraph {
    private List<Particle> particles;

    public ParticleEngine() {
        particles = new ArrayList<Particle>();
        MessageManager.getInstance().addListeners(this,
                MessageCode.ENEMY_DIED,
                MessageCode.PROJECTILE_HIT_ENEMY,
                MessageCode.SWORD_HIT_ENEMY);
    }

    public void update(float delta) {
        for (Particle particle : particles) {
            particle.update(delta);
        }

        for (int i = 0; i < particles.size(); ) {
            if (!particles.get(i).isAlive()) {
                swapWithLastAndRemove(i, particles);
            } else {
                i++;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Particle particle : particles) {
            particle.render(batch);
        }
    }

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    private void swapWithLastAndRemove(int i, List<Particle> list) {
        if (list.size() == 1) {
            list.remove(0);
        } else {
            int indexOfLast = list.size() - 1;
            Particle last = list.get(indexOfLast);
            list.set(i, last);
            list.remove(indexOfLast);
        }
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        int code = msg.message;
        Vector2 position = (Vector2) msg.extraInfo;
        if (code == MessageCode.ENEMY_DIED) {
            addParticle(new BloodParticle(position.x, position.y));
            return true;
        } else if (code == MessageCode.SWORD_HIT_ENEMY || code == MessageCode.PROJECTILE_HIT_ENEMY) {
            for (int i = 0; i < 4; i++) {
                addParticle(new BloodParticle(position.x, position.y));
            }
            return true;
        }
        return false;
    }

}
