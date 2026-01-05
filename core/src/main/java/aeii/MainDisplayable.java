package aeii;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public final class MainDisplayable extends PaintableObject
{
	public static final int MAX_SAVES = 10;
	public static final String FSO_MAP_SUFFIX = ".aem";
	
	public static String[] FSO_PARENT_DIR; 
	public static String[] FSO_PARENT_DIR_SAVE;

	public String midletVersion = "1.0";
	public static byte FOOTER_HEIGHT = 32;
	public int width_;
	public int height_;
	public int halfWidth_;
	public int halfHeight_;
	public int missionsComplete = 0;
	public static String[] skirmishLevelsNames = new String[12];
	public static int[] SKIRMISH_LOCKED_LEVELS = new int[] { -1, -1, -1, -1, -1, -1, -1, -1 };
	public boolean[] skirmishLevelLockFlags;
	public static final int[] SKIRMISH_START_MONEY = new int[] { 500, 1000, 2000, 5000, 10000, 25000, 50000, 75000, 100000, 150000, 200000 };
	public static final int[] SKIRMISH_UNIT_CAPS = new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200 };
	public int skirmishStartMoney;
	public int skirmishUnitCap;
	public byte skirmishMode;
	
	public static int KEY_LSK = 1024;
	public static int KEY_RSK = 2048;
	
	public String[] INGAME_MENU_TEXT;
	public String[] INGAME_ACTIONS_TEXT;
	public SpriteFrame[] sprActionIcons;
	public String[] MENU_ITEMS_TEXT;
	public SpriteFrame[] sfmMenuIcons;
	public String[] MENU_PLAYER_MODE_TEXT;
	public String[] MENU_ON_OFF_TEXT;
	public byte[] var_5e3;
	public byte[] MAIN_MENU_ITEMS;
	public byte[] PLAY_MENU_ITEMS;
	public static final byte[][] CURSOR_FRAME_SEQUENCES = new byte[][] { { (byte)0, (byte)1 }, { (byte)2, (byte)3, (byte)4 }, { (byte)0, (byte)1 }, { (byte)5 } };
	public static final byte[] var_6d7 = new byte[] { (byte)0 };
	public long var_726;
	public long var_755;

	public static final int[][][] PALETTE_REPLACE_TABLE = new int[][][]
	{
		new int[0][],
		{ { 150, 217, 244 }, { 65, 149, 233 }, { 0, 100, 198 }, { 12, 53, 112 } },
		{ { 244, 158, 156 }, { 219, 36, 113 }, { 161, 0, 112 }, { 95, 5, 120 } },
		{ { 171, 237, 90 }, { 99, 190, 37 }, { 0, 153, 55 }, { 0, 85, 82 } },
		{ { 0, 118, 150 }, { 0, 65, 114 }, { 0, 43, 75 }, { 0, 22, 48 } }
	};

	public static final String[] FRACTION_COLOR_PREFIXES = new String[] { "blue", "red", "green", "black" };
	public static final int[] FRACTION_COLORS = new int[] { 0xA0A0A0, 0x65C6, 0xE80052, 0x9A31, 0x4172 };
	public static final int[] FRACTION_BACKGROUND_MUSIC = new int[] { -1, 2, 3, 2, 3 };
	public static final int[] FRACTION_BATTLE_MUSIC = new int[] { -1, 4, 5, 4, 5 };
	public Sprite[][] sprUnitIcons;

	public static final byte[] WATER_PAIR = new byte[] { (byte)1, (byte)2 };
	public static final byte BROKEN_BUILDING = 27;
	public static final byte FRACTION_BUILDINGS = 37;
	public static final byte CUSTOM_TILES = FRACTION_BUILDINGS + 10;
	
	public static byte[] TERRAIN_DEFENCE_BONUS = new byte[] { (byte)0, (byte)5, (byte)10, (byte)10, (byte)15, (byte)0, (byte)5, (byte)15, (byte)15, (byte)15 };
	public static byte[] TERRAIN_STEPS_REQUIRED = new byte[] { (byte)1, (byte)1, (byte)2, (byte)2, (byte)3, (byte)3, (byte)1, (byte)1, (byte)1, (byte)1 };
	
	public SpriteFrame[] sfmSmallTiles;
	public byte[] tilesTypes;
	public byte[] smallTiles;
	public int mapWidthPixels;
	public int mapHeightPixels;
	public int mapX;
	public int mapY;
	public int mapWidth;
	public int mapHeight;
	private SpriteFrame sprTombStone;
	public SpriteFrame[] sfmTiles;
	public Sprite sprCursor;
	public Sprite sprCursorCopy;
	public Sprite sprSideArrow;
	public Sprite sprArrow;
	public Sprite sprButtons;
	public Sprite sprMenu;
	public Sprite sprSmoke;
	public Sprite sprSpark;
	public Sprite sprRedSpark;
	public Sprite sprStatus;
	public Sprite sprSmallSpark;
	public Sprite sprPortraits;
	public int cursorMapX;
	public int cursorMapY;
	public byte[][] mapData;
	public byte gameState;
	public byte prevGameState;
	public long delayCounter;
	public int currentMission;
	public int currentSkirmishLevelNumber;
	public int currentAttackTargetUnit;
	public Unit[] unitsWithinAttackRange;
	public Unit currentSelectedUnit;
	public int selectedUnitPrevMapX;
	public int selectedUnitPrevMapY;
	public byte[][] mapAlphaData;
	public boolean paintMapAlphaDataFlag;
	public boolean showAtackRange;
	public boolean drawCursorFlag;
	public Vector units;
	public Vector currentRoute;
	public int var_129c;
	public int var_12cb;
	public long var_12ff;
	public byte turnQueueLength;
	public byte[] fractionsPosInTurnQueue;
	public byte[] fractionsTurnQueue;
	public byte[] playerTeams;
	public byte currentTurningPlayer;
	public short currentTurn;
	public Unit[] fractionKings;
	public Unit[][] fractionsAllKings;
	public int[] fractionsKingCount;
	public int[] money;
	public byte[][] fractionKingPositions;
	public byte[] playerModes;
	public AuxDisplayable auxInGameMenu;
	public Vector activeEffects;
	public Vector newEffects;
	public Unit lastDeadUnit;
	public Unit var_16e5;
	public long var_1728;
	public Unit var_1774;
	public byte var_17b3;
	public long var_17c8;
	public int var_17d5;
	public boolean var_17f7;
	public boolean var_1824;
	public byte mode;
	public SpriteFrame sfmLogo;
	public SpriteFrame sfmSplash;
	public SpriteFrame sfmGameLogo;
	public SpriteFrame sfmGlow;
	public int introTransMode;
	public boolean var_19c3;
	public int combatDrapValue;
	public int var_1a23;
	public int var_1a3b;
	public int var_1a98;
	public long var_1ac1;
	public Unit currentAttackUnit;
	public Unit currentAttackVictimUnit;
	public boolean var_1b42;
	public long var_1b6a;
	public boolean cursorPositionChanged;
	public long var_1c06;
	public int currentWaterPairTile;
	public int var_1c69;
	public SpriteFrame[] sprWaterPair;
	public boolean paintLoadingStringFlag;
	public boolean paintPressAnyKeyFlag;
	public static int[] INSTRUCTION_TITLES = new int[] { 85, 83, 83, 83, 83, 83, 83, 83, 83, 175, 84, 84, 84, 175, 147, 159, 151, 155, 167, 171 };
	public static int[] INSTRUCTION_TEXT_MESSAGES = new int[] { 15, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214  };
	public int currentHelp;
	public AuxDisplayable auxInstructionsContainer;
	public AuxDisplayable auxInstructionsPartSelector;
	public AuxDisplayable auxInstructionsTextMessage;
	public AuxDisplayable auxObjectivesMessage;
	public int allowedUnits;
	public AuxDisplayable auxSkirmishLevelList;
	public AuxDisplayable auxSkirmishLevelListContainer;
	public AuxDisplayable auxSelectLevelList;
	public boolean var_202c;
	public int var_207c;
	public byte[][] buildingData;
	public byte[][] castleData;
	public Sprite sprBigCircle;
	public Sprite sprSmallCircle;
	public Unit currentUnitUnderCursor;
	public Sprite sprHudIcons;
	public Sprite sprHudIcons2;
	public int var_21e9;
	public int var_223e;
	public int glowX;
	public boolean activeFlag;
	public int statusBarOffset;
	public boolean var_2319;
	public Sprite sprMiniIcons;
	public Sprite[] buildingEffects;
	public Sprite sprBSmoke;
	public AuxDisplayable auxSkirmishMapPreviewContainer;
	public AuxDisplayable auxSkirmishTeamSetupContainer;
	public AuxDisplayable auxSkirmishStartMoneySelector;
	public AuxDisplayable auxSkirmishUnitCapSelector;
	public AuxDisplayable[] auxSkirmishPlayerModeSelector;
	public AuxDisplayable[] auxSkirmishPlayerTeamSelector;
	public AuxDisplayable auxSettingsMenuContainer;
	public AuxDisplayable[] auxSettingsSwitches;
	public Sprite sprAlpha;
	public int drapAlphaValue;
	public boolean drawAlphaDrap;
	public boolean inverseDrapAlpha;
	public boolean updateAlphaDrap;
	public Vector var_26d9;
	public Sprite sprLevelUp;
	public int var_2786;
	public Sprite[] sprKingHeadIcons;
	public AuxDisplayable auxUnitBuyContainer;
	public AuxDisplayable auxBuyUnitInfo;
	public AuxDisplayable auxBuyUnitSelector;
	public AuxDisplayable auxBuyUnitDescription;
	public Sprite sprArrowIcons;
	public Sprite heavenFuryBlast;
	public Unit heavenFuryTarget;
	public int var_2985;
	public boolean var_29c1;
	public boolean var_29de;
	public String currentMapName;
	public String[] saveInfoStrings;
	public byte[] saveCurrentFraction;
	public int[] saveCurrentMission;
	public AuxDisplayable auxLoadGameContainer;
	public AuxDisplayable auxSaveGameContainer;
	public AuxDisplayable var_2b0e;
	public AuxDisplayable var_2b46;
	public AuxDisplayable auxExitGameQuestion;
	public AuxDisplayable auxNewGameQuestion;
	public AuxDisplayable auxSaveOverwrireQuestion;
	public byte skirmishLevelPlayerCount;
	public byte[] skirmishLevelFractions;
	public int downloadedSkirmishLevelCount;
	public String[] downloadedSkirmishLevelNames;
	public int[] downloadedSkirmishLevelNumbers;
	public String[] ONLINE_MENU_ITEMS;
	public String[] DOWNLOAD_LEVELS_MENU_ITEMS;
	public AuxDisplayable auxOnLineMenuContainer;
	public AuxDisplayable auxDownloadLevelsMenuContainer;
	public AuxDisplayable var_2df9;
	public AuxDisplayable var_2e13;
	public AuxDisplayable var_2e20;
	public AuxDisplayable auxDownloadableSkirmishLevelsContainer;
	public AuxDisplayable var_2e5d;
	public AuxDisplayable var_2e87;
	public int var_2ee6;
	public String var_2f35;
	public int availableRMSDownloadSize;
	public boolean macrospaceHighScoreUpload;
	public boolean statusBarNeedsRepaint;
	public boolean tileIconNeedsRepaint;
	public SpriteFrame sfmGameOver;
	public TextureRegion[][] alphaCoveredTiles;
	public int loadProgress;
	public StringBuffer cheatStringBuffer;
	public Unit var_31be;
	public int mapStepMax;
	public int mapStepMin;
	public boolean var_326f;
	public int alphaWindowSize;
	public int lineHeight;
	public int var_3308;
	public int linesOnScreen;
	public int var_3395;
	public SpriteFrame sprIntro;
	public String[] textLines;
	public boolean var_3437;
	public byte var_3491;
	public int var_34b0;
	public int var_34ec;
	public int introHeight;
	public int var_357f;
	public int var_35c4;
	public int var_3622;
	public int var_3685;
	public int var_3695;
	public int currentIncome;
	public static final byte[] var_36cf = new byte[] { (byte)0, (byte)2, (byte)3, (byte)3, (byte)1, (byte)3, (byte)3, (byte)3, (byte)3, (byte)3, (byte)3, (byte)3 };
	public byte var_36e4;
	public int var_3733;
	public int var_3781;
	public Unit var_37c2;
	public Unit var_37e4;
	public Unit var_381f;
	public int var_3864;
	public long var_38a4;
	public Unit[] var_38d5;
	public Unit[] var_3903;
	public byte[] var_3947;
	public int[][] var_395f;
	public int[] var_399e;
	public int var_39d0;
	public byte[][] var_39e3;
	public int castleCount;
	public int var_3a15;
	public int var_3a34;
	public int var_3a41;
	public int var_3a5c;
	public Vector var_3aad;
	public boolean var_3aed;
	public int scriptUpdateDelayCounter;
	public AuxDisplayable auxMapNameMessage;
	public Unit crystallEscortLeader;
	public Unit crystallEscortCrystall;
	public Unit crystallEscortFollower;
	public Unit templeWarrior;
	public Unit var_3c5c;
	public Unit lastFinishedMoveUnit;
	public int scriptState;
	public String[][][] script;
	public long var_3cee;
	public int var_3d08;
	public boolean var_3d48;
	public boolean var_3d57;
	public int mapTargetX;
	public int mapTargetY;
	public int var_3e0c;
	public CombatAnimation combatAttackerUnitAnimation;
	public CombatAnimation combatVictimUnitAnimation;
	public long var_3eaa;
	public boolean var_3f02;
	public Vector combatAnimationSprites;
	public boolean shakeMapFlag;
	public long shakeMapTime;
	public long shakeMapDelayCounterStart;
	public boolean victimUnitResponded;
	public boolean combatDrawDrapFlag;
	public boolean combatHeaderNeedsRepaint;
	public boolean combatFooterNeedsRepaint;
	public int var_4138;
	public int var_4191;
	public int var_41ef;
	public String[] var_4217;
	public String[] var_4263;
	public String[] var_4277;
	public String[] var_4289;
	public String[] var_42a0;
	public int[] var_42c2;
	public String provisionHighScorePortalCode;
	public String provisionHighScoreGameCode;
	public String provisionHighScoreURL;
	private ByteArrayOutputStream var_43a8;
	private DataOutputStream var_43e4;
	public int var_442b;
	public boolean var_4482;
	public PaintableObject var_44bb;
	public int var_44e6;
	public AuxDisplayable auxImportLevelsContainer;
	public AuxDisplayable auxImportLevelsList;
	public AuxDisplayable auxExportLevelsContainer;
	public AuxDisplayable auxExportLevelsList;
	// public FileSystemObject fso; // REMOVED
	public String[] currentFileList;
	public byte currentTile;
	public byte currentUnitType;
	public Unit currentUnit;
	public byte levelEditorMode;
	public AuxDisplayable auxEditorLevelListContainer;
	public AuxDisplayable auxEditorLevelList;
	public AuxDisplayable auxExportableLevelListContainer;
	public AuxDisplayable auxExportableLevelList;
	public int[] tileProbabilities;
	public int[] tilePrevProbabilities;
	public boolean[] selflags;

	public MainDisplayable()
	{
		FSO_PARENT_DIR = new String[] { ".." };
		FSO_PARENT_DIR_SAVE = new String[] { PaintableObject.getLocaleString(300), ".." };

		levelEditorMode = 0;

		if(Renderer.devmode)
		{
			INGAME_MENU_TEXT = new String[] { PaintableObject.getLocaleString(66), PaintableObject.getLocaleString(70), PaintableObject.getLocaleString(71), PaintableObject.getLocaleString(4), PaintableObject.getLocaleString(314), PaintableObject.getLocaleString(60) };
		}
		else
		{
			INGAME_MENU_TEXT = new String[] { PaintableObject.getLocaleString(66), PaintableObject.getLocaleString(70), PaintableObject.getLocaleString(71), PaintableObject.getLocaleString(4), PaintableObject.getLocaleString(60) };
		}

		INGAME_ACTIONS_TEXT = new String[] { PaintableObject.getLocaleString(63), PaintableObject.getLocaleString(67), PaintableObject.getLocaleString(68), PaintableObject.getLocaleString(62), PaintableObject.getLocaleString(69), PaintableObject.getLocaleString(61), PaintableObject.getLocaleString(64) };

		sprActionIcons = new SpriteFrame[INGAME_ACTIONS_TEXT.length];
		MENU_ITEMS_TEXT = new String[] { PaintableObject.getLocaleString(1), PaintableObject.getLocaleString(2), PaintableObject.getLocaleString(5), PaintableObject.getLocaleString(3), PaintableObject.getLocaleString(6), PaintableObject.getLocaleString(8), PaintableObject.getLocaleString(7), PaintableObject.getLocaleString(9), PaintableObject.getLocaleString(10), PaintableObject.getLocaleString(11), PaintableObject.getLocaleString(4) };
		sfmMenuIcons = new SpriteFrame[MENU_ITEMS_TEXT.length];
		MENU_PLAYER_MODE_TEXT = new String[] { PaintableObject.getLocaleString(35), PaintableObject.getLocaleString(36), PaintableObject.getLocaleString(37) };
		MENU_ON_OFF_TEXT = new String[] { PaintableObject.getLocaleString(29), PaintableObject.getLocaleString(30) };
		var_5e3 = new byte[] { (byte)0, (byte)6, (byte)5, (byte)7, (byte)8, (byte)9 };
		MAIN_MENU_ITEMS = new byte[] { (byte)0, (byte)6, (byte)5, (byte)7, (byte)8, (byte)9 };
		PLAY_MENU_ITEMS = new byte[] { (byte)1, (byte)2, (byte)3, (byte)4 };
		var_726 = 0L;
		paintMapAlphaDataFlag = false;
		showAtackRange = false;
		drawCursorFlag = true;
		units = new Vector();
	
turnQueueLength = 2;
		fractionsPosInTurnQueue = new byte[5];
		fractionsTurnQueue = new byte[4];
		playerTeams = new byte[4];
		currentTurningPlayer = 0;
		money = new int[4];
		fractionKingPositions = new byte[4][2];
		playerModes = new byte[4];
		activeEffects = new Vector();
		newEffects = new Vector();
		var_17f7 = false;
		var_1824 = false;
		var_19c3 = false;
		var_1b42 = true;
		cursorPositionChanged = false;
		paintLoadingStringFlag = false;
		currentHelp = -1;
		allowedUnits = 8;
		var_202c = false;
		activeFlag = true;
		buildingEffects = new Sprite[0];
		var_26d9 = new Vector(2);
		var_2985 = 0;
		var_29c1 = true;
		ONLINE_MENU_ITEMS = new String[] { PaintableObject.getLocaleString(46), PaintableObject.getLocaleString(47), PaintableObject.getLocaleString(291) };
		DOWNLOAD_LEVELS_MENU_ITEMS = new String[] { PaintableObject.getLocaleString(289), PaintableObject.getLocaleString(298), PaintableObject.getLocaleString(48), PaintableObject.getLocaleString(49) };
		macrospaceHighScoreUpload = true;
		statusBarNeedsRepaint = true;
		tileIconNeedsRepaint = true;
		cheatStringBuffer = new StringBuffer();
		var_31be = null;
		mapStepMax = 12;
		mapStepMin = 1;
		var_357f = 0;
		var_3622 = 24;
		var_3685 = 8;
		var_3695 = var_3685 >> 1;
		var_36e4 = 0;
		var_3aed = false;
		var_3c5c = null;
		lastFinishedMoveUnit = null;
		var_3d48 = false;
		var_3d57 = false;
		mapTargetX = -1;
		mapTargetY = -1;
		var_3e0c = 0;
		combatAnimationSprites = new Vector();
		shakeMapFlag = false;
		provisionHighScorePortalCode = "Macrospace";
		provisionHighScoreGameCode = "msaeii";
		provisionHighScoreURL = "http://msaeii.scores.macrospace.com/connectx/in";
		mode = 4;
	}

	public final void load() throws IOException
	{
		setLoadProgress(0);
		Renderer.loadResources();
		setLoadProgress(18);
		Renderer.createPlayerArrays();

		for(int var1 = 0; var1 < Renderer.MUSIC_NAMES.length; ++var1)
		{
			Renderer.createPlayer(var1);
			setLoadProgress(19 + var1);
		}

		setLoadProgress(28);
		AuxDisplayable.currentMainDisplayable = this;
		setLoadProgress(29);
		PaintableObject.createSinTab();
		setLoadProgress(30);
		Renderer.loadCharSprites();
		setLoadProgress(32);
		sprActionIcons = (new Sprite("action_icons")).frames;
		setLoadProgress(34);
		sfmMenuIcons = (new Sprite("menu_icons")).frames;
		setLoadProgress(36);
		sprHudIcons = new Sprite("hud_icons");
		setLoadProgress(38);
		sprHudIcons2 = new Sprite("hud_icons_2");
		setLoadProgress(40);
		sprArrow = new Sprite("arrow");
		setLoadProgress(42);
		sprSideArrow = new Sprite("side_arrow");
		setLoadProgress(44);
		sprButtons = new Sprite("buttons");
		setLoadProgress(46);
		sprMenu = new Sprite("menu");
		setLoadProgress(48);
		sprBigCircle = new Sprite("big_circle");
		setLoadProgress(50);
		sprSmallCircle = new Sprite("small_circle");
		setLoadProgress(52);
		sprSmallSpark = new Sprite("small_spark");
		setLoadProgress(54);
		sprAlpha = new Sprite("alpha");
		setLoadProgress(56);

		try
		{
			sfmGameOver = new SpriteFrame("gameover");
		}
		catch(Exception var15)
		{
			;
		}

		setLoadProgress(58);
		
sfmLogo = new SpriteFrame("ms_logo");
		setLoadProgress(62);
		
		DataInputStream var17;
		InputStream is = Renderer.getResourceAsStream("tiles0.prop");
		
		if(is != null)
		{
			String[] line;
			String s;
			int index;

			while(true)
			{
				s = Renderer.readLine(is);

				if(s == null)
				{
					break;
				}

				index = s.indexOf(';');

				if(index >= 0)
				{
					s = s.substring(0, index);
				}

				s = s.trim();

				if(s.length() == 0)
				{
					continue;
				}

				line = Renderer.tokenizeString(s, ' ');
				
				if(line[0].equalsIgnoreCase("TypeCount"))
				{
					index = Integer.parseInt(line[1]);
					
					TERRAIN_STEPS_REQUIRED = new byte[index];
					TERRAIN_DEFENCE_BONUS = new byte[index];
					
					CombatAnimation.BACKGROUNDS = new String[index];
					CombatAnimation.FOREGROUNDS = new String[index];
				}
				else if(line[0].equalsIgnoreCase("TypeDef"))
				{
					index = Integer.parseInt(line[1]);

					TERRAIN_STEPS_REQUIRED[index] = (byte)Integer.parseInt(line[2]);
					TERRAIN_DEFENCE_BONUS[index] = (byte)Integer.parseInt(line[3]);
					
					CombatAnimation.BACKGROUNDS[index] = line[4];
					CombatAnimation.FOREGROUNDS[index] = line[5];
				}
				else if(line[0].equalsIgnoreCase("TileCount"))
				{
					tilesTypes = new byte[Integer.parseInt(line[1])];
					smallTiles = new byte[tilesTypes.length];
				}
				else if(line[0].equalsIgnoreCase("TileDef"))
				{
					index = Integer.parseInt(line[1]);
					
					tilesTypes[index] = (byte)Integer.parseInt(line[2]);
					smallTiles[index] = (byte)Integer.parseInt(line[3]);
				}
			}

			is.close();
		}
		
		setLoadProgress(64);
		Sprite var19 = new Sprite("stiles0");
		sfmSmallTiles = var19.frames;
		setLoadProgress(70);
		sprMiniIcons = new Sprite("mini_icons");
		setLoadProgress(72);

		sfmTiles = (new Sprite("tiles0")).frames;

		width_ = super.width;
		height_ = super.height;
		halfWidth_ = width_ >> 1;
		halfHeight_ = height_ >> 1;
		introTransMode = 0;

		int var5;
		for(var5 = 0; var5 < 12; ++var5)
		{
			skirmishLevelsNames[var5] = PaintableObject.getLocaleString(101 + var5);
		}

		loadMainSettings();
		setLoadProgress(74);

		try
		{
			byte[] d = Renderer.getRMSData("settings", 1);
			if (d != null) missionsComplete = d[0];
		}
		catch(Exception var14)
		{
			;
		}

		setLoadProgress(76);
		downloadedSkirmishLevelNames = new String[0];
		downloadedSkirmishLevelNumbers = new int[0];

		// Skip downloaded levels RMS loading for now
		setLoadProgress(80);

		availableRMSDownloadSize = Renderer.getAvailableRMSSize("download");
		setLoadProgress(84);

		saveInfoStrings = new String[MAX_SAVES];
		saveCurrentFraction = new byte[MAX_SAVES];
		saveCurrentMission = new int[MAX_SAVES];

		for(var5 = 0; var5 < MAX_SAVES; ++var5)
		{
			saveCurrentFraction[var5] = -1;
			saveCurrentMission[var5] = -1;
			byte[] var21 = null;

			try
			{
				var21 = Renderer.getRMSData("save", var5);
			}
			catch(Exception var13)
			{
				;
			}

			if(var21 != null && var21.length != 0)
			{
				var17 = new DataInputStream(new ByteArrayInputStream(var21));
				byte var7 = var17.readByte();
				byte var8 = var17.readByte();
				var17.readByte();
				var17.readByte();
				byte var11 = var17.readByte();
				short var12 = var17.readShort();
				var17.close();
				saveCurrentFraction[var5] = var11;
				saveInfoStrings[var5] = getSaveInfoString(var7, var8, var12);
				saveCurrentMission[var5] = var8;
			}
			else
			{
				saveInfoStrings[var5] = "\n" + PaintableObject.getLocaleString(79) + "\n ";
			}
		}

		setLoadProgress(90);
		var5 = 0;
		// Skip app properties for version/highscore config
		
		setLoadProgress(96);
		// Logic for skPos removed (key mapping fixed in Renderer)

		setLoadProgress(100);
		Renderer.startPlayer(0, 0);
		mode = 0;
	}

	public final void setLoadProgress(int var1)
	{
		loadProgress = var1;
		PaintableObject.currentRenderer.repaintAndService();
	}

	public final String getSaveInfoString(int gameMode, int mission, int turn)
	{
		String var4;

		if(gameMode == 0)
		{
			var4 = PaintableObject.getLocaleString(121 + mission);
		}
		else
		{
			var4 = getSkirmishLevelName(mission);
		}

		return PaintableObject.getLocaleString(32 + gameMode) + "\n" + var4 + "\n" + "Current turn: " + (turn + 1);
	}

	public final boolean isActive()
	{
		return activeFlag && PaintableObject.currentRenderer.currentDisplayable == this;
	}

	public final void loadMainGameResources() throws IOException
	{
		sfmLogo = null;
		sfmSplash = null;
		sfmGameLogo = null;
		sfmGlow = null;
		System.gc();
		height_ = super.height - FOOTER_HEIGHT;
		halfHeight_ = height_ >> 1;
		Renderer.stopCurrentPlayer();

		if(mode != 1)
		{
			mode = 1;
			Unit.readUnitData(this);
			Renderer.loadResources();
			sprUnitIcons = new Sprite[8][12];

			byte fraction;
			byte unit;
			SpriteFrame[] var5;

			for(fraction = 0; fraction < 8; ++fraction)
			{
				Sprite unitIcons = new Sprite("unit_icons", fraction);
				int iconCount = unitIcons.getFrameCount() / 12;

				for(unit = 0; unit < 12; ++unit)
				{
					if((fraction & 1) == 1)
					{
						var5 = new SpriteFrame[] { unitIcons.frames[unit] };
						sprUnitIcons[fraction][unit] = new Sprite(var5);
					}
					else
					{
						var5 = new SpriteFrame[iconCount];

						for(int i = 0; i < iconCount; ++i)
						{
							var5[i] = unitIcons.frames[i * 12 + unit];
						}

						sprUnitIcons[fraction][unit] = new Sprite(var5);
					}
				}
			}

			alphaCoveredTiles = new TextureRegion[2][sfmTiles.length];

			for(fraction = 0; fraction < 2; ++fraction)
			{
				for(unit = 0; unit < sfmTiles.length; ++unit)
				{
					// Creating colored tiles for move range visualization
					// This used to draw on a mutable Image. In libGDX we can use Pixmap or FrameBuffer.
					// For simplicity, let's use Pixmap.
					Pixmap pixmap = new Pixmap(24, 24, Pixmap.Format.RGBA8888);
					// Draw base tile? No, original just drew tile then alpha over it.
					// We need to replicate sfmTiles[unit].paint(g) onto pixmap.
					// This is hard with Pixmap as we have TextureRegions.
					// Alternative: Just use a colored overlay sprite in rendering.
					
					// Placeholder: transparent color
					pixmap.setColor(0, 0, 0, 0); 
					pixmap.fill();
					alphaCoveredTiles[fraction][unit] = new TextureRegion(new Texture(pixmap));
					pixmap.dispose();
				}
			}

			sprPortraits = new Sprite("portraits");
			sprCursor = new Sprite("cursor");
			sprRedSpark = new Sprite("redspark");
			sprSmoke = new Sprite("smoke");
			sprSpark = new Sprite("spark");
			sprStatus = new Sprite("status");
			sprArrowIcons = new Sprite("arrow_icons");
			sprTombStone = new SpriteFrame("tombstone");
			sprLevelUp = new Sprite("levelup");
			sprKingHeadIcons = new Sprite[2];
			sprKingHeadIcons[0] = new Sprite("king_head_icons");
			sprKingHeadIcons[1] = new Sprite("king_head_icons", (byte)0);
			sprCursor.setExternalFrameSequence(CURSOR_FRAME_SEQUENCES[0]);
			sprCursorCopy = new Sprite(sprCursor);
			sprCursorCopy.setExternalFrameSequence(CURSOR_FRAME_SEQUENCES[3]);
			sprWaterPair = new SpriteFrame[2];
			var_1c69 = WATER_PAIR[0];
			sprWaterPair[0] = sfmTiles[WATER_PAIR[0]];
			sprWaterPair[1] = sfmTiles[WATER_PAIR[1]];
			sprBSmoke = new Sprite("b_smoke");
		}
	}

	public final void keyPressed(int var1, int var2)
	{
		// Cheat codes logic removed/simplified
	}

	public final void showNotify()
	{
		statusBarNeedsRepaint = true;
		tileIconNeedsRepaint = true;
		combatFooterNeedsRepaint = true;
		combatHeaderNeedsRepaint = true;
	}

	// Save/Load logic simplified
	public final byte[] getGameSaveData() throws IOException
	{
		return new byte[0]; // Placeholder
	}

	public final void loadGameSaveData(byte[] var1) throws IOException
	{
		// Placeholder
	}

	public static final void loadMainSettings()
	{
		try
		{
			byte[] var0 = Renderer.getRMSData("settings", 0);
			if (var0 != null && var0.length > 0) {
				for(int var1 = 0; var1 < 4; ++var1)
				{
					Renderer.mainSettings[var1] = (var0[0] & 1 << var1) != 0;
				}
			}
		}
		catch(Exception var2)
		{
			return;
		}

	}

	public final void saveMainSettings()
	{
		try
		{
			byte[] var1 = new byte[1];

			for(int var2 = 0; var2 < 4; ++var2)
			{
				if(Renderer.mainSettings[var2])
				{
					var1[0] = (byte)(var1[0] | 1 << var2);
				}
			}

			Renderer.setRMSData("settings", 0, var1);
		}
		catch(Exception var3)
		{
			;
		}
	}

	// Core game logic methods kept as they are essential
	
	public final void update()
	{
		delayCounter += 50; 
		
		if (activeEffects.size() > 0) {
			for(int i=0; i<activeEffects.size(); i++) {
				Sprite s = (Sprite)activeEffects.elementAt(i);
				s.update();
				if(!s.active) {
					activeEffects.removeElementAt(i);
					i--;
				}
			}
		}
		
		// Intro Logic
		if (mode == 0) {
			if (delayCounter > 3000) { // 3 seconds delay
				mode = 4; // Switch to Main Menu
				createCircleMenu(MAIN_MENU_ITEMS, halfWidth_, halfHeight_, this);
			}
		}
		else if (mode == 4) {
			// Menu logic is handled by AuxDisplayable usually
		}
	}

	public final void paint(SpriteBatch batch)
	{
		// Main render loop
		if (mode == 0) {
			// Intro/Menu
			if (sfmLogo != null) {
				sfmLogo.paint(batch, halfWidth_ - sfmLogo.imgwidth/2, halfHeight_ - sfmLogo.imgheight/2);
			}
		} else if (mode == 4) {
			// Main Menu Background
			if (sfmTiles != null && sfmTiles.length > 1) {
				// Draw some background tiles
				for(int x=0; x<width_; x+=24) {
					for(int y=0; y<height_; y+=24) {
						sfmTiles[1].paint(batch, x, y); // Grass
					}
				}
			}
			// Logo
			if (sfmGameLogo != null) {
				sfmGameLogo.paint(batch, halfWidth_ - sfmGameLogo.imgwidth/2, 20);
			} else if (sfmLogo != null) {
				sfmLogo.paint(batch, halfWidth_ - sfmLogo.imgwidth/2, 20);
			}
		}
	}
	
	// Helper methods needed by other classes
	public final Unit getUnit(int x, int y, byte mode) {
		for(int i=0; i<units.size(); i++) {
			Unit u = (Unit)units.elementAt(i);
			if (u.currentMapPosX == x && u.currentMapPosY == y) return u;
		}
		return null;
	}
	
	public final byte getTileType(int x, int y) {
		if (x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) return 0;
		// Need to restore mapData logic
		return 0;
	}
	
	public boolean isFractionBuilding(byte tileId) {
		return tileId >= FRACTION_BUILDINGS && tileId < CUSTOM_TILES;
	}
	
	public void addCombatAnimationSprite(Sprite s) {
		combatAnimationSprites.addElement(s);
	}
	
	public void removeCombatAnimationSprite(Sprite s) {
		combatAnimationSprites.removeElement(s);
	}
	
	public void shakeMap(int time) {
		shakeMapFlag = true;
		shakeMapTime = time;
		shakeMapDelayCounterStart = delayCounter;
	}
	
	// Stub for missing methods called by other classes
	public void menuStateChanged(AuxDisplayable d, int idx, String txt, byte action) {}
	public void loadLevel(int id) {}
	public boolean sub_e68(int x, int y, int team) { return false; }
	public Unit buyUnit(Unit u, int x, int y) { return null; }
	public boolean canBuyUnit(Unit u, int x, int y) { return false; }
	public void clearAttackData() {}
	public void setArrayValuesEx(byte[][] arr, int val) {}
	public void moveCursor(int x, int y) {}
	public void moveMapShowPoint(int x, int y) {}
	public void completeMission() {}
	public void sub_58e() {}
	public void addDownloadedSkirmishLevel(String name, byte[] data) {}
	
	public static void drawOutlinedString(SpriteBatch batch, String str, int x, int y, int anchor, int color, int outlineColor) {
		Renderer.fontA.setColor(Color.BLACK);
		Renderer.fontA.draw(batch, str, x+1, y);
		Renderer.fontA.draw(batch, str, x-1, y);
		Renderer.fontA.draw(batch, str, x, y+1);
		Renderer.fontA.draw(batch, str, x, y-1);
		Renderer.fontA.setColor(Color.WHITE);
		Renderer.fontA.draw(batch, str, x, y);
	}
	
	public String getSkirmishLevelName(int id) {
		if (id < skirmishLevelsNames.length) return skirmishLevelsNames[id];
		return "Unknown";
	}
	
	// Paint helper stubs for compatibility
	public final void paintMap(SpriteBatch g) {}
	public final void drawLoadingString(SpriteBatch g) {}
	public final void paintIntro(SpriteBatch g) {}
	public final void paintIntroTransition(SpriteBatch g) {}
	public final void sub_b8e(SpriteBatch var1, int var2, int var3, int var4, int var5) {}
	public final void drawCombatHeader(SpriteBatch var1, Unit var2, Unit var3, int var4) {}
	public final void drawSoftButton(SpriteBatch var1, int var2, int var3, int var4) {}
	public static final void drawWavedImage(SpriteBatch g, int var1, int var2, int dir, SpriteFrame img, int x, int y, int var7) {}
	public static final void drawDrap(SpriteBatch g, int color, int var2, int var3, int var4, int x, int y, int width, int height) {}
	public final void paintCombatAnimation(SpriteBatch g) {}

	public Sprite createSimpleSparkSprite(Sprite baseSprite, int x, int y, int bounceDelta, int bounceMode, int delay, int mode) {
		Sprite s = Sprite.createSimpleSparkSprite(baseSprite, x, y, bounceDelta, bounceMode, delay, (byte)mode);
		if (activeEffects == null) activeEffects = new Vector();
		activeEffects.addElement(s);
		return s;
	}
	
	public final void createCircleMenu(byte[] var1, int var2, int var3, PaintableObject var4)
	{
		AuxDisplayable auxdisp = new AuxDisplayable((byte)0, 0);

		var_21e9 = var2;
		var_223e = var3;

		int var6 = var1.length;
		Vector var7 = new Vector(var6);
		Vector var8 = new Vector(var6);

		for(int var9 = 0; var9 < var6; ++var9)
		{
			byte var10 = var1[var9];
			if(macrospaceHighScoreUpload || var10 != 6)
			{
				var7.addElement(MENU_ITEMS_TEXT[var10]);
				var8.addElement(sfmMenuIcons[var10]);
			}
		}

		String[] var12 = new String[var7.size()];
		SpriteFrame[] var11 = new SpriteFrame[var8.size()];

		var7.copyInto(var12);
		var8.copyInto(var11);

		auxdisp.initCircleMenuAuxDisplayable(var12, var11, halfWidth_, var_21e9, var_223e, 3, (byte)1);
		auxdisp.setNextDisplayable(var4);

		PaintableObject.currentRenderer.setCurrentDisplayable(auxdisp);
	}

	public final void createInGameMiniMenu(byte[] var1, Unit var2)
	{
		auxInGameMenu = new AuxDisplayable((byte)0, 0);
		int var3;
		String[] var4 = new String[var3=var1.length];
		SpriteFrame[] var5 = new SpriteFrame[var3];

		for(int var6 = 0; var6 < var1.length; ++var6)
		{
			var4[var6] = INGAME_ACTIONS_TEXT[var1[var6]];
			var5[var6] = sprActionIcons[var1[var6]];
		}

		if(cursorMapY * 24 <= height_ / 2 - 24)
		{
			auxInGameMenu.initMiniListAuxDisplayable(var4, var5, 0, height_ - sprButtons.height, 36);
		}
		else
		{
			auxInGameMenu.initMiniListAuxDisplayable(var4, var5, width_, 0, 8);
		}

		auxInGameMenu.setNextDisplayable(this);
		PaintableObject.currentRenderer.setCurrentDisplayable(auxInGameMenu);
	}
}
