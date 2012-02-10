/**
* dExchange v1.x
* Copyright (C) 2011 Visual Illusions Entertainment
* @author darkdiplomat <darkdiplomat@visualillusionsent.net>
*
* This file is part of dExchange.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with dExchange.  If not, see http://www.gnu.org/licenses/gpl.html
*/

//TODO Need to do messages for S-SHOP, Need to do S-TRADE?, Need to possible do PI-SHOP or PI-TRADE? (Player Inventory), Possible add chests to Commands?

public class dExListener extends PluginListener {
	dExchange dEx;
	dExActions dExA;
	dExData dExD;
	int idsign = 0;
	
	public dExListener(dExchange dEx){
		this.dEx = dEx;
		dExA = dEx.dExA;
		dExD = dEx.dExD;
	}
	
	public boolean onCommand(Player player, String[] cmd){
		if (cmd[0].equals("/dex")){
			if(cmd.length > 1){
				if (cmd[1].equals("buy")){
					if((player.canUseCommand("/dexbc"))||(player.canUseCommand("/dexadmin"))||(player.canUseCommand("/dexall"))){
						if(cmd.length < 4){
							player.notify("Usage: /dex buy <item> <amount>");
							return true;
						}
						else{
							if(cmd.length > 4){
								if(cmd[4].equals("stack")){
									return dExA.BuyCommand(player, cmd[2], cmd[3], true);
								}else{
									return dExA.BuyCommand(player, cmd[2], cmd[3], false);
								}
							}
							else{
								return dExA.BuyCommand(player, cmd[2], cmd[3], false);
							}
						}
					}
				}
				else if(cmd[1].equals("sell")){
					if((player.canUseCommand("/dexsc"))||(player.canUseCommand("/dexadmin"))||(player.canUseCommand("/dexall"))){
						if(cmd.length < 4){
							player.notify("Usage: /dex sell <item> <amount>");
							return true;
						}
						else{
							if(cmd.length > 4){
								if(cmd[4].equals("stack")){
									return dExA.SellCommand(player, cmd[2], cmd[3], true);
								}else{
									return dExA.SellCommand(player, cmd[2], cmd[3], false);
								}
							}
							else{
								return dExA.SellCommand(player, cmd[2], cmd[3], false);
							}
						}
					}
				}
				else if(cmd[1].equals("list")){
					if (cmd.length < 3){
						return dExA.displayList(player, "1");
					}
					else{
						return dExA.displayList(player, cmd[2]);
					}
				}
				else if((cmd[1].equalsIgnoreCase("ppricechange"))||(cmd[1].equalsIgnoreCase("ppc"))){
					if((player.canUseCommand("/dexppc"))||(player.canUseCommand("/dexadmin"))||(player.canUseCommand("/dexall"))){
						if(cmd.length > 2){
							if(cmd[2].equalsIgnoreCase("CANCEL")){
								return dExA.cancelSignChange(player, false, false);
							}
							else{
								return dExA.setSignPrice(player, cmd[2]);
							}
						}
						else{
							player.notify("Usage: /dex ppricechange (ppc) <newprice|CANCEL>");
							return true;
						}
					}
				}
				else if((cmd[1].equalsIgnoreCase("pamountchange"))||(cmd[1].equalsIgnoreCase("pac"))){
					if((player.canUseCommand("/dexpac"))||(player.canUseCommand("/dexadmin"))||(player.canUseCommand("/dexall"))){
						if(cmd.length > 2){
							if(cmd[2].equalsIgnoreCase("CANCEL")){
								return dExA.cancelSignChange(player, true, false);
							}
							else{
								return dExA.setSignAmount(player, cmd[2]);
							}
						}
						else{
							player.notify("Usage: /dex pamountchange (pac) <newamount|CANCEL>");
							return true;
						}
					}
				}
				else if(cmd[1].equalsIgnoreCase("additem")){
					if(player.canUseCommand("/dexadmin")){
						if(cmd.length > 6){
							return dExA.addItemList(player, cmd[2], cmd[3], cmd[4], cmd[5], cmd[6]);
						}
						else{
							player.notify("Usage: /dex additem <Name> <ID> <Damage> <BuyPrice> <SellPrice>");
							return true;
						}
					}
				}
				else if(cmd[1].equalsIgnoreCase("cisp")){
					if(player.canUseCommand("/dexadmin")){
						if(cmd.length > 3){
							return dExA.updateItemSPrice(player, cmd[2], cmd[3]);
						}
						else{
							player.notify("Usage: /dex cisp <Name> <SellPrice>");
							return true;
						}
					}
				}
				else if(cmd[1].equalsIgnoreCase("cibp")){
					if(player.canUseCommand("/dexadmin")){
						if(cmd.length > 3){
							return dExA.updateItemBPrice(player, cmd[2], cmd[3]);
						}
						else{
							player.notify("Usage: /dex cibp <Name> <BuyPrice>");
							return true;
						}
					}
				}
				else if(cmd[1].equalsIgnoreCase("removeItem")){
					if(player.canUseCommand("/dexadmin")){
						if(cmd.length > 2){
							return dExA.removeItemFromList(player, cmd[2]);
						}
						else{
							player.notify("Usage: /dex removeitem <Name>");
							return true;
						}
					}
				}
				else{
					return dExA.priceCheck(player, cmd[1]);
				}
			}
			else{
				player.sendMessage("§7-----§6dExchange by §aDarkDiplomat§7-----");
				player.sendMessage("§7-----§6"+dEx.version+" Installed§7-----");
				if(!dEx.isLatest()){
					player.sendMessage("§7-----§6An update is availible! Latest = §2"+dEx.CurrVer+"§7-----");
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean onSignChange(Player player, Sign sign){
		if(sign.getText(0).equalsIgnoreCase("[G-SHOP]")){
			return dExA.makeGShop(player, sign);
		}else if (sign.getText(0).equalsIgnoreCase("[G-TRADE]")){
			return dExA.makeGTrade(player, sign);
		}
		else if(sign.getText(0).equalsIgnoreCase("[P-SHOP]")){
			if(!dExD.canmakemore(player)){
				return true;
			}
			else{
				return dExA.makePShop(player, sign);
			}
		}
		else if(sign.getText(0).equalsIgnoreCase("[P-TRADE]")){
			if(!dExD.canmakemore(player)){
				return true;
			}
			else{
				return dExA.makePTrade(player, sign);
			}
		}
		else if(sign.getText(0).equalsIgnoreCase("[S-SHOP]")){
			return dExA.makeSShop(player, sign);
		}
		return false;
	}
	
	public boolean onBlockPlace(Player player, Block blockPlaced, Block blockClicked, Item itemInHand){
		if (blockPlaced.getType() == 54){
			if(dExD.checklink(player)){
				etc.getServer().getWorld(player.getLocation().dimension).setBlockAt(54, blockPlaced.getX(), blockPlaced.getY(), blockPlaced.getZ());
				blockPlaced.update();
				Chest chest = (Chest)player.getWorld().getOnlyComplexBlock(blockPlaced);
				itemInHand.setAmount(itemInHand.getAmount()-1);
				player.getInventory().update();
				dExD.closelink(player, chest);
				String mess = dExD.pmessage(204, "", "");
				player.sendMessage(mess);
				dExD.logAct(302, player.getName(),"", "", "", "", "", "", String.valueOf(chest.getX()), String.valueOf(chest.getY()), String.valueOf(chest.getZ()), String.valueOf(chest.getWorld().getType().getId()), "", "", "");
				return true;
			}
		}
		return false;
	}
	
	public boolean onBlockDestroy(Player player, Block block){
		if (block.getType() == 54){
			if(dExD.checklink(player)){
				if(!isChestOwner(player, block)){
					player.notify("This chest is protected and you do not own it!");
					return true;
				}
				Chest chest = (Chest)player.getWorld().getOnlyComplexBlock(block);
				if(isDEXChest(player, chest)){
					player.notify("This chest is part of a link you do not own!");
					return true;
				}
				dExD.closelink(player, chest);
				String mess = dExD.pmessage(204, "", "");
				player.sendMessage(mess);
				dExD.logAct(302, player.getName(),"", "", "", "", "", "", String.valueOf(chest.getX()), String.valueOf(chest.getY()), String.valueOf(chest.getZ()), String.valueOf(chest.getWorld().getType().getId()), "", "", "");
				return true;
			}
		}
		else if((block.getType() == 63) || (block.getType() == 68)){
			Sign sign = (Sign)player.getWorld().getComplexBlock(block);
			String pname = sign.getText(3);
			if(dExD.checkNameFix(sign.getText(3))){
				pname = dExD.NameFix(sign.getText(3));
			}
			if(sign.getText(0).equals("§9[P-TRADE]")||sign.getText(0).equals("§5[P-SHOP]")){
				if((player.getName().equals(pname)) || (player.canUseCommand("/dexadmin"))){
					if(dExA.SPC.containsKey(player)||dExA.SAC.containsKey(player)){
						return dExA.SignChange(player, sign);
					}
				}
				else{
					if(dExA.SPC.containsKey(player)||dExA.SAC.containsKey(player)){
						dExA.cancelSignChange(player, false, true);
						return dExD.ErrorMessage(player, 132);
					}
				}
			}
		}
		return false;
	}
	
	public boolean onBlockRightClick(Player player, Block block, Item iih){
		if((block.getType() == 63) || (block.getType() == 68)){
			Sign sign = (Sign)player.getWorld().getComplexBlock(block);
			if(sign.getText(0).equals("§2[G-TRADE]")){
				if((player.canUseCommand("/dexugts"))||(player.canUseCommand("/dexadmin"))||(player.canUseCommand("/dexall"))){
					Item item = player.getItemStackInHand();
					return dExA.GobalSignSell(player, item, sign);
				}
				else{
					return dExD.ErrorMessage(player, 102);
				}
			}
			else if (sign.getText(0).equals("§1[G-SHOP]")){
				if((player.canUseCommand("/dexugss"))||(player.canUseCommand("/dexadmin"))||(player.canUseCommand("/dexall"))){
					return dExA.gobalBuySign(player, sign);
				}
				else{
					return dExD.ErrorMessage(player, 102);
				}
			}
			else if (sign.getText(0).equals("§5[P-SHOP]")){
				if((player.canUseCommand("/dexupss"))||(player.canUseCommand("/dexadmin"))||(player.canUseCommand("/dexall"))){
					Chest chest = dExD.getChest(sign);
					if(chest != null){
						return dExA.PlayerBuySign(player, sign, chest);
					}
					else{
						return dExD.ErrorMessage(player, 117);
					}
				}
				else{
					return dExD.ErrorMessage(player, 102);
				}
			}
			else if (sign.getText(0).equals("§9[P-TRADE]")){
				if(player.canUseCommand("/dexupts")||player.canUseCommand("/dexadmin")||player.canUseCommand("/dexall")){
					Chest chest = dExD.getChest(sign);
					if(chest != null){
						Item item = player.getItemStackInHand();
						return dExA.PlayerSellSign(player, sign, chest, item);
					}
					else{
						return dExD.ErrorMessage(player, 117);
					}
				}
				else{
					return dExD.ErrorMessage(player, 102);
				}
			}
			else if (sign.getText(0).equals("§7[S-SHOP]")){
				if(player.canUseCommand("/dexusss")||player.canUseCommand("/dexadmin")||player.canUseCommand("/dexall")){
					Chest chest = dExD.getChest(sign);
					if(chest != null){
						return dExA.ServerBuySign(player, sign, chest);
					}
					else{
						return dExD.ErrorMessage(player, 117);
					}
				}
				else{
					return dExD.ErrorMessage(player, 102);
				}
			}
		}
		else if(block.getType() == 54){
			Sign sign;
			Chest chest = (Chest)player.getWorld().getOnlyComplexBlock(block);
			if (chest.findAttachedChest() != null){
				sign = dExD.getSign(chest);
				if (sign != null){
					if((sign.getText(0).equals("§5[P-SHOP]")) || (sign.getText(0).equals("§9[P-TRADE]"))){
						String pname = sign.getText(3);
						if(dExD.checkNameFix(sign.getText(3))){
							pname = dExD.NameFix(sign.getText(3));
						}
						if((player.getName().equals(pname)) || (player.canUseCommand("/dexadmin"))){
							return false;
						}
						else{
							return dExD.ErrorMessage(player, 118);
						}
					}
					else if(sign.getText(0).equals("§7[S-SHOP]")){
						if((player.canUseCommand("/dexadmin"))){
							return false;
						}
						else{
							return dExD.ErrorMessage(player, 118);
						}
					}
				}
				else{
					Block block2 = null;
					int x = chest.getX()-2;
					while((x < chest.getX()+2) && (block2 == null)){
						x++;
						int z = chest.getZ();
						while(z < chest.getZ()+2){
							block2 = (Block)player.getWorld().getBlockAt(x, chest.getY(), chest.getZ());
							if(block2.getType() == 54){
								break;
							}
							else{
								block2 = null;
								z++;
							}
						}
					}
					Chest chest2 = (Chest)player.getWorld().getOnlyComplexBlock(block2);
					sign = dExD.getSign(chest2);
					if (sign != null){
						if((sign.getText(0).equals("§5[P-SHOP]")) || (sign.getText(0).equals("§9[P-TRADE]"))){
							String pname = sign.getText(3);
							if(dExD.checkNameFix(sign.getText(3))){
								pname = dExD.NameFix(sign.getText(3));
							}
							if((player.getName().equals(pname)) || (player.canUseCommand("/dexadmin"))){
								return false;
							}
							else{
								return dExD.ErrorMessage(player, 118);
							}
						}
						else if(sign.getText(0).equals("§7[S-SHOP]")){
							if((player.canUseCommand("/dexadmin"))){
								return false;
							}
							else{
								return dExD.ErrorMessage(player, 118);
							}
						}
					}
				}
			}
			else{
				sign = dExD.getSign(chest);
				if (sign != null){
					if((sign.getText(0).equals("§5[P-SHOP]")) || (sign.getText(0).equals("§9[P-TRADE]"))){
						String pname = sign.getText(3);
						if(dExD.checkNameFix(sign.getText(3))){
							pname = dExD.NameFix(sign.getText(3));
						}
						if((player.getName().equals(pname)) || (player.canUseCommand("/dexadmin"))){
							return false;
						}
						else{
							return dExD.ErrorMessage(player, 118);
						}
					}
					else if(sign.getText(0).equals("§7[S-SHOP]")){
						if((player.canUseCommand("/dexadmin"))){
							return false;
						}
						else{
							return dExD.ErrorMessage(player, 118);
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean onBlockBreak(Player player, Block block){
		if((block.getType() == 63) || (block.getType() == 68)){
			Sign sign = (Sign)etc.getServer().getWorld(player.getLocation().dimension).getComplexBlock(block);
			if((sign.getText(0).equals("§5[P-SHOP]")) || (sign.getText(0).equals("§9[P-TRADE]"))){
				String pname = sign.getText(3);
				if(dExD.checkNameFix(sign.getText(3))){
					pname = dExD.NameFix(sign.getText(3));
				}
				if ((player.getName().equals(pname) || (player.canUseCommand("/dexadmin")))){
					dExD.removePlayerSignTotal(dExD.NameFix(sign.getText(3)));
					if(dExD.checklink(player)){
						Sign sign2 = dExD.getOpenLinkSign(player);
						int x = sign.getX(), y = sign.getY(), z = sign.getZ(), w = sign.getWorld().getType().getId();
						int cx = sign2.getX(), cy = sign2.getY(), cz = sign2.getZ(), cw = sign2.getWorld().getType().getId();
						if((x == cx)&&(y == cy)&&(z == cz)&&(w == cw)){
							dExD.cancellink(player);
							String mess = dExD.pmessage(205, "", "");
							player.sendMessage(mess);
							return false;
						}
					}
					else{
						Chest chest = dExD.getChest(sign);
						if (chest != null){
							dExD.removeline(sign, chest, false);
							String mess = dExD.pmessage(211, "", "");
							player.sendMessage(mess);
							dExD.logAct(303, player.getName(), "", "SIGN", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getY()), String.valueOf(chest.getX()), String.valueOf(chest.getY()), String.valueOf(chest.getZ()), String.valueOf(chest.getWorld().getType().getId()), "", "", "");
							return false;
						}
					}
				}
				else{
					sign.setText(0, sign.getText(0));
					sign.setText(1, sign.getText(1));
					sign.setText(2, sign.getText(2));
					sign.setText(3, sign.getText(3));
					sign.update();
					return dExD.ErrorMessage(player, 119);
				}
			}
		}
		else if(block.getType() == 54){
			Chest chest = (Chest)player.getWorld().getOnlyComplexBlock(block);
			Sign sign = dExD.getSign(chest);
			if (chest.findAttachedChest() != null){
				sign = dExD.getSign(chest);
				if (sign != null){
					if((sign.getText(0).equals("§5[P-SHOP]")) || (sign.getText(0).equals("§9[P-TRADE]"))){
						String pname = sign.getText(3);
						if(dExD.checkNameFix(sign.getText(3))){
							pname = dExD.NameFix(sign.getText(3));
						}
						if((player.getName().equals(pname)) || (player.canUseCommand("/dexadmin"))){
							dExD.removeline(sign, chest, true);
							String mess = dExD.pmessage(211, "", "");
							player.sendMessage(mess);
							dExD.logAct(303, player.getName(), "", "CHEST", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getY()), String.valueOf(chest.getX()), String.valueOf(chest.getY()), String.valueOf(chest.getZ()), String.valueOf(chest.getWorld().getType().getId()), "", "", "");
							return false;
						}
						else{
							return dExD.ErrorMessage(player, 120);
						}
					}
				}
				else{
					Block block2 = null;
					int x = chest.getX()-2;
					while((x < chest.getX()+2) && (block2 == null)){
						x++;
						int z = chest.getZ();
						while(z < chest.getZ()+2){
							block2 = (Block)player.getWorld().getBlockAt(x, chest.getY(), chest.getZ());
							if(block2.getType() == 54){
								break;
							}
							else{
								block2 = null;
								z++;
							}
						}
					}
					Chest chest2 = (Chest)player.getWorld().getOnlyComplexBlock(block2);
					sign = dExD.getSign(chest2);
					if (sign != null){
						if((sign.getText(0).equals("§5[P-SHOP]")) || (sign.getText(0).equals("§9[P-TRADE]"))){
							String pname = sign.getText(3);
							if(dExD.checkNameFix(sign.getText(3))){
								pname = dExD.NameFix(sign.getText(3));
							}
							if((player.getName().equals(pname)) || (player.canUseCommand("/dexadmin"))){
								dExD.removeline(sign, chest, true);
								String mess = dExD.pmessage(211, "", "");
								player.sendMessage(mess);
								dExD.logAct(303, player.getName(), "", "CHEST", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getY()), String.valueOf(chest.getX()), String.valueOf(chest.getY()), String.valueOf(chest.getZ()), String.valueOf(chest.getWorld().getType().getId()), "", "", "");
								return false;
							}
							else{
								return dExD.ErrorMessage(player, 120);
							}
						}
					}
				}
			}
			else if (sign != null){
				if((sign.getText(0).equals("§5[P-SHOP]")) || (sign.getText(0).equals("§9[P-TRADE]"))){
					String pname = sign.getText(3);
					if(dExD.checkNameFix(sign.getText(3))){
						pname = dExD.NameFix(sign.getText(3));
					}
					if((player.getName().equals(pname)) || (player.canUseCommand("/dexadmin"))){
						dExD.removeline(sign, chest, true);
						String mess = dExD.pmessage(211, "", "");
						player.sendMessage(mess);
						dExD.logAct(303, player.getName(), "", "CHEST", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getY()), String.valueOf(chest.getX()), String.valueOf(chest.getY()), String.valueOf(chest.getZ()), String.valueOf(chest.getWorld().getType().getId()), "", "", "");
						return false;
					}
					else{
						return dExD.ErrorMessage(player, 120);
					}
				}
			}
		}
		return false;
	}
	
	private boolean isChestOwner(Player player, Block block){
		boolean isOwner = true, isSet = false;
		PluginLoader load = etc.getLoader();
		if(load.getPlugin("ChastityChest") != null && load.getPlugin("ChastityChest").isEnabled()){
			try{
				isOwner = (Boolean)load.callCustomHook("ChastityChest-Check", new Object[]{player});
				isSet = true;
			}catch(Exception E){ //API Failed/Non-Existent
				isOwner = true;
				isSet = false;
			}
		}
		else if(load.getPlugin("LWC") != null && load.getPlugin("LWC").isEnabled()){
			try{
				isOwner = (Boolean)load.callCustomHook("LWC-AccessCheck", new Object[]{player, block});
				isSet = true;
			}catch(Exception E){ //API Failed/Non-Existent
				isOwner = true;
				isSet = false;
			}
		}
		if(load.getPlugin("Realms") != null && load.getPlugin("Realms").isEnabled() && !isSet){
			try{
				isOwner = (Boolean)load.callCustomHook("Realms-PermissionCheck", new Object[]{"INTERACT", player, block});
				isSet = true;
			}catch(Exception E){ //API Failed/Non-Existent
				isOwner = true;
				isSet = false;
			}
		}
		if(load.getPlugin("Cuboids2") != null && load.getPlugin("Cuboids2").isEnabled() && !isSet){
			try{
				isOwner = (Boolean)load.callCustomHook("CuboidsAPI", new Object[]{"PLAYER_ALLOWED", player, block});
			}catch(Exception E){ //API Failed/Non-Existent
				isOwner = true;
			}
		}
		return isOwner;
	} 
	
	private boolean isDEXChest(Player player, Chest chest){
		Sign sign = dExD.getSign(chest);
		if (chest.findAttachedChest() != null){
			if (sign != null){
				if((sign.getText(0).equals("§5[P-SHOP]")) || (sign.getText(0).equals("§9[P-TRADE]"))){
					String pname = sign.getText(3);
					if(dExD.checkNameFix(sign.getText(3))){
						pname = dExD.NameFix(sign.getText(3));
					}
					if(!player.getName().equals(pname) && !player.canUseCommand("/dexadmin")){
						return true;
					}
				}
			}
			else{
				Block block2 = null;
				int x = chest.getX()-2;
				while((x < chest.getX()+2) && (block2 == null)){
					int z = chest.getZ();
					while(z < chest.getZ()+2){
						block2 = (Block)player.getWorld().getBlockAt(x, chest.getY(), chest.getZ());
						if(block2.getType() == 54){
							break;
						}
						else{
							block2 = null;
							z++;
							x++;
						}
					}
				}
				Chest chest2 = (Chest)player.getWorld().getOnlyComplexBlock(block2);
				sign = dExD.getSign(chest2);
				if (sign != null){
					if((sign.getText(0).equals("§5[P-SHOP]")) || (sign.getText(0).equals("§9[P-TRADE]"))){
						String pname = sign.getText(3);
						if(dExD.checkNameFix(sign.getText(3))){
							pname = dExD.NameFix(sign.getText(3));
						}
						if(!player.getName().equals(pname) && !player.canUseCommand("/dexadmin")){
							return true;
						}
					}
				}
			}
		}
		else if (sign != null){
			if((sign.getText(0).equals("§5[P-SHOP]")) || (sign.getText(0).equals("§9[P-TRADE]"))){
				String pname = sign.getText(3);
				if(dExD.checkNameFix(sign.getText(3))){
					pname = dExD.NameFix(sign.getText(3));
				}
				if(!player.getName().equals(pname) && !player.canUseCommand("/dexadmin")){
					return true;
				}
			}
			else if(sign.getText(0).equals("§7[S-SHOP]")){
				if(!player.canUseCommand("/dexpsss") && !player.canUseCommand("/dexadmin") && !player.canUseCommand("/dexall")){
					return true;
				}
			}
		}
		return false;
	}
}
