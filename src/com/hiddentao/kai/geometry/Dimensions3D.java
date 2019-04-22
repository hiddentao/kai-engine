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



public class Dimensions3D
{
	public int width = 0;
	public int height = 0;
	public int depth = 0;
	
	public Dimensions3D(int aWidth, int aHeight, int aDepth)
	{
		width = aWidth;
		height = aHeight;
		depth = aDepth;
	}
	
	
	public String toString()
	{
		StringBuilder buf = new StringBuilder(36);
		buf.append("[dim: ");
		buf.append("w=");
		buf.append(width);
		buf.append(", ");
		buf.append("h=");
		buf.append(height);
		buf.append(", ");
		buf.append("d=");
		buf.append(depth);
		buf.append("]");
		return buf.toString();		
	}


	@Override
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof Dimensions3D) )
		{
			throw new ClassCastException("obj must be of type: " + Dimensions3D.class.getName());
		}
		
		Dimensions3D other = (Dimensions3D)obj;
		
		return (	this.width == other.width &&
					this.height == other.height &&
					this.depth == other.depth
				);
	}
	
	
	
}

