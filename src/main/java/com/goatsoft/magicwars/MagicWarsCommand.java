package com.goatsoft.magicwars;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class MagicWarsCommand extends CommandBase
{
   private ArrayList<String> aliases;
   private ArrayList<String> classNames;
   private ArrayList<String> flags;

   public MagicWarsCommand()
   {
      this.aliases = new ArrayList<String>();
      this.aliases.add("magicwars");
      this.aliases.add("mw");

      this.classNames = new ArrayList<String>();
      this.classNames.add("Thaumcraft");
      this.classNames.add("BloodMagic");
      this.classNames.add("Witchery");
      this.classNames.add("TConstruct");

      this.flags = new ArrayList<String>();
      this.flags.add("true");
      this.flags.add("false");
   }

   @Override
   public String getCommandName()
   {
      return "magicwars";
   }

   @Override
   public String getCommandUsage(ICommandSender sender)
   {
      return "magicwars <player> [Thaumcraft|BloodMagic|TConstruct] [true|false]";
   }

   @Override
   public List getCommandAliases()
   {
      return this.aliases;
   }

   @Override
   public void processCommand(ICommandSender sender, String[] args)
   {
      EntityPlayerMP player = null;
      if(args.length > 0)
      {
         player = getPlayer(sender, args[0]);
         if(player == null)
         {
            sender.addChatMessage(new ChatComponentText("Invalid player"));
            return;
         }
      }

      if(args.length == 1)
      {
         processCommandStatus(sender, player, args);
      }
      else if(args.length == 3)
      {
         processCommandModify(sender, player, args);
      }
      else
      {
         sender.addChatMessage(new ChatComponentText("Invalid arguments"));
      }
   }

   private NBTTagCompound getMWData(EntityPlayerMP player)
   {
      NBTTagCompound playerData = player.getEntityData();
      NBTBase baseData = playerData.getTag("MagicWars");
      NBTTagCompound mwData = null;
      if((baseData != null) && (baseData instanceof NBTTagCompound))
      {
         mwData = (NBTTagCompound) baseData;
      }
      if(mwData == null)
      {
         mwData = new NBTTagCompound();
         playerData.setTag("MagicWars", mwData);
      }
      return mwData;
   }

   private void processCommandStatus(ICommandSender sender, EntityPlayerMP player, String[] args)
   {
      sender.addChatMessage(new ChatComponentText(args[0] + " MagicWars Status"));
      NBTTagCompound mwData = getMWData(player);
      for(String mod : this.classNames)
      {
         sender.addChatMessage(new ChatComponentText(mod + ": " + mwData.getBoolean(mod)));
      }
   }

   private void processCommandModify(ICommandSender sender, EntityPlayerMP player, String[] args)
   {
      if(!this.classNames.contains(args[1]))
      {
         sender.addChatMessage(new ChatComponentText("Invalid mod name"));
         return;
      }

      if(!this.flags.contains(args[2]))
      {
         sender.addChatMessage(new ChatComponentText("Invalid enable flag"));
         return;
      }
      
      boolean flag = true;
      if(args[2].equals("false")) flag = false;

      NBTTagCompound mwData = getMWData(player);
      mwData.setBoolean(args[1], flag);
      MagicWars.network.sendTo(new StatusSyncMessage(mwData), player);

      processCommandStatus(sender, player, args);
   }

   @Override
   public List addTabCompletionOptions(ICommandSender sender, String[] args)
   {
      if(args.length == 1)
      {
         return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
      }
      else if(args.length == 2)
      {
         return this.classNames;
      }
      else if(args.length == 3)
      {
         return this.flags;
      }
      return null;
   }

   @Override
   public boolean isUsernameIndex(String[] args, int index)
   {
      if(index == 1) return true;
      return false;
   }
}

