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

import java.util.List;

/**
 * Provides the validator list. The class which is responsible to return
 * validator list should implements this class.
 */

public interface IValidatorProvider {

	/**
	 * Returns the validator list. Each of the list is the instance of
	 * <code>AbstractSemanticValidator</code>.
	 * 
	 * @return the provided validator list
	 */

	public List<ValidationNode> getValidators();
}