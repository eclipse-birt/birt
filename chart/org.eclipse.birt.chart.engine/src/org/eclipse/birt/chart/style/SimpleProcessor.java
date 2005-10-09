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

package org.eclipse.birt.chart.style;

import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;

/**
 * SimpleProcessor
 */
public final class SimpleProcessor implements IStyleProcessor
{

	/**
	 * The constructor.
	 */
	public SimpleProcessor( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.style.IStyleProcessor#getStyle(org.eclipse.birt.chart.model.attribute.StyledComponent)
	 */
	public IStyle getStyle( StyledComponent name )
	{
		// Always return the default value.
		TextAlignment ta = TextAlignmentImpl.create( );
		FontDefinition font = FontDefinitionImpl.create( "SansSerif", //$NON-NLS-1$
				12, false, false, false, false, false, 0, ta );

		return new SimpleStyle( font,
				ColorDefinitionImpl.BLACK( ),
				null,
				null,
				null );
	}

}
