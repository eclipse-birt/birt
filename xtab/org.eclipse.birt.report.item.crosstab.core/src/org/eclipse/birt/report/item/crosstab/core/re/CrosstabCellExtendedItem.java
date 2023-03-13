/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.re;

import org.eclipse.birt.report.engine.extension.IExtendedItem;
import org.eclipse.birt.report.engine.extension.IReportEventHandler;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemPreparation;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IReportItemQuery;
import org.eclipse.birt.report.item.crosstab.core.script.internal.handler.CrosstabHandlerCache;

/**
 * CrosstabCellExtendedItem
 */
public class CrosstabCellExtendedItem implements IExtendedItem {

	private CrosstabHandlerCache handlerCache;

	CrosstabCellExtendedItem(CrosstabHandlerCache handlerCache) {
		this.handlerCache = handlerCache;
	}

	@Override
	public IReportEventHandler createEventHandler() {
		return new CrosstabCellReportEventHandler(handlerCache);
	}

	@Override
	public IReportItemExecutor createExecutor() {
		return null;
	}

	@Override
	public IReportItemPreparation createPreparation() {
		return null;
	}

	@Override
	public IReportItemPresentation createPresentation() {
		return null;
	}

	@Override
	public IReportItemQuery createQuery() {
		return null;
	}

	@Override
	public void release() {
		handlerCache = null;
	}

}
