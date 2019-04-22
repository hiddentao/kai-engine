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

package com.hiddentao.kai.renderer;

import com.hiddentao.kai.renderer.software.SoftwareRenderSystem;
import com.hiddentao.kai.utils.HasStaticData;
import com.hiddentao.kai.utils.StaticDataManager;



/**
 * Class which manages the different types of rendering systems available 
 * to the application.
 */
public class RenderingSystemManager implements HasStaticData
{
	private static RenderingSystemManager iInstance = null;
	
	private SoftwareRenderSystem iSoftwareRenderSystem = new SoftwareRenderSystem();
	
	
	
	private RenderingSystemManager()
	{
		StaticDataManager.register(this);
	}
	
	
	public void resetStaticData()
	{
		iInstance = null;
	}	
	
	
	
	/**
	 * Get singleton instance of this class.
	 * @return a non-null instance.
	 */
	public static RenderingSystemManager instance()
	{
		if (null == iInstance)
		{
			iInstance = new RenderingSystemManager(); 
		}
		return iInstance;
	}
	
	
	/**
	 * Get the default rendering system for use by the application.
	 * @return a non-null rendering system.
	 */
	public RenderingSystem getDefaultRenderSystem()
	{
		return iSoftwareRenderSystem;
	}


}



