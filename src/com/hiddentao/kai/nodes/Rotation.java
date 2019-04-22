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

import com.hiddentao.kai.geometry.Angles3D;
import com.hiddentao.kai.geometry.VectorComponents;


/**
 * A rotation in 3D space.
 */
public class Rotation extends Node implements VectorComponents
{
	public Angles3D angles = new Angles3D();
	
	
	/**
	 * Constructor.
	 * 
	 * Initialises this to be a zero rotation vector.
	 */
	public Rotation()
	{
		this(new Angles3D(0,0,0));
	}

	
	/**
	 * Constructor.
	 * 
	 * @param aAngles the angles to initialise {@link #angles} with.
	 */
	public Rotation(Angles3D aAngles)
	{
		angles.set(aAngles.iAngleX, aAngles.iAngleY, aAngles.iAngleZ);
	}
	
	
	
	
	@Override
	protected void doVisitDown(NodeVisitor visitor)
	{
		visitor.processRotation(
				angles.iAngleX,
				angles.iAngleY,
				angles.iAngleZ
				);

	}

	@Override
	protected void doVisitUp(NodeVisitor visitor)
	{
		visitor.processRotation(
				-angles.iAngleX,
				-angles.iAngleY,
				-angles.iAngleZ
				);
	}

}



