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

package org.eclipse.birt.chart.ui.swt;

import java.io.UnsupportedEncodingException;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * Transfers simple text regardless of platform specific. Replacing default
 * <code>TextTransfer</code> for this simple transfer is for a drop issue in
 * Linux platform, i.e. Linux will insert the transfered string into the target
 * cursor after invoking <code>DropTargetAdapter.drop()</code>. Please refer to
 * bugzilla #115783
 */

public class SimpleTextTransfer extends ByteArrayTransfer {

	private static final SimpleTextTransfer instance = new SimpleTextTransfer();

	private static final String TYPE_NAME = "simple-text-transfer";//$NON-NLS-1$

	private static final int TYPEID = registerType(TYPE_NAME);

	private SimpleTextTransfer() {

	}

	public static SimpleTextTransfer getInstance() {
		return instance;
	}

	protected int[] getTypeIds() {
		return new int[] { TYPEID };
	}

	protected String[] getTypeNames() {
		return new String[] { TYPE_NAME };
	}

	private boolean checkText(Object object) {
		return (object != null && object instanceof String && ((String) object).length() > 0);
	}

	protected boolean validate(Object object) {
		return checkText(object);
	}

	/**
	 * This implementation of <code>javaToNative</code> converts plain text
	 * represented by a java <code>String</code> to a platform specific
	 * representation.
	 * 
	 * @param object       a java <code>String</code> containing text
	 * @param transferData an empty <code>TransferData</code> object; this object
	 *                     will be filled in on return with the platform specific
	 *                     format of the data
	 * 
	 * @see Transfer#javaToNative
	 */
	public void javaToNative(Object object, TransferData transferData) {
		if (checkText(object) && isSupportedType(transferData)) {
			try {
				super.javaToNative(((String) object).getBytes("UTF-8"), //$NON-NLS-1$
						transferData);
			} catch (UnsupportedEncodingException e) {
				// do not need to log
				super.javaToNative(((String) object).getBytes(), transferData);
			}
		}
	}

	/**
	 * This implementation of <code>nativeToJava</code> converts a platform specific
	 * representation of plain text to a java <code>String</code>.
	 * 
	 * @param transferData the platform specific representation of the data to be
	 *                     converted
	 * @return a java <code>String</code> containing text if the conversion was
	 *         successful; otherwise null
	 * 
	 * @see Transfer#nativeToJava
	 */
	public Object nativeToJava(TransferData transferData) {
		if (!isSupportedType(transferData)) {
			return null;
		}
		byte[] bytes = (byte[]) super.nativeToJava(transferData);

		try {
			return new String(bytes, "UTF-8"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			// do not need to log
		}
		return new String(bytes);
	}

}
