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

package com.hiddentao.kai.app;

import com.hiddentao.kai.utils.HasStaticDataImpl;


/**
 * This object allows the the Kai engine to access properties of the 
 * application in which it is running. 
 * 
 * An application should register itself with this object as soon as possible.
 */
public final class AppBridge extends HasStaticDataImpl
{
	private static AppBridge iInstance = null;
	
	private App iApp = null;
	
	
	private AppBridge()
	{
	}

	private static AppBridge getInstance()
	{
		if (null == iInstance)
		{
			iInstance = new AppBridge();
		}
		return iInstance;
	}
	
	public void resetStaticData()
	{
		iInstance = null;
	}
	
	
	/**
	 * Set the active {@link App} object.
	 * @param aApp the {@link App} object to set.
	 */
	public static void setApp(App aApp)
	{
		getInstance().iApp = aApp;
	}

	/**
	 * Get the active {@link App} object.
	 * @return the active {@link App} object.
	 */
	public static App getApp()
	{
		return getInstance().iApp;
	}


}


