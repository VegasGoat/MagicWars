package com.goatsoft.magicwars;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class PlayerModifier
{
   /**
    * Modify a player after they join the world.
    */
   @SubscribeEvent
   public void onJoinWorld(EntityJoinWorldEvent event)
   {
      if (event.entity instanceof EntityPlayer)
      {
         EntityPlayer player = (EntityPlayer) event.entity;

         // install our container that allows filtering recipes
         if(!(player.inventoryContainer instanceof FilteredContainerPlayer))
         {
            player.inventoryContainer = new FilteredContainerPlayer(player.inventory, !event.world.isRemote, player);
            player.openContainer = player.inventoryContainer;
         }
      }
   }

   /**
    * Sync player data to client on login.
    */
   @SubscribeEvent
   public void onLogin(PlayerLoggedInEvent event)
   {
      if((event.player instanceof EntityPlayerMP) && (!event.player.worldObj.isRemote))
      {
         EntityPlayerMP player = (EntityPlayerMP) event.player;
         try
         {
            NBTTagCompound mwData = (NBTTagCompound) player.getEntityData().getTag("MagicWars");
            if(mwData != null)
            {
               MagicWars.network.sendTo(new StatusSyncMessage(mwData), player);
            }
         }
         catch(ClassCastException ex)
         {
         }
      }
   }
}

