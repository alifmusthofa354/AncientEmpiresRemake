package aeii;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;

public final class Renderer implements Screen, InputProcessor
{
	public static boolean devmode = false;
	public static boolean nointro = false;
	
	public static BitmapFont fontB;
	public static BitmapFont fontA;
	public static int fontABaseLine;
	public static int fontABaseLineEx;
	public static int fontBHeight;
	
	// Character mapping tables from original code
	public static final short[] var_17c;
	public static final short[] var_1dc;
	public static final byte[][] var_21f;
	
	public PaintableObject currentDisplayable;
	public static int width;
	public static int height;
	public int gameActionsStateImmediate = 0;
	public int gameActionsStateDelayed;
	public int lastGameAction = 0;
	public long keyPressedTime;
	public static Sprite[] sprChars;
	public static Random rnd;
	public static boolean[] mainSettings;
	public static String[] var_5fa;
	public static int[] rgb;
	public boolean var_667 = false;
	public static int delayedMusic;
	public static int delayedLoopCount;
	public static boolean delayPlayerStart;
	public static final String[] MUSIC_NAMES;
	public static final byte[] var_7c7;
	public static Music[] players;
	public static Music currentPlayer;
	public static boolean[] playerReadyFlags;
	public static int currentMusic;
	public static int currentLoopCount;
	public static byte[][] resourceData;
	public static String[] resourceNames;

	public static SpriteBatch batch;

