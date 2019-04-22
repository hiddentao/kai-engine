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

package com.hiddentao.kai.math;


/**
 * Mathematical constants.
 */
public interface MathConstants
{
	/**
	 * The maximum allowable angle value in degrees.
	 */
	public static float MAX_DEGREES = 360;
	/**
	 * The maximum allowable angle value in radians.
	 */
	public static float MAX_RADIANS = 2 * (float)Math.PI;
	/**
	 * Degrees-to-radians multipler.
	 */
	public static float DEG_TO_RAD = (float)Math.PI / 180;
	/**
	 * Radians-to-degrees multipler.
	 */
	public static float RAD_TO_DEG = 180 / (float)Math.PI;
}


