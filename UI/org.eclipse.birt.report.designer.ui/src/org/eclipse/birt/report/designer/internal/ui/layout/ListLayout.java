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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.AbstractHintLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Provide layout management for List element
 * 
 */
public class ListLayout extends AbstractHintLayout {

	/** The layout constraints */
	protected Map constraints = new HashMap();

	private static final int verticalSpan = 2;

	private String layoutPreference = DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT;

	/**
	 * Gets the layout preference
	 * 
	 * @return
	 */
	public String getLayoutPreference() {
		return layoutPreference;
	}

	public void setLayoutPreference(String layoutPreference) {
		this.layoutPreference = layoutPreference;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.draw2d.
	 * IFigure, int, int)
	 */
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		// Dimension dim = container.getSize().getCopy();
		Dimension dim = container.getClientArea().getCopy().getSize();

		dim.width = Math.max(container.getMinimumSize().width, wHint)
				- container.getBorder().getInsets(container).getWidth();

		int dealWith = -1;
		if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT.equals(layoutPreference)) {
			dealWith = dim.width;
		} else if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals(layoutPreference) && wHint > 0) {
			dealWith = wHint - container.getBorder().getInsets(container).getWidth();
			if (dealWith < 0) {
				dealWith = -1;
			}
		}

		List list = container.getChildren();
		int size = list.size();
		int width = 0;
		int height = 0;
		for (int i = 0; i < size; i++) {
			IFigure figure = (IFigure) list.get(i);
			Dimension prefSize = figure.getPreferredSize(dealWith, hHint);

			height = height + prefSize.height;
			width = Math.max(prefSize.width, width);
		}
		if (height > 0) {
			dim.height = height + container.getInsets().getHeight() + (size - 1) * verticalSpan;
		}
		if (width > 0) {
			dim.width = width + container.getBorder().getInsets(container).getWidth();
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
		List datas = new ArrayList();
		for (int i = 0; i < size; i++) {
			IFigure figure = (IFigure) list.get(i);
			ListData data = (ListData) getConstraint(figure);
			ListLayoutWorkingData workingData = new ListLayoutWorkingData(figure, data.order);
			datas.add(workingData);

		}
		Collections.sort(datas, new ListDataComparator());
		int height = 0;
		for (int i = 0; i < size; i++) {
			ListLayoutWorkingData data = (ListLayoutWorkingData) datas.get(i);
			IFigure figure = data.child;
			Dimension dim = figure.getPreferredSize(bounds.width, -1);
			data.bounds = new Rectangle(0, height, dim.width, dim.height);
			height = height + dim.height + verticalSpan;
			setBoundsOfChild(parent, figure, data.bounds);
		}
	}

	protected void setBoundsOfChild(IFigure parent, IFigure child, Rectangle bounds) {
		parent.getClientArea(Rectangle.SINGLETON);
		bounds.translate(Rectangle.SINGLETON.x, Rectangle.SINGLETON.y);

		if (!bounds.equals(child.getBounds())) {
			child.setBounds(bounds);
		}
	}

	static class ListLayoutWorkingData {

		public IFigure child;

		public int order;

		public Rectangle bounds;

		/**
		 * @param child
		 * @param order
		 */
		public ListLayoutWorkingData(IFigure child, int order) {
			super();
			this.child = child;
			this.order = order;
		}

		public Rectangle getBounds() {
			return bounds;
		}

		public void setBounds(Rectangle bounds) {
			this.bounds = bounds;
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
		for (int i = 0; i < size; i++) {
			IFigure figure = (IFigure) list.get(i);
			Dimension min = figure.getMinimumSize(wHint, hHint);
			height = height + min.height;
			if (min.width > width) {
				width = min.width;
			}
		}
		if (height > 0) {
			dim.height = height + container.getInsets().getHeight() + (size - 1) * verticalSpan;
		}

		dim.width = width + container.getInsets().getWidth();
		// dim.expand(container.getInsets().getWidth(),
		// container.getInsets().getHeight());
		return dim;
	}

	private static class ListDataComparator implements Comparator {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object o1, Object o2) {
			if (o1 instanceof ListLayoutWorkingData && o2 instanceof ListLayoutWorkingData) {
				return ((ListLayoutWorkingData) o1).order - ((ListLayoutWorkingData) o2).order;
			}
			return 0;
		}

	}
}