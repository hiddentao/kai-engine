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


/**
 * To be implemented by any class which contains static data.
 * 
 * Classes which implement this should call 
 * {@link StaticDataManager#register(HasStaticData)} with a reference to one 
 * of their object instances so that their static data gets cleared up when 
 * {@link StaticDataManager#resetAllStaticData()} gets called later on.
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
public interface HasStaticData
{
	/**
	 * Reset all static data held by this class.
	 * 
	 * This gets called by {@link StaticDataManager} on all the objects which 
	 * have registered themselves with it.
	 */
	public void resetStaticData();
}
