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

package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IRowContent;

/**
 * 
 * the row content object which contains cell content objects
 * 
 * @version $Revision: 1.5 $ $Date: 2005/10/27 02:13:34 $
 */
public class RowContent extends AbstractContent implements IRowContent
{

	/**
	 * constructor
	 * 
	 * @param row
	 *            the row deign
	 */
	public RowContent( ReportContent report )
	{
		super(report);
	}

	public void accept( IContentVisitor visitor , Object value)
	{
		visitor.visitRow( this , value);
	}
}