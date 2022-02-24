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

package org.eclipse.birt.report.designer.internal.ui.command;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.IReportEditor;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ImageEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.LabelEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.AbstractMultiPageEditor;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class BaseInsertHandler extends SelectionHandler {
	private String insertType;

	protected SlotHandle slotHandle;

	private Object model;

	protected static final String STACK_MSG_INSERT_ELEMENT = Messages
			.getString("BaseInsertMenuAction.stackMsg.insertElement"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		if (Policy.TRACING_ACTIONS) {
			System.out.println("Insert action >> Run ..."); //$NON-NLS-1$
		}
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(STACK_MSG_INSERT_ELEMENT);

		boolean retValue = initializeVariable(event);
		if (retValue == false) {
			return Boolean.FALSE;
		}

		try {
			final Request req = insertElement();
			if (req != null) {
				stack.commit();
				selectElement(req.getExtendedData().get(IRequestConstants.REQUEST_KEY_RESULT), true);
				return Boolean.TRUE;
			}
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
		stack.rollback();

		return Boolean.FALSE;
	}

	protected boolean initializeVariable(ExecutionEvent event) {
		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		Object obj = UIUtil.getVariableFromContext(context, ICommandParameterNameContants.BASE_INSERT_TYPE_NAME);
		if (obj == null || (obj instanceof String)) {
			insertType = (String) obj;
		}
		if (insertType == null) {
			return false;
		}

		obj = UIUtil.getVariableFromContext(context, ICommandParameterNameContants.BASE_INSERT_SLOT_HANDLE_NAME);
		if (obj == null || (obj instanceof SlotHandle)) {
			slotHandle = (SlotHandle) obj;
		}
		if (slotHandle == null) {
			return false;
		}

		obj = UIUtil.getVariableFromContext(context, ICommandParameterNameContants.BASE_INSERT_MODEL_NAME);
		if (obj == null) {
			return false;
		}
		model = obj;

		return true;
	}

	protected Request insertElement() throws Exception {
		Request request = new Request(IRequestConstants.REQUEST_TYPE_INSERT);
		Map extendsData = new HashMap();
		extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_SLOT, slotHandle);

		extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_TYPE, insertType);

		extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_POSITION, InsertAction.BELOW);

		request.setExtendedData(extendsData);

		if (ProviderFactory.createProvider(slotHandle.getElementHandle()).performRequest(model, request)) {
			return request;
		}
		return null;
	}

	protected void selectElement(final Object element, final boolean edit) {
		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				if (element instanceof ReportItemHandle) {
					IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService()
							.getActivePart();
					IEditorPart epart = null;
					if (part instanceof AbstractMultiPageEditor) {
						epart = ((AbstractMultiPageEditor) part).getActivePageInstance();
					} else if (part instanceof IReportEditor) {
						IEditorPart activeEditor = ((IReportEditor) part).getEditorPart();
						if (activeEditor instanceof AbstractMultiPageEditor) {
							epart = ((AbstractMultiPageEditor) activeEditor).getActivePageInstance();
						}
					}

					if (epart instanceof GraphicalEditorWithFlyoutPalette) {
						GraphicalViewer viewer = ((GraphicalEditorWithFlyoutPalette) epart).getGraphicalViewer();
						Object cpart = viewer.getEditPartRegistry().get(element);

						if (cpart instanceof EditPart) {
							viewer.flush();
							viewer.select((EditPart) cpart);
						}

						if (edit && cpart instanceof LabelEditPart) {
							((LabelEditPart) cpart).performDirectEdit();
						} else if (edit && cpart instanceof ImageEditPart) {
							((ImageEditPart) cpart).performDirectEdit();
						}
						// fix bugzilla#145284
						// TODO check extension setting here to decide if popup
						// the builder
						// else if ( edit
						// && cpart instanceof ExtendedEditPart )
						// {
						// ( (ExtendedEditPart) cpart ).performDirectEdit( );
						// }
					}
				}
			}
		});
	}

}
