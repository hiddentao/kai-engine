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



/**
 * Structure to hold a set of 3D angles.
 */
public final class Angles3D
{
	/**
	 * Pitch.
	 */
	public float iAngleX = 0;
	/**
	 * Yaw.
	 */
	public float iAngleY = 0;
	/**
	 * Roll.
	 */
	public float iAngleZ = 0;
	
	
	
	/**
	 * Constructor - initialises angles to 0.
	 */
	public Angles3D()
	{
		set(0,0,0);
	}

	
	/**
	 * Constructor.
	 * @param angleX the yaw angle.
	 * @param angleY the pitch angle.
	 * @param angleZ the roll angle.
	 */
	public Angles3D(float angleX, float angleY, float angleZ)
	{
		set(angleX, angleY, angleZ);
	}
	
	
	
	/**
	 * Set the angle values.
	 * @param aAngleX the pitch angle.
	 * @param aAngleY the yaw angle.
	 * @param aAngleZ the roll angle.
	 */
	public void set(float aAngleX, float aAngleY, float aAngleZ)
	{
		iAngleX = aAngleX;
		iAngleY = aAngleY;
		iAngleZ = aAngleZ;		
	}
	
	
	
	/**
	 * Increase the X-axis (pitch) angle by the given amount.
	 * 
	 * @param aAmount the amount to increase by. If negative then angle 
	 * value will be decreased.
	 * @param aMaxValue the maximum allowable final value. The final angle will 
	 * be wrapped as necessary such that its value falls between 0 and this 
	 * parameter. 
	 */
	public void incX(float aAmount, float aMaxAngleValue)
	{
		iAngleX = (iAngleX + aAmount) % aMaxAngleValue;
		if (0 > iAngleX)
		{
			iAngleX += aMaxAngleValue;
		}
	}
	
	
	
	/**
	 * Increase the Y-axis (yaw) angle by the given amount.
	 * 
	 * @param aAmount the amount to increase by. If negative then angle 
	 * value will be decreased.
	 * @param aMaxValue the maximum allowable final value. The final angle will 
	 * be wrapped as necessary such that its value falls between 0 and this 
	 * parameter. 
	 */
	public void incY(float aAmount, float aMaxAngleValue)
	{
		iAngleY = (iAngleY + aAmount) % aMaxAngleValue;
		if (0 > iAngleY)
		{
			iAngleY += aMaxAngleValue;
		}
	}

	
	
	/**
	 * Increase the Z-axis (roll) angle by the given amount.
	 * 
	 * @param aAmount the amount to increase by. If negative then angle 
	 * value will be decreased.
	 * @param aMaxValue the maximum allowable final value. The final angle will 
	 * be wrapped as necessary such that its value falls between 0 and this 
	 * parameter. 
	 */
	public void incZ(float aAmount, float aMaxAngleValue)
	{
		iAngleZ = (iAngleZ + aAmount) % aMaxAngleValue;
		if (0 > iAngleZ)
		{
			iAngleZ += aMaxAngleValue;
		}
	}
	
	
	
	/**
	 * Multiply these angles by a given value.
	 * @param aVal the scalar value to multiply by.
	 * @return a non-null {@link Angles3D}.
	 */
	public Angles3D mult(float aVal)
	{
		return new Angles3D(iAngleX * aVal, iAngleY * aVal, iAngleZ * aVal);
	}
	
	
	
	
	public String toString()
	{
		StringBuilder buf = new StringBuilder(24);
		buf.append("{");
		buf.append("ø");
		buf.append(": ");
		buf.append(iAngleX);
		buf.append(", ");
		buf.append(iAngleY);
		buf.append(", ");
		buf.append(iAngleZ);
		buf.append("}");
		return buf.toString();		
	}
}



