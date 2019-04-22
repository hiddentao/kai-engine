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

package com.hiddentao.kai.renderer.software;

import java.awt.Color;

import com.hiddentao.kai.geometry.Vec4;
import com.hiddentao.kai.geometry.VectorComponents;
import com.hiddentao.kai.logging.Logger;
import com.hiddentao.kai.utils.HasStaticData;
import com.hiddentao.kai.utils.StaticDataManager;



/**
 * Class responsible for drawing stuff onto the viewport.
 */
final class Rasteriser implements HasStaticData, VectorComponents
{
	private final static Logger LOG = Logger.getLogger(Rasteriser.class.getName());
	
	private static Rasteriser iInstance = null;
	
	private int iFrameNumber = 0;
	
	private Vec4 iTempVec1 = new Vec4();
	private Vec4 iTempVec2 = new Vec4();
	
	private float[][] iZBuffer = null;
	private int[][] iZBufferFrameNumber = null;
	
	private int iTempInt = -1;
	private float iTempFloat = -1;
	

	private Rasteriser()
	{
		StaticDataManager.register(this);
	}
	
	public static Rasteriser getInstance()
	{
		if (null == iInstance)
		{
			iInstance = new Rasteriser();
		}
		return iInstance;
	}
	
	
	
	/**
	 * Reset the rasteriser's internal state such that it's ready to draw 
	 * the next frame.
	 * 
	 * This method should be called at the start of each rendering iteration.
	 */
	public void resetForNextFrame(RasterSettings aSettings)
	{
		if (aSettings.iZBufferEnabled)
		{
			resetZBuffer(aSettings);
		}
		// inc. the frame number
		if (Integer.MAX_VALUE == ++iFrameNumber)
		{
			iFrameNumber = 1;
		}
	}
	
	
	
	/**
	 * Clear the Z-buffer.
	 */
	private void resetZBuffer(RasterSettings aSettings)
	{
		final int width = aSettings.iViewportDimensions.width;
		final int height = aSettings.iViewportDimensions.height;
		
		// Ensure that z-buffer has been initialised and that it is big 
		// enough to hold values for the whole viewport 
		if (null == iZBuffer || iZBuffer.length < width || 
				iZBuffer[0].length < height)
		{
			iZBuffer = new float[width][height];
			iZBufferFrameNumber = new int[width][height];
			iFrameNumber = 1;
		}
	}
	
	
	
