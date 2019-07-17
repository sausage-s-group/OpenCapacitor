package refined_energetic_storage;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import refined_energetic_storage.common.CommonProxy;

@Mod(modid = RefinedEnergeticStorage.MODID, name = RefinedEnergeticStorage.NAME, version = RefinedEnergeticStorage.VERSION, acceptedMinecraftVersions = "1.12.2")
public class RefinedEnergeticStorage {

    public final static String MODID = "refined_energetic_storage";
    public final static String NAME = "Refined Energetic Storage";
    public final static String VERSION = "1.0";

    @Mod.Instance(RefinedEnergeticStorage.MODID)
    public static RefinedEnergeticStorage instance;

    @SidedProxy(serverSide = "refined_energetic_storage.common.CommonProxy", clientSide = "refined_energetic_storage.client.ClientProxy")
    public CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
