package de.zonlykroks.persephone.check;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.Bukkit;

public class Check extends PacketListenerAbstract {

    public int violations;
    public int setbackVL;

    public String name;
    public String checkType;
    public boolean setback,experimental;
    protected final PersephonePlayer player;

    protected float buffer = 0;

    public Check(PersephonePlayer player) {
        this.player = player;
    }

    public void flag(String debug) {
        violations++;

        if(setback && violations > setbackVL) {
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendMessage(debug));
        }
    }
}
