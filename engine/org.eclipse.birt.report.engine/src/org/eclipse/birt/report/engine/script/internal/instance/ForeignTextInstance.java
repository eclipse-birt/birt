/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.script.internal.instance;

import org.eclipse.birt.report.engine.api.script.instance.IForeignTextInstance;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;

public abstract class ForeignTextInstance extends ReportItemInstance implements IForeignTextInstance
{
	public ForeignTextInstance( ForeignContent content )
	{
		super( content );
	}
	
	protected ForeignTextInstance() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.script.IForeignTextInstance#getText()
	 */
	public String getText( )
	{
		ForeignContent fc = ( ForeignContent ) content;
		String type = fc.getRawType( );
		if ( IForeignContent.TEMPLATE_TYPE.equals( type )
				|| IForeignContent.HTML_TYPE.equals( type )
				|| IForeignContent.TEXT_TYPE.equals( type ) )
		{
			return ( fc.getRawType( ) == null ? null : fc.getRawType( )
					.toString( ) );
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.script.IForeignTextInstance#setText(java.lang.String)
	 */
	public void setText( String value )
	{
		( ( ForeignContent ) content ).setRawValue( value );
	}
}
