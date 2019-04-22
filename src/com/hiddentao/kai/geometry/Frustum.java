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

import com.hiddentao.kai.logging.Logger;



/**
 * Represents a rectangular viewing frustum.
 */
public final class Frustum
{
	private static Logger LOG = Logger.getLogger(Frustum.class.getName());
	
	public float left = 0;
	public float right = 0;
	public float top = 0;
	public float bottom = 0;
	public float near = 0;
	public float far = 0;
	
	/**
	 * The vertical field-of-view angle in degrees.
	 */
	public float verticalFOV = 0;
	
	
	/**
	 * Constructor - initialises this to some default values.
	 */
	public Frustum()
	{
		this(-1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -100.0f, 90);
	}
	
	
	
	
	
	/**
	 * Constructor.
	 * @param left the left edge plane x-coordinate.
	 * @param right the right edge plane x-coordinate.
	 * @param top the top edge plane y-coordinate.
	 * @param bottom the bottom edge plane y-coordinate.
	 * @param near the front edge plane z-coordinate.
	 * @param far the back edge plane z-coordinate.
	 * @param verticalFOV the vertical field-of-view angle in degrees.
	 */
	public Frustum(float left, float right, float top, float bottom, float near, float far, float verticalFOV)
	{
		set(left,right,top,bottom,near,far,verticalFOV);
	}
	
	
	
	
	/**
	 * Set this frustum's values.
	 * @param left the left edge plane x-coordinate.
	 * @param right the right edge plane x-coordinate.
	 * @param top the top edge plane y-coordinate.
	 * @param bottom the bottom edge plane y-coordinate.
	 * @param near the front edge plane z-coordinate.
	 * @param far the back edge plane z-coordinate.
	 * @param verticalFOV the vertical field-of-view angle in degrees.
	 */
	public void set(float left, float right, float top, float bottom, float near, float far, float verticalFOV)
	{
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.near = near;
		this.far = far;
		this.verticalFOV = verticalFOV;
	}
	
	
	/**
	 * Calculate this frustum's view plane depth based on its current values.
	 * @return the view plane depth.
	 */
	public float viewPlane()
	{
		/*
		 * tan (theta / 2) = (total frustum height / 2) / viewplane
		 */
		return -(top-bottom) * 0.5f / (float)Math.tan(verticalFOV*Math.PI/360);
	}
	
	
	


}





