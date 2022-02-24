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

import org.eclipse.birt.report.designer.core.commands.DeleteCommand;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.ReportFigureUtilities;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DataEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.LabelDirectEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.ReportElementDragTracker;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.figures.FirstLevelHandleDataItemFigure;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.ICrosstabCellAdapterFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Menu;

/**
 * The first level handle dataitem editpart.
 */
public class FirstLevelHandleDataItemEditPart extends DataEditPart {
	private MenuManager manager;

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public FirstLevelHandleDataItemEditPart(Object model) {
		super(model);
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ReportComponentEditPolicy() {
			protected org.eclipse.gef.commands.Command createDeleteCommand(GroupRequest deleteRequest) {
				// Object model = ((EditPart) parts.get( i ) ).getModel( ) ;
				Object parent = this.getHost().getParent().getModel();
				if (parent instanceof CrosstabCellAdapter) {
					if (ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE
							.equals(((CrosstabCellAdapter) parent).getPositionType())
							|| ICrosstabCellAdapterFactory.CELL_MEASURE
									.equals(((CrosstabCellAdapter) parent).getPositionType())) {
						return new Command() {
						};
					}
				}
				DeleteCommand command = new DeleteCommand(this.getHost().getModel());
				return command;
			}
		});

		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new LabelDirectEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * DataEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		FirstLevelHandleDataItemFigure label = new FirstLevelHandleDataItemFigure();
		label.setLayoutManager(new StackLayout());
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#refreshBackgroundColor(org.eclipse.birt.report.model.
	 * api.DesignElementHandle)
	 */
	protected void refreshBackgroundColor(DesignElementHandle handle) {
		super.refreshBackgroundColor(handle);
//		Object obj = handle.getProperty( StyleHandle.BACKGROUND_COLOR_PROP );
//
//		if ( obj == null )
//		{
//			getFigure( ).setBackgroundColor( ReportColorConstants.TableGuideFillColor );
//			getFigure( ).setOpaque( true );
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request req) {
		DragEditPartsTracker track = new ReportElementDragTracker(this) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.gef.tools.SelectEditPartTracker#handleButtonDown(int)
			 */
			protected boolean handleButtonDown(int button) {
				if (getCurrentViewer() instanceof DeferredGraphicalViewer) {
					((DeferredGraphicalViewer) getCurrentViewer()).initStepDat();
				}
				boolean bool = super.handleButtonDown(button);

				if ((button == 3 || button == 1))
				// && isInState(STATE_INITIAL))
				{
					if (getSourceEditPart() instanceof FirstLevelHandleDataItemEditPart) {
						FirstLevelHandleDataItemEditPart first = (FirstLevelHandleDataItemEditPart) getSourceEditPart();
						if (first.contains(getLocation())) {
							// MenuManager manager = new LevelCrosstabPopMenuProvider( getViewer( ) );
							manager.createContextMenu(getViewer().getControl());
							Menu menu = manager.getMenu();

							menu.setVisible(true);
							return true;
						}
					}
				}
				return bool;
			}
		};
		return track;
	}

	/**
	 * The point if in the triangle.
	 * 
	 * @param pt
	 * @return
	 */
	public boolean contains(Point pt) {
		FirstLevelHandleDataItemFigure figure = (FirstLevelHandleDataItemFigure) getFigure();
		Rectangle bounds = figure.getClientArea();
		Point center = figure.getCenterPoint(bounds);

		figure.translateToAbsolute(center);
		return ReportFigureUtilities.isInTriangle(center, FirstLevelHandleDataItemFigure.TRIANGLE_HEIGHT, pt);
	}

	/**
	 * @param manager
	 */
	public void setManager(MenuManager manager) {
		this.manager = manager;
	}
}
