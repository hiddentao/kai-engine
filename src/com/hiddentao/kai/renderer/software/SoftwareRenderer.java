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
import java.awt.Graphics2D;
import java.util.Iterator;

import com.hiddentao.kai.geometry.Dimensions2D;
import com.hiddentao.kai.geometry.Frustum;
import com.hiddentao.kai.geometry.Mat4;
import com.hiddentao.kai.geometry.Triangle3D;
import com.hiddentao.kai.geometry.Vec4;
import com.hiddentao.kai.geometry.VectorComponents;
import com.hiddentao.kai.geometry.util.MatrixStack;
import com.hiddentao.kai.geometry.util.MatrixStack.MatrixStackException;
import com.hiddentao.kai.logging.Logger;
import com.hiddentao.kai.nodes.Mesh;
import com.hiddentao.kai.nodes.Node;
import com.hiddentao.kai.renderer.Camera;
import com.hiddentao.kai.renderer.Renderer;

final class SoftwareRenderer extends Renderer implements VectorComponents
{
	private final static Logger LOG = Logger.getLogger(SoftwareRenderer.class.getName());

	private RasterSettings iRasterSettings = null;
	
	private boolean iBackfaceCullingEnabled = true;
	private Color iBgColor = Color.BLACK;
	private Mat4 iViewportMat = new Mat4();
	private Mat4 iCameraMat = new Mat4();
	private Mat4 iProjectionMat = new Mat4();
	private Mat4 iTransformationMatrix = new Mat4();
	
	private float iAspectRatio = 1;
	private Frustum iCameraFrustum = null;
	
	private Vec4[] iTransformedVectors1 = new Vec4[3];
	private Vec4[] iTransformedVectors2 = new Vec4[3];
	
	/** Vector used in backface culling algorithm. */
	private Vec4 iCameraDirection = new Vec4();

	/** 
	 * The normalised camera vector, once the camera (and therefore the world) 
	 * has been transformed to the origin.
	 */
	private Vec4 iTransformedCameraDirection = new Vec4(0,0,-1,0);
	
	/** Whether perspective projection is enabled. */
	private boolean iPerspectiveProjectionEnabled = false;
	
	private Vec4 iTempVec1 = new Vec4();
	private Mat4 iTempMat1 = new Mat4();
	private StringBuilder iTempStr = new StringBuilder(24);
	private Color iTempCol1 = new Color(0);
	private Vec4 iLightVec = new Vec4();
	
	
	public SoftwareRenderer()
	{
		// initialise array to hold a transformed triangle's vectors
		for (int i=0; i<3; ++i)
		{
			iTransformedVectors1[i] = new Vec4();
			iTransformedVectors2[i] = new Vec4();
		}

		iRasterSettings = new RasterSettings();

		enableBackfaceCulling(true);
		enableWireframeMode(false);
		enableZBuffer(true);
	}
	
	
	
	@Override
	public void render(Node aRoot, Graphics2D aGraphics)
	{
		try
		{
			final long startTime = System.nanoTime();

			// rasteriser settings
			iRasterSettings.iGraphics = aGraphics;
			iRasterSettings.iGraphics.setBackground(iBgColor);
			iRasterSettings.iGraphics.clearRect(
					0, 0, 
					iRasterSettings.iViewportDimensions.width,
					iRasterSettings.iViewportDimensions.height
					);
			
			// reset rasteriser
			Rasteriser.getInstance().resetForNextFrame(iRasterSettings);
		
			// set initial transformation matrix
			iTransformationMatrix.set(iProjectionMat).multEq(iCameraMat);
			
			// reset lighting
			iLightVec.setZero();
			
			// render the scene
			visitScene(aRoot);
			
			// show fps
			final long timeTakenInNanos = System.nanoTime() - startTime + 1;	// >0
			iTempStr.setLength(0);
			iTempStr.append(Math.round((1.0f / (timeTakenInNanos / 1000000000.0f))));
			iTempStr.append(" fps");
			iRasterSettings.iGraphics.setColor(Color.WHITE);
			iRasterSettings.iGraphics.drawString(iTempStr.toString(), 5, 10);
		}
		catch (Exception e)
		{
			LOG.error("Error rendering frame", e);
		}			
	}

	
	
	/**
	 * Traverse down the scenegraph starting at the given node and processing 
	 * all its kids.
	 * @param aNode the node to start from. If null then nothing happens.
	 * @throws MatrixStackException 
	 */
	private void visitScene(Node aNode) throws MatrixStackException
	{
		if (null != aNode)
		{
			aNode.visitDown(this);
			final int NUM_KIDS = aNode.numberOfChildren();
			for (Iterator<Node> kids = aNode.children(); kids.hasNext(); )
			{
				// push current matrix if there is more than one kid
				if (1 < NUM_KIDS)
					MatrixStack.getInstance().push(iTransformationMatrix);
				
				Node child = kids.next();
				visitScene(child);
				
				// pop current matrix if there is more than one kid
				if (1 < NUM_KIDS)
					MatrixStack.getInstance().pop(iTransformationMatrix);
				
			} // end foreach kid
		} // end if node is not null
	}
	
	
	
	
	@Override
	protected void setupCamera(Camera aCamera)
	{
		/* setup camera matrix */

		iCameraMat.setIdentity();

		if (null == aCamera)
		{
			iCameraDirection.setZero();
		}
		else
		{
			iCameraDirection.set(aCamera.focus);
			iCameraDirection.minusEq(aCamera.eye);
			iCameraDirection.normalise();
			
			Vec4[] uvn = aCamera.getCameraUVNAxes();
			
			/* 
			 * Setup matrix:
			 * 
			 * Mat 	= AxisTransform x Translate
			 * 		
			 * 		 	|Ux Uy Uz 0|  |1 0 0 -Ex|
			 * 		=	|Vx Vy Vz 0|  |0 1 0 -Ex|
			 * 			|Nx Ny Nz 0|  |0 0 1 -Ex|
			 * 			| 0  0  0 1|  |0 0 0 -Ex|
			 * 
			 * 
			 * 		 	|Ux Uy Uz -U.E|
			 * 		=	|Vx Vy Vz -V.E|
			 * 			|Nx Ny Nz -N.E|
			 * 			| 0  0   0   1|
			 * 
			 */
			// setup axes
			iCameraMat.val[0][0] = uvn[0].val[_X_];
			iCameraMat.val[0][1] = uvn[0].val[_Y_];
			iCameraMat.val[0][2] = uvn[0].val[_Z_];
			iCameraMat.val[0][3] = -uvn[0].getDotProduct(aCamera.eye);
			iCameraMat.val[1][0] = uvn[1].val[_X_];
			iCameraMat.val[1][1] = uvn[1].val[_Y_];
			iCameraMat.val[1][2] = uvn[1].val[_Z_];
			iCameraMat.val[1][3] = -uvn[1].getDotProduct(aCamera.eye);
			iCameraMat.val[2][0] = uvn[2].val[_X_];
			iCameraMat.val[2][1] = uvn[2].val[_Y_];
			iCameraMat.val[2][2] = uvn[2].val[_Z_];
			iCameraMat.val[2][3] = -uvn[2].getDotProduct(aCamera.eye);
		}
		
		/*
		 * Setup projection matrix
		 */
		
		// frustum
		iCameraFrustum = aCamera.frustum;

		// projection type
        iProjectionMat.setIdentity();

        iPerspectiveProjectionEnabled = !aCamera.isOrthographic();
		
		// perspective projection?
		if (iPerspectiveProjectionEnabled)
		{
			float viewPlane = iCameraFrustum.viewPlane();
			iProjectionMat.val[0][0] = viewPlane;
			iProjectionMat.val[1][1] = viewPlane;
			iProjectionMat.val[2][2] = 1;	 
			iProjectionMat.val[3][2] = 1;	
			iProjectionMat.val[3][3] = 0;
		}
	}
	
	
	
