package open_capacitor.init;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import open_capacitor.item.ItemChip;
import open_capacitor.item.ItemDisc;

import static open_capacitor.OpenCapacitor.IB;
import static open_capacitor.OpenCapacitor.MODID;
import static sausage_core.api.util.common.SausageUtils.nonnull;

@ObjectHolder(MODID)
public class OCItems {
	public static final ItemChip chip = nonnull();
	public static final ItemDisc disc = nonnull();
	public static final Item disc_housing = nonnull();
	public static void init() {
		IB.addItem("chip", new ItemChip());
		IB.addItem("disc", new ItemDisc());
		IB.addItem("disc_housing");
	}
}
