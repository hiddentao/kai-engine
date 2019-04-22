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

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.hiddentao.kai.app.AppBridge;
import com.hiddentao.kai.geometry.Dimensions2D;
import com.hiddentao.kai.logging.Logger;
import com.hiddentao.kai.nodes.Node;
import com.hiddentao.kai.renderer.interaction.InteractionHandler;


/**
 * A surface which renders graphics and handles user input.
 */
public final class RenderingSurface extends JPanel
{
	private static Logger LOG = Logger.getLogger(RenderingSurface.class.getName());
	
	private static final long serialVersionUID = -6195462797470825673L;
	
	private InteractionHandler iHandler = null;
	private Renderer iRenderer = null;
	private Node iScene = null;
	private Dimensions2D iDimensions = new Dimensions2D();
	private Camera iCamera = null;

	
	/**
	 * Constructor.
	 * 
	 * @param iContainer the top-level container which holds this surface. 
	 * This may be null.
	 */
	public RenderingSurface()
	{
		super();
		setDoubleBuffered(true);
	}
	
	
	
	/**
	 * Get the scene camera.
	 * @return the scene camera. Default is null.
	 */
	public Camera getCamera()
	{
		return iCamera;
	}

	
	/**
	 * Set the scene camera.
	 * @param camera the camera. If null then there is no camera positioning.
	 */
	public void setCamera(Camera camera)
	{
		iCamera = camera;
		refresh();
	}
	
	

	/**
	 * Get the scene that is being rendered.
	 * @return the scene. Default is null.
	 */
	public Node getScene()
	{
		return iScene;
	}

	/**
	 * Set the scene to render.
	 * @param scene the scene. If null then no scene is rendered.
	 */
	public void setScene(Node scene)
	{
		iScene = scene;
		refresh();
	}




	/**
	 * Get the current renderer.
	 * @return the current renderer. Default is null.
	 */
	public Renderer getRenderer()
	{
		return iRenderer;
	}
	
	/**
	 * Set the current renderer.
	 * 
	 * @param aRenderer the new renderer. Use null to disable rendering..
	 */	
	public void setRenderer(Renderer aRenderer)
	{
		iRenderer = aRenderer;
		refresh();
	}
	
	
	/**
	 * Get the current interaction handler for this window.
	 * @return the current interaction handler. Default is null.
	 */
	public InteractionHandler getInteractonHandler()
	{
		return iHandler;
	}
	
	
	/**
	 * Set the current interaction handler.
	 * 
	 * This window should now be considered as the the owner of this handler. 
	 * {@link InteractionHandler#reset()} will get called automatically.
	 * 
	 * @param aHandler the new interaction handler. Use null to disable 
	 * interaction handling.
	 */
	public void setInteractionHandler(InteractionHandler aHandler)
	{
		if (null != iHandler)
		{
			//AppBridge.getApp().removeKeyListener(iHandler);
			removeKeyListener(iHandler);
			removeMouseListener(iHandler);
			removeMouseMotionListener(iHandler);
			removeMouseWheelListener(iHandler);
			iHandler.associate(null);
		}
		
		iHandler = aHandler;
		
		if (null != iHandler)
		{
			iHandler.associate(this);
			iHandler.reset();
			
			//AppBridge.getApp().addKeyListener(iHandler);
			addKeyListener(iHandler);
			addMouseListener(iHandler);
			addMouseMotionListener(iHandler);
			addMouseWheelListener(iHandler);
		}
	}

	
	
	
	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		if (null == iRenderer || null == iCamera || null == iScene)
			return;
		
		if (!(aGraphics instanceof Graphics2D))
		{
			LOG.error("paintComponent: arg0 is not a Graphics2D instance");
			return;
		}
		
		iDimensions.width = getWidth();
		iDimensions.height = getHeight();
		
		iRenderer.setupViewport(iDimensions);
		iRenderer.setupCamera(iCamera);
		
		iRenderer.render(iScene, (Graphics2D)aGraphics);
		
		// show commands
		if (null != iHandler)
		{
			String commands[] = iHandler.getKeyAndMouseCommands();
			if (null != commands)
			{
				FontMetrics fontMetrics = aGraphics.getFontMetrics();
				
				int fontHeight = fontMetrics.getHeight() + 2;
				int rightEdge = iDimensions.width - 2;
				
				for (int i=0; i<commands.length; ++i)
				{
					byte[] b = commands[i].getBytes();
					aGraphics.drawString(commands[i],
							rightEdge - fontMetrics.bytesWidth(b, 0, b.length), 
							10 + (fontHeight * i));
				}
			}
		}
	}

	

	/**
	 * Re-render the scene.
	 */
	public void refresh()
	{
		AppBridge.getApp().refreshGui();
	}
}



