package de.zonlykroks.persephone.check.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import de.zonlykroks.persephone.util.PersephonePlayer;
import de.zonlykroks.persephone.util.PlayerUtils;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class NPC extends PacketListenerAbstract {

    private final Random rand = new Random();

    private final String displayName;
    private final int ping;

    private final GameMode gameMode;
    private int entityId;

    private final UUID uuid;

    private final UserProfile userProfile;

    private final Location location;

    private final Player target;

    public WrapperPlayServerPlayerInfo.PlayerData playerData;

    protected NPC(Player player,String displayName, Location location,GameMode gameMode, int ping) {
        this.target = player;
        this.displayName = displayName;
        this.gameMode = gameMode;
        this.ping = ping;
        this.uuid = UUID.randomUUID();
        this.userProfile = new UserProfile(this.uuid,this.displayName);
        this.location = location;

        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    public void spawn() {
        this.playerData = new WrapperPlayServerPlayerInfo.PlayerData(
                Component.text(this.displayName),
                this.userProfile,
                this.gameMode,
                this.ping);

        this.entityId = SpigotReflectionUtil.generateEntityId();
        
        WrapperPlayServerPlayerInfo wrapperPlayServerPlayerInfo = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.ADD_PLAYER,playerData);
        WrapperPlayServerSpawnPlayer wrapperPlayServerSpawnEntity = new WrapperPlayServerSpawnPlayer(entityId,this.uuid, SpigotConversionUtil.fromBukkitLocation(this.location));
        WrapperPlayServerEntityRotation wrapperPlayClientPlayerRotation = new WrapperPlayServerEntityRotation(this.entityId,rand.nextFloat(180),rand.nextFloat(180), PlayerUtils.isOnGround(this.location));

        WrapperPlayServerEntityEquipment wrapperPlayServerEntityEquipment = new WrapperPlayServerEntityEquipment(entityId, List.of(
                new Equipment(EquipmentSlot.HELMET, SpigotConversionUtil.fromBukkitItemStack(new ItemStack(getHelmet()))),
                new Equipment(EquipmentSlot.CHEST_PLATE,SpigotConversionUtil.fromBukkitItemStack(new ItemStack(getChestPlate()))),
                new Equipment(EquipmentSlot.LEGGINGS,SpigotConversionUtil.fromBukkitItemStack(new ItemStack(getLeggings()))),
                new Equipment(EquipmentSlot.BOOTS,SpigotConversionUtil.fromBukkitItemStack(new ItemStack(getBoots())))));

        WrapperPlayServerPlayerInfoRemove wrapperPlayServerPlayerInfoRemove = new WrapperPlayServerPlayerInfoRemove(this.uuid);

        Object channel = SpigotReflectionUtil.getChannel(this.target);

        PacketEvents.getAPI().getProtocolManager().sendPacket(channel,wrapperPlayServerPlayerInfo);
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel,wrapperPlayServerSpawnEntity);
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel,wrapperPlayServerEntityEquipment);
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel,wrapperPlayClientPlayerRotation);
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel,wrapperPlayServerPlayerInfoRemove);
    }

    public void deleteNPC() {
        WrapperPlayServerPlayerInfo wrapperPlayServerPlayerInfo = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER,this.playerData);
        WrapperPlayServerDestroyEntities wrapperPlayServerDestroyEntities = new WrapperPlayServerDestroyEntities(this.entityId);
        Object channel = SpigotReflectionUtil.getChannel(this.target);
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel,wrapperPlayServerDestroyEntities);
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel,wrapperPlayServerPlayerInfo);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            PersephonePlayer persephonePlayer = PersephonePlayer.getPlayer(event.getUser().getUUID());
            WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = new WrapperPlayClientInteractEntity(event);
            if(wrapperPlayClientInteractEntity.getEntityId() == this.entityId) {
                NPCManager.handleHit(persephonePlayer);
            }
        }
    }

    public Material getHelmet() {
        int r = rand.nextInt(5);

        if(r == 0) {
            return Material.LEATHER_HELMET;
        }else if(r == 1) {
            return Material.CHAINMAIL_HELMET;
        }else if(r == 2) {
            return Material.IRON_HELMET;
        }else if(r == 3) {
            return Material.GOLDEN_HELMET;
        }else if(r == 4) {
            return Material.TURTLE_HELMET;
        }else {
            return Material.NETHERITE_HELMET;
        }
    }

    public Material getChestPlate() {
        int r = rand.nextInt(4);

        if(r == 0) {
            return Material.LEATHER_CHESTPLATE;
        }else if(r == 1) {
            return Material.CHAINMAIL_CHESTPLATE;
        }else if(r == 2) {
            return Material.IRON_CHESTPLATE;
        }else if(r == 3) {
            return Material.GOLDEN_CHESTPLATE;
        }else {
            return Material.NETHERITE_CHESTPLATE;
        }
    }

    public Material getLeggings() {
        int r = rand.nextInt(4);

        if(r == 0) {
            return Material.LEATHER_LEGGINGS;
        }else if(r == 1) {
            return Material.CHAINMAIL_LEGGINGS;
        }else if(r == 2) {
            return Material.IRON_LEGGINGS;
        }else if(r == 3) {
            return Material.GOLDEN_LEGGINGS;
        }else {
            return Material.NETHERITE_LEGGINGS;
        }
    }

    public Material getBoots() {
        int r = rand.nextInt(4);

        if(r == 0) {
            return Material.LEATHER_BOOTS;
        }else if(r == 1) {
            return Material.CHAINMAIL_BOOTS;
        }else if(r == 2) {
            return Material.IRON_BOOTS;
        }else if(r == 3) {
            return Material.GOLDEN_BOOTS;
        }else {
            return Material.NETHERITE_BOOTS;
        }
    }
}
