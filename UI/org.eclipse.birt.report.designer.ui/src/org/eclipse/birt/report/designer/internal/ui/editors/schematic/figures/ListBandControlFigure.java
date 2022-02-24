/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.ReportFigureUtilities;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * Presents list band figure figure for list band edit part
 * 
 */
public class ListBandControlFigure extends Figure {

	public static final Dimension CONTROL_SIZE = new Dimension(88, 19);
	private static final Insets DISPLAY_MARGIN = new Insets(15);
	private static final String TOOLTIP_LIST_DETAIL = Messages.getString("ListBandControlFigure.tooltip.ListDetail"); //$NON-NLS-1$
	private static final String TOOLTIP_LIST_HEADER = Messages.getString("ListBandControlFigure.tooltip.ListHeader"); //$NON-NLS-1$
	private static final String TOOLTIP_LIST_FOOTER = Messages.getString("ListBandControlFigure.tooltip.ListFooter"); //$NON-NLS-1$
	private static final String TOOLTIP_GROUP_HEADER = Messages.getString("ListBandControlFigure.tooltip.GroupHeader"); //$NON-NLS-1$
	private static final String TOOLTIP_GROUP_FOOTER = Messages.getString("ListBandControlFigure.tooltip.GroupFooter"); //$NON-NLS-1$

	private ListBandEditPart owner;

