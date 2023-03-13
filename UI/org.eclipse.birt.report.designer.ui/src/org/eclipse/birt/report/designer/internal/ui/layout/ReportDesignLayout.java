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

import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.ReportDesignMarginBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractReportEditPart;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;

/**
 * Provides layout management for ReportDesign element. This class is extened
 * from ReportFlowLayout. The main behavior is similar with flowlayout but add
 * inline and block support.
 */

public class ReportDesignLayout extends AbstractPageFlowLayout {

	private boolean isAuto = false;
	private int maxWidth = -1;
	private int maxHeight = -1;

	/**
	 * The constructor.
	 *
	 * @param viewer
	 */
	public ReportDesignLayout(GraphicalEditPart owner) {
		super(owner);
	}

	private void initLayout() {
		maxHeight = -1;
		maxWidth = -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void layout(IFigure parent) {
		initLayout();
		super.layout(parent);

		ReportDesignMarginBorder border = (ReportDesignMarginBorder) parent.getBorder();
		Insets insets = border.getMarginInsets();

		Dimension extend = new Dimension(getInitInsets().getWidth(), getInitInsets().getHeight());
		Dimension prefSize = new Dimension(maxWidth, maxHeight).expand(extend);

		Rectangle bounds = parent.getBounds().getCopy();

		bounds.height = Math.max(
				prefSize.height + border.getTrueBorderInsets().getHeight() + border.getPaddingInsets().getHeight(),
				getInitSize().height);

		if (bounds.width < getInitSize().width) {
			bounds.width = getInitSize().width;
		}

		Result result = getReportBounds(bounds);
		bounds = result.reportSize;

		Rectangle rect = new Rectangle(0, 0, bounds.x + bounds.width + result.rightSpace,
				bounds.y + bounds.height + result.bottomSpace);

		// int contentWidth = prefSize.width - insets.getWidth() - getInitSize().width +
		// getInitInsets().getWidth();
		int contentWidth = maxWidth - getInitSize().width + getInitInsets().getWidth()
				+ border.getTrueBorderInsets().left + border.getPaddingInsets().left;
		if (insets.right < contentWidth) {
			ReportDesignMarginBorder reportDesignMarginBorder = new ReportDesignMarginBorder(
					new Insets(insets.top, insets.left, insets.bottom, contentWidth));
			reportDesignMarginBorder.setBackgroundColor(border.getBackgroundColor());

			// parent.setBorder( reportDesignMarginBorder );
			getAbstractReportEditPart().refreshMarginBorder(reportDesignMarginBorder);
			bounds.width = bounds.width + contentWidth - insets.right;
		} else if (getInitInsets().right > contentWidth && insets.right != getInitInsets().right) {
			ReportDesignMarginBorder reportDesignMarginBorder = new ReportDesignMarginBorder(
					new Insets(insets.top, insets.left, insets.bottom, getInitInsets().right));
			reportDesignMarginBorder.setBackgroundColor(border.getBackgroundColor());
			// parent.setBorder( reportDesignMarginBorder );
			getAbstractReportEditPart().refreshMarginBorder(reportDesignMarginBorder);
			bounds.width = getInitSize().width;

		} else if (insets.right > contentWidth && getInitInsets().right < contentWidth) {
			ReportDesignMarginBorder reportDesignMarginBorder = new ReportDesignMarginBorder(
					new Insets(insets.top, insets.left, insets.bottom, contentWidth));
			reportDesignMarginBorder.setBackgroundColor(border.getBackgroundColor());
			// parent.setBorder( reportDesignMarginBorder );
			getAbstractReportEditPart().refreshMarginBorder(reportDesignMarginBorder);
			bounds.width = bounds.width + contentWidth - insets.right;
		}

		setViewProperty(rect, bounds);

		Rectangle temp = bounds.getCopy();
		temp.width = getInitSize().width;
		getOwner().getViewer().setProperty(DeferredGraphicalViewer.RULER_SIZE,
				new Rectangle(bounds.x, bounds.y, getInitSize().width, bounds.height));

		parent.setBounds(bounds);

		// parent.getParent( ).setSize( rect.getSize( ) );
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ReportFlowLayout#
	 * postLayoutRow(org.eclipse.birt.report.designer.internal.ui.layout.
	 * ReportFlowLayout.WorkingData)
	 */
	@Override
	void postLayoutRow(WorkingData data) {
//		ReportDesignMarginBorder border = (ReportDesignMarginBorder)getOwner( ).getFigure( ).getBorder( );
//		Insets insets = border.getTrueBorderInsets( );
//		if (data.rowY + insets.getHeight( ) > maxHeight)
//		{
//			maxHeight = data.rowY + insets.getHeight( );
//		}
//		if (data.rowWidth + insets.getWidth( ) > maxWidth )
//		{
//			maxWidth = data.rowWidth + insets.getWidth( );
//		}

		if (data.rowY > maxHeight) {
			maxHeight = data.rowY;
		}
		if (data.rowWidth > maxWidth) {
			maxWidth = data.rowWidth;
		}
	}

	/**
	 * @return
	 */
	public boolean isAuto() {
		return isAuto;
	}

	/**
	 * @param isAuto
	 */
	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	private AbstractReportEditPart getAbstractReportEditPart() {
		return (AbstractReportEditPart) getOwner();
	}
}