	public Renderer()
	{
		// Initialize LibGDX stuff
		batch = new SpriteBatch();
		fontA = new BitmapFont(); // Default font
		fontB = new BitmapFont(); // Default font
		
		// Adjust font scaling if necessary
		fontA.setUseIntegerPositions(true);
		fontB.setUseIntegerPositions(true);

		fontABaseLine = (int)fontA.getCapHeight();
		fontABaseLineEx = fontABaseLine + 6;
		fontBHeight = (int)fontB.getLineHeight();

		PaintableObject.currentRenderer = this;
		width = 640; 
		height = 480;
		
		rnd = new Random();
		mainSettings = new boolean[] { true, true, true, true };
		
		// Input handling
		Gdx.input.setInputProcessor(this);
		
		try {
			loadResources();
			PaintableObject.loadLocale("lang.dat", false);
			
			// Load game
			var_5fa = new String[] { PaintableObject.getLocaleString(26), PaintableObject.getLocaleString(28), PaintableObject.getLocaleString(25), PaintableObject.getLocaleString(24) };
		
			MainDisplayable var2 = new MainDisplayable();
			currentDisplayable = var2;
			var2.load();
			rgb = new int[width * height];
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final int randomToRange(int var0)
	{
		if (var0 <= 0) return 0;
		return randomFromRange(0, var0);
	}

	public static final int randomFromRange(int var0, int var1)
	{
		if (var1 <= var0) return var0;
		return var0 + Math.abs(rnd.nextInt()) % (var1 - var0);
	}

	public static final int random()
	{
		return rnd.nextInt();
	}

	public static final int randomFromProbabilities(int[] probabilities)
	{
		int test = randomToRange(probabilities[probabilities.length - 1]);

		for(int i = 0; i < probabilities.length; i++)
		{
			if(test < probabilities[i])
			{
				return i;
			}
		}

		return -1;
	}

	public static final void convertProbabilities(int[] probabilities, boolean flag)
	{
		if(flag)
		{
			for(int i = 1; i < probabilities.length; i++)
			{
				probabilities[i] += probabilities[i - 1];
			}
		}
		else
		{
			for(int i = probabilities.length - 1; i >= 1; i--)
			{
				probabilities[i] -= probabilities[i - 1];
			}
		}
	}
	
	public static String readLine(InputStream is) throws IOException
	{
		StringBuffer res = new StringBuffer();
		char c;
		
		while(true)
		{
			int i = is.read();
			if (i == -1) break;
			c = (char)i;

			if(c == '\n')
			{
				return res.toString();
			}
			else if(c != '\r')
			{
				res.append(c);
			}
		}
		if(res.length() > 0) return res.toString();
		return null;
	}

	public static String[] tokenizeString(String str, char sep)
	{
		Vector v = new Vector();
		int index;

		while(str.length() > 0)
		{
			index = str.indexOf(sep);

			if(index >= 0)
			{
				v.addElement(str.substring(0, index));
				str = str.substring(index + 1);
			}
			else if(index == 0)
			{
				str = str.substring(1);
			}
			else
			{
				v.addElement(str);
				break;
			}
		}

		String[] res = new String[v.size()];
		v.copyInto(res);

		return res;
	}

	public static final byte[] getRMSData(String var0, int var1)
	{
		try
		{
			FileHandle file = Gdx.files.local("rms/" + var0 + "_" + var1 + ".bin");
			if (file.exists()) {
				return file.readBytes();
			}
			return null;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public static final void setRMSData(String var0, int var1, byte[] var2)
	{
		try
		{
			FileHandle file = Gdx.files.local("rms/" + var0 + "_" + var1 + ".bin");
			file.writeBytes(var2, false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static final int addRMSRecord(String var0, byte[] var1)
	{
		int i = 0;
		while(true) {
			FileHandle file = Gdx.files.local("rms/" + var0 + "_" + i + ".bin");
			if (!file.exists()) {
				file.writeBytes(var1, false);
				return i;
			}
			i++;
		}
	}

	public static final void deleteRMSRecord(String var0, int var1)
	{
		try
		{
			FileHandle file = Gdx.files.local("rms/" + var0 + "_" + var1 + ".bin");
			if (file.exists()) file.delete();
		}
		catch(Exception e)
		{
		}
	}

	public static final int getAvailableRMSSize(String var0)
	{
		return 1024 * 1024;
	}

	public static final int getGraphicStringWidth(byte var0, String var1)
	{
		if (sprChars != null && sprChars[var0] != null)
			return sprChars[var0].width * var1.length();
		return 0;
	}

	public static final int getGraphicFontHeight(byte var0)
	{
		if (sprChars != null && sprChars[var0] != null)
			return sprChars[var0].height;
		return 0;
	}

	public static final void setColor(SpriteBatch batch, int var1)
	{
		float r = ((var1 >> 16) & 0xFF) / 255f;
		float g = ((var1 >> 8) & 0xFF) / 255f;
		float b = (var1 & 0xFF) / 255f;
		batch.setColor(r, g, b, 1f);
	}

	public static final void fillAlphaRect(SpriteBatch batch, int color, int x, int y, int width, int height)
	{
		// Not implemented perfectly
	}

	public final void showNotify()
	{
		var_667 = false;
		delayPlayerStart = false;
		clearKeyStates();
		if(currentDisplayable != null)
		{
			currentDisplayable.showNotify();
		}
	}

	public final void hideNotify()
	{
		clearKeyStates();
		if(currentDisplayable != null)
		{
			if(!var_667)
			{
				delayPlayerStart = true;
				stopCurrentPlayer();
			}
			var_667 = false;
		}
	}

	public static final void drawAlignedGraphicString(SpriteBatch batch, String var1, int var2, int var3, int var4, int var5)
	{
		if((var5 & 8) != 0)
		{
			var2 -= getGraphicStringWidth((byte)var4, var1);
		}
		else if((var5 & 1) != 0)
		{
			var2 -= getGraphicStringWidth((byte)var4, var1) / 2;
		}

		if((var5 & 32) != 0)
		{
			var3 -= getGraphicFontHeight((byte)var4);
		}
		else if((var5 & 2) != 0)
		{
			var3 -= getGraphicFontHeight((byte)var4) / 2;
		}

		drawGraphicString(batch, var1, var2, var3, var4);
	}

	public static final void drawGraphicString(SpriteBatch batch, String var1, int var2, int var3, int var4)
	{
		boolean var5 = false;
		int var8 = 0;

		for(int var9 = var1.length(); var8 < var9; ++var8)
		{
			char var7;
			if((var7 = var1.charAt(var8)) >= var_17c[var4] && var7 <= var_1dc[var4])
			{
				byte var6;
				if((var6 = var_21f[var4][var7 - var_17c[var4]]) != -1)
				{
					sprChars[var4].setCurrentFrame(var6);
					sprChars[var4].paint(batch, var2, var3);
					var2 += sprChars[var4].width;
				}
				else
				{
					fontA.draw(batch, String.valueOf(var7), var2, var3);
					var2 += 8;
				}
			}
		}

	}

	public static final void drawString(SpriteBatch batch, String var1, int var2, int var3, int var4)
	{
		fontA.draw(batch, var1, var2, var3);
	}

	public final void setCurrentDisplayable(PaintableObject var1)
	{
		clearKeyStates();
		var1.showNotify();
		currentDisplayable = var1;
	}

	public final void repaintAndService()
	{
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		batch.begin();
		if (currentDisplayable != null) {
			currentDisplayable.update();
			currentDisplayable.paint(batch);
		}
		batch.end();
		
		if(isShown() && !delayPlayerStart)
		{
			if(delayedMusic >= 0)
			{
				startPlayer(delayedMusic, delayedLoopCount);
				if(currentPlayer != null && currentPlayer.isPlaying())
				{
					delayedMusic = -1;
				}
			}
		}
	}
	
	public boolean isShown() { return true; }
	
	public int getWidth() { return Gdx.graphics.getWidth(); }
	public int getHeight() { return Gdx.graphics.getHeight(); }

	public final int getGameAction(int keycode)
	{
		switch(keycode) {
			case Input.Keys.UP: return 1;
			case Input.Keys.DOWN: return 6;
			case Input.Keys.LEFT: return 2;
			case Input.Keys.RIGHT: return 5;
			case Input.Keys.ENTER:
			case Input.Keys.SPACE: return 8;
			case Input.Keys.A: return 1024;
			case Input.Keys.S: return 2048;
			case Input.Keys.ESCAPE: return 0;
		}
		return 0;
	}

	public final String getKeyNameEx(int var1)
	{
		if (var1 == 1024) return "A";
		if (var1 == 2048) return "S";
		return "";
	}

	@Override
	public boolean keyDown(int keycode) {
		int mappedAction = 0;
		if (keycode == Input.Keys.UP) mappedAction = 1;
		else if (keycode == Input.Keys.DOWN) mappedAction = 2; // Original logic mapping check
		else if (keycode == Input.Keys.LEFT) mappedAction = 4;
		else if (keycode == Input.Keys.RIGHT) mappedAction = 8;
		else if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) mappedAction = 16;
		else if (keycode == Input.Keys.A) mappedAction = 1024;
		else if (keycode == Input.Keys.S) mappedAction = 2048;
		else if (keycode == Input.Keys.ESCAPE) mappedAction = 0;
		
		handleKeyPressedGameAction(mappedAction);
		if (currentDisplayable != null) {
			currentDisplayable.keyPressed(keycode, mappedAction);
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		int mappedAction = 0;
		if (keycode == Input.Keys.UP) mappedAction = 1;
		else if (keycode == Input.Keys.DOWN) mappedAction = 2;
		else if (keycode == Input.Keys.LEFT) mappedAction = 4;
		else if (keycode == Input.Keys.RIGHT) mappedAction = 8;
		else if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) mappedAction = 16;
		else if (keycode == Input.Keys.A) mappedAction = 1024;
		else if (keycode == Input.Keys.S) mappedAction = 2048;
		
		handleKeyReleasedGameAction(mappedAction);
		return true;
	}

	public final void handleKeyPressedGameAction(int var1)
	{
		lastGameAction = var1;
		keyPressedTime = System.currentTimeMillis();
		gameActionsStateImmediate |= var1;
		gameActionsStateDelayed |= var1;
	}

	public final void handleKeyReleasedGameAction(int var1)
	{
		if(var1 == lastGameAction)
		{
			lastGameAction = 0;
		}

		gameActionsStateImmediate &= ~var1;
	}
	
	public final void clearKeyStates()
	{
		lastGameAction = 0;
		gameActionsStateImmediate = 0;
		gameActionsStateDelayed = 0;
	}

	public final boolean wasKeyPressed(int var1)
	{
		boolean var2 = (gameActionsStateDelayed & var1) != 0;
		gameActionsStateDelayed &= ~var1;
		return var2;
	}

	public final boolean isKeyPressed(int var1)
	{
		return (gameActionsStateImmediate & var1) != 0;
	}

	public final boolean isKeyHeld(int var1)
	{
		return lastGameAction == var1 && System.currentTimeMillis() - keyPressedTime >= 400L;
	}

	public static final void loadCharSprites()
	{
		sprChars[0] = new Sprite("chars");
		sprChars[1] = new Sprite("lchars");
	}

	public final void setFPS(int fps)
	{
	}

	public static final void vibrate(int var0)
	{
		if(mainSettings[1])
		{
			Gdx.input.vibrate(var0 * 4);
		}
	}

	public static final void createPlayerArrays()
	{
		players = new Music[MUSIC_NAMES.length];
		playerReadyFlags = new boolean[MUSIC_NAMES.length];
	}

	public static final void createPlayer(int var0)
	{
		try
		{
			playerReadyFlags[var0] = false;
			FileHandle file = Gdx.files.internal(MUSIC_NAMES[var0] + ".ogg");
			if (file.exists()) {
				players[var0] = Gdx.audio.newMusic(file);
				playerReadyFlags[var0] = true;
			}
		}
		catch(Exception var2)
		{
			var2.printStackTrace();
		}
	}

	public static final void startPlayer_(int var0, int var1)
	{
		startPlayer(var0, var1);
	}

	public static final void stopCurrentPlayer()
	{
		try
		{
			if(currentPlayer != null)
			{
				currentPlayer.stop();
				currentPlayer = null;
				currentMusic = -1;
			}
		}
		catch(Exception var0)
		{
			return;
		}

	}

	public static final void startPlayer(int var0, int var1)
	{
		try
		{
			if(!playerReadyFlags[var0])
			{
				return;
			}

			if(currentPlayer != null)
			{
				currentPlayer.stop();
			}

			if(var_7c7[var0] == 1 && mainSettings[0])
			{
				if(delayPlayerStart)
				{
					delayedMusic = var0;
					delayedLoopCount = var1;
				}
				else
				{
					currentPlayer = players[var0];
					if (var1 == -1 || var1 > 1) currentPlayer.setLooping(true);
					else currentPlayer.setLooping(false);
					
					currentPlayer.play();
					currentMusic = var0;
					currentLoopCount = var1;
				}
			}
		}
		catch(Exception var2)
		{
			return;
		}

	}

	public static final void stopPlayer(int var0)
	{
		try
		{
			if(!playerReadyFlags[var0])
			{
				return;
			}

			if(currentPlayer == players[var0])
			{
				currentPlayer.stop();
				currentPlayer = null;
				currentMusic = -1;
			}
		}
		catch(Exception var1)
		{
			return;
		}

	}

	public static final void loadResources() throws IOException
	{
	}

	public static final byte[] getResource(String var0)
	{
		if (var0.startsWith("/")) var0 = var0.substring(1);
		try { 
			return Gdx.files.internal(var0).readBytes();
		} catch (Exception e) {
			return null;
		}
	}

	public static final InputStream getResourceAsStream(String var0)
	{
		if (var0.startsWith("/")) var0 = var0.substring(1);
		try { 
			return Gdx.files.internal(var0).read();
		} catch (Exception e) {
			return null;
		}
	}

	@Override public void show() {}
	@Override public void resize(int width, int height) {}
	@Override public void pause() {}
	@Override public void resume() {}
	@Override public void hide() {}
	@Override public void dispose() {
		batch.dispose();
		fontA.dispose();
		fontB.dispose();
		for(Music m : players) {
			if(m != null) m.dispose();
		}
	}
	
	@Override public boolean keyTyped(char character) { return false; }
	@Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
	@Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
	@Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
	@Override public boolean mouseMoved(int screenX, int screenY) { return false; }
	@Override public boolean scrolled(float amountX, float amountY) { return false; }

	static
	{
		var_17c = new short[] { (short)45, (short)43 };
		var_1dc = new short[] { (short)57, (short)57 };
		var_21f = new byte[][] { { (byte)10, (byte)11, (byte)-1, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9 }, { (byte)12, (byte)-1, (byte)11, (byte)-1, (byte)10, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9 } };
		sprChars = new Sprite[2];
		delayedMusic = -1;
		delayPlayerStart = false;
		MUSIC_NAMES = new String[] { "main_theme", "bg_story", "bg_good", "bg_bad", "battle_good", "battle_bad", "victory", "gameover", "game_complete" };
		var_7c7 = new byte[] { (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1 };
	}
}
