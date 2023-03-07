package net.sekao.superkoalio;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.sekao.superkoalio.MainScreen;

public class MyGdxGame extends Game {
    public SpriteBatch batch;

    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MainMenuScreen(this));
        
    }
}
