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

import java.awt.Color;



/**
 * A vertex of a 3D polygon.
 */
public final class Vertex3D implements VectorComponents
{
	public Vec4 point = new Vec4();
	public Vec4 normal = new Vec4();
	public Color color = Color.WHITE;
	
	
	/**
	 * Constructor.
	 * @param aPoint the non-null vertex coordinate.
	 * @param aNormal the non-null vertex normal vector.
	 */
	public Vertex3D(Vec4 aPoint, Vec4 aNormal)
	{
		set(aPoint, aNormal);
	}
	
	
	/**
	 * Constructor.
	 * @param aPoint the non-null vertex coordinate.
	 */
	public Vertex3D(Vec4 aPoint)
	{
		set(aPoint);
	}	
	
	
	/**
	 * Constructor - initalise to default values.
	 */
	public Vertex3D()
	{
		Vec4 zeroVec = new Vec4();
		set(zeroVec, zeroVec);
	}
	
	
	/**
	 * Set this vertex's value.
	 * @param aPoint the non-null vertex coordinate.
	 * @param aNormal the non-null vertex normal vector.
	 */
	public void set(Vec4 aPoint, Vec4 aNormal)
	{
		set(aPoint);
		this.normal.set(aNormal);
		this.normal.val[_W_] = 0;	// direction vector
	}
	
	
	
	/**
	 * Set this vertex's coordinate.
	 * @param aPoint the non-null vertex coordinate.
	 */
	public void set(Vec4 aPoint)
	{
		this.point.set(aPoint);
		this.point.val[_W_] = 1;	// point
	}	
	
	
	
	
	/**
	 * Set this vertex's value.
	 * @param aVertex the vertex whose value to copy.
	 */
	public void set(Vertex3D aVertex)
	{
		set(aVertex.point, aVertex.normal);
	}
	
	
	
	public String toString()
	{
		StringBuilder buf = new StringBuilder(64);
		buf.append("{vtx: p=");
		buf.append(point.toString());
		buf.append(", n=");
		buf.append(normal.toString());
		buf.append("}");
		return buf.toString();
	}
}




