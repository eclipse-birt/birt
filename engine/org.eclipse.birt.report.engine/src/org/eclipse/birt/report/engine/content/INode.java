/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content;

import java.util.Iterator;

/**
 * Tree interface.
 * 
 * implement memory management policy. The node may exist in disk or keep in the
 * memory.
 */
public interface INode {

	/**
	 * Get the parent of the node, or return null if the node is in tree top level.
	 * 
	 * @return the parent of the node.
	 */
	INode getParent();

	/**
	 * Set the parent of the node.
	 * 
	 * @param parent the parent of the node.
	 */
	void setParent(INode parent);

	/**
	 * Get the sibling node immediately preceding the specified node.
	 * 
	 * @return the sibling node immediately preceding the specified node.
	 */
	INode getPrevious();

	/**
	 * Set the sibling node immediately preceding the specified node.
	 * 
	 * @param previous the sibling node immediately preceding the specified node.
	 */
	void setPrevious(INode previous);

	/**
	 * Get the sibling node immediately following the specified node.
	 * 
	 * @return the sibling node immediately following the specified node.
	 */
	INode getNext();

	/**
	 * Set the sibling node immediately following the specified node.
	 * 
	 * @param next the sibling node immediately following the specified node.
	 */
	void setNext(INode next);

	/**
	 * Append a child to this node.
	 * 
	 * @param child the child need to be appended.
	 */
	void appendChild(INode child);

	/**
	 * Get the children of the node.
	 * 
	 * @return the children of the node.
	 */
	Iterator getChildren();

	/**
	 * Remove all children of the node.
	 */
	void removeChildren();
}
