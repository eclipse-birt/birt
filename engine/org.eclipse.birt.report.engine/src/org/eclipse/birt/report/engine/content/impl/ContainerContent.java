/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
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
