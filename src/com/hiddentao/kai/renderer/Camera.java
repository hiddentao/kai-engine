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

package com.hiddentao.kai.renderer;

import com.hiddentao.kai.geometry.Angles3D;
import com.hiddentao.kai.geometry.Frustum;
import com.hiddentao.kai.geometry.Quat;
import com.hiddentao.kai.geometry.Vec4;
import com.hiddentao.kai.logging.Logger;
import com.hiddentao.kai.math.MathConstants;


/**
 * Represents a UVN camera in the 3D scene.
 */
public class Camera implements MathConstants
{
	private static Logger LOG = Logger.getLogger(Camera.class.getName());

	public Vec4 eye = new Vec4();
	public Vec4 up = new Vec4();
	public Vec4 focus = new Vec4();
	public Frustum frustum = new Frustum();
	
	private boolean iIsOrthographic = false;

	private Vec4[] iUVN = new Vec4[3];

	
	
	// temporary vectors used in calculations
	private Vec4 iTempFocusVec = new Vec4();
	private Vec4 iTempEyeVec = new Vec4();
	private Vec4 iTempUpVec = new Vec4();
	private Vec4 iTempVecU = new Vec4();
	private Vec4 iTempVecV = new Vec4();
	private Vec4 iTempVecW = new Vec4();
	
	private Quat iTempQuatQ = new Quat();
	private Quat iTempQuatQi = new Quat();
	private Quat iTempQuatEye = new Quat();
	private Quat iTempQuatUp = new Quat();
	private Quat iTempQuatRes1 = new Quat();
	
	
	
	
	/**
	 * Constructor - initialises this to some default values.
	 */
	public Camera()
	{
		for (int i=0; i<3; ++i)
		{
			iUVN[i] = new Vec4();
		}
		reset();
	}
	
	
	
	/**
	 * Reset this camera to default values.
	 */
    public void reset()
    {
        eye.set( 0.0f, 0.0f, 2.0f, 1.0f );
        focus.set( 0.0f, 0.0f, 0.0f, 1.0f );
    	up.set( 0.0f, 1.0f, 0.0f, 0.0f );

	    frustum.set( -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -100.0f, 90.0f );
    }	
	
	
	
	/**
	 * Get whether this camera uses orthographic projection.
	 * @return true if it does; false otherwise.
	 */
	public boolean isOrthographic()
	{
		return iIsOrthographic;
	}

	
	/**
	 * Set whether this camera should use orthographic projection.
	 * @param isOrthographic true if it should; false otherwise.
	 */
	public void setOrthographic(boolean isOrthographic)
	{
		LOG.info("Orthographic projection enabled: " + isOrthographic);
		iIsOrthographic = isOrthographic;
	}
	
	
	
	/**
	 * Get 3 vectors representing the UVN axes of this camera.
	 * @return an array of 3 vectors.
	 */
	public Vec4[] getCameraUVNAxes()
	{
		updateUVNAxes();
		return iUVN;
	}

	

	
	/**
	 * Update the UVN axes vectors to reflect the current position, 
	 * orientation and focus of this camera.
	 */
	private void updateUVNAxes()
	{
		// V (y) axis
		iUVN[1].set(up);
		iUVN[1].normalise();
		// N (z) axis
		iUVN[2].set(eye);
		iUVN[2].minusEq(focus);
		iUVN[2].normalise();
		// U (x) axis
		Vec4.calculateCrossProduct(iUVN[1], iUVN[2], iUVN[0]);
	}
	
	
	
	
	/**
	 * Rotate the camera around its focal point.
	 * @param aAngles the 3D rotation angles (in degrees).
	 */
    public void rotateAroundFocus(Angles3D aAngles)
    {
        // vectors
    	iTempFocusVec.set(focus);
    	iTempEyeVec.set(eye);
    	iTempEyeVec.minusEq(focus);
    	iTempUpVec.set(up);
    	
        // angles
        Angles3D radians = aAngles.mult(DEG_TO_RAD);
        
        // u,v,w axes (camera coordinate system based at focal point)
        iTempVecV.set(iTempUpVec);
        iTempVecV.normalise();
        iTempVecW.set(iTempEyeVec);
        iTempVecW.normalise();
        Vec4.calculateCrossProduct(iTempVecV, iTempVecW, iTempVecU);
        iTempVecU.normalise();
        
        // rotation quaternions (we're rotating around u,v,w axes)
        Quat.calculateEulerRotationQuat(radians, iTempVecU, iTempVecV, iTempVecW, iTempQuatQ);
        Quat.calculateInverse(iTempQuatQ, iTempQuatQi);
        
        // vector quaternions (input vectors)
        iTempQuatEye.set(0, iTempVecW);
        iTempQuatUp.set(0, iTempVecV);
        
        /*
         * do rotation
         *  -> q_eye_new = q * q_eye * qi
         *  -> q_up_new = q * q_up * qi;
         */
        
        // eye
        iTempQuatRes1.set(iTempQuatQ).multEq(iTempQuatEye);
        iTempQuatRes1.multEq(iTempQuatQi);
        iTempQuatRes1.iVec.normalise();
        iTempQuatRes1.iVec.multEq(iTempEyeVec.getLength());
        iTempFocusVec.plusEq(iTempQuatRes1.iVec);
        eye.set(iTempFocusVec);

        // up
        iTempQuatRes1.set(iTempQuatQ).multEq(iTempQuatUp);
        iTempQuatRes1.multEq(iTempQuatQi);
        iTempQuatRes1.iVec.normalise();
        up.set(iTempQuatRes1.iVec);
        
    }	
	
	
	
}



