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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.TOC;

public class TocExpressionPropertyDescriptorProvider extends
		ExpressionPropertyDescriptorProvider
{

	public TocExpressionPropertyDescriptorProvider( String property,
			String element )
	{
		super( property, element );
	}

	public Object load( )
	{
		Object value = super.load( );
		if ( value instanceof TOC )
		{
			value = ( ( (TOC) value ).getExpressionProperty( TOC.TOC_EXPRESSION ) );
		}
		return value;
	}

	public void save( Object value ) throws SemanticException
	{
		if ( isReadOnly( ) )
			return;
		if ( input instanceof GroupElementHandle )
		{
			if ( value instanceof Expression )
			{
				TOC toc = StructureFactory.createTOC( );
				toc.setExpressionProperty( TOC.TOC_EXPRESSION,
						(Expression) value );
				( (GroupElementHandle) input ).setProperty( property, toc );
			}
			else
				( (GroupElementHandle) input ).setProperty( property, value );
		}
		else if ( input instanceof List )
		{
			if ( value instanceof Expression )
			{
				TOC toc = StructureFactory.createTOC( );
				toc.setExpressionProperty( TOC.TOC_EXPRESSION,
						(Expression) value );
				DEUtil.getGroupElementHandle( (List) input )
						.setProperty( property, toc );
			}
			else
				DEUtil.getGroupElementHandle( (List) input )
						.setProperty( property, value );
		}
	}
}