	@Override
	protected void setupViewport(Dimensions2D aViewport)
	{
		iRasterSettings.iViewportDimensions.set(aViewport);
		
		/**
		 * Setup world-to-viewport transformation
		 * 
		 * x_screen = (a + x_orig * a)
		 * y_screen = (b - y_orig * b)
		 * 
		 * where:
		 * 		a = (0.5 * width - 0.5)
		 * 		b = (0.5 * height - 0.5)
		 */
		
		float a = (float)Math.floor(0.5f * iRasterSettings.iViewportDimensions.width - 0.5f);
		float b = (float)Math.floor(0.5f * iRasterSettings.iViewportDimensions.height - 0.5f);
		
		iAspectRatio = (float)iRasterSettings.iViewportDimensions.width / 
							(float)iRasterSettings.iViewportDimensions.height;

		iViewportMat.setIdentity();  // preserve z and w values
		iViewportMat.val[0][0] = a / iAspectRatio;
		iViewportMat.val[0][3] = a;
		iViewportMat.val[1][1] = -b;
		iViewportMat.val[1][3] = b;	
	}
	
	

	@Override
	public void process(Node aNode)
	{
		LOG.trace("Processing node: " + aNode);
	}
	
	

	@Override
	public void process(Mesh aMesh)
	{
		LOG.trace("Processing mesh: " + aMesh);
		
		for (Iterator<Triangle3D> tris = aMesh.polygonIterator(); tris.hasNext(); )
		{
			// flag which indicates whether this triangle should be culled
			boolean triangleCulled = false;

			// get next polygon
			Triangle3D tri = tris.next();
			

			for (int i=0; i<3 && !triangleCulled; ++i)
			{
				// local-to-world-to-camera transformations
				Mat4.transformVector(iTransformationMatrix, tri.vertices[i].point, iTransformedVectors1[i]);		
				
				// frustum-culling
				if (	iTransformedVectors1[i].val[_Z_] > iCameraFrustum.near 
					||	iTransformedVectors1[i].val[_Z_] < iCameraFrustum.far 
						)
				{
					triangleCulled = true;
					break;
				}
				
				// divide by w (but ensure we keep value of z for depth sorting later on)
				iTransformedVectors1[i].val[_X_] /= iTransformedVectors1[i].val[_W_];
				iTransformedVectors1[i].val[_Y_] /= iTransformedVectors1[i].val[_W_];
				iTransformedVectors1[i].val[_W_] = 1;
				
				// projection-to-screen transformations
				Mat4.transformVector(iViewportMat, iTransformedVectors1[i], iTransformedVectors2[i]);
				
			} // end foreach vertex
			
			// if triangle has been frustum-culled then skip to next triangle
            if (triangleCulled)
                continue;

            // recalculate the polygon normal
            Vec4.calculateSurfaceNormal( 
                    iTransformedVectors1[0], 
                    iTransformedVectors1[1], 
                    iTransformedVectors1[2],
                    iTempVec1
                    );

            // get dot product with camera direction vector
            float dotProduct = iTempVec1.getDotProduct(iTransformedCameraDirection);

            // if backface culling is enabled
            // (we could do this earlier if using orthographic projection but 
            //   for code simplicity sake we're doing it here so that it 
            //   doesn't matter what type of projection is active).
            if (iBackfaceCullingEnabled)
            {
                // polygon is visible iff view direction vector and polygon 
                // normal vector are heading in opposing directions 
                if (0 < dotProduct)
                {
                    continue;
                }
            } // end if backface culling enabled

            // get absolute dot product
            if (0 > dotProduct)
            	dotProduct = -dotProduct;
            
            // lighting?
            if (null != tri.color)
            {
            	// work out how bright tri should be
                dotProduct = iTempVec1.getDotProduct(iLightVec);
                if (0 > dotProduct)
                	dotProduct = -dotProduct;
                
                iTempCol1 = new Color(
                		(int) (tri.color.getRed() * dotProduct),
                		(int) (tri.color.getGreen() * dotProduct),
                		(int) (tri.color.getBlue() * dotProduct)
                		);
            }
            else
            {
            	iTempCol1 = tri.color;
            }
            
			Rasteriser.getInstance().drawTriangle(iRasterSettings, 
					iTempCol1,
					iTransformedVectors2[0], tri.vertices[0].normal, tri.vertices[0].color, 
					iTransformedVectors2[1], tri.vertices[1].normal, tri.vertices[1].color, 
					iTransformedVectors2[2], tri.vertices[2].normal, tri.vertices[2].color);
			
		} // end foreach triangle
	}

	


