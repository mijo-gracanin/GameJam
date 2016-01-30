package com.pixelfarmers.goat.fx;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class ParticleEngine {
    private List<Particle> particles;

    public ParticleEngine() {
        particles = new ArrayList<Particle>();
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
}
