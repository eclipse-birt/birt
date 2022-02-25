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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.command.ComplexPropertyCommand;
import org.eclipse.birt.report.model.command.PropertyCommand;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.StructureContextUtil;

/**
 * A handle to a member of a property structure. A structure list occurs in an
 * element property that contains a list of structures. The class handles a
 * member of one structure in the list.
 *
 *
 * @see StructureHandle
 */

public class MemberHandle extends SimpleValueHandle {

	/**
	 * The context to the member itself.
	 */

	protected StructureContext memberContext;

	/**
	 * Constructs a member handle with the given structure handle and the member
	 * property definition. This form is used by the <code>StructureIterator</code>
	 * class.
	 *
	 * @param structHandle a handle to the structure
	 * @param member       definition of the member within the structure
	 */

	public MemberHandle(StructureHandle structHandle, StructPropertyDefn member) {
		super(structHandle.getElementHandle());

		if (!StructureContextUtil.isValidStructureHandle(structHandle)) {
			throw new RuntimeException("The structure is floating, and its handle is invalid!"); //$NON-NLS-1$
		}

		memberContext = StructureContextUtil.getMemberContext(structHandle, member);
		assert memberContext != null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#getDefn()
	 */
	@Override
	public IPropertyDefn getDefn() {
		return memberContext.getPropDefn();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#getRawValue()
	 */
	@Override
	protected Object getRawValue() {
		return memberContext.getValue(getModule());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#setValue(java.lang
	 * .Object)
	 */
	@Override
	public void setValue(Object value) throws SemanticException {
		PropertyCommand cmd = new PropertyCommand(getModule(), getElement());
		cmd.setMember(memberContext, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#removeItem(int)
	 */
	@Override
	public void removeItem(int posn) throws PropertyValueException {
		ComplexPropertyCommand cmd = new ComplexPropertyCommand(getModule(), getElement());

		try {
			cmd.removeItem(memberContext, posn);
		} catch (PropertyValueException e) {
			throw e;
		} catch (SemanticException e) {
			assert false;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#addItem(java.lang
	 * .Object)
	 */
	@Override
	public void addItem(Object item) throws SemanticException {
		if (item == null) {
			return;
		}

		ComplexPropertyCommand cmd = new ComplexPropertyCommand(getModule(), getElement());
		cmd.addItem(memberContext, item);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.ValueHandle#getPropertyDefn()
	 */
	@Override
	public IElementPropertyDefn getPropertyDefn() {
		return memberContext.getElementProp();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.ValueHandle#getContext()
	 */
	@Override
	public StructureContext getContext() {
		return memberContext;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#isReadOnly()
	 */

	@Override
	public boolean isReadOnly() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#isVisible()
	 */

	@Override
	public boolean isVisible() {
		return true;
	}

}
