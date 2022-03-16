/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.actionhandler;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.BirtUtility;

/**
 * This action handler is to handle cancel current task.
 * <p>
 * Task should be engine task including RunTask,RenderTask,RunAndRenderTask and
 * so on.
 * <p>
 * When viewer is processing an engine task, put it in current session using
 * unique id from request.So this action handler can find out current task
 * according to an unique id.
 */
public class BirtCancelTaskActionHandler extends AbstractBaseActionHandler {

	/**
	 * Constructor.
	 *
	 * @param context
	 * @param operation
	 */
	public BirtCancelTaskActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	/**
	 * execute the action
	 */
	@Override
	protected void __execute() throws Exception {
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean();
		assert attrBean != null;

		// cancel task
		BirtUtility.cancelTask(context.getRequest(), attrBean.getTaskId());
		handleUpdate();
	}

	/**
	 * After done action,update response
	 *
	 */
	protected void handleUpdate() {
		// do nothing
	}

	/**
	 * Implement getReportService()
	 */
	@Override
	protected IViewerReportService getReportService() {
		return null;
	}
}
