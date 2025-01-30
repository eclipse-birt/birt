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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.swt.widgets.Display;

/**
 * Customizes eclipse creation tool to popup element builders.
 */
public class ReportCreationTool extends CreationTool {

	private static final String MODEL_CREATE_ELEMENT_TRANS = Messages
			.getString("ReportCreationTool.ModelTrans.CreateElement"); //$NON-NLS-1$

	private AbstractToolHandleExtends preHandle;

	private boolean isCreating = false;

	/**
	 * Constructor
	 *
	 * @param factory
	 * @param preHandle
	 */
	public ReportCreationTool(CreationFactory factory, AbstractToolHandleExtends preHandle) {
		super(factory);
		this.preHandle = preHandle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.tools.CreationTool#performCreation(int)
	 */
	@Override
	protected void performCreation(int button) {
		isCreating = true;
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

		stack.startTrans(MODEL_CREATE_ELEMENT_TRANS);

		Command command = getCurrentCommand();
		boolean hasCommand = command != null && command.canExecute();

		if (getTargetEditPart() != null) {
			if (preHandle != null) {
				preHandle.setRequest(this.getCreateRequest());
				preHandle.setTargetEditPart(getTargetEditPart());

				if (hasCommand) {
					if (!preHandle.preHandleMouseUp()) {
						// if a popup dialog was cancelled.
						// All create logic should be finished there.
						stack.rollback();
						handleFinished();
						return;
					}
				}
			}
		}

		super.performCreation(button);
		final EditPartViewer viewer = getCurrentViewer();
		// fix bugzilla#145284
		if (!hasCommand || (preHandle != null && !preHandle.postHandleCreation())) {
			stack.rollback();
			handleFinished();
			return;
		}

		stack.commit();
		selectAddedObject(viewer);
		isCreating = false;
	}

	/**
	 * Performs the creation. Runs the creation via simulating the mouse move event.
	 *
	 * @param editPart the current EditPart
	 */
	public void performCreation(EditPart editPart) {
		if (editPart == null) {
			return;
		}
		setTargetEditPart(editPart);
		boolean validateCurr = handleValidatePalette(getFactory().getObjectType(), getTargetEditPart());
		if (!validateCurr) {
			// Validates the parent part
			setTargetEditPart(editPart.getParent());
		}
		if (validateCurr || handleValidatePalette(getFactory().getObjectType(), getTargetEditPart())) {
			// Sets the insertion point
			IFigure figure = ((GraphicalEditPart) editPart).getFigure();
			Rectangle rect = figure.getBounds().getCopy();
			figure.translateToAbsolute(rect);
			Point point = rect.getRight();
			point.performTranslate(1, 1);
			getCreateRequest().setLocation(point);

			setCurrentCommand(getCommand());
			performCreation(MOUSE_BUTTON1);
		}
		eraseTargetFeedback();
	}

	/*
	 * Add the newly created object to the viewer's selected objects.
	 */
	private void selectAddedObject(final EditPartViewer viewer) {
		final Object model = getNewObjectFromRequest();
		// final EditPartViewer viewer = getCurrentViewer( );
		selectAddedObject(model, viewer);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.tools.TargetingTool#getTargetRequest()
	 */
	@Override
	public CreateRequest getTargetRequest() {
		return super.getTargetRequest();
	}

	/**
	 * Gets the new Object from request
	 *
	 * @return
	 */
	public Object getNewObjectFromRequest() {
		return getCreateRequest().getExtendedData().get(DesignerConstants.KEY_NEWOBJECT);
	}

	/**
	 * Selects or clicks added object
	 *
	 * @param model  new object, null will do nothing
	 * @param viewer edit part viewer, null will do nothing
	 */
	public static void selectAddedObject(final Object model, final EditPartViewer viewer) {
		selectAddedObject(model, viewer, new Request(ReportRequest.CREATE_ELEMENT));
	}

	/**
	 * @param model
	 * @param viewer
	 * @param edit
	 */
	public static void selectAddedObject(final Object model, final EditPartViewer viewer, boolean edit) {
		selectAddedObject(model, viewer, new Request(ReportRequest.CREATE_ELEMENT), edit);
	}

	/**
	 * @param model
	 * @param viewer
	 * @param request
	 */
	public static void selectAddedObject(final Object model, final EditPartViewer viewer, final Request request) {
		selectAddedObject(model, viewer, request, true);
	}

	/**
	 * Selects or clicks added object
	 *
	 * @param model   new object, null will do nothing
	 * @param viewer  edit part viewer, null will do nothing
	 * @param request the request sended to EditPart
	 */
	public static void selectAddedObject(final Object model, final EditPartViewer viewer, final Request request,
			final boolean edit) {
		if (model == null || viewer == null) {
			return;
		}

		Display.getCurrent().asyncExec(new Runnable() {

			@Override
			public void run() {
				// modify
				Object editpart = viewer.getEditPartRegistry().get(model);

				if (editpart instanceof EditPart) {
					viewer.flush();
					viewer.select((EditPart) editpart);

					if (edit && ((EditPart) editpart).understandsRequest(request)) {
						((EditPart) editpart).performRequest(request);
					}

					viewer.reveal((EditPart) editpart);

				} else {
					List list = new ArrayList();
					list.add(model);
					ReportRequest r = new ReportRequest();
					r.setType(ReportRequest.CREATE_ELEMENT);
					r.getExtendedData().putAll(request.getExtendedData());
					r.setSelectionObject(list);
					SessionHandleAdapter.getInstance().getMediator().notifyRequest(r);
				}

			}
		});
	}

	protected static String getCreationType(String template) {
		String type = ""; //$NON-NLS-1$
		if (IReportElementConstants.REPORT_ELEMENT_IMAGE.equalsIgnoreCase(template)) {
			type = ReportDesignConstants.IMAGE_ITEM;
		} else if (IReportElementConstants.REPORT_ELEMENT_TABLE.equalsIgnoreCase(template)) {
			type = ReportDesignConstants.TABLE_ITEM;
		} else if (IReportElementConstants.REPORT_ELEMENT_TEXTDATA.equalsIgnoreCase(template)) {
			type = ReportDesignConstants.TEXT_DATA_ITEM;
		}

		else if (IReportElementConstants.REPORT_ELEMENT_TEXT.equalsIgnoreCase(template)
				|| IReportElementConstants.AUTOTEXT_DATE.equalsIgnoreCase(template)
				|| IReportElementConstants.AUTOTEXT_CREATEDON.equalsIgnoreCase(template)
				|| IReportElementConstants.AUTOTEXT_CREATEDBY.equalsIgnoreCase(template)
				|| IReportElementConstants.AUTOTEXT_FILENAME.equalsIgnoreCase(template)
				|| IReportElementConstants.AUTOTEXT_LASTPRINTED.equalsIgnoreCase(template)) {
			type = ReportDesignConstants.TEXT_ITEM;
		} else if (IReportElementConstants.AUTOTEXT_TOTAL_PAGE_COUNT.equalsIgnoreCase(template)
				|| IReportElementConstants.AUTOTEXT_PAGE.equalsIgnoreCase(template)
				|| IReportElementConstants.AUTOTEXT_VARIABLE.equalsIgnoreCase(template)) {
			type = ReportDesignConstants.AUTOTEXT_ITEM;
		} else if (IReportElementConstants.AUTOTEXT_AUTHOR_PAGE_DATE.equalsIgnoreCase(template)
				|| IReportElementConstants.AUTOTEXT_CONFIDENTIAL_PAGE.equalsIgnoreCase(template)
				|| IReportElementConstants.REPORT_ELEMENT_GRID.equalsIgnoreCase(template)
				|| IReportElementConstants.AUTOTEXT_PAGEXOFY.equalsIgnoreCase(template)) {
			type = ReportDesignConstants.GRID_ITEM;
		} else if (IReportElementConstants.REPORT_ELEMENT_LABEL.equalsIgnoreCase(template)) {
			type = ReportDesignConstants.LABEL_ITEM;
		} else if (IReportElementConstants.REPORT_ELEMENT_DATA.equalsIgnoreCase(template)) {
			type = ReportDesignConstants.DATA_ITEM;
		} else if (IReportElementConstants.REPORT_ELEMENT_LIST.equalsIgnoreCase(template)) {
			type = ReportDesignConstants.LIST_ITEM;
		} else if (template.startsWith(IReportElementConstants.REPORT_ELEMENT_EXTENDED)) {
			// type = ReportDesignConstants.EXTENDED_ITEM;
			type = template.substring(IReportElementConstants.REPORT_ELEMENT_EXTENDED.length());
		}
		return type;
	}

	/**
	 * Validates specified creation type can be inserted to layout editor.
	 *
	 * @param objectType     specified creation type
	 * @param targetEditPart
	 * @return validate result
	 */
	public static boolean handleValidatePalette(Object objectType, EditPart targetEditPart) {
		// bug #278597
		ModuleHandle reportHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		if (reportHandle instanceof LibraryHandle && IReportElementConstants.AUTOTEXT_VARIABLE.equals(objectType)) {
			return false;
		}
		return objectType instanceof String && targetEditPart != null
				&& DNDUtil.handleValidateTargetCanContainType(targetEditPart.getModel(),
						ReportCreationTool.getCreationType((String) objectType))
				&& DNDUtil.handleValidateTargetCanContainMore(targetEditPart.getModel(), 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.tools.CreationTool#handleMove()
	 */
	@Override
	protected boolean handleMove() {
		boolean validateTrue = false;
		if (isCreating) {
			return true;
		}
		updateTargetUnderMouse();

		if (getTargetEditPart().getModel() instanceof LibraryHandle) {
			// return true;
		}

		if (getTargetEditPart() != null) {
			validateTrue = handleValidatePalette(getFactory().getObjectType(), getTargetEditPart());
		}

		if (validateTrue) {
			updateTargetRequest();
			setCurrentCommand(getCommand());
			showTargetFeedback();
		} else {
			setCurrentCommand(null);
		}
		return validateTrue;
	}
}
