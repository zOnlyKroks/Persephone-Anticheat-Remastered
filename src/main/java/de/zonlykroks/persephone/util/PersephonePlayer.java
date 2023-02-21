package de.zonlykroks.persephone.util;

import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.util.Vector3f;
import de.zonlykroks.persephone.check.PlayerSpecificCheckInitiator;
import de.zonlykroks.persephone.processor.*;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class PersephonePlayer {

    public static final Map<UUID,PersephonePlayer> persephonePlayerMap = new ConcurrentHashMap<>();

    public final Player bukkitPlayer;
    public Location from;
    public Location to;

    @Deprecated
    public boolean clientOnGround,clientLastOnGround;

    public boolean serverGround = true,lastServerGround = true,lastLastServerGround = true;

    public float pitch,lastPitch;

    public float deltaPitch,lastDeltaPitch;

    public float yaw,lastYaw;

    public float deltaYaw,lastDeltaYaw;

    public double currentY,lastY,currentX,lastX,currentZ,lastZ;

    public double distanceXZ;

    public double deltaY,lastDeltaY,deltaX,lastDeltaX,deltaZ,lastDeltaZ, accelY;

    public Location placedBlockPosition;
    public BlockFace placedBlockFace;

    public int airTicks;

    public Entity attackedEntity,lastAttackedEntity;

    private final PlayerSpecificCheckInitiator playerSpecificCheckInitiator;

    private final MovementProcessor movementProcessor;
    private final BlockProcessor blockProcessor;
    private final CombatProcessor combatProcessor;

    private final ActionProcessor actionProcessor;

    private final OtherProcessor otherProcessor;

    public InteractionHand useItemHand;

    public boolean sprinting, sneaking, sendingAction, placing, isAttacking,isSwinging;

    public boolean isWindowOpen;

    public double combatNPCHits = 0;

    public int lastHitTicks = 0;

    public float belowBlockFrictionCurrentLocation;

    public Vector3f cursorPosition;

    public InteractionHand currentBlockPlaceInteractionHand;

    public int blockPlaceSequence;


    public PersephonePlayer(Player player) {
        this.bukkitPlayer = player;
        persephonePlayerMap.putIfAbsent(player.getUniqueId(),this);
        from = new Location(bukkitPlayer.getWorld(),0,0,0);
        to = new Location(bukkitPlayer.getWorld(),0,0,0);
        movementProcessor = new MovementProcessor(this);
        blockProcessor = new BlockProcessor(this);
        combatProcessor = new CombatProcessor(this);
        actionProcessor = new ActionProcessor(this);
        otherProcessor = new OtherProcessor(this);


        playerSpecificCheckInitiator = new PlayerSpecificCheckInitiator();
        playerSpecificCheckInitiator.registerChecksForPlayer(this);
    }

    public static PersephonePlayer getPlayer(UUID uuid) {
        return persephonePlayerMap.get(uuid);
    }

    public static void removePlayer(UUID uuid) {persephonePlayerMap.remove(uuid);}

    public boolean isPlayerExempt() {
        return bukkitPlayer.getGameMode() == GameMode.SPECTATOR || bukkitPlayer.getGameMode() == GameMode.CREATIVE;
    }
}
