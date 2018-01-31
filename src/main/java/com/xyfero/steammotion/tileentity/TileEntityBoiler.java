package com.xyfero.steammotion.tileentity;

import com.xyfero.steammotion.item.ItemPack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

public class TileEntityBoiler extends TileEntity implements ITickable, ISidedInventory {
    private int burnTime;
    private static final int[] SLOT = new int[] {0};
    private NonNullList<ItemStack> itemStacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        itemStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, itemStacks);
        burnTime = compound.getInteger("BurnTime");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", burnTime);
        ItemStackHelper.saveAllItems(compound, itemStacks);

        System.out.println("Saving..." + itemStacks.get(0).getItem().toString());

        return compound;
    }

    public ItemStack takeJetpack() {
        return removeStackFromSlot(0);
    }
//
    /**
     * Returns true if success, false if full
     *
     * @param itemstack
     * @return
     */
    public boolean putJetpack(ItemStack itemstack) {
        if(itemStacks.get(0).isEmpty()) {
            setInventorySlotContents(0, itemstack);
            markDirty();
            return true;
        }
        return false;
    }

    private final int amount = 10;

    public void update() {
        if(!world.isRemote) {
            ItemStack itemstack = itemStacks.get(0);
            if(!itemstack.isEmpty() && burnTime > 0 && itemstack.getMetadata() > 0) {
                ItemStack slot = itemStacks.get(0);

                if((int)(double)100 * (slot.getMaxDamage() - slot.getMetadata() - amount)/slot.getMaxDamage() != (int)(double)100 * (slot.getMaxDamage() - slot.getMetadata())/slot.getMaxDamage()) {
                    needsUpdate();
                }


                burnTime--;
                if(slot.getMetadata() - amount < 0) {
                    slot.setItemDamage(0);
                } else {
                    slot.setItemDamage(slot.getMetadata() - amount);
                }
            }
        }
    }

    public void addBurnTime(int time) {
        burnTime += time;
        markDirty();
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        // getUpdateTag() is called whenever the chunkdata is sent to the
        // client. In contrast getUpdatePacket() is called when the tile entity
        // itself wants to sync to the client. In many cases you want to send
        // over the same information in getUpdateTag() as in getUpdatePacket().
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        // Prepare a packet for syncing our TE to the client. Since we only have to sync the stack
        // and that's all we have we just write our entire NBT here. If you have a complex
        // tile entity that doesn't need to have all information on the client you can write
        // a more optimal NBT here.
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        // Here we get the packet from the server and read it into our client side tile entity
        this.readFromNBT(packet.getNbtCompound());
    }

    /**
     * ISidedInventory
     */

    public int[] getSlotsForFace(EnumFacing side) {
        return SLOT;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return stack.getMetadata() == 0;
    }

    /**
     * IInventory
     */

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return 1;
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : itemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index) {
        return itemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count) {
        needsUpdate();
        return ItemStackHelper.getAndSplit(itemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(itemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack) {
        needsUpdate();
        itemStacks.set(index, stack);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit() {
        return 1;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    public void openInventory(EntityPlayer player) {
    }

    public void closeInventory(EntityPlayer player) {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() instanceof ItemPack;
    }

    public int getField(int id) {
        switch(id) {
            case 0:
                return burnTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch(id) {
            case 0:
                burnTime = value;
                break;
        }
    }

    public int getFieldCount() {
        return 1;
    }

    public void clear() {
        itemStacks.clear();
    }

    /**
     * IWorldNameable
     */

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName() {
        return "Boiler";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName() {
        return false;
    }

    private void needsUpdate() {
        if (world != null) {
            IBlockState state = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), state, state, 3);
        }
    }
}
