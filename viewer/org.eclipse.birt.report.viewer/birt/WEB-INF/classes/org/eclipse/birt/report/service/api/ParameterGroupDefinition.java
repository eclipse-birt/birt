/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.api;

import java.util.List;

/**
 * Viewer representation of a parameter group
 * 
 * TODO: Add more javadoc
 * 
 */
public class ParameterGroupDefinition {

	private String name;

	private String displayName;

	private List parameters;

	private boolean cascade;

	private String promptText;

	private String helpText;

	public ParameterGroupDefinition(String name, String displayName, String promptText, List parameters,
			boolean cascade) {
		this.name = name;
		this.displayName = displayName;
		this.promptText = promptText;
		this.parameters = parameters;
		this.cascade = cascade;
	}

	public ParameterGroupDefinition(String name, String displayName, String promptText, List parameters,
			boolean cascade, String helpText) {
		this.name = name;
		this.displayName = displayName;
		this.promptText = promptText;
		this.parameters = parameters;
		this.cascade = cascade;
		this.helpText = helpText;
	}

	public ParameterGroupDefinition() {
		// todo
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getPromptText() {
		return promptText;
	}

	public List getParameters() {
		return parameters;
	}

	public void setParameters(List parameters) {
		this.parameters = parameters;
	}

	public String getHelpText() {
		return this.helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	public int getParameterCount() {
		if (parameters != null)
			return parameters.size();
		return 0;
	}

	public boolean cascade() {
		return cascade;
	}

	public boolean equals(Object obj) {
		if (name == null || !(obj instanceof ParameterGroupDefinition))
			return false;
		ParameterGroupDefinition other = (ParameterGroupDefinition) obj;
		return getName().equals(other.getName());
	}

	public int hashCode() {
		if (name == null)
			return 0;
		return name.hashCode();
	}

}
