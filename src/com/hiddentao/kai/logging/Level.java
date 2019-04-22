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

package com.hiddentao.kai.logging;


/**
 * The different logging levels/priorities.
 */
public enum Level
{
	TRACE(0, "TRACE"),
	DEBUG(1,"DEBUG"),
	INFO(2, "INFO"),
	WARN(3, "WARN"),
	ERROR(4, "ERROR"),
	FATAL(5, "FATAL");
	
	
	private int iIntValue = -1;
	private String iDescription = null;
	
	
	/**
	 * Constructor.
	 * @param aIntValue the integer value of the level.
	 * @param aDescription the textual description of the level.
	 */
	Level(int aIntValue, String aDescription)
	{
		iIntValue = aIntValue;
		iDescription = aDescription;
	}
	
	
	/**
	 * Get the integer value of this level.
	 * @return a non-negative integer.
	 */
	public int intValue()
	{
		return iIntValue;
	}
	
	
	public String toString()
	{
		return iDescription;
	}
}


