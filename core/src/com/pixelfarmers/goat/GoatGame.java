package com.pixelfarmers.goat;

import com.badlogic.gdx.Game;

public class GoatGame extends Game {
	
	@Override
	public void create() {
		setScreen(new GameScreen(this));
	}

}
