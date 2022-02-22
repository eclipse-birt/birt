/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.property;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage;
import org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPageContainer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * TODO: Please document
 *
 * @version $Revision: 1.2 $ $Date: 2006/06/15 07:40:09 $
 */

public class PropertyNode {

	private transient ArrayList subNodes = null;

	private transient String id = null;

	private transient String nodeLabel = null;

	private transient Image nodeImage = null;

	private transient IPropertyPage page = null;

	private transient Control pageControl = null;

	public PropertyNode(String nodeId) {
		id = nodeId;
	}

	/**
	 * @param id The id to set.
	 */
	public final void setId(String id) {
		this.id = id;
	}

	/**
	 * @param labelImage The labelImage to set.
	 */
	public final void setNodeImage(Image labelImage) {
		this.nodeImage = labelImage;
	}

	/**
	 * @param labelText The labelText to set.
	 */
	public final void setNodeLabel(String labelText) {
		this.nodeLabel = labelText;
	}

	/**
	 * @param page The page to set.
	 */
	public final void setPage(IPropertyPage page) {
		assert (page != null) : " page is null"; //$NON-NLS-1$
		this.page = page;
	}

	public PropertyNode(String nodeId, String label) {
		this(nodeId);
		nodeLabel = label;
	}

	public PropertyNode(String nodeId, String label, Image image) {
		this(nodeId, label);
		nodeImage = image;
	}

	public PropertyNode(String nodeId, String label, Image image, IPropertyPage page) {
		this(nodeId, label, image);
		assert (page != null) : " page is null"; //$NON-NLS-1$
		this.page = page;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.PropertyNode#getId()
	 */
	public final String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.PropertyNode#getLabelText()
	 */
	public final String getNodeLabel() {
		if (nodeLabel == null) {
			return page.getName();
		}
		return nodeLabel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.PropertyNode#getLabelImage()
	 */
	public final Image getNodeImage() {
		/*
		 * if ( nodeImage == null ) { return page.getImage( ); }
		 */
		return nodeImage;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.PropertyNode#getPage()
	 */
	public final IPropertyPage getPage() {
		return page;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.PropertyNode#add(org.eclipse.birt.report.
	 * designer.ui.PropertyNode)
	 */
	public final void add(PropertyNode node) {
		assert (node != null) : "Null node cannot be added"; //$NON-NLS-1$
		if (subNodes == null) {
			subNodes = new ArrayList(5);
		}
		subNodes.add(node);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.PropertyNode#remove(java.lang.String)
	 */
	public final PropertyNode remove(String id) {
		assert (id != null) : "Null id passed"; //$NON-NLS-1$
		if (subNodes == null) {
			return null;
		}
		for (int n = 0; n < subNodes.size(); n++) {
			PropertyNode subNode = (PropertyNode) subNodes.get(n);
			if (subNode.getId().equals(id)) {
				if (subNodes.remove(subNode)) {
					return subNode;
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.PropertyNode#getSubNode(java.lang.String)
	 */
	public final PropertyNode getSubNode(String id) {
		assert (id != null) : "Null id passed"; //$NON-NLS-1$
		if (subNodes == null) {
			return null;
		}
		Iterator iter = subNodes.iterator();
		while (iter.hasNext()) {
			PropertyNode subNode = (PropertyNode) iter.next();
			if (subNode.getId().equals(id)) {
				return subNode;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.PropertyNode#getSubNodes()
	 */
	public final PropertyNode[] getSubNodes() {
		if (subNodes == null) {
			return null;
		}
		return (PropertyNode[]) subNodes.toArray(new PropertyNode[] {});
	}

	public final Control createPageControl(Composite parent) {
		assert (parent != null) : "parent composite is null"; //$NON-NLS-1$
		pageControl = page.createPageControl(parent);
		assert (pageControl != null) : "The page returned a null control"; //$NON-NLS-1$
		return pageControl;
	}

	public void removePageControl() {
		this.pageControl = null;
	}

	public final Control getPageControl() {
		return pageControl;
	}

	/**
	 * @return Returns the pageInitialized.
	 */
	public final boolean isPageControlCreated() {
		return (pageControl != null);
	}

	public final boolean hasSubNodes() {
		return (subNodes != null && subNodes.size() > 0);
	}

	public final void setContainer(IPropertyPageContainer container) {
		assert (page != null) : "There is no page present"; //$NON-NLS-1$
		page.setContainer(container);
	}
}
