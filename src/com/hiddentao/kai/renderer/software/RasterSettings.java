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

import java.awt.Graphics2D;

import com.hiddentao.kai.geometry.Dimensions2D;

/**
 * Settings for the rasteriser.
 */
class RasterSettings
{
	/**
	 * Viewport size.
	 */
	public Dimensions2D iViewportDimensions = new Dimensions2D();
	/**
	 * Whether Z-buffer is enabled or not.
	 */
	public boolean iZBufferEnabled = false;
	/**
	 * Whether wireframe mode is enabled or not.
	 */
	public boolean iWireframeModeEnabled = false;
	/**
	 * The graphics device context to use
	 */
	public Graphics2D iGraphics = null;
}

