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

package org.eclipse.birt.report.designer.internal.lib.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.birt.report.designer.core.commands.CreateCommand;
import org.eclipse.birt.report.designer.core.model.LibraryHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.lib.commands.SetCurrentEditModelCommand;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.ReportDesignMarginBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GraphicsViewModelEventProcessor;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportDesignEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportContainerEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportFlowLayoutEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportRootFigure;
import org.eclipse.birt.report.designer.internal.ui.layout.AbstractPageFlowLayout;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportDesignLayout;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.widgets.Display;

/**
 * This is the content edit part for Library. All other library elements puts on
 * to it
 */
public class LibraryReportDesignEditPart extends ReportDesignEditPart implements PropertyChangeListener {

	private static final Insets INSETS = new Insets(30, 30, 30, 30);
	private static final Dimension DEFAULTSIZE = new Dimension(800, 1000);

	/**
	 * @param obj
	 */
	public LibraryReportDesignEditPart(Object obj) {
		super(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		ReportRootFigure figure = new ReportRootFigure();

		figure.setOpaque(true);
		figure.setShowMargin(showMargin);

		// LibraryReportDesignLayout layout = new LibraryReportDesignLayout(this);
		ReportDesignLayout layout = new ReportDesignLayout(this);

		Dimension size = DEFAULTSIZE;

		Rectangle bounds = new Rectangle(0, 0, size.width - 1, size.height - 1);

		layout.setInitSize(bounds);

		figure.setLayoutManager(layout);
		ReportDesignMarginBorder border = new ReportDesignMarginBorder(INSETS);
		border.reInitStyle();
		figure.setBorder(border);

		figure.setBounds(bounds.getCopy());

		return figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.ui.editor.edit.ReportElementEditPart#
	 * getModelChildren()
	 */
	protected List getModelChildren() {

		return HandleAdapterFactory.getInstance().getLibraryHandleAdapter(getModel()).getChildren();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.internal.ui.editors.schematic.editparts.
	 * AbstractReportEditPart#refreshFigure()
	 */
	public void refreshFigure() {

		ReportRootFigure figure = (ReportRootFigure) getFigure();
		figure.setShowMargin(showMargin);
		Dimension size = DEFAULTSIZE;

		Rectangle bounds = new Rectangle(0, 0, size.width - 1, size.height - 1);

		((AbstractPageFlowLayout) getFigure().getLayoutManager()).setInitSize(bounds);

		((AbstractPageFlowLayout) getFigure().getLayoutManager()).setInitInsets(INSETS);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportDesignEditPart#activate()
	 */
	public void activate() {
		HandleAdapterFactory.getInstance().getLibraryHandleAdapter(getModel()).addPropertyChangeListener(this);
		super.activate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportDesignEditPart#deactivate()
	 */
	public void deactivate() {
		HandleAdapterFactory.getInstance().getLibraryHandleAdapter(getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	public void propertyChange(final PropertyChangeEvent evt) {
		if (LibraryHandleAdapter.CURRENTMODEL.equals(evt.getPropertyName())
				|| LibraryHandleAdapter.CREATE_ELEMENT.equals(evt.getPropertyName())) {

			refresh();
			Display.getCurrent().asyncExec(new Runnable() {

				public void run() {
					Object model = evt.getNewValue();
					Object editpart = getViewer().getEditPartRegistry().get(model);
					if (editpart instanceof EditPart) {
						getViewer().flush();
						if (!(editpart instanceof EmptyEditPart)) {
							getViewer().select((EditPart) editpart);
						}
					}
					if (editpart != null) {
						getViewer().reveal((EditPart) editpart);

						if (LibraryHandleAdapter.CREATE_ELEMENT.equals(evt.getPropertyName())) {
							Request request = new Request(ReportRequest.CREATE_ELEMENT);
							if (((EditPart) editpart).understandsRequest(request)) {
								((EditPart) editpart).performRequest(request);
							}
						}
					}

				}
			});
		}

	}

	private boolean isModelInModuleHandle() {
		List list = getModelChildren();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Object obj = list.get(i);
			if (obj instanceof DesignElementHandle) {
				DesignElementHandle handle = (DesignElementHandle) obj;
				if (handle.getRoot() == null
						&& (!getChildren().isEmpty() && ((EditPart) getChildren().get(0)).getModel().equals(handle))) {
					return false;
				}
			}
		}
		return true;
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ReportFlowLayoutEditPolicy() {

			protected org.eclipse.gef.commands.Command getCreateCommand(CreateRequest request) {
//						List list = getHost().getChildren();
//						Boolean direct = (Boolean) request.getExtendedData()
//								.get(DesignerConstants.DIRECT_CREATEITEM);
//						if (list.size() != 0
//								&& !(list.get(0) instanceof EmptyEditPart)
//								&& (direct == null || !direct.booleanValue())) {
//							return UnexecutableCommand.INSTANCE;
//						}
				// EditPart after = getInsertionReference( request );
				// final DesignElementHandle newObject =
				// (DesignElementHandle) request.getExtendedData( )
				// .get( DesignerConstants.KEY_NEWOBJECT );

				CreateCommand command = new CreateCommand(request.getExtendedData());

				Object model = this.getHost().getModel();
				if (model instanceof SlotHandle) {
					command.setParent(model);
				} else if (model instanceof ListBandProxy) {
					command.setParent(((ListBandProxy) model).getSlotHandle());
				} else {
					command.setParent(model);
				}
				// No previous edit part
				// if ( after != null )
				// {
				// command.setAfter( after.getModel( ) );
				// }

				return command;
			}
		});

		installEditPolicy(EditPolicy.CONTAINER_ROLE, new ReportContainerEditPolicy());
	}

	protected void notifyModelChange(Object focus) {
		super.notifyModelChange(focus);
		if (!isModelInModuleHandle()) {
			SetCurrentEditModelCommand command = new SetCurrentEditModelCommand(null);
			command.execute();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getAdapter(java.lang.
	 * Class)
	 */
	public Object getAdapter(Class key) {
		if (key == IModelEventProcessor.class) {
			return new GraphicsViewModelEventProcessor(this) {
				public void clear() {
					super.clear();
					Object oldObj = HandleAdapterFactory.getInstance().getLibraryHandleAdapter().getOldEditorModel();
					SetCurrentEditModelCommand c = new SetCurrentEditModelCommand(oldObj);
					Object obj = HandleAdapterFactory.getInstance().getLibraryHandleAdapter().getCurrentEditorModel();
					if (obj instanceof DesignElementHandle && ((DesignElementHandle) obj).getContainer() != null) {
						c = new SetCurrentEditModelCommand(obj);
					} else if (oldObj instanceof DesignElementHandle
							&& ((DesignElementHandle) oldObj).getContainer() == null) {
						c = new SetCurrentEditModelCommand(null);
					}
					c.execute();
				}
			};
		}
		return super.getAdapter(key);
	}

	@Override
	protected void updateLayoutPreference() {
		// must do nothing now, because libraryHandle don't support the property
	}

	@Override
	public void refreshMarginBorder(ReportDesignMarginBorder border) {
		border.reInitStyle();
		getFigure().setBorder(border);
	}
}
