package open_capacitor.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import open_capacitor.init.OCItems;
import open_capacitor.util.EnergySize;
import sausage_core.api.util.common.SausageUtils;

public class ItemDisc extends ItemChip {
    public ItemDisc() {
        setMaxStackSize(1);
        setMaxDamage(101);
        setNoRepair();
        SausageUtils.register(MinecraftForge.EVENT_BUS, AnvilUpdateEvent.class,
                event -> event.setCanceled(event.getLeft().getItem() == this));
    }

    @Override
    public ItemStack with(EnergySize size) {
        ItemStack with = super.with(size);
        with.setItemDamage(with.getMaxDamage());
        return with;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (playerIn.isSneaking()) {
            ItemHandlerHelper.giveItemToPlayer(playerIn, OCItems.chip.with(sizeOf(playerIn.getHeldItem(handIn))));
            return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(OCItems.disc_housing));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
