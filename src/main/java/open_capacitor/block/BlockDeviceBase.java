package open_capacitor.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import open_capacitor.init.OCBlocks;
import open_capacitor.tile.TileController;
import open_capacitor.tile.TileDeviceBase;

import javax.annotation.Nonnull;

import static open_capacitor.tile.TileDeviceBase.INVALID;
import static sausage_core.api.util.common.SausageUtils.nonnull;

public abstract class BlockDeviceBase extends BlockContainer {
    protected BlockDeviceBase() {
        super(Material.IRON);
    }

    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
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
                    worldIn.notifyNeighborsOfStateChange(pos, blockIn, false);
                }
            } else if (blockIn instanceof BlockDeviceBase) {
                deviceBase.getController().ifPresent(tileController -> tileController.devices.remove(pos));
                if (deviceBase.controller != INVALID) {
                    deviceBase.controller = INVALID;
                    worldIn.notifyNeighborsOfStateChange(pos, this, false);
                }
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!worldIn.isRemote) {
            BlockPos controllerPos = INVALID;
            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos neighbor = pos.offset(facing);
                TileEntity neighborTile = worldIn.getTileEntity(neighbor);
                if (neighborTile instanceof TileController) {
                    controllerPos = neighbor;
                    break;
                } else if (neighborTile instanceof TileDeviceBase) {
                    controllerPos = ((TileDeviceBase) neighborTile).controller;
                    break;
                }
            }
            if (controllerPos != INVALID) {
                worldIn.notifyNeighborsOfStateChange(pos, this, false);
                worldIn.notifyNeighborsOfStateChange(controllerPos, OCBlocks.controller, false);
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (!worldIn.isRemote) {
            TileDeviceBase deviceBase = nonnull(((TileDeviceBase) worldIn.getTileEntity(pos)));
            if (deviceBase.controller != INVALID) {
                worldIn.notifyNeighborsOfStateChange(pos, this, false);
                worldIn.notifyNeighborsOfStateChange(deviceBase.controller, OCBlocks.controller, false);
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        boolean result = true;
        BlockPos firstPos = INVALID;
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos neighbor = pos.offset(facing);
            TileEntity neighborTile = worldIn.getTileEntity(neighbor);
            if (neighborTile instanceof TileController) {
                if (firstPos == INVALID) {
                    firstPos = neighbor;
                } else if (!firstPos.equals(neighbor)) {
                    result = false;
                    break;
                }
            } else if (neighborTile instanceof TileDeviceBase) {
                TileDeviceBase deviceBase = ((TileDeviceBase) neighborTile);
                if (deviceBase.controller != INVALID) {
                    if (firstPos == INVALID) {
                        firstPos = deviceBase.controller;
                    } else if (!firstPos.equals(deviceBase.controller)) {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result && super.canPlaceBlockAt(worldIn, pos);
    }
}
