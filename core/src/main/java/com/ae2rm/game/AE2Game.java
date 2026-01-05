package com.ae2rm.game;

import com.badlogic.gdx.Game;
import aeii.Renderer;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class AE2Game extends Game {
    @Override
    public void create() {
        setScreen(new Renderer());
    }
}