package com.xyfero.steammotion.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
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
        return new TileEntityFurnace();
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!keepInventory)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityFurnace) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityFurnace)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }


    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Blocks.FURNACE);
    }
}
