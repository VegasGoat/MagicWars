package com.goatsoft.magicwars;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tconstruct.tools.blocks.CraftingStationBlock;

public class FilteredCraftingStation extends CraftingStationBlock
{
   public FilteredCraftingStation()
   {
      super(Material.wood);
   }

   @Override
   public TileEntity createNewTileEntity (World world, int meta)
   {
      return new FilteredCraftingStationLogic();
   }
}

