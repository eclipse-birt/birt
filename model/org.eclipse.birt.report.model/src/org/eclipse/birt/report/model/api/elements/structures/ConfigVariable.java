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

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;

/**
 * This class represents a configuration variable in the report's config
 * variable list. Reports frequently have deployment-specific dependencies. For
 * example, developers often use a test database during development, but target
 * a production database once the report is deployed. Or, an OEM may use a
 * different company name in report titles for each of their customers.A
 * configuration variable is simply a name/value pair very similar to an
 * environment variable on Unix. Indeed, configuration variables include
 * environment variables, along with other BIRT-specific values.
 * <p>
 * This is a managed object, meaning that all changes should be made though the
 * command layer so that they can be undone and redone. Each config variable has
 * the following properties:
 *
 * <p>
 * <dl>
 * <dt><strong>Name </strong></dt>
 * <dd>a config variable has a unique and required name, so the report design
 * can use the variable name to identify a config variable.</dd>
 *
 * <dt><strong>Value </strong></dt>
 * <dd>value of the config variable.</dd>
 * </dl>
 *
 */

public class ConfigVariable extends ReferencableStructure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String CONFIG_VAR_STRUCT = "ConfigVar"; //$NON-NLS-1$

	/**
	 * Name of the config variable name member.
	 */

	public static final String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * Name of the config variable value member.
	 */

	public static final String VALUE_MEMBER = "value"; //$NON-NLS-1$

	/**
	 * The config variable name.
	 */

	private String name = null;

	/**
	 * The config variable value.
	 */

	private String value = null;

	/**
	 * Constructs the config var with a required name.
	 */

	public ConfigVariable() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	@Override
	public String getStructName() {
		return CONFIG_VAR_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.
	 * String)
	 */

	@Override
	protected Object getIntrinsicProperty(String memberName) {
		if (NAME_MEMBER.equals(memberName)) {
			return name;
		}
		if (VALUE_MEMBER.equals(memberName)) {
			return value;
		}

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.
	 * String, java.lang.Object)
	 */

	@Override
	protected void setIntrinsicProperty(String memberName, Object value) {
		if (NAME_MEMBER.equals(memberName)) {
			name = (String) value;
		} else if (VALUE_MEMBER.equals(memberName)) {
			this.value = (String) value;
		} else {
			assert false;
		}
	}

	/**
	 * Returns the config variable name.
	 *
	 * @return the config variable name.
	 */

	public String getName() {
		return (String) getProperty(null, NAME_MEMBER);
	}

	/**
	 * Sets the config variable name.
	 *
	 * @param name the name to set
	 */

	public void setName(String name) {
		setProperty(NAME_MEMBER, name);
	}

	/**
	 * Returns the config variable value.
	 *
	 * @return the config variable value.
	 */

	public String getValue() {
		return (String) getProperty(null, VALUE_MEMBER);
	}

	/**
	 * Sets the config vatiable value.
	 *
	 * @param value the config value to set
	 */

	public void setValue(String value) {
		setProperty(VALUE_MEMBER, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();

		buf.append("("); //$NON-NLS-1$
		buf.append(NAME_MEMBER);
		buf.append(" = "); //$NON-NLS-1$
		buf.append(name);
		buf.append(", "); //$NON-NLS-1$
		buf.append(VALUE_MEMBER);
		buf.append(" = "); //$NON-NLS-1$
		buf.append(value);
		buf.append(")"); //$NON-NLS-1$

		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.
	 * model.api.SimpleValueHandle, int)
	 */

	@Override
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new ConfigVariableHandle(valueHandle, index);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt.report
	 * .model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	@Override
	public List validate(Module module, DesignElement element) {
		ArrayList list = new ArrayList();

		if (StringUtil.isBlank(name)) {
			list.add(new PropertyValueException(element, getDefn().getMember(NAME_MEMBER), name,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.ReferencableStructure#
	 * isReferencableProperty(java.lang.String)
	 */

	@Override
	public boolean isReferencableProperty(String memberName) {
		return NAME_MEMBER.equalsIgnoreCase(memberName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#getReferencableProperty()
	 */

	@Override
	public String getReferencableProperty() {
		return name;
	}
}
