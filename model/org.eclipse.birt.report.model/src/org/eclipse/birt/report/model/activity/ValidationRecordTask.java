/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.activity;

import java.util.Stack;

import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.validators.ValidationExecutor;

/**
 * The task to perform validation checks after the execution of records.
 * 
 */

public class ValidationRecordTask extends RecordTask {

	/**
	 * The executor for validation. It can collect the validators and perform
	 * validation one by one.
	 */

	private ValidationExecutor validationExecutor = null;

	/**
	 * Constructs a <code>ValidationInterceptorTask</code> with the given module.
	 * 
	 * @param module the report module
	 */

	public ValidationRecordTask(Module module) {
		super(module);
		validationExecutor = new ValidationExecutor(module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.RecordTask#doTask(org.eclipse.
	 * birt.report.model.activity.ActivityRecord, java.util.Stack)
	 */
	public void doTask(ActivityRecord record, Stack<CompoundRecord> transStack) {
		assert validationExecutor != null;

		if (MetaDataDictionary.getInstance().useValidationTrigger()) {
			validationExecutor.perform(record.getValidators(), true);
		}
	}
}
