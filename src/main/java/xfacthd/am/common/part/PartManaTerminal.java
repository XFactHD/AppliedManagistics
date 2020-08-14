package xfacthd.am.common.part;

import appeng.api.implementations.parts.IPartStorageMonitor;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStackWatcher;
import appeng.api.networking.storage.IStackWatcherHost;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartModel;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import appeng.parts.reporting.PartTerminal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import xfacthd.am.api.IAEManaStack;
import xfacthd.am.api.IManaStorageChannel;

import javax.annotation.Nonnull;

public class PartManaTerminal extends AMPart implements IPartStorageMonitor, IStackWatcherHost
{
    public PartManaTerminal(ItemStack stack)
    {
        super(stack);
    }

    @Override
    public void updateWatcher(IStackWatcher iStackWatcher)
    {

    }

    @Override
    public void onStackChange(IItemList<?> stacks, IAEStack<?> fullStack, IAEStack<?> diffStack, IActionSource source, IStorageChannel<?> channel)
    {
        if (channel instanceof IManaStorageChannel)
        {

        }
    }

    @Override
    public IAEManaStack getDisplayed()
    {
        return null;
    }

    @Override
    public boolean isLocked()
    {
        return true;
    }

    @Override
    public boolean showNetworkInfo(RayTraceResult result)
    {
        return false;
    }

    @Override
    public void getBoxes(IPartCollisionHelper pch)
    {
        pch.addBox( 2, 2, 14, 14, 14, 16 );
        pch.addBox( 4, 4, 13, 12, 12, 14 );
    }

    @Nonnull
    @Override
    public IPartModel getStaticModels()
    {
        if (isActive())
        {
            return PartTerminal.MODELS_HAS_CHANNEL;
        }
        else if (isPowered())
        {
            return PartTerminal.MODELS_ON;
        }
        else
        {
            return PartTerminal.MODELS_OFF;
        }
    }
}