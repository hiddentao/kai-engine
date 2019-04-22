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
 * This class provides methods to convert integers to their bit string 
 * representations.
 */
public final class BitPrinter
{
	private BitPrinter() {}
	
	
	/**
	 * Get the bit string representation of the given long integer.
	 * @return a non-null string.
	 */
	public static String getBitString(long aValue)
	{
		String ret = "";
		int count = 0;
		do
		{
			ret = ( (aValue & 0x1) == 1 ? "1" : "0") + ret;
			aValue >>= 1;
			if ( ++count % 4 == 0 && 64 > count && 0 != aValue)
				ret = " " + ret;
		} while (0 != aValue && 64 > count);
		
		return ret;
	}
	
}
