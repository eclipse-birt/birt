/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public IReportEventHandler createEventHandler() {
		return new CrosstabReportEventHandler();
	}

	public IReportItemExecutor createExecutor() {
		return new CrosstabReportItemExecutor();
	}

	public IReportItemPreparation createPreparation() {
		return new CrosstabReportItemPreparation();
	}

	public IReportItemPresentation createPresentation() {
		return null;
	}

	public IReportItemQuery createQuery() {
		return new CrosstabReportItemQuery();
	}

	public void release() {
	}

}
