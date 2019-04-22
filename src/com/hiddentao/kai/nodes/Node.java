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

package com.hiddentao.kai.nodes;

import java.util.HashSet;
import java.util.Iterator;


/**
 * Represents a node in a scene graph.
 */
public abstract class Node
{
	/**
	 * Whether the node and its children are enabled for rendering.
	 */
	private boolean iIsEnabled = true;
	
	/**
	 * The parent of this node.
	 */
	private Node iParent = null;
	
	/**
	 * This children of this node.
	 */
	private HashSet<Node> iChildren = new HashSet<Node>();
	
	
	
	/**
	 * Empty constructor. 
	 */
	public Node() {}
	
	
	/**
	 * Constructor.
	 * @param aParent the node to set as this new node's parent.
	 */
	public Node(Node aParent)
	{
		iParent = aParent;
	}
	
	
	
	/**
	 * Change this node's parent.
	 * @param aNewParent the new parent to set for this node. Can be null.
	 */
	private void setParent(Node aNewParent)
	{
		iParent = aNewParent;
	}
	
	
	
	/**
	 * Get this parent of this node.
	 * @return the parent node, or null if this node has no parent.
	 */
	public Node parent() 
	{
		return iParent;
	}
	
	
	/**
	 * Get whether this node and its chilren are enabled for rendering.
	 * @return true if so; false otherwise.
	 */
	public boolean isEnabled()
	{
		return iIsEnabled;
	}
	
	
	/**
	 * Set whether this node and its chilren are enabled for rendering.
	 * @param aEnabled true to enable it; false to disable it.
	 */
	public void setEnabled(boolean aEnabled)
	{
		iIsEnabled = aEnabled;
	}
	
	
	/**
	 * Get an iterator over this node's children.
	 * @return an iterator.
	 */
	public Iterator<Node> children()
	{
		return iChildren.iterator();
	}
	
	
	/**
	 * Get the no. of children this node has.
	 * @return a value >= 0.
	 */
	public int numberOfChildren()
	{
		return iChildren.size();
	}
	
	
	/**
	 * Attach a child node to this one.
	 * @param aChild the child node to attach. If null then this function 
	 * simply returns. The child's parent is set to this node.
	 */
	public void attachChild(Node aChild)
	{
		if (null != aChild)
		{
			iChildren.add(aChild);
		}
	}
	
	
	
	/**
	 * Detach a child node from this one.
	 * 
	 * @param aChild the child node to dettach. The child's parent is set 
	 * to null.
	 * 
	 * @return the detached child if it was indeed a child of this node; 
	 * otherwise null. If the given child is null then null is returned.
	 */
	public Node detachChild(Node aChild)
	{
		if (null != aChild)
		{
			if (iChildren.remove(aChild))
			{
				aChild.setParent(null);
				return aChild;
			}
		}
		
		return null;
	}
	
	
	
	/**
	 * Detach all child nodes from this one.
	 */
	public void detachAllChildren()
	{
		for (Node child : iChildren)
		{
			if (iChildren.remove(child))
				child.setParent(null);
		}
	}
	
	
	
	
	/**
	 * Process this node, assuming that we are traversing up the 
	 * scenegraph tree.
	 * 
	 * Note: if this node is disabled then this call has no effect.
	 * 
	 * @param aVisitor the visitor which wishes to process this node.
	 */
	public void visitDown(NodeVisitor aVisitor)
	{
		if (iIsEnabled)
		{
			doVisitDown(aVisitor);
		}
	}
	

	/**
	 * Process this node, assuming that we are traversing up the 
	 * scenegraph tree.
	 * 
	 * Note: if this node is disabled then this call has no effect.
	 * 
	 * @param aVisitor the visitor which wishes to process this node.
	 */
	public void visitUp(NodeVisitor aVisitor)
	{
		if (iIsEnabled)
		{
			doVisitUp(aVisitor);
		}
	}


	
	/**
	 * Process this node, assuming that we are traversing up the 
	 * scenegraph tree.
	 * 
	 * This function should call the appropriate 'processs' method on the 
	 * given {@link NodeVisitor}.
	 * 
	 * @param aVisitor the visitor which wishes to process this node.
	 */	
	protected abstract void doVisitDown(NodeVisitor aVisitor);
	
	
	/**
	 * Process this node, assuming that we are traversing up the 
	 * scenegraph tree.
	 * 
	 * This function should call the appropriate 'processs' method on the 
	 * given {@link NodeVisitor}.
	 * 
	 * @param aVisitor the visitor which wishes to process this node.
	 */
	protected abstract void doVisitUp(NodeVisitor aVisitor);
	
	
}

