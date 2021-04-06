/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitTableBand(this, value);
	}

	public int getBandType() {
		return bandContent.getBandType();
	}

	public void setBandType(int bandType) {
		bandContent.setBandType(bandType);
	}

	public String getGroupID() {
		return bandContent.getGroupID();
	}

}