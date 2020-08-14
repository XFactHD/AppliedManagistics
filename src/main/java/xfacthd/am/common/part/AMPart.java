package xfacthd.am.common.part;

import appeng.api.AEApi;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.security.IActionHost;
import appeng.api.parts.BusSupport;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.parts.PartItemStack;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public abstract class AMPart implements IPart, IGridHost, IActionHost, IPowerChannelState, IGridProxyable
{
    protected IPartHost host;
    protected TileEntity hostTile;
    protected AEPartLocation cableSide;
    private boolean isPowered = false;
    private boolean isActive = false;
    private int ownerID;
    private AENetworkProxy proxy;
    private ItemStack partStack;

    public AMPart(ItemStack stack)
    {
        partStack = stack.copy();
        if (stack.hasTagCompound())
        {
            //noinspection ConstantConditions
            readFromNBT(partStack.getTagCompound());
        }

        proxy = new AENetworkProxy(this, "am_part_proxy", partStack, true);
        proxy.setValidSides(EnumSet.noneOf(EnumFacing.class));
        proxy.setFlags(GridFlags.REQUIRE_CHANNEL);
    }

    @Override
    public void securityBreak()
    {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(this.getItemStack(PartItemStack.BREAK));
        this.getDrops(drops, false);
        this.host.removePart(this.cableSide, false);
    }

    @Override
    public IGridNode getGridNode()
    {
        return getProxy().getNode();
    }

    @Nullable
    @Override
    public IGridNode getGridNode(@Nonnull AEPartLocation location)
    {
        return getGridNode();
    }

    @Nonnull
    @Override
    public IGridNode getActionableNode()
    {
        return getProxy().getNode();
    }

    @Nonnull
    @Override
    public AECableType getCableConnectionType(@Nonnull AEPartLocation location)
    {
        return AECableType.GLASS;
    }

    @Override
    public boolean isPowered() {
        try
        {
            if (!hostTile.getWorld().isRemote && (this.proxy != null))
            {

                IEnergyGrid eGrid = this.proxy.getEnergy();

                if (eGrid != null)
                {
                    this.isPowered = eGrid.isNetworkPowered();
                }
                else
                {
                    this.isPowered = false;
                }
            }
        }
        catch (Exception ignored) { }

        return this.isPowered;
    }

    @Override
    public boolean isActive()
    {
        if (!hostTile.getWorld().isRemote)
        {
            if (this.getProxy().getNode() != null)
            {
                this.isActive = this.getProxy().isActive();
            }
            else
            {
                this.isActive = false;
            }
        }

        return this.isActive;
    }

    @Override
    public int isProvidingWeakPower()
    {
        return 0;
    }

    @Override
    public int isProvidingStrongPower()
    {
        return 0;
    }

    @Override
    public boolean canConnectRedstone()
    {
        return false;
    }

    @Override
    public boolean isSolid()
    {
        return false;
    }

    @Override
    public boolean requireDynamicRender()
    {
        return false;
    }

    @Override
    public void setPartHostInfo(AEPartLocation location, IPartHost host, TileEntity tile)
    {
        cableSide = location;
        this.host = host;
        hostTile = tile;
    }

    @Override
    public boolean onActivate(EntityPlayer player, EnumHand hand, Vec3d vec3d)
    {
        return false;
    }

    @Override
    public boolean onShiftActivate(EntityPlayer player, EnumHand hand, Vec3d vec3d)
    {
        return false;
    }

    @Override
    public boolean onClicked(EntityPlayer player, EnumHand hand, Vec3d pos)
    {
        return false;
    }

    @Override
    public boolean onShiftClicked(EntityPlayer player, EnumHand hand, Vec3d pos)
    {
        return false;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, Random random) { }

    @Override
    public void onPlacement(EntityPlayer player, EnumHand hand, ItemStack stack, AEPartLocation side)
    {
        this.ownerID = AEApi.instance().registries().players().getID(player.getGameProfile());
    }

    @Override
    public boolean canBePlacedOn(final BusSupport type)
    {
        return type == BusSupport.CABLE;
    }

    @Override
    public ItemStack getItemStack(PartItemStack type)
    {
        ItemStack stack = this.partStack.copy();

        // Save NBT data if the host was wrenched
        if (type == PartItemStack.WRENCH)
        {
            NBTTagCompound nbt = new NBTTagCompound();

            this.writeToNBT(nbt, type);

            if (!nbt.hasNoTags())
            {
                stack.setTagCompound(nbt);
            }
        }

        return stack;
    }

    @Override
    public void getDrops(List<ItemStack> list, boolean b)
    {

    }

    @Override
    public AENetworkProxy getProxy()
    {
        return proxy;
    }

    @MENetworkEventSubscribe
    public void setPower(final MENetworkPowerStatusChange event)
    {
        this.updateStatus();
    }

    @MENetworkEventSubscribe
    public void updateChannels(final MENetworkChannelsChanged event)
    {
        this.updateStatus();
    }

    @Override
    public void removeFromWorld()
    {
        if (this.getProxy().getNode() != null)
        {
            this.getProxy().getNode().destroy();
        }
    }

    @Override
    public void addToWorld()
    {
        if (hostTile.getWorld().isRemote) { return; }

        getProxy().onReady();

        getProxy().getNode().setPlayerID(this.ownerID);

        if ((this.hostTile != null) && (this.host != null))
        {
            getProxy().getNode().updateState();
        }

        this.updateStatus();
    }

    private void updateStatus()
    {
        if (hostTile.getWorld().isRemote) { return; }

        if (this.getProxy().getNode() != null)
        {
            boolean currentlyActive = this.getProxy().isActive();

            if (currentlyActive != this.isActive)
            {
                this.isActive = currentlyActive;
                this.host.markForUpdate();
            }
        }

        this.onNeighborChanged(null, null, null);
    }

    public final void markForSave()
    {
        if (this.host != null) { this.host.markForSave(); }
    }

    public final void markForUpdate()
    {
        if (this.host != null) { this.host.markForUpdate(); }
    }

    @Override
    public int getLightLevel()
    {
        return 0;
    }

    @Override
    public void gridChanged()
    {

    }

    @Override
    public final DimensionalCoord getLocation()
    {
        return new DimensionalCoord(hostTile.getWorld(), hostTile.getPos().getX(), hostTile.getPos().getY(), hostTile.getPos().getZ());
    }

    @Override
    public IGridNode getExternalFacingNode()
    {
        return null;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        writeToNBT(nbt, PartItemStack.WORLD);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey("owner"))
        {
            this.ownerID = nbt.getInteger("owner");
        }
    }

    public void writeToNBT(final NBTTagCompound data, final PartItemStack saveType)
    {
        if (saveType == PartItemStack.WORLD)
        {
            data.setInteger("owner", this.ownerID);
        }
    }

    @Override
    public void writeToStream(final ByteBuf stream) throws IOException
    {
        stream.writeBoolean(this.isActive());
        stream.writeBoolean(this.isPowered());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean readFromStream(final ByteBuf stream) throws IOException
    {
        boolean oldActive = this.isActive;
        boolean oldPowered = this.isPowered;

        this.isActive = stream.readBoolean();
        this.isPowered = stream.readBoolean();

        return ((oldActive != this.isActive) || (oldPowered != this.isPowered));
    }

    @Override
    public float getCableConnectionLength(AECableType type)
    {
        return 2;
    }

    @Override
    public boolean isLadder(EntityLivingBase entity)
    {
        return false;
    }

    @Override
    public void onEntityCollision(Entity entity)
    {

    }

    @Override
    public void onNeighborChanged(IBlockAccess world, BlockPos blockPos, BlockPos blockPos1)
    {

    }
}