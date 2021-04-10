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

package org.eclipse.birt.report.item.crosstab.core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.IDimensionModel;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

/**
 * Base test case.
 * 
 */

abstract public class BaseTestCase extends TestCase {

	/**
	 * design handle
	 */
	protected ReportDesignHandle designHandle = null;

	private static final String INPUT_FOLDER = "input/";//$NON-NLS-1$
	private static final String GOLDEN_FOLDER = "golden/";//$NON-NLS-1$

	/**
	 * Byte array output stream.
	 */

	protected ByteArrayOutputStream os = null;

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if (designHandle != null) {
			designHandle.close();
			designHandle = null;
		}
	}

	/**
	 * Opens report design.
	 * 
	 * @param fileName
	 * @throws Exception
	 */

	protected void openDesign(String fileName) throws Exception {
		ThreadResources.setLocale(ULocale.ENGLISH);
		IDesignEngine designEngine = new DesignEngine(new DesignConfig());
		// MetaDataDictionary.reset( );
		// initialize the metadata.

		designEngine.getMetaData();

		SessionHandle sh = designEngine.newSessionHandle(ULocale.getDefault());
		designHandle = sh.openDesign(getResource(INPUT_FOLDER + fileName));
	}

	private ReportDesignHandle loadGolden(String fileName) throws Exception {
		ThreadResources.setLocale(ULocale.ENGLISH);
		IDesignEngine designEngine = new DesignEngine(new DesignConfig());
		// MetaDataDictionary.reset( );
		// initialize the metadata.

		designEngine.getMetaData();

		SessionHandle sh = designEngine.newSessionHandle(ULocale.getDefault());
		return sh.openDesign(getResource(GOLDEN_FOLDER + fileName));
	}

	/**
	 * Create report design.
	 * 
	 * @throws Exception
	 */

	protected void createDesign() throws Exception {
		ThreadResources.setLocale(ULocale.ENGLISH);
		IDesignEngine designEngine = new DesignEngine(new DesignConfig());
		// MetaDataDictionary.reset( );
		// initialize the metadata.

		designEngine.getMetaData();

		SessionHandle sh = designEngine.newSessionHandle(ULocale.getDefault());
		designHandle = sh.createDesign();
	}

	/**
	 * Gets resource file path.
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */

	private String getResource(String fileName) throws Exception {
		if (fileName == null)
			return null;
		URL url = getClass().getResource(fileName);
		if (url == null)
			return null;
		return url.toString();
	}

	/**
	 * Gets the input stream of the given name resources.
	 */

	protected InputStream getResourceAStream(String name) {
		return this.getClass().getResourceAsStream(name);
	}

	/**
	 * Compares the two text files.
	 * 
	 * @param goldenReader the reader for golden file
	 * @param outputReader the reader for output file
	 * @return true if two text files are same.
	 * @throws Exception if any exception
	 */
	private boolean compareFile(Reader goldenReader, Reader outputReader) throws Exception {
		StringBuffer errorText = new StringBuffer();

		BufferedReader lineReaderA = null;
		BufferedReader lineReaderB = null;
		boolean same = true;
		int lineNo = 1;
		try {
			lineReaderA = new BufferedReader(goldenReader);
			lineReaderB = new BufferedReader(outputReader);

			String strA = lineReaderA.readLine().trim();
			String strB = lineReaderB.readLine().trim();
			while (strA != null) {
				// ignore id part
				strA = strA.replaceAll("id=\"\\d+\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
				strB = strB.replaceAll("id=\"\\d+\"", ""); //$NON-NLS-1$ //$NON-NLS-2$

				same = strA.trim().equals(strB.trim());

				if (!same) {
					StringBuffer message = new StringBuffer();

					message.append("line="); //$NON-NLS-1$
					message.append(lineNo);
					message.append(" is different:\n"); //$NON-NLS-1$
					message.append(" The line from golden file: ");//$NON-NLS-1$
					message.append(strA);
					message.append("\n");//$NON-NLS-1$
					message.append(" The line from output file: ");//$NON-NLS-1$
					message.append(strB);
					message.append("\n");//$NON-NLS-1$
					throw new Exception(message.toString());
				}

				strA = lineReaderA.readLine();
				strB = lineReaderB.readLine();
				lineNo++;
			}
			same = strB == null;
		} finally {
			try {
				if (lineReaderA != null)
					lineReaderA.close();
				if (lineReaderB != null)
					lineReaderB.close();
			} catch (Exception e) {
				lineReaderA = null;
				lineReaderB = null;

				errorText.append(e.toString());

				throw new Exception(errorText.toString());
			}
		}

		return same;
	}

	/**
	 * Compares two text file. The comparison will ignore the line containing
	 * "modificationDate".
	 * 
	 * @param goldenFileName the 1st file name to be compared.
	 * @param os             the 2nd output stream to be compared.
	 * @return true if two text files are same char by char
	 * @throws Exception if any exception.
	 */
	protected boolean compareFile(String goldenFileName) throws Exception {
		if (os == null)
			return false;

		// String tmpGoldenFileName = GOLDEN_FOLDER + goldenFileName;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ReportDesignHandle golden = loadGolden(goldenFileName);
		golden.serialize(baos);
		baos.flush();

		// InputStream streamA = getResourceAStream( tmpGoldenFileName );
		InputStream streamA = new ByteArrayInputStream(baos.toByteArray());

		InputStream streamB = new ByteArrayInputStream(os.toByteArray());
		InputStreamReader readerA = new InputStreamReader(streamA);
		InputStreamReader readerB = new InputStreamReader(streamB);

		boolean ok = true;
		try {
			ok = compareFile(readerA, readerB);
		} catch (Exception e) {
			throw e;
		}

		return ok;
	}

	/**
	 * Eventually, this method will call
	 * {@link ReportDesignHandle#serialize(java.io.OutputStream)}to save the output
	 * file of some unit test.
	 * 
	 * @param moduleHandle the module to save, either a report design or a library
	 * @throws IOException if error occurs while saving the file.
	 */

	protected void save(ModuleHandle moduleHandle) throws IOException {
		os = new ByteArrayOutputStream();
		if (moduleHandle != null)
			moduleHandle.serialize(os);
		os.close();
	}

	protected void save(ModuleHandle moduleHandle, String path) throws IOException {
		moduleHandle.saveAs(path);
	}

	/**
	 * Create simple cube handle.
	 * 
	 * @param module
	 * @return
	 */

	protected CubeHandle prepareCube() throws SemanticException {
		ElementFactory factory = designHandle.getElementFactory();

		// create cube
		CubeHandle cubeHandle = factory.newTabularCube("Cube_Test_1");//$NON-NLS-1$
		designHandle.getCubes().add(cubeHandle);

		DimensionHandle dimensionHandle = factory.newTabularDimension("Customer");//$NON-NLS-1$
		cubeHandle.add(CubeHandle.DIMENSIONS_PROP, dimensionHandle);

		HierarchyHandle hierarchyHandle = factory.newTabularHierarchy("Hierarchy");//$NON-NLS-1$
		dimensionHandle.add(DimensionHandle.HIERARCHIES_PROP, hierarchyHandle);

		LevelHandle levelHandle = factory.newTabularLevel(dimensionHandle, "CUSTOMER_SEX");//$NON-NLS-1$
		hierarchyHandle.add(HierarchyHandle.LEVELS_PROP, levelHandle);

		levelHandle = factory.newTabularLevel(dimensionHandle, "CUSTOMER_REGION");//$NON-NLS-1$
		hierarchyHandle.add(HierarchyHandle.LEVELS_PROP, levelHandle);

		DimensionHandle dimensionHandle2 = factory.newTabularDimension("Product");//$NON-NLS-1$
		cubeHandle.add(CubeHandle.DIMENSIONS_PROP, dimensionHandle2);

		HierarchyHandle hierarchyHandle2 = factory.newTabularHierarchy("Hierarchy2");//$NON-NLS-1$
		dimensionHandle2.add(DimensionHandle.HIERARCHIES_PROP, hierarchyHandle2);

		LevelHandle levelHandle2 = factory.newTabularLevel(dimensionHandle2, "PRODUCT_TYPE");//$NON-NLS-1$
		hierarchyHandle2.add(HierarchyHandle.LEVELS_PROP, levelHandle2);

		levelHandle2 = factory.newTabularLevel(dimensionHandle2, "PRODUCT_NAME");//$NON-NLS-1$
		hierarchyHandle2.add(HierarchyHandle.LEVELS_PROP, levelHandle2);

		MeasureGroupHandle groupHandle = factory.newTabularMeasureGroup("measure group");//$NON-NLS-1$
		cubeHandle.add(CubeHandle.MEASURE_GROUPS_PROP, groupHandle);

		MeasureHandle measureHandle = factory.newTabularMeasure("QUANTITY_PRICE");//$NON-NLS-1$
		groupHandle.add(MeasureGroupHandle.MEASURES_PROP, measureHandle);

		measureHandle = factory.newTabularMeasure("QUANTITY");//$NON-NLS-1$
		groupHandle.add(MeasureGroupHandle.MEASURES_PROP, measureHandle);

		measureHandle = factory.newTabularMeasure("QUANTITY_NUMBER");//$NON-NLS-1$
		groupHandle.add(MeasureGroupHandle.MEASURES_PROP, measureHandle);

		measureHandle = factory.newTabularMeasure("QUANTITY_SIZE");//$NON-NLS-1$
		groupHandle.add(MeasureGroupHandle.MEASURES_PROP, measureHandle);
		return cubeHandle;
	}

	protected CrosstabReportItemHandle createSimpleCrosstab(ModuleHandle module) {
		assert designHandle != null;

		try {
			// create cube
			CubeHandle cubeHandle = prepareCube();

			DimensionHandle dimensionHandle = cubeHandle.getDimension("Customer");//$NON-NLS-1$

			DimensionHandle dimensionHandle2 = cubeHandle.getDimension("Product");//$NON-NLS-1$

			PropertyHandle propHandle = dimensionHandle.getPropertyHandle(IDimensionModel.HIERARCHIES_PROP);
			HierarchyHandle hierarchyHandle = (HierarchyHandle) propHandle.get(0);

			propHandle = dimensionHandle2.getPropertyHandle(IDimensionModel.HIERARCHIES_PROP);
			HierarchyHandle hierarchyHandle2 = (HierarchyHandle) propHandle.get(0);

			// create cross tab
			CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil
					.getReportItem(CrosstabExtendedItemFactory.createCrosstabReportItem(module, cubeHandle, null));

			DimensionViewHandle dimensionViewHandle = crosstabItem.insertDimension(dimensionHandle,
					ICrosstabConstants.ROW_AXIS_TYPE, -1);

			dimensionViewHandle.insertLevel(hierarchyHandle.getLevel(0), -1);
			dimensionViewHandle.insertLevel(hierarchyHandle.getLevel(1), -1);

			DimensionViewHandle dimensionViewHandle2 = crosstabItem.insertDimension(dimensionHandle2,
					ICrosstabConstants.COLUMN_AXIS_TYPE, -1);

			dimensionViewHandle2.insertLevel(hierarchyHandle2.getLevel(0), -1);
			dimensionViewHandle2.insertLevel(hierarchyHandle2.getLevel(1), -1);

			crosstabItem.insertMeasure(cubeHandle.getMeasure("QUANTITY_PRICE"), -1);//$NON-NLS-1$
			crosstabItem.insertMeasure(cubeHandle.getMeasure("QUANTITY"), -1);//$NON-NLS-1$
			crosstabItem.insertMeasure(cubeHandle.getMeasure("QUANTITY_NUMBER"), -1);//$NON-NLS-1$
			crosstabItem.insertMeasure(cubeHandle.getMeasure("QUANTITY_SIZE"), -1);//$NON-NLS-1$
			return crosstabItem;
		} catch (SemanticException e) {
			e.printStackTrace();
		}
		return null;
	}
}
