package com.goatsoft.magicwars;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tconstruct.tools.blocks.CraftingSlab;
import java.util.List;

public class FilteredCraftingSlab extends CraftingSlab
{
   public FilteredCraftingSlab()
   {
      super(Material.wood);
   }

   @Override
   public TileEntity createTileEntity(World world, int meta)
   {
      return new FilteredCraftingStationLogic();
   }

   @Override
   public TileEntity createNewTileEntity(World world, int meta)
   {
      return createTileEntity(world, meta);
   }

   @Override
   public void getSubBlocks(Item b, CreativeTabs tab, List list)
   {
      list.add(new ItemStack(b, 1, 0));
   }
}

