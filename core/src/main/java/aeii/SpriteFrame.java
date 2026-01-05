package aeii;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.io.InputStream;

public final class SpriteFrame
{

	public TextureRegion image;
	private boolean useTransform = false;
	private int frameX;
	private int frameY;
	public int imgwidth;
	public int imgheight;
	private int translateX;
	private int translateY;
	public int transform = 0;


	public SpriteFrame(SpriteFrame var1, int var2, int var3, int var4, int var5)
	{
		image = var1.image;
		imgwidth = var4;
		imgheight = var5;
		frameX = var2 * var4 + var1.frameX;
		frameY = var3 * var5 + var1.frameY;
		useTransform = true;
	}

	public SpriteFrame(SpriteFrame var1, int var2)
	{
		image = var1.image;
		imgwidth = var1.imgwidth;
		imgheight = var1.imgheight;
		frameX = var1.frameX;
		frameY = var1.frameY;
		translateX = var1.translateX;
		translateY = var1.translateY;
		useTransform = var1.useTransform;

		if((var2 & 1) != 0)
		{
			transform = 2;
		}
		else if((var2 & 2) != 0)
		{
			transform = 1;
		}
		else if((var2 & 4) != 0)
		{
			transform = 6;
		}
		else if((var2 & 8) != 0)
		{
			transform = 3;
		}
		else if((var2 & 16) != 0)
		{
			transform = 5;
		}
	}

	public SpriteFrame(String var1, int width, int height)
	{
		// Note: ImageProcessor scaling ignored for now, using raw image. 
		// LibGDX handles drawing scaling better.
		// If explicit scaling is needed, Pixmap resizing should be used.
		byte[] var2 = Renderer.getResource(var1 + ".png");
		if (var2 == null) {
			// Fallback if not found in cache/res (should be in assets)
			var2 = Gdx.files.internal(var1 + ".png").readBytes();
		}
		
		Pixmap pixmap = new Pixmap(var2, 0, var2.length);
		image = new TextureRegion(new Texture(pixmap));
		pixmap.dispose();
		
		imgwidth = image.getRegionWidth();
		imgheight = image.getRegionHeight();
		useTransform = false;
	}

	public SpriteFrame(String var1)
	{
		this(var1, -1, -1);
	}

	public final void align(int mode, int x, int y)
	{
		if(!useTransform)
		{
			int var4 = mode & 13;
			int var5 = mode & 50;
			
			if(transform == 2)
			{
				if((var4 & 4) != 0)
				{
					var4 = 8;
				}
				else if((var4 & 8) != 0)
				{
					var4 = 4;
				}
			}
			else if(transform == 1)
			{
				if((var5 & 16) != 0)
				{
					var4 = 32;
				}
				else if((var4 & 32) != 0)
				{
					var4 = 16;
				}
			}
			
			mode = var4 | var5;
			
			if((mode & 8) != 0)
			{
				translateX = x - imgwidth;
			}
			else if((mode & 1) != 0)
			{
				translateX = x - imgwidth >> 1;
			}

			if((mode & 32) != 0)
			{
				translateY = y - imgheight;
			}
			else if((mode & 2) != 0)
			{
				translateY = y - imgheight >> 1;
			}
		}
	}

	public final void translate(int var1, int var2)
	{
		translateX += var1;
		translateY += var2;
	}

	public final void paint(SpriteBatch batch, int var2, int var3)
	{
		paintEx(batch, var2, var3, 20);
	}

	public final void paintEx(SpriteBatch batch, int x, int y, int anchor)
	{
		int drawX = x + translateX;
		int drawY = y + translateY;
		
		// In J2ME graphics (0,0) is top-left.
		// In LibGDX with standard camera (0,0) is usually bottom-left unless camera is y-down.
		// Assuming we will set up camera as y-down to match J2ME coordinates.
		
		int regX = image.getRegionX();
		int regY = image.getRegionY();
		int regW = imgwidth;
		int regH = imgheight;
		
		if (useTransform) {
			regX += frameX;
			regY += frameY;
		} else {
			// standard full image
		}
		
		// J2ME Transform mapping
		boolean flipX = false;
		boolean flipY = false;
		float rotation = 0;
		
		switch (transform) {
			case 2: // TRANS_MIRROR
				flipX = true;
				break;
			case 1: // TRANS_MIRROR_ROT180 (Flip H + Rot 180 = Flip V)
				flipY = true;
				break;
			case 3: // TRANS_ROT180
				rotation = 180;
				break;
			case 6: // TRANS_ROT270
				rotation = 90; // LibGDX rotation is counter-clockwise? J2ME is clockwise? 
				// J2ME ROT270 is 270 clockwise = 90 counter-clockwise.
				break;
			case 5: // TRANS_ROT90
				rotation = -90; // 90 clockwise = -90 counter
				break;
			// Combined transforms might need review
		}
		
		// We need to use draw with region params to handle flip/rotation correctly
		// batch.draw(texture, x, y, originX, originY, width, height, scaleX, scaleY, rotation, srcX, srcY, srcWidth, srcHeight, flipX, flipY)
		
		batch.draw(image.getTexture(), 
				drawX, drawY, 
				0, 0, // Origin (0,0 for top-left logic usually, but rotation pivots around origin)
				imgwidth, imgheight, 
				1f, 1f, 
				rotation, 
				regX, regY, 
				regW, regH, 
				flipX, flipY);
	}

