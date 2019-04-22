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

import com.hiddentao.kai.geometry.Angles3D;
import com.hiddentao.kai.geometry.Vec4;
import com.hiddentao.kai.geometry.builders.CubeBuilder;
import com.hiddentao.kai.geometry.util.ColorUtils;
import com.hiddentao.kai.logging.Logger;
import com.hiddentao.kai.math.MathConstants;
import com.hiddentao.kai.nodes.Mesh;
import com.hiddentao.kai.nodes.NodeGroup;
import com.hiddentao.kai.nodes.Rotation;
import com.hiddentao.kai.nodes.Translation;
import com.hiddentao.kai.renderer.RenderingSurface;
import com.hiddentao.kai.timer.TimerControl;
import com.hiddentao.kai.timer.TimerEvent;
import com.hiddentao.utils.collections.DynamicArray;


/**
 * An animated scene.
 */
public final class AnimScene extends NodeGroup implements TimerEvent
{
	private static Logger LOG = Logger.getLogger(AnimScene.class.getName());
	
	private static final float PIECE_WIDTH = 0.3f;
	
	
	private volatile DynamicArray<Rotation> iRotations = new DynamicArray<Rotation>(4);
	private volatile DynamicArray<Angles3D> iAngleIncrements = new DynamicArray<Angles3D>(4);
	
	
	private volatile RenderingSurface iSurface = null;
	
	
	/**
	 * Constructor.
	 * @param aSurface the active rendering surface.
	 */
	private AnimScene(RenderingSurface aSurface)
	{
		iSurface = aSurface;
	}
	

	
	/**
	 * Construct an instance of this animated scene.
	 * 
	 * @param aSurface the rendering surface.
	 * @return the constructed scene.
	 */
	public static AnimScene construct(RenderingSurface aSurface)
	{
		AnimScene a = new AnimScene(aSurface);
		
		// setup angle increments for each piece
		a.iAngleIncrements.add(new Angles3D(MathConstants.DEG_TO_RAD,0,0));
		a.iAngleIncrements.add(new Angles3D(0,MathConstants.DEG_TO_RAD,0));
		a.iAngleIncrements.add(new Angles3D(0,0,MathConstants.DEG_TO_RAD));
		a.iAngleIncrements.add(new Angles3D(MathConstants.DEG_TO_RAD,MathConstants.DEG_TO_RAD,MathConstants.DEG_TO_RAD));
		
		// construct each piece
		for (int i=0; i<4; ++i)
		{
			a.iRotations.add(new Rotation());
			a.addPiece(new Translation(new Vec4((i-1.5f)*PIECE_WIDTH*2,0,0,1)), a.iRotations.get(i));
		}

		TimerControl.getInstance().add(a, 10);
		
		return a;
	}
	
	
	
	
	/**
	 * Construct and add a piece to this scene.
	 * 
	 * @param aTranslation the translation of the piece from the origin.
	 * @param aRotation the rotation of the piece about its origin.
	 */
	private void addPiece(Translation aTranslation, Rotation aRotation)
	{
		Mesh m = CubeBuilder.buildMesh(new Vec4(0,0,0,1), PIECE_WIDTH);
		m.setColor(ColorUtils.getRandomBrightColor());
		aRotation.attachChild(m);
		aTranslation.attachChild(aRotation);
		this.attachChild(aTranslation);			
	}
	
	
	
	
	public void doTimerEvent()
	{
		// if we're showing then repaint
		if (null != iSurface)
		{
			for (int i=0; i<4; ++i)
			{
				Angles3D angles = iRotations.get(i).angles;
				Angles3D inc = iAngleIncrements.get(i);
				angles.incX(inc.iAngleX, MathConstants.MAX_RADIANS);
				angles.incY(inc.iAngleY, MathConstants.MAX_RADIANS);
				angles.incZ(inc.iAngleZ, MathConstants.MAX_RADIANS);
			}
			iSurface.refresh();
		}
	}

	public String getTimerEventDescription()
	{
		return "Animated scene";
	}
	
}
