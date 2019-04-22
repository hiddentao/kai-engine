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

import java.awt.Color;
import java.awt.Graphics2D;

import com.hiddentao.kai.geometry.Dimensions2D;
import com.hiddentao.kai.nodes.Node;
import com.hiddentao.kai.nodes.NodeVisitor;




/**
 * A 3D world renderer.
 */
public abstract class Renderer extends NodeVisitor
{
	/**
	 * Setup the rendering viewport.
	 * 
	 * This always gets called before {@link #setupCamera(Camera)} has been 
	 * called.
	 * 
	 * @param aViewport the viewport dimensions.
	 */
	protected abstract void setupViewport(Dimensions2D aViewport);
	

	/**
	 * Setup the rendering camera.
	 * 
	 * This always gets called after {@link #setupViewport(Dimensions2D)} has 
	 * been called.
	 * 
	 * @param aCam the camera. If null then is unused.
	 */
	protected abstract void setupCamera(Camera aCam);

	
	/**
	 * Render a frame.
	 * 
	 * @param aRoot the root node of the scenegraph. May be null.
	 * @param aGraphics the graphics context.
	 */
	public abstract void render(Node aRoot, Graphics2D aGraphics);  
	
	
	/**
	 * Set the background colour of the rendering viewport.
	 * @param aColor a non-null colour.
	 */
	public abstract void setBackgroundColor(Color aColor);
	
	
	/**
	 * Enable or disable backface culling.
	 * @param aVal true to enable; false to disable. It's 
	 * enabled by default.
	 */
	public abstract void enableBackfaceCulling(boolean aVal);

	
	/**
	 * Get whether backface culling is enabled or not.
	 * @return true if enabled; false otherwise.
	 */
	public abstract boolean isBackfaceCullingEnabled();

	
	/**
	 * Enable or disable wireframe mode.
	 * @param aVal true to enable; false to disable. It's 
	 * disabled by default.
	 */
	public abstract void enableWireframeMode(boolean aVal);	
	
	
	
	/**
	 * Get whether wireframe mode is enabled or not.
	 * @return true if enabled; false otherwise.
	 */
	public abstract boolean isWireframeModeEnabled();
	
	
	
	/**
	 * Enable or disable per-pixel Z-buffer.
	 * @param aVal true to enable; false to disable. It's 
	 * enabled by default.
	 */
	public abstract void enableZBuffer(boolean aVal);	
	
	
	
	/**
	 * Get whether per-pixel Z-buffer is enabled or not.
	 * @return true if enabled; false otherwise.
	 */
	public abstract boolean isZBufferEnabled();
	

	
}



