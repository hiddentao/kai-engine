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

package com.hiddentao.kai.demos;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Iterator;

import com.hiddentao.kai.geometry.Angles3D;
import com.hiddentao.kai.geometry.Vec2;
import com.hiddentao.kai.logging.Logger;
import com.hiddentao.kai.math.MathConstants;
import com.hiddentao.kai.nodes.DirectionalLight;
import com.hiddentao.kai.nodes.Node;
import com.hiddentao.kai.nodes.Translation;
import com.hiddentao.kai.renderer.Camera;
import com.hiddentao.kai.renderer.Renderer;
import com.hiddentao.kai.renderer.interaction.InteractionHandler;


/**
 * Demo custom interaction handler.
 */
public final class DemoInteractionHandler extends InteractionHandler implements MathConstants
{
	private static Logger LOG = Logger.getLogger(DemoInteractionHandler.class.getName());
	
	private final String[] iKeyAndMouseCommands = {
			"Mouse click = next scene",
			"Mouse drag = rotate object",
			"Mouse wheel / scroll = shrink/expand group",
			"up/down = inc/dec vertical FOV angle",
			"B = toggle backface culling",
			"P = toggle perspective projection",
			"R = reset camera",
			"W = toggle wireframe mode",
			"Z = toggle z-buffer"
	};	
	
	private Angles3D iCameraAngles = new Angles3D();
	private Vec2 iMouseXY = new Vec2();
	
	private ArrayList<Node> iScenes = null;
	private int iCurrentScene = 0;
	private float iSceneScalingFactor = 1.0f;
	
	private volatile DirectionalLight iLight = new DirectionalLight(0,0,1);

	
	
	
	/**
	 * Constructor.
	 * 
	 * @param aMeshes list of scenes.
	 */
	public DemoInteractionHandler(ArrayList<Node> aScenes)
	{
		super();
		iScenes = aScenes;
	}


	public void doReset()
	{
		if (null != iSurface)
		{
			Camera camera = iSurface.getCamera();
			if (null != camera)
			{
				camera.reset();
			}
			
			iCurrentScene = 0;
			showNextScene();
		}
	}
	
	

	public void keyPressed(KeyEvent arg0)
	{
		Renderer renderer = null;
		Camera camera = null;
		
		if (null != iSurface)
		{
			renderer = iSurface.getRenderer();
			camera = iSurface.getCamera();
		}

		
		switch (arg0.getKeyCode())
		{
			case KeyEvent.VK_R:
				reset();
				break;
			case KeyEvent.VK_B:
				// toggle backface culling
				if (null != renderer)
				{
					renderer.enableBackfaceCulling(!renderer.isBackfaceCullingEnabled());
				}
				break;
			case KeyEvent.VK_W:
				// toggle wireframe mode
				if (null != renderer)
				{
					renderer.enableWireframeMode(!renderer.isWireframeModeEnabled());
				}
				break;
			case KeyEvent.VK_Z:
				// toggle z-buffer
				if (null != renderer)
				{
					renderer.enableZBuffer(!renderer.isZBufferEnabled());
				}
				break;
			case KeyEvent.VK_P:
				// toggle perspective projection
				if (null != camera)
				{
					camera.setOrthographic(!camera.isOrthographic());
				}
				break;
			case KeyEvent.VK_UP:
				// inc FOV angle
				if (null != camera)
				{
					camera.frustum.verticalFOV = (camera.frustum.verticalFOV + 1) % 360;
					LOG.info("Vertical FOV angle: " + camera.frustum.verticalFOV);
				}
				break;
			case KeyEvent.VK_DOWN:
				// dec FOV angle
				if (null != camera)
				{
					camera.frustum.verticalFOV = (camera.frustum.verticalFOV + 359) % 360;
					LOG.info("Vertical FOV angle: " + camera.frustum.verticalFOV);
				}
				break;
		}
		
		refreshDisplay();
	}

	public void keyReleased(KeyEvent arg0)
	{
	}

	public void keyTyped(KeyEvent arg0)
	{
	}

	public void mouseClicked(MouseEvent arg0)
	{
		showNextScene();
	}

	public void mouseEntered(MouseEvent arg0)
	{
	}

	public void mouseExited(MouseEvent arg0)
	{
	}

	public void mousePressed(MouseEvent arg0)
	{
		iMouseXY.set(arg0.getX(), arg0.getY());
	}

	public void mouseReleased(MouseEvent arg0)
	{
		iMouseXY.setZero();
	}

	public void mouseDragged(MouseEvent arg0)
	{
		int x = arg0.getX();
		int y = arg0.getY();
		
		iCameraAngles.iAngleX = (float)(y - iMouseXY.y);
		iCameraAngles.iAngleY = (float)(x - iMouseXY.x);
		iCameraAngles.iAngleZ = 0;
		
		iMouseXY.x = x;
		iMouseXY.y = y;

		Camera camera = iSurface.getCamera();
		if (null != camera)
			camera.rotateAroundFocus(iCameraAngles);
		
		refreshDisplay();
	}

	public void mouseMoved(MouseEvent arg0)
	{
	}

	public void mouseWheelMoved(MouseWheelEvent arg0)
	{
		// inc/dec scaling factor
		iSceneScalingFactor += (arg0.getWheelRotation() / 10.0f);
		
		// ensure it's capped
		if (1.0f > iSceneScalingFactor)
			iSceneScalingFactor = 1.0f;
		if (2.0f < iSceneScalingFactor)
			iSceneScalingFactor = 2.0f;
		
		// apply the scaling factor to each translation vector
		Node currentScene = iScenes.get(iCurrentScene);
		for (Iterator<Node> it = currentScene.children(); it.hasNext(); )
		{
			Node n = it.next();
			if (n instanceof Translation)
			{
				((Translation)n).setScaleFactor(iSceneScalingFactor);
			}
		}
		
		refreshDisplay();
	}
	

	
	protected void refreshDisplay()
	{
		if (null != iSurface)
		{
			iSurface.refresh();
		}
	}
	
	
	@Override
	public String[] getKeyAndMouseCommands()
	{
		return iKeyAndMouseCommands;
	}	

	
	/**
	 * Show the next scene.
	 */
	private void showNextScene()
	{
		iCurrentScene = (iCurrentScene + 1) % iScenes.size();
		
		LOG.info("Switching to scene " + (iCurrentScene+1));
		
		iLight.detachAllChildren();
		iLight.attachChild(iScenes.get(iCurrentScene));
		if (null != iSurface)
		{
			iSurface.setScene(iLight);
		}
	}


}


