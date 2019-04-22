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
 * A translation in 3D space.
 * 
 * This object has a 'scaling factor' which provides an extra 
 * degree of control over the translation vector. When a {@link NodeVisitor} 
 * visits this object it calls back to 
 * {@link NodeVisitor#processTranslation(float, float, float)} with each 
 * component of the translation vector multiplied by the scaling 
 * factor. The default scaling factor is 1.0.
 */
public class Translation extends Node implements VectorComponents
{
	public Vec4 vector = new Vec4();

	private float iScalingFactor = 1.0f; 
	
	
	/**
	 * Constructor.
	 * 
	 * Initialises this to be a zero translation vector.
	 */
	public Translation()
	{
		this(new Vec4(0,0,0,0));
	}

	
	/**
	 * Constructor.
	 * 
	 * @param aVec the value to initialise {@link #vector} with.
	 */
	public Translation(Vec4 aVec)
	{
		vector.set(aVec);
	}
	
	
	/**
	 * Get the scaling factor of this translation vector.
	 * @return the scaling factor.
	 */
	public float getScaleFactor()
	{
		return iScalingFactor;
	}
	
	
	/**
	 * Get the scaling factor of this translation vector.
	 * @param aScalingFactor the scaling factor.
	 */
	public void setScaleFactor(float aScalingFactor)
	{
		iScalingFactor = aScalingFactor;
	}
	
	
	
	@Override
	protected void doVisitDown(NodeVisitor visitor)
	{
		visitor.processTranslation(
				vector.val[_X_] * iScalingFactor, 
				vector.val[_Y_] * iScalingFactor, 
				vector.val[_Z_] * iScalingFactor 
				);

	}

	@Override
	protected void doVisitUp(NodeVisitor visitor)
	{
		visitor.processTranslation(
				-vector.val[_X_] * iScalingFactor, 
				-vector.val[_Y_] * iScalingFactor, 
				-vector.val[_Z_] * iScalingFactor 
				);
	}

}



