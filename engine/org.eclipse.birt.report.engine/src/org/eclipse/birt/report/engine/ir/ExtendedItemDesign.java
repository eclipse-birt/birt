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

package org.eclipse.birt.report.engine.ir;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;

/**
 * Extended Item. 
 * 
 * @version $Revision: 1.3 $ $Date: 2005/04/21 12:24:27 $
 */
public class ExtendedItemDesign extends ReportItemDesign
{
	IBaseQueryDefinition[] queries;

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.ReportItemDesign#accept(org.eclipse.birt.report.engine.ir.ReportItemVisitor)
	 */
	public void accept( IReportItemVisitor visitor )
	{
		visitor.visitExtendedItem( this );
	}
	
	public void setQueries(IBaseQueryDefinition[] queries)
	{
		this.queries = queries;
	}
	
	public IBaseQueryDefinition[] getQueries()
	{
		return this.queries;
	}
}