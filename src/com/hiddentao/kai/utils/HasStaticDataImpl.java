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
 * To be extended by any class which contains static data.
 * 
 * This acts as a helper base class for classes which wish to implement 
 * {@link HasStaticData}. This calls 
 * {@link StaticDataManager#register(HasStaticData)} in the constructor.
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
public abstract class HasStaticDataImpl implements HasStaticData
{
	/**
	 * Constructor.
	 * 
	 * Calls {@link StaticDataManager#register(HasStaticData)}. 
	 */
	protected HasStaticDataImpl()
	{
		StaticDataManager.register(this);
	}
}





