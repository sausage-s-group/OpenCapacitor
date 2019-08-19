package open_capacitor;

import open_capacitor.init.OCBlocks;
import open_capacitor.init.OCItems;
import open_capacitor.tile.TileController;
import open_capacitor.tile.TileDriver;
import sausage_core.api.util.common.SausageUtils;

public class OCContent {

    public static void preInit() {
        OCBlocks.init();
        OCItems.init();
    }

    public static void init() {
        SausageUtils.registerTileEntities(OpenCapacitor.MODID, TileController.class, TileDriver.class);
    }

    public static void postInit() {

    }

}
