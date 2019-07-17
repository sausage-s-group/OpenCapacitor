package refined_energetic_storage.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import refined_energetic_storage.RefinedEnergeticStorage;
import refined_energetic_storage.block.BlockLoader;

public class RESContent {

    public static void preInit(FMLPreInitializationEvent event) {

    }

    public static void init(FMLInitializationEvent event) {
        BlockLoader.init();
        RefinedEnergeticStorage.IB.registerAll();
    }

    public static void postInit(FMLPostInitializationEvent event) {

    }

}
