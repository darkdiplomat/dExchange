import java.util.ArrayList;

public class dExChest {
	private Chest chest;
	private String owner;
	private ArrayList<Sign> Signs;
	
	public dExChest(Chest chest, String owner){
		this.chest = chest;
		this.owner = owner;
		Signs = new ArrayList<Sign>();
	}
	
	public void addSign(Sign sign){
		Signs.add(sign);
	}
	
	public void removeSign(Sign sign){
		Signs.remove(sign);
	}
	
	public boolean containsSign(Sign sign){
		return Signs.contains(sign);
	}
	
	public boolean emptyarray(){
		return Signs.isEmpty();
	}
	
	public Sign sign(Sign sign){
		int s = Signs.lastIndexOf(sign);
		return Signs.get(s);
	}
	
	public String toString(){
		String dExC = chest.toString()+Signs.toString()+owner;
		return dExC;
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
