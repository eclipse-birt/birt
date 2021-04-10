
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.api.cube;

import java.io.EOFException;
import java.io.IOException;
import java.util.Map;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineThreadLocal;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.NamingUtil;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Hierarchy;

/**
 * 
 */

public class CubeMaterializer {
	private IDocumentManager documentManager;
	private DataEngine dataEngine;

	/**
	 * 
	 * @param pathName
	 * @param managerName
	 * @throws BirtOlapException
	 * @throws IOException
	 */
	public CubeMaterializer(DataEngineImpl dataEngine, String managerName) throws DataException, IOException {
		this(dataEngine, managerName, 0);
	}

	/**
	 * 
	 * @param pathName
	 * @param managerName
	 * @param cacheSize
	 * @throws DataException
	 * @throws IOException
	 */
	public CubeMaterializer(DataEngineImpl dataEngine, String managerName, int cacheSize)
			throws DataException, IOException {
		this.dataEngine = dataEngine;
		setShutdownListener();
		DataEngineThreadLocal.getInstance().getPathManager().setTempPath(dataEngine.getSession().getTempDir());
		documentManager = DocumentManagerFactory.createFileDocumentManager(dataEngine.getSession().getTempDir(),
				managerName, cacheSize);
		if (this.dataEngine != null) {
			DocManagerMap.getDocManagerMap().set(String.valueOf(this.dataEngine.hashCode()),
					dataEngine.getSession().getTempDir() + managerName, documentManager);
		}
	}

	/**
	 * 
	 * @throws DataException
	 * @throws IOException
	 */
	public CubeMaterializer(DataEngineImpl dataEngine) throws DataException, IOException {
		this.dataEngine = dataEngine;
		setShutdownListener();
		documentManager = DocumentManagerFactory.createFileDocumentManager(dataEngine.getSession().getTempDir());
	}

	/**
	 * 
	 */
	private void setShutdownListener() {
		if (dataEngine == null)
			return;
		dataEngine.addShutdownListener(new DocManagerReleaser(dataEngine));
	}

	/**
	 * 
	 * @return
	 */
	public IDocumentManager getDocumentManager() {
		return documentManager;
	}

