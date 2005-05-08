/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.extension;

import java.io.OutputStream;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Implements a default generation peer that does nothing
 */
public class ReportItemGenerationBase implements IReportItemGeneration {

	protected ExtendedItemHandle modelHandle;
	protected IBaseQueryDefinition[] queries;
	
    /**
     * Constructor that does nothing
     */
    public ReportItemGenerationBase() {
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#getSize()
     */
    public Size getSize() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#finish()
     */
    public void finish() {
    }

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#setModelObject(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject(ExtendedItemHandle modelHandle) {
		this.modelHandle = modelHandle;
	}
	public void setReportQueries(IBaseQueryDefinition[] queries)
	{
		this.queries = queries;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#onRowSets(org.eclipse.birt.report.engine.extension.IRowSet[])
	 */
	public void onRowSets(IRowSet[] rowSets) throws BirtException {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#serialize(java.io.OutputStream)
	 */
	public void serialize(OutputStream ostream) throws BirtException {
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#needSerialization()
	 */
	public boolean needSerialization() {
		return false;
	}
}
