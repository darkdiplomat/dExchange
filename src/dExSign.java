import java.util.ArrayList;

public class dExSign {
	private Sign sign;
	private String owner;
	private ArrayList<Chest> Chests;
	
	public dExSign(Sign sign, String owner){
		Chests = new ArrayList<Chest>();
		this.sign = sign;
		this.owner = owner;
	}
	
	public void addChest(Chest chest){
		Chests.add(chest);
	}
	
	public void removeChest(Chest chest){
		Chests.remove(chest);
	}
	
	public boolean containsChest(Chest chest){
		return Chests.contains(chest);
	}
	
	public boolean emptyarray(){
		return Chests.isEmpty();
	}
	
	public Chest chest(Chest chest){
		int c = Chests.lastIndexOf(chest);
		return Chests.get(c);
	}
	
	public String toString(){
		String dExS = sign.toString()+Chests.toString()+owner;
		return dExS;
	}
	
	public boolean isOwner(String name){
		return name.equals(owner);
	}
}

/*******************************************************************************\
* dExchange v1.x                                                                *
* Copyright (C) 2011-2012 Visual Illusions Entertainment                        *
* @author darkdiplomat <darkdiplomat@visualillusionsent.net>                    *
*                                                                               *
* This file is part of dExchange.                                               *                       
*                                                                               *
* This program is free software: you can redistribute it and/or modify          *
* it under the terms of the GNU General Public License as published by          *
* the Free Software Foundation, either version 3 of the License, or             *
* (at your option) any later version.                                           *
*                                                                               *
* This program is distributed in the hope that it will be useful,               *
* but WITHOUT ANY WARRANTY; without even the implied warranty of                *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                 *
* GNU General Public License for more details.                                  *
*                                                                               *
* You should have received a copy of the GNU General Public License             *
* along with this program.  If not, see http://www.gnu.org/licenses/gpl.html.   *
\*******************************************************************************/