	@Override
	public void processTranslation(float ax, float ay, float az)
	{
		LOG.trace("processTranslation: x=" + ax + ", y=" + ay + ", z=" + az);
		
		// construct a translation matrix
		iTempMat1.setIdentity();
		iTempMat1.val[0][3] = ax;
		iTempMat1.val[1][3] = ay;
		iTempMat1.val[2][3] = az;
		
		// add to current transformation matrix
		iTransformationMatrix.multEq(iTempMat1);
	}
		
	
	
	@Override
	public void processRotation(float ax, float ay, float az)
	{
		LOG.trace("processRotation: ax=" + ax + ", ay=" + ay + ", az=" + az);
		
		// X rotation
		iTempMat1.setIdentity();
		iTempMat1.val[1][1] = (float)Math.cos(ax);
		iTempMat1.val[1][2] = -(float)Math.sin(ax);
		iTempMat1.val[2][1] = (float)Math.sin(ax);
		iTempMat1.val[2][2] = (float)Math.cos(ax);
		iTransformationMatrix.multEq(iTempMat1);
		
		// Y rotation
		iTempMat1.setIdentity();
		iTempMat1.val[0][0] = (float)Math.cos(ay);
		iTempMat1.val[0][2] = (float)Math.sin(ay);
		iTempMat1.val[2][0] = -(float)Math.sin(ay);
		iTempMat1.val[2][2] = (float)Math.cos(ay);
		iTransformationMatrix.multEq(iTempMat1);
		
		// Z rotation
		iTempMat1.setIdentity();
		iTempMat1.val[0][0] = (float)Math.cos(az);
		iTempMat1.val[0][1] = -(float)Math.sin(az);
		iTempMat1.val[1][0] = (float)Math.sin(az);
		iTempMat1.val[1][1] = (float)Math.cos(az);
		iTransformationMatrix.multEq(iTempMat1);
	}	

	


	@Override
	public void processDirectionalLight(Vec4 aVec)
	{
		iLightVec.set(aVec.val[_X_], aVec.val[_Y_], aVec.val[_Z_], 0);
	}



	@Override
	public void setBackgroundColor(Color aColor)
	{
		iBgColor = aColor;
	}



	@Override
	public void enableBackfaceCulling(boolean val)
	{
		iBackfaceCullingEnabled = val;
		LOG.info("Backface culling enabled: " + val);
	}


	@Override
	public boolean isBackfaceCullingEnabled()
	{
		return iBackfaceCullingEnabled;
	}

	
	@Override
	public void enableWireframeMode(boolean val)
	{
		iRasterSettings.iWireframeModeEnabled = val;
		LOG.info("Wireframe mode enabled: " + val);
	}


	@Override
	public boolean isWireframeModeEnabled()
	{
		return iRasterSettings.iWireframeModeEnabled;
	}



	@Override
	public void enableZBuffer(boolean val)
	{
		iRasterSettings.iZBufferEnabled = val;
		LOG.info("Z-buffer enabled: " + val);
	}


	@Override
	public boolean isZBufferEnabled()
	{
		return iRasterSettings.iZBufferEnabled;
	}


}


