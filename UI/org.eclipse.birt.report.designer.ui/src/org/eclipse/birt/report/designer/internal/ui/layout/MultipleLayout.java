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

package org.eclipse.birt.report.designer.internal.ui.layout;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.TableFigure;
import org.eclipse.draw2d.AbstractHintLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;

/**
 * Layout the multiple figure.
 */

public class MultipleLayout extends AbstractHintLayout {

	private boolean needlayout = true;

	/**
	 * Mark dirty flag to trigger re-layout.
	 */
	public void markDirty() {
		needlayout = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.draw2d.
	 * IFigure, int, int)
	 */
	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		Rectangle rect = container.getParent().getClientArea().getCopy();
		List list = container.getChildren();
		if (list.size() == 0) {
			return Dimension.SINGLETON;
		}

		Figure child = (Figure) list.get(0);

		wHint = Math.max(-1, wHint - container.getInsets().getWidth());
		hHint = Math.max(-1, hHint - container.getInsets().getHeight());

		wHint = Math.max(wHint, rect.width - container.getInsets().getWidth());
		hHint = Math.max(hHint, rect.height - container.getInsets().getHeight());

		if (child instanceof TableFigure && needlayout) {
			IFigure tablePane = ((LayeredPane) ((LayeredPane) ((TableFigure) child).getContents())
					.getLayer(LayerConstants.PRINTABLE_LAYERS)).getLayer(LayerConstants.PRIMARY_LAYER);
			LayoutManager layoutManager = tablePane.getLayoutManager();

			((TableLayout) layoutManager).markDirty();
			container.getBounds().width = wHint;
			container.getBounds().height = hHint;
			// child.invalidateTree( );
			child.validate();

			// dim = getPreferredSize( container, wHint, hHint ).expand(
			// container.getInsets( ).getWidth( ), container.getInsets( ).getHeight( ) );;
			needlayout = false;
		}

		Dimension dim = child.getPreferredSize(wHint, hHint).expand(container.getInsets().getWidth(),
				container.getInsets().getHeight());

		return dim;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void layout(IFigure container) {
		List list = container.getChildren();
		for (int i = 0; i < list.size(); i++) {
			Figure child = (Figure) list.get(i);
			Dimension dim = child.getPreferredSize();
			Rectangle bounds = new Rectangle(container.getClientArea().x, container.getClientArea().y, dim.width,
					dim.height);
			if (!child.getBounds().equals(bounds)) {
				child.setBounds(bounds);
				// container.getBounds( ).width = bounds.width;
			}
		}

	}
}
