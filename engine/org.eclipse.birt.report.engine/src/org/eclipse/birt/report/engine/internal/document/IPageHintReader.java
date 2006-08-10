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

package org.eclipse.birt.report.engine.internal.document;

import java.io.IOException;

import org.eclipse.birt.report.engine.presentation.IPageHint;

/**
 * interfaces used to read the page hints.
 *
 * @version $Revision: 1.1 $ $Date: 2006/04/05 13:22:52 $
 */
public interface IPageHintReader
{

	void open( ) throws IOException;

	void close( );

	long getTotalPage( ) throws IOException;

	IPageHint getPageHint( long pageNumber ) throws IOException;
	
	long findPage(long content) throws IOException;
}
