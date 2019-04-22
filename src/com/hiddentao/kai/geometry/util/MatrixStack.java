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

package com.hiddentao.kai.geometry.util;

import com.hiddentao.kai.geometry.Mat4;
import com.hiddentao.kai.utils.HasStaticDataImpl;
import com.hiddentao.utils.collections.DynamicArray;


/**
 * A LIFO stack for holding matrices.
 * 
 * TODO: can we implement the stack in a more memory-efficient way? 
 */
public final class MatrixStack extends HasStaticDataImpl
{
	private static MatrixStack iInstance = null;
	
	/** The stack of matrices */
	private DynamicArray<Mat4> iStack = new DynamicArray<Mat4>(8,4);
	
	
	private MatrixStack() {}
	
	public static MatrixStack getInstance()
	{
		if (null == iInstance)
		{
			iInstance = new MatrixStack();
		}
		return iInstance;
	}
	
	public void resetStaticData()
	{
		iInstance = null;
	}
	
	
	/**
	 * Push a matrix onto the stack.
	 * @param aMatrix a non-null matrix.
	 */
	public void push(Mat4 aMatrix)
	{
		iStack.add(new Mat4(aMatrix));
	}

	
	/**
	 * Pop the top matrix off the stack.
	 * 
	 * @return the non-null matrix removed from the top of the stack if 
	 * successful.
	 * 
	 * @throws MatrixStackException if there are no items to pop.
	 */
	public Mat4 pop() throws MatrixStackException 
	{
		if (0 >= iStack.size())
		{
			throw new MatrixStackException("There are no items available to pop.");
		}
		
		return iStack.remove(iStack.size()-1);
	}
	
	
	
	/**
	 * Pop the top matrix off the stack.
	 * 
	 * @param aMatrix the non-null matrix which will get overwritten with the 
	 * matrix from the top of the stack.
	 * 
	 * @throws MatrixStackException if there are no items to pop.
	 */
	public void pop(Mat4 aMatrix) throws MatrixStackException 
	{
		aMatrix.set(pop());
	}	
	
	
	

	/**
	 * Exception thrown when using the {@link MatrixStack} class.
	 */
	public class MatrixStackException extends Exception
	{
		public MatrixStackException(String message)
		{
			super(message);
		}
	}
}


