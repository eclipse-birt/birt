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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;


public class ContainerContent extends AbstractContent
		implements
			IContainerContent
{
	ArrayList children;
	
	public ContainerContent(ReportContent report)
	{
		super(report);
	}
	
	public void accept( IContentVisitor visitor , Object value)
	{
		visitor.visitContainer(this, value);
	}
	
	public List getChildren()
	{
		if (children == null)
		{
			children = new ArrayList();
		}
		return children;
	}
}
