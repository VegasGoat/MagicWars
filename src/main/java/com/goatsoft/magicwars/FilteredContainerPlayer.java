package com.goatsoft.magicwars;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;

public class FilteredContainerPlayer extends ContainerPlayer
{
   private EntityPlayer player;

   public FilteredContainerPlayer(InventoryPlayer inv, boolean local, EntityPlayer player)
   {
      super(inv, local, player);
      this.player = player;
   }

   @Override
   public void onCraftMatrixChanged(IInventory changedInv)
   {
      // gets called from base constructor, when we're not ready yet
      if(this.player == null) return;

      FilterCommon.filterCrafting(this.craftMatrix, this.player, this.craftResult);
   }
}

