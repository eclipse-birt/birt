/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts;

import org.eclipse.birt.report.designer.ui.extensions.ReportItemBuilderUI;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * The builder for crate the x-tab to binding a cube handle
 */

public class CrosstabBuilder extends ReportItemBuilderUI {

	/**
	 * Constructor
	 */
	public CrosstabBuilder() {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.ReportItemBuilderUI#open(org.
	 * eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public int open(ExtendedItemHandle handle) {
//		InsertCubeDialog insertCubeDialog = new InsertCubeDialog( );
//		if ( insertCubeDialog.open( ) == Window.OK )
//		{
//			if ( insertCubeDialog.getResult( ) != null )
//			{
//				try
//				{
//					handle.setProperty( ICrosstabReportItemConstants.CUBE_PROP,
//							insertCubeDialog.getResult( ) );
//				}
//				catch ( SemanticException e )
//				{
//					ExceptionHandler.handle( e );
//				}
//			}
//			else
//			{
//				return Window.CANCEL;
//			}
//		}
//		else
//		{
//			return Window.CANCEL;
//		}
		return super.open(handle);
	}

}
