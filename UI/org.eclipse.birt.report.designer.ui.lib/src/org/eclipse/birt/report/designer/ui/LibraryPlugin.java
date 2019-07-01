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

package org.eclipse.birt.report.designer.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * LibraryPlugin
 */
public class LibraryPlugin extends AbstractUIPlugin
{

	public LibraryPlugin( )
	{
		super( );
	}

	public void start( BundleContext context ) throws Exception
	{
		super.start( context );
		ReportPlugin.getDefault( )
				.addIgnoreViewID( "org.eclipse.birt.report.designer.ui.editors.LibraryEditor" ); //$NON-NLS-1$
	}
}
