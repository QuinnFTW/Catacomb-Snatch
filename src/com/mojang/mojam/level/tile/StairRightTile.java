package com.mojang.mojam.level.tile;

import java.util.List;

import com.mojang.mojam.entity.Entity;
import com.mojang.mojam.level.Level;
import com.mojang.mojam.math.BB;
import com.mojang.mojam.network.TurnSynchronizer;
import com.mojang.mojam.screen.Art;
import com.mojang.mojam.screen.Bitmap;
import com.mojang.mojam.screen.Screen;

public class StairRightTile extends Tile {
	static final int WALLHEIGHT = 56;
	public static final int COLOR = 0xff009999;
	private static final String NAME = "STAIR";

	public StairRightTile() {
		img = 3;
		minimapColor = Art.stairRightTileColors[img & 6][img / 8];
	}
	
	public void init(Level level, int x, int y) {
		super.init(level, x, y);
	}

	public boolean canPass(Entity e) {
		return false;
	}

	public void addClipBBs(List<BB> list, Entity e) {
		if (canPass(e))
			return;

		list.add(new BB(this, x * Tile.WIDTH, y * Tile.HEIGHT - 6, (x + 1)
				* Tile.WIDTH, (y + 1) * Tile.HEIGHT));
	}

	public void render(Screen screen) {
		screen.blit(Art.stairRightTiles[img][0], x * Tile.WIDTH, y * Tile.HEIGHT
				- (WALLHEIGHT - Tile.HEIGHT));
	}

	public void renderTop(Screen screen) {
		screen.blit(Art.stairRightTiles[img][0], x * Tile.WIDTH, y * Tile.HEIGHT
				- (WALLHEIGHT - Tile.HEIGHT), 32, 32);
	}

	public boolean isBuildable() {
		return false;
	}

	public boolean castShadow() {
		return true;
	}

	public int getColor() {
		return StairRightTile.COLOR;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return StairRightTile.NAME;
	}

	@Override
	public Bitmap getBitMapForEditor() {
		return Art.stairRightTiles[0][0];
	}
	
	@Override
	public int getMiniMapColor() {
		return  minimapColor;
	}
}
