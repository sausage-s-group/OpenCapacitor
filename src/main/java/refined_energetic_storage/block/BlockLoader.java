package refined_energetic_storage.block;

import net.minecraft.block.Block;
import refined_energetic_storage.RefinedEnergeticStorage;

import static sausage_core.api.util.common.SausageUtils.nonnull;

public class BlockLoader {

    public final static Block controller = nonnull();

    public static void init() {
        RefinedEnergeticStorage.IB.addBlock("controller", new BlockController());
    }

}
