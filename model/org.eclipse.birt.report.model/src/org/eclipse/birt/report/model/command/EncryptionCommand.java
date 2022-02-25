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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.EncryptionException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Sets encryption to a design element.
 *
 */

public class EncryptionCommand extends AbstractElementCommand {

	/**
	 * Constructor.
	 *
	 * @param module the module
	 * @param obj    the element to modify.
	 */

	public EncryptionCommand(Module module, DesignElement obj) {
		super(module, obj);
	}

	/**
	 *
	 * @param propName
	 * @param encryptionID
	 * @throws SemanticException
	 */
	public void setEncryption(String propName, String encryptionID) throws SemanticException {
		ElementPropertyDefn propDefn = element.getPropertyDefn(propName);
		if (propDefn == null) {
			throw new PropertyNameException(element, propName);
		}
		setEncryption(propDefn, encryptionID);
	}

	/**
	 *
	 * @param propDefn
	 * @param encryptionID
	 * @throws SemanticException
	 */
	public void setEncryption(ElementPropertyDefn propDefn, String encryptionID) throws SemanticException {
		assert propDefn != null;
		// if property is not encryptable, then throw exception
		if (!propDefn.isEncryptable()) {
			throw new EncryptionException(element, EncryptionException.DESIGN_EXCEPTION_INVALID_ENCRYPTABLE_PROPERTY,
					new String[] { element.getIdentifier(), propDefn.getName() });
		}

		encryptionID = StringUtil.trimString(encryptionID);
		// if encryption is not found, throw exception
		if (encryptionID != null && MetaDataDictionary.getInstance().getEncryptionHelper(encryptionID) == null) {
			throw new EncryptionException(element, EncryptionException.DESIGN_EXCEPTION_INVALID_ENCRYPTION,
					new String[] { encryptionID });
		}

		// if old local encryption and new encryption is equal, then do nothing
		String oldEncryption = element.getLocalEncryptionID(propDefn);
		if ((encryptionID == null && oldEncryption == null)
				|| (encryptionID != null && encryptionID.equals(oldEncryption))) {
			return;
		}

		EncryptionRecord record = new EncryptionRecord(module, element, propDefn, encryptionID);

		getActivityStack().execute(record);
	}
}
