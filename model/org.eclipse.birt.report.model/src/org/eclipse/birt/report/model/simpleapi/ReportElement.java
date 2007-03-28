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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IReportElement;

public class ReportElement extends DesignElement implements IReportElement
{

	private ReportElementHandle reportElementHandle;

	public ReportElement( ReportElementHandle handle )
	{
		super( handle );
		this.reportElementHandle = handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setCustomXml(java.lang.String)
	 */

	public void setCustomXml( String customXml ) throws SemanticException
	{
		reportElementHandle.setCustomXml( customXml );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getName()
	 */

	public String getName( )
	{
		return reportElementHandle.getName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setName(java.lang.String)
	 */

	public void setName( String name ) throws SemanticException
	{
		reportElementHandle.setName( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getCustomXml()
	 */

	public String getCustomXml( )
	{
		return reportElementHandle.getCustomXml( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setComments(java.lang.String)
	 */

	public void setComments( String theComments ) throws SemanticException
	{
		reportElementHandle.setComments( theComments );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getComments()
	 */

	public String getComments( )
	{
		return reportElementHandle.getComments( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setDisplayNameKey(java.lang.String)
	 */

	public void setDisplayNameKey( String displayNameKey )
			throws SemanticException
	{
		reportElementHandle.setDisplayNameKey( displayNameKey );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getDisplayNameKey()
	 */

	public String getDisplayNameKey( )
	{
		return reportElementHandle.getDisplayNameKey( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setDisplayName(java.lang.String)
	 */

	public void setDisplayName( String displayName ) throws SemanticException
	{

		reportElementHandle.setDisplayName( displayName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getDisplayName()
	 */

	public String getDisplayName( )
	{
		return reportElementHandle.getDisplayName( );
	}
}
