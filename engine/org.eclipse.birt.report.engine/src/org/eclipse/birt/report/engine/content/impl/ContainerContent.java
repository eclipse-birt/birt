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

package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;

public class ContainerContent extends AbstractContent
		implements
			IContainerContent
{

	/**
	 * constructor use by serialize and deserialize
	 */
	public ContainerContent( )
	{
	}

	public int getContentType( )
	{
		return CONTAINER_CONTENT;
	}

	public ContainerContent( ReportContent report )
	{
		super( report );
	}

	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitContainer( this, value );
	}
}
