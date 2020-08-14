package xfacthd.am.common.part;

import appeng.api.AEApi;
import appeng.api.config.FuzzyMode;
import appeng.api.implementations.parts.IPartStorageMonitor;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStackWatcher;
import appeng.api.networking.storage.IStackWatcherHost;
import appeng.api.parts.IPartModel;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import appeng.client.render.TesrRenderHelper;
import appeng.me.GridAccessException;
import appeng.parts.reporting.AbstractPartDisplay;
import appeng.parts.reporting.PartPanel;
import appeng.parts.reporting.PartStorageMonitor;
import appeng.util.IWideReadableNumberConverter;
import appeng.util.ReadableNumberConverter;
import appeng.util.item.AEItemStack;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.common.item.ModItems;
import xfacthd.am.api.IAEManaStack;
import xfacthd.am.api.IManaStorageChannel;
import xfacthd.am.common.grid.AEManaStack;

import javax.annotation.Nonnull;

//TODO: check if this even works at all
public class PartManaStorageMonitor extends AbstractPartDisplay implements IPartStorageMonitor, IStackWatcherHost
{
    private static final IWideReadableNumberConverter NUMBER_CONVERTER = ReadableNumberConverter.INSTANCE;
    private String lastHumanReadableText;
    private IStackWatcher myWatcher;
    private final AEManaStack watchedStack = new AEManaStack(0);
    private final IAEItemStack displayStack = AEItemStack.fromItemStack(new ItemStack(ModItems.manaTablet)).setStackSize(0);

    public PartManaStorageMonitor(ItemStack stack)
    {
        super(stack);
    }

    private void configureWatchers()
    {
        if( this.myWatcher != null )
        {
            this.myWatcher.reset();
        }

        try
        {

            if(this.myWatcher != null)
            {
                this.myWatcher.add(watchedStack);
            }

            this.updateReportingValue(this.getProxy().getStorage().getInventory(AEApi.instance().storage().getStorageChannel(IManaStorageChannel.class)));

        }
        catch(final GridAccessException e)
        {
            // >.>
        }
    }

    private void updateReportingValue(final IMEMonitor<IAEManaStack> inventory)
    {
        final IAEManaStack result = inventory.getStorageList().findPrecise(watchedStack);
        if(result == null)
        {
            this.watchedStack.setStackSize(0);
        }
        else
        {
            this.watchedStack.setStackSize(result.getStackSize());
        }
    }

    @Override
    @SideOnly( Side.CLIENT )
    public void renderDynamic(double x, double y, double z, float partialTicks, int destroyStage)
    {
        if((this.getClientFlags() & (PartPanel.POWERED_FLAG | PartPanel.CHANNEL_FLAG)) != (PartPanel.POWERED_FLAG | PartPanel.CHANNEL_FLAG))
        {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

        EnumFacing facing = this.getSide().getFacing();

        TesrRenderHelper.moveToFace(facing);
        TesrRenderHelper.rotateToFace(facing, this.getSpin());
        displayStack.setStackSize(watchedStack.getStackSize());
        TesrRenderHelper.renderItem2dWithAmount(displayStack, 0.8f, 0.17f); //TODO: replace with rendering of the "mana fluid"

        GlStateManager.popMatrix();
    }

    @Override
    public void updateWatcher(final IStackWatcher newWatcher)
    {
        this.myWatcher = newWatcher;
        this.configureWatchers();
    }

    @Override
    public void onStackChange(final IItemList list, final IAEStack fullStack, final IAEStack diffStack, final IActionSource src, final IStorageChannel chan)
    {
        System.out.println(fullStack == null);
        if(fullStack == null)
        {
            this.watchedStack.setStackSize(0);
        }
        else
        {
            this.watchedStack.setStackSize(fullStack.getStackSize());
        }

        final long stackSize = this.watchedStack.getStackSize();
        final String humanReadableText = NUMBER_CONVERTER.toWideReadableForm(stackSize);

        if(!humanReadableText.equals(this.lastHumanReadableText))
        {
            this.lastHumanReadableText = humanReadableText;
            this.getHost().markForUpdate();
        }
    }

    @Override
    public boolean requireDynamicRender()
    {
        return true;
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
    public boolean showNetworkInfo(RayTraceResult rayTraceResult)
    {
        return false;
    }

    @Nonnull
    @Override
    public IPartModel getStaticModels()
    {
        if(this.isActive() && this.isPowered())
        {
            return PartStorageMonitor.MODELS_HAS_CHANNEL;
        }
        else if( this.isPowered() )
        {
            return PartStorageMonitor.MODELS_ON;
        }
        else
        {
            return PartStorageMonitor.MODELS_OFF;
        }
    }
}