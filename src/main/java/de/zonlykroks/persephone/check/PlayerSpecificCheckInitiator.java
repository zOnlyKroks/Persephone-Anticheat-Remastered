package de.zonlykroks.persephone.check;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import de.zonlykroks.persephone.util.PersephonePlayer;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PlayerSpecificCheckInitiator {

    public static final List<String> packagesToScan = new ArrayList<>();

    public void registerChecksForPlayer(PersephonePlayer player) {
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(packagesToScan.toArray(String[]::new)));
        Set<Class<? extends Check>> filtered = reflections.getSubTypesOf(Check.class).stream().filter(aClass -> {
            if(aClass.isAnnotationPresent(CheckData.class)) {
                return true;
            }else {
                System.out.println("The class: " + aClass.getName() + " is not Annotated with the CheckData Annotation! Ignoring it to not throw any errors further down the line!");
                return false;
            }
        }).collect(Collectors.toUnmodifiableSet());

        filtered.forEach(aClass -> {
            try {
                Check check = aClass.getDeclaredConstructor(PersephonePlayer.class).newInstance(player);
                CheckData annotation = aClass.getAnnotation(CheckData.class);
                check.setbackVL = annotation.setbackVl();
                check.name = annotation.name();
                check.checkType = annotation.checkType();
                check.experimental = annotation.experimental();
                check.setback = annotation.setback();
                check.damage = annotation.damage();
                check.damageAmount = annotation.damageAmount();
                PacketEvents.getAPI().getEventManager().registerListener(new PacketListenerAbstract() {
                    @Override
                    public void onPacketReceive(PacketReceiveEvent event) {
                        if(event.getUser().getEntityId() == player.bukkitPlayer.getEntityId())
                            check.onPacketReceive(event);
                    }

                    @Override
                    public void onPacketSend(PacketSendEvent event) {
                        if(event.getUser().getEntityId() == player.bukkitPlayer.getEntityId())
                            check.onPacketSend(event);
                    }
                });
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException e) {
                System.err.println("Could not create new instance for check: " + aClass.getName() + " , cowardly refusing to register this check! Contact the developer to resolve this error. The following excerpt is of great need for the developer!");
                e.printStackTrace();
            }
        });
    }
}
