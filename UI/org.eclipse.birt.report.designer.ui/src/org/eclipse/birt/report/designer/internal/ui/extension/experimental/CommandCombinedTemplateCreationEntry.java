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

package org.eclipse.birt.report.designer.internal.ui.extension.experimental;

import org.eclipse.birt.report.designer.internal.ui.palette.ReportElementFactory;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;

/**
 * 
 */

public class CommandCombinedTemplateCreationEntry extends
		CombinedTemplateCreationEntry
{

	private PaletteEntryExtension paletteEntry;

	public CommandCombinedTemplateCreationEntry(
			PaletteEntryExtension paletteEntry )
	{
		super( paletteEntry.getLabel( ),
				paletteEntry.getDescription( ),
				new ReportElementFactory( paletteEntry.getItemName( ) ),
				paletteEntry.getIcon( ),
				paletteEntry.getIconLarge( ) );
		this.paletteEntry = paletteEntry;
	}

	public Tool createTool( )
	{
		// TODO Auto-generated method stub
		return new CreationTool( ) {

			protected void performCreation( int button )
			{
				// TODO Auto-generated method stub
				try
				{
					getCreateRequest( ).setFactory( new PaletteEntryCreationFactory( paletteEntry ) );
					super.performCreation( button );
				}
				catch ( Exception e )
				{
					ExceptionHandler.handle( e );
				}
			}

		};
	}
}

class PaletteEntryCreationFactory implements CreationFactory
{

	private PaletteEntryExtension paletteEntry;

	public PaletteEntryCreationFactory( PaletteEntryExtension paletteEntry )
	{
		this.paletteEntry = paletteEntry;
	}

	public Object getNewObject( )
	{
		try
		{
			return this.paletteEntry.executeCreate( );
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
		return null;
	}

	public Object getObjectType( )
	{
		return this.paletteEntry.getItemName( );
	}

}
