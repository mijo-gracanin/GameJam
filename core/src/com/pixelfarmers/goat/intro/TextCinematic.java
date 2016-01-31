package com.pixelfarmers.goat.intro;


import com.badlogic.gdx.graphics.Camera;

public class TextCinematic extends Cinematic {
    private final String text;
    private final TextDrawer textDrawer;
    private float delay;

    private boolean textDrawn = false;
    private boolean textCleared = false;

    public TextCinematic(float duration, float delay, String text, TextDrawer textDrawer) {
        super(duration);
        this.delay = delay;
        this.text = text;
        this.textDrawer = textDrawer;
    }

    @Override
    protected void updateImpl(float delta, Camera camera) {
        if (!textCleared) {
            textDrawer.clear();
            textCleared = true;
        }
        if (delay > 0) {
            delay -= delta;
            return;
        }
        if (textDrawn) {
            return;
        }
        textDrawn = true;
        textDrawer.drawText(text);
    }
}