	/**
	 * Draw a triangle using the Z-buffer. 
	 * 
	 * @param aTriangleColor the color of the triangle.
	 * @param aPoint1 the first point.
	 * @param aNormal1 the first point's normal.
	 * @param aColor1 the first point's color.
	 * @param aPoint2 the second point.
	 * @param aNormal2 the second point's normal.
	 * @param aColor2 the second point's color.
	 * @param aPoint3 the third point.
	 * @param aNormal3 the third point's normal.
	 * @param aColor3 the third point's color.
	 * @param aWireframeMode whether to draw in wireframe mode.
	 */
	public void drawTriangle(RasterSettings aSettings, 
			Color aTriangleColor,
			Vec4 aPoint1, Vec4 aNormal1, Color aColor1,  
			Vec4 aPoint2, Vec4 aNormal2, Color aColor2, 
			Vec4 aPoint3, Vec4 aNormal3, Color aColor3
			)
	{
		if (null != aTriangleColor)
		{
			aSettings.iGraphics.setColor(aTriangleColor);
		}
		
		// point 1 and 2 lie on same horizontal line
		if ((int)aPoint1.val[_Y_] == (int)aPoint2.val[_Y_])
		{
			drawTopDownOrBottomUpTriangle(aSettings, 
					aTriangleColor, 
					aPoint1, aNormal1, aColor1, 
					aPoint2, aNormal2, aColor2, 
					aPoint3, aNormal3, aColor3,
					true);
		}
		// point 2 and 3 lie on same horizontal line
		else if ((int)aPoint2.val[_Y_] == (int)aPoint3.val[_Y_])
		{
			drawTopDownOrBottomUpTriangle(aSettings, 
					aTriangleColor, 
					aPoint2, aNormal2, aColor2, 
					aPoint3, aNormal3, aColor3,
					aPoint1, aNormal1, aColor1, 
					true);
		}
		// point 1 and 3 lie on same horizontal line
		else if ((int)aPoint1.val[_Y_] == (int)aPoint3.val[_Y_])
		{
			drawTopDownOrBottomUpTriangle(aSettings, 
					aTriangleColor, 
					aPoint1, aNormal1, aColor1, 
					aPoint3, aNormal3, aColor3,
					aPoint2, aNormal2, aColor2,
					true);
		}
		
		/* at this point we know that no 2 points have the same y-value. */
		
		// point 1 is higher than point 2
		else if ((int)aPoint1.val[_Y_] < (int)aPoint2.val[_Y_]) 
		{
			// point 2 is higher than point 3
			if ((int)aPoint2.val[_Y_] < (int)aPoint3.val[_Y_])
			{
				drawTriangleWithDifferingPointYValues(aSettings, 
						aTriangleColor, 
						aPoint1, aNormal1, aColor1, 
						aPoint2, aNormal2, aColor2, 				
						aPoint3, aNormal3, aColor3);
			}
			// point 3 is higher than point 2 but lower than point 1
			else if ((int)aPoint1.val[_Y_] < (int)aPoint3.val[_Y_])
			{
				drawTriangleWithDifferingPointYValues(aSettings, 
						aTriangleColor, 
						aPoint1, aNormal1, aColor1, 
						aPoint3, aNormal3, aColor3,
						aPoint2, aNormal2, aColor2); 				
			}
			// point 3 is higher than both point 1 and 2
			else
			{
				drawTriangleWithDifferingPointYValues(aSettings, 
						aTriangleColor, 
						aPoint3, aNormal3, aColor3,
						aPoint1, aNormal1, aColor1, 
						aPoint2, aNormal2, aColor2); 				
			}
		}
		// point 1 is NOT higher than point 2
		else
		{
			// point 3 is higher than point 2
			if ((int)aPoint3.val[_Y_] < (int)aPoint2.val[_Y_])
			{
				drawTriangleWithDifferingPointYValues(aSettings, 
						aTriangleColor, 
						aPoint3, aNormal3, aColor3,
						aPoint2, aNormal2, aColor2, 				
						aPoint1, aNormal1, aColor1); 
			}
			// point 3 is lower than point 2 but higher than point 1
			else if ((int)aPoint3.val[_Y_] < (int)aPoint1.val[_Y_])
			{
				drawTriangleWithDifferingPointYValues(aSettings, 
						aTriangleColor, 
						aPoint2, aNormal2, aColor2, 				
						aPoint3, aNormal3, aColor3,
						aPoint1, aNormal1, aColor1); 
			}
			// point 3 is lower than both point 1 and 2
			else
			{
				drawTriangleWithDifferingPointYValues(aSettings, 
						aTriangleColor, 
						aPoint2, aNormal2, aColor2, 				
						aPoint1, aNormal1, aColor1, 
						aPoint3, aNormal3, aColor3);
			}
		}
	}	
	
	
	
	
	
