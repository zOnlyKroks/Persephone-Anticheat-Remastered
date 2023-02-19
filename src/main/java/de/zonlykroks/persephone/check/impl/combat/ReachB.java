package de.zonlykroks.persephone.check.impl.combat;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.MathUtil;
import de.zonlykroks.persephone.util.PersephonePlayer;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

@CheckData(name = "Reach", checkType = "B")
public class ReachB extends Check {
    public ReachB(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = new WrapperPlayClientInteractEntity(event);

            if(wrapperPlayClientInteractEntity.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) return;

            final Entity p2 = player.attackedEntity;
            final Player p = player.bukkitPlayer;


            final double distance = MathUtil.getHorizontalDistance(p.getLocation(), p2.getLocation()) - 0.35;
            double maxReach = 4.2;
            final double yawDifference = 180 - Math.abs(Math.abs(p.getEyeLocation().getYaw()) - Math.abs(p2.getLocation().getYaw()));
            final double KB = getKB(p);
            maxReach+= Math.abs(p.getVelocity().length() + p2.getVelocity().length()) * 0.4;
            maxReach+= yawDifference * 0.01;
            maxReach+= p.getPing() * 0.01097;

            if(maxReach < 4.2) {
                maxReach = 4.2;
            }
            if(KB > 0) {
                maxReach += KB;
            }
            if (p2 instanceof Slime) {
                final Slime slime = (Slime) p2;
                maxReach += slime.getSize()/4;
            }
            if (p2 instanceof MagmaCube) {
                final MagmaCube MagmaCube = (MagmaCube) p2;
                maxReach += MagmaCube.getSize()/4;
            }
            if (p2 instanceof Spider) {
                maxReach += 1.0;
            }
            if (p2 instanceof Giant) {
                maxReach += 2.0;
            }

            final String en = p2.getName();

            if(distance > maxReach) {
                flag(MathUtil.trim(3, distance) + " > " + MathUtil.trim(3, maxReach) + "; KB: " + KB + "; Attacked: " + en);
            }
        }
    }

    private int getKB(Player p){
        int enchantmentLevel = 0;
        final ItemStack[] inv = p.getInventory().getContents();
        for(final ItemStack item:inv){
            if (item != null) {
                if(item.getType() != null){
                    if(item.getEnchantments().containsKey(Enchantment.KNOCKBACK)){
                        return enchantmentLevel = item.getEnchantmentLevel(Enchantment.KNOCKBACK);
                    }
                }
            }
        }
        return enchantmentLevel;
    }
}
