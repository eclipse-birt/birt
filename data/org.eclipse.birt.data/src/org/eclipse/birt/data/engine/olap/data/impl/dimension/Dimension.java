/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import java.io.IOException;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;
import org.eclipse.birt.data.engine.olap.data.impl.NamingUtil;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.DiskIndex;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.IndexKey;
import org.eclipse.birt.data.engine.script.ScriptConstants;

/**
 * Describes a dimension. In current implement a dimension only contains one
 * hierarchy.
 */

public class Dimension implements IDimension {

	protected String name = null;
	protected IDocumentManager documentManager = null;
	private IDocumentObject documentObj = null;
	private Hierarchy hierarchy = null;
	private int length = 0;
	private boolean isTime;
	private static Logger logger = Logger.getLogger(Dimension.class.getName());

	protected Dimension() {
	}

	/**
	 *
	 * @param name
	 * @param documentManager
	 * @param hierarchy
	 * @param isTime
	 * @throws BirtException
	 * @throws IOException
	 */
	public Dimension(String name, IDocumentManager documentManager, Hierarchy hierarchy, boolean isTime)
			throws DataException, IOException {
		Object[] params = { name, documentManager, hierarchy, Boolean.valueOf(isTime) };
		logger.entering(Dimension.class.getName(), ScriptConstants.DIMENSION_SCRIPTABLE, params);
		this.name = name;
		this.documentManager = documentManager;
		this.isTime = isTime;
		documentObj = documentManager.createDocumentObject(NamingUtil.getDimensionDocName(name));
		documentObj.writeBoolean(isTime);
		documentObj.writeString(hierarchy.getName());
		ILevel[] levels = hierarchy.getLevels();
		for (int i = 0; i < levels.length; i++) {
			documentObj.writeString(levels[i].getLeveType());
		}
		this.hierarchy = (Hierarchy) hierarchy;
		length = hierarchy.size();
		// close document object
		documentObj.close();
		documentObj = null;
		logger.exiting(Dimension.class.getName(), ScriptConstants.DIMENSION_SCRIPTABLE);
	}

	Dimension(String name, IDocumentManager documentManager) throws IOException, DataException {
		Object[] params = { name, documentManager };
		logger.entering(Dimension.class.getName(), ScriptConstants.DIMENSION_SCRIPTABLE, params);
		this.name = name;
		this.documentManager = documentManager;
		loadFromDisk();
		logger.exiting(Dimension.class.getName(), ScriptConstants.DIMENSION_SCRIPTABLE);
	}

	protected void loadFromDisk() throws IOException, DataException {
		documentObj = documentManager.openDocumentObject(NamingUtil.getDimensionDocName(name));
		if (documentObj == null) {
			throw new DataException(ResourceConstants.DIMENSION_NOT_EXIST, name);
		}
		isTime = documentObj.readBoolean();
		String hierarchyName = documentObj.readString();
		hierarchy = this.loadHierarchy(hierarchyName);
		hierarchy.loadFromDisk();
		length = hierarchy.size();
		Level[] levels = (Level[]) hierarchy.getLevels();
		try {
			for (int i = 0; i < levels.length; i++) {
				levels[i].setLevelType(documentObj.readString());
			}
		} catch (java.io.EOFException e) {
		}
		documentObj.close();
		documentObj = null;
	}

	protected Hierarchy loadHierarchy(String hierarchyName) {
		return new Hierarchy(documentManager, name, hierarchyName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.api.cube.IDimension#getAllRows()
	 */
	@Override
	public IDiskArray getAllRows(StopSign stopSign) throws IOException {
		try {
			return hierarchy.readAllRows(stopSign);
		} catch (DataException e) {
			IOException ex = new IOException(e.getLocalizedMessage());
			ex.initCause(e);
			throw ex;
		}
	}

	/**
	 *
	 * @param position
	 * @return
	 * @throws IOException
	 */
	public DimensionRow getRowByPosition(int position) throws IOException {
		return hierarchy.readRowByPosition(position);
	}

	/**
	 *
	 * @param positionArray
	 * @param stopSign
	 * @return
	 * @throws IOException
	 */
	public IDiskArray getDimensionRowByPositions(IDiskArray positionArray, StopSign stopSign) throws IOException {
		BufferedStructureArray resultArray = new BufferedStructureArray(DimensionRow.getCreator(),
				positionArray.size());

		for (int i = 0; i < positionArray.size(); i++) {
			if (stopSign.isStopped()) {
				break;
			}
			int pos = ((Integer) positionArray.get(i)).intValue();
			resultArray.add(hierarchy.readRowByPosition(pos));
		}
		return resultArray;
	}

	/**
	 *
	 * @param offset
	 * @return
	 * @throws IOException
	 */
	public DimensionRow getDimensionRowByOffset(int offset) throws IOException {
		return hierarchy.readRowByOffset(offset);
	}

	/**
	 *
	 * @param level
	 * @param keyValue
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public IDiskArray find(Level level, Object[] keyValue) throws IOException, DataException {
		DiskIndex index = level.getDiskIndex();
		if (index == null) {
			return null;
		}
		return index.find(keyValue);
	}

	/**
	 *
	 * @param level
	 * @param keyValue
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public IDiskArray findPosition(Level level, Object[] keyValue) throws IOException, DataException {
		IDiskArray indexKeyArray = find(level, keyValue);
		int len = 0;
		for (int i = 0; i < indexKeyArray.size(); i++) {
			IndexKey key = (IndexKey) indexKeyArray.get(i);
			len += key.getDimensionPos().length;
		}
		IDiskArray result = new BufferedPrimitiveDiskArray(len);
		for (int i = 0; i < indexKeyArray.size(); i++) {
			IndexKey key = (IndexKey) indexKeyArray.get(i);
			int[] pos = key.getDimensionPos();
			for (int j = 0; j < pos.length; j++) {
				result.add(Integer.valueOf(pos[j]));
			}
		}
		return result;
	}

	/**
	 *
	 * @param level
	 * @param keyValue
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public IndexKey findFirst(Level level, Object[] keyValue) throws IOException, DataException {
		DiskIndex index = level.getDiskIndex();
		if (index == null) {
			return null;
		}
		return index.findFirst(keyValue);
	}

	/**
	 *
	 * @param level
	 * @param selections
	 * @return Dimension index array.
	 * @throws IOException
	 * @throws DataException
	 */
	public IDiskArray find(Level[] levels, ISelection[][] filters) throws IOException, DataException {
		return DimensionFilterHelper.find(levels, filters);
	}

	public Level getDetailLevel() {
		return (Level) (hierarchy.getLevels()[hierarchy.getLevels().length - 1]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IDimension#findAll()
	 */
	@Override
	public IDiskArray findAll() throws IOException {
		IDiskArray result = new BufferedPrimitiveDiskArray(length);
		int lastPos = length() - 1;
		for (int i = 0; i <= lastPos; i++) {
			result.add(Integer.valueOf(i));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IDimension#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IDimension#getHierarchy()
	 */
	@Override
	public IHierarchy getHierarchy() {
		return hierarchy;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IDimension#isTime()
	 */
	@Override
	public boolean isTime() {
		return isTime;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IDimension#length()
	 */
	@Override
	public int length() {
		return length;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IDimension#close()
	 */
	@Override
	public void close() throws IOException {
		hierarchy.close();
	}

}