	/**
	 * Draw a triangle whose three points all have different y-values. 
	 * 
	 * The points are supplied in ascending order of y-value (i.e. lowest 
	 * to highest).
	 * 
	 * @param aTriangleColor the color of the triangle.
	 * @param aPoint1 the first point.
	 * @param aNormal1 the first point's normal.
	 * @param aColor1 the first point's color.
	 * @param aPoint2 the second point.
	 * @param aNormal2 the second point's normal.
	 * @param aColor2 the second point's color.
	 * @param aPoint3 the third point.
	 * @param aNormal3 the third point's normal.
	 * @param aColor3 the third point's color.
	 */
	private void drawTriangleWithDifferingPointYValues(
			RasterSettings aSettings, 
			Color aTriangleColor,
			Vec4 aPoint1, Vec4 aNormal1, Color aColor1,  
			Vec4 aPoint2, Vec4 aNormal2, Color aColor2, 
			Vec4 aPoint3, Vec4 aNormal3, Color aColor3
			)
	{
		/*
		 * We draw this triangle by splitting it into a top-down and bottom-up 
		 * triangle pair and then drawing those.
		 * 
		 * 		   [t]
		 * 			o
		 * 		   o o
		 *    [m] o   o [p]
		 *          o  o
		 *            o o
		 *              oo
		 *                o [b]
		 *                 
		 * 
		 * y(p) = y(t) + d * (y(b) - y(t))      where d = delta
		 * x(p) = x(t) + d * (x(b) - x(t))
		 * z(p) = z(t) + d * (z(b) - z(t))
		 * 
		 * But we know that y(p) = y(m), thus:
		 * 
		 * y(m) = y(t) + d * (y(b) - y(t))
		 * 
		 * d = (y(m) - y(t)) / (y(b) - y(t))
		 */             
		
		float delta = (aPoint2.val[_Y_] - aPoint1.val[_Y_]) / (aPoint3.val[_Y_] - aPoint1.val[_Y_]);
		
		// calculate point P
		iTempVec1.val[_X_] = aPoint1.val[_X_] + delta * (aPoint3.val[_X_] - aPoint1.val[_X_]);
		iTempVec1.val[_Y_] = aPoint1.val[_Y_] + delta * (aPoint3.val[_Y_] - aPoint1.val[_Y_]);
		iTempVec1.val[_Z_] = aPoint1.val[_Z_] + delta * (aPoint3.val[_Z_] - aPoint1.val[_Z_]);
		
		// calculate normal at point P
		iTempVec2.set(aNormal1);
		iTempVec2.plusEq(aNormal3);
		iTempVec2.normalise();

		// TODO: calculate correct interpolated color for the point P
		
		// draw the bottom-up bit
		drawTopDownOrBottomUpTriangle(aSettings, 
				aTriangleColor, 
				aPoint2, aNormal2, aColor2,
				iTempVec1, iTempVec2, aColor1,
				aPoint1, aNormal1, aColor1,
				false);
		
		// draw the top-down bit
		drawTopDownOrBottomUpTriangle(aSettings, 
				aTriangleColor, 
				aPoint2, aNormal2, aColor2,
				iTempVec1, iTempVec2, aColor1,
				aPoint3, aNormal3, aColor3,
				false);
		
	}		
	
	
	
	
	
