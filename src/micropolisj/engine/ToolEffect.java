// This file is part of MicropolisJ.
// Copyright (C) 2013 Jason Long
// Portions Copyright (C) 1989-2007 Electronic Arts Inc.
//
// MicropolisJ is free software; you can redistribute it and/or modify
// it under the terms of the GNU GPLv3, with additional terms.
// See the README file, included in this distribution, for details.

package micropolisj.engine;

import static micropolisj.engine.TileConstants.CLEAR;
import micropolisj.util.Utilities;

class ToolEffect implements ToolEffectIfc {
	final Micropolis city;
	final ToolPreview preview;
	public final int originX;
	public final int originY;
	int playerID;

	public ToolEffect(Micropolis city) {
		this(city, 0);
	}
	
	public ToolEffect(Micropolis city, int playerID) {
	    this(city, 0, 0, playerID);
	}

	public ToolEffect(Micropolis city, int xpos, int ypos, int playerID) {
		this.city = city;
		this.preview = new ToolPreview(playerID);
		this.originX = xpos;
		this.originY = ypos;
		this.playerID = playerID;
	}

	// implements ToolEffectIfc
	public int getTile(int dx, int dy) {
		int c = preview.getTile(dx, dy);
		if(c != CLEAR) {
			return c;
		}

		if(city.testBounds(originX + dx, originY + dy)) {
			return city.getTile(originX + dx, originY + dy);
		}
		else {
			// tiles outside city's boundary assumed to be
			// tile #0 (dirt).
			return 0;
		}
	}

	// implements ToolEffectIfc
	public void makeSound(int dx, int dy, Sound sound) {
		preview.makeSound(dx, dy, sound);
	}

	// implements ToolEffectIfc
	public void setTile(int dx, int dy, int tileValue) {
		preview.setTile(dx, dy, tileValue);
	}

	// implements ToolEffectIfc
	public void spend(int amount) {
		preview.spend(amount);
	}

	// implements ToolEffectIfc
	public void toolResult(ToolResult tr) {
		preview.toolResult(tr);
	}

	
	//TODO: dont bulldoze enemy buildings
	public ToolResult apply(int playerID) {
		if(originX - preview.offsetX < 0 || originX - preview.offsetX + preview.getWidth() > city.getWidth()
				|| originY - preview.offsetY < 0 || originY - preview.offsetY + preview.getHeight() > city.getHeight()) {
			return ToolResult.UH_OH;
		}

		if(city.getPlayerInfo(playerID).budget.totalFunds < preview.cost) {
			return ToolResult.INSUFFICIENT_FUNDS;
		}

	
		
		boolean anyFound = false;
		for(int y = 0; y < preview.tiles.length; y++) {
			for(int x = 0; x < preview.tiles[y].length; x++) {
				int c = preview.tiles[y][x];
				if(c != CLEAR) {
					city.setTile(originX + x - preview.offsetX, originY + y - preview.offsetY, (char) c);
					anyFound = true;
				}
			}
		}

		for(ToolPreview.SoundInfo si : preview.sounds) {
			city.makeSound(si.x, si.y, si.sound);
		}

		if(anyFound && preview.cost != 0) {
			city.spend(preview.cost, city.getPlayerInfo(playerID));
			return ToolResult.SUCCESS;
		}
		else {
			return preview.toolResult;
		}
	}
	public int getPlayerID() {
	    return playerID;
	}

}
