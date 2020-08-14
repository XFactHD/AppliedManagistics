package xfacthd.am.common.part;

import appeng.api.AEApi;
import appeng.api.networking.IGridNode;
import appeng.api.networking.events.MENetworkCellArrayUpdate;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartModel;
import appeng.api.parts.PartItemStack;
import appeng.api.storage.ICellContainer;
import appeng.api.storage.ICellInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.IStorageChannel;
import appeng.core.sync.GuiBridge;
import appeng.helpers.IPriorityHost;
import appeng.me.GridAccessException;
import appeng.parts.misc.PartStorageBus;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import vazkii.botania.api.mana.IManaPool;
import xfacthd.am.api.IAEManaStack;
import xfacthd.am.api.IManaStorageChannel;
import xfacthd.am.common.utils.MEManaStorageBusHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PartManaStorageBus extends AMPart implements ICellContainer, IGridTickable, IPriorityHost
{
    private boolean manaPoolPresent = false;
    private IManaPool manaPoolTile = null;
    private int lastManaPoolState = -1;
    private int priority = 0;
    private boolean wasActive = false;
    private boolean firstTick = true;

    private IMEInventoryHandler<IAEManaStack> handler = new MEManaStorageBusHandler(null, this);

    public PartManaStorageBus(ItemStack stack)
    {
        super(stack);
    }

    @Nonnull
    @Override
    public TickingRequest getTickingRequest(@Nonnull IGridNode iGridNode)
    {
        return new TickingRequest(20, 20, false, false);
    }

    @Nonnull
    @Override
    public TickRateModulation tickingRequest(@Nonnull IGridNode node, int ticksSinceLastCall)
    {
        if (firstTick)
        {
            firstTick = false;

            onNeighborChanged(hostTile.getWorld(), hostTile.getPos(), hostTile.getPos().offset(cableSide.getFacing()));
        }

        //TODO: check if this is necessary
        if (!manaPoolPresent && lastManaPoolState != -1)
        {
            lastManaPoolState = -1;
        }
        else if (manaPoolPresent && lastManaPoolState != manaPoolTile.getCurrentMana())
        {
            lastManaPoolState = manaPoolTile.getCurrentMana();
            postCellEvent();
        }
        return TickRateModulation.SAME;
    }

    @Override
    public List<IMEInventoryHandler> getCellArray(IStorageChannel<?> channel)
    {
        if (channel != AEApi.instance().storage().getStorageChannel(IManaStorageChannel.class))
        {
            return new LinkedList<>();
        }
        return Collections.singletonList(handler);
    }

    @Override
    public void saveChanges(@Nullable ICellInventory<?> inventory)
    {

    }

    public void postCellEvent()
    {
        final boolean currentActive = this.getProxy().isActive();
        if(this.wasActive != currentActive)
        {
            this.wasActive = currentActive;
            try
            {
                this.getProxy().getGrid().postEvent(new MENetworkCellArrayUpdate());
                this.host.markForUpdate();
            }
            catch(final GridAccessException e)
            {
                // :P
            }
        }
    }

    @Override
    public void onNeighborChanged(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        if (pos == null || neighbor == null) { return; }

        if (pos.offset(cableSide.getFacing()).equals(neighbor))
        {
            TileEntity te = world.getTileEntity(neighbor);
            if (te instanceof IManaPool)
            {
                handler = new MEManaStorageBusHandler((IManaPool)te, this);
                manaPoolPresent = true;
                manaPoolTile = (IManaPool)te;
            }
            else
            {
                handler = new MEManaStorageBusHandler(null, this);
                manaPoolPresent = false;
                manaPoolTile = null;
            }

            postCellEvent();
        }
    }

    @MENetworkEventSubscribe
    public void updateChannels(final MENetworkChannelsChanged changedChannels)
    {
        final boolean currentActive = this.getGridNode().isActive();
        if (this.wasActive != currentActive)
        {
            this.wasActive = currentActive;
            this.host.markForUpdate();

            postCellEvent();
        }
    }

    @Override
    public int getPriority()
    {
        return priority;
    }

    @Override
    public void setPriority(int p)
    {
        priority = p;
    }

    @Override
    public ItemStack getItemStackRepresentation()
    {
        return getItemStack(PartItemStack.BREAK);
    }

    @Override
    public GuiBridge getGuiBridge()
    {
        return null;
    }

    @Override
    public void blinkCell(int cell) { }

    @Override
    public void getBoxes(IPartCollisionHelper pch)
    {
        pch.addBox(3, 3, 15, 13, 13, 16);
        pch.addBox(2, 2, 14, 14, 14, 15);
        pch.addBox(5, 5, 12, 11, 11, 14);
    }

    @Nonnull
    @Override
    public IPartModel getStaticModels() //TODO: use own textures
    {
        if(this.isActive() && this.isPowered())
        {
            return PartStorageBus.MODELS_HAS_CHANNEL;
        }
        else if( this.isPowered() )
        {
            return PartStorageBus.MODELS_ON;
        }
        else
        {
            return PartStorageBus.MODELS_OFF;
        }
    }
}