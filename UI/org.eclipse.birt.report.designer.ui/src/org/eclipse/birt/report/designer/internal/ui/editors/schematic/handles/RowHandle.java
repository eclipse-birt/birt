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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles;

import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.RowHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.IContainer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.RowTracker;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * Row drag handle
 */
public class RowHandle extends AbstractHandle implements IContainer {

	private static final String TOOLTIP_TABLE_DETAIL = Messages.getString("RowHandle.tooltip.TableDetail"); //$NON-NLS-1$
	private static final String TOOLTIP_TABLE_HEADER = Messages.getString("RowHandle.tooltip.TableHeader"); //$NON-NLS-1$
	private static final String TOOLTIP_TABLE_FOOTER = Messages.getString("RowHandle.tooltip.TableFooter"); //$NON-NLS-1$
	private static final String TOOLTIP_GROUP_HEADER = Messages.getString("RowHandle.tooltip.GroupHeader"); //$NON-NLS-1$
	private static final String TOOLTIP_GROUP_FOOTER = Messages.getString("RowHandle.tooltip.GroupFooter"); //$NON-NLS-1$

	private static final String TOOLTIP_GRID_ROW = Messages.getString("RowHandle.tooltip.GridRow"); //$NON-NLS-1$

	private int rowNumber;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
	 */
	protected DragTracker createDragTracker() {
		return new RowTracker((TableEditPart) getOwner(), rowNumber, this);
	}

	/**
	 * Creates a MoveHandle for the given <code>GraphicalEditPart</code> using a
	 * default {@link Locator}.
	 * 
	 * @param owner The GraphicalEditPart to be moved by this handle.
	 */
	public RowHandle(TableEditPart owner, int number) {
		this(owner, new NothingLocator(owner.getFigure()), number);
	}

