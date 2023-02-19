package de.zonlykroks.persephone.check;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import de.zonlykroks.persephone.Persephone;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Check extends PacketListenerAbstract {

    public int violations;
    public int setbackVL;

    public String name;
    public String checkType;
    public boolean damage;
    public float damageAmount;
    public boolean setback,experimental;
    protected final PersephonePlayer player;

    protected float buffer = 0;

    public int totalFailedTimes = 0;

    public Check(PersephonePlayer player) {
        this.player = player;
    }

    public void flag(String debug) {
        if(player.isPlayerExempt()) return;

        violations++;

        if(violations > setbackVL) {

            Bukkit.getScheduler().callSyncMethod(Persephone.persephone, () -> {

                if(damage)
                    player.bukkitPlayer.damage(damageAmount);

                return true;
            });

            Bukkit.getOnlinePlayers().forEach(player1 -> {
                player1.sendMessage(ChatColor.DARK_PURPLE + "Persephone: " + ChatColor.RED + player.bukkitPlayer.getName() + "" + ChatColor.GRAY + " failed check: " + this.name + " (" + this.checkType + "), debug: " + debug);
            });

            totalFailedTimes++;
            violations = 0;
        }
    }
}
