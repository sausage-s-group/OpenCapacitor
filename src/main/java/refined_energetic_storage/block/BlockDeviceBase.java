package refined_energetic_storage.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import refined_energetic_storage.init.RESBlocks;
import refined_energetic_storage.tile.IRESDevice;
import refined_energetic_storage.tile.TileController;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class BlockDeviceBase extends BlockContainer {

    public BlockDeviceBase(Material material) {
        super(material);
    }

    public BlockDeviceBase(Material material, MapColor mapColor) {
        super(material, mapColor);
    }

    static Set<BlockPos> updatedBlocks = new HashSet<>();

    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        boolean result = true;
        boolean firstFound = false;
        BlockPos controllerPos = null;
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(facing);
            TileEntity tile = worldIn.getTileEntity(neighbor);
            if (tile != null) {
                if (tile instanceof IRESDevice) {
                    IRESDevice device = (IRESDevice) tile;
                    if (device.getControllerPos() != null && firstFound && !neighbor.equals(controllerPos)) {
                        result = false;
                        break;
                    } else if (device.getControllerPos() != null) {
                        firstFound = true;
                        controllerPos = device.getControllerPos();
                    }
                }
                if (tile instanceof TileController) {
                    if (firstFound && !neighbor.equals(controllerPos)) {
                        result = false;
                        break;
                    } else {
                        firstFound = true;
                        controllerPos = neighbor;
                    }
                }
            }
        }
        return result && super.canPlaceBlockAt(worldIn, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        IRESDevice device = Objects.requireNonNull((IRESDevice) worldIn.getTileEntity(pos));
        if (!updatedBlocks.contains(pos)) {
            if (blockIn instanceof BlockController) {
                if (worldIn.getTileEntity(fromPos) == null) {
                    device.setControllerPos(null);
                } else if (worldIn.getTileEntity(fromPos) instanceof TileController) {
                    device.setControllerPos(fromPos);
                    TileController tileController = Objects.requireNonNull((TileController) worldIn.getTileEntity(fromPos));
                    tileController.addNewDevice(pos);
                } else if (worldIn.getTileEntity(fromPos) instanceof IRESDevice) {
                    IRESDevice fromDevice = Objects.requireNonNull((IRESDevice) worldIn.getTileEntity(fromPos));
                    device.setControllerPos(fromDevice.getControllerPos());
                    if (device.getControllerPos() != null) {
                        TileController tileController = Objects.requireNonNull((TileController) worldIn.getTileEntity(device.getControllerPos()));
                        tileController.addNewDevice(pos);
                    }
                }
                updatedBlocks.add(pos);
                worldIn.notifyNeighborsOfStateChange(pos, blockIn, false);
            } else if (blockIn instanceof BlockDeviceBase) {
                if (device.getControllerPos() != null) {
                    TileController tileController = Objects.requireNonNull((TileController) worldIn.getTileEntity(device.getControllerPos()));
                    tileController.removeDevice(pos);
                }
                device.setControllerPos(null);
                updatedBlocks.add(pos);
                worldIn.notifyNeighborsOfStateChange(pos, this, false);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        BlockPos controllerPos = null;
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(facing);
            if (worldIn.getTileEntity(neighbor) instanceof TileController) {
                worldIn.notifyNeighborsOfStateChange(neighbor, RESBlocks.controller, false);
                updatedBlocks.clear();
                return;
            } else if (worldIn.getTileEntity(neighbor) instanceof IRESDevice) {
                IRESDevice device = Objects.requireNonNull((IRESDevice) worldIn.getTileEntity(neighbor));
                if (controllerPos == null) {
                    controllerPos = device.getControllerPos();
                }
            }
        }
        if (controllerPos != null) {
            updatedBlocks.add(pos);
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            updatedBlocks.clear();
            worldIn.notifyNeighborsOfStateChange(controllerPos, RESBlocks.controller, false);
            updatedBlocks.clear();
        }
    }

    @Override
    public void breakBlock(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        BlockPos controllerPos = null;
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(facing);
            if (worldIn.getTileEntity(neighbor) instanceof IRESDevice) {
                IRESDevice device = Objects.requireNonNull((IRESDevice) worldIn.getTileEntity(neighbor));
                if (controllerPos == null) {
                    controllerPos = device.getControllerPos();
                }
            }
        }
        if (controllerPos != null) {
            updatedBlocks.add(pos);
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            updatedBlocks.clear();
        }
        super.breakBlock(worldIn, pos, state);
        if (controllerPos != null) {
            worldIn.notifyNeighborsOfStateChange(controllerPos, RESBlocks.controller, false);
            updatedBlocks.clear();
            TileController tileController = Objects.requireNonNull((TileController) worldIn.getTileEntity(controllerPos));
            tileController.removeDevice(pos);
        }
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
