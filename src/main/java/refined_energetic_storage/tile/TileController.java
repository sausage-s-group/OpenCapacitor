package refined_energetic_storage.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import sausage_core.api.core.tile.TileBase;

import java.util.HashSet;
import java.util.Set;

public class TileController extends TileBase {

    private Set<BlockPos> devicePos = new HashSet<>();

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList tagList = nbt.getTagList("devices", 10);
        tagList.forEach(tag -> devicePos.add(NBTUtil.getPosFromTag((NBTTagCompound) tag)));
        super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList tagList = new NBTTagList();
        devicePos.forEach(blockPos -> tagList.appendTag(NBTUtil.createPosTag(blockPos)));
        nbt.setTag("devices", tagList);
        return super.writeToNBT(nbt);
    }

    public void addNewDevice(BlockPos pos) {
        if (world.getTileEntity(pos) instanceof IRESDevice) {
            devicePos.add(pos);
            this.notifyClient();
        }
    }
}
