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

package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;

/**
 *
 * table band content object There are three type: table header, table footer,
 * table body
 *
 */
public class TableBandContent extends AbstractBandContent implements ITableBandContent {

	TableBandContent(ITableBandContent band) {
		super(band);
	}

	TableBandContent(IReportContent report) {
		super(report);
	}

	@Override
	public int getContentType() {
		return TABLE_BAND_CONTENT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.ReportElementContent#accept(org.
	 * eclipse.birt.report.engine.content.ReportContentVisitor)
	 */
	@Override
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitTableBand(this, value);

	}

	@Override
	protected IContent cloneContent() {
		return new TableBandContent(this);
	}

}
