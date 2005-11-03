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
package org.eclipse.birt.report.engine.extension;

import java.io.InputStream;
import java.util.Locale;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Implements a default presentation peer that does nothing
 */
public class ReportItemPresentationBase implements IReportItemPresentation {
    
    protected ExtendedItemHandle modelHandle;
    protected int dpi = 72;
    protected String outputFormat;
    protected String supportedImageFormats;
    protected Locale locale;
    protected IBaseQueryDefinition[] queries;
    protected IEngineService service;

	/**
     * Constructor that does nothing
     */
    public ReportItemPresentationBase() {
    }
    
   
    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#getOutputType(java.lang.String, java.lang.String)
     */
    public int getOutputType() {
        return OUTPUT_NONE;
    }

    public Object getOutputContent()
	{
		return null;
	}
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#getSize()
     */
    public Size getSize() {
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#finish()
     */
    public void finish() {
    }

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setModelObject(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject(ExtendedItemHandle modelHandle) {
		this.modelHandle = modelHandle;
	}
	public void setReportQueries(IBaseQueryDefinition[] queries)
	{
		this.queries = queries;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setResolution(int)
	 */
	public void setResolution(int dpi) {
		this.dpi = dpi;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setOutputFormat(java.lang.String)
	 */
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setSupportedImageFormats(java.lang.String)
	 */
	public void setSupportedImageFormats(String supportedImageFormats) {
		this.supportedImageFormats = supportedImageFormats;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#deserialize(java.io.InputStream)
	 */
	public void deserialize(InputStream istream) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#onRowSets(org.eclipse.birt.report.engine.extension.IRowSet[])
	 */
	public Object onRowSets(IRowSet[] rowSets) throws BirtException {
		return null;
	}

	/**
	 * @return the image MIME type (e.g. "image/svg+xml")
	 */
	public String getImageMIMEType() {
		return ""; //$NON-NLS-1$
	}

	public void setEngineService( IEngineService service ) {
		this.service = service;
	}

	public IEngineService getEngineService() {
		return service;
	}
	
}
