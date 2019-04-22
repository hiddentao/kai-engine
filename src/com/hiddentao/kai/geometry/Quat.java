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
 * Represents a quaternion.
 */
public final class Quat implements VectorComponents
{
	/**
	 * The real part.
	 */
	public float iReal = 0;
	/**
	 * The imaginary (xi,yi,ji) part.
	 */
	public Vec4 iVec = new Vec4();
	
	
	// Temporary vectors for calculations
	private volatile Vec4 iTempVec1 = new Vec4();
	private volatile Vec4 iTempVec2 = new Vec4();
	
	
	
	
	/**
	 * Constructor - initialise this to default values.
	 */
	public Quat()
	{
		this(0, new Vec4(0,0,0,0));
	}
	
	
	/**
	 * Constructor.
	 * @param aReal the real part.
	 * @param aVec the imaginary part.
	 */
	public Quat(float aReal, Vec4 aVec)
	{
		set(aReal, aVec);
	}
	
	
	/**
	 * Constructor.
	 * @param aQuat the quaternion whose values to copy.
	 */
	public Quat(Quat aQuat)
	{
		set(aQuat);
	}	
	
	
	
	
	/**
	 * Constructor.
	 * @param aReal the real part.
	 * @param aVec the imaginary part.
	 */
	private Quat(double aReal, Vec4 aVec)
	{
		this((float)aReal, aVec);
	}	
	
	
	
	/**
	 * Set this quaternion's value.
	 * 
	 * @param aReal the real part.
	 * @param aVec the imaginary part.
	 * @return this.
	 */
	public Quat set(float aReal, Vec4 aVec)
	{
		this.iReal = aReal;
		this.iVec.set(aVec);
		return this;
	}
	
	
	
	/**
	 * Set this quaternion's value.
	 * @param aQ the quaternion whose values to copy.
	 * @return this.
	 */
	public Quat set(Quat aQ)
	{
		return set(aQ.iReal, aQ.iVec);
	}	
	
	
	
	
	/**
	 * Get the length of this quaternion.
	 * @return a value >= 0.
	 */
    public float getLength()
    {
        return (float)Math.sqrt(iReal*iReal + 
        		iVec.val[_X_]*iVec.val[_X_] + 
        		iVec.val[_Y_]*iVec.val[_Y_] + 
        		iVec.val[_Z_]*iVec.val[_Z_]
        );
    }
    
    
	/**
	 * Normalise this quaternion so that it becomes a unit quaternion whose 
	 * {@link #getLength()} equals 1. 
	 */
    public void setNormalised()
    {
        float len = getLength();
        if (0 == len)
        	return;
        iReal /= len;
        iVec.multEq(1/len);
    }

    
    /**
     * Calculate the conjugate of this quaternion.
     * 
     * @param aOrig the original quaternion. 
     * @param aResult will hold the result.
     */
    public static void calculateConjugate(Quat aOrig, Quat aResult)
    {
    	aResult.iReal = aOrig.iReal;
    	aResult.iVec.set(-aOrig.iVec.val[_X_], -aOrig.iVec.val[_Y_], -aOrig.iVec.val[_Z_], 0);
    }

    
    
    /**
     * Calculate the inverse of a quaternion.
     * 
     * @param aOrig the original quaternion. 
     * @param aResult will hold the result.
     */
    public static void calculateInverse(Quat aOrig, Quat aResult)
    {
    	calculateConjugate(aOrig, aResult);
        float len = aOrig.getLength();
        if (0.0 != len)
        {
        	aResult.multEq(1/len);
        }
    }	

    
    
	
	/**
	 * Add another quaternion to this one.
	 * @param aQuat the quaternion to add to this one.
	 */
	public void plusEq(Quat aQuat)
	{
		iReal += aQuat.iReal;
		iVec.plusEq(aQuat.iVec);
	}
	
	
	
	
	/**
	 * Subtract another quaternion from this one.
	 * 
	 * @param aQuat the quaternion to subtract from this one.
	 */
	public void minusEq(Quat aQuat)
	{
		iReal -= aQuat.iReal;
		iVec.minusEq(aQuat.iVec);
	}	
	
	
	
	
	/**
	 * Multiply this quaternion by a scalar value.
	 * 
	 * @param aVal the scalar value to multiply this quaternion by.
	 */
	public void multEq(float aVal)
	{
		iReal *= aVal;
		iVec.multEq(aVal);
	}
	
	
	
	
	
