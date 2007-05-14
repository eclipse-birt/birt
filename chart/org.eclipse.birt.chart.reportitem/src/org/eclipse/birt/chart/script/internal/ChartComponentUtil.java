/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script.internal;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;

/**
 * 
 */

public class ChartComponentUtil
{

	public static ColorDefinition createEMFColor( )
	{
		ColorDefinition cd = ColorDefinitionImpl.BLACK( );
		return cd;
	}

	public static FontDefinition createEMFFont( )
	{
		FontDefinition fd = FontDefinitionImpl.createEmpty( );
		return fd;
	}

	public static Text createEMFText( )
	{
		Text desc = TextImpl.create( "" ); //$NON-NLS-1$
		desc.setColor( createEMFColor( ) );
		desc.setFont( createEMFFont( ) );
		return desc;
	}

	public static Label createEMFLabel( )
	{
		Label label = LabelImpl.create( );
		label.setCaption( createEMFText( ) );
		label.setVisible( true );
		return label;
	}
}