	/**
	 * @param dimensionName
	 * @param hierarchyName
	 * @param iterator
	 * @param levelDefs
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public IHierarchy createHierarchy(String dimensionName, String hierarchyName, IDatasetIterator iterator,
			ILevelDefn[] levelDefs, StopSign stopSign) throws IOException, BirtException {
		Hierarchy hierarchy = new Hierarchy(documentManager, dimensionName, hierarchyName);
		hierarchy.createAndSaveHierarchy(iterator, levelDefs, stopSign);
		return hierarchy;
	}

	/**
	 * 
	 * @param name
	 * @param hierarchy
	 * @return
	 * @throws BirtException
	 * @throws IOException
	 */
	public IDimension createDimension(String name, IHierarchy hierarchy) throws BirtException, IOException {
		if (hierarchy instanceof Hierarchy) {
			return new Dimension(name, documentManager, (Hierarchy) hierarchy, false);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param name
	 * @param hierarchy
	 * @return
	 * @throws BirtException
	 * @throws IOException
	 */
	public IDimension createTimeDimension(String name, IHierarchy hierarchy) throws BirtException, IOException {
		if (hierarchy instanceof Hierarchy) {
			return new Dimension(name, documentManager, (Hierarchy) hierarchy, true);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param name
	 * @param dimensions
	 * @param factTable
	 * @param measureColumns
	 * @param stopSign
	 * @return
	 * @deprecated
	 * @throws IOException
	 * @throws BirtException
	 */
	public void createCube(String name, String[][] keyColumnNames, IDimension[] dimensions, IDatasetIterator factTable,
			String[] measureColumns, StopSign stopSign) throws IOException, BirtException {
		createCube(name, keyColumnNames, keyColumnNames, dimensions, factTable, measureColumns, 0, stopSign);
	}

	/**
	 * 
	 * @param name
	 * @param factTableJointColumnNames
	 * @param DimJointColumnNames
	 * @param dimensions
	 * @param factTable
	 * @param measureColumns
	 * @param stopSign
	 * @throws IOException
	 * @throws BirtException
	 */
	public void createCube(String name, String[][] factTableJointColumnNames, String[][] DimJointColumnNames,
			IDimension[] dimensions, IDatasetIterator factTable, String[] measureColumns, long cacheSize,
			StopSign stopSign) throws IOException, BirtException {
		this.createCube(name, factTableJointColumnNames, DimJointColumnNames, dimensions, factTable, measureColumns,
				null, null, cacheSize, stopSign);
	}

	/**
	 * 
	 * @param name
	 * @param factTableJointColumnNames
	 * @param DimJointColumnNames
	 * @param dimensions
	 * @param factTable
	 * @param measureColumns
	 * @param stopSign
	 * @throws IOException
	 * @throws BirtException
	 */
	public void createCube(String name, String[][] factTableJointColumnNames, String[][] DimJointColumnNames,
			IDimension[] dimensions, IDatasetIterator factTable, String[] measureColumns, Map calculatedMeasure,
			String[] measureAggrFunctionNames, long cacheSize, StopSign stopSign) throws IOException, BirtException {
		if (dimensions.length == 0) {
			throw new DataException(ResourceConstants.MISSING_DIMENSION_IN_CUBE, name);
		}
		Cube cube = new Cube(name, documentManager);
		cube.create(factTableJointColumnNames, DimJointColumnNames, dimensions, factTable, measureColumns,
				calculatedMeasure, measureAggrFunctionNames, cacheSize, stopSign);
		cube.close();
		documentManager.flush();
	}

	/**
	 * 
	 * @param cubeName
	 * @param writer
	 * @throws IOException
	 * @throws DataException
	 */
	public void saveCubeToReportDocument(String cubeName, IDocArchiveWriter writer, StopSign stopSign)
			throws IOException, DataException {
		Cube cube = new Cube(cubeName, documentManager);
		cube.load(stopSign);
		// save cube
		saveDocObjToReportDocument(NamingUtil.getCubeDocName(cubeName), writer, stopSign);
		// save facttable
		String factTableName = cube.getFactTable().getName();
		saveDocObjToReportDocument(NamingUtil.getFactTableName(factTableName), writer, stopSign);
		saveDocObjToReportDocument(NamingUtil.getFTSUListName(factTableName), writer, stopSign);
		// save FTSU
		IDocumentObject documentObject = documentManager.openDocumentObject(NamingUtil.getFTSUListName(factTableName));
		try {
			String FTSUName = documentObject.readString();
			while (FTSUName != null) {
				saveDocObjToReportDocument(FTSUName, writer, stopSign);
				FTSUName = documentObject.readString();
			}
		} catch (EOFException e) {

		}
		// save dimension
		IDimension[] dimensions = cube.getDimesions();
		for (int i = 0; i < dimensions.length; i++) {
			saveDocObjToReportDocument(NamingUtil.getDimensionDocName(dimensions[i].getName()), writer, stopSign);
			IHierarchy hierarchy = dimensions[i].getHierarchy();
			saveDocObjToReportDocument(NamingUtil.getHierarchyDocName(dimensions[i].getName(), hierarchy.getName()),
					writer, stopSign);
			saveDocObjToReportDocument(
					NamingUtil.getHierarchyOffsetDocName(dimensions[i].getName(), hierarchy.getName()), writer,
					stopSign);
			ILevel[] levels = hierarchy.getLevels();
			for (int j = 0; j < levels.length; j++) {
				saveDocObjToReportDocument(
						NamingUtil.getLevelIndexDocName(dimensions[i].getName(), levels[j].getName()), writer,
						stopSign);
				saveDocObjToReportDocument(
						NamingUtil.getLevelIndexOffsetDocName(dimensions[i].getName(), levels[j].getName()), writer,
						stopSign);

			}
		}

		writer.flush();
	}

	/**
	 * 
	 * @param name
	 * @param writer
	 * @param stopSign
	 * @throws IOException
	 * @throws DataException
	 */
	private void saveDocObjToReportDocument(String name, IDocArchiveWriter writer, StopSign stopSign)
			throws IOException, DataException {
		if (writer.exists(name)) {
			return;
		}
		IDocumentObject documentObject = documentManager.openDocumentObject(name);
		RAOutputStream outputStreadm = writer.createRandomAccessStream(name);
		byte[] buffer = new byte[4096];

		int readSize = documentObject.read(buffer, 0, buffer.length);

		while (!stopSign.isStopped() && readSize >= 0) {
			outputStreadm.write(buffer, 0, readSize);
			readSize = documentObject.read(buffer, 0, buffer.length);
		}
		outputStreadm.flush();
		outputStreadm.close();
		documentObject.close();
	}

	/**
	 * @throws IOException
	 * 
	 */
	public void close() throws IOException {
		documentManager.flush();
	}
}