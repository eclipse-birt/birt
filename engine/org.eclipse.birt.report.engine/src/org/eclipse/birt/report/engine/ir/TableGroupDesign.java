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
 * @version $Revision: 1.6 $ $Date: 2006/06/13 15:37:20 $
 */
public class TableGroupDesign extends GroupDesign
{
	public TableGroupDesign( )
	{
        //modify bug161912
		//header = new TableBandDesign( );
		//footer = new TableBandDesign( );
	}

	public Object accept( IReportItemVisitor visitor, Object value )
	{
		return visitor.visitTableGroup( this, value );
	}

}
