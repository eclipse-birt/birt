/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
