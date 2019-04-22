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

package com.hiddentao.kai.nodes;

import java.awt.Color;
import java.util.ArrayList;

import com.hiddentao.kai.geometry.Triangle3D;
import com.hiddentao.kai.geometry.Vertex3D;
import com.hiddentao.kai.logging.Logger;
import com.hiddentao.utils.collections.DynamicArray;
import com.hiddentao.utils.collections.NonMutableIterator;


/**
 * Represents a 3D mesh.
 */
public final class Mesh extends Node
{
	private final static Logger LOG = Logger.getLogger(Mesh.class.getName());
	
	
	private ArrayList<Vertex3D> iVertices = new ArrayList<Vertex3D>();
	private ArrayList<Triangle3D> iPolygons = new ArrayList<Triangle3D>();
	
	
	/**
	 * Add a vertex to this mesh.
	 * @param aVertex a non-null vertex.
	 * @return the vertex's index in this mesh; or -1 if vertex didn't get 
	 * added to the internal list.
	 * @see #addPolygon(int, int, int)
	 */
	private int addVertex(Vertex3D aVertex)
	{
		if (null != aVertex && !iVertices.contains(aVertex))
		{
			if (iVertices.add(aVertex))
			{
				return iVertices.size()-1;
			}
		}
		
		return -1;
	}
	

	/**
	 * Add polygons to this mesh.
	 * 
	 * The {@link Triangle3D}s' vertices also get added to the vertex list 
	 * unless they're already present.
	 * 
	 * @param aPolygons the polygons to add (must be non-null)
	 * 
	 * @return the polygon's index in this mesh; or -1 if it didn't get added 
	 * to the internal list.
	 */
	public void addPolygons(DynamicArray<Triangle3D> aPolygons)
	{
		for (Triangle3D tri : aPolygons)
		{
			addPolygon(tri);
		}
	}
	
	
	
	/**
	 * Add a polygon to this mesh.
	 * 
	 * The {@link Triangle3D}'s vertices also get added to the vertex list 
	 * unless they're already present.
	 * 
	 * @param aPolygon the polygon to add (must be non-null)
	 * 
	 * @return the polygon's index in this mesh; or -1 if it didn't get added 
	 * to the internal list.
	 */
	public int addPolygon(Triangle3D aPolygon)
	{
		if (null != aPolygon)
		{
			for (int i=0; i<aPolygon.vertices.length; ++i)
			{
				addVertex(aPolygon.vertices[i]);
			}
			
			if (iPolygons.add(aPolygon))
			{
				return iPolygons.size()-1;
			}
		}
		
		return -1;
	}	
	
	
	
	/**
	 * Apply a colour to all the polygons in this mesh. 
	 * @param aColor a non-null colour.
	 */
	public void setColor(Color aColor)
	{
		for (Triangle3D tri : iPolygons)
		{
			tri.color = aColor;
		}
	}
	
	
	
	
	/**
	 * Get an iterator over this mesh's polygons.
	 * @return a non-null iterator.
	 */
	public NonMutableIterator<Triangle3D> polygonIterator()
	{
		return new NonMutableIterator<Triangle3D>(iPolygons.iterator());
	}
	
	
	
    /**
     * Calculate and save normal vectors for all vertices of this mesh.
     */
    public void calculateVertexNormals()
    {
        LOG.trace("Clearing vertex normals...");
    	
        for (Vertex3D v : iVertices)
        {
        	v.normal.setZero();
        }
        
        LOG.trace("Calculating polygon and vertex normals...");
        
        for (Triangle3D tri : iPolygons)
        {
        	// calculate normal for the this polygon
        	tri.calculateSurfaceNormal();
        	// now apply to all the vertices
        	tri.vertices[0].normal.plusEq(tri.normal);
        	tri.vertices[1].normal.plusEq(tri.normal);
        	tri.vertices[2].normal.plusEq(tri.normal);
        }
        
        LOG.trace("Normalising vertex normals...");
    	
        for (Vertex3D v : iVertices)
        {
        	v.normal.normalise();
        }
        
    }

	
	
	@Override
	protected void doVisitDown(NodeVisitor visitor)
	{
		visitor.process(this);
	}


	@Override
	protected void doVisitUp(NodeVisitor visitor)
	{
		visitor.process(this);
	}

}



