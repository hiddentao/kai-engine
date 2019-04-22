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

import com.hiddentao.kai.geometry.Vec4;


/**
 * Represents an object which 'visits' {@link Node}s in a scenegraph.
 */
public abstract class NodeVisitor
{
	/**
	 * Visit a scenegraph, moving from the given node back up to the root. 
	 * @param aNode the node to visit first.
	 */
	public void visitUp(Node aNode)
	{
		if (null != aNode)
		{
			aNode.visitUp(this);
			
			if (null != aNode.parent())
				visitUp(aNode.parent());
		}
	}
	
	
	/**
	 * Visit a scenegraph, moving from root down to the given node. 
	 * @param aNode the node to visit last.
	 */
	public void visitDown(Node aNode)
	{
		if (null != aNode)
		{
			if (null != aNode.parent())
				visitDown(aNode.parent());
			
			aNode.visitDown(this);
		}
	}
	
	
	/**
	 * Process the given {@link Node}. 
	 */
	public abstract void process(Node aNode);
	/**
	 * Process the given {@link Mesh}. 
	 */
	public abstract void process(Mesh aMesh);
	/**
	 * Process a 3D translation. 
	 * @param aX the amount by which to translate along the x-axis.
	 * @param aY the amount by which to translate along the y-axis.
	 * @param aZ the amount by which to translate along the z-axis.
	 */
	public abstract void processTranslation(float aX, float aY, float aZ);
	/**
	 * Process a 3D rotation. 
	 * @param aX the amount by which to rotate around the x-axis.
	 * @param aY the amount by which to rotate around the y-axis.
	 * @param aZ the amount by which to rotate around the z-axis.
	 */
	public abstract void processRotation(float aX, float aY, float aZ);		
	/**
	 * Process a directional light.
	 * @param aVec the direction vector of the light. 
	 */
	public abstract void processDirectionalLight(Vec4 aVec);		
}


