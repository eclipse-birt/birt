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

package org.eclipse.birt.report.model.metadata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.validators.AbstractSemanticValidator;

/**
 * Represents the collection of semantic validation triggers. Each one is the
 * instance of <code>SemanticTriggerDefn</code>.
 */

public class SemanticTriggerDefnSet {

	/**
	 * List of the definitions for semantic validator applied to this property.
	 */

	protected List<SemanticTriggerDefn> triggerList = null;

	/**
	 * Adds the definition for semantic validator.
	 *
	 * @param validatorDefn the definition to add
	 */

	void add(SemanticTriggerDefn validatorDefn) {
		if (triggerList == null) {
			triggerList = new ArrayList<>();
		}

		triggerList.add(validatorDefn);
	}

	/**
	 * Adds all trigger definition into this trigger collection.
	 *
	 * @param triggers
	 */

	public void add(SemanticTriggerDefnSet triggers) {
		if (triggers != null && triggers.triggerList != null) {
			Iterator<SemanticTriggerDefn> iter = triggers.triggerList.iterator();
			while (iter.hasNext()) {
				SemanticTriggerDefn trigger = iter.next();

				add(trigger);
			}
		}
	}

	/**
	 * Returns the list of semantic validator's definitions. Each of the list is the
	 * instance of <code>TriggerDefn</code>.
	 *
	 * @return the list of semantic validator's definitions.
	 */

	public List<SemanticTriggerDefn> getTriggerList() {
		return triggerList;
	}

	/**
	 * Builds all semantic validation triggers.
	 *
	 * @throws MetaDataException if the validator is not found.
	 */

	public void build() throws MetaDataException {
		if (triggerList != null) {
			Iterator<SemanticTriggerDefn> iter = triggerList.iterator();
			while (iter.hasNext()) {
				SemanticTriggerDefn validatorDefn = iter.next();

				if (validatorDefn.getValidator() == null) {
					AbstractSemanticValidator validator = MetaDataDictionary.getInstance()
							.getSemanticValidator(validatorDefn.getValidatorName());
					if (validator == null) {
						throw new MetaDataException(new String[] { validatorDefn.getValidatorName() },
								MetaDataException.DESIGN_EXCEPTION_VALIDATOR_NOT_FOUND);
					}

					validatorDefn.setValidator(validator);
				}
			}
		}
	}

	/**
	 * Validates whether the list contains the validator with the given
	 * <code>validatorName</code>.
	 *
	 * @param validatorName the name of the validator definition
	 * @return <code>true</code> if the list contains the given validator. Otherwise
	 *         <code>false</code>.
	 */

	boolean contain(String validatorName) {
		if (triggerList == null) {
			return false;
		}

		assert validatorName != null;

		for (int i = 0; i < triggerList.size(); i++) {
			SemanticTriggerDefn tmpDefn = triggerList.get(i);
			if (validatorName.equalsIgnoreCase(tmpDefn.getValidatorName())) {
				return true;
			}
		}
		return false;
	}

}
