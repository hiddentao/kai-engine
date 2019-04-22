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
 * A 2-dimensional rectangle defined by two opposing corner points.
 */
public final class Rect2D
{
	/** The corner point */
	public Vec2 point1 = new Vec2();
	
	/** The opposing corner point to {@link #point1} */
	public Vec2 point2 = new Vec2();
	
	
	/**
	 * Constructor.
	 * @param aPoint1 the first corner point.
	 * @param aPoint2 the opposing corner point to the first one.
	 */
	public Rect2D(Vec2 aPoint1, Vec2 aPoint2)
	{
		point1 = aPoint1;
		point2 = aPoint2;
	}
	
	
	/**
	 * Get the width of this rectangle.
	 * @return a value >= 0.
	 */
	public float width()
	{
		return Math.abs(point1.x - point2.x);
	}

	
	
	/**
	 * Get the height of this rectangle.
	 * @return a value >= 0.
	 */
	public float height()
	{
		return Math.abs(point1.y - point2.y);
	}
	
	
	/**
	 * Ensure that {@link #point1} is the top-left point and {@link #point2} 
	 * is the bottom-right point. 
	 */
	public void setNormalisedPointOrder()
	{
		float minY = Math.min(point1.y, point2.y);
		float maxY = Math.max(point1.y, point2.y);
		float minX = Math.min(point1.x, point2.x);
		float maxX = Math.max(point1.x, point2.x);
		
		point1 = new Vec2(minX, minY);
		point2 = new Vec2(maxX, maxY);
	}
	
}

