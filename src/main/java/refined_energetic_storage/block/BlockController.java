package refined_energetic_storage.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
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
import refined_energetic_storage.tile.IRESDevice;
import refined_energetic_storage.tile.TileController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class BlockController extends BlockContainer {

    public BlockController() {
        super(Material.IRON, MapColor.GRAY);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileController();
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        boolean result = true;
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(facing);
            TileEntity tile = worldIn.getTileEntity(neighbor);
            if (tile != null) {
                if (tile instanceof TileController) {
                    result = false;
                    break;
                }
                if (tile instanceof IRESDevice) {
                    IRESDevice device = (IRESDevice) tile;
                    result = device.getControllerPos() == null;
                }
            }
        }
        return result && super.canPlaceBlockAt(worldIn, pos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        BlockDeviceBase.updatedBlocks.clear();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.sendMessage(new TextComponentString("Server Devices: " + ((TileController) Objects.requireNonNull(worldIn.getTileEntity(pos))).getDevicePos().toString()));
        }
        if (worldIn.isRemote) {
            playerIn.sendMessage(new TextComponentString("Client Devices: " + ((TileController) Objects.requireNonNull(worldIn.getTileEntity(pos))).getDevicePos().toString()));
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        BlockDeviceBase.updatedBlocks.clear();
    }

}
