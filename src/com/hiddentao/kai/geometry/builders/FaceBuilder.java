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

package com.hiddentao.kai.geometry.builders;


import com.hiddentao.kai.geometry.Triangle3D;
import com.hiddentao.kai.geometry.Vertex3D;
import com.hiddentao.kai.logging.Logger;
import com.hiddentao.kai.nodes.Mesh;
import com.hiddentao.utils.collections.DynamicArray;


/**
 * Class used to build a flat face consisting of {@link Triangle3D}s.
 */
public final class FaceBuilder
{
	private final static Logger LOG = Logger.getLogger(FaceBuilder.class.getName());
	
	private FaceBuilder() {}

	
	
	
	/**
	 * Build a 3D face consisting of {@link Triangle3D}s.
	 * 
	 * @param aVertices a list of {@link Vertex3D}s which make up the 
	 * face, in clockwise order from top-left as when viewing the face from 
	 * the front.
	 * 
	 * @return a non-null array of {@link Triangle3D}s representing the face.
	 * 
	 * @throws IllegalArgumentException if less than 3 vertices are supplied.
	 */
	public static DynamicArray<Triangle3D> buildPolygons(
			Vertex3D... aVertices
			)
	{
		if (null == aVertices || 2 >= aVertices.length)
		{
			throw new IllegalArgumentException("At least 3 vertices need to be supplied");
		}
		
		final int MAX_TRIANGLES = getExpectedNumberOfTriangles(aVertices.length); 
		
		DynamicArray<Triangle3D> tris = new DynamicArray<Triangle3D>(MAX_TRIANGLES);

		// create all triangles except very last one
		int i = -1;
		while (MAX_TRIANGLES-1 > ++i)
		{
			int ix2 = i * 2;
			tris.add(
					new Triangle3D(aVertices[ix2], aVertices[ix2+1], 
											aVertices[ix2+2])
			);
		}
		
		// add last one (links back to first vertex)
		tris.add(
				new Triangle3D(aVertices[aVertices.length-2], aVertices[aVertices.length-1], 
										aVertices[0])
		);
		
		return tris;
	}
	
	
	
	/**
	 * Build a 3D face consisting of {@link Triangle3D}s.
	 * 
	 * @param aVertices a list of {@link Vertex3D}s which make up the 
	 * face, in clockwise order from top-left as when viewing the face from 
	 * the front.
	 * 
	 * @return a mesh representing the face.
	 */
	public static Mesh buildMesh(Vertex3D... aVertices)
	{
		Mesh mesh = new Mesh();
		
		DynamicArray<Triangle3D> faces = buildPolygons(aVertices);

		for (Triangle3D tri : faces)
		{
			mesh.addPolygon(tri);
		}
		
		// calculate normals
		mesh.calculateVertexNormals();
		
		/* done */
		return mesh;
	}	
	
	
	
	
	/**
	 * Get the number of {@link Triangle3D}s you would expect to create for 
	 * a given number of vertices.
	 * @param aNumVertices the number of vertices.
	 * @return an integer value.
	 */
	public static int getExpectedNumberOfTriangles(int aNumVertices)
	{
		return aNumVertices - 2;
	}
}


