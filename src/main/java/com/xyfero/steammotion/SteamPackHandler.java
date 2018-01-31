package com.xyfero.steammotion;

import ibxm.Player;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashSet;
import java.util.Set;

public class SteamPackHandler {
    private Set<EntityPlayer> players = new HashSet<>();

    public void addPlayer(EntityPlayer player) {
        players.add(player);
    }

    public void onTick() {
        for(EntityPlayer player : players) {

        }
    }
}