	/**
	 * Draw a triangle where atleast two of the points have the same y-value.
	 * 
	 * The first two points form the base line. The third point is the 
	 * other corner of the triangle to which the triangle is incrementally 
	 * rendered.
	 * 
	 * @param aTriangleColor the color of the triangle.
	 * @param aPoint1 the first point.
	 * @param aNormal1 the first point's normal.
	 * @param aColor1 the first point's color.
	 * @param aPoint2 the second point.
	 * @param aNormal2 the second point's normal.
	 * @param aColor2 the second point's color.
	 * @param aPoint3 the third point.
	 * @param aNormal3 the third point's normal.
	 * @param aColor3 the third point's color.
	 * @param aDrawLineBetweenPoints1And2 whether to draw a line between 
	 * aPoint1 and aPoint2.
	 */
	private void drawTopDownOrBottomUpTriangle(RasterSettings aSettings,
			Color aTriangleColor,
			Vec4 aPoint1, Vec4 aNormal1, Color aColor1,
			Vec4 aPoint2, Vec4 aNormal2, Color aColor2,
			Vec4 aPoint3, Vec4 aNormal3, Color aColor3,
			boolean aDrawLineBetweenPoints1And2
			)
	{
		final int END_Y = (int)aPoint3.val[_Y_];
		final int START_Y = (int)aPoint1.val[_Y_];
		
		float x1 = aPoint1.val[_X_], x2 = aPoint2.val[_X_];
		float old_x1 = x1, old_x2 = x2;
		
		float z1 = aPoint1.val[_Z_], z2 = aPoint2.val[_Z_];
		float old_z1 = z1, old_z2 = z2;

		final float x3 = aPoint3.val[_X_];
		final float z3 = aPoint3.val[_Z_];
		
		int old_y = START_Y;

		/* work out how much to inc/dec x and z by as we inc/dec y */
		
		// height
		final int y_diff = END_Y - START_Y;
		final int y_diff_abs = (y_diff > 0 ? y_diff : -y_diff);

		// dx/dy
		final float x_inc1 = ( x3 - x1 ) * 1.0f / y_diff_abs;
		final float x_inc2 = ( x3 - x2 ) * 1.0f / y_diff_abs;
		// dz/dy
		final float z_inc1 = ( z3 - z1 ) * 1.0f / y_diff_abs;
		final float z_inc2 = ( z3 - z2 ) * 1.0f / y_diff_abs;
		
		// draw base line?
		if (aDrawLineBetweenPoints1And2)
		{
			drawHorizontalLine(aSettings, aTriangleColor, 
					(int)x1, z1, aColor1, 
					(int)x2, z2, aColor2,
					(int)aPoint1.val[_Y_]);
		}
		
		/* now draw the other two lines */
		
		// if triangle is just a straight line
		if (0 == y_diff)
		{
			if (x3 < x1)
			{
				drawHorizontalLine(aSettings, aTriangleColor, 
						(int)x3, z3, aColor3, 
						(int)x2, z2, aColor2,
						START_Y
						);
			}
			else if (x3 > x2)
			{
				drawHorizontalLine(aSettings, aTriangleColor, 
						(int)x1, z1, aColor1, 
						(int)x3, z3, aColor3,
						START_Y
						);
			}
			else
			{	
				drawHorizontalLine(aSettings, aTriangleColor, 
						(int)x1, z1, aColor1, 
						(int)x2, z2, aColor2,
						START_Y
						);
			}
		}
		else
		{
			// top-down
			if (y_diff > 0)
			{
				for (int y = START_Y; y <= END_Y; y += 1.0f)
				{
					// if we're not in the first loop iteration
					if (y != old_y)
					{
						// draw more of the line from point 1 to point 3
						drawHorizontalLine(aSettings, 
								aTriangleColor, 
								(int)old_x1, old_z1, aColor1,
								(int)x1, z1, aColor1,
								old_y
								);
						// draw more of the line from point 2 to point 3
						drawHorizontalLine(aSettings, 
								aTriangleColor, 
								(int)old_x2, old_z2, aColor2,
								(int)x2, z2, aColor2,
								old_y
								);

						// shading mode
						if (!aSettings.iWireframeModeEnabled)
						{
							// draw line from x1 to x2
							drawHorizontalLine(aSettings, 
									aTriangleColor, 
									(int)x1, z1, aColor1,
									(int)x2, z2, aColor1,
									old_y
									);
						}
					}
					// save x's
					old_x1 = x1;
					old_x2 = x2;
					// save z's
					old_z1 = z1;
					old_z2 = z2;
					// save y
					old_y = y;
					// next values
					x1 += x_inc1;
					x2 += x_inc2;
					z1 += z_inc1;
					z2 += z_inc2;
				} // end for y
			} // end top-down
			// bottom-up
			else /*if (y_diff < 0)*/
			{
				for (int y = START_Y; y >= END_Y; y -= 1.0f)
				{
					// if we're not in the first loop iteration
					if (y != old_y)
					{
						// draw more of the line from point 1 to point 3
						drawHorizontalLine(aSettings, 
								aTriangleColor, 
								(int)old_x1, old_z1, aColor1,
								(int)x1, z1, aColor1,
								old_y
								);
						// draw more of the line from point 2 to point 3
						drawHorizontalLine(aSettings, 
								aTriangleColor, 
								(int)old_x2, old_z2, aColor2,
								(int)x2, z2, aColor2,
								old_y
								);

						// shading mode
						if (!aSettings.iWireframeModeEnabled)
						{
							// draw line from x1 to x2
							drawHorizontalLine(aSettings, 
									aTriangleColor, 
									(int)x1, z1, aColor1,
									(int)x2, z2, aColor1,
									old_y
									);
						}
					}
					// save x's
					old_x1 = x1;
					old_x2 = x2;
					// save z's
					old_z1 = z1;
					old_z2 = z2;
					// save y
					old_y = y;
					// next values
					x1 += x_inc1;
					x2 += x_inc2;
					z1 += z_inc1;
					z2 += z_inc2;
				} // end for y
			} // end bottom-up
			
			
			// draw final part of line from point 1 to point 3
			drawHorizontalLine(aSettings, 
					aTriangleColor, 
					(int)old_x1, old_z1, aColor1,
					(int)x3, z3, aColor3,
					old_y
					);
			// draw final part of line from point 2 to point 3
			drawHorizontalLine(aSettings, 
					aTriangleColor, 
					(int)old_x2, old_z2, aColor2,
					(int)x3, z3, aColor3,
					old_y
					);
			
		} // end triangle is not just a line
		

	}

	
	