	public ListBandControlFigure(ListBandEditPart owner) {
		this.owner = owner;

		String tp = getTooltipText();
		if (tp != null) {
			Label tooltip = new Label(tp);
			tooltip.setBorder(new MarginBorder(0, 2, 0, 2));
			setToolTip(tooltip);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		graphics.setForegroundColor(ReportColorConstants.ShadowLineColor);
		graphics.setLineStyle(SWT.LINE_SOLID);
		graphics.drawRectangle(getBounds().getCopy().shrink(2, 1));
		graphics.setBackgroundColor(ReportColorConstants.ListControlFillColor);
		graphics.fillRectangle(getBounds().getCopy().shrink(3, 2));
	}

	private String getTooltipText() {
		int type = ((ListBandProxy) owner.getModel()).getType();

		switch (type) {
		case ListBandProxy.LIST_HEADER_TYPE:
			return TOOLTIP_LIST_HEADER;

		case ListBandProxy.LIST_DETAIL_TYPE:
			return TOOLTIP_LIST_DETAIL;

		case ListBandProxy.LIST_FOOTER_TYPE:
			return TOOLTIP_LIST_FOOTER;

		case ListBandProxy.LIST_GROUP_HEADER_TYPE:
			return TOOLTIP_GROUP_HEADER;

		case ListBandProxy.LIST_GROUP_FOOTER_TYPE:
			return TOOLTIP_GROUP_FOOTER;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		// return CONTROL_SIZE;//88, 19
		Dimension dimension = new Dimension(0, CONTROL_SIZE.height);
		List list = getChildren();
		for (int i = 0; i < list.size(); i++) {
			Figure figure = (Figure) list.get(i);
			dimension.width = dimension.width + figure.getSize().width;
		}
		return dimension;
		// return super.getPreferredSize( wHint, hHint );
	}

	public static class ListBandControlVisible extends Figure implements MouseListener {

		private ListBandEditPart owner;

		private boolean state = true;

		public ListBandControlVisible(ListBandEditPart owner) {
			setBounds(new Rectangle(0, 0, 20, 19));
			this.owner = owner;
			addMouseListener(this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.draw2d.MouseListener#mousePressed(org.eclipse.draw2d.
		 * MouseEvent)
		 */
		public void mousePressed(MouseEvent me) {
			state = !state;
			IFigure parent = this;
			while ((parent = parent.getParent()) != null) {
				if (parent instanceof ReportShowFigure) {
					((ReportShowFigure) parent).setShowing(state);
					getOwner().markDirty(true);
					break;
				}
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.draw2d.MouseListener#mouseReleased(org.eclipse.draw2d
		 * .MouseEvent)
		 */
		public void mouseReleased(MouseEvent me) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.draw2d.MouseListener#mouseDoubleClicked(org.eclipse.draw2d
		 * .MouseEvent)
		 */
		public void mouseDoubleClicked(MouseEvent me) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		protected void paintFigure(Graphics graphics) {
			// graphics.setBackgroundColor( ColorConstants.white );
			Rectangle rect = getBounds().getCopy().shrink(6, 6);
			graphics.fillRectangle(rect);

			IFigure parent = this;
			while ((parent = parent.getParent()) != null) {
				if (parent instanceof ReportShowFigure) {
					state = ((ReportShowFigure) parent).isControlShowing();
					break;
				}
			}
			ReportFigureUtilities.paintExpandHandle(graphics, 8, getBounds().getCenter(), !state);
		}

		protected ListBandEditPart getOwner() {
			return owner;
		}
	}

	public static class ListControlDisplayNameFigure extends Figure {

		private ListBandEditPart owner;
		private String text = ""; //$NON-NLS-1$

		/**
		 * @param owner
		 */
		public ListControlDisplayNameFigure(ListBandEditPart owner) {
			super();
			this.owner = owner;
			text = (((ListBandProxy) owner.getModel()).getDisplayName());

			Font font = getFont();

			Shell sl = null;
			GC gc = null;

			if (font == null) {
				sl = new Shell();
				gc = new GC(sl);
				font = gc.getFont();
			}

			int width = FigureUtilities.getTextWidth(text, font);

			if (gc != null) {
				gc.dispose();
			}

			if (sl != null) {
				sl.dispose();
			}

			setBounds(new Rectangle(35, 0, width + DISPLAY_MARGIN.right, 19));
			setBorder(new MarginBorder(8, 0, 0, 0));
		}

		public ListBandEditPart getOwner() {
			return owner;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		protected void paintFigure(Graphics graphics) {
			Rectangle rect = getClientArea().getCopy();
			// String text = ( ( (ListBandProxy) getOwner( ).getModel( )
			// ).getDisplayName( ) );
			graphics.setForegroundColor(ReportColorConstants.DarkShadowLineColor);
			graphics.drawString(text, rect.x, rect.y - 6);
		}

		/**
		 * @param text
		 */
		public void setText(String text) {
			this.text = text;
		}
	}

	public static class ListIconFigure extends Figure {

		private ListBandEditPart owner;

		/**
		 * @param owner
		 */
		public ListIconFigure(ListBandEditPart owner) {
			super();
			this.owner = owner;
			setBounds(new Rectangle(17, 2, 16, 16));

		}

		public ListBandEditPart getOwner() {
			return owner;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		protected void paintFigure(Graphics graphics) {
			Rectangle rect = getClientArea().getCopy();
			graphics.drawImage(getImage(), rect.x, rect.y);
		}

		private Image getImage() {
			int type = ((ListBandProxy) getOwner().getModel()).getType();
			String imageType = null;
			switch (type) {
			case ListBandProxy.LIST_HEADER_TYPE:
				imageType = IReportGraphicConstants.ICON_NODE_HEADER;
				break;
			case ListBandProxy.LIST_DETAIL_TYPE:
				imageType = IReportGraphicConstants.ICON_NODE_DETAILS;
				break;
			case ListBandProxy.LIST_FOOTER_TYPE:
				imageType = IReportGraphicConstants.ICON_NODE_FOOTER;
				break;
			case ListBandProxy.LIST_GROUP_HEADER_TYPE:
				imageType = IReportGraphicConstants.ICON_NODE_GROUP_HEADER;
				break;
			case ListBandProxy.LIST_GROUP_FOOTER_TYPE:
				imageType = IReportGraphicConstants.ICON_NODE_GROUP_FOOTER;
				break;
			}
			return ReportPlatformUIImages.getImage(imageType);
		}
	}

	public static class ListControlMenuFigure extends AbstractHandle {

		/**
		 * @param owner
		 */
		public ListControlMenuFigure(GraphicalEditPart owner, Locator loc) {
			super(owner, loc);
			setBounds(new Rectangle(55, 0, 18, 19));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		protected void paintFigure(Graphics graphics) {
			graphics.setForegroundColor(ColorConstants.black);

			Point center = getBounds().getCenter();

			int height = 5;
			center.y -= height / 2;

			ReportFigureUtilities.paintDoubleArrow(graphics, height, center);

			center.x += 2;
			center.y += height + 2;

			graphics.setBackgroundColor(ColorConstants.black);
			ReportFigureUtilities.paintTriangle(graphics, 4, center);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
		 */
		protected DragTracker createDragTracker() {
			return new MenuTracker(getOwner());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.handles.AbstractHandle#addNotify()
		 */
		public void addNotify() {
		}
	}

	private static class MenuTracker extends DragEditPartsTracker {

		/**
		 * @param sourceEditPart
		 */
		public MenuTracker(EditPart sourceEditPart) {
			super(sourceEditPart);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.tools.DragEditPartsTracker#handleButtonUp(int)
		 */
		protected boolean handleButtonUp(int button) {
			boolean bool = super.handleButtonUp(button);
			if (button == 1) {
				getSourceEditPart().getViewer().getContextMenu().getMenu().setVisible(true);
			}
			return bool;
		}
	}

}
