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

package com.hiddentao.kai.renderer.interaction;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import com.hiddentao.kai.renderer.RenderingSurface;


/**
 * An object which handles user-interaction with a {@link RenderingSurface}.
 */
public abstract class InteractionHandler implements	KeyListener, 
													MouseListener, 
													MouseMotionListener, 
													MouseWheelListener
{
	/** The rendering surface with which this handler is currently associated. */
	protected RenderingSurface iSurface = null;
	
	
	/**
	 * Associate a rendering surface with this handler.
	 * 
	 * This gets called from {@link RenderingSurface#setInteractionHandler(InteractionHandler)}.
	 * 
	 * @param aSurface the rendering surface. May be null.
	 */
	public final void associate(RenderingSurface aSurface)
	{
		iSurface = aSurface;
		reset();
	}
	
	
	/**
	 * Reset this handler's internal state.
	 *
	 * This internally calls {@link #doReset()} followed by 
	 * {@link #refreshDisplay()}.
	 */
	public final void reset()
	{
		doReset();
		refreshDisplay();
	}
	
	
	/**
	 * Reset this handler's internal state.
	 *
	 * This should reset any and all internaly tracking variables..
	 */
	protected abstract void doReset();
	
	
	/**
	 * Refresh the displayed scene.
	 */
	protected abstract void refreshDisplay();
	
	
	/**
	 * Get a textual description of keyboard and mouse commands to show to 
	 * the user.
	 * @return a list of strings, each to be shown on a separate line. If 
	 * null then there are no commands for this interaction handler.
	 */
	public abstract String[] getKeyAndMouseCommands();
}


