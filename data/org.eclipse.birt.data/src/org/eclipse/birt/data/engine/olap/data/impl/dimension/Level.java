
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
package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import java.io.IOException;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.DiskIndex;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.IndexKey;

/**
 * Describes a level. A level is composed of member located at this level.
 */

public class Level implements ILevel {
	private String name;
	private int[] keyDataType;
	private String[] keyColNames;
	private int[] attributeDataTypes;
	private String[] attributeColNames;
	private int size;
	private String levelType;

	private DiskIndex diskIndex = null;

	private static Logger logger = Logger.getLogger(Level.class.getName());

	/**
	 * 
	 * @param documentManager
	 * @param levelDef
	 * @param keyDataType
	 * @param attributeDataTypes
	 * @param size
	 * @throws IOException
	 * @throws DataException
	 */
	public Level(IDocumentManager documentManager, ILevelDefn levelDef, int[] keyDataType, int[] attributeDataTypes,
			int size, DiskIndex diskIndex) throws IOException, DataException {
		Object[] params = { documentManager, levelDef, keyDataType, attributeDataTypes, Integer.valueOf(size),
				diskIndex };
		logger.entering(Level.class.getName(), "Level", params);
		this.name = levelDef.getLevelName();
		this.setKeyDataType(keyDataType);
		this.setKeyColNames(levelDef.getKeyColumns());
		this.setAttributeDataTypes(attributeDataTypes);
		this.setAttributeColNames(levelDef.getAttributeColumns());
		this.setSize(size);
		this.setDiskIndex(diskIndex);
		logger.exiting(Level.class.getName(), "Level");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.api.ILevel#getKeyDataType(java.lang.String)
	 */
	public int getKeyDataType(String keyName) {
		for (int i = 0; i < getKeyColNames().length; i++) {
			if (getKeyColNames()[i].equals(keyName)) {
				return this.getKeyDataType()[i];
			}
		}
		return DataType.UNKNOWN_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.api.ILevel#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.api.ILevel#size()
	 */
	public int size() {
		return getSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		Level other = (Level) o;
		return this.name.equals(other.name);
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.name.hashCode();
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (diskIndex != null)
			diskIndex.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.api.ILevel#getAttributeDataType(java.lang.
	 * String)
	 */
	public int getAttributeDataType(String attributeName) {
		for (int i = 0; i < getAttributeColNames().length; i++) {
			if (getAttributeColNames()[i].equals(attributeName)) {
				return this.getAttributeDataTypes()[i];
			}
		}
		return DataType.UNKNOWN_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.api.ILevel#getAttributeNames()
	 */
	public String[] getAttributeNames() {
		return getAttributeColNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.api.ILevel#getKeyName()
	 */
	public String[] getKeyNames() {
		return getKeyColNames();
	}

	/**
	 * @param keyDataType the keyDataType to set
	 */
	void setKeyDataType(int[] keyDataType) {
		this.keyDataType = keyDataType;
	}

	/**
	 * @return the keyDataType
	 */
	int[] getKeyDataType() {
		return keyDataType;
	}

	/**
	 * @param keyColNames the keyColNames to set
	 */
	void setKeyColNames(String[] keyColNames) {
		this.keyColNames = keyColNames;
	}

	/**
	 * @return the keyColNames
	 */
	String[] getKeyColNames() {
		return keyColNames;
	}

	/**
	 * @param attributeDataTypes the attributeDataTypes to set
	 */
	void setAttributeDataTypes(int[] attributeDataTypes) {
		this.attributeDataTypes = attributeDataTypes;
	}

	/**
	 * @return the attributeDataTypes
	 */
	int[] getAttributeDataTypes() {
		return attributeDataTypes;
	}

	/**
	 * @param attributeColNames the attributeColNames to set
	 */
	void setAttributeColNames(String[] attributeColNames) {
		this.attributeColNames = attributeColNames;
	}

	/**
	 * @return the attributeColNames
	 */
	String[] getAttributeColNames() {
		return attributeColNames;
	}

	/**
	 * @param size the size to set
	 */
	void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the size
	 */
	int getSize() {
		return size;
	}

	/**
	 * @param diskIndex the diskIndex to set
	 */
	void setDiskIndex(DiskIndex diskIndex) {
		this.diskIndex = diskIndex;
	}

	/**
	 * @return the diskIndex
	 */
	DiskIndex getDiskIndex() {
		return diskIndex;
	}

	public IDiskArray getAllPosition() throws DataException, IOException {
		IDiskArray result;
		if (diskIndex == null) {
			result = new BufferedPrimitiveDiskArray(0);
		} else {
			IDiskArray indexKeyArray = diskIndex.findAll();
			result = new BufferedPrimitiveDiskArray(indexKeyArray.size());
			for (int i = 0; i < indexKeyArray.size(); i++) {
				IndexKey key = (IndexKey) indexKeyArray.get(i);
				result.add(Integer.valueOf(key.getDimensionPos()[0]));
			}
		}
		return result;
	}

	void setLevelType(String levelType) {
		this.levelType = levelType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.api.ILevel#getLeveType()
	 */
	public String getLeveType() {
		return levelType;
	}
}
