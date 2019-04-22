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
 * A 2-dimensional cartesian co-ordinate vector.
 */
public final class Vec2
{
	public float x = 0.0f;
	public float y = 0.0f;
	
	
	/**
	 * Constructor - initialises this to be a zero vector.
	 */
	public Vec2()
	{
		this(0,0);
	}
	
	/**
	 * Constructor.
	 * @param x the x-component value to set.
	 * @param y the y-component value to set.
	 */
	public Vec2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * Set this vector to 0.
	 */
	public void setZero()
	{
		x = 0.0f;
		y = 0.0f;
	}
	
	
	/**
	 * Set this vector's value.
	 * @param x the new x value.
	 * @param y the new y value.
	 * @param z the new z value.
	 */
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	

	
	
	/**
	 * Get the length of this vector.
	 * @return a value >= 0
	 */
	public float length()
	{
		return (float)(Math.sqrt(x*x + y*y));
	}
	
	
	/**
	 * Normalise this vector so that it becomes a unit vector whose 
	 * {@link #length()} equals 1. 
	 */
	public void setNormalised()
	{
		float len = length();
		if (0 < len)
		{
			x /= len;
			y /= len;
		}
	}
	
	
	
	/**
	 * Invert this vector so that is facing in the opposite direction.
	 */
	public void setInverted()
	{
		x = -x;
		y = -y;
	}
	
	
	
	/**
	 * Get the sum of this vector and another vector.
	 * @param aVec the vector to add to this one.
	 * @return a non-null vector.
	 */
	public Vec2 plus(Vec2 aVec)
	{
		return new Vec2(x + aVec.x, y + aVec.y);
	}
	
	
	
	
	/**
	 * Get the different of this vector and another vector.
	 * @param aVec the vector to subtract from this one.
	 * @return a non-null vector.
	 */
	public Vec2 minus(Vec2 aVec)
	{
		return new Vec2(x - aVec.x, y - aVec.y);
	}	
	
	
	
	
	/**
	 * Get the result of multiplying this vector by a scalar value.
	 * @param aVal the scalar value to multiply this vector by.
	 * @return a non-null vector.
	 */
	public Vec2 mult(float aVal)
	{
		return new Vec2(x * aVal, y * aVal);
	}
	
	

	
}


