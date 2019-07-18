package refined_energetic_storage.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import refined_energetic_storage.RefinedEnergeticStorage;
import refined_energetic_storage.block.BlockController;
import refined_energetic_storage.block.BlockTestDevice;

import static sausage_core.api.util.common.SausageUtils.nonnull;

@ObjectHolder(RefinedEnergeticStorage.MODID)
public class RESBlocks {

    public static final Block controller = nonnull();
    public static final Block test_device = nonnull();

    public static void init() {
        RefinedEnergeticStorage.IB.addBlock("controller", new BlockController());
        RefinedEnergeticStorage.IB.addBlock("test_device", new BlockTestDevice());
    }

}
