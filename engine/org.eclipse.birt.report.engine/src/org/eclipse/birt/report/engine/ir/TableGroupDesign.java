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

/**
 * group defined in table item
 * 
 * @see TableItemDesign
 * @version $Revision: 1.5 $ $Date: 2005/05/08 06:59:45 $
 */
public class TableGroupDesign extends GroupDesign
{
	public TableGroupDesign( )
	{
		header = new TableBandDesign( );
		footer = new TableBandDesign( );
	}

	public Object accept( IReportItemVisitor visitor, Object value )
	{
		return visitor.visitTableGroup( this, value );
	}

}
