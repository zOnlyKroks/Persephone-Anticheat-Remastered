package de.zonlykroks.persephone.processor;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAnimation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.util.PersephonePlayer;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.entity.Entity;

public class CombatProcessor extends PacketListenerAbstract {
    private final PersephonePlayer persephonePlayer;

    public CombatProcessor(PersephonePlayer persephonePlayer) {
        this.persephonePlayer = persephonePlayer;
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(persephonePlayer.bukkitPlayer.getEntityId() == event.getUser().getEntityId()) {
            if(event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
                WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = new WrapperPlayClientInteractEntity(event);

                if(wrapperPlayClientInteractEntity.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                    persephonePlayer.isAttacking = true;

                    Entity entity = SpigotReflectionUtil.getEntityById(wrapperPlayClientInteractEntity.getEntityId());

                    persephonePlayer.lastAttackedEntity = persephonePlayer.attackedEntity;
                    persephonePlayer.attackedEntity = entity;

                    persephonePlayer.lastHitTicks = 0;
                }
            }else if(event.getPacketType() == PacketType.Play.Client.ANIMATION) {
                WrapperPlayClientAnimation wrapperPlayClientAnimation = new WrapperPlayClientAnimation(event);

                persephonePlayer.isSwinging = true;
            }else if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
                persephonePlayer.lastHitTicks++;
                persephonePlayer.isSwinging = false;
            }
        }
    }
}
