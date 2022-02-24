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

package org.eclipse.birt.report.designer.internal.ui.editors.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.util.mediator.request.IRequestConverter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ISelectionHandlesEditPolicy;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Handle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.ui.parts.DomainEventDispatcher;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 
 * @author David Michonneau
 */
public class DeferredGraphicalViewer extends ScrollingGraphicalViewer {

	private DomainEventDispatcher eventDispatcher;
	private OriginStepData stepData = new OriginStepData();
	/**
	 * The actual layout area size.
	 */
	public static final String LAYOUT_SIZE = "Layout Size"; //$NON-NLS-1$

	public static final String REPORT_SIZE = "Report Size"; //$NON-NLS-1$

	public static final String RULER_SIZE = "Ruler Size"; //$NON-NLS-1$

	public static final String PROPERTY_MARGIN_VISIBILITY = "Property Margin Visibility"; //$NON-NLS-1$

	public static final String PROPERTY_MARGIN_RESET = "Reset Margin"; //$NON-NLS-1$

	/** Show the guide */
	public static final String PROPERTY_DRAG_GUIDE = "Drag Guide"; //$NON-NLS-1$

	/**
	 * Sets the selection to the given selection and fires selection changed. The
	 * ISelection should be an {@link IStructuredSelection}or it will be ignored.
	 * 
	 * @see ISelectionProvider#setSelection(ISelection)
	 */
	public void setSelection(ISelection newSelection) {
		setSelection(newSelection, true);

	}

	/**
	 * Sets the selection to the given selection and fires selection changed. The
	 * ISelection should be an {@link IStructuredSelection}or it will be ignored.
	 * 
	 * @see ISelectionProvider#setSelection(ISelection)
	 */
	public void setSelection(ISelection newSelection, boolean dispatch) {
		if (!(newSelection instanceof IStructuredSelection))
			return;

		List editparts = ((IStructuredSelection) newSelection).toList();
		List selection = primGetSelectedEditParts();

		setFocus(null);
		for (int i = 0; i < selection.size(); i++)
			((EditPart) selection.get(i)).setSelected(EditPart.SELECTED_NONE);
		selection.clear();

		editparts = flitterEditpart(editparts);
		// for create handle
		selection.addAll(editparts);

		for (int i = 0; i < editparts.size(); i++) {
			EditPart part = (EditPart) editparts.get(i);
			if (i == editparts.size() - 1)
				part.setSelected(EditPart.SELECTED_PRIMARY);
			else
				part.setSelected(EditPart.SELECTED);

		}

		if (dispatch) {
			fireSelectionChanged();
		}
	}

	/**
	 * @param editparts
	 */
	private List flitterEditpart(List editparts) {
		List copy = new ArrayList(editparts);
		int size = editparts.size();
		for (int i = 0; i < size; i++) {
			EditPart part = (EditPart) editparts.get(i);
			if (part.getAdapter(ISelectionFilter.class) != null) {
				copy = ((ISelectionFilter) part.getAdapter(ISelectionFilter.class)).filterEditpart(copy);
			}
		}
		editparts = copy;
		return editparts;
	}

	/**
	 * @see GraphicalViewer#findHandleAt(org.eclipse.draw2d.geometry.Point)
	 */
	public Handle findHandleAt(Point p) {
		LayerManager layermanager = (LayerManager) getEditPartRegistry().get(LayerManager.ID);
		if (layermanager == null)
			return null;
		List list = new ArrayList(3);
		// list.add(layermanager.getLayer(LayerConstants.PRIMARY_LAYER));
		list.add(layermanager.getLayer(LayerConstants.CONNECTION_LAYER));
		list.add(layermanager.getLayer(LayerConstants.FEEDBACK_LAYER));
		IFigure handle = getLightweightSystem().getRootFigure().findFigureAtExcluding(p.x, p.y, list);
		if (handle instanceof Handle)
			return (Handle) handle;
		return null;
	}

	/**
	 * Exposes it to public.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.ScrollingGraphicalViewer#getFigureCanvas()
	 */
	public FigureCanvas getFigureCanvas() {
		return super.getFigureCanvas();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.parts.AbstractEditPartViewer#appendSelection(org.eclipse.
	 * gef.EditPart)
	 */
	public void appendSelection(EditPart editpart) {
		if (editpart != focusPart)
			setFocus(null);
		List list = primGetSelectedEditParts();
		list.remove(editpart);
		list.add(editpart);

		setSelection(new StructuredSelection(list));
	}

	public void setEditDomain(EditDomain domain) {
		super.setEditDomain(domain);
		eventDispatcher = new ReportDomainEventDispatcher(domain, this);
		eventDispatcher.setEnableKeyTraversal(true);
		getLightweightSystem().setEventDispatcher(eventDispatcher);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#getEventDispatcher()
	 */
	protected DomainEventDispatcher getEventDispatcher() {
		return eventDispatcher;
	}

	// We override reveal to show the Handles of the selected EditPart when
	// scrolling
	public void reveal(EditPart part) {
		// In some case, the editor control is not created, but get the sync selection
		// event.
		// Fix this problem temporary.
		if (getFigureCanvas() == null || getFigureCanvas().isDisposed()) {
			return;
		}

		Viewport port = getFigureCanvas().getViewport();
		IFigure target = ((GraphicalEditPart) part).getFigure();

		Rectangle exposeRegion = target.getBounds().getCopy();

		// Get the primary editpolicy
		EditPolicy policy = part.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);

		// If the policy let us access the handles, proceed, otherwise
		// default to original behaviour
		if (!(policy instanceof ISelectionHandlesEditPolicy)) {
			super.reveal(part);
			return;
		}

		// First translate exposeRegion to the root level
		target = target.getParent();
		while (target != null && target != port) {
			target.translateToParent(exposeRegion);
			target = target.getParent();
		}

		// Merge selection handles if any to the exposeRegion
		List handles = ((ISelectionHandlesEditPolicy) policy).getHandles();
		for (Iterator iter = handles.iterator(); iter.hasNext();) {
			AbstractHandle handle = (AbstractHandle) iter.next();

			Locator locator = handle.getLocator();
			locator.relocate(handle);
			exposeRegion.union(handle.getBounds().getCopy());
		}

		exposeRegion.getExpanded(5, 5);

		Dimension viewportSize = port.getClientArea().getSize();

		Point topLeft = exposeRegion.getTopLeft();
		Point bottomRight = exposeRegion.getBottomRight().translate(viewportSize.getNegated());
		Point finalLocation = new Point();
		if (viewportSize.width < exposeRegion.width)
			finalLocation.x = Math.min(bottomRight.x, Math.max(topLeft.x, port.getViewLocation().x));
		else
			finalLocation.x = Math.min(topLeft.x, Math.max(bottomRight.x, port.getViewLocation().x));

		if (viewportSize.height < exposeRegion.height)
			finalLocation.y = Math.min(bottomRight.y, Math.max(topLeft.y, port.getViewLocation().y));
		else
			finalLocation.y = Math.min(topLeft.y, Math.max(bottomRight.y, port.getViewLocation().y));

		getFigureCanvas().scrollSmoothTo(finalLocation.x, finalLocation.y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#fireSelectionChanged()
	 */
	protected void fireSelectionChanged() {

//		Display.getCurrent( ).asyncExec( new Runnable( ) {
//
//			public void run( )
//			{
		if (DeferredGraphicalViewer.this.getControl() == null
				|| DeferredGraphicalViewer.this.getControl().isDisposed()) {
			// If editor is closed, don't fire the event.
			return;
		}

		ReportRequest request = new ReportRequest(DeferredGraphicalViewer.this);
		List list = new ArrayList();
		if (getSelection() instanceof IStructuredSelection) {
			list = ((IStructuredSelection) getSelection()).toList();
		}
		request.setSelectionObject(list);
		request.setType(ReportRequest.SELECTION);

		request.setRequestConverter(new EditorReportRequestConvert());
		// SessionHandleAdapter.getInstance().getMediator().pushState();
		SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);

		super.fireSelectionChanged();
		// SessionHandleAdapter.getInstance().getMediator().popState();
//			}
//
//		} );

	}

	/**
	 * 
	 */
	public void initStepDat() {
		Viewport port = ((FigureCanvas) getControl()).getViewport();
		stepData.minX = port.getHorizontalRangeModel().getMinimum();
		stepData.maxX = port.getHorizontalRangeModel().getMaximum();
		stepData.valueX = port.getHorizontalRangeModel().getValue();
		stepData.extendX = port.getHorizontalRangeModel().getExtent();

		stepData.minY = port.getVerticalRangeModel().getMinimum();
		stepData.maxY = port.getVerticalRangeModel().getMaximum();
		stepData.valueY = port.getVerticalRangeModel().getValue();
		stepData.extendY = port.getVerticalRangeModel().getExtent();
	}

	/**
	 * @return
	 */
	public OriginStepData getOriginStepData() {
		return stepData;
	}

	public static class OriginStepData {

		public int minX, maxX, valueX, extendX;
		public int minY, maxY, valueY, extendY;
	}

	/**
	 * EditorReportRequestConvert
	 */
	public static class EditorReportRequestConvert implements IRequestConverter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.designer.core.util.mediator.request.IRequestConvert#
		 * convertSelectionToModelLisr(java.util.List)
		 */
		public List convertSelectionToModelLisr(List list) {
			List retValue = new ArrayList();
			int size = list.size();
			boolean isDummy = false;
			for (int i = 0; i < size; i++) {
				Object object = list.get(i);
				if (!(object instanceof EditPart)) {
					continue;
				}
				EditPart part = (EditPart) object;
				if (part instanceof DummyEditpart) {
					retValue.add(part.getModel());
					isDummy = true;
				} else if (isDummy) {
					break;
				} else if (part.getModel() instanceof ListBandProxy) {
					retValue.add(((ListBandProxy) part.getModel()).getSlotHandle());
				} else {
					Object model = part.getModel();
					if (model instanceof IAdaptable) {
						Object temp = ((IAdaptable) model).getAdapter(DesignElementHandle.class);
						model = temp == null ? model : temp;
					}
					if (model instanceof ReportItemHandle) {
						ReportItemHandle handle = (ReportItemHandle) model;
						if (handle.getCurrentView() != null) {
							model = handle.getCurrentView();
						}
					}
					retValue.add(model);

				}
			}

			return retValue;
		}

	}
}
