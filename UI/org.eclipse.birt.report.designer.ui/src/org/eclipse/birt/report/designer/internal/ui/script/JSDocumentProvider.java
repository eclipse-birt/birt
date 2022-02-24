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

package org.eclipse.birt.report.designer.internal.ui.script;

import org.eclipse.birt.report.designer.internal.ui.editors.DocumentProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.StorageDocumentProvider;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.DefaultPartitioner;
import org.eclipse.ui.ISaveablePart;

/**
 * Subclass of <code>DocumentProvider</code>, creates documents and sets
 * partition scanners
 * 
 * 
 */
public class JSDocumentProvider extends StorageDocumentProvider {

	/**
	 * Array of token types
	 */
	private static String[] colorTokens = { JSPartitionScanner.JS_COMMENT, JSPartitionScanner.JS_STRING,
			JSPartitionScanner.JS_KEYWORD };

	public JSDocumentProvider() {
		super();
	}

	/**
	 * Creates a new javascript's document provider with the specified saveable
	 * part.
	 * 
	 * @param part the saveable part.
	 */
	public JSDocumentProvider(ISaveablePart part) {
		super(part);
	}

	/**
	 * @see DocumentProvider#createDocument(java.lang.Object)
	 */
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner = new DefaultPartitioner(new JSPartitionScanner(), colorTokens);
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}

}
