package open_capacitor.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import open_capacitor.tile.TileController;
import open_capacitor.tile.TileDeviceBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static sausage_core.api.util.common.SausageUtils.nonnull;

public class BlockController extends BlockContainer {
    public BlockController() {
        super(Material.IRON);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileController();
    }

    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        nonnull(((TileController) worldIn.getTileEntity(pos))).notifyNeighbor(this);
    }

    @Override
    public void breakBlock(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        nonnull(((TileController) worldIn.getTileEntity(pos))).notifyNeighbor(this);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        boolean result = true;
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos neighbor = pos.offset(facing);
            TileEntity neighborTile = worldIn.getTileEntity(neighbor);
            if (neighborTile instanceof TileController) {
                result = false;
                break;
            } else if (neighborTile instanceof TileDeviceBase) {
                TileDeviceBase deviceBase = ((TileDeviceBase) neighborTile);
                if (!pos.equals(deviceBase.controller)) {
                    result = false;
                    break;
                }
            }
        }
        return result && super.canPlaceBlockAt(worldIn, pos);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.sendMessage(new TextComponentString("S: " + nonnull((TileController) worldIn.getTileEntity(pos)).devices.toString()));
        } else {
            playerIn.sendMessage(new TextComponentString("C: " + nonnull((TileController) worldIn.getTileEntity(pos)).devices.toString()));
        }
        return true;
    }
}
