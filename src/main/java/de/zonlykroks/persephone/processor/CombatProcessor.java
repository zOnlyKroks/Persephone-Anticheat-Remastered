package de.zonlykroks.persephone.processor;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import de.zonlykroks.persephone.Persephone;
import de.zonlykroks.persephone.check.npc.NPC;
import de.zonlykroks.persephone.check.npc.NPCManager;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

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
                Bukkit.getScheduler().callSyncMethod(Persephone.persephone,() -> {
                    for(Entity entity : persephonePlayer.bukkitPlayer.getWorld().getEntities()) {
                        boolean contains = false;
                        for(NPC npc : NPCManager.playerToEntityID.values()) {
                            if(npc.getEntityId() == entity.getEntityId())
                                contains = true;
                        }

                        if(entity.getEntityId() == wrapperPlayClientInteractEntity.getEntityId() && !contains) {
                            persephonePlayer.lastAttackedEntity = persephonePlayer.attackedEntity;
                            persephonePlayer.attackedEntity = entity;

                            persephonePlayer.lastHitTicks = -1;
                        }
                    }

                    persephonePlayer.lastHitTicks++;

                    return true;
                });
            }
        }
    }
}
