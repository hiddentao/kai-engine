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
import com.hiddentao.kai.geometry.Vec4;
import com.hiddentao.kai.geometry.VectorComponents;
import com.hiddentao.kai.geometry.Vertex3D;
import com.hiddentao.kai.nodes.Mesh;
import com.hiddentao.utils.collections.DynamicArray;



/**
 * Class used to build cube objects.
 */
public final class CubeBuilder implements VectorComponents
{
	private CubeBuilder() {}
	
	
	/**
	 * Build a cube.
	 * 
	 * @param aCentreOfCube the centre of the cube located in 3D space.
	 * @param aDepthWidthHeight the depth/width/height of the cube. Must not 
	 * be 0 or negative.
	 * 
	 * @return a non-null array of {@link Triangle3D}s representing the cube.
	 */
	public static DynamicArray<Triangle3D> buildPolygons(Vec4 aCentreOfCube, float aDepthWidthHeight)
	{
		float aDepthWidthHeight_div_2 = aDepthWidthHeight * 0.5f;
		
		// get vector pointing to top-left-front corner
		Vec4 vec = new Vec4(aCentreOfCube);
		vec.val[_X_] -= aDepthWidthHeight_div_2;
		vec.val[_Y_] += aDepthWidthHeight_div_2;
		vec.val[_Z_] += aDepthWidthHeight_div_2;

		/* construct vertices */
		
		DynamicArray<Vertex3D> vertices = new DynamicArray<Vertex3D>(8);
		for (int i=0; i<2; ++i)
		{
			// top-left-front corner
			vertices.add(new Vertex3D(vec));
			// top-right-front corner
			vec.val[_X_] += aDepthWidthHeight;
			vertices.add(new Vertex3D(vec));
			// bottom-right-front corner
			vec.val[_Y_] -= aDepthWidthHeight;
			vertices.add(new Vertex3D(vec));
			// bottom-left-front corner
			vec.val[_X_] -= aDepthWidthHeight;
			vertices.add(new Vertex3D(vec));

			// back to top-left-front, and shifting to back face
			vec.val[_Y_] += aDepthWidthHeight;
			vec.val[_Z_] -= aDepthWidthHeight;
		}

		
		/* construct faces */

		DynamicArray<Triangle3D> faces = new DynamicArray<Triangle3D>(
				FaceBuilder.getExpectedNumberOfTriangles(vertices.size()), 2
				);
		
		// front
		faces.addAll(FaceBuilder.buildPolygons(
				vertices.get(0),
				vertices.get(1),
				vertices.get(2),
				vertices.get(3))
				);
		// right
		faces.addAll(FaceBuilder.buildPolygons(
				vertices.get(1),
				vertices.get(5),
				vertices.get(6),
				vertices.get(2))
				);
		// left
		faces.addAll(FaceBuilder.buildPolygons(
				vertices.get(4),
				vertices.get(0),
				vertices.get(3),
				vertices.get(7))
				);
		// top
		faces.addAll(FaceBuilder.buildPolygons(
				vertices.get(4),
				vertices.get(5),
				vertices.get(1),
				vertices.get(0))
				);
		// bottom
		faces.addAll(FaceBuilder.buildPolygons(
				vertices.get(3),
				vertices.get(2),
				vertices.get(6),
				vertices.get(7))
				);
		// back
		faces.addAll(FaceBuilder.buildPolygons(
				vertices.get(5),
				vertices.get(4),
				vertices.get(7),
				vertices.get(6))
				);

		
		return faces;
	}
	
	
	/**
	 * Build a cube.
	 * @param aCentreOfCube the centre of the cube located in 3D space.
	 * @param aDepthWidthHeight the depth/width/height of the cube. Must not 
	 * be 0 or negative.
	 * @return a non-null {@link Mesh} representing the cube.
	 */
	public static Mesh buildMesh(Vec4 aCentreOfCube, float aDepthWidthHeight)
	{
		Mesh cubeMesh = new Mesh();
		
		DynamicArray<Triangle3D> faces = buildPolygons(aCentreOfCube, aDepthWidthHeight);

		for (Triangle3D tri : faces)
		{
			cubeMesh.addPolygon(tri);
		}
		
		// calculate vertex normals
		cubeMesh.calculateVertexNormals();
		
		/* done */
		return cubeMesh;
	}
	

}


