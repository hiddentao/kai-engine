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

import com.hiddentao.kai.logging.Logger;


/**
 * A flat 3D triangle, with 3 {@link Vertex3D}s.
 */
public class Triangle3D
{
	private final static Logger LOG = Logger.getLogger(Triangle3D.class.getName());
	
	
	public Vertex3D[] vertices = new Vertex3D[3];
	public Vec4 normal = new Vec4();
	public Color color = null;
	
	
	/**
	 * Constructor.
	 * @param aVertex1 the first vertex (must be non-null).
	 * @param aVertex2 the second vertex (must be non-null).
	 * @param aVertex3 the third vertex (must be non-null).
	 */
	public Triangle3D(Vertex3D aVertex1, Vertex3D aVertex2, Vertex3D aVertex3)
	{
		vertices[0] = aVertex1;
		vertices[1] = aVertex2;
		vertices[2] = aVertex3;
	}
	
	
	/**
	 * Calculate this triangle's {@link #normal} vector.
	 */
	public void calculateSurfaceNormal()
	{
    	Vec4.calculateSurfaceNormal( 
    	        vertices[0].point, 
    	        vertices[1].point, 
    	        vertices[2].point,
    	        normal
    	        );
	}
	
}






