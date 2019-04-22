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
import com.hiddentao.kai.geometry.VectorComponents;


/**
 * A directional light.
 *
 * This object projects a light in a given direction.
 */
public class DirectionalLight extends Node implements VectorComponents
{
	public Vec4 vector = new Vec4();

	/**
	 * Constructor.
	 * 
	 * Initialises this to be a zero translation vector.
	 */
	public DirectionalLight()
	{
		this(0,0,0);
	}

	
	/**
	 * Constructor.
	 * 
	 * @param aVec the value to initialise {@link #vector} with.
	 */
	public DirectionalLight(Vec4 aVec)
	{
		this(aVec.val[_X_], aVec.val[_Y_], aVec.val[_Z_]);
	}
	
	
	
	/**
	 * Constructor.
	 * 
	 * @param x the x-component value to set.
	 * @param y the y-component value to set.
	 * @param z the z-component value to set. 
	 */
	public DirectionalLight(float x, float y, float z)
	{
		vector.set(x,y,z,0);
	}
	

	
	@Override
	protected void doVisitDown(NodeVisitor visitor)
	{
		visitor.processDirectionalLight(vector);
	}

	@Override
	protected void doVisitUp(NodeVisitor visitor)
	{
		visitor.processDirectionalLight(vector);
	}

}



