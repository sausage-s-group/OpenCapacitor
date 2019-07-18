package open_capacitor.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import open_capacitor.util.EnergySize;
import open_capacitor.util.UnitPrefix;

import javax.annotation.Nullable;
import java.util.List;

public class ItemChip extends Item {


	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(isInCreativeTab(tab)) {
			items.add(with(new EnergySize(256, UnitPrefix.kilo)));
			for(UnitPrefix prefix : UnitPrefix.values()) {
				switch(prefix) {
					default:
						for(int i = 1; i < 1024; i *= 4) {
							items.add(with(new EnergySize(i, prefix)));
						}
					case NONE:
					case kilo:
					case Bronto:
					case Nona:
					case Dogga:
					case Corydon:
					case Xero:
				}
			}
		}
	}

	public ItemStack with(EnergySize size) {
		ItemStack stack = new ItemStack(this, 1, 0);
		stack.setTagCompound(size.toNBT());
		return stack;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt != null) {
			EnergySize size = EnergySize.fromNBT(nbt);
			tooltip.add(size.toString());
		}
	}
}
