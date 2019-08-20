package open_capacitor.tile;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import open_capacitor.block.BlockController;
import open_capacitor.block.BlockDeviceBase;
import open_capacitor.init.OCBlocks;
import sausage_core.api.core.tile.TileBase;

import java.util.Optional;

import static sausage_core.api.util.common.SausageUtils.nonnull;

public abstract class TileDeviceBase extends TileBase implements ITickable {
    public static final BlockPos INVALID = BlockPos.ORIGIN.down();
    public BlockPos controller = INVALID;

    public TileDeviceBase() {
        network = true;
    }

    public Optional<TileController> getController() {
        if(!hasController()) return Optional.empty();
        return Optional.ofNullable(world.getTileEntity(controller)).map(TileController.class::cast);
    }

    public boolean hasController() {
        return controller != INVALID;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        controller = NBTUtil.getPosFromTag(nbt.getCompoundTag("controller"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("controller", NBTUtil.createPosTag(controller));
        return super.writeToNBT(nbt);
    }

    @Override
    public void update() {
        this.notifyClient();
    }

    private void onNeighborUpdate(World worldIn, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            TileDeviceBase deviceBase = nonnull(((TileDeviceBase) worldIn.getTileEntity(pos)));
            if (blockIn instanceof BlockController) {
                boolean requireUpdate = false;
                TileEntity fromTile = worldIn.getTileEntity(fromPos);
                if (fromTile == null && deviceBase.controller != INVALID) {
                    deviceBase.controller = INVALID;
                    requireUpdate = true;
                } else if (fromTile instanceof TileController) {
                    if (!fromPos.equals(deviceBase.controller)) {
                        deviceBase.controller = fromPos;
                        deviceBase.getController().ifPresent(tileController -> tileController.devices.add(pos));
                        requireUpdate = true;
                    }
                } else if (fromTile instanceof TileDeviceBase) {
                    BlockPos fromController = ((TileDeviceBase) fromTile).controller;
                    if (!deviceBase.controller.equals(fromController)) {
                        deviceBase.controller = fromController;
                        deviceBase.getController().ifPresent(tileController -> tileController.devices.add(pos));
                        requireUpdate = true;
                    }
                }
                if (requireUpdate) {
                    notifyNeighbor(OCBlocks.controller);
                }
            } else if (blockIn instanceof BlockDeviceBase) {
                deviceBase.getController().ifPresent(tileController -> tileController.devices.remove(pos));
                if (deviceBase.controller != INVALID) {
                    deviceBase.controller = INVALID;
                    notifyNeighbor(worldIn.getBlockState(pos).getBlock());
                }
            }
        }
    }

    public void notifyNeighbor(Block updater) {
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(facing);
            TileEntity neighborTile = world.getTileEntity(neighbor);
            if (neighborTile instanceof TileDeviceBase) {
                ((TileDeviceBase) neighborTile).onNeighborUpdate(world, updater, pos);
            }
        }
    }
}
