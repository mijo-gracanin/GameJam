package com.pixelfarmers.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
	
	@Override
	public void create () {
		setScreen(new GameScreen());
	}
}
