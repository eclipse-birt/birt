/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.views;

import org.eclipse.gef.Request;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

/**
 * Interface to deal with the tree node
 */
public interface INodeProvider {

	public final static String SUF_INVALID = "Invalid"; //$NON-NLS-1$

	/**
	 * Creates the menu based on the given objects
	 * 
	 * @param sourceViewer the source viewer
	 * @param object       the selection of the objects which the menu is based on
	 * @param menu         the menu manager
	 */

	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu);

	/**
	 * Gets the display name of the node
	 * 
	 * @param model the model of the node
	 * @return Returns the display name for the node
	 */
	public String getNodeDisplayName(Object model);

	/**
	 * Gets the icon image for the node
	 * 
	 * @param model the model of the node
	 * @return Returns the icon image for the node
	 */
	public Image getNodeIcon(Object model);

	/**
	 * Gets the tooltip of the node
	 * 
	 * @param model the model of the node
	 * @return Returns the tooltip name for the node, or null if no tooltip is
	 *         needed.
	 */
	public String getNodeTooltip(Object model);

	/**
	 * Gets the given model object's children
	 * 
	 * @param object report element object
	 * @return an array contained all children of the given model
	 */
	public Object[] getChildren(Object object);

	/**
	 * Returns if the given model object has children
	 * 
	 * @param object report element object
	 * @return if the given model object has children
	 */
	public boolean hasChildren(Object object);

	/**
	 * Gets container of the given model object
	 * 
	 * @param model report element object
	 * @return the element's container
	 */
	public Object getParent(Object model);

	/**
	 * Gets the command to process the specified request for the element
	 * 
	 * @param model   the element to process
	 * @param request the request to process
	 * @return Returns true if the request was performed, otherwise false
	 * @throws Exception
	 */

	public boolean performRequest(Object model, Request request) throws Exception;

	public boolean isReadOnly(Object model);
}