	/**
	 * Creates a MoveHandle for the given <code>GraphicalEditPart</code> using the
	 * given <code>Locator</code>.
	 * 
	 * @param owner The GraphicalEditPart to be moved by this handle.
	 * @param loc   The Locator used to place the handle.
	 */
	public RowHandle(TableEditPart owner, Locator loc, int number) {
		super(owner, loc);
		this.rowNumber = number;

		initialize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
	 */
	public boolean containsPoint(int x, int y) {

		return getBounds().getCopy().shrink(2, 2).contains(x, y);
	}

	/**
	 * @return height.
	 */
	public int getHeight() {
		TableEditPart part = (TableEditPart) getOwner();

		return HandleAdapterFactory.getInstance().getRowHandleAdapter(part.getRow(rowNumber)).getHeight();

	}

	/**
	 * Initializes the handle. Sets the {@link DragTracker}and DragCursor.
	 */
	protected void initialize() {
		setOpaque(true);
		LineBorder bd = new LineBorder(1);
		bd.setColor(ReportColorConstants.HandleBorderColor);
		setBorder(bd);

		String tp = getTooltipText();
		if (tp != null) {
			Label tooltip = new Label(tp);
			tooltip.setBorder(new MarginBorder(0, 2, 0, 2));
			setToolTip(tooltip);
		}

		setCursor(Cursors.ARROW);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		if (isSelect()) {
			graphics.setBackgroundColor(ReportColorConstants.SelctionFillColor);
		} else {
			graphics.setBackgroundColor(ReportColorConstants.TableGuideFillColor);
		}

		graphics.setLineStyle(SWT.LINE_SOLID);
		Rectangle bounds = getBounds().getCopy().resize(-1, -1);
		graphics.fillRectangle(bounds);

		Font font = FontManager.getFont("Dialog", 7, SWT.NORMAL);//$NON-NLS-1$

		graphics.setFont(font);

		Image image = getImage();
		if (image == null)
			return;

		graphics.setForegroundColor(ColorConstants.white);
		graphics.setXORMode(true);

		org.eclipse.swt.graphics.Rectangle rect = image.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		graphics.drawImage(image, x, y);

		TableEditPart part = (TableEditPart) getOwner();
		RowHandleAdapter rowHandleAdapter = HandleAdapterFactory.getInstance()
				.getRowHandleAdapter(part.getRow(getRowNumber()));
		String type = rowHandleAdapter.getType();
		String displayName = rowHandleAdapter.getDisplayName();

		if (TableHandleAdapter.TABLE_GROUP_HEADER.equals(type) || TableHandleAdapter.TABLE_GROUP_FOOTER.equals(type)) {
			graphics.drawString(displayName, x + rect.width + 2, y + 2);
		}

		graphics.setBackgroundColor(ColorConstants.black);

		// ReportFigureUtilities.paintBevel( graphics,
		// getBounds( ).getCopy( ),
		// true );

		graphics.setXORMode(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.
	 * IContainer#contains(org.eclipse.draw2d.geometry.Point)
	 */
	public boolean contains(Point pt) {
		return false;
	}

	/**
	 * Get row number.
	 * 
	 * @return row number
	 */
	public int getRowNumber() {
		return rowNumber;
	}

	/**
	 * Set row number.
	 * 
	 * @param rowNumber row number.
	 */
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	private String getTooltipText() {
		TableEditPart part = (TableEditPart) getOwner();
		RowHandleAdapter rha = HandleAdapterFactory.getInstance().getRowHandleAdapter(part.getRow(getRowNumber()));
		String type = rha.getType();
		if (TableHandleAdapter.TABLE_HEADER.equals(type)) {
			return TOOLTIP_TABLE_HEADER;
		} else if (TableHandleAdapter.TABLE_DETAIL.equals(type)) {
			return TOOLTIP_TABLE_DETAIL;
		} else if (TableHandleAdapter.TABLE_FOOTER.equals(type)) {
			return TOOLTIP_TABLE_FOOTER;
		} else if (TableHandleAdapter.TABLE_GROUP_HEADER.equals(type)) {
			Object obj = rha.getHandle().getContainer();
			String name = null;
			try {
				name = ExpressionUtil.getColumnBindingName(((TableGroupHandle) obj).getKeyExpr());
			} catch (BirtException e) {
			}
			if (obj instanceof TableGroupHandle && name != null) {
				return TOOLTIP_GROUP_HEADER + " (" + name + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				return TOOLTIP_GROUP_HEADER;
			}
		} else if (TableHandleAdapter.TABLE_GROUP_FOOTER.equals(type)) {
			Object obj = rha.getHandle().getContainer();
			String name = null;
			try {
				name = ExpressionUtil.getColumnBindingName(((TableGroupHandle) obj).getKeyExpr());
			} catch (BirtException e) {
			}
			if (obj instanceof TableGroupHandle && name != null) {
				return TOOLTIP_GROUP_FOOTER + " (" + name + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				return TOOLTIP_GROUP_FOOTER;
			}
		}

		return TOOLTIP_GRID_ROW;
	}

	private Image getImage() {
		TableEditPart part = (TableEditPart) getOwner();
		if (part.getRow(getRowNumber()) == null) {
			return null;
		}
		String type = HandleAdapterFactory.getInstance().getRowHandleAdapter(part.getRow(getRowNumber())).getType();
		if (TableHandleAdapter.TABLE_HEADER.equals(type)) {
			type = IReportGraphicConstants.ICON_NODE_HEADER;
		} else if (TableHandleAdapter.TABLE_DETAIL.equals(type)) {
			type = IReportGraphicConstants.ICON_NODE_DETAILS;
		} else if (TableHandleAdapter.TABLE_FOOTER.equals(type)) {
			type = IReportGraphicConstants.ICON_NODE_FOOTER;
		} else if (TableHandleAdapter.TABLE_GROUP_HEADER.equals(type)) {
			type = IReportGraphicConstants.ICON_NODE_GROUP_HEADER;
		} else if (TableHandleAdapter.TABLE_GROUP_FOOTER.equals(type)) {
			type = IReportGraphicConstants.ICON_NODE_GROUP_FOOTER;
		} else {
			return null;
		}
		return ReportPlatformUIImages.getImage(type);
	}

	/**
	 * Judges if the row is selected.
	 * 
	 * @return true if selected, else false.
	 */
	public boolean isSelect() {
		TableEditPart part = (TableEditPart) getOwner();
		List list = part.getViewer().getSelectedEditParts();
		Object obj = part.getRow(getRowNumber());
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (((EditPart) list.get(i)).getModel() == obj) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.handles.AbstractHandle#getOwner()
	 */
	public GraphicalEditPart getOwner() {
		return super.getOwner();
	}
}
