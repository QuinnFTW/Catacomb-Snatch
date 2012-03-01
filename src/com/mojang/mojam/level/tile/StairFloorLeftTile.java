package com.mojang.mojam.level.tile;

import com.mojang.mojam.level.Level;
import com.mojang.mojam.screen.Art;
import com.mojang.mojam.screen.Bitmap;
import com.mojang.mojam.screen.Screen;

public class StairFloorLeftTile extends Tile {
	public static final int COLOR = 0xffFF00FF;
	private static final String NAME = "STAIRFLOORLEFT";

	public StairFloorLeftTile() {
		img = 1;
		minimapColor = Art.stairFloorLeftTileColors[img & 7][img / 8];
	}
	public void init(Level level, int x, int y) {
		super.init(level, x, y);
	}

	public void render(Screen screen) {
		super.render(screen);
	}

	public int getColor() {
		return StairFloorLeftTile.COLOR;
	}

	public String getName() {
		return NAME;
	}

	@Override
	public Bitmap getBitMapForEditor() {
		return Art.stairFloorLeftTiles[5][0];
	}
	
	@Override
	public int getMiniMapColor() {
		return minimapColor;
	}
}
