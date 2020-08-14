package xfacthd.am.common.grid;

import appeng.api.config.FuzzyMode;
import appeng.api.storage.data.IItemList;
import appeng.fluids.util.MeaningfulFluidIterator;
import xfacthd.am.api.IAEManaStack;

import javax.annotation.Nonnull;
import java.util.*;

public class ManaList implements IItemList<IAEManaStack>
{
    private final ArrayList<IAEManaStack> manaStacks = new ArrayList<>();

    @Override
    public void add(IAEManaStack stack)
    {
        if (stack == null) { return; }
        if (manaStacks.isEmpty())
        {
            manaStacks.add(stack.copy());
        }
        else
        {
            manaStacks.get(0).add(stack);
        }
    }

    @Override
    public void addStorage(IAEManaStack stack)
    {
        if (stack == null) { return; }
        if (manaStacks.isEmpty())
        {
            manaStacks.add(stack.copy());
        }
        else
        {
            manaStacks.get(0).add(stack);
        }
    }

    @Override
    public void addCrafting(IAEManaStack stack)
    {
        if (stack == null) { return; }

        /*IAEManaStack record = getManaRecord(stack);
        if (record != null)
        {
            record.setCraftable(true);
            return;
        }

        IAEManaStack newStack = stack.copy();
        newStack.setStackSize(0);
        newStack.setCraftable(true);
        putManaRecord(newStack);*/
    }

    @Override
    public void addRequestable(IAEManaStack stack)
    {
        if (stack == null) { return; }

        /*IAEManaStack record = getManaRecord(stack);
        if (record != null)
        {
            record.incCountRequestable(stack.getCountRequestable());
            return;
        }

        IAEManaStack newStack = stack.copy();
        newStack.setStackSize(0);
        newStack.setCraftable(true);
        newStack.setCountRequestable(stack.getCountRequestable());
        putManaRecord(newStack);*/
    }

    @Override
    public IAEManaStack getFirstItem()
    {
        return manaStacks.isEmpty() ? null : manaStacks.get(0);
    }

    @Override
    public int size()
    {
        return manaStacks.size();
    }

    @Override
    @Nonnull
    public Iterator<IAEManaStack> iterator()
    {
        return new MeaningfulFluidIterator<>(manaStacks.iterator());
    }

    @Override
    public void resetStatus()
    {
        for (IAEManaStack stack : this)
        {
            stack.reset();
        }
    }

    @Override
    public IAEManaStack findPrecise(IAEManaStack stack)
    {
        return (stack == null || manaStacks.isEmpty()) ? null : manaStacks.get(0);
    }

    @Override
    public Collection<IAEManaStack> findFuzzy(IAEManaStack stack, FuzzyMode mode)
    {
        return stack == null ? Collections.emptyList() : Collections.singletonList(findPrecise(stack));
    }

    @Override
    public boolean isEmpty()
    {
        return !iterator().hasNext();
    }
}