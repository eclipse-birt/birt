/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.io.IOException;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDocumentWriter;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.ir.EngineIRWriter;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class DocumentWriter implements IDocumentWriter {

	private IArchiveFile archive;

	public DocumentWriter(IArchiveFile file) {
		this.archive = file;
	}

	@Override
	public void setRunnable(IReportRunnable runnable) throws EngineException {
		if (archive == null || runnable == null) {
			return;
		}

		try {
			ArchiveWriter writer = new ArchiveWriter(archive);
			ReportDesignHandle design = (ReportDesignHandle) runnable.getDesignHandle();

			// rewrite design
			RAOutputStream out = writer.createRandomAccessStream(ReportDocumentConstants.DESIGN_STREAM);
			org.eclipse.birt.report.model.api.util.DocumentUtil.serialize(design, out);
			out.close();

			// rewrite original design
			out = writer.createRandomAccessStream(ReportDocumentConstants.ORIGINAL_DESIGN_STREAM);
			org.eclipse.birt.report.model.api.util.DocumentUtil.serialize(design, out);
			out.close();

			// rewrite internal report
			Report report = new ReportParser().parse((ReportDesignHandle) runnable.getDesignHandle());
			out = writer.createRandomAccessStream(ReportDocumentConstants.DESIGN_IR_STREAM);
			new EngineIRWriter().write(out, report);
			out.close();
		} catch (IOException ex) {
			throw new EngineException("exception when updating runnable into a document", ex);
		}
	}

	@Override
	public void close() {

	}
}
