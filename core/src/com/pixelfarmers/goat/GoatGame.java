package com.pixelfarmers.goat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.pixelfarmers.goat.constants.MessageCode;
import com.pixelfarmers.goat.screen.FinishScreen;
import com.pixelfarmers.goat.screen.GameOverScreen;
import com.pixelfarmers.goat.screen.GameScreen;
import com.pixelfarmers.goat.screen.MainMenuScreen;

public class GoatGame extends Game implements Telegraph {
	
	@Override
	public void create() {
		MessageManager.getInstance().addListeners(this, MessageCode.OPEN_WIN_SCREEN, MessageCode.OPEN_GAMEOVER_SCREEN, MessageCode.OPEN_GAME_SCREEN);
		setScreen(new MainMenuScreen());
	}

	@Override
	public boolean handleMessage(Telegram msg) {
		if(msg.message == MessageCode.OPEN_WIN_SCREEN) {
			setScreen(new FinishScreen());
			return true;
		}

		if(msg.message == MessageCode.OPEN_GAMEOVER_SCREEN) {
			setScreen(new GameOverScreen());
			return true;
		}

		if(msg.message == MessageCode.OPEN_GAME_SCREEN) {
			setScreen(new GameScreen());
			return true;
		}

		return false;
	}
}
