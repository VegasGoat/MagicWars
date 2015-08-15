package com.goatsoft.magicwars;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.Minecraft;

public class StatusSyncMessage implements IMessage
{
   public NBTTagCompound data;

   public StatusSyncMessage()
   {
      this.data = new NBTTagCompound();
   }

   public StatusSyncMessage(NBTTagCompound data)
   {
      this.data = data;
   }

   public void fromBytes(ByteBuf buf)
   {
      this.data = ByteBufUtils.readTag(buf);
   }

   public void toBytes(ByteBuf buf)
   {
      ByteBufUtils.writeTag(buf, this.data);
   }

   public static class Handler implements IMessageHandler<StatusSyncMessage, IMessage>
   {
      public IMessage onMessage(StatusSyncMessage message, MessageContext ctx)
      {
         Minecraft.getMinecraft().thePlayer.getEntityData().setTag("MagicWars", message.data);
         return null;
      }
   }
}

