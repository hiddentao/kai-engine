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

package com.hiddentao.kai.geometry.util;

import java.awt.Color;
import java.util.Random;


/**
 * Utilities for dealing with {@link Color}s.
 */
public final class ColorUtils
{
	private ColorUtils() {}
	
	
	/**
	 * Generate a random bright colour.
	 * @return a non-null colour.
	 */
	public static Color getRandomBrightColor()
	{
		Random r = new Random();
		return new Color(140 + r.nextInt(115), 140 + r.nextInt(115), 140 + r.nextInt(115));
	}
}



