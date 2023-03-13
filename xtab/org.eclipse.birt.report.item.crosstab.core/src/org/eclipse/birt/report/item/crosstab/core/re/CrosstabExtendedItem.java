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
import org.eclipse.birt.report.item.crosstab.core.re.executor.CrosstabReportItemExecutor;

/**
 * CrosstabExtendedItem
 */
public class CrosstabExtendedItem implements IExtendedItem {

	@Override
	public IReportEventHandler createEventHandler() {
		return new CrosstabReportEventHandler();
	}

	@Override
	public IReportItemExecutor createExecutor() {
		return new CrosstabReportItemExecutor();
	}

	@Override
	public IReportItemPreparation createPreparation() {
		return new CrosstabReportItemPreparation();
	}

	@Override
	public IReportItemPresentation createPresentation() {
		return null;
	}

	@Override
	public IReportItemQuery createQuery() {
		return new CrosstabReportItemQuery();
	}

	@Override
	public void release() {
	}

}
