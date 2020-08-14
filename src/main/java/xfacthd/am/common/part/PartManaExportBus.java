package xfacthd.am.common.part;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.config.Upgrades;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartModel;
import appeng.api.storage.IMEMonitor;
import appeng.api.util.AECableType;
import appeng.me.GridAccessException;
import appeng.me.helpers.MachineSource;
import appeng.parts.automation.PartExportBus;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import vazkii.botania.api.mana.IManaPool;
import xfacthd.am.api.IAEManaStack;
import xfacthd.am.api.IManaStorageChannel;
import xfacthd.am.common.grid.AEManaStack;

import javax.annotation.Nonnull;

public class PartManaExportBus extends AMPartUpgradeable implements IGridTickable
{
    private IActionSource source;
    private boolean manaPoolPresent = false;
    private IManaPool manaPoolTile = null;
    private boolean firstTick = true;

    public PartManaExportBus(ItemStack stack)
    {
        super(stack, 4);

        source = new MachineSource(this);
    }

    @Nonnull
    @Override
    public TickingRequest getTickingRequest(@Nonnull IGridNode node)
    {
        return new TickingRequest(2, 20, false, false);
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

        if (manaPoolPresent)
        {
            boolean worked = false;

            try
            {
                final IMEMonitor<IAEManaStack> inv = this.getProxy().getStorage().getInventory(AEApi.instance().storage().getStorageChannel(IManaStorageChannel.class));
                IAEManaStack stack = inv.extractItems(new AEManaStack(calculateMaxTransfer()), Actionable.SIMULATE, source);
                if (stack != null)
                {
                    int oldMana = manaPoolTile.getCurrentMana();
                    manaPoolTile.recieveMana((int) stack.getStackSize());
                    int toRemove = manaPoolTile.getCurrentMana() - oldMana;
                    inv.extractItems(new AEManaStack(toRemove), Actionable.MODULATE, source);
                    worked = toRemove != 0;
                }
            }
            catch (GridAccessException e)
            {
                //:3
            }

            return worked ? TickRateModulation.FASTER : TickRateModulation.SLOWER;
        }
        else
        {
            return TickRateModulation.SLOWER;
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
                manaPoolPresent = true;
                manaPoolTile = (IManaPool)te;
            }
            else
            {
                manaPoolPresent = false;
                manaPoolTile = null;
            }
        }
    }

    @Override
    public void getBoxes(IPartCollisionHelper pch)
    {
        pch.addBox( 4, 4, 12, 12, 12, 14 );
        pch.addBox( 5, 5, 14, 11, 11, 15 );
        pch.addBox( 6, 6, 15, 10, 10, 16 );
        pch.addBox( 6, 6, 11, 10, 10, 12 );
    }

    private int calculateMaxTransfer()
    {
        switch(this.getInstalledUpgrades(Upgrades.SPEED))
        {
            default:
            case 0:
                return 100;
            case 1:
                return 200;
            case 2:
                return 400;
            case 3:
                return 800;
            case 4:
                return 1600;
        }
    }

    @Override
    public float getCableConnectionLength(AECableType type)
    {
        return 5;
    }

    @Nonnull
    @Override
    public IPartModel getStaticModels()
    {
        if(this.isActive() && this.isPowered())
        {
            return PartExportBus.MODELS_HAS_CHANNEL;
        }
        else if(this.isPowered())
        {
            return PartExportBus.MODELS_ON;
        }
        else
        {
            return PartExportBus.MODELS_OFF;
        }
    }
}