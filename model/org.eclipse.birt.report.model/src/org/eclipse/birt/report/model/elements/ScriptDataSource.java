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

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IScriptDataSourceModel;

/**
 * This class represents the data source that is defined in JavaScript. The
 * actual implementation can also be in Java, with a JavaScript wrapper. The
 * application is responsible for implementing two operations:
 *
 * <p>
 * <dl>
 * <dt><strong>Open </strong></dt>
 * <dd>connect to the external system. Report an error if the connection fails.
 * </dd>
 *
 * <dt><strong>Close </strong></dt>
 * <dd>drop the connection to the external system.</dd>
 * </dl>
 *
 * A scripted data source may use user-defined properties to define
 * connection-specific properties. Doing so allows the developer to put the data
 * source into a library, and use it in many reports. For example, the developer
 * may define a SOAP data source with properties for the server name, port
 * number and so on. The scripts associated with the data source use the ROM
 * scripting objects to access the value of these custom properties.
 *
 */

public class ScriptDataSource extends DataSource implements IScriptDataSourceModel {

	/**
	 * Constructs a default <code>ScriptDataSource</code>.
	 */

	public ScriptDataSource() {
	}

	/**
	 * Constructs the script data source with name.
	 *
	 * @param theName the script data source name
	 */

	public ScriptDataSource(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitScriptDataSource(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public String getElementName() {
		return ReportDesignConstants.SCRIPT_DATA_SOURCE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.
	 * report.model.elements.ReportDesign)
	 */

	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @param module the report design
	 * @return an API handle for this element
	 */

	public ScriptDataSourceHandle handle(Module module) {
		if (handle == null) {
			handle = new ScriptDataSourceHandle(module, this);
		}
		return (ScriptDataSourceHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.
	 * report.model.elements.ReportDesign)
	 */

	@Override
	public List validate(Module module) {
		List list = super.validate(module);

		return list;
	}
}
