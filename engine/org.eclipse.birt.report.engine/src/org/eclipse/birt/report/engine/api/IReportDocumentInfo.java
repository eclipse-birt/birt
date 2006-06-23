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

package org.eclipse.birt.report.engine.api;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;

/**
 * the interface used to access the traisent informations of a report document.
 * 
 * @version $Revision: 1.2 $ $Date: 2006/04/12 05:40:31 $
 */
public interface IReportDocumentInfo
{
	List getErrors( );
	IReportDocument openReportDocument() throws BirtException;
}
