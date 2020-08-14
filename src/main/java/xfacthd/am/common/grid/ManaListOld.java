package xfacthd.am.common.grid;

import appeng.api.config.FuzzyMode;
import appeng.api.storage.data.IItemList;
import appeng.fluids.util.MeaningfulFluidIterator;
import xfacthd.am.api.IAEManaStack;

import javax.annotation.Nonnull;
import java.util.*;

public class ManaListOld implements IItemList<IAEManaStack>
{
    private final Map<IAEManaStack, IAEManaStack> records = new HashMap<>();

    @Override
    public void add(IAEManaStack stack)
    {
        if (stack == null) { return; }

        IAEManaStack record = getManaRecord(stack);
        if (record != null)
        {
            record.add(stack);
            return;
        }

        putManaRecord(stack.copy());
    }

    @Override
    public void addStorage(IAEManaStack stack)
    {
        if (stack == null) { return; }

        IAEManaStack record = getManaRecord(stack);
        if (record != null)
        {
            record.incStackSize(stack.getStackSize());
            return;
        }

        putManaRecord(stack.copy());
    }

    @Override
    public void addCrafting(IAEManaStack stack)
    {
        if (stack == null) { return; }

        IAEManaStack record = getManaRecord(stack);
        if (record != null)
        {
            record.setCraftable(true);
            return;
        }

        IAEManaStack newStack = stack.copy();
        newStack.setStackSize(0);
        newStack.setCraftable(true);
        putManaRecord(newStack);
    }

    @Override
    public void addRequestable(IAEManaStack stack)
    {
        if (stack == null) { return; }

        IAEManaStack record = getManaRecord(stack);
        if (record != null)
        {
            record.incCountRequestable(stack.getCountRequestable());
            return;
        }

        IAEManaStack newStack = stack.copy();
        newStack.setStackSize(0);
        newStack.setCraftable(true);
        newStack.setCountRequestable(stack.getCountRequestable());
        putManaRecord(newStack);
    }

    @Override
    public IAEManaStack getFirstItem()
    {
        //TODO: check why this stupid construct is necessary
        for (IAEManaStack stack : this)
        {
            return stack;
        }

        return null;
    }

    @Override
    public int size()
    {
        return records.values().size();
    }

    @Override
    @Nonnull
    public Iterator<IAEManaStack> iterator()
    {
        //TODO: check if this works
        return new MeaningfulFluidIterator<>(records.values().iterator());
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
        return stack == null ? null : getManaRecord(stack);
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

    private IAEManaStack getManaRecord(IAEManaStack stack)
    {
        return records.get(stack);
    }

    private IAEManaStack putManaRecord(IAEManaStack stack)
    {
        return records.put(stack, stack);
    }
}