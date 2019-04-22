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
 * A three-dimensional plane. A plane is defined by a point and a normal.
 */
public final class Plane
{
	public Vec4 point;
	public Vec4 normal;
	
	
	/**
	 * Constructor.
	 * @param aPoint a point on the plane.
	 * @param aNormal the normal to the plane.
	 */
	public Plane(Vec4 aPoint, Vec4 aNormal)
	{
		set(aPoint, aNormal);
	}
	
	
	/**
	 * Set this plane's point and normal.
	 * @param aPoint a point on the plane.
	 * @param aNormal the normal to the plane.
	 */
	public void set(Vec4 aPoint, Vec4 aNormal)
	{
		point.set(aPoint);
		normal.set(aNormal);
	}
}


