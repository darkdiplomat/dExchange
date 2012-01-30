dExchange v1.x
Copyrighted (c) 2011 Visual Illusions Entertainment
Authored by: darkdiplomat

/**
* dExchange v1.x
* Copyright (C) 2011 Visual Illusions Entertainment
* @author darkdiplomat <darkdiplomat@hotmail.com>
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
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

[b][size=18pt]Features:[/size][/b]
[list]
[*]Global Shop Signs/Commands
[*]Item Lists with Global Price Checking
[*]Player Shops
[*]Remote chests for Player shops
[/list]

[size=18pt][b]Permissions:[/b][/size]
[list]
[*][b]/dexall[/b] - use/place all signs
[*][b]/dexadmin[/b] - use/place/destory all signs
[*][b]/dexpgss[/b] - place G-SHOP Signs
[*][b]/dexugss[/b] - use G-SHOP Signs
[*][b]/dexpgts[/b] - place G-TRADE Signs
[*][b]/dexugts[/b] - use G-TRADE Signs
[*][b]/dexppss[/b] - place P-SHOP Signs
[*][b]/dexupts[/b] - use P-SHOP Signs
[*][b]/dexppts[/b] - place P-TRADE Signs
[*][b]/dexupts[/b] - use P-TRADE Signs
[*][b]/dexbc[/b] - use /dex buy
[*][b]/dexsc[/b] - use /dex sell
[/list]

[size=18pt][b]Commands:[/b][/size]
[list]
[*][b]/dex buy <ItemName> <amount> - buy an item from Global
[*][b]/dex sell <ItemName> <amount> - sell an item to Global
[*][b]/dex <ItemName> - Checks Item ID Damage Buy Price and Sell Price
[*][b]/dex list [page#] - displays a list of Items and Prices
[/list]

[size=18pt][b]Sign Formats:[/b][/size]
[size=10pt][b][G-SHOP][/b] - used to buy items from a global shop [/size]
[list]
[*]Line1= [g-shop]
[*]Line2= ItemName
[*]Line3= Amount
[*]Line4= Price (auto filled in)
[/list]

[size=10pt][b][G-TRADE][/d] - used to sell items to a global shop [/size]
Line1: [g-trade]
All other lines blank

[size=10pt][b][P-SHOP][/b] - Player owned Shop [/size]
[list]
[*]Line1 = [p-shop]
[*]Line2 = ID:DAMAGE:AMOUNT or ID:AMOUNT or ITEMNAME:AMOUNT
[*]Line 3 = Price the player wants to charge for the item
[*]Line 4 = Player's Name (auto filled in)
[/list]
Place or Left click a chest to complete the link

[size=10pt][b][P-TRADE][/b] - Player owned sell sign [/size]
Ok a little more info on P-TRADE  this allows players to sell items to the player who owns the sign. Amount is the amount the player wants of an item. and will decrease as the items are sold to the player.
[list]
[*]Line1 = [p-trade]
[*]Line2 = ID:DAMAGE:AMOUNT or ID:AMOUNT or ITEMNAME:AMOUNT
[*]Line 3 = Price the player wants to charge for the item
[*]Line 4 = Player's Name (auto filled in)
[/list]
Place or Left click a chest to complete the link
		
[size=18pt][b]Configuration Files:[/b][/size]
	[b]plugins/config/dExchange/[/b]
		[b]dExSettings.ini[/b] - Basic Settings
			[b]Global Account[/b] = Account to use for Global Actions (takes and gives money to this account) (must be a joint account) (set to N/A to disable)
                        [b]BlackListedItems[/b]= Items that players are not allowed to sell/buy via P-SHOP or P-TRADE signs
                        [b]LogActions[/b] = logs actions of dExchange to the dConomy logs
                        [b]Use-MySQL[/b]= set to true to use MySQL
                        [b]Use-CanaryMySQL[/b]= set to true to use Canary's MySQL connection settings
                        [b]UserName[/b]= Username for database
                        [b]Password[/b]= Password for database
                        [b]DataBase[/b]= database for where MySQL data is stored

[size=18pt][b]Logging Transactions:[/b][/size]
	If turned on it will log transactions through dConomy's Log system

[size=18pt][b]MySQL Things:[/b][/size]
	There are 2 tables for dExchange which need be created manual before MySQL is turned on
		[b]dExchange[/b] - Item Lists for ID Damage BuyPrice(Global) and SellPrice(Global)
		[b]dExchangeChests[/b] - Locations of Signs and Chests for P-SHOP and P-TRADE signs

        Use this .sql file to create the needed data  - dExchange.sql
	
[size=18pt][b]FlatFile Things:[/b][/size]
	ItemNameFile - List of Items and Prices
        ChestTrade - List of Sign/Chest locations used for P-SHOP and P-TRADE

[size=18pt][b]Planned Features For Future Release:[/b][/size]
Group Specific Black Lists for P-Shop and P-Trade Signs
Global Shops that use chests

[size=18pt][b]Known Bugs:[/b][/size]
There are some issues will selling items to a sign if you have an item that is exactly like the one being sold further up the hot bar (ie: if slot 2 matches slot 6 and you want to sell slot 6  slot 2 is taken but doesnt update properly)
Buying from a sign with an item in hand also cause the inventory to not update properly (item will be there though)

[size=18pt][b]ChangeLog:[/b][/size]
[changelog]
v1.0.0 - Initial Release
[/changelog]

[size=18pt][b]Download:[/b][/size]
[url=http://bit.ly/rJiMRy][size=12pt][b]dExchange v1.0.0[/b][/size][/url]
[url=http://bit.ly/v39Zsv]Dropbox Backup[/url]