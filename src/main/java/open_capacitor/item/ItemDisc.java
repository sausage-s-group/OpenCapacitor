package open_capacitor.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import open_capacitor.init.OCItems;
import open_capacitor.util.EnergySize;
import open_capacitor.util.OCEnergyStorage;
import open_capacitor.util.UnitPrefix;
import org.lwjgl.input.Keyboard;
import sausage_core.api.util.common.SausageUtils;
import sausage_core.api.util.math.BigMath;
import sausage_core.api.util.math.UFMBigInt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;

public class ItemDisc extends Item {
    public ItemDisc() {
        setMaxStackSize(1);
        setNoRepair();
        SausageUtils.register(MinecraftForge.EVENT_BUS, AnvilUpdateEvent.class,
                event -> event.setCanceled(event.getLeft().getItem() == this));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(with(new EnergySize(256, UnitPrefix.kilo), false));
            items.add(with(new EnergySize(256, UnitPrefix.kilo), true));
            for (UnitPrefix prefix : UnitPrefix.values()) {
                switch (prefix) {
                    default:
                        for (int i = 1; i < 1024; i *= 4) {
                            items.add(with(new EnergySize(i, prefix), false));
                            items.add(with(new EnergySize(i, prefix), true));
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

    public ItemStack with(EnergySize size, boolean full) {
        ItemStack stack = new ItemStack(this);
        OCEnergyStorage storage = new OCEnergyStorage(size);
        if (full)
            storage.energy.assign(storage.limit);
        stack.setTagCompound(storage.toNBT());
        return stack;
    }

    public OCEnergyStorage storageOf(ItemStack stack) {
        return OCEnergyStorage.fromNBT(stack.getTagCompound());
    }

    public void setStorage(ItemStack stack, OCEnergyStorage storage) {
        stack.setTagCompound(storage.toNBT());
    }

    public EnergySize sizeOf(ItemStack stack) {
        return storageOf(stack).size;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (playerIn.isSneaking()) {
            ItemHandlerHelper.giveItemToPlayer(playerIn, OCItems.chip.with(sizeOf(playerIn.getHeldItem(handIn))));
            return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(OCItems.disc_housing));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getTagCompound() != null) {
            OCEnergyStorage storage = storageOf(stack);
            UFMBigInt energy = storage.energy;
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(String.format("%s / %sFE", energy.toStringGroup(), storage.limit.toStringGroup()));
            } else {
                EnergySize size = storage.size;
                BigDecimal about = BigMath.divide(energy.toBigInt(), size.prefix.maxOf(1), 2, RoundingMode.HALF_EVEN);
                tooltip.add(String.format("%s / %s", about, size));
                tooltip.add("§fPress §aLSHIFT§f to show the exact value§8");
            }
        }
    }

    // test only, remove after the driver is done
    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ICapabilityProvider() {
            @Override
            public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                return capability == CapabilityEnergy.ENERGY;
            }

            @Nullable
            @Override
            public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                return SausageUtils.rawtype(new IEnergyStorage() {

                    @Override
                    public int receiveEnergy(int maxReceive, boolean simulate) {
                        OCEnergyStorage storage = storageOf(stack);
                        int ret = (int) storage.fill(UFMBigInt.of(1, maxReceive)).asLong();
                        if (!simulate) {
                            setStorage(stack, storage);
                        }
                        return maxReceive - ret;
                    }

                    @Override
                    public int extractEnergy(int maxExtract, boolean simulate) {
                        OCEnergyStorage storage = storageOf(stack);
                        int ret = (int) storage.drain(UFMBigInt.of(1, maxExtract)).asLong();
                        if (!simulate) {
                            setStorage(stack, storage);
                        }
                        return ret;
                    }

                    @Override
                    public int getEnergyStored() {
                        return storageOf(stack).energy.toBigInt().min(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
                    }

                    @Override
                    public int getMaxEnergyStored() {
                        return sizeOf(stack).limit().min(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
                    }

                    @Override
                    public boolean canExtract() {
                        return true;
                    }

                    @Override
                    public boolean canReceive() {
                        return true;
                    }
                });
            }
        };
    }
}
