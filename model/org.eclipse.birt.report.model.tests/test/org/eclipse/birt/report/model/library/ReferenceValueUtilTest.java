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

package org.eclipse.birt.report.model.library;

import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;

/**
 * Tests whether report design can handle the cases of loading libraires.
 */

public class ReferenceValueUtilTest extends BaseTestCase {

	/**
	 * Tests elements that is extended in multi-level.
	 *
	 * <ul>
	 * <li>A table is extended triple times. DataSet of it can be resolved.
	 * <li>A table is extended triple times. DataSet of it cannot be resolved.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testMultiExtendedElements() throws Exception {
		openDesign("DesignWithOneCompositeLibrary.xml"); //$NON-NLS-1$

		TableHandle table = (TableHandle) designHandle.findElement("table3"); //$NON-NLS-1$
		ReferenceValue refValue = (ReferenceValue) table.getElement().getProperty(design,
				IReportItemModel.DATA_SET_PROP);

		assertEquals("Lib1.dataSet1", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, design));

		LibraryHandle compsiteLib = designHandle.getLibrary("CompositeLib"); //$NON-NLS-1$
		assertEquals("Lib1.dataSet1", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, compsiteLib.getModule()));

		LibraryHandle lib1 = compsiteLib.getLibrary("Lib1"); //$NON-NLS-1$
		assertEquals("dataSet1", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, lib1.getModule()));

		table = (TableHandle) designHandle.findElement("table4"); //$NON-NLS-1$
		refValue = (ReferenceValue) table.getElement().getProperty(design, IReportItemModel.DATA_SET_PROP);
		assertEquals("CompositeLib.dataSet1", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, design));
		assertEquals("dataSet1", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, compsiteLib.getModule()));
		assertEquals("CompositeLib.dataSet1", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, lib1.getModule()));

		table = (TableHandle) designHandle.findElement("table5"); //$NON-NLS-1$
		refValue = (ReferenceValue) table.getElement().getProperty(design, IReportItemModel.DATA_SET_PROP);

		assertEquals("Lib1.noExistedDataSet", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, design));
		assertEquals("Lib1.noExistedDataSet", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, compsiteLib.getModule()));
		assertEquals("noExistedDataSet", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, lib1.getModule()));
	}

	/**
	 * Tests elements that is extended in multi-level.
	 *
	 * <ul>
	 * <li>A image is extended triple times. Embedded iamge of it can be resolved.
	 * <li>A image is extended triple times. Embedded iamge of it cannot be
	 * resolved.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testMultiExtendedImages() throws Exception {
		openDesign("LibraryStructureTest_4.xml"); //$NON-NLS-1$

		ImageHandle image = (ImageHandle) designHandle.findElement("image1"); //$NON-NLS-1$

		ReferenceValue refValue = (ReferenceValue) image.getElement().getProperty(design,
				IImageItemModel.IMAGE_NAME_PROP);
		assertEquals("Lib1.image1", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, design));

		LibraryHandle compsiteLib = designHandle.getLibrary("CompositeLib"); //$NON-NLS-1$
		assertEquals("Lib1.image1", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, compsiteLib.getModule()));

		LibraryHandle lib1 = compsiteLib.getLibrary("Lib1"); //$NON-NLS-1$
		assertEquals("image1", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, lib1.getModule()));

		image = (ImageHandle) designHandle.findElement("image2"); //$NON-NLS-1$
		refValue = (ReferenceValue) image.getElement().getProperty(design, IImageItemModel.IMAGE_NAME_PROP);

		assertEquals("Lib1.noExistedImage", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, design));
		assertEquals("Lib1.noExistedImage", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, compsiteLib.getModule()));
		assertEquals("noExistedImage", ReferenceValueUtil //$NON-NLS-1$
				.needTheNamespacePrefix(refValue, lib1.getModule()));
	}
}
