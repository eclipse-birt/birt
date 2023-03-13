/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;

public interface IReportExecutor {

	IReportItemExecutor createPageExecutor(long pageNumber, MasterPageDesign pageDesign) throws BirtException;

	IReportContent execute() throws BirtException;

	/**
	 * close the executor, if the executor is closed, all sub executor will be
	 * termiante also.
	 *
	 * @throws BirtException
	 */
	void close() throws BirtException;

	/**
	 * does the executor has child executor
	 *
	 * @return
	 * @throws BirtException
	 */
	boolean hasNextChild() throws BirtException;

	IReportItemExecutor getNextChild() throws BirtException;

}
