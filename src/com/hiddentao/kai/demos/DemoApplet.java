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

import java.util.ArrayList;

import javax.swing.applet.JAbstractApplet;

import com.hiddentao.kai.app.App;
import com.hiddentao.kai.app.AppBridge;
import com.hiddentao.kai.geometry.Vec4;
import com.hiddentao.kai.geometry.VectorComponents;
import com.hiddentao.kai.geometry.builders.CubeBuilder;
import com.hiddentao.kai.geometry.util.ColorUtils;
import com.hiddentao.kai.logging.Level;
import com.hiddentao.kai.logging.Logger;
import com.hiddentao.kai.nodes.Mesh;
import com.hiddentao.kai.nodes.Node;
import com.hiddentao.kai.nodes.NodeGroup;
import com.hiddentao.kai.nodes.Translation;
import com.hiddentao.kai.renderer.Camera;
import com.hiddentao.kai.renderer.Renderer;
import com.hiddentao.kai.renderer.RenderingSurface;
import com.hiddentao.kai.renderer.RenderingSystemManager;
import com.hiddentao.kai.utils.StaticDataManager;



/**
 * Main entry point of demos.
 */
public class DemoApplet extends JAbstractApplet implements App, VectorComponents
{
	@Override
	public void start()
	{
		super.start();
		
		Logger.setMinimumLevel(Level.INFO);
		
		// set global content pane
		AppBridge.setApp(this);

		// setup renderer and surface
		RenderingSurface surface = new RenderingSurface();
		Renderer renderer = RenderingSystemManager.instance().getDefaultRenderSystem().renderer();
		renderer.enableBackfaceCulling(true);
		renderer.enableWireframeMode(false);
		renderer.enableZBuffer(true);
		surface.setRenderer(renderer);

		// camera
		Camera cam = new Camera();
		cam.setOrthographic(false);
		surface.setCamera(cam);

		// scenes
		ArrayList<Node> scenes = new ArrayList<Node>();
		scenes.add(constructScene1());
		scenes.add(constructScene2());
		scenes.add(AnimScene.construct(surface));
		
		surface.setInteractionHandler(new DemoInteractionHandler(scenes));
		
		// show the surface
		this.getContentPane().add(surface);
		
		// focus on rendered view
		surface.requestFocusInWindow();
	}


	@Override
	public void destroy()
	{
		super.destroy();
		StaticDataManager.resetAllStaticData();
	}


	/**
	 * Construct our scene.
	 * @return a {@link Mesh} node.
	 */
	private Mesh constructScene1()
	{
		/*
		 * Our scene will consist of a set of a single large cube.
		 */

	    Vec4 pieceOrigin = new Vec4(0,0,0,1);
		Mesh m = CubeBuilder.buildMesh( pieceOrigin, 1.0f );
		m.setColor( ColorUtils.getRandomBrightColor() );
		return m;
	}


	
	/**
	 * Construct our scene.
	 * @return a {@link NodeGroup} of {@link Translation} nodes, each of which 
	 * has a {@link Mesh} node as a child.
	 */
	private NodeGroup constructScene2()
	{
		/*
		 * Our scene will consist of a set of 27 cubes, each placed next 
		 * to each other such that together they form a single large cube.
		 */
		
		// length of a side of each cube
		final float BLOCK_WIDTH = 0.2f;
		
		NodeGroup group = new NodeGroup();
		
		Vec4 nextPieceTranslation = new Vec4();
		Vec4 pieceOrigin = new Vec4(0,0,0,1);
		
		for (int z=0; z<3; ++z)
		{
			nextPieceTranslation.val[_Z_] = (1 - z) * BLOCK_WIDTH; 

			for (int y=0; y<3; ++y)
			{
				nextPieceTranslation.val[_Y_] = (1 - y) * BLOCK_WIDTH; 

				for (int x=0; x<3; ++x)
				{
					nextPieceTranslation.val[_X_] = (x - 1) * BLOCK_WIDTH; 

					Translation t = new Translation(nextPieceTranslation);
					Mesh m = CubeBuilder.buildMesh(pieceOrigin, BLOCK_WIDTH);
					m.setColor(ColorUtils.getRandomBrightColor());
					t.attachChild(m);
					group.attachChild(t);
				}
			}
		}

		return group;
	}	
	
}


