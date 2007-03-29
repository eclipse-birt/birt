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

package org.eclipse.birt.chart.script.internal.attribute;

import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.script.api.attribute.IText;

/**
 * 
 */

public class TextImpl implements IText
{

	private Text text;

	public TextImpl( Text text )
	{
		this.text = text;
	}

	public String getValue( )
	{
		return text.getValue( );
	}

	public void setValue( String value )
	{
		text.setValue( value );
	}

}
