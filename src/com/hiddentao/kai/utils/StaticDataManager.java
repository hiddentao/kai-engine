/**
 * Copyright (C) 2010 Ramesh Nair (www.hiddentao.com)
 * 
 * This is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This is distributed in the hope that it will  be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hiddentao.kai.utils;

import java.util.HashSet;
import java.util.Set;



/**
 * Manages the unloading of static data within an application.
 * 
 * Background info:
 * 
 * If the application is actually a Java applet then it is wise to clear any 
 * static data during the shutdown process. This is because it is usually the 
 * case that refreshing an applet web page causes the applet to be restarted 
 * but using the same classloader as was used for the first instance of the 
 * applet. Thus it is highly likely that any static references that aren't 
 * manually reset end up pointing to invalid memory areas. 
 */
public final class StaticDataManager
{
	private static Set<HasStaticData> iStaticDataHolders = null;
	
	
	private StaticDataManager() {}
	
	
	/**
	 * Register an object with this class.
	 * 
	 * All registered objects are invoked when {@link #resetAllStaticData()} 
	 * gets called.
	 * 
	 * @param aObject the object to register. if null then is ignored.
	 */
	public static void register(HasStaticData aObject)
	{
		if (null != aObject)
		{
			if (null == iStaticDataHolders)
				iStaticDataHolders = new HashSet<HasStaticData>();
			
			iStaticDataHolders.add(aObject);
		}
	}
	
	
	/**
	 * Reset all static data held by this class and by all objects which 
	 * have registered themselves with this class.
	 */
	public static void resetAllStaticData()
	{
		for (HasStaticData obj : iStaticDataHolders)
		{
			obj.resetStaticData();
		}
		
		// remember reset my own data
		iStaticDataHolders = null;
	}
}

