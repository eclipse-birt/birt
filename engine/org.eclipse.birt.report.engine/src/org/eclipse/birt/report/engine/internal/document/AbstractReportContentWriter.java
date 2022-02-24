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

package org.eclipse.birt.report.engine.internal.document;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;

/**
 * writer used to create the content stream.
 *
 */
abstract public class AbstractReportContentWriter implements IReportContentWriter {

	protected static Logger logger = Logger.getLogger(IReportContentWriter.class.getName());

	public long writeFullContent(IContent content) throws IOException, BirtException {
		long offset = getOffset();
		new ContentWriterVisitor().write(content, this);
		return offset;
	}

	/**
	 * use to writer the content into the disk.
	 * 
	 */
	private static class ContentWriterVisitor extends ContentVisitorAdapter {

		public void write(IContent content, IReportContentWriter writer) throws BirtException {
			visit(content, writer);
		}

		public Object visitContent(IContent content, Object value) {
			IReportContentWriter writer = (IReportContentWriter) value;
			try {
				writer.writeContent(content);
				Iterator iter = content.getChildren().iterator();
				while (iter.hasNext()) {
					IContent child = (IContent) iter.next();
					visitContent(child, value);
				}
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "write content failed");
			}
			return value;
		}

		public Object visitPage(IPageContent page, Object value) {
			IReportContentWriter writer = (IReportContentWriter) value;
			try {
				writer.writeContent(page);
				// output all the page header
				Iterator iter = page.getHeader().iterator();
				while (iter.hasNext()) {
					IContent content = (IContent) iter.next();
					visitContent(content, value);
				}
				// output all the page footer
				iter = page.getFooter().iterator();
				while (iter.hasNext()) {
					IContent content = (IContent) iter.next();
					visitContent(content, value);
				}
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "write content failed");
			}
			return value;
		}
	}

}
