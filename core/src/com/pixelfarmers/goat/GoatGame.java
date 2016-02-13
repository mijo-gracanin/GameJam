package com.pixelfarmers.goat;

import com.badlogic.gdx.Game;
import com.pixelfarmers.goat.screen.MainMenuScreen;

public class GoatGame extends Game {
	
	@Override
	public void create() {
		setScreen(new MainMenuScreen(this));
	}

}
