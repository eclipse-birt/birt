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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.rulers.RulerChangeListener;
import org.eclipse.gef.rulers.RulerProvider;

/**
 * Editor ruler editPart.
 *
 */
public class EditorRulerEditPart extends AbstractGraphicalEditPart {

	protected GraphicalViewer diagramViewer;
	private RulerProvider rulerProvider;
	private boolean horizontal;
	private RulerChangeListener listener = new RulerChangeListener.Stub() {

		@Override
		public void notifyGuideReparented(Object guide) {
			handleGuideReparented(guide);
		}

		@Override
		public void notifyUnitsChanged(int newUnit) {
			handleUnitsChanged(newUnit);
		}
	};

	/**
	 * The constructor.
	 *
	 * @param model
	 */
	public EditorRulerEditPart(Object model) {
		setModel(model);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		getRulerProvider().addRulerChangeListener(listener);
		getRulerFigure().setZoomManager(getZoomManager());
		super.activate();
		if (Policy.TRACING_RULER) {
			System.out.println("Ruler >> Activated"); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.internal.ui.rulers.RulerEditPart#getDragTracker(org.eclipse.
	 * gef.Request)
	 */
	@Override
	public DragTracker getDragTracker(Request request) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new EditRulerSelectionPolicy());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.internal.ui.rulers.RulerEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		EditorRulerFigure ruler = new EditorRulerFigure(isHorizontal(), getRulerProvider().getUnit());
		if (ruler.getUnit() == RulerProvider.UNIT_PIXELS) {
			ruler.setInterval(100, 2);
		}
		return ruler;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.internal.ui.rulers.RulerEditPart#handleUnitsChanged(int)
	 */
	public void handleUnitsChanged(int newUnit) {
		getRulerFigure().setUnit(newUnit);
		((EditorRulerFigure) getFigure()).setLeftSpace(((EditorRulerProvider) getRulerProvider()).getLeftSpace());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		super.deactivate();
		getRulerProvider().removeRulerChangeListener(listener);
		rulerProvider = null;
		getRulerFigure().setZoomManager(null);
		if (Policy.TRACING_RULER) {
			System.out.println("Ruler >> Dectivated"); //$NON-NLS-1$
		}
	}

	/**
	 * Returns the GraphicalViewer associated with the diagram.
	 *
	 * @return graphical viewer associated with the diagram.
	 */
	public GraphicalViewer getDiagramViewer() {
		return diagramViewer;
	}

	public IFigure getGuideLayer() {
		LayerManager lm = (LayerManager) diagramViewer.getEditPartRegistry().get(LayerManager.ID);
		if (lm != null) {
			return lm.getLayer(LayerConstants.GUIDE_LAYER);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List getModelChildren() {
		return getRulerProvider().getGuides();
	}

	/**
	 * Gets the ruler figure.
	 *
	 * @return
	 */
	protected EditorRulerFigure getRulerFigure() {
		return (EditorRulerFigure) getFigure();
	}

	/**
	 * Gets the provider.
	 *
	 * @return
	 */
	public RulerProvider getRulerProvider() {
		return rulerProvider;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPart#getTargetEditPart(org.eclipse.gef.Request)
	 */
	@Override
	public EditPart getTargetEditPart(Request request) {
		if (request.getType().equals(REQ_MOVE)) {
			return this;
		} else {
			return super.getTargetEditPart(request);
		}
	}

	/**
	 * Gets the zoom manager.
	 *
	 * @return
	 */
	public ZoomManager getZoomManager() {
		return (ZoomManager) diagramViewer.getProperty(ZoomManager.class.toString());
	}

	/**
	 * @param guide
	 */
	public void handleGuideReparented(Object guide) {
		refreshChildren();
		EditPart guidePart = (EditPart) getViewer().getEditPartRegistry().get(guide);
		if (guidePart != null) {
			getViewer().select(guidePart);
		}
	}

	/**
	 * @return
	 */
	public boolean isHorizontal() {
		return horizontal;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPart#setParent(org.eclipse.gef.EditPart)
	 */
	@Override
	public void setParent(EditPart parent) {
		super.setParent(parent);
		if (getParent() != null && diagramViewer == null) {
			diagramViewer = (GraphicalViewer) getViewer().getProperty(GraphicalViewer.class.toString());
			RulerProvider hProvider = (RulerProvider) diagramViewer
					.getProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER);
			if (hProvider != null && hProvider.getRuler() == getModel()) {
				rulerProvider = hProvider;
				horizontal = true;
			} else {
				rulerProvider = (RulerProvider) diagramViewer.getProperty(RulerProvider.PROPERTY_VERTICAL_RULER);
			}
		}
	}

	/**
	 * EditRulerSelectionPolicy
	 */
	public static class EditRulerSelectionPolicy extends SelectionEditPolicy {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideFocus()
		 */
		@Override
		protected void hideFocus() {
			((EditorRulerFigure) getHostFigure()).setDrawFocus(false);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideSelection()
		 */
		@Override
		protected void hideSelection() {
			((EditorRulerFigure) getHostFigure()).setDrawFocus(false);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showSelection()
		 */
		@Override
		protected void showSelection() {
		}
	}
}
