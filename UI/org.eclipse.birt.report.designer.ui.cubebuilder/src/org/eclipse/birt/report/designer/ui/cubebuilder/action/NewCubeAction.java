/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.page.CubeBuilder;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

public class NewCubeAction extends Action {

	public static final String ID = "org.eclipse.birt.report.designer.ui.actions.NewCubeAction"; //$NON-NLS-1$

	/**
	 * 
	 */
	public NewCubeAction() {
		super();
		setId(ID);
	}

	/**
	 * @param text
	 */
	public NewCubeAction(String text) {
		super(text);
		setId(ID);
	}

	/**
	 * @param text
	 * @param style
	 */
	public NewCubeAction(String text, int style) {
		super(text, style);
		setId(ID);
	}

	/**
	 * @param text
	 * @param image
	 */
	public NewCubeAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("New cube action >> Run ..."); //$NON-NLS-1$
		}
		if (SessionHandleAdapter.getInstance().getReportDesignHandle() == null) {
			return;
		}
		// Get the list of data sets before inserting a new Data Set
		List existingCubes = getCubes();

		CommandStack stack = getActionStack();
		stack.startPersistentTrans(Messages.getString("NewCubeAction.trans.cube.new")); //$NON-NLS-1$

		TabularCubeHandle newCube = DesignElementFactory.getInstance()
				.newTabularCube(Messages.getString("NewCubeAction.DataCube")); //$NON-NLS-1$

		boolean isFailed = true;
		try {
			SessionHandleAdapter.getInstance().getReportDesignHandle().getCubes().add(newCube);

			CubeBuilder builder = new CubeBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(), newCube);

			String wizardTitle = Messages.getString("cube.new");//$NON-NLS-1$
			builder.setTitle(wizardTitle);

			int result = builder.open();

			notifyResult(result == WizardDialog.OK);

			if (result == WizardDialog.OK) {
				isFailed = false;
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}

		if (!isFailed) {
			stack.commit();
		} else {
			stack.rollback();
			return;
		}
		List newCubes = getCubes();
		CubeHandle cube = findNewCube(existingCubes, newCubes);

		ReportRequest request = new ReportRequest(ReportRequest.CREATE_ELEMENT);
		List selectionObjects = new ArrayList();
		selectionObjects.add(cube);
		request.setSelectionObject(selectionObjects);
		SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);

	}

	private CommandStack getActionStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	private List getCubes() {
		return SessionHandleAdapter.getInstance().getReportDesignHandle().getAllCubes();
	}

	private CubeHandle findNewCube(List existingCubes, List newCubes) {
		for (int i = 0; i < newCubes.size(); i++) {
			if (!existingCubes.contains(newCubes.get(i))) {
				return (CubeHandle) newCubes.get(i);
			}
		}
		return null;
	}
}