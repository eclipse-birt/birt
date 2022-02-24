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

package org.eclipse.birt.data.engine.impl.rd;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.FolderArchiveReader;
import org.eclipse.birt.core.archive.FolderArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import org.junit.After;
import org.junit.Before;

/**
 * 
 */
public abstract class RDTestCase extends APITestCase {
	protected DataEngine myGenDataEngine;
	protected DataEngine myPreDataEngine;
	protected DataEngine myPreDataEngine2;

	protected IDocArchiveWriter archiveWriter;
	protected IDocArchiveReader archiveReader;

	protected ScriptableObject scope;
	protected String fileName;
	protected String fileName2;

	private static int index = 0;

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#setUp()
	 */
	@Before
	public void rdSetUp() throws Exception {

		index++;
		fileName = getOutputPath() + this.getClass().getSimpleName() + File.separator + this.getTestName()
				+ File.separator + "RptDocumentTemp" + File.separator + "testData_" + index;
		index++;
		fileName2 = getOutputPath() + this.getClass().getSimpleName() + File.separator + this.getTestName()
				+ File.separator + "RptDocumentTemp" + File.separator + "testData_" + index;

		// make sure these 2 files are fresh
		deleteFile(new File(fileName));
		deleteFile(new File(fileName2));

		DataEngineContext deContext1 = newContext(DataEngineContext.MODE_GENERATION, fileName, fileName2);
		deContext1.setTmpdir(this.getTempDir());
		myGenDataEngine = DataEngine.newDataEngine(deContext1);

		myGenDataEngine.defineDataSource(this.dataSource);
		myGenDataEngine.defineDataSet(this.dataSet);

		Context context = Context.enter();
		scope = context.initStandardObjects();
		Context.exit();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#tearDown()
	 */
	@After
	public void rdTearDown() throws Exception {
		if (archiveWriter != null) {
			try {
				archiveWriter.finish();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (archiveReader != null) {
			try {
				archiveReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (myGenDataEngine != null) {
			myGenDataEngine.shutdown();
			myGenDataEngine = null;
		}
		if (myPreDataEngine != null) {
			myPreDataEngine.shutdown();
			myPreDataEngine = null;
		}
		if (myPreDataEngine2 != null) {
			myPreDataEngine2.shutdown();
			myPreDataEngine2 = null;
		}
		if (fileName != null) {
			deleteFile(new File(fileName));
		}

		if (fileName2 != null) {
			deleteFile(new File(fileName));
		}
	}

	/**
	 * @return
	 */
	protected boolean useFolderArchive() {
		return false;
	}

	/**
	 * @return folder for report document
	 */
	private String getOutputPath() {
		return this.getOutputFolder().getAbsolutePath() + File.separator;
	}

	/**
	 * @param type
	 * @param fileName
	 * @return
	 * @throws BirtException
	 */
	protected DataEngineContext newContext(int type, String fileName) throws BirtException {
		return newContext(type, fileName, null);
	}

	/**
	 * @param type
	 * @return context
	 * @throws BirtException
	 */
	protected DataEngineContext newContext(int type, String fileName, String fileName2) throws BirtException {
		boolean useFolder = useFolderArchive();
		switch (type) {
		case DataEngineContext.MODE_GENERATION: {
			try {
				if (useFolder == true)
					archiveWriter = new FolderArchiveWriter(fileName);
				else
					archiveWriter = new FileArchiveWriter(fileName);
				archiveWriter.initialize();
			} catch (IOException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
			DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.MODE_GENERATION, null, null,
					archiveWriter);
			context.setTmpdir(this.getTempDir());
			return context;
		}
		case DataEngineContext.MODE_PRESENTATION: {
			try {
				if (useFolder == true)
					archiveReader = new FolderArchiveReader(fileName, true);
				else
					archiveReader = new FileArchiveReader(fileName);
				archiveReader.open();
			} catch (IOException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
			DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.MODE_PRESENTATION, null,
					archiveReader, null);
			context.setTmpdir(this.getTempDir());
			return context;
		}
		case DataEngineContext.MODE_UPDATE: {
			try {
				if (useFolder == true)
					archiveReader = new FolderArchiveReader(fileName);
				else
					archiveReader = new FileArchiveReader(fileName);
				archiveReader.open();

				if (useFolder == true)
					archiveWriter = new FolderArchiveWriter(fileName2);
				else
					archiveWriter = new FileArchiveWriter(fileName2);

				archiveWriter.initialize();
			} catch (IOException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
			DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.MODE_UPDATE, null,
					archiveReader, archiveWriter);
			context.setTmpdir(this.getTempDir());
			return context;
		}
		default:
			throw new IllegalArgumentException("" + type);
		}
	}

	/**
	 * @throws DataException
	 */
	protected void closeArchiveWriter() throws DataException {
		if (archiveWriter != null)
			try {
				archiveWriter.finish();
				archiveWriter = null;
			} catch (IOException e) {
				throw new DataException("error", e);
			}
	}

	/**
	 * @throws DataException
	 */
	protected void closeArchiveReader() throws DataException {
		if (archiveReader != null)
			try {
				archiveReader.close();
				archiveReader = null;
			} catch (Exception e) {
				throw new DataException("error", e);
			}
	}

}