	public SpriteFrame(String name, int fraction)
	{
		byte[] imgdata = null;

		if(fraction >= 0)
		{
			// Try to find in cache first via Renderer (which we need to update too)
			imgdata = Renderer.getResource(MainDisplayable.FRACTION_COLOR_PREFIXES[fraction / 2] + "/" + name + ".png");
		}

		if(imgdata == null)
		{
			imgdata = Renderer.getResource(name + ".png");
		}
		
		if (imgdata == null) {
			// Fallback to direct file load
			try {
				if(fraction >= 0) {
					try {
						imgdata = Gdx.files.internal(MainDisplayable.FRACTION_COLOR_PREFIXES[fraction / 2] + "/" + name + ".png").readBytes();
					} catch (Exception e) {}
				}
				if (imgdata == null) {
					imgdata = Gdx.files.internal(name + ".png").readBytes();
				}
			} catch (Exception e) {
				Gdx.app.error("SpriteFrame", "Could not load image: " + name);
			}
		}

		if(imgdata != null && fraction >= 0 && (fraction & 1) == 1)
		{
			byte[] temp = new byte[imgdata.length];
			System.arraycopy(imgdata, 0, temp, 0, imgdata.length);
			replacePaletteColor(temp, 0);
			imgdata = temp;
		}

		if (imgdata != null) {
			Pixmap p = new Pixmap(imgdata, 0, imgdata.length);
			image = new TextureRegion(new Texture(p));
			p.dispose();
			
			imgwidth = (short)image.getRegionWidth();
			imgheight = (short)image.getRegionHeight();
		} else {
			imgwidth = 0;
			imgheight = 0;
		}
	}

	public static final void replacePaletteColor(byte[] imgdata, int fraction)
	{
		try
		{
			int var2 = 33;
			int pltStartIndex = 0;

			int pltLength;
			for(pltLength = imgdata.length - 3; pltStartIndex < pltLength; ++pltStartIndex)
			{
				if(imgdata[pltStartIndex] == 80 && imgdata[pltStartIndex + 1] == 76 && imgdata[pltStartIndex + 2] == 84)
				{
					var2 = pltStartIndex - 4;
					break;
				}
			}

			pltLength = ((imgdata[var2] & 255) << 24 | (imgdata[var2 + 1] & 255) << 16 | (imgdata[var2 + 2] & 255) << 8 | imgdata[var2 + 3] & 255) & -1;
			pltStartIndex = var2 + 4;
			int var5 = -1;

			for(int var6 = 0; var6 < 4; ++var6)
			{
				var5 = updateCRC(imgdata[pltStartIndex + var6], var5);
			}

			pltStartIndex += 4;

			int pltIndex;

			for(pltIndex = pltStartIndex; pltIndex < pltStartIndex + pltLength; pltIndex += 3)
			{
				int cr = imgdata[pltIndex] & 255;
				int cg = imgdata[pltIndex + 1] & 255;
				int cb = imgdata[pltIndex + 2] & 255;

				if(true) // fraction == 0)
				{
					if(cr != 244 || cg != 244 || cb != 230)
					{
						int var13;
						cr = var13 = (cr + cg + cb) / 3;
						imgdata[pltIndex] = (byte)cr;
						imgdata[pltIndex + 1] = (byte)var13;
						imgdata[pltIndex + 2] = (byte)var13;
					}
				}
//				else if(fraction != 1)
//				{
//					int[][] repSourceColors = MainDisplayable.PALETTE_REPLACE_TABLE[1];
//					int[][] repDestColors = MainDisplayable.PALETTE_REPLACE_TABLE[fraction];
//
//					for(int repColorIndex = 0; repColorIndex < repSourceColors.length; ++repColorIndex)
//					{
//						if(repSourceColors[repColorIndex][0] == cr && repSourceColors[repColorIndex][1] == cg && repSourceColors[repColorIndex][2] == cb)
//						{
//							imgdata[pltIndex] = (byte)repDestColors[repColorIndex][0];
//							imgdata[pltIndex + 1] = (byte)repDestColors[repColorIndex][1];
//							imgdata[pltIndex + 2] = (byte)repDestColors[repColorIndex][2];
//							break;
//						}
//					}
//				}

				var5 = updateCRC(imgdata[pltIndex], var5);
				var5 = updateCRC(imgdata[pltIndex + 1], var5);
				var5 = updateCRC(imgdata[pltIndex + 2], var5);
			}

			var5 = ~var5;
			pltIndex = var2 + 8 + pltLength;
			imgdata[pltIndex] = (byte)(var5 >> 24);
			imgdata[pltIndex + 1] = (byte)(var5 >> 16);
			imgdata[pltIndex + 2] = (byte)(var5 >> 8);
			imgdata[pltIndex + 3] = (byte)var5;
		}
		catch(Exception var16)
		{
			var16.printStackTrace();
		}
	}

	public static final int updateCRC(byte var0, int var1)
	{
		int var2 = var0 & 255;
		var1 ^= var2;

		for(int var3 = 0; var3 < 8; ++var3)
		{
			if((var1 & 1) != 0)
			{
				var1 = var1 >>> 1 ^ -306674912;
			}
			else
			{
				var1 >>>= 1;
			}
		}

		return var1;
	}
}
