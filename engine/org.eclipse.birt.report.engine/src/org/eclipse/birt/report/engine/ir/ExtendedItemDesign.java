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

package org.eclipse.birt.report.engine.ir;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;

/**
 * Extended Item. 
 * 
 * @version $Revision: 1.6 $ $Date: 2005/11/11 06:26:41 $
 */
public class ExtendedItemDesign extends ReportItemDesign
{
	IBaseQueryDefinition[] queries;

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.ReportItemDesign#accept(org.eclipse.birt.report.engine.ir.ReportItemVisitor)
	 */
	public Object accept( IReportItemVisitor visitor , Object value)
	{
		return visitor.visitExtendedItem( this, value);
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