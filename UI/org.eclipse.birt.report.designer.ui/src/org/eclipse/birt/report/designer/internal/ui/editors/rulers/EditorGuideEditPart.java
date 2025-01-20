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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.zoom.ZoomListener;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.AccessibleHandleProvider;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.rulers.RulerChangeListener;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Cursor;

/**
 * Guide Editpart.
 *
 */
public class EditorGuideEditPart extends AbstractGraphicalEditPart {

	public static final int MIN_DISTANCE_BW_GUIDES = 5;
	public static final int DELETE_THRESHOLD = 20;

	private AccessibleEditPart accPart;
	private GuideLineFigure guideLineFig;
	private Cursor cursor = null;
	private ZoomListener zoomListener = new ZoomListener() {

		@Override
		public void zoomChanged(double zoom) {
			handleZoomChanged();
		}
	};
	private RulerChangeListener listener = new RulerChangeListener.Stub() {

		@Override
		public void notifyGuideMoved(Object guide) {
			if (getModel() == guide) {
				handleGuideMoved();
			}
		}

		@Override
		public void notifyPartAttachmentChanged(Object part, Object guide) {
			if (getModel() == guide) {
				handlePartAttachmentChanged(part);
			}
		}
	};

	/**
	 * @param model
	 */
	public EditorGuideEditPart(Object model) {
		setModel(model);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.internal.ui.rulers.GuideEditPart#getDragTracker(org.eclipse.
	 * gef.Request)
	 */
	@Override
	public DragTracker getDragTracker(Request request) {
		return new EditorGuideDragTracker(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		guideLineFig = createGuideLineFigure();

		return new EditorGuideFigure(isHorizontal());
	}

	public IFigure getGuideLineFigure() {
		return guideLineFig;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new GuideSelectionPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new EditorDragGuidePolicy());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		getRulerProvider().addRulerChangeListener(listener);
		if (getZoomManager() != null) {
			getZoomManager().addZoomListener(zoomListener);
		}
	}

	/**
	 * Creates the guideline figure.
	 *
	 * @return
	 */
	protected GuideLineFigure createGuideLineFigure() {
		return new GuideLineFigure();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		if (getZoomManager() != null) {
			getZoomManager().removeZoomListener(zoomListener);
		}
		getRulerProvider().removeRulerChangeListener(listener);
		if (getGuideLineFigure().getParent() != null) {
			getGuideLineFigure().getParent().remove(getGuideLineFigure());
		}
		super.deactivate();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getAccessibleEditPart()
	 */
	@Override
	protected AccessibleEditPart getAccessibleEditPart() {
		if (accPart == null) {
			accPart = new AccessibleGraphicalEditPart() {

				@Override
				public void getDescription(AccessibleEvent e) {
					if (getRulerProvider() != null) {
						getRulerProvider().getAccGuideDescription(e, getModel());
					}
				}

				@Override
				public void getName(AccessibleEvent e) {
					if (getRulerProvider() != null) {
						getRulerProvider().getAccGuideName(e, getModel());
					}
				}

				@Override
				public void getValue(AccessibleControlEvent e) {
					if (getRulerProvider() != null) {
						getRulerProvider().getAccGuideValue(e, getModel());
					}
				}
			};
		}
		return accPart;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class key) {
		if (key == AccessibleHandleProvider.class) {
			return new AccessibleHandleProvider() {

				@Override
				public List getAccessibleHandleLocations() {
					List result = new ArrayList();
					Point pt = getFigure().getBounds().getCenter();
					getFigure().translateToAbsolute(pt);
					result.add(pt);
					return result;
				}
			};
		}
		return super.getAdapter(key);
	}

	/**
	 * Gets the current cursor.
	 *
	 * @return
	 */
	public Cursor getCurrentCursor() {
		if (cursor == null) {
			return getFigure().getCursor();
		}
		return cursor;
	}

	/**
	 * Gets the guide layer
	 *
	 * @return
	 */
	public IFigure getGuideLayer() {
		return getRulerEditPart().getGuideLayer();
	}

	/**
	 * Gets the ruler editpart.
	 *
	 * @return
	 */
	public EditorRulerEditPart getRulerEditPart() {
		return (EditorRulerEditPart) getParent();
	}

	/**
	 * Gets the ruler provider
	 *
	 * @return
	 */
	public RulerProvider getRulerProvider() {
		return getRulerEditPart().getRulerProvider();
	}

	/**
	 * Gets the position
	 *
	 * @return
	 */
	public int getZoomedPosition() {
		double position = getRulerProvider().getGuidePosition(getModel());
		if (getZoomManager() != null) {
			position = Math.round(position * getZoomManager().getZoom());
		}
		return (int) position;
	}

	/**
	 * Gets the zoom manager.
	 *
	 * @return
	 */
	public ZoomManager getZoomManager() {
		return getRulerEditPart().getZoomManager();
	}

	/**
	 *
	 */
	protected void handleGuideMoved() {
		refreshVisuals();
	}

	/**
	 * @param part
	 */
	protected void handlePartAttachmentChanged(Object part) {
	}

	/**
	 *
	 */
	protected void handleZoomChanged() {
		refreshVisuals();
	}

	/**
	 * @return
	 */
	public boolean isHorizontal() {
		return !getRulerEditPart().isHorizontal();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		updateLocationOfFigures(getZoomedPosition());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPart#removeNotify()
	 */
	@Override
	public void removeNotify() {
		GraphicalEditPart nextSelection = null;
		if (getParent().isActive()) {
			// This guide is being deleted (but the ruler is still active). If
			// this guide
			// is selected, determine which part is to be selected next.
			int thisPos = getRulerProvider().getGuidePosition(getModel());
			if (getSelected() != SELECTED_NONE || hasFocus()) {
				List siblings = getParent().getChildren();
				int minDistance = -1;
				for (int i = 0; i < siblings.size(); i++) {
					EditorGuideEditPart guide = (EditorGuideEditPart) siblings.get(i);
					if (guide == this) {
						continue;
					}
					int posDiff = Math.abs(thisPos - getRulerProvider().getGuidePosition(guide.getModel()));
					if (minDistance == -1 || posDiff < minDistance) {
						minDistance = posDiff;
						nextSelection = guide;
					}
				}
				if (nextSelection == null) {
					nextSelection = (GraphicalEditPart) getParent();
				}
			}
		}
		super.removeNotify();
		if (nextSelection != null) {
			getViewer().select(nextSelection);
		}
	}

	/**
	 * @param c
	 */
	public void setCurrentCursor(Cursor c) {
		cursor = c;
	}

	/**
	 * @param position
	 */
	public void updateLocationOfFigures(int position) {
		getRulerEditPart().setLayoutConstraint(this, getFigure(), Integer.valueOf(position));
		Point guideFeedbackLocation = getGuideLineFigure().getBounds().getLocation();
		if (isHorizontal()) {
			guideFeedbackLocation.y = position;
		} else {
			guideFeedbackLocation.x = position;
		}
		getGuideLineFigure().setLocation(guideFeedbackLocation);
		getGuideLineFigure().revalidate();

		getFigure().getParent().repaint();
	}

	public static class GuideSelectionPolicy extends SelectionEditPolicy {

		@Override
		protected void hideFocus() {
			// ((GuideFigure)getHostFigure()).setDrawFocus(false);
		}

		@Override
		protected void hideSelection() {
			// ((GuideFigure)getHostFigure()).setDrawFocus(false);
		}

		@Override
		protected void showFocus() {
			// ((GuideFigure)getHostFigure()).setDrawFocus(true);
		}

		@Override
		protected void showSelection() {
			// ((GuideFigure)getHostFigure()).setDrawFocus(true);
		}
	}

	public static class GuideLineFigure extends Figure {

		public GuideLineFigure() {
			setPreferredSize(1, 1);
		}

		@Override
		protected void paintFigure(Graphics g) {
			g.setLineStyle(Graphics.LINE_DOT);
			g.setXORMode(true);
			g.setForegroundColor(ColorConstants.darkGray);
			if (bounds.width > bounds.height) {
				g.drawLine(bounds.x, bounds.y, bounds.right(), bounds.y);
				g.drawLine(bounds.x + 2, bounds.y, bounds.right(), bounds.y);
			} else {
				g.drawLine(bounds.x, bounds.y, bounds.x, bounds.bottom());
				g.drawLine(bounds.x, bounds.y + 2, bounds.x, bounds.bottom());
			}
		}
	}

}
