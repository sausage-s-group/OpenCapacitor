package open_capacitor;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;
import sausage_core.api.util.common.SausageUtils;
import sausage_core.api.util.registry.IBRegistryManager;

import javax.annotation.Nonnull;

@Mod(modid = OpenCapacitor.MODID, name = OpenCapacitor.NAME, version = OpenCapacitor.VERSION, acceptedMinecraftVersions = "1.12.2")
@Mod.EventBusSubscriber
public class OpenCapacitor {
    public static final String MODID = "open_capacitor";
    public static final String NAME = "OpenCapacitor";
    public static final String VERSION = "1.0";
    public static final IBRegistryManager IB = new IBRegistryManager(OpenCapacitor.MODID,
            new CreativeTabs(OpenCapacitor.MODID) {
                @Override
                @Nonnull
                public ItemStack createIcon() {
                    return new ItemStack(Blocks.GRASS);
                }
            }
    );
    @Instance(OpenCapacitor.MODID)
    public static OpenCapacitor instance;

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        OCContent.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        OCContent.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        OCContent.postInit(event);
    }

}
