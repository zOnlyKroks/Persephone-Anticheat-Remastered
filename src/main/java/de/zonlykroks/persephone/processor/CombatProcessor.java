package de.zonlykroks.persephone.processor;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAnimation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.Persephone;
import de.zonlykroks.persephone.check.npc.NPC;
import de.zonlykroks.persephone.check.npc.NPCManager;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

public class CombatProcessor extends PacketListenerAbstract {
    private final PersephonePlayer persephonePlayer;

    public CombatProcessor(PersephonePlayer persephonePlayer) {
        this.persephonePlayer = persephonePlayer;
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = new WrapperPlayClientInteractEntity(event);

            if(wrapperPlayClientInteractEntity.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                persephonePlayer.isAttacking = true;
                Bukkit.getScheduler().callSyncMethod(Persephone.persephone,() -> {
                    for(Entity entity : persephonePlayer.bukkitPlayer.getWorld().getEntities()) {
                        if (NPCManager.playerToEntityID.values().stream().map(npc -> npc.getEntityId() == wrapperPlayClientInteractEntity.getEntityId()).toList().size() != 0) {
                            if (entity.getLocation().toVector().equals(new Vector(wrapperPlayClientInteractEntity.getTarget().get().x, wrapperPlayClientInteractEntity.getTarget().get().y, wrapperPlayClientInteractEntity.getTarget().get().z))) {

                                System.out.println("Found entity");

                                persephonePlayer.lastAttackedEntity = persephonePlayer.attackedEntity;
                                persephonePlayer.attackedEntity = entity;

                                persephonePlayer.lastHitTicks = 0;
                            }
                        }
                    }
                    return true;
                });
            }
        }else if(event.getPacketType() == PacketType.Play.Client.ANIMATION) {
            WrapperPlayClientAnimation wrapperPlayClientAnimation = new WrapperPlayClientAnimation(event);

            persephonePlayer.isSwinging = true;
        }else if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            persephonePlayer.lastHitTicks++;
        }
    }
}