	/**
	 * Multiply this quaternion by another one.
	 * @param aQuat the quaternion to multiply with this one.
	 */
	public synchronized void multEq(Quat aQuat)
	{
		/*
		 * ret.qr = qr*other.qr + qv.dot(other.qv);
		 * ret.qv = other.qv*qr + qv*other.qr + qv.cross(other.qv);
		 */
		
		
		// real part
		final float newReal = iReal*aQuat.iReal + iVec.getDotProduct(aQuat.iVec);

		// imaginary part
		iTempVec2.set(aQuat.iVec);
		iTempVec2.multEq(iReal);
		
		iTempVec1.set(iVec);
		iTempVec1.multEq(aQuat.iReal);
		iTempVec2.plusEq(iTempVec1);
		
		Vec4.calculateCrossProduct(iVec, aQuat.iVec, iTempVec1);
		iTempVec2.plusEq(iTempVec1);
		
		
		this.iVec.set(iTempVec2);
		this.iReal = newReal;
	}	
	
    
    
    
	
    /**
     * Get a rotation quaternion for the given 3D Euler angles around the
     * global coordinate axis system.
     * 
     * Note:
     *  - x-axis is assumed to be (1,0,0)
     *  - y-axis is assumed to be (0,1,0)
     *  - z-axis is assumed to be (0,0,1)
     * 
     *  
     * @param aResult will hold the result.
     */
	public static void calculateEulerRotationQuat(Angles3D aAngles, Quat aResult)
	{
		Quat qx = new Quat( Math.cos(aAngles.iAngleX * 0.5), new Vec4((float)Math.sin(aAngles.iAngleX * 0.5),0,0,0) );
		Quat qy = new Quat( Math.cos(aAngles.iAngleY * 0.5), new Vec4(0,(float)Math.sin(aAngles.iAngleY * 0.5),0,0) );
		Quat qz = new Quat( Math.cos(aAngles.iAngleZ * 0.5), new Vec4(0,0,(float)Math.sin(aAngles.iAngleZ * 0.5),0) );
		
		/*
		 * q = qz * qy * qx
		 */
		qz.multEq(qy);
		qz.multEq(qx);
		
		aResult.set(qz);
	}
	
	
	
	
    /**
     * Get a rotation quaternion for the given 3D Euler angles around the
     * given axis vectors.
     *
     * @param u a unit vector representing the x-axis.
     * @param v a unit vector representing the y-axis.
     * @param w a unit vector representing the z-axis.
     *  
     * @param aResult will hold the result.
     */
	public static void calculateEulerRotationQuat(Angles3D aAngles, Vec4 u, Vec4 v, Vec4 w, Quat aResult)
	{
		Vec4 newU = new Vec4(u);
		newU.multEq((float)Math.sin(aAngles.iAngleX * 0.5));
		
		Vec4 newV = new Vec4(v);
		newV.multEq((float)Math.sin(aAngles.iAngleY * 0.5));

		Vec4 newW = new Vec4(w);
		newW.multEq((float)Math.sin(aAngles.iAngleZ * 0.5));

		Quat qx = new Quat( Math.cos(aAngles.iAngleX * 0.5), newU );
		Quat qy = new Quat( Math.cos(aAngles.iAngleY * 0.5), newV );
		Quat qz = new Quat( Math.cos(aAngles.iAngleZ * 0.5), newW );
		
		/*
		 * q = qz * qy * qx
		 */
		qz.multEq(qy);
		qz.multEq(qx);
		
		aResult.set(qz);
	}	
	
}



