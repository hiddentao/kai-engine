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
 * A 3-dimensional cartesian co-ordinate vector with the an 
 * additional w-component.
 */
public final class Vec4 implements VectorComponents
{
	/**
	 * x,y,z,w.
	 */
	public float[] val = new float[4];
	

	
	/**
	 * Constructor - initialises this to be a zero vector.
	 */
	public Vec4()
	{
		this(0,0,0,0);
	}
	
	
	
	/**
	 * Constructor.
	 * @param x the x-component value to set.
	 * @param y the y-component value to set.
	 * @param z the z-component value to set.
	 * @param w the w-component value to set.
	 */	
	public Vec4(float x, float y, float z, float w)
	{
		set(x,y,z,w);
	}
	
	
	
	/**
	 * Constructor.
	 * @param aVec the vector whose values to copy.
	 */	
	public Vec4(Vec4 aVec)
	{
		set(aVec);
	}	
	
	
	
	
	/**
	 * Set this vector to 0.
	 */
	public void setZero()
	{
		set(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	
	
	/**
	 * Set this vector's value.
	 * 
	 * @param x the new x value.
	 * @param y the new y value.
	 * @param z the new z value.
	 * @param w the new w value.
	 */
	public void set(float x, float y, float z, float w)
	{
		this.val[_X_] = x;
		this.val[_Y_] = y;
		this.val[_Z_] = z;
		this.val[_W_] = w;
	}
	

	
	/**
	 * Set this vector's value.
	 * 
	 * @param aVec the vector whose values to copy.
	 */
	public void set(Vec4 aVec)
	{
		set(aVec.val[_X_], aVec.val[_Y_], aVec.val[_Z_], aVec.val[_W_]);
	}	
	
	
	
	
	
	
	/**
	 * Get the length of this vector.
	 * 
	 * This doesn't use the w-component when calculating this value.
	 */
	public float getLength()
	{
		return (float)Math.sqrt(
				val[_X_]*val[_X_] + 
				val[_Y_]*val[_Y_] + 
				val[_Z_]*val[_Z_]
		);
	}
	
	
	
	
	/**
	 * Normalise this vector so that its length equals 1.
	 * 
	 * This doesn't use or modify the w-component.
	 */
	public void normalise()
	{
		float len = getLength();
		if (0 != len)
		{
			multEq(1/len);
		}
	}
	

	
	
	/**
	 * Invert this vector so that is facing in the opposite direction.
	 * 
	 * This does not modify the w-component.
	 */
	public void invert()
	{
		val[_X_] = -val[_X_];
		val[_Y_] = -val[_Y_];
		val[_Z_] = -val[_Z_];
	}

	

	
	/**
	 * Add another vector to this vector.
	 * 
	 * This modifies all the vector's components, including the w-component. 
	 * 
	 * @param aVec the vector to add to this one.
	 */
	public void plusEq(Vec4 aVec)
	{
		val[_X_] += aVec.val[_X_];
		val[_Y_] += aVec.val[_Y_];
		val[_Z_] += aVec.val[_Z_];
		val[_W_] += aVec.val[_W_];
	}	
	
	
	/**
	 * Get the sum of this vector and another vector.
	 * 
	 * @param aVec the vector to add to this one.
	 * @return a new non-null vector containing the result.
	 */
	public Vec4 plus(Vec4 aVec)
	{
		Vec4 ret = new Vec4(this);
		ret.plusEq(aVec);
		return ret;
	}		
	
	
	
	
	/**
	 * Subtract another vector from this vector.
	 * 
	 * This modifies all the vector's components, including the w-component. 
	 * 
	 * @param aVec the vector to subtract from this one.
	 */
	public void minusEq(Vec4 aVec)
	{
		val[_X_] -= aVec.val[_X_];
		val[_Y_] -= aVec.val[_Y_];
		val[_Z_] -= aVec.val[_Z_];
		val[_W_] -= aVec.val[_W_];
	}	
	
	
	
	/**
	 * Get the difference of this vector and another vector.
	 * 
	 * @param aVec the vector to subtract from this one.
	 * @return a new non-null vector containing the result.
	 */
	public Vec4 minus(Vec4 aVec)
	{
		Vec4 ret = new Vec4(this);
		ret.minusEq(aVec);
		return ret;
	}		
	
	
	
	/**
	 * Multiply this vector by a scalar value.
	 *
	 * This modifies all the vector's components, including the w-component. 
	 * 
	 * @param aVal the scalar value to multiply this vector by.
	 */
	public void multEq(float aVal)
	{
		val[_X_] *= aVal;
		val[_Y_] *= aVal;
		val[_Z_] *= aVal;
		val[_W_] *= aVal;
	}
	

	
	/**
	 * Get the result of multiplying this vector by a scalar value.
	 * 
	 * @param aVal the scalar value to multiply this vector by.
	 * @return a new non-null vector containing the result.
	 */
	public Vec4 mult(float aVal)
	{
		Vec4 ret = new Vec4(this);
		ret.multEq(aVal);
		return ret;
	}		
	
	
	
	
	/**
	 * Get the dot product of this and another vector.
	 * 
	 * The w-component is ignored in this calculation.
	 * 
	 * @param aVec the other vector.
	 */
	public float getDotProduct(Vec4 aVec)
	{
		return 	val[_X_] * aVec.val[_X_] 
		        + val[_Y_] * aVec.val[_Y_] 
		        + val[_Z_] * aVec.val[_Z_]; 
	}
	
	
	
	/**
	 * Calculate the cross product of two vectors.
	 * 
	 * The operation is performed in this order:  (aVec1) x (aVec2)
	 * 
	 * @param aVec1 the first vector.
	 * @param aVec2 the second vector.
	 * @param aResultVector will hold the result. Its w-component will be 0.
	 */
	public static void calculateCrossProduct(Vec4 aVec1, Vec4 aVec2, Vec4 aResultVector)
	{
		aResultVector.set(
				/* this.y*other.z - this.z*other.y */
				aVec1.val[_Y_]*aVec2.val[_Z_] - aVec1.val[_Z_]*aVec2.val[_Y_], 
				/* this.z*other.x - this.x*other.z */
				aVec1.val[_Z_]*aVec2.val[_X_] - aVec1.val[_X_]*aVec2.val[_Z_], 
				/* this.x*other.y - this.y*other.x */
				aVec1.val[_X_]*aVec2.val[_Y_] - aVec1.val[_Y_]*aVec2.val[_X_], 
				0
				);
	}	
	
	
	
	
	
	/**
	 * Calculate the surface normal for the plane represented by the 3 
	 * given points.
	 * 
	 * @param aVec1 the first point on the plane.
	 * @param aVec2 the second point on the plane.
	 * @param aVec3 the third point on the plane.
	 * @param aResultVector the vector to store the result in.
	 */
	public static void calculateSurfaceNormal(
			Vec4 aVec1, Vec4 aVec2, Vec4 aVec3, 
			Vec4 aResultVector)
	{
        // calculate normal for this polygon
		Vec4 edge1 = new Vec4(aVec2);
		edge1.minusEq(aVec1);
		
		Vec4 edge2 = new Vec4(aVec3);
		edge2.minusEq(aVec2);
		
		calculateCrossProduct(edge2, edge1, aResultVector);
		aResultVector.normalise();
	}
	
	
	
	
	public String toString()
	{
		StringBuilder buf = new StringBuilder(24);
		buf.append("[");
		buf.append(val[_X_]);		// x
		buf.append(", ");
		buf.append(val[_Y_]);		// y
		buf.append(", ");
		buf.append(val[_Z_]);		// z
		buf.append(", ");
		buf.append(val[_W_]);		// w
		buf.append("]");
		return buf.toString();
	}
	
}


