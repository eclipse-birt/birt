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

package org.eclipse.birt.report.engine.internal.document;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.presentation.IPageHint;

/**
 * writer used to write the page hints.
 *
 */
public interface IPageHintWriter extends IPageHintConstant {

	void close();

	void writePageHint(IPageHint pageHint) throws IOException;

	void writeTotalPage(long totalPage) throws IOException;

	void writePageVariables(Collection<PageVariable> variables) throws IOException;
}
