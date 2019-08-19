package open_capacitor;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import open_capacitor.init.OCItems;
import org.apache.logging.log4j.Logger;
import sausage_core.api.core.common.ItemGroup;
import sausage_core.api.util.registry.IBRegistryManager;

@Mod(modid = OpenCapacitor.MODID, name = OpenCapacitor.NAME, version = OpenCapacitor.VERSION, acceptedMinecraftVersions = "1.12.2")
@Mod.EventBusSubscriber
public class OpenCapacitor {
    public static final String MODID = "open_capacitor";
    public static final String NAME = "OpenCapacitor";
    public static final String VERSION = "1.0";
    public static final IBRegistryManager IB = new IBRegistryManager(OpenCapacitor.MODID,
            new ItemGroup(OpenCapacitor.MODID, () -> new ItemStack(OCItems.disc_housing)));

    @Instance(OpenCapacitor.MODID)
    public static OpenCapacitor instance;

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        OCContent.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        OCContent.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        OCContent.postInit();
    }

}
