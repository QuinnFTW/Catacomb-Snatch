package com.mojang.mojam.level.tile;

import com.mojang.mojam.level.Level;
import com.mojang.mojam.screen.Art;
import com.mojang.mojam.screen.Bitmap;
import com.mojang.mojam.screen.Screen;

public class StairFloorRightTile extends Tile {
	public static final int COLOR = 0xffC0C0C0;
	private static final String NAME = "STAIRFLOORLEFT";

	public StairFloorRightTile() {
		img = 1;
		minimapColor = Art.stairFloorRightTileColors[img & 7][img / 8];
	}
	public void init(Level level, int x, int y) {
		super.init(level, x, y);
	}

	public void render(Screen screen) {
		super.render(screen);
	}

	public int getColor() {
		return StairFloorRightTile.COLOR;
	}

	public String getName() {
		return NAME;
	}

	@Override
	public Bitmap getBitMapForEditor() {
		return Art.stairFloorRightTiles[5][0];
	}
	
	@Override
	public int getMiniMapColor() {
		return minimapColor;
	}
}
