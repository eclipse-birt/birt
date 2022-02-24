/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.layout;

import java.util.Hashtable;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.layout.ReportItemConstraint;
import org.eclipse.draw2d.AbstractHintLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 
 */

public class FirstCellLayout extends AbstractHintLayout {
	private Hashtable constraints = new Hashtable();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.AbstractHintLayout#calculateMinimumSize(org.eclipse.draw2d
	 * .IFigure, int, int)
	 */
	protected Dimension calculateMinimumSize(IFigure container, int wHint, int hHint) {
		if (wHint > -1)
			wHint = Math.max(0, wHint - container.getInsets().getWidth());
		if (hHint > -1)
			hHint = Math.max(0, hHint - container.getInsets().getHeight());

		List list = container.getChildren();
		// Rectangle rect = container.getClientArea( );
		Dimension retValue = new Dimension();
		Rectangle contraint = getChildContraint(container);
		for (int i = 0; i < list.size(); i++) {
			Figure child = (Figure) list.get(i);
			Dimension min = child.getMinimumSize((wHint - contraint.width) > 0 ? wHint - contraint.width : -1, hHint);
			retValue.width = retValue.width + min.width;
			retValue.height = Math.max(retValue.height, min.height);
		}
		retValue.width += container.getInsets().getWidth();
		retValue.height += container.getInsets().getHeight();
		// retValue.union( getBorderPreferredSize( container ) );
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.draw2d.
	 * IFigure, int, int)
	 */
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		if (wHint > -1)
			wHint = Math.max(0, wHint - container.getInsets().getWidth());
		if (hHint > -1)
			hHint = Math.max(0, hHint - container.getInsets().getHeight());
		List list = container.getChildren();
		// Rectangle rect = container.getClientArea( );
		Dimension retValue = new Dimension();
		Rectangle contraint = getChildContraint(container);
		for (int i = 0; i < list.size(); i++) {
			Figure child = (Figure) list.get(i);
			Dimension min = child.getPreferredSize((wHint - contraint.width) > 0 ? wHint - contraint.width : -1, hHint);
			retValue.width = retValue.width + min.width;
			retValue.height = Math.max(retValue.height, min.height);
		}
		retValue.width += container.getInsets().getWidth();
		retValue.height += container.getInsets().getHeight();
		// retValue.union( getBorderPreferredSize( container ) );
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	public void layout(IFigure container) {
		List list = container.getChildren();
		Rectangle rect = container.getClientArea();
		Rectangle contraint = getChildContraint(container);

		for (int i = 0; i < list.size(); i++) {
			Figure child = (Figure) list.get(i);
			if (constraints.get(child) == null) {
				child.setBounds(new Rectangle(rect.x, rect.y, rect.width - contraint.width, rect.height));
			} else {
				child.setBounds(
						new Rectangle(rect.x + rect.width - contraint.width, rect.y, contraint.width, rect.height));
			}
		}
	}

	private Rectangle getChildContraint(IFigure figure) {
		List list = figure.getChildren();

		Rectangle contraint = new Rectangle();
		for (int i = 0; i < list.size(); i++) {
			Figure child = (Figure) list.get(i);
			if (constraints.get(child) != null) {
				contraint = (Rectangle) constraints.get(child);
			}
		}
		return contraint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.AbstractLayout#getConstraint(org.eclipse.draw2d.IFigure)
	 */
	public Object getConstraint(IFigure child) {
		return constraints.get(child);
	}

	/**
	 * Sets the layout constraint of the given figure. The constraints can only be
	 * of type {@link ReportItemConstraint}.
	 * 
	 * @see LayoutManager#setConstraint(IFigure, Object)
	 */
	public void setConstraint(IFigure figure, Object newConstraint) {
		super.setConstraint(figure, newConstraint);
		if (newConstraint != null) {
			// store the constraint in a HashTable
			constraints.put(figure, newConstraint);

		}
	}
}
