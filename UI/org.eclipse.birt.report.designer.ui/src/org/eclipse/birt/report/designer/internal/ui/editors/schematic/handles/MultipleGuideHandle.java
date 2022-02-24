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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractTableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.MultipleEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.providers.SchematicContextMenuProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.ReportElementDragTracker;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemViewProvider;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.handles.MoveHandleLocator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * The GuideHandle for the multiple EditPart.
 */

public class MultipleGuideHandle extends AbstractGuideHandle {

	private static final String REMOVE = Messages.getString("MultipleGuideHandle.RemoveView");//$NON-NLS-1$ ;

	/**
	 * Constructor
	 * 
	 * @param owner
	 */
	public MultipleGuideHandle(GraphicalEditPart owner) {
		super(owner, new MutipleLocator(owner));
	}

	/**
	 * Set the selection view.
	 * 
	 * @param number
	 */
	public void setSelected(int number) {
		List list = getChildren();
		if (number < 0 || number > list.size() - 1) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof ShowSourceFigure) {
				continue;
			}
			ChildrenGuideHandle handle = (ChildrenGuideHandle) list.get(i);
			if (i == number) {
				handle.setSelected(true);
			} else {
				handle.setSelected(false);
			}
		}

		repaint();
	}

	/**
	 * Add the children.
	 * 
	 * @param list
	 */
	public void addChildren(List list) {
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			Figure figure = (Figure) children.get(i);
			remove(figure);
		}

		Font font = getFont();
		if (font == null) {
			font = getDefaultFont();
			setFont(font);
		}

		ChildrenGuideHandle first = new ChildrenGuideHandle(getOwner(), 0);
		first.setSelected(true);
		first.setIndicatorLabel(getLabel(getOwner().getModel()));
		first.setIndicatorIcon(getImage(getOwner().getModel()));
		Dimension dim = first.calculateIndicatorDimension(font, 1);
		first.setSize(dim);

		add(first);

		for (int i = 1; i <= list.size(); i++) {
			ChildrenGuideHandle handle = new ChildrenGuideHandle(getOwner(), i);
			handle.setIndicatorLabel(getLabel(list.get(i - 1)));
			handle.setIndicatorIcon(getImage(list.get(i - 1)));
			Dimension size = handle.calculateIndicatorDimension(font, 1);
			handle.setSize(size);
			add(handle);
		}
	}

	private Font getDefaultFont() {
		return FontManager.getFont("Tahoma", 8, SWT.NORMAL);//$NON-NLS-1$
	}

	/**
	 * ShowSourceFigure
	 */
	private static class ShowSourceFigure extends Figure {

		protected void paintFigure(Graphics graphics) {
			Rectangle rect = getBounds();

			graphics.setLineWidth(2);
			graphics.drawRectangle(rect);

			graphics.drawLine(rect.x, rect.y - 1, rect.x + rect.width, rect.y - 1);
			graphics.drawLine(rect.x, rect.y, rect.x, rect.y + rect.height);

			graphics.setLineWidth(3);
			graphics.drawLine(rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + rect.height);
			graphics.drawLine(rect.x, rect.y + rect.height, rect.x + rect.width, rect.y + rect.height);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.draw2d.Figure#containsPoint(int, int)
		 */
		public boolean containsPoint(int x, int y) {
			return false;
		}
	}

	private static class ChildrenGuideHandle extends AbstractGuideHandle {

		private boolean isSelected = false;
		private Figure showSource = new ShowSourceFigure();
		// private boolean enter;
		private int number;
		protected Image image;
		protected String indicatorLabel = "  ";//$NON-NLS-1$
		int gap;
		protected Insets gapInsets = new Insets(3, 3, 3, 3);

		public ChildrenGuideHandle(GraphicalEditPart owner, int number) {
			super(owner, new NothingLocator());
			this.number = number;
		}

		protected DragTracker createDragTracker() {
			return new ChildrenDragTracker(getOwner(), number);
		}

		/**
		 * Sets the left corner label
		 * 
		 * @param indicatorLabel
		 */
		public void setIndicatorLabel(String indicatorLabel) {
			if (indicatorLabel != null) {
				this.indicatorLabel = indicatorLabel;
			}
		}

		public void mouseEntered(MouseEvent me) {
			if (showSource.getParent() == null && !isSelected()) {
				Rectangle rect = getBounds().getCopy();
				showSource.setBounds(rect);
				getParent().add(showSource);
			}
			super.mouseEntered(me);
		}

		public void mouseExited(MouseEvent me) {
			if (showSource.getParent() != null) {
				showSource.getParent().remove(showSource);
			}
			super.mouseExited(me);
		}

		/**
		 * Sets the left corner
		 * 
		 * @param image
		 */
		public void setIndicatorIcon(Image image) {
			this.image = image;
		}

		public void paintFigure(Graphics graphics) {
			int width = 1;
			Rectangle bounds = getBounds().getCopy();
			bounds.y = bounds.y + 2;

			if (isSelected()) {
				graphics.setBackgroundColor(ReportColorConstants.TableGuideFillColor);
			} else {
				graphics.setBackgroundColor(ReportColorConstants.MultipleSelectionHandleColor);
			}
			graphics.fillRectangle(bounds);
			graphics.setForegroundColor(ReportColorConstants.ShadowLineColor);
			bounds = getBounds().getCopy();
			graphics.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height);
			graphics.drawLine(bounds.x, bounds.y + bounds.height - 1, bounds.x + bounds.width,
					bounds.y + bounds.height - 1);
			graphics.drawLine(bounds.x + bounds.width - 1, bounds.y, bounds.x + bounds.width - 1,
					bounds.y + bounds.height);
			graphics.setForegroundColor(ReportColorConstants.TableGuideFillColor);
			graphics.drawLine(bounds.x, bounds.y, bounds.x + bounds.width - 1, bounds.y);
			int x = getBounds().x + gapInsets.left;
			if (image != null) {
				graphics.drawImage(image, x, bounds.y + gapInsets.top);
				x += image.getBounds().width + gap;
			}
			graphics.setForegroundColor(ReportColorConstants.TableGuideTextColor);
			graphics.drawString(indicatorLabel, x + 2 * width, bounds.y + 2 + gapInsets.top - width);
		}

		protected Dimension calculateIndicatorDimension(Font font, int width) {
			gap = 0;
			Dimension iconDimension = new Dimension();
			if (image != null) {
				iconDimension = new Dimension(image);
				gap = 2;
			}
			Dimension d = FigureUtilities.getTextExtents(indicatorLabel, font);
			int incheight = 0;
			if (iconDimension.height > d.height) {
				incheight = iconDimension.height - d.height;
			}
			d.expand(iconDimension.width + gap + gapInsets.left + gapInsets.right + 2 * width + 2,
					incheight + gapInsets.top + gapInsets.bottom);

			return d;
		}

		/**
		 * @return
		 */
		public boolean isSelected() {
			return isSelected;
		}

		/**
		 * @param isSelected
		 */
		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
			if (isSelected) {
				if (showSource.getParent() != null) {
					showSource.getParent().remove(showSource);
				}
			}
		}
	}

	/**
	 * Calculate the size.
	 * 
	 * @return
	 */
	protected Dimension calculateIndicatorDimension() {
		Dimension retValue = new Dimension();
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			Figure figure = (Figure) children.get(i);
			if (figure instanceof ShowSourceFigure) {
				continue;
			}
			// retValue = retValue.union( figure.getSize( ) );
			retValue.width = retValue.width + figure.getSize().width;
			retValue.height = Math.max(retValue.height, figure.getSize().height);
		}

		return retValue;
	}

	/**
	 * ChildrenDragTracker
	 */
	private static class ChildrenDragTracker extends ReportElementDragTracker {

		private IMenuListener listener = new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				Action action = new Action(REMOVE) {

					public void run() {
						((MultipleEditPart) ChildrenDragTracker.this.getSourceEditPart()).removeView(number);
					}

					public boolean isEnabled() {
						int position = number - 1;
						List list = ((ReportItemHandle) (ChildrenDragTracker.this.getSourceEditPart().getModel()))
								.getViews();
						if (position < 0 && position > list.size() - 1) {
							return false;
						}
						return ((DesignElementHandle) list.get(position)).canDrop();
					}

				};

				ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
				action.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
				manager.add(action);
			}

		};

		private int number;

		/**
		 * Constructor.
		 * 
		 * @param sourceEditPart
		 * @param number
		 */
		public ChildrenDragTracker(EditPart sourceEditPart, int number) {
			super(sourceEditPart);
			this.number = number;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.gef.tools.SelectEditPartTracker#performConditionalSelection()
		 */
		protected void performConditionalSelection() {
			super.performConditionalSelection();
			((MultipleEditPart) getSourceEditPart()).setCurrentView(number);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.tools.SelectEditPartTracker#performSelection()
		 */
		protected void performSelection() {
			if (hasSelectionOccurred())
				return;
			EditPart real = null;
			List children = getSourceEditPart().getChildren();
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i) instanceof AbstractTableEditPart) {
					real = (EditPart) children.get(i);
					break;
				}
			}
			if (real == null) {
				real = getSourceEditPart();
			}
			setFlag(FLAG_SELECTION_PERFORMED, true);
			EditPartViewer viewer = getCurrentViewer();
			List selectedObjects = viewer.getSelectedEditParts();

			if (getCurrentInput().isModKeyDown(SWT.MOD1)) {
				if (selectedObjects.contains(getSourceEditPart()))
					viewer.deselect(getSourceEditPart());
				else {
					if (number == 0) {
						viewer.appendSelection(real);
					} else {
						viewer.appendSelection(getSourceEditPart());
					}
				}
			} else if (getCurrentInput().isShiftKeyDown()) {
				if (number == 0) {
					viewer.appendSelection(real);
				} else {
					viewer.appendSelection(getSourceEditPart());
				}
			} else {
				if (number == 0) {
					viewer.select(real);
				} else {
					viewer.select(getSourceEditPart());
				}
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.tools.SelectEditPartTracker#handleButtonDown(int)
		 */
		protected boolean handleButtonDown(int button) {
			if (button == 3 && number != 0) {
				((SchematicContextMenuProvider) getSourceEditPart().getViewer().getContextMenu()).setProxy(listener);
			}
			return super.handleButtonDown(button);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.tools.DragEditPartsTracker#handleButtonUp(int)
		 */
		protected boolean handleButtonUp(int button) {
			return super.handleButtonUp(button);
		}
	}

	private Image getImage(Object obj) {
		// if (obj instanceof TableHandle)
		// {
		// return ReportPlatformUIImages.getImage(
		// IReportGraphicConstants.ICON_ELEMENT_TABLE );
		// }
		Object ownerModel = getOwner().getModel();

		if (ownerModel == obj) {
			return ProviderFactory.createProvider(obj).getNodeIcon(obj);
		}

		return null;
	}

	private String getLabel(Object obj) {
		Object ownerModel = getOwner().getModel();
		if (ownerModel == obj) {
			return ProviderFactory.createProvider(obj).getNodeDisplayName(obj);
		}

		Object[] objs = ElementAdapterManager.getAdapters(getOwner().getModel(), IReportItemViewProvider.class);
		if (objs != null) {
			return ((IReportItemViewProvider) objs[0]).getViewName();
		}

		return ""; //$NON-NLS-1$
	}

	private static class MutipleLocator extends MoveHandleLocator {

		/**
		 * @param ref
		 */
		public MutipleLocator(GraphicalEditPart part) {
			super(part.getFigure());
		}

		public void relocate(IFigure target) {
			Rectangle bounds;
			if (getReference() instanceof HandleBounds)
				bounds = ((HandleBounds) getReference()).getHandleBounds();
			else
				bounds = getReference().getBounds();

			Dimension dim = ((MultipleGuideHandle) target).calculateIndicatorDimension();
			bounds = new PrecisionRectangle(new Rectangle(bounds.x, bounds.y + bounds.height, dim.width, dim.height));
			Rectangle copy = bounds.getCopy();
			getReference().translateToAbsolute(bounds);
			target.translateToRelative(bounds);
			bounds.width = copy.width;
			bounds.height = copy.height;

			target.setBounds(bounds);
			relocateChildren(target, getReference());
		}

		private void relocateChildren(IFigure parent, IFigure reference) {
			List children = parent.getChildren();

			int size = children.size();
			int width = parent.getBounds().x;

			// Dimension pDim = parent.getSize( );
			Dimension pDim = ((MultipleGuideHandle) parent).calculateIndicatorDimension();

			int height = pDim.height;
			int y = parent.getBounds().y;
			for (int i = 0; i < size; i++) {

				IFigure f = (IFigure) children.get(i);
				if (f instanceof ShowSourceFigure) {
					continue;
				}
				Rectangle bounds = f.getBounds().getCopy();

				bounds = new PrecisionRectangle(bounds);
				Dimension dim = bounds.getSize();

				bounds.width = dim.width;
				bounds.height = height;

				bounds.y = y;
				bounds.x = width;

				width = width + dim.width;

				f.setBounds(bounds);
			}
		}
	}
}
