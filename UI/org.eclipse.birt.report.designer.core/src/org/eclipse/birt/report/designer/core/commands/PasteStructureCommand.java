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

import org.eclipse.birt.report.designer.core.model.views.outline.EmbeddedImageNode;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.core.IStructure;
import org.eclipse.gef.commands.Command;

/**
 * Paste structure to container.
 */

public class PasteStructureCommand extends Command
{

	private IStructure copyData;
	private Object container;

	public PasteStructureCommand( IStructure copyData, Object container )
	{
		this.copyData = copyData;
		this.container = container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute( )
	{
		return DEUtil.handleValidateTargetCanContainStructure( container,
				copyData );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute( )
	{
		if ( container instanceof EmbeddedImageNode )
		{
			container = ( (EmbeddedImageNode) container ).getReportDesignHandle( );
		}
		try
		{
			( (ReportDesignHandle) container ).addImage( (EmbeddedImage) copyData.copy( ) );
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
		}
	}
}