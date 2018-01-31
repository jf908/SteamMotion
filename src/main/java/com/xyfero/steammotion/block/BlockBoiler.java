package com.xyfero.steammotion.block;

import com.xyfero.steammotion.item.ItemPack;
import com.xyfero.steammotion.tileentity.TileEntityBoiler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBoiler extends BlockContainer {
    private static boolean keepInventory;

    protected BlockBoiler() {
        super(Material.IRON);

        SteamMotionBlock.setBlockName(this, "boiler");
        setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.FURNACE);
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityBoiler();
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!keepInventory)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityBoiler) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityBoiler)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn.getHeldItem(hand).getItem() instanceof ItemPack) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityBoiler) {
                ItemStack itemstack = playerIn.getHeldItem(hand).copy();
                boolean flag = ((TileEntityBoiler) tileentity).putJetpack(itemstack);
                if(flag) {
                    if(!worldIn.isRemote) {
                        playerIn.getHeldItem(hand).shrink(1);
                    }
                    return true;
                }
            }
        } else if(playerIn.getHeldItem(hand).isEmpty()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityBoiler) {
                ItemStack is = ((TileEntityBoiler)tileentity).takeJetpack();
                if(!is.isEmpty()) {
                    playerIn.addItemStackToInventory(is);
                    return true;
                }
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
