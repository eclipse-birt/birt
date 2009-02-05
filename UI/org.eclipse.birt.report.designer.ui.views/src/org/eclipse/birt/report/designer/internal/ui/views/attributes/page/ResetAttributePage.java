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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;

/**
 * The sup-class of all attribute page, provides common register/unregister
 * implementation to DE model, and default refresh process after getting a
 * notify from DE.
 */
public abstract class ResetAttributePage extends AttributePage
{

	public void reset( )
	{
		if ( !canReset( ) )
			return;
		Section[] sectionArray = getSections( );
		for ( int i = 0; i < sectionArray.length; i++ )
		{
			Section section = (Section) sectionArray[i];
			section.reset( );
		}
	}

	public boolean canReset( )
	{
		return true;
	}

}