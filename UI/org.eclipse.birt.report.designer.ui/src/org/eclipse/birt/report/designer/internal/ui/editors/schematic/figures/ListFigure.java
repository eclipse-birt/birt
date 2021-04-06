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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.SectionBorder;
import org.eclipse.birt.report.designer.internal.ui.layout.IFixLayoutHelper;
import org.eclipse.birt.report.designer.internal.ui.layout.ListLayout;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * List item figure
 * 
 */
public class ListFigure extends ReportElementFigure implements IFixLayoutHelper {

	private static final String BORDER_TEXT = Messages.getString("ListFigure.BORDER_TEXT"); //$NON-NLS-1$

	/** the dirty flag */
	private boolean dirty = true;

	private Dimension recommendSize = new Dimension();

	public ListFigure() {
		SectionBorder border = new SectionBorder();
		border.setIndicatorLabel(BORDER_TEXT);
		border.setIndicatorIcon(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_LIST));
		setBorder(border);
		setLayoutManager(new ListLayout());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		// graphics.drawRectangle(getBounds().getCopy().shrink(2,2));
	}

	/**
	 * Marks dirty flag on all children of this list.
	 * 
	 * @param flag the flag to mark.
	 */
	public void markDirtyTree(boolean flag) {
		markDirtyTree(this, flag);
	}

	/**
	 * Marks dirty flag on all children with the specified container.
	 * 
	 * @param container the container to mark.
	 * @param flag      the flag to mark.
	 */
	public void markDirtyTree(IFigure container, boolean flag) {
		if (container instanceof ListFigure) {
			((ListFigure) container).markDirty(flag);
		}

		Collection children = container.getChildren();

		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			Object child = iterator.next();

			if (child instanceof IFigure) {
				markDirtyTree((IFigure) child, flag);
			}
		}
	}

	/**
	 * Marks dirty flag.
	 * 
	 * @param flag the flag to mark.
	 */
	public void markDirty(boolean flag) {
		dirty = flag;
	}

	/**
	 * Returns <code>true</code> if need layout, <code>false</code> otherwise.
	 */
	public boolean isDirty() {
		return dirty;
	}

	public void setRecommendSize(Dimension recommendSize) {
		this.recommendSize = recommendSize;
	}

	@Override
	public Dimension getFixPreferredSize(int w, int h) {
		Dimension size;
		if (recommendSize.width > 0) {
			size = getPreferredSize(recommendSize.width, h);
		} else {
			size = getPreferredSize(w, h);
		}

		int width = size.width;
		int height = size.height;
		if (recommendSize.width > 0) {
			width = recommendSize.width;
		}

		return new Dimension(width, height);
	}

	@Override
	public Dimension getFixMinimumSize(int w, int h) {
		Dimension size;
		if (recommendSize.width > 0) {
			size = getPreferredSize(recommendSize.width, h);
		} else {
			size = getPreferredSize(w, h);
		}
		int width = size.width;
		int height = size.height;
		if (recommendSize.width > 0) {
			width = recommendSize.width;
		}

		return new Dimension(width, height);
	}
}
