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
 * A 4x4 matrix.
 */
public final class Mat4 implements VectorComponents
{
	/**
	 * The zero matrix.
	 */
	private static final float[][] ZERO =
	{
		{0, 0, 0, 0},
		{0, 0, 0, 0},
		{0, 0, 0, 0},
		{0, 0, 0, 0}
	};
	
	
	/**
	 * The identity matrix.
	 */
	private static final float[][] IDENTITY =
	{
		{1, 0, 0, 0},
		{0, 1, 0, 0},
		{0, 0, 1, 0},
		{0, 0, 0, 1}
	};
	
	
	
	/** The row-column component values of the matrix. */
	public float[][] val = new float[4][4];

	
	// For temporary calculations
	private volatile float[][] iTempVal = new float[4][4];
	
	
	
	/**
	 * Constructor - initialises this to be a zero matrix.
	 */
	public Mat4()
	{
		setZero();
	}
	
	
	
	/**
	 * Constructor.
	 * @param aMat the matrix whose values to copy.
	 */
	public Mat4(Mat4 aMat)
	{
		set(aMat);
	}	
	
	
	

	/**
	 * Set this matrix's component values.
	 * @param aMat the matrix whose values to copy.
	 * @return this.
	 */
	public Mat4 set(Mat4 aMat)
	{
		set(aMat.val);
		return this;
	}
	
	
	
	
	/**
	 * Set this matrix's component values.
	 * @param aArray the source array to copy from.
	 */
	private void set(float[][] aArray)
	{
		for (int row=0; row<4; ++row)
			System.arraycopy(aArray[row],0,this.val[row],0,4);
	}
	
	
	
	
	/**
	 * Set this matrix to be a zero matrix.
	 */
	public void setZero()
	{
		set(ZERO);
	}
	
	
	
	/**
	 * Set this matrix to be the identity matrix.
	 */
	public void setIdentity()
	{
		set(IDENTITY);
	}
	

	
	
	
	/**
	 * Add another matrix to this matrix.
	 * 
	 * @param aMat the matrix to add to this one.
	 */
	public void plusEq(Mat4 aMat)
	{
		for (int row=0; row<4; ++row)
		{
			for (int col=0; col<4; ++col)
			{
				this.val[row][col] += aMat.val[row][col];
			}
		}
	}	
	
	
	
    /**
     * Get the sum of this matrix and another matrix.
     * 
     * @param aMat the matrix to add to this one.
     * @return a non-null matrix containing the result.
     */
    public Mat4 plus(Mat4 aMat)
    {
        Mat4 mat = new Mat4(this);
        mat.plusEq( aMat );
        return mat;
    }   	
	
	
	
	
	/**
	 * Subtract another matrix from this one.
	 * 
	 * @param aMat the matrix to subtract from this one.
	 */
	public void minusEq(Mat4 aMat)
	{
		for (int row=0; row<4; ++row)
		{
			for (int col=0; col<4; ++col)
			{
				this.val[row][col] -= aMat.val[row][col];
			}
		}
	}	
	
	
	
    /**
     * Get the difference of this matrix and another matrix.
     * 
     * @param aMat the matrix to subtract from this one.
     * @return a non-null matrix containing the result.
     */
    public Mat4 minus(Mat4 aMat)
    {
        Mat4 mat = new Mat4(this);
        mat.minusEq( aMat );
        return mat;
    }   	
	
	
	
	
	/**
	 * Multiply this matrix by a scalar value.
	 * 
	 * @param aScalar the value to multiply by.
	 */
	public void multEq(float aScalar)
	{
		for (int row=0; row<4; ++row)
		{
			for (int col=0; col<4; ++col)
			{
				this.val[row][col] *= aScalar;
			}
		}
	}		
	
	
	
    /**
     * Get the result of multiplying this matrix by a scalar value.
     * 
     * @param aScalar the value to multiply by.
     * @return a non-null matrix containing the result.
     */
    public Mat4 mult(float aScalar)
    {
        Mat4 mat = new Mat4(this);
        mat.multEq( aScalar );
        return mat;
    }	
	
	
	
	/**
	 * Multiply this matrix by another matrix.
	 * 
	 * The multiplication order is:  (this matrix) * (aMat)
	 * 
	 * @param aMat the matrix to multiply with.
	 */
	public synchronized void multEq(Mat4 aMat)
	{
		for (int row=0; row<4; ++row)
		{
			for (int col=0; col<4; ++col)
			{
				float sum = 0.0f;
				
				for (int j=0; j<4; ++j)
				{
					sum += this.val[row][j] * aMat.val[j][col];
				}
				
				iTempVal[row][col] = sum;
			}
		}
		
		set(iTempVal);
	}	
	
	
	
    /**
     * Get the result of multiplying this matrix by another matrix.
     * 
     * The multiplication order is:  (this matrix) * (aMat)
     * 
     * @param aMat the matrix to multiply with.
     * 
     * @return a non-null matrix containing the result.
     */
    public Mat4 mult(Mat4 aMat)
    {
        Mat4 mat = new Mat4(this);
        mat.multEq( aMat );
        return mat;
    }   	

	
	
	/**
	 * Multiply a matrix by a given vector.
	 * 
	 * The multiplication order is:  (matrix) * (vector)
	 * 
	 * @param aMat the matrix to multiply with.
	 * @param aVec the vector to multiply with.
	 * @param aResult the vector to store the result in.
	 */
	public static void transformVector(Mat4 aMat, Vec4 aVec, Vec4 aResult)
	{
		for (int row=0; row<4; ++row)
		{
			for (int col=0; col<4; ++col)
			{
				aResult.val[row] = 
						aMat.val[row][0] * aVec.val[_X_] + 
						aMat.val[row][1] * aVec.val[_Y_] + 
						aMat.val[row][2] * aVec.val[_Z_] + 
						aMat.val[row][3] * aVec.val[_W_]
						;
			}
		}
	}
	
	

	
	public String toString()
	{
		StringBuilder buf = new StringBuilder(64);
		buf.append("\n[\t");
		for (int row=0; 4 > row; ++row)
		{
			for (int col=0; 4 > col; ++col)
			{
				buf.append(val[row][col]);
				if (3 > col)
				{
					buf.append(",");
					buf.append("\t");
				}
			}
			if (3 > row)
				buf.append("\n\t");
		}
		buf.append("\t]\n");
		return buf.toString();		
	}
}


