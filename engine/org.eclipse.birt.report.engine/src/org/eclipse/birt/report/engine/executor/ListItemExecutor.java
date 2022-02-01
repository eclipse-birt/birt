/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.ListItemDesign;

/**
 * Defines execution logic for a List report item.
 * 
 */
public class ListItemExecutor extends ListingElementExecutor {

	/**
	 * @param context execution context
	 * @param visitor visitor object for driving the execution
	 */
	protected ListItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.LISTITEM);
	}

	/**
	 * Execute a listint and create the contents.
	 * 
	 * List create a serials of contents.
	 * 
	 * The execution process is:
	 * 
	 * <li>create an container which will contain all the contents it creates.
	 * <li>push it into the stack
	 * <li>open query
	 * <li>process action, bookmark, style and visibility
	 * <li>call the onCreate if necessary
	 * <li>call emitter to start the list
	 * <li>access the query
	 * <li>call emitter to end the list
	 * <li>close the query.
	 * <li>pop up the container.
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#load(org.eclipse.birt.report.engine.ir.ReportItemDesign,
	 *      org.eclipse.birt.report.engine.emitter.IReportEmitter)
	 */
	public IContent execute() {
		ListItemDesign listDesign = (ListItemDesign) getDesign();

		IListContent listContent = report.createListContent();
		setContent(listContent);

		executeQuery();

		initializeContent(listDesign, listContent);

		processAction(listDesign, listContent);
		processBookmark(listDesign, listContent);
		processStyle(listDesign, listContent);
		processVisibility(listDesign, listContent);
		processUserProperties(listDesign, listContent);

		if (context.isInFactory()) {
			handleOnCreate(listContent);
		}
		startTOCEntry(listContent);

		// create an empty result set to handle the showIfBlank
		boolean showIfBlank = "true".equalsIgnoreCase(content.getStyle().getShowIfBlank());
		if (showIfBlank && rsetEmpty) {
			createQueryForShowIfBlank();
		}

		// prepare to execute the children
		prepareToExecuteChildren();

		return listContent;
	}

	public void close() throws BirtException {
		finishTOCEntry();
		closeQuery();
		super.close();
	}

	public IReportItemExecutor getNextChild() {
		IReportItemExecutor executor = super.getNextChild();
		if (executor instanceof ListBandExecutor) {
			ListBandExecutor bandExecutor = (ListBandExecutor) executor;
			bandExecutor.setListingExecutor(this);
		}
		return executor;
	}
}
