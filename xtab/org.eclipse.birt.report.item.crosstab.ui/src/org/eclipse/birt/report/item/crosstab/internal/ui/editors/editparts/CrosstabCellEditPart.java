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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.IBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.ISelectionFilter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.CellBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.CellFigure;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportFlowLayout;
import org.eclipse.birt.report.designer.internal.ui.util.bidi.BidiUIUtils;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editpolicies.CrosstabCellContainerEditPolicy;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editpolicies.CrosstabCellFlowLayoutEditPolicy;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.figures.CrosstabCellFigure;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.handles.CrosstavCellDragHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.views.provider.CrosstabCellBreadcrumbNodeProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.GroupRequest;

/**
 * Crosstab cell element editpart, the model is CrosstabCellAdapter
 */

public class CrosstabCellEditPart extends AbstractCellEditPart {

	/**
	 * The all drag column and row handle
	 */
	private List handles = null;

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public CrosstabCellEditPart(Object model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return getCrosstabCellAdapter().getModelList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .ReportElementEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ReportComponentEditPolicy() {

			protected org.eclipse.gef.commands.Command createDeleteCommand(GroupRequest deleteRequest) {
				return UnexecutableCommand.INSTANCE;
			}

			protected Command getOrphanCommand() {
				return new Command() {

				};
			}
		});
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CrosstabCellFlowLayoutEditPolicy());
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new CrosstabCellContainerEditPolicy());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .ReportElementEditPart#refreshFigure()
	 */
	// TODO now only fresh the border and the background.
	public void refreshFigure() {
		CellBorder cborder = new CellBorder();

		if (getFigure().getBorder() instanceof CellBorder) {
			cborder.setBorderInsets(((CellBorder) getFigure().getBorder()).getBorderInsets());
		}

		refreshBorder(getCrosstabCellAdapter().getDesignElementHandle(), cborder);
		refreshBackground(getCrosstabCellAdapter().getDesignElementHandle());

		((CellFigure) getFigure()).setDirectionRTL(BidiUIUtils.INSTANCE.isDirectionRTL(getModel())); // bidi_hcg
		if (getCrosstabCellAdapter().getDesignElementHandle() != null) {
			setTextAliment(getCrosstabCellAdapter().getDesignElementHandle().getPrivateStyle());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .ReportElementEditPart#activate()
	 */
	public void activate() {
		if (handles == null) {
			handles = getHandleList();
		}
		// IFigure layer = getLayer( CrosstabTableEditPart.CELL_HANDLE_LAYER );
		IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
		int size = handles.size();
		for (int i = 0; i < size; i++) {
			Figure handle = (Figure) handles.get(i);
			layer.add(handle);
		}
		super.activate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .ReportElementEditPart#deactivate()
	 */
	public void deactivate() {
		// IFigure layer = getLayer( CrosstabTableEditPart.CELL_HANDLE_LAYER );
		IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
		int size = handles.size();
		for (int i = 0; i < size; i++) {
			Figure handle = (Figure) handles.get(i);
			layer.remove(handle);
		}
		super.deactivate();
	}

	/**
	 * Gets the column and rwo drag handle
	 * 
	 * @return
	 */
	protected List getHandleList() {
		List retValue = new ArrayList();
		CrosstabTableEditPart parent = (CrosstabTableEditPart) getParent();

		int columnNumner = parent.getColumnCount();
		int rowNumer = parent.getRowCount();
		if (getColumnNumber() + getColSpan() - 1 < columnNumner) {
			CrosstavCellDragHandle column = new CrosstavCellDragHandle(this, PositionConstants.EAST,
					getColumnNumber() + getColSpan() - 1, getColumnNumber() + getColSpan());
			retValue.add(column);
		} else {
			CrosstavCellDragHandle column = new CrosstavCellDragHandle(this, PositionConstants.EAST,
					getColumnNumber() + getColSpan() - 1, getColumnNumber() + getColSpan() - 1);
			retValue.add(column);
		}
		if (getRowNumber() + getRowSpan() - 1 < rowNumer) {
			CrosstavCellDragHandle row = new CrosstavCellDragHandle(this, PositionConstants.SOUTH,
					getRowNumber() + getRowSpan() - 1, getRowNumber() + getRowSpan());
			retValue.add(row);
		} else {
			CrosstavCellDragHandle row = new CrosstavCellDragHandle(this, PositionConstants.SOUTH,
					getRowNumber() + getRowSpan() - 1, getRowNumber() + getRowSpan() - 1);
			retValue.add(row);
		}
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		CellFigure figure = new CrosstabCellFigure();
		ReportFlowLayout rflayout = new ReportFlowLayout();
		figure.setLayoutManager(rflayout);
		figure.setOpaque(false);

		return figure;
	}

	/**
	 * @return
	 */
	protected CrosstabCellAdapter getCrosstabCellAdapter() {
		return (CrosstabCellAdapter) getModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutCell#
	 * getColSpan()
	 */
	public int getColSpan() {
		return getCrosstabCellAdapter().getColumnSpan();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutCell#
	 * getColumnNumber()
	 */
	public int getColumnNumber() {
		return getCrosstabCellAdapter().getColumnNumber();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutCell#
	 * getRowNumber()
	 */
	public int getRowNumber() {
		return getCrosstabCellAdapter().getRowNumber();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutCell#
	 * getRowSpan()
	 */
	public int getRowSpan() {
		return getCrosstabCellAdapter().getRowSpan();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createChild(java.lang.Object)
	 */
	protected EditPart createChild(Object model) {
		EditPart part = CrosstabGraphicsFactory.INSTANCEOF.createEditPart(this, model);
		if (part != null) {
			return part;
		}
		return super.createChild(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getAdapter(java.lang
	 * .Class)
	 */
	public Object getAdapter(Class key) {
		if (key == ISelectionFilter.class) {
			return new ISelectionFilter() {

				public List filterEditpart(List editparts) {
					int size = editparts.size();
					List copy = new ArrayList(editparts);

					boolean hasCell = false;
					boolean hasOther = false;
					for (int i = 0; i < size; i++) {
						if (editparts.get(i) instanceof CrosstabCellEditPart) {
							hasCell = true;
						} else {
							hasOther = true;
						}
					}
					if (hasCell && hasOther) {

						for (int i = 0; i < size; i++) {
							if (editparts.get(i) instanceof CrosstabCellEditPart) {
								copy.remove(editparts.get(i));
							}
						}
					}
					editparts = copy;
					return editparts;
				}

			};
		}
		if (key == IBreadcrumbNodeProvider.class) {
			return new CrosstabCellBreadcrumbNodeProvider();
		}
		return super.getAdapter(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .ReportElementEditPart#contentChange(java.util.Map)
	 */
	protected void contentChange(Map info) {
		((ReportElementEditPart) getParent()).refresh();
		if (getParent() != null) {
			super.contentChange(info);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractEditPart#showTargetFeedback(org.eclipse
	 * .gef.Request)
	 */
	public void showTargetFeedback(Request request) {
		if (this.getSelected() == 0 && isActive() && request.getType() == RequestConstants.REQ_SELECTION) {

			if (isFigureLeft(request)) {
				this.getViewer().setCursor(ReportPlugin.getDefault().getLeftCellCursor());
			} else {
				this.getViewer().setCursor(ReportPlugin.getDefault().getRightCellCursor());
			}
		}
		super.showTargetFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractEditPart#eraseTargetFeedback(org.eclipse
	 * .gef.Request)
	 */
	public void eraseTargetFeedback(Request request) {
		if (isActive()) {
			this.getViewer().setCursor(null);
		}
		super.eraseTargetFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .ReportElementEditPart#isinterestSelection(java.lang.Object)
	 */
	public boolean isinterestSelection(Object object) {
		if (object instanceof DesignElementHandle) {
			return getCrosstabCellAdapter().getCrosstabCellHandle().getModelHandle() == object;
		}
		return super.isinterestSelection(object);
	}

	@Override
	public boolean isFixLayout() {
		return ((CrosstabTableEditPart) getParent()).isFixLayout();
	}
}
