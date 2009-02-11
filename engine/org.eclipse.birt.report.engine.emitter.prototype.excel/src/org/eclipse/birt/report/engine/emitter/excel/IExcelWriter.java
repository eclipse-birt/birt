/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
/**
 * 
 */
/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.content.IReportContent;

public interface IExcelWriter
{

	public void start( IReportContent report, Map<StyleEntry, Integer> styles,
			HashMap<String, BookmarkDef> bookmarkList ) throws IOException;

	public void end( ) throws IOException;

	public void startSheet( int[] coordinates ) throws IOException;

	public void endSheet( );

	public void startRow( double rowHeight );

	public void endRow( );

	public void outputData( SheetData data ) throws IOException;

}
