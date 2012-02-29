package com.mojang.mojam.level.gamemode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.mojang.mojam.MojamComponent;
import com.mojang.mojam.entity.building.ShopItem;
import com.mojang.mojam.entity.mob.SpawnableEnemy;
import com.mojang.mojam.entity.mob.Team;
import com.mojang.mojam.entity.mob.SpikeTrap;
import com.mojang.mojam.level.DifficultyInformation;
import com.mojang.mojam.level.HoleTile;
import com.mojang.mojam.level.Level;
import com.mojang.mojam.level.LevelInformation;
import com.mojang.mojam.level.tile.*;
import com.mojang.mojam.entity.building.SpawnerEntity;
import com.mojang.mojam.entity.loot.Loot;
import com.mojang.mojam.entity.building.Turret;

public class GameMode {

	public static final int LEVEL_BORDER_SIZE = 16;
	
	protected Level newLevel;
	
	public Level generateLevel(LevelInformation li,  int player1Character, int player2Character)  throws IOException {
		BufferedImage bufferedImage;
		//System.out.println("Loading level from file: "+li.getPath());
		if(li.vanilla){
			bufferedImage = ImageIO.read(MojamComponent.class.getResource(li.getPath()));
		} else {
			bufferedImage = ImageIO.read(new File(li.getPath()));
		}
		int w = bufferedImage.getWidth() + LEVEL_BORDER_SIZE;
		int h = bufferedImage.getHeight() + LEVEL_BORDER_SIZE;
		
		newLevel = new Level(w, h, player1Character, player2Character);
		
		processLevelImage(bufferedImage, w, h);
		darkenMap(w, h);
		
		setupPlayerSpawnArea();
		setTickItems();
		setVictoryCondition();
		setTargetScore();
		return newLevel;
	}
	
	private void processLevelImage(BufferedImage bufferedImage, int w, int h) {		
		int[] rgbs = defaultRgbArray(w, h);
		
		bufferedImage.getRGB(0, 0, w - 16, h - 16, rgbs, 8 + 8 * w, w);
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int col = rgbs[x + y * w] & 0xffffff;
				loadColorTile(col, x, y);
			}
		}
	}
	
	private int[] defaultRgbArray(int width, int height) {
		int[] rgbs = new int[width * height];
		Arrays.fill(rgbs, 0xffA8A800);

		for (int y = 0 + 4; y < height - 4; y++) {
			for (int x = (width / 2) - 4; x < (width / 2) + 3; x++) {
				rgbs[x + y * width] = 0xff888800;
			}
		}
		for (int y = 0 + 5; y < height - 5; y++) {
			for (int x = (width / 2) - 2; x < (width / 2) + 1; x++) {
				rgbs[x + y * width] = 0xffA8A800;
			}
		}
		return rgbs;
	}
	
	private void darkenMap(int w, int h) {
		for (int y = 0; y < h + 1; y++) {
			for (int x = 0; x < w + 1; x++) {
				if (x <= 8 || y <= 8 || x >= w - 8 || y >= h - 8) {
					newLevel.getSeen()[x + y * (w + 1)] = true;
				}
			}
		}
	}
	
	protected void loadColorTile(int color, int x, int y) {
		switch (color) {
		case 0xA8A800:
			newLevel.setTile(x, y, new SandTile());
			break;			
		case 0x969696:
			newLevel.setTile(x, y, new UnbreakableRailTile(new FloorTile()));
			break;			
		case 0x888800:
			newLevel.setTile(x, y, new UnpassableSandTile());
			break;
		case 0xFF7777:
			newLevel.setTile(x, y, new DestroyableWallTile());
			break;
		case 0x000000:
			newLevel.setTile(x, y, new HoleTile());
			break;
		case 0xff0000:
			newLevel.setTile(x, y, new WallTile());
			break;
		case 0xCC00FF:
			newLevel.setTile(x, y, new StairTile());
			break;
		case 0x00ffff:
			newLevel.setTile(x, y, new StairLeftTile());
			break;
		case 0x009999:
			newLevel.setTile(x, y, new StairRightTile());
			break;
		case 0xff00ff:
			newLevel.setTile(x, y, new StairFloorLeftTile());
			break;
		case 0xc0c0c0:
			newLevel.setTile(x, y, new StairFloorRightTile());
			break;
		case 0x0000ff:
			newLevel.addEntity(new SpikeTrap(x * Tile.WIDTH,y * Tile.HEIGHT));
			break;
		case 0x006600:
			newLevel.addEntity(new SpawnerEntity(x * Tile.WIDTH+Tile.WIDTH/2,y * Tile.HEIGHT+Tile.HEIGHT/2, SpawnableEnemy.BAT));
			break;
		case 0x009900:
			newLevel.addEntity(new SpawnerEntity(x * Tile.WIDTH+Tile.WIDTH/2,y * Tile.HEIGHT+Tile.HEIGHT/2, SpawnableEnemy.SNAKE));
			break;			
		case 0x00CC00:
			newLevel.addEntity(new SpawnerEntity(x * Tile.WIDTH+Tile.WIDTH/2,y * Tile.HEIGHT+Tile.HEIGHT/2, SpawnableEnemy.MUMMY));
			break;
		case 0x00FF00:
			newLevel.addEntity(new SpawnerEntity(x * Tile.WIDTH+Tile.WIDTH/2,y * Tile.HEIGHT+Tile.HEIGHT/2, SpawnableEnemy.SCARAB));
			break;
		case 0x990099:
			newLevel.addEntity(new Turret(x * Tile.WIDTH+Tile.WIDTH/2,y * Tile.HEIGHT+Tile.HEIGHT/2,Team.Team1));
			break;
		case 0x990066:
			newLevel.addEntity(new Turret(x * Tile.WIDTH+Tile.WIDTH/2,y * Tile.HEIGHT+Tile.HEIGHT/2,Team.Neutral));
			break;
		case 0x990033:
			newLevel.addEntity(new Turret(x * Tile.WIDTH+Tile.WIDTH/2,y * Tile.HEIGHT+Tile.HEIGHT/2,Team.Team2));
			break;
		case 0x100700:
			for (int i = 0; i < 4; i++) {
				double dir = i * Math.PI * 2 / 8;
				newLevel.addEntity(new Loot(x * Tile.WIDTH+Tile.WIDTH/2,y * Tile.HEIGHT+Tile.HEIGHT/2, Math.cos(dir), Math.sin(dir), 200, false));
			}
			break;
		default:
			newLevel.setTile(x, y, new FloorTile());
			break;
		}
	}
	
	protected void setupPlayerSpawnArea() {
		newLevel.maxMonsters = 1500 + (int)DifficultyInformation.calculateStrength(500);	
		
		newLevel.addEntity(new ShopItem(32 * (newLevel.width / 2 - 1.5), 4.5 * 32,
				ShopItem.SHOP_TURRET, Team.Team2));
		newLevel.addEntity(new ShopItem(32 * (newLevel.width / 2 - .5), 4.5 * 32,
				ShopItem.SHOP_HARVESTER, Team.Team2));
		newLevel.addEntity(new ShopItem(32 * (newLevel.width / 2 + .5), 4.5 * 32,
				ShopItem.SHOP_BOMB, Team.Team2));

		newLevel.addEntity(new ShopItem(32 * (newLevel.width / 2 - 1.5), (newLevel.height - 4.5) * 32,
				ShopItem.SHOP_TURRET, Team.Team1));
		newLevel.addEntity(new ShopItem(32 * (newLevel.width / 2 - .5), (newLevel.height - 4.5) * 32,
				ShopItem.SHOP_HARVESTER, Team.Team1));
		newLevel.addEntity(new ShopItem(32 * (newLevel.width / 2 + .5), (newLevel.height - 4.5) * 32,
				ShopItem.SHOP_BOMB, Team.Team1));
		
		newLevel.setTile((newLevel.width / 2) - 1, 7, new UnbreakableRailTile(new SandTile()));	
	    newLevel.setTile((newLevel.width / 2) - 1, newLevel.height - 8, new UnbreakableRailTile(new SandTile()));
	}
	
	protected void setTickItems() {
	}
	
	protected void setVictoryCondition() {
	}
	
	protected void setTargetScore() {
	}
}
