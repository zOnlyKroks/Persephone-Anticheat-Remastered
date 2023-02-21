package de.zonlykroks.persephone.check.impl.block;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.LocationUtils;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.Material;
import org.bukkit.util.RayTraceResult;

@CheckData(name = "Scaffolding", checkType = "A",setbackVl = 2)
public class ScaffoldingA extends Check {

    public ScaffoldingA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) return;

        RayTraceResult rayTraceResult = player.bukkitPlayer.rayTraceBlocks(5);

        if (rayTraceResult == null) {
            if(buffer++ > 4) {
                this.flag(" placed block in air, raytrace-result is null");
                player.bukkitPlayer.getWorld().getBlockAt(player.placedBlockPosition).setType(Material.AIR);
            }
        }else buffer -= buffer > 0 ? 0.5 : 0;
    }
}
