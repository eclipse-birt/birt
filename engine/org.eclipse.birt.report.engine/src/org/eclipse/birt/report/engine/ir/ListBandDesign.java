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
 * List Band.
 * 
 * @version $Revision: 1.6 $ $Date: 2005/11/11 06:26:41 $
 */
public class ListBandDesign extends BandDesign
{
	public Object accept( IReportItemVisitor visitor, Object value )
	{
		return visitor.visitListBand(this, value);
	}
}
