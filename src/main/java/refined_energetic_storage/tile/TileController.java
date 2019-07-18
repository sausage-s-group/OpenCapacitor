package refined_energetic_storage.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import sausage_core.api.core.tile.TileBase;
import sausage_core.api.util.nbt.NBTs;

import java.util.HashSet;
import java.util.Set;

public class TileController extends TileBase implements ITickable {

    private Set<BlockPos> devicePos = new HashSet<>();
    private boolean needUpdate = false;

    public TileController() {
        this.network = true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList tagList = nbt.getTagList("devices", 10);
        tagList.forEach(tag -> devicePos.add(NBTUtil.getPosFromTag((NBTTagCompound) tag)));
        super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("devices", devicePos.stream().map(NBTUtil::createPosTag).collect(NBTs.toNBTList()));
        return super.writeToNBT(nbt);
    }

    public void addNewDevice(BlockPos pos) {
        if (world.getTileEntity(pos) instanceof IRESDevice) {
            devicePos.add(pos);
            needUpdate = true;
        }
    }

    public void removeDevice(BlockPos pos) {
        devicePos.remove(pos);
        needUpdate = true;
    }

    public void removeAllDevice(BlockPos pos) {
        devicePos.clear();
        needUpdate = true;
    }

    public Set<BlockPos> getDevicePos() {
        return devicePos;
    }

    @Override
    public void update() {
        if (needUpdate) {
            this.notifyClient();
            needUpdate = false;
        }
    }
}
