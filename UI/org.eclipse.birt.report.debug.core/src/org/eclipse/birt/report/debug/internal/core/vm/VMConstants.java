/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  Others: See git history
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.core.vm;

/**
 * VMConstants
 */
public interface VMConstants {

	VMVariable[] NO_VARS = new VMVariable[0];
	VMVariable[] NO_CHILD = NO_VARS;
	VMStackFrame[] NO_FRAMES = new VMStackFrame[0];

	String UNDEFINED_LITERAL = "<undefined>"; //$NON-NLS-1$
	String EVALUATOR_LITERAL = "<evaluator>"; //$NON-NLS-1$
	String ERROR_LITERAL = "<error>"; //$NON-NLS-1$

	String UNDEFINED_TYPE = "<undefined>"; //$NON-NLS-1$
	String EXCEPTION_TYPE = "<exception>"; //$NON-NLS-1$

	int VM_IDLE = 0;
	int VM_STEP_OVER = 1;
	int VM_STEP_INTO = 2;
	int VM_STEP_OUT = 3;
	int VM_RESUME = 4;
	int VM_SUSPEND = 5;
	int VM_TERMINATE = 6;
	int VM_SUSPENDED_BREAKPOINT = 7;
	int VM_SUSPENDED_CLIENT = 8;
	int VM_STARTED = 9;
	int VM_TERMINATED = 10;
	int VM_RESUMED = 11;
	int VM_SUSPENDED_STEP_INTO = 12;
	int VM_SUSPENDED_STEP_OVER = 13;
	int VM_SUSPENDED_STEP_OUT = 14;

	int OP_RETURN_VALUE_MASK = 0x10;
	int OP_ARGUMENT_MASK = 0x100;
	int OP_RESUME = 0x01;
	int OP_SUSPEND = 0x02;
	int OP_STEP_OVER = 0x03;
	int OP_STEP_INTO = 0x04;
	int OP_STEP_OUT = 0x05;
	int OP_TERMINATE = 0x06;
	int OP_CLEAR_BREAKPOINTS = 0x09;
	int OP_QUERY_TERMINATED = 0x11;
	int OP_QUERY_SUSPENDED = 0x12;
	int OP_GET_VARIABLES = 0x13;
	int OP_GET_STACKFRAMES = 0x14;
	int OP_ADD_BREAKPOINT = 0x101;
	int OP_MOD_BREAKPOINT = 0x102;
	int OP_REMOVE_BREAKPOINT = 0x103;
	int OP_EVALUATE = 0x111;
	int OP_GET_STACKFRAME = 0x112;
	int OP_GET_MEMBERS = 0x113;

	int ADD = 1;
	int REMOVE = 2;
	int CHANGE = 3;
	int CLEAR = 4;

	String[] EVENT_NAMES = { "IDLE", //$NON-NLS-1$
			"STEP_OVER", //$NON-NLS-1$
			"STEP_INTO", //$NON-NLS-1$
			"STEP_OUT", //$NON-NLS-1$
			"RESUME", //$NON-NLS-1$
			"SUSPEND", //$NON-NLS-1$
			"TERMINATE", //$NON-NLS-1$
			"SUSPENDED_BREAKPOINT", //$NON-NLS-1$
			"SUSPENDED_CLIENT", //$NON-NLS-1$
			"STARTED", //$NON-NLS-1$
			"TERMINATED", //$NON-NLS-1$
			"RESUMED", //$NON-NLS-1$
			"SUSPENDED_STEP_INTO", //$NON-NLS-1$
			"SUSPENDED_STEP_OVER", //$NON-NLS-1$
			"SUSPENDED_STEP_OUT", //$NON-NLS-1$
	};

}
