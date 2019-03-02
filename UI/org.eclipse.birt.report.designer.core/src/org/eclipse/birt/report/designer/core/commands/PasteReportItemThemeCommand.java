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

package org.eclipse.birt.report.designer.core.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemThemeHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.util.CopyUtil;
import org.eclipse.birt.report.model.api.util.IElementCopy;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.namespace.INameHelper;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.gef.commands.Command;

/**
 * Paste structure to container.
 */

public class PasteReportItemThemeCommand extends Command
{
	protected static final Logger logger = Logger.getLogger( PasteReportItemThemeCommand.class.getName( ) );
	private IElementCopy copyData;
	private SlotHandle container;
	private ThemeHandle theme;

	public PasteReportItemThemeCommand( IElementCopy copyData, SlotHandle container, ThemeHandle param )
	{
		this.copyData = copyData;
		this.container = container;
		this.theme = param;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute( )
	{
		return DNDUtil.handleValidateTargetCanContain( container, copyData );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute( )
	{
		try
		{
			ModuleHandle module = theme.getModuleHandle( );
			Module library = module.getModule( );
			DesignElementHandle source = this.copyData.getHandle( module );
			DesignElementHandle handle = CopyUtil.copy( source ).getHandle( module );
			if ( DesignerConstants.TRACING_COMMANDS )
			{
				System.out.println( "PasteReportItemThemeCommand >>  Starts. Source: " //$NON-NLS-1$
						+ handle
						+ ",Target: " //$NON-NLS-1$
						+ DEUtil.getDisplayLabel( container ) );
			}
			if ( handle instanceof ReportItemThemeHandle )
			{
				DesignElement element = handle.getElement( );
				NameExecutor executor = new NameExecutor( library, element.getContainer( ), element );
				INameHelper helper = executor.getNameHelper( );
				String namePrefix = ( (ReportItemThemeHandle) handle ).getType( ) + "-" + theme.getName( ); //$NON-NLS-1$
				handle.setName( null );
				String name = helper.getUniqueName( Module.THEME_NAME_SPACE, handle.getElement( ), namePrefix );
				handle.setName( name );
				container.add( handle );
			}
			if ( DesignerConstants.TRACING_COMMANDS )
			{
				System.out.println( "PasteReportItemThemeCommand >>  Finished" ); //$NON-NLS-1$
			}
		}
		catch ( Exception e )
		{
			if ( DesignerConstants.TRACING_COMMANDS )
			{
				System.out.println( "PasteReportItemThemeCommand >>  Failed" ); //$NON-NLS-1$
			}
			logger.log( Level.SEVERE,e.getMessage( ), e);
		}
	}
}