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

package org.eclipse.birt.report.model.api.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.ReportDesignSerializer;
import org.eclipse.birt.report.model.writer.DocumentWriter;

/**
 * Utility class for the serialize a report design in which all the elements
 * localize their property values from the referred external resources, such as
 * library elements, library embedded images.
 */

public class DocumentUtil {

	/**
	 * Writes the report design to the given output stream. The caller must call
	 * <code>onSave</code> if the save succeeds.
	 * 
	 * @param designHandle the report design to serialize
	 * 
	 * @param out          the output stream to which the design is written.
	 * @throws IOException if the file cannot be written to the output stream
	 *                     successfully.
	 */

	public static ReportDesignHandle serialize(ReportDesignHandle designHandle, OutputStream out) throws IOException {
		assert out != null;
		if (designHandle == null)
			return null;

		ReportDesign target = null;
		ReportDesign source = (ReportDesign) designHandle.getModule();

		// localize element property value
		List list = (List) source.getLocalProperty(source.getRoot(), IModuleModel.LIBRARIES_PROP);
		if (list == null || list.size() == 0) {
			// need to make a copy for the original one to avoid possible
			// multiple thread issues.

			Module copy = (Module) designHandle.copy();
			ReportDesign clonedDesign = (ReportDesign) copy;
			new DocumentWriter(clonedDesign).write(out);
			return clonedDesign.handle();
		}

		target = localizeDesign(source);

		assert target != null;

		// use the writer for the document, not the general design writer
		new DocumentWriter(target).write(out);

		return target.handle();
	}

	/**
	 * Gets a localized report design based on the source design.
	 * 
	 * @param source the source design
	 * @return the localized report design based on the source design
	 */

	static ReportDesign localizeDesign(ReportDesign source) {
		assert source != null;

		ReportDesignSerializer visitor = new ReportDesignSerializer();
		source.apply(visitor);

		return visitor.getTarget();
	}

}
