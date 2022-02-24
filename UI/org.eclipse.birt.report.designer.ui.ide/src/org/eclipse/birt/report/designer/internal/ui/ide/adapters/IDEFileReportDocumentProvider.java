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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.report.designer.internal.ui.editors.FileReportDocumentProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.xml.XMLPartitionScanner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IURIEditorInput;

/**
 * IDEFileReportDocumentProvider
 */
public class IDEFileReportDocumentProvider extends FileReportDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner = new FastPartitioner(new XMLPartitionScanner(),
					new String[] { XMLPartitionScanner.XML_TAG, XMLPartitionScanner.XML_COMMENT });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}

	@Override
	protected boolean setDocumentContent(IDocument document, IEditorInput editorInput, String encoding)
			throws CoreException {
		if (super.setDocumentContent(document, editorInput, encoding)) {
			return true;
		}

		IPath path = null;

		if (editorInput instanceof IPathEditorInput) {
			path = ((IPathEditorInput) editorInput).getPath();
		} else if (editorInput instanceof IURIEditorInput) {
			path = new Path(((IURIEditorInput) editorInput).getURI().getPath());
		}

		if (path != null) {
			File file = path.toFile();

			if (file != null && file.exists()) {
				InputStream stream = null;
				try {
					stream = new FileInputStream(file);
					setDocumentContent(document, stream, encoding);
					return true;
				} catch (Exception e) {
				} finally {
					if (stream != null)
						try {
							stream.close();
						} catch (IOException e) {
						}
				}
			}
		}

		return false;
	}
}
