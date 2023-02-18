package de.zonlykroks.persephone.check.npc;

import com.github.retrooper.packetevents.protocol.player.GameMode;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class NPCManager {

    private static final Random rand = new Random();

    public static final Map<UUID,NPC> playerToEntityID = new HashMap<>();

    public static void spawnNPC(Player player, String displayName) {
        NPC npc = new NPC(player,displayName,player.getLocation(),GameMode.SURVIVAL, rand.nextInt(200));
        playerToEntityID.put(player.getUniqueId(),npc);
        npc.spawn();
    }

    public static void handleHit(PersephonePlayer persephonePlayer) {
        if(persephonePlayer.combatNPCHits++ >= 10) {
            persephonePlayer.bukkitPlayer.sendMessage("HAXORMANN HAUT NPC!!");
        }
    }

}
