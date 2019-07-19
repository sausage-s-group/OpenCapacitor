package open_capacitor.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import open_capacitor.block.BlockController;
import open_capacitor.block.BlockDriver;

import static open_capacitor.OpenCapacitor.*;
import static sausage_core.api.util.common.SausageUtils.nonnull;

@ObjectHolder(MODID)
public class OCBlocks {

    public static final Block controller = nonnull();
    public static final Block driver = nonnull();

    public static void init() {
        IB.addBlock("controller", new BlockController());
        IB.addBlock("driver", new BlockDriver());
    }

}
