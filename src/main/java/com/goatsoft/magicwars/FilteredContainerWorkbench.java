package com.goatsoft.magicwars;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public class FilteredContainerWorkbench extends ContainerWorkbench
{
   private EntityPlayer player;

   public FilteredContainerWorkbench(EntityPlayer player, InventoryPlayer inv, World worldObj, int x, int y, int z)
   {
      super(inv, worldObj, x, y, z);
      this.player = player;
   }

   @Override
   public boolean canInteractWith(EntityPlayer player)
   {
      return true;
   }

   @Override
   public void onCraftMatrixChanged(IInventory changedInv)
   {
      // gets called from base constructor, when we're not ready yet
      if(this.player == null) return;

      FilterCommon.filterCrafting(this.craftMatrix, this.player, this.craftResult);
   }
}

