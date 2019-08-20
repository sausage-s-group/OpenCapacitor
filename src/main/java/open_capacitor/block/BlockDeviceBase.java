package open_capacitor.block;

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
                nonnull(((TileDeviceBase) worldIn.getTileEntity(pos))).notifyNeighbor(this);
                nonnull(((TileController) worldIn.getTileEntity(controllerPos))).notifyNeighbor(OCBlocks.controller);
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (!worldIn.isRemote) {
            TileDeviceBase deviceBase = nonnull(((TileDeviceBase) worldIn.getTileEntity(pos)));
            if (deviceBase.controller != INVALID) {
                nonnull(((TileDeviceBase) worldIn.getTileEntity(pos))).notifyNeighbor(this);
                nonnull(((TileController) worldIn.getTileEntity(deviceBase.controller))).notifyNeighbor(OCBlocks.controller);
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
