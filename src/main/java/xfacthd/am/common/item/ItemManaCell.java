package xfacthd.am.common.item;

import appeng.api.AEApi;
import appeng.api.config.FuzzyMode;
import appeng.api.implementations.items.IStorageCell;
import appeng.api.storage.ICellInventory;
import appeng.api.storage.ICellInventoryHandler;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IItemList;
import appeng.core.localization.GuiText;
import appeng.items.contents.CellConfig;
import appeng.items.contents.CellUpgrades;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.input.Keyboard;
import xfacthd.am.api.IAEManaStack;
import xfacthd.am.api.IManaStorageChannel;
import xfacthd.am.common.Content;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemManaCell extends ItemBase implements IStorageCell<IAEManaStack> //TODO: check
{
    public ItemManaCell()
    {
        super("item_mana_cell", 1, CellSize.getNames());
    }

    @Override
    public int getBytes(@Nonnull ItemStack stack)
    {
        return CellSize.fromStack(stack).sizeBytes;
    }

    @Override
    public int getBytesPerType(@Nonnull ItemStack stack)
    {
        return 1;
    }

    @Override
    public int getTotalTypes(@Nonnull ItemStack stack)
    {
        return 1;
    }

    @Override
    public double getIdleDrain()
    {
        return 0.5;
    }

    @Override
    public boolean storableInStorageCell()
    {
        return false;
    }

    @Override
    public boolean isBlackListed(@Nonnull ItemStack stack, @Nonnull IAEManaStack manaStack)
    {
        return false;
    }

    @Override
    public boolean isStorageCell(@Nonnull ItemStack stack)
    {
        return true;
    }

    @Nonnull
    @Override
    public IStorageChannel<IAEManaStack> getChannel()
    {
        return AEApi.instance().storage().getStorageChannel(IManaStorageChannel.class);
    }

    @Override
    public boolean isEditable(ItemStack stack)
    {
        return false;
    }

    @Override
    public IItemHandler getUpgradesInventory(ItemStack stack)
    {
        return new CellUpgrades(stack, 0);
    }

    @Override
    public IItemHandler getConfigInventory(ItemStack stack)
    {
        return new CellConfig(stack);
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack stack)
    {
        return FuzzyMode.IGNORE_ALL;
    }

    @Override
    public void setFuzzyMode(ItemStack stack, FuzzyMode mode)
    {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(final ItemStack stack, final World world, final List<String> lines, final ITooltipFlag advancedTooltips) {

        ICellInventoryHandler<IAEManaStack> inventoryHandler = AEApi.instance().registries().cell().getCellInventory(stack, null, this.getChannel());
        if (inventoryHandler != null)
        {
            final ICellInventory<?> cellInventory = inventoryHandler.getCellInv();

            if (cellInventory != null)
            {
                lines.add(cellInventory.getUsedBytes() + " " + GuiText.Of.getLocal() + ' ' + cellInventory.getTotalBytes() + ' ' + GuiText.BytesUsed.getLocal());
            }
        }
    }

    public enum CellSize
    {
        SIZE_1K   (   1, 0.5),
        SIZE_4K   (   4, 1.0),
        SIZE_16K  (  16, 1.5),
        SIZE_64K  (  64, 2.0),
        SIZE_256K ( 256, 2.5),
        SIZE_1024K(1024, 3.0),
        SIZE_4096K(4096, 3.5);

        int sizeBytes;
        double idleDraw;

        CellSize(int sizeKB, double idleDraw)
        {
            this.sizeBytes = sizeKB * 1024;
            this.idleDraw = idleDraw;
        }

        public static List<String> getNames()
        {
            List<String> names = new ArrayList<>();
            for (CellSize size : values())
            {
                names.add(size.toString().toLowerCase(Locale.ENGLISH));
            }
            return names;
        }

        public static CellSize fromStack(ItemStack stack)
        {
            if (stack.getMetadata() < values().length)
            {
                return values()[stack.getMetadata()];
            }
            return null;
        }

        public ItemStack getStack(int size)
        {
            return new ItemStack(Content.itemManaCell, size, ordinal());
        }
    }
}