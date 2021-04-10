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

package org.eclipse.birt.data.engine.olap.data.impl;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTable;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableAccessor;

/**
 * Default implements of ICube interface.
 */

public class Cube implements ICube {
	protected String name;
	protected IDocumentManager documentManager;
	protected IDimension[] dimension;
	private FactTable factTable;

	private static Logger logger = Logger.getLogger(Cube.class.getName());

	/**
	 * 
	 * @param name
	 * @param documentManager
	 */
	public Cube(String name, IDocumentManager documentManager) {
		Object[] params = { name, documentManager };
		logger.entering(Cube.class.getName(), "Cube", params);
		this.name = name;
		this.documentManager = documentManager;
		logger.exiting(Cube.class.getName(), "Cube");
	}

	/**
	 * 
	 * @param keyColumnNames
	 * @param dimension
	 * @param iterator
	 * @param measureColumnName
	 * @param stopSign
	 * @throws IOException
	 * @throws BirtException
	 */
	public void create(String[][] keyColumnNames, IDimension[] dimension, IDatasetIterator iterator,
			String[] measureColumnName, long cacheSize, StopSign stopSign) throws IOException, BirtException {
		create(keyColumnNames, keyColumnNames, dimension, iterator, measureColumnName, cacheSize, stopSign);
	}

	/**
	 * 
	 * @param keyColumnNames
	 * @param dimension
	 * @param iterator
	 * @param measureColumnName
	 * @param stopSign
	 * @throws IOException
	 * @throws BirtException
	 */
	public void create(String[][] keyColumnNames, IDimension[] dimension, IDatasetIterator iterator,
			String[] measureColumnName, StopSign stopSign) throws IOException, BirtException {
		create(keyColumnNames, keyColumnNames, dimension, iterator, measureColumnName, 0, stopSign);
	}

	public void create(String[][] factTableJointColumnNames, String[][] DimJointColumnNames, IDimension[] dimension,
			IDatasetIterator iterator, String[] measureColumnName, Map calculatedMeasure,
			String[] measureColumnAggregations, long cacheSize, StopSign stopSign) throws IOException, BirtException {
		IDocumentObject documentObject = documentManager.createDocumentObject(NamingUtil.getCubeDocName(name));
		documentObject.writeString(name);
		documentObject.writeInt(dimension.length);
		for (int i = 0; i < dimension.length; i++) {
			documentObject.writeString(dimension[i].getName());
		}
		this.dimension = dimension;
		Dimension[] tDimensions = new Dimension[dimension.length];
		for (int i = 0; i < tDimensions.length; i++) {
			tDimensions[i] = (Dimension) dimension[i];
		}
		FactTableAccessor factTableConstructor = new FactTableAccessor(documentManager);
		factTableConstructor.setMemoryCacheSize(cacheSize);
		factTable = factTableConstructor.saveFactTable(name, factTableJointColumnNames, DimJointColumnNames, iterator,
				tDimensions, measureColumnName, calculatedMeasure, measureColumnAggregations, stopSign);
		documentObject.close();
		documentManager.flush();
	}

	/**
	 * 
	 * @param factTableJointColumnNames
	 * @param DimJointColumnNames
	 * @param dimension
	 * @param iterator
	 * @param measureColumnName
	 * @param stopSign
	 * @throws IOException
	 * @throws BirtException
	 */
	public void create(String[][] factTableJointColumnNames, String[][] DimJointColumnNames, IDimension[] dimension,
			IDatasetIterator iterator, String[] measureColumnName, long cacheSize, StopSign stopSign)
			throws IOException, BirtException {
		this.create(factTableJointColumnNames, DimJointColumnNames, dimension, iterator, measureColumnName, null, null,
				cacheSize, stopSign);
	}

	/**
	 * 
	 * @param stopSign
	 * @throws IOException
	 * @throws BirtException
	 */
	public void load(StopSign stopSign) throws IOException, DataException {
		IDocumentObject documentObject = documentManager.openDocumentObject(NamingUtil.getCubeDocName(name));

		if (documentObject == null)
			throw new DataException(ResourceConstants.DOCUMENTOBJECT_NOT_EXIST, new Object[] { name });

		documentObject.seek(0);
		name = documentObject.readString();
		dimension = new IDimension[documentObject.readInt()];

		for (int i = 0; i < dimension.length; i++) {
			String name = documentObject.readString();
			dimension[i] = loadDimension(name);
		}
		FactTableAccessor factTableConstructor = new FactTableAccessor(documentManager);
		factTable = factTableConstructor.load(name, stopSign);
		documentObject.close();
	}

	protected IDimension loadDimension(String name) throws DataException, IOException {
		return DimensionFactory.loadDimension(name, documentManager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.api.ICube#getDimesions()
	 */
	public IDimension[] getDimesions() {
		return dimension;
	}

	/**
	 * 
	 * @return
	 */
	public FactTable getFactTable() {
		return factTable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.api.ICube#close()
	 */
	public void close() throws IOException {
		for (int i = 0; i < dimension.length; i++) {
			dimension[i].close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.api.cube.ICube#getAllMeasureNames()
	 */
	public String[] getMeasureNames() {
		return factTable.getMeasureNames();
	}

}
