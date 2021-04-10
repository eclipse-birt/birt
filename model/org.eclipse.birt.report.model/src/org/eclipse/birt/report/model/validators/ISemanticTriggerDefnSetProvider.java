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

package org.eclipse.birt.report.model.validators;

import org.eclipse.birt.report.model.metadata.SemanticTriggerDefnSet;

/**
 * Provides the set of semantic trigger definition. The class which is
 * responsible to provides the list of semantic validation trigger definition
 * should implements this interface.
 */

public interface ISemanticTriggerDefnSetProvider {

	/**
	 * Returns the semantic validation trigger set.
	 * 
	 * @return the semantic validation trigger set
	 */

	public SemanticTriggerDefnSet getTriggerDefnSet();

}