package open_capacitor;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import open_capacitor.init.OCBlocks;
import open_capacitor.init.OCItems;
import open_capacitor.tile.TileController;
import open_capacitor.tile.TileDriver;
import sausage_core.api.util.common.SausageUtils;

public class OCContent {

    public static void preInit(FMLPreInitializationEvent event) {
        OCBlocks.init();
        OCItems.init();
    }

    public static void init(FMLInitializationEvent event) {
        SausageUtils.registerTileEntities(OpenCapacitor.MODID, TileController.class, TileDriver.class);
    }

    public static void postInit(FMLPostInitializationEvent event) {

    }

}
