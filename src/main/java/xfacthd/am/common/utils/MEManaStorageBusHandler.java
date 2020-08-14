package xfacthd.am.common.utils;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.data.IItemList;
import vazkii.botania.api.mana.IManaPool;
import xfacthd.am.api.IAEManaStack;
import xfacthd.am.api.IMEManaHandler;
import xfacthd.am.common.grid.AEManaStack;
import xfacthd.am.common.part.PartManaStorageBus;

public class MEManaStorageBusHandler implements IMEManaHandler
{
    private IManaPool storageTile;
    private PartManaStorageBus owner;

    public MEManaStorageBusHandler(IManaPool tile, PartManaStorageBus bus)
    {
        storageTile = tile;
        owner = bus;
    }

    @Override
    public AccessRestriction getAccess()
    {
        //TODO: implement access settings on storage bus
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean canAccept(IAEManaStack stack)
    {
        return storageTile != null && !storageTile.isFull();
    }

    @Override
    public int getPriority()
    {
        return owner.getPriority();
    }

    @Override
    public IAEManaStack injectItems(IAEManaStack stack, Actionable actionable, IActionSource source)
    {
        if (getAccess().hasPermission(AccessRestriction.WRITE) && storageTile != null)
        {
            if (storageTile.isFull()) { return stack; }
            if (stack == null || !stack.isMeaningful()) { return stack; }

            int oldMana = storageTile.getCurrentMana();
            int manaToStore = (int)stack.getStackSize();
            storageTile.recieveMana(manaToStore);
            int newMana = storageTile.getCurrentMana();
            if (actionable == Actionable.SIMULATE) { storageTile.recieveMana(newMana - oldMana); }

            long toReturn = (manaToStore - (newMana - oldMana)) + (stack.getStackSize() - manaToStore);
            System.out.println("Old mana: " + oldMana + ", Mana to store: " + manaToStore + ", New mana: " + newMana + ", To return: " + toReturn);
            return toReturn == 0 ? null : stack.setStackSize(toReturn);
        }
        return stack;
    }

    @Override
    public IAEManaStack extractItems(IAEManaStack stack, Actionable actionable, IActionSource iActionSource)
    {
        if (getAccess().hasPermission(AccessRestriction.READ) && storageTile != null)
        {
            if (storageTile.getCurrentMana() == 0) { return null; }
            if (stack == null || !stack.isMeaningful()) { return null; }

            int manaToRemove = Math.min((int)stack.getStackSize(), storageTile.getCurrentMana());
            if (manaToRemove != 0)
            {
                storageTile.recieveMana(-manaToRemove);
                return stack.setStackSize(manaToRemove);
            }
        }
        return null;
    }

    @Override
    public IItemList<IAEManaStack> getAvailableItems(IItemList<IAEManaStack> stacks)
    {
        if (getAccess().hasPermission(AccessRestriction.READ) && storageTile != null)
        {
            stacks.add(new AEManaStack(storageTile.getCurrentMana()));
        }
        return stacks;
    }
}