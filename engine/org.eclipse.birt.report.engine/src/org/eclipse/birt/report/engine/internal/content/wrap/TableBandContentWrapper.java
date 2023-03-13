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

package org.eclipse.birt.report.engine.internal.content.wrap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.ITableBandContent;

/**
 *
 * table band content object There are three type: table header, table footer,
 * table body
 *
 */
public class TableBandContentWrapper extends AbstractContentWrapper implements ITableBandContent {
	ITableBandContent bandContent;

	public TableBandContentWrapper(ITableBandContent content) {
		super(content);
		bandContent = content;
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
	public int getBandType() {
		return bandContent.getBandType();
	}

	@Override
	public void setBandType(int bandType) {
		bandContent.setBandType(bandType);
	}

	@Override
	public String getGroupID() {
		return bandContent.getGroupID();
	}

}