	/**
	 * Draw a horizontal line.
	 * 
	 * @param aSettings
	 * @param aFlatShadeColor the line color if flat-shading.
	 * @param x1 the x-coordinate of the start point.
	 * @param z1 the z-coordinate of the start point.
	 * @param color1 the color of the start point.
	 * @param x2 the x-coordinate of the end point.
	 * @param z2 the z-coordinate of the end point.
	 * @param color2 the color of the end point.
	 * @param y the y-coordinate.
	 */
	private void drawHorizontalLine(RasterSettings aSettings, 
			Color aFlatShadeColor, 
			int x1, float z1, Color color1, 
			int x2, float z2, Color color2,
			int y)
	{
		// check that it's visible
		if (0 > y || aSettings.iViewportDimensions.height <= y)
			return;
		
		// if z-buffer is enabled
		if (aSettings.iZBufferEnabled)
		{
			// ensure x1 is the left-most point
			if (x1 > x2)
			{
				iTempInt = x1;
				x1 = x2;
				x2 = iTempInt;
				iTempFloat = z1;
				z1 = z2;
				z2 = iTempFloat;
			}
			
			// work out z-increment (use 0 if the line is actually just a dot)
			float z_inc = (x1 < x2) ? (z2 - z1) / (x2 - x1) : 0;

			// clip to viewport boundaries
			if (0 > x1)
			{
			    if (0 > x2)
			        return;
			    
				z1 = z1 + (-x1) * z_inc;
				x1 = 0;
			}
			if (aSettings.iViewportDimensions.width <= x2)
			{
	            if (aSettings.iViewportDimensions.width <= x1)
	                return;
	            
	            z2 = z2 - (x2-aSettings.iViewportDimensions.width-1) * z_inc;
				x2 = aSettings.iViewportDimensions.width-1;
			}

			int x = x1;
			float z = z1;
			
			while (x <= x2)
			{
				// iterate past visible pixels until we hit one that's 
				// occluded or until we pass the last pixel
				while (x <= x2 && (iZBufferFrameNumber[x][y] != iFrameNumber || z > iZBuffer[x][y]) )
				{
					++x; z += z_inc;
					continue;
				}
				
				/*
				 * NOTE: if we've passed the last pixel (i.e. x > x2) then it
				 * means that the rest of the line is visible
				 */
				
				// draw a line up until the occluded pixel and update the
				// z-buffer and start points
				if (x > x1)
				{
					aSettings.iGraphics.drawLine(x1,y,x-1,y);
					
					z = z1;
					for (int i=x1; i<x; ++i)
					{
						iZBuffer[i][y] = z;
						iZBufferFrameNumber[i][y] = iFrameNumber;
						z += z_inc;
					}
					// restore value of z to what it should be
					z += z_inc;
				}
				
				// iterate past occluded pixels until we hit one that's 
				// visible or until we pass the last pixel
				while (x <= x2 && (iZBufferFrameNumber[x][y] == iFrameNumber && z <= iZBuffer[x][y]) )
				{
					++x; z += z_inc;
					continue;
				}
				
				// next set of visible pixels start at this point
				x1 = x;
				z1 = z;
			}
		}
		// if z-buffer is off
		else
		{
			aSettings.iGraphics.drawLine(x1,y,x2,y);
		}
	}
	
	
	
	
	/**
	 * Draw a vertical line.
	 * 
	 * @param aSettings
	 * @param aFlatShadeColor the line color if flat-shading.
	 * @param y1 the y-coordinate of the start point.
	 * @param z1 the z-coordinate of the start point.
	 * @param color1 the color of the start point.
	 * @param y2 the y-coordinate of the end point.
	 * @param z2 the z-coordinate of the end point.
	 * @param color2 the color of the end point.
	 * @param x the x-coordinate.
	 */
	private void drawVerticalLine(RasterSettings aSettings, 
			Color aFlatShadeColor, 
			int y1, float z1, Color color1, 
			int y2, float z2, Color color2,
			int x)
	{
		// check that it's visible
		if (0 > x || aSettings.iViewportDimensions.width <= x)
			return;
		
		
		// if z-buffer is enabled
		if (aSettings.iZBufferEnabled)
		{
			// ensure y1 is the top-most point
			if (y1 > y2)
			{
				iTempInt = y1;
				y1 = y2;
				y2 = iTempInt;
				iTempFloat = z1;
				z1 = z2;
				z2 = iTempFloat;
			}
			
			// work out z-increment (use 0 if the line is actually just a dot)
			float z_inc = (y1 < y2) ? (z2 - z1) / (y2 - y1) : 0;
			
			// clip to viewport boundaries
			if (0 > y1)
			{
	            if (0 > y2)
	                return;

	            z1 = z1 + (-y1) * z_inc;
				y1 = 0;
			}
			if (aSettings.iViewportDimensions.width <= y2)
			{	    
			    if (aSettings.iViewportDimensions.width <= y1)
			        return;
			    
				z2 = z2 - (y2-aSettings.iViewportDimensions.width-1) * z_inc;
				y2 = aSettings.iViewportDimensions.height-1;
			}
			
			int y = y1;
			float z = z1;
			
			while (y <= y2)
			{
				// iterate past visible pixels until we hit one that's 
				// occluded or until we pass the last pixel
				while (y <= y2 && (iZBufferFrameNumber[x][y] != iFrameNumber || z > iZBuffer[x][y]) )
				{
					++y; z += z_inc;
					continue;
				}
				
				/*
				 * NOTE: if we've passed the last pixel (i.e. x > x2) then it
				 * means that the rest of the line is visible
				 */
				
				// draw a line up until the occluded pixel and update the
				// z-buffer and start points
				if (y > y1)
				{
					aSettings.iGraphics.drawLine(x,y1,x,y-1);
					z = z1;
					for (int i=y1; i<y; ++i)
					{
						iZBuffer[x][i] = z;
						iZBufferFrameNumber[x][i] = iFrameNumber;
						z += z_inc;
					}
					// restore value of z to what it should be
					z += z_inc;
				}
				
				// iterate past occluded pixels until we hit one that's 
				// visible or until we pass the last pixel
				while (y <= y2 && (iZBufferFrameNumber[x][y] == iFrameNumber && z <= iZBuffer[x][y]) )
				{
					++y; z += z_inc;
					continue;
				}
				
				// next set of visible pixels start at this point
				y1 = y;
				z1 = z;
			}
		}
		// if z-buffer is off
		else
		{
			aSettings.iGraphics.drawLine(x,y1,x,y2);
		}
	}
	
	public void resetStaticData()
	{
		iInstance = null;
	}
}



