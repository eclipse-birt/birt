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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.core.DesignSessionImpl;
import org.eclipse.birt.report.model.writer.DesignWriter;
import org.eclipse.birt.report.model.writer.ModuleWriter;

/**
 * This class represents the root element in the report design hierarchy.
 * Contains the list of data sets, data sources, master pages, components, body
 * content, scratch pad and more. Code modules in the report gives
 * specifications for global scripts that apply to the report as a whole.Report
 * design is valid if it is opened without error or with semantic error.
 * Otherwise, it's invalid.
 * 
 */

public class ReportDesign extends ReportDesignImpl {

	/**
	 * Default constructor.
	 * 
	 * @deprecated
	 */

	public ReportDesign() {
		super(null);
	}

	/**
	 * Constructs the report design with the session.
	 * 
	 * @param session the session that owns this design
	 */

	public ReportDesign(DesignSessionImpl session) {
		super(session);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitReportDesign(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ReportDesignImpl#handle()
	 */
	public ReportDesignHandle handle() {
		if (handle == null) {
			handle = new ReportDesignHandle(this);
		}
		return (ReportDesignHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getWriter()
	 */

	public ModuleWriter getWriter() {
		return new DesignWriter(this);
	}
}
