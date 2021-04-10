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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IReportContent;

public class ContainerContent extends AbstractContent implements IContainerContent {
	ContainerContent(IContainerContent container) {
		super(container);
	}

	public int getContentType() {
		return CONTAINER_CONTENT;
	}

	ContainerContent(IReportContent report) {
		super(report);
	}

	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitContainer(this, value);
	}

	protected IContent cloneContent() {
		return new ContainerContent(this);
	}
}
