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

import java.awt.event.KeyListener;



/**
 * Implemented by the application which is using the engine, this interface 
 * allows the engine to communicate with the application via {@link AppBridge}.  
 */
public interface App
{
	/**
	 * Repaint the application's display.
	 */
	public void refreshGui();
	
	
	/**
	 * Add a {@link KeyListener} to the appilcation's top-level window.
	 */
	public void addKeyListener(KeyListener aListener);
	
	
	/**
	 * Remove a {@link KeyListener} from the appilcation's top-level window.
	 */
	public void removeKeyListener(KeyListener aListener);
	
}

