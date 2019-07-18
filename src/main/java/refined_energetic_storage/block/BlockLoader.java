package refined_energetic_storage.block;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import refined_energetic_storage.RefinedEnergeticStorage;

import static sausage_core.api.util.common.SausageUtils.nonnull;

@GameRegistry.ObjectHolder(RefinedEnergeticStorage.MODID)
public class BlockLoader {

    public static final Block controller = nonnull();
    public static final Block test_device = nonnull();

    public static void init() {
        RefinedEnergeticStorage.IB.addBlock("controller", new BlockController());
        RefinedEnergeticStorage.IB.addBlock("test_device", new BlockTestDevice());
    }

}
