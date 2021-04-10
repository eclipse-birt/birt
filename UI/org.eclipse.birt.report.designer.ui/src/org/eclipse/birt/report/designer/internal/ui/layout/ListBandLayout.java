/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.layout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportShowFigure;
import org.eclipse.draw2d.AbstractHintLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The layout manage provides layout management for list band.
 * <P>
 */
public class ListBandLayout extends AbstractHintLayout {

	/** The layout constraints */
	protected Map constraints = new HashMap();

	private static final int verticalSpan = 1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.draw2d.
	 * IFigure, int, int)
	 */
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		Dimension dim = container.getSize().getCopy();
		if (wHint > 0) {
			dim.width = wHint;
		}
		List list = container.getChildren();
		int size = list.size();
		int width = 0;
		int height = 0;

		ReportShowFigure showFigure = null;
		if (container instanceof ReportShowFigure) {
			showFigure = (ReportShowFigure) container;
		}
		for (int i = 0; i < size; i++) {

			IFigure figure = (IFigure) list.get(i);
			if (showFigure == null || showFigure.getContent() != figure || showFigure.isControlShowing()) {
				Dimension prefSize = figure.getPreferredSize(wHint, hHint);

				height = height + prefSize.height;
				width = Math.max(prefSize.width, width);
			}
		}
		if (height > 0) {
			dim.height = height + container.getInsets().getHeight() + (size - 1) * verticalSpan;
		}
		if (width > 0) {
			dim.width = width + container.getInsets().getWidth();
		}
		return dim;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	public void layout(IFigure parent) {
		Rectangle bounds = parent.getClientArea().getCopy();
		List list = parent.getChildren();
		int size = list.size();
		int height = 0;

		ReportShowFigure showFigure = null;
		if (parent instanceof ReportShowFigure) {
			showFigure = (ReportShowFigure) parent;
		}
		for (int i = 0; i < size; i++) {
			IFigure figure = (IFigure) list.get(i);
			if (showFigure != null && showFigure.getContent() == figure && !showFigure.isControlShowing()) {
				setBoundsOfChild(parent, figure, new Rectangle(0, height, 0, 0));
			} else {
				Dimension dim = figure.getPreferredSize(bounds.width, -1);
				setBoundsOfChild(parent, figure, new Rectangle(0, height, dim.width, dim.height));
				height = height + dim.height + verticalSpan;
			}

		}

	}

	protected void setBoundsOfChild(IFigure parent, IFigure child, Rectangle bounds) {
		parent.getClientArea(Rectangle.SINGLETON);
		bounds.translate(Rectangle.SINGLETON.x, Rectangle.SINGLETON.y);

		if (!bounds.equals(child.getBounds())) {
			child.setBounds(bounds);
		}
	}

	/**
	 * @see LayoutManager#getConstraint(IFigure)
	 */
	public Object getConstraint(IFigure figure) {
		return constraints.get(figure);
	}

	/**
	 * @see LayoutManager#remove(IFigure)
	 */
	public void remove(IFigure figure) {
		super.remove(figure);
		constraints.remove(figure);
	}

	/**
	 * Sets the layout constraint of the given figure. The constraints can only be
	 * of type {@link Rectangle}.
	 * 
	 * @see LayoutManager#setConstraint(IFigure, Object)
	 */
	public void setConstraint(IFigure figure, Object newConstraint) {
		super.setConstraint(figure, newConstraint);
		if (newConstraint != null)
			constraints.put(figure, newConstraint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.AbstractHintLayout#calculateMinimumSize(org.eclipse.draw2d
	 * .IFigure, int, int)
	 */
	protected Dimension calculateMinimumSize(IFigure container, int wHint, int hHint) {
		Dimension dim = new Dimension();

		List list = container.getChildren();
		int size = list.size();
		int height = 0;
		int width = 0;

		ReportShowFigure showFigure = null;
		if (container instanceof ReportShowFigure) {
			showFigure = (ReportShowFigure) container;
		}
		for (int i = 0; i < size; i++) {

			IFigure figure = (IFigure) list.get(i);
			if (showFigure == null || showFigure.getContent() != figure || showFigure.isControlShowing()) {
				Dimension min = figure.getMinimumSize(wHint, hHint);
				height = height + min.height;
				if (min.width > width) {
					width = min.width;
				}
			}
		}
		if (height > 0) {
			dim.height = height + container.getInsets().getHeight() + (size - 1) * verticalSpan;
		}
		dim.width = width + container.getInsets().getWidth();
		return dim;
	}

}