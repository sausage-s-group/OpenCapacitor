package open_capacitor.init;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import open_capacitor.item.ItemChip;

import static open_capacitor.OpenCapacitor.*;
import static sausage_core.api.util.common.SausageUtils.nonnull;

@ObjectHolder(MODID)
public class OCItems {
	public static final Item chip = nonnull();
	public static void init() {
		IB.addItem("chip", new ItemChip());
	}
}
