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

package com.hiddentao.kai.geometry;


/**
 * Represents 2-dimensional dimensions.
 */
public class Dimensions2D
{
	public int width = 0;
	public int height = 0;
	public float aspectRatio = 1.0f;
	
	
	/**
	 * Constructor - initialises dimensions to 0.
	 */
	public Dimensions2D()
	{
		this(0,0);
	}
	
	/**
	 * Constructor.
	 * @param aWidth the width to set.
	 * @param aHeight the height to set.
	 */
	public Dimensions2D(int aWidth, int aHeight)
	{
		set(aWidth, aHeight);
	}	
	
	
	
	/**
	 * Set this object's value.
	 * @param aDim the object whose values to copy.
	 */
	public void set(Dimensions2D aDim)
	{
		if (null != aDim)
			set(aDim.width, aDim.height);
	}
	
	
	/**
	 * Set this object's value.
	 * @param aWidth the width to set.
	 * @param aHeight the height to set.
	 */
	public void set(int aWidth, int aHeight)
	{
		this.width = aWidth;
		this.height = aHeight;
		this.aspectRatio = (0 == this.height ? 0 : this.width / this.height);
	}
	
}


