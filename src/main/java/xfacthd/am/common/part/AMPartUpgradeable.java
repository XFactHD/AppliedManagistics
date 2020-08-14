package xfacthd.am.common.part;

import appeng.api.config.Upgrades;
import appeng.api.implementations.IUpgradeableHost;
import appeng.api.util.IConfigManager;
import appeng.parts.automation.StackUpgradeInventory;
import appeng.parts.automation.UpgradeInventory;
import appeng.util.ConfigManager;
import appeng.util.IConfigManagerHost;
import appeng.util.inv.IAEAppEngInventory;
import appeng.util.inv.InvOperation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public abstract class AMPartUpgradeable extends AMPart implements IAEAppEngInventory, IConfigManagerHost, IUpgradeableHost
{
    private IConfigManager manager;
    private UpgradeInventory upgrades;

    public AMPartUpgradeable(ItemStack stack, int upgradeSlots)
    {
        super(stack);

        this.upgrades = new StackUpgradeInventory(stack, this, upgradeSlots);
        this.manager = new ConfigManager( this );
    }

    @Override
    public void updateSetting(IConfigManager manager, final Enum settingName, final Enum newValue)
    {

    }

    @Override
    public void onChangeInventory(final IItemHandler inv, final int slot, final InvOperation mc, final ItemStack removedStack, final ItemStack newStack)
    {
        if(inv == this.upgrades)
        {
            this.upgradesChanged();
        }
    }

    public void upgradesChanged()
    {

    }

    @Override
    public void saveChanges()
    {
        host.markForSave();
    }

    @Override
    public TileEntity getTile()
    {
        return hostTile;
    }

    @Override
    public IConfigManager getConfigManager()
    {
        return manager;
    }

    @Override
    public void getDrops(List<ItemStack> drops, boolean wrenched )
    {
        for(final ItemStack is : this.upgrades)
        {
            if(!is.isEmpty())
            {
                drops.add(is);
            }
        }
    }

    @Override
    public IItemHandler getInventoryByName(String name)
    {
        if(name.equals("upgrades"))
        {
            return this.upgrades;
        }

        return null;
    }

    @Override
    public int getInstalledUpgrades(Upgrades u)
    {
        return this.upgrades.getInstalledUpgrades(u);
    }

    @Override
    public void readFromNBT(net.minecraft.nbt.NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.manager.readFromNBT(nbt);
        this.upgrades.readFromNBT(nbt, "upgrades");
    }

    @Override
    public void writeToNBT(net.minecraft.nbt.NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.manager.writeToNBT(nbt);
        this.upgrades.writeToNBT(nbt, "upgrades");
    }
}