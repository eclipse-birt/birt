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

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.ViewportAutoexposeHelper;

/**
 * add comment here
 *
 */
public class EditorRulerRootEditPart extends AbstractGraphicalEditPart implements RootEditPart {

	private static final Insets VERTICAL_THRESHOLD = new Insets(18, 0, 18, 0);
	private static final Insets HORIZONTAL_THRESHOLD = new Insets(0, 18, 0, 18);

	private boolean horizontal;
	private EditPart contents;
	private EditPartViewer viewer;

	/**
	 * Constructor
	 *
	 * @param isHorzontal whether or not the corresponding model ruler is horizontal
	 */
	public EditorRulerRootEditPart(boolean isHorzontal) {
		super();
		horizontal = isHorzontal;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		super.deactivate();
		((RulerViewport) getFigure()).dispose();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#addChildVisual(org.eclipse.gef.EditPart,
	 *      int)
	 */
	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
		getViewport().setContents(child);
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		return new RulerViewport();
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == AutoexposeHelper.class) {
			if (((EditorRulerEditPart) getContents()).isHorizontal()) {
				return new ViewportAutoexposeHelper(this, HORIZONTAL_THRESHOLD);
			}
			return new ViewportAutoexposeHelper(this, VERTICAL_THRESHOLD);
		}
		return super.getAdapter(adapter);
	}

	/**
	 * @see org.eclipse.gef.RootEditPart#getContents()
	 */
	@Override
	public EditPart getContents() {
		return contents;
	}

	/**
	 * @see org.eclipse.gef.EditPart#getRoot()
	 */
	@Override
	public RootEditPart getRoot() {
		return this;
	}

	/**
	 * @see org.eclipse.gef.EditPart#getViewer()
	 */
	@Override
	public EditPartViewer getViewer() {
		return viewer;
	}

	/**
	 * Convenience method to get to the viewport
	 *
	 * @return the figure cast as a viewport
	 */
	protected Viewport getViewport() {
		return (Viewport) getFigure();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#removeChildVisual(org.eclipse.gef.EditPart)
	 */
	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		getViewport().setContents(null);
	}

	/**
	 * @see org.eclipse.gef.RootEditPart#setContents(org.eclipse.gef.EditPart)
	 */
	@Override
	public void setContents(EditPart editpart) {
		if (contents == editpart) {
			return;
		}
		if (contents != null) {
			removeChild(contents);
		}
		contents = editpart;
		if (contents != null) {
			addChild(contents, 0);
		}
	}

	/**
	 * @see org.eclipse.gef.RootEditPart#setViewer(org.eclipse.gef.EditPartViewer)
	 */
	@Override
	public void setViewer(EditPartViewer newViewer) {
		if (viewer == newViewer) {
			return;
		}
		if (viewer != null) {
			unregister();
		}
		viewer = newViewer;
		if (viewer != null) {
			register();
		}
	}

	/**
	 * A RulerViewport shares a RangeModel with that of the primary GraphicalViewer.
	 * The shared RangeModel is set on this viewport externally by a client (in this
	 * case, RulerComposite).
	 *
	 * @author Pratik Shah
	 * @since 3.0
	 */
	public class RulerViewport extends Viewport {

		/**
		 * Constructor
		 */
		public RulerViewport() {
			super(true);
			setLayoutManager(null);
			// The range model that's not shared is initialized such that it
			// can't scroll
			// anymore (otherwise, CTRL + SHIFT + ARROW scrolls it).
			RangeModel bogusRangeModel;
			if (horizontal) {
				bogusRangeModel = getVerticalRangeModel();
			} else {
				bogusRangeModel = getHorizontalRangeModel();
			}
			bogusRangeModel.setMinimum(0);
			bogusRangeModel.setMaximum(100);
			bogusRangeModel.setValue(0);
			bogusRangeModel.setExtent(100);
		}

		/**
		 * This is the method that does the actual layout. We don't want a layout to
		 * occur when layout() is invoked, rather when something changes in the shared
		 * RangeModel.
		 *
		 * @param force if <code>true</code>, the contents will be resized and
		 *              revalidated; otherwise, just a repaint will occur.
		 */
		protected void doLayout(boolean force) {
			repaint();
			/*
			 * @TODO:Pratik It seems you don't really need this force argument. Those that
			 * invoke doLayout(false) can invoke repaint() themselves. doLayout() should
			 * just layout.
			 */
			if (force) {
				RangeModel rModel;
				if (horizontal) {
					rModel = getHorizontalRangeModel();
				} else {
					rModel = getVerticalRangeModel();
				}
				Rectangle contentBounds = Rectangle.SINGLETON;
				if (horizontal) {
					contentBounds.y = 0;
					contentBounds.x = rModel.getMinimum();
					contentBounds.height = this.getContents().getPreferredSize().height;
					contentBounds.width = rModel.getMaximum() - rModel.getMinimum();
				} else {
					contentBounds.y = rModel.getMinimum();
					contentBounds.x = 0;
					contentBounds.height = rModel.getMaximum() - rModel.getMinimum();
					contentBounds.width = this.getContents().getPreferredSize().width;
				}
				if (!this.getContents().getBounds().equals(contentBounds)) {
					this.getContents().setBounds(contentBounds);
					this.getContents().revalidate();
				}
			}
		}

		/**
		 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
		 */
		@Override
		public Dimension getPreferredSize(int wHint, int hHint) {
			if (this.getContents() == null) {
				return new Dimension();
			}
			Dimension pSize = this.getContents().getPreferredSize(wHint, hHint);
			if (horizontal) {
				RangeModel rModel = getHorizontalRangeModel();
				pSize.width = rModel.getMaximum() - rModel.getMinimum();
			} else {
				RangeModel rModel = getVerticalRangeModel();
				pSize.height = rModel.getMaximum() - rModel.getMinimum();
			}
			return pSize.expand(getInsets().getWidth(), getInsets().getHeight());
		}

		/**
		 * Since the RangeModel is shared with that of the editor's, a RulerViewport
		 * should not adjust it.
		 *
		 * @see org.eclipse.draw2d.Viewport#readjustScrollBars()
		 */
		@Override
		protected void readjustScrollBars() {
		}

		/**
		 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
		 */
		@Override
		protected void paintBorder(Graphics graphics) {
			super.paintBorder(graphics);
			if (this.getContents() != null && ((EditorRulerFigure) this.getContents()).getDrawFocus()) {
				Rectangle focusBounds = getBounds().getCopy();
				if (((EditorRulerFigure) this.getContents()).isHorizontal()) {
					focusBounds.resize(-2, -4);
					focusBounds.x++;
				} else {
					focusBounds.resize(-4, -2);
					focusBounds.y++;
				}
				graphics.setForegroundColor(ColorConstants.black);
				graphics.setBackgroundColor(ColorConstants.white);
				graphics.drawFocus(focusBounds);
			}
		}

		/**
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (this.getContents() != null && event.getSource() instanceof RangeModel) {
				String property = event.getPropertyName();
				doLayout(RangeModel.PROPERTY_MAXIMUM.equals(property) || RangeModel.PROPERTY_MINIMUM.equals(property)
						|| RangeModel.PROPERTY_VALUE.equals(property));
			}
		}

		/**
		 * @see org.eclipse.draw2d.Viewport#setContents(org.eclipse.draw2d.IFigure)
		 */
		@Override
		public void setContents(IFigure figure) {
			super.setContents(figure);
			// Need to layout when contents change
			if (this.getContents() != null) {
				doLayout(true);
			}
		}

		/**
		 * RulerViewport uses local coordinates.
		 *
		 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
		 */
		@Override
		protected boolean useLocalCoordinates() {
			return true;
		}

		/**
		 * Dispose the RangeModel.
		 */
		public void dispose() {
			if (getHorizontalRangeModel() instanceof RulerDefaultRangeModel) {
				((RulerDefaultRangeModel) getHorizontalRangeModel()).dispose();
			}
			if (getVerticalRangeModel() instanceof RulerDefaultRangeModel) {
				((RulerDefaultRangeModel) getVerticalRangeModel()).dispose();
			}
		}
	}

}
