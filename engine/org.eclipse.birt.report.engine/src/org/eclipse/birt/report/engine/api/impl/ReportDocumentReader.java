/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.script.ParameterAttribute;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IBookmarkInfo;
import org.eclipse.birt.report.engine.api.IReportDocumentHelper;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.impl.LinkedObjectManager.LinkedEntry;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.BookmarkContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.executor.ApplicationClassLoader;
import org.eclipse.birt.report.engine.executor.ExecutorManager;
import org.eclipse.birt.report.engine.extension.engine.IReportDocumentExtension;
import org.eclipse.birt.report.engine.extension.engine.IReportEngineExtension;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.document.IPageHintReader;
import org.eclipse.birt.report.engine.internal.document.PageHintReader;
import org.eclipse.birt.report.engine.internal.document.v3.ReportContentReaderV3;
import org.eclipse.birt.report.engine.internal.document.v4.InstanceIDComparator;
import org.eclipse.birt.report.engine.internal.executor.doc.Fragment;
import org.eclipse.birt.report.engine.internal.index.DocumentIndexReader;
import org.eclipse.birt.report.engine.internal.index.IDocumentIndexReader;
import org.eclipse.birt.report.engine.ir.EngineIRReader;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.PageSection;
import org.eclipse.birt.report.engine.toc.ITOCReader;
import org.eclipse.birt.report.engine.toc.ITreeNode;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.engine.toc.TOCReader;
import org.eclipse.birt.report.engine.toc.TOCView;
import org.eclipse.birt.report.engine.toc.TreeNode;
import org.eclipse.birt.report.engine.toc.document.TOCReaderV0;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * core stream format TAG DOCUMENT VERSION
 * 
 * TAG CORE_VERSION_0 DOCUMENT_VERSION TAG
 * 
 * CORE_VERSION_1 DOCUMENT_VERSION MAP
 */
public class ReportDocumentReader implements IReportletDocument, ReportDocumentConstants, IReportDocumentHelper {

	static private Logger logger = Logger.getLogger(ReportDocumentReader.class.getName());

	private ReportEngine engine;
	private IDocArchiveReader archive;
	private Map moduleOptions;
	/*
	 * version, parameters, globalVariables are loaded from core stream.
	 */
	private String coreVersion;
	private Map properties = new HashMap();
	private HashMap parameters;
	private HashMap globalVariables;
	/**
	 * used to load the page hints
	 */
	private IPageHintReader pageHintReader;
	private ITOCReader tocReader;
	private IDocumentIndexReader indexReader;
	/** Design name */
	private String systemId;

	private int checkpoint = CHECKPOINT_INIT;

	private long pageCount;

	private boolean sharedArchive;

	private ApplicationClassLoader applicationClassLoader;

	private ReportRunnable preparedRunnable = null;

	private ReportRunnable reportRunnable = null;

	private boolean coreStreamLoaded = false;

	private LinkedEntry<ReportDocumentReader> engineCacheEntry;

	private byte[] bodyData;
	private TreeNode cachedTreeV0;

	public ReportDocumentReader(ReportEngine engine, IDocArchiveReader archive, boolean sharedArchive)
			throws EngineException {
		this(null, engine, archive, sharedArchive);
	}

	public ReportDocumentReader(ReportEngine engine, IDocArchiveReader archive) throws EngineException {
		this(null, engine, archive, false);
	}

	public ReportDocumentReader(String systemId, ReportEngine engine, IDocArchiveReader archive, Map options)
			throws EngineException {
		this(systemId, engine, archive, false, options);
	}

	public ReportDocumentReader(String systemId, ReportEngine engine, IDocArchiveReader archive, boolean sharedArchive)
			throws EngineException {
		this(systemId, engine, archive, sharedArchive, null);
	}

	public ReportDocumentReader(String systemId, ReportEngine engine, IDocArchiveReader archive, boolean sharedArchive,
			Map options) throws EngineException {
		this.engine = engine;
		this.archive = archive;
		this.systemId = systemId;
		this.sharedArchive = sharedArchive;
		this.moduleOptions = new HashMap();
		this.moduleOptions.put(ModuleOption.PARSER_SEMANTIC_CHECK_KEY, Boolean.FALSE);
		if (options != null) {
			this.moduleOptions.putAll(options);
		}

		loadCoreStreamHeader();
	}

	public IDocArchiveReader getArchive() {
		return this.archive;
	}

	public String getVersion() {
		return (String) properties.get(BIRT_ENGINE_VERSION_KEY);
	}

	public String getProperty(String key) {
		return (String) properties.get(key);
	}

	static public class ReportDocumentCoreInfo {

		public int checkpoint;
		public long pageCount;
		public String systemId;
		public HashMap globalVariables;
		public HashMap parameters;
		public ITOCReader tocReader;
		public IDocumentIndexReader indexReader;
	}

	private void loadCoreStreamHeader() throws EngineException {
		try {
			Object lock = archive.lock(CORE_STREAM);
			try {
				synchronized (lock) {
					RAInputStream in = archive.getStream(CORE_STREAM);
					try {
						DataInputStream di = new DataInputStream(in);

						// check the document version and core stream version
						checkVersion(di);

						// load the stream header, check point, system id and
						// page count
						loadCoreStreamHeader(di);
					} finally {
						in.close();
					}
				}
			} finally {
				archive.unlock(lock);
			}
		} catch (IOException ee) {
			close();
			throw new EngineException(MessageConstants.REPORT_DOCUMENT_OPEN_ERROR, ee);
		}
	}

	private void loadCoreStreamHeader(DataInputStream di) throws IOException {
		// load info into a document info object
		ReportDocumentCoreInfo documentInfo = new ReportDocumentCoreInfo();

		loadCoreStreamHeader(di, documentInfo);

		if (checkpoint != documentInfo.checkpoint) {
			checkpoint = documentInfo.checkpoint;
			pageCount = documentInfo.pageCount;
			if (systemId == null) {
				systemId = documentInfo.systemId;
			}
		}
	}

	private void loadCoreStreamHeader(DataInputStream di, ReportDocumentCoreInfo documentInfo) throws IOException {
		if (CORE_VERSION_UNKNOWN.equals(coreVersion)) {
			loadCoreStreamHeaderUnknown(di, documentInfo);
		} else if (CORE_VERSION_0.equals(coreVersion) || CORE_VERSION_1.equals(coreVersion)
				|| CORE_VERSION_2.equals(coreVersion)) {
			loadCoreStreamHeaderV0(di, documentInfo);
		} else {
			throw new IOException(MessageConstants.UNSUPPORTED_CORE_STREAM_VERSION + coreVersion);
		}
	}

	/**
	 * read the core stream header which has version UNKNOWN and V0.
	 * 
	 * The core stream header contains 3 field:
	 * <li>check point</li>
	 * <li>page count</li>
	 * <li>system id</li>
	 * 
	 * @param coreStream   the core stream. The version has been loaded from the
	 *                     stream.
	 * @param documentInfo the loaded information is saved into this object.
	 * @throws IOException
	 */
	private void loadCoreStreamHeaderUnknown(DataInputStream coreStream, ReportDocumentCoreInfo documentInfo)
			throws IOException {
		documentInfo.checkpoint = CHECKPOINT_INIT;
		documentInfo.pageCount = PAGECOUNT_INIT;
		if (!archive.exists(CHECKPOINT_STREAM)) {
			// no check point stream, old version, return -1
			documentInfo.checkpoint = CHECKPOINT_END;
			initializePageHintReader();
			if (pageHintReader != null) {
				documentInfo.pageCount = pageHintReader.getTotalPage();
			}
		} else {
			RAInputStream in = archive.getStream(CHECKPOINT_STREAM);
			try {
				DataInputStream di = new DataInputStream(in);
				documentInfo.checkpoint = IOUtil.readInt(di);
				documentInfo.pageCount = IOUtil.readLong(di);
			} finally {
				if (in != null) {
					in.close();
				}
			}
		}
		// load the report design name
		documentInfo.systemId = IOUtil.readString(coreStream);
	}

	/**
	 * load the core stream header for stream with version V1 and V2
	 * 
	 * the core stream header contains 3 field:
	 * 
	 * <li>check point</li>
	 * <li>page count</li>
	 * <li>system id</li>
	 * 
	 * @param di           core stream, the version has been loaded from this
	 *                     stream.
	 * @param documentInfo the header is saved into this object.
	 * @throws IOException
	 */
	private void loadCoreStreamHeaderV0(DataInputStream di, ReportDocumentCoreInfo documentInfo) throws IOException {
		documentInfo.checkpoint = CHECKPOINT_INIT;
		documentInfo.pageCount = PAGECOUNT_INIT;

		documentInfo.checkpoint = IOUtil.readInt(di);
		documentInfo.pageCount = IOUtil.readLong(di);

		// load the report design name
		documentInfo.systemId = IOUtil.readString(di);
	}

	private void loadCoreStreamLazily() {
		if (!coreStreamLoaded) {
			synchronized (this) {
				if (!coreStreamLoaded) {
					try {
						loadCoreStream();
					} catch (IOException ee) {
						logger.log(Level.SEVERE, "Failed to load core stream", ee); //$NON-NLS-1$
					}
				}
			}
		}
	}

	synchronized public void refresh() {
		try {
			loadCoreStream();
		} catch (IOException ee) {
			logger.log(Level.SEVERE, "Failed to refresh", ee); //$NON-NLS-1$
		}
	}

	protected void loadCoreStream() throws IOException {
		Object lock = archive.lock(CORE_STREAM);
		try {
			synchronized (lock) {
				RAInputStream in = archive.getStream(CORE_STREAM);
				try {
					DataInputStream di = new DataInputStream(in);

					// check the document version and core stream version
					checkVersion(di);

					loadCoreStream(di);

					coreStreamLoaded = true;
				} finally {
					in.close();
				}
			}
		} finally {
			archive.unlock(lock);
		}
	}

	private void loadCoreStream(DataInputStream di) throws IOException {
		// load info into a document info object
		ReportDocumentCoreInfo documentInfo = new ReportDocumentCoreInfo();

		loadCoreStreamHeader(di, documentInfo);
		loadCoreStreamBody(di, documentInfo);

		// save the document info into the object.
		checkpoint = documentInfo.checkpoint;
		pageCount = documentInfo.pageCount;
		globalVariables = documentInfo.globalVariables;
		parameters = documentInfo.parameters;
		if (checkpoint == CHECKPOINT_END) {
			if (indexReader == null) {
				indexReader = documentInfo.indexReader;
			} else {
				if (documentInfo.indexReader != null) {
					documentInfo.indexReader.close();
				}
			}
			if (tocReader == null) {
				tocReader = documentInfo.tocReader;
				if (tocReader != null) {
					cachedTreeV0 = (TreeNode) tocReader.readTree();
				}
			} else {
				if (documentInfo.tocReader != null) {
					documentInfo.tocReader.close();
				}
			}
		}
	}

	private void loadCoreStreamBody(DataInputStream di, ReportDocumentCoreInfo documentInfo) throws IOException {
		if (CORE_VERSION_UNKNOWN.equals(coreVersion)) {
			loadCoreStreamBodyUnknown(di, documentInfo);
		} else if (CORE_VERSION_0.equals(coreVersion) || CORE_VERSION_1.equals(coreVersion)) {
			loadCoreStreamBodyV0(di, documentInfo);
		} else if (CORE_VERSION_2.equals(coreVersion)) {
			loadCoreStreamBodyV2(di, documentInfo);
		} else {
			throw new IOException("unsupported core stream version: " + coreVersion);
		}
	}

	protected void loadCoreStreamBodyUnknown(DataInputStream coreStream, ReportDocumentCoreInfo documentInfo)
			throws IOException {
		// load the report parameters
		loadCoreStreamBodyToBuffer(coreStream);
		if (documentInfo.checkpoint == CHECKPOINT_END) {
			documentInfo.indexReader = new DocumentIndexReader(IDocumentIndexReader.VERSION_0, archive);
		}
	}

	protected void loadCoreStreamBodyV0(DataInputStream coreStream, ReportDocumentCoreInfo documentInfo)
			throws IOException {
		Map originalParameters = IOUtil.readMap(coreStream);
		documentInfo.parameters = convertToCompatibleParameter(originalParameters);
		// load the persistence object
		documentInfo.globalVariables = (HashMap) IOUtil.readMap(coreStream);
		if (documentInfo.checkpoint == CHECKPOINT_END) {
			HashMap<String, Long> bookmarks = readMap(coreStream);
			documentInfo.tocReader = new TOCReader(coreStream, null);
			HashMap<String, Long> reportletsIndexById = readMap(coreStream);
			HashMap<String, Long> reportletsIndexByBookmark = readMap(coreStream);
			documentInfo.indexReader = new DocumentIndexReader(IDocumentIndexReader.VERSION_1,
					reportletsIndexByBookmark, reportletsIndexById, bookmarks);
		}
	}

	protected void loadCoreStreamBodyV2(DataInputStream coreStream, ReportDocumentCoreInfo documentInfo)
			throws IOException {
		loadCoreStreamBodyToBuffer(coreStream);
		if (documentInfo.checkpoint == CHECKPOINT_END) {
			documentInfo.indexReader = new DocumentIndexReader(IDocumentIndexReader.VERSION_2, archive);
		}
	}

	private void loadCoreStreamBodyToBuffer(DataInputStream stream) throws IOException {
		byte[] datas = new byte[1024];
		int length = -1;
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		while ((length = stream.read(datas)) > 0) {
			byteOut.write(datas, 0, length);
		}
		bodyData = byteOut.toByteArray();
	}

	private HashMap<String, Long> readMap(DataInputStream di) throws IOException {
		HashMap<String, Long> map = new HashMap<String, Long>();
		long count = IOUtil.readLong(di);
		for (long i = 0; i < count; i++) {
			String bookmark = IOUtil.readString(di);
			long pageNumber = IOUtil.readLong(di);
			map.put(bookmark, Long.valueOf(pageNumber));
		}
		return map;
	}

	private HashMap convertToCompatibleParameter(Map parameters) {
		if (parameters == null) {
			return null;
		}
		HashMap result = new HashMap();
		Iterator iterator = parameters.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			Object key = entry.getKey();
			Object valueObj = entry.getValue();
			ParameterAttribute paramAttr = null;
			if (valueObj instanceof ParameterAttribute) {
				paramAttr = (ParameterAttribute) valueObj;
			} else if (valueObj instanceof Object[]) {
				Object[] values = (Object[]) valueObj;
				if (values.length == 2) {
					Object value = values[0];
					if (values[1] == null || values[1] instanceof String) {
						paramAttr = new ParameterAttribute(value, (String) values[1]);
					} else if (values[1] instanceof String[]) {
						paramAttr = new ParameterAttribute((Object[]) value, (String[]) values[1]);
					}

				}
			}
			if (paramAttr == null) {
				paramAttr = new ParameterAttribute(valueObj, null);
			}
			result.put(key, paramAttr);
		}
		return result;
	}

	/**
	 * <pre>
	 *    		TAG		CORE_V	DOC_V	ENG_V	HIGHLIGHT	VERSION
	 * 2.0.0	x		NULL	1.0.0	NULL	FALSE		2.0.0
	 * 2.0.1	x		NULL	1.0.0	NULL	FALSE		2.0.0
	 * 2.0.2	x		NULL	1.0.0	NULL	FALSE		2.0.0
	 * 2.1.0	x		NULL	2.1.0	NULL	TRUE		2.1.0
	 * 2.1.1	x		NULL	2.1.0	NULL	TRUE		2.1.0
	 * 2.1.2	x		NULL	2.1.0	NULL	TRUE		2.1.0
	 * 2.1.3	x		v0		2.1.3	NULL	TRUE		2.1.3
	 * 2.2.0	x		v0		2.1.3	NULL	TRUE		2.1.3
	 * 2.2.1	x		v1		2.1.3	2.2.1	FALSE		2.2.1
	 * 2.2.2	x		v1		2.1.3	2.2.1	FALSE		2.2.1
	 * 2.3.0	x		v1		2.1.3	2.2.1	FALSE		2.2.1
	 * 2.3.1	x		v1		2.1.3	2.2.1	FALSE		2.2.1
	 * 2.3.2	x		v1		2.1.3	2.3.2	FALSE		2.3.2
	 * 2.5.0	x		v1		2.1.3	2.5.0	FALSE		2.5.0
	 * </pre>
	 */
	protected void checkVersion(DataInputStream di) throws IOException {
		String tag = IOUtil.readString(di);
		if (!REPORT_DOCUMENT_TAG.equals(tag)) {
			throw new IOException("unknown report document tag" + tag);//$NON-NLS-1$
		}
		String docVersion = IOUtil.readString(di);
		if (docVersion == null) {
			throw new IOException("invalid core stream format");//$NON-NLS-1$
		}

		if (!docVersion.startsWith(CORE_VERSION_PREFIX)) {
			// there is no core version, so set the core version to unknown.
			coreVersion = CORE_VERSION_UNKNOWN;
		} else if (CORE_VERSION_0.equals(docVersion) || CORE_VERSION_1.equals(docVersion)
				|| CORE_VERSION_2.equals(docVersion)) {
			coreVersion = docVersion;
			docVersion = IOUtil.readString(di);
			if (CORE_VERSION_1.equals(coreVersion) || CORE_VERSION_2.equals(coreVersion)) {
				properties = IOUtil.readMap(di);
			}
		} else {
			throw new IOException("unknown core stream version" + tag);//$NON-NLS-1$
		}

		String[] supportedVersions = new String[] { REPORT_DOCUMENT_VERSION_1_0_0, REPORT_DOCUMENT_VERSION_2_1_0,
				REPORT_DOCUMENT_VERSION_2_1_3 };
		boolean supportedVersion = false;
		for (int i = 0; i < supportedVersions.length; i++) {
			if (supportedVersions[i].equals(docVersion)) {
				supportedVersion = true;
				break;
			}
		}
		if (supportedVersion == false) {
			throw new IOException("unsupport report document version " + docVersion); //$NON-NLS-1$
		}

		// test if request extension are present
		String extensions = (String) properties.get(BIRT_ENGINE_EXTENSIONS);
		if (extensions != null && extensions.length() > 0) {
			String[] extIds = extensions.split(";");
			for (String extId : extIds) {
				IReportEngineExtension ext = engine.getEngineExtension(extId);
				if (ext == null) {
					throw new IOException("unsupported report extension:" + extId);
				}
			}
		}

		if (properties.get(BIRT_ENGINE_VERSION_KEY) == null) {
			if (REPORT_DOCUMENT_VERSION_1_0_0.equals(docVersion)) {
				properties.put(BIRT_ENGINE_VERSION_KEY, BIRT_ENGINE_VERSION_2_0_0);
			} else if (REPORT_DOCUMENT_VERSION_2_1_0.equals(docVersion)) {
				properties.put(BIRT_ENGINE_VERSION_KEY, BIRT_ENGINE_VERSION_2_1_0);
			} else if (REPORT_DOCUMENT_VERSION_2_1_3.equals(docVersion)) {
				properties.put(BIRT_ENGINE_VERSION_KEY, BIRT_ENGINE_VERSION_2_1_3);
			}
		}

		String version = getVersion();
		// FIXME: test if the version is later than BIRT_ENGINE_VERSION

		if (properties.get(DATA_EXTRACTION_TASK_VERSION_KEY) == null) {
			// check the data extraction task version
			if (BIRT_ENGINE_VERSION_2_0_0.equals(docVersion) || BIRT_ENGINE_VERSION_2_1_0.equals(version)) {
				properties.put(DATA_EXTRACTION_TASK_VERSION_KEY, DATA_EXTRACTION_TASK_VERSION_0);
			} else {
				properties.put(DATA_EXTRACTION_TASK_VERSION_KEY, DATA_EXTRACTION_TASK_VERSION_1);
			}
		}
		// assign the page-hint version
		if (properties.get(PAGE_HINT_VERSION_KEY) == null) {
			properties.put(PAGE_HINT_VERSION_KEY, PAGE_HINT_VERSION_2);
		}
	}

	public void close() {
		if (tocReader != null) {
			try {
				tocReader.close();
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Failed to close the tocReader", ex); //$NON-NLS-1$
			}
			tocReader = null;
		}

		if (indexReader != null) {
			try {
				indexReader.close();
			} catch (IOException ex) {
				logger.log(Level.WARNING, "failed to close the index reader", ex);
			}
			indexReader = null;
		}

		if (pageHintReader != null) {
			pageHintReader.close();
			pageHintReader = null;
		}
		if (archive != null) {
			if (!sharedArchive) {
				try {
					archive.close();
				} catch (IOException ex) {
					logger.log(Level.SEVERE, "Failed to close the archive", ex); //$NON-NLS-1$
				}
			}
			archive = null;
		}

		if (extensions != null) {
			for (Map.Entry<String, IReportDocumentExtension> entry : extensions.entrySet()) {
				String name = entry.getKey();
				IReportDocumentExtension extension = entry.getValue();
				if (extension != null) {
					try {
						extension.close();
					} catch (EngineException ex) {
						logger.log(Level.SEVERE, "Failed to close the extension " + name, ex);
					}
				}
			}

			extensions.clear();
			extensions = null;
		}
		if (applicationClassLoader != null) {
			applicationClassLoader.close();
		}
		if (engineCacheEntry != null) {
			LinkedObjectManager<ReportDocumentReader> manager = engineCacheEntry.getManager();
			synchronized (manager) {
				manager.remove(engineCacheEntry);
			}
		}
		this.bodyData = null;
		this.engine = null;
		this.cachedTreeV0 = null;
		if (globalVariables != null) {
			globalVariables.clear();
			globalVariables = null;
		}
		if (moduleOptions != null) {
			moduleOptions.clear();
			moduleOptions = null;
		}
		this.preparedRunnable = null;
		this.reportRunnable = null;
		if (parameters != null) {
			parameters.clear();
			parameters = null;
		}
	}

	public InputStream getDesignStream() {
		try {
			if (archive.exists(ORIGINAL_DESIGN_STREAM)) {
				return archive.getStream(ORIGINAL_DESIGN_STREAM);
			}
			return archive.getStream(DESIGN_STREAM);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Failed to open the design!", ex); //$NON-NLS-1$
			return null;
		}
	}

	private ReportRunnable loadReportRunnable(String systemId, String streamName) {

		try {
			if (archive.exists(streamName)) {
				InputStream stream = archive.getStream(streamName);
				try {
					String name = systemId;
					if (name == null) {
						name = archive.getName();
					}
					ReportRunnable reportRunnable = (ReportRunnable) engine.openReportDesign(name, stream,
							moduleOptions);
					return reportRunnable;
				} finally {
					stream.close();
				}
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Failed to open the design!", ex); //$NON-NLS-1$
		}
		return null;
	}

	public synchronized IReportRunnable getReportRunnable() {
		if (reportRunnable == null) {
			reportRunnable = loadReportRunnable(systemId, ORIGINAL_DESIGN_STREAM);
			if (reportRunnable != null) {
				reportRunnable.setPrepared(false);
			} else {
				reportRunnable = getOnPreparedRunnable();
			}
		}
		if (reportRunnable != null) {
			return reportRunnable.cloneRunnable();
		}
		return null;
	}

	public synchronized IReportRunnable getPreparedRunnable() {
		ReportRunnable runnable = getOnPreparedRunnable();
		if (runnable != null) {
			return runnable.cloneRunnable();
		}
		return null;
	}

	public synchronized IReportRunnable getDocumentRunnable() {
		return getOnPreparedRunnable();
	}

	public ReportRunnable getOnPreparedRunnable() {
		if (preparedRunnable == null) {
			preparedRunnable = loadReportRunnable(systemId, DESIGN_STREAM);
			if (preparedRunnable != null) {
				preparedRunnable.setPrepared(true);
			}
		}
		return preparedRunnable;
	}

	/**
	 * @deprecated
	 */
	public Map getParameterValues() {
		loadCoreStreamLazily();
		loadVariableLazily();
		Map result = new HashMap();
		if (parameters != null) {
			Iterator iterator = parameters.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String name = (String) entry.getKey();
				ParameterAttribute value = (ParameterAttribute) entry.getValue();
				result.put(name, value.getValue());
			}
		}
		return result;
	}

	public Map getParameterValues(ClassLoader loader) {
		Map result = new HashMap();
		try {
			Map parameters = loadParameters(loader);
			if (parameters != null) {
				Iterator iterator = parameters.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry entry = (Map.Entry) iterator.next();
					String name = (String) entry.getKey();
					ParameterAttribute value = (ParameterAttribute) entry.getValue();
					result.put(name, value.getValue());
				}
			}
		} catch (EngineException ex) {
			logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
		return result;
	}

	protected void loadVariableLazily() {
		if (globalVariables == null) {
			assert (bodyData != null);

			ClassLoader loader = getClassLoader();
			DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(bodyData));
			try {
				parameters = (HashMap) IOUtil.readMap(dataStream, loader);
				parameters = (HashMap) convertToCompatibleParameter(parameters);
				globalVariables = (HashMap) IOUtil.readMap(dataStream, loader);
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Failed to load parameters and variables in the core stream", ex); //$NON-NLS-1$
			}
		}
	}

	public Map<String, ParameterAttribute> loadParameters(ClassLoader loader) throws EngineException {
		loadCoreStreamLazily();
		if (bodyData == null) {
			return parameters;
		}
		try {
			DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(bodyData));
			;
			Map parameters = IOUtil.readMap(dataStream, loader);
			return convertToCompatibleParameter(parameters);
		} catch (IOException ee) {
			throw new EngineException("Failed to load parameters in the core stream", ee); //$NON-NLS-1$
		}
	}

	public Map loadVariables(ClassLoader loader) throws EngineException {
		loadCoreStreamLazily();
		if (bodyData == null) {
			return globalVariables;
		}
		try {
			DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(bodyData));
			;
			// skip the parameters
			IOUtil.readMap(dataStream, loader);
			// load the persistence object
			return IOUtil.readMap(dataStream, loader);
		} catch (IOException ee) {
			throw new EngineException("Failed to load variables in the core stream", ee); //$NON-NLS-1$
		}
	}

	public Map getParameterDisplayTexts() {
		loadCoreStreamLazily();
		loadVariableLazily();
		Map result = new HashMap();
		if (parameters != null) {
			Iterator iterator = parameters.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String name = (String) entry.getKey();
				ParameterAttribute value = (ParameterAttribute) entry.getValue();
				result.put(name, value.getDisplayText());
			}
		}
		return result;
	}

	public long getPageCount() {
		return pageCount;
	}

	public IPageHint getPageHint(long pageNumber) {
		initializePageHintReader();
		if (pageHintReader != null) {
			try {
				return pageHintReader.getPageHint(pageNumber);
			} catch (IOException ex) {
				logger.log(Level.WARNING, "Failed to load page hint " + pageNumber, ex);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#getPageNumber(java
	 * .lang.String)
	 */
	public long getPageNumber(String bookmark) {
		if (!isComplete()) {
			return -1;
		}

		loadCoreStreamLazily();
		if (indexReader != null) {
			try {
				return indexReader.getPageOfBookmark(bookmark);
			} catch (IOException ex) {
				logger.log(Level.WARNING, "failed to load the page number of bookmark:" + bookmark, ex);
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#getBookmarks()
	 */
	public List<String> getBookmarks() {
		if (!isComplete()) {
			return null;
		}

		loadCoreStreamLazily();
		if (indexReader != null) {
			try {
				return indexReader.getBookmarks();
			} catch (IOException ex) {
				logger.log(Level.WARNING, "failed to load the bookmark list", ex);
			}
		}
		return null;
	}

	public List<BookmarkContent> getBookmarkContents() {
		if (!isComplete()) {
			return null;
		}

		loadCoreStreamLazily();
		if (indexReader != null) {
			try {
				return indexReader.getBookmarkContents();
			} catch (IOException ex) {
				logger.log(Level.WARNING, "failed to load the bookmark info list", ex);
			}
		}
		return null;
	}

	public List<IBookmarkInfo> getBookmarkInfos(Locale locale) throws EngineException {
		if (!isComplete()) {
			return null;
		}
		ArrayList<IBookmarkInfo> results = new ArrayList<IBookmarkInfo>();

		loadCoreStreamLazily();
		if (indexReader != null) {
			try {
				List<BookmarkContent> bookmarks = indexReader.getBookmarkContents();
				if (bookmarks == null) {
					return null;
				}
				ReportDesignHandle report = this.getReportDesign();

				for (BookmarkContent bookmark : bookmarks) {
					String bookmarkString = bookmark.getBookmark();
					if (bookmarkString.startsWith(ExecutorManager.BOOKMARK_PREFIX)
							|| bookmarkString.indexOf(TOCBuilder.TOC_PREFIX) != -1) {
						continue;
					}
					long designId = bookmark.getElementId();
					DesignElementHandle handle = report.getElementByID(designId);
					if (handle == null)
						continue;
					String elementType = handle.getDefn().getName();
					String displayName = null;
					if (handle instanceof ReportItemHandle) {
						displayName = ((ReportItemHandle) handle).getBookmarkDisplayName();
					}
					if (locale != null) {
						if (handle instanceof ReportElementHandle) {
							ReportElementHandle elementHandle = (ReportElementHandle) handle;
							displayName = ModuleUtil.getExternalizedValue(elementHandle, bookmarkString, displayName,
									ULocale.forLocale(locale));

						}
					}
					results.add(new BookmarkInfo(bookmarkString, displayName, elementType));
				}
			} catch (IOException ex) {
				throw new EngineException(MessageConstants.BOOKMARK_FETCHING_EXCEPTION, ex);
			}
		}
		return results;
	}

	/**
	 * @param bookmark the bookmark that a page number is to be retrieved upon
	 * @return the page number that the bookmark appears
	 */
	public long getBookmark(String bookmark) {
		if (!isComplete()) {
			return -1;
		}

		loadCoreStreamLazily();
		if (indexReader != null) {
			try {
				return indexReader.getOffsetOfBookmark(bookmark);
			} catch (IOException ex) {
				logger.log(Level.WARNING, "failed to load the offset of bookmark:" + bookmark, ex);
			}
		}
		return -1;
	}

	/**
	 * @deprecated
	 */
	public ITOCTree getTOCTree(String format, ULocale locale) {
		return getTOCTree(format, locale, TimeZone.getDefault());
	}

	/**
	 * @deprecated
	 */
	public ITOCTree getTOCTree(String format, ULocale locale, TimeZone timeZone) {
		try {
			ITreeNode root = getTOCTree();
			if (root != null) {
				ReportDesignHandle report = ((ReportRunnable) getOnPreparedRunnable()).getReport();
				return new TOCView(root, report, locale, timeZone, format);
			}
		} catch (EngineException ex) {
			logger.log(Level.WARNING, ex.getMessage(), ex);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#findTOC(java.lang.
	 * String)
	 */
	/**
	 * @deprecated
	 */
	public TOCNode findTOC(String tocNodeId) {
		ITOCTree tree = getTOCTree("viewer", ULocale.getDefault());
		if (tree != null) {
			return tree.findTOC(tocNodeId);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#findTOCByName(java
	 * .lang.String)
	 */
	/**
	 * @deprecated
	 */
	public List findTOCByName(String tocName) {
		ITOCTree tree = getTOCTree("viewer", ULocale.getDefault());
		if (tree != null) {
			return tree.findTOCByValue(tocName);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#getChildren(java.lang
	 * .String)
	 */
	/**
	 * @deprecated
	 */
	public List getChildren(String tocNodeId) {
		TOCNode node = findTOC(tocNodeId);
		if (node != null) {
			return node.getChildren();
		}
		return null;
	}

	private void initializePageHintReader() {
		if (pageHintReader != null) {
			return;
		}
		synchronized (this) {
			if (pageHintReader != null) {
				return;
			}
			try {
				pageHintReader = new PageHintReader(this);
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "can not open the page hint stream", ex);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#getName()
	 */
	public String getName() {
		return archive.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#getGlobalVariables()
	 */
	/**
	 * @deprecated
	 */
	public Map getGlobalVariables(String option) {
		loadCoreStreamLazily();
		loadVariableLazily();
		return globalVariables;
	}

	public long getPageNumber(InstanceID iid) {
		if (!isComplete()) {
			return -1;
		}
		initializePageHintReader();
		if (pageHintReader == null) {
			return -1;
		}

		int version = pageHintReader.getVersion();

		try {
			if (version == IPageHintReader.VERSION_0) {
				long offset = getInstanceOffset(iid);
				if (offset == -1) {
					return -1;
				}
				long totalPage = pageHintReader.getTotalPage();
				for (long pageNumber = 1; pageNumber <= totalPage; pageNumber++) {
					IPageHint hint = pageHintReader.getPageHint(pageNumber);
					PageSection section = hint.getSection(0);

					if (offset >= section.startOffset) {
						return pageNumber;
					}
				}
			}

			else if (version == IPageHintReader.VERSION_1) {
				long offset = getInstanceOffset(iid);
				if (offset == -1) {
					return -1;
				}
				long totalPage = pageHintReader.getTotalPage();
				for (long pageNumber = 1; pageNumber <= totalPage; pageNumber++) {
					IPageHint hint = pageHintReader.getPageHint(pageNumber);
					int sectionCount = hint.getSectionCount();
					for (int i = 0; i < sectionCount; i++) {
						PageSection section = hint.getSection(i);

						if (section.startOffset <= offset && offset <= section.endOffset) {
							return pageNumber;
						}
					}
				}
			} else if (version == IPageHintReader.VERSION_2 || version == IPageHintReader.VERSION_3
					|| version == IPageHintReader.VERSION_4 || version == IPageHintReader.VERSION_5
					|| version == IPageHintReader.VERSION_6) {
				long totalPage = pageHintReader.getTotalPage();
				for (long pageNumber = 1; pageNumber <= totalPage; pageNumber++) {
					IPageHint hint = pageHintReader.getPageHint(pageNumber);
					int sectionCount = hint.getSectionCount();
					Fragment fragment = new Fragment(new InstanceIDComparator());
					for (int i = 0; i < sectionCount; i++) {
						PageSection section = hint.getSection(i);
						fragment.addSection(section.starts, section.ends);
					}
					fragment.build();
					if (fragment.inFragment(iid)) {
						return pageNumber;
					}
				}
			}
		} catch (IOException ex) {

		}
		return -1;
	}

	public long getInstanceOffset(InstanceID iid) {
		if (!isComplete()) {
			return -1l;
		}
		if (iid == null) {
			return -1l;
		}
		loadCoreStreamLazily();

		if (indexReader != null) {
			try {
				long offset = indexReader.getOffsetOfInstance(iid.toUniqueString());
				if (offset == -1) {
					offset = indexReader.getOffsetOfInstance(iid.toString());
				}
				return offset;
			} catch (IOException ex) {
				logger.log(Level.WARNING, "failed to get the offset of instance:" + iid.toUniqueString(), ex);
			}
		}
		return -1L;
	}

	public long getBookmarkOffset(String bookmark) {
		if (!isComplete()) {
			return -1;
		}
		if (bookmark == null) {
			return -1l;
		}

		loadCoreStreamLazily();
		if (indexReader != null) {
			try {
				return indexReader.getOffsetOfBookmark(bookmark);
			} catch (IOException ex) {
				logger.log(Level.WARNING, "failed to get the offset of bookmark:" + bookmark, ex);
			}
		}
		return -1L;
	}

	/**
	 * @deprecated
	 */
	public ClassLoader getClassLoader() {
		if (applicationClassLoader != null) {
			return applicationClassLoader;
		}
		synchronized (this) {
			if (applicationClassLoader == null) {
				applicationClassLoader = AccessController.doPrivileged(new PrivilegedAction<ApplicationClassLoader>() {

					public ApplicationClassLoader run() {
						return new ApplicationClassLoader(engine, getOnPreparedRunnable(),
								engine.getConfig().getAppContext());
					}
				});
			}
		}
		return applicationClassLoader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#isComplete()
	 */
	public boolean isComplete() {
		return checkpoint == CHECKPOINT_END;
	}

	public ReportDesignHandle getReportDesign() {
		IReportRunnable reportRunnable = getReportRunnable();
		if (reportRunnable != null) {
			return (ReportDesignHandle) reportRunnable.getDesignHandle();
		}
		return null;
	}

	public Report getReportIR(ReportDesignHandle designHandle) {
		InputStream stream = null;
		try {
			stream = archive.getStream(DESIGN_IR_STREAM);
			EngineIRReader reader = new EngineIRReader();
			Report reportIR = reader.read(stream);
			reportIR.setVersion(getVersion());
			reader.link(reportIR, designHandle);
			return reportIR;
		} catch (IOException ioex) {
			// an error occurs in reading the engine ir
			logger.log(Level.FINE, "Failed to load the engine IR", ioex);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ignored) {
				}
				stream = null;
			}
		}
		return null;
	}

	private InstanceID loadInstanceID(ReportContentReaderV3 reader, long offset) throws IOException {
		IContent content = reader.readContent(offset);
		if (content != null) {
			InstanceID iid = content.getInstanceID();
			DocumentExtension ext = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
			long parentOffset = ext.getParent();
			if (parentOffset != -1) {
				InstanceID pid = loadInstanceID(reader, parentOffset);
				if (pid != null) {
					return new InstanceID(pid, iid);
				}
			}
			return iid;
		}
		return null;
	}

	public InstanceID getBookmarkInstance(String bookmark) {
		if (bookmark == null || bookmark.length() == 0) {
			return null;
		}

		long offset = getBookmarkOffset(bookmark);

		if (offset < 0)
			return null;

		try {
			RAInputStream is = archive.getStream(CONTENT_STREAM);
			try {
				ReportContentReaderV3 reader = new ReportContentReaderV3(new ReportContent(), is,
						applicationClassLoader);
				try {
					return loadInstanceID(reader, offset);
				} finally {
					reader.close();
				}
			} finally {
				is.close();
			}
		} catch (IOException ioe) {
			logger.log(Level.FINE, "Failed to get the instance ID of the bookmark: " + bookmark, ioe);
		}
		return null;
	}

	private Boolean isReportlet;
	private String reportletBookmark;
	private String reportletInstanceID;

	public boolean isReporltetDocument() throws IOException {
		return loadReportletStream();
	}

	public String getReportletBookmark() throws IOException {
		if (loadReportletStream()) {
			return reportletBookmark;
		}
		return null;
	}

	public InstanceID getReportletInstanceID() throws IOException {
		if (loadReportletStream()) {
			return InstanceID.parse(reportletInstanceID);
		}
		return null;
	}

	private boolean loadReportletStream() throws IOException {
		if (isReportlet == null) {
			if (archive.exists(REPORTLET_DOCUMENT_STREAM)) {
				RAInputStream in = archive.getInputStream(REPORTLET_DOCUMENT_STREAM);
				try {
					int version = in.readInt();
					if (version != REPORTLET_DOCUMENT_VERSION_0) {
						throw new IOException("unsupported reportlet document " + version);
					}
					int size = in.readInt();
					byte[] bytes = new byte[size];
					in.readFully(bytes, 0, size);
					DataInputStream s = new DataInputStream(new ByteArrayInputStream(bytes));
					reportletBookmark = IOUtil.readString(s);
					reportletInstanceID = IOUtil.readString(s);
					isReportlet = Boolean.TRUE;
				} finally {
					in.close();
				}
			} else {
				isReportlet = Boolean.FALSE;
			}
		}
		return isReportlet.booleanValue();
	}

	HashMap<String, IReportDocumentExtension> extensions = new HashMap<String, IReportDocumentExtension>();

	synchronized public IReportDocumentExtension getDocumentExtension(String name) throws EngineException {
		IReportDocumentExtension extension = extensions.get(name);
		if (extension == null) {
			IReportEngineExtension engineExtension = this.engine.getEngineExtension(name);
			if (engineExtension != null) {
				extension = engineExtension.createDocumentExtension(this);
				extensions.put(name, extension);
			}
		}
		return extension;
	}

	/**
	 * @deprecated
	 */
	synchronized public ITreeNode getTOCTree() throws EngineException {
		if (!isComplete()) {
			return null;
		}
		loadCoreStreamLazily();
		try {
			if (tocReader == null) {
				tocReader = new TOCReader(archive, getClassLoader());
			}
			return tocReader.readTree();
		} catch (IOException ex) {
			throw new EngineException(MessageConstants.FAILED_TO_LOAD_TOC_TREE_EXCEPTION, ex);
		}
	}

	public ITOCReader getTOCReader(ClassLoader loader) throws EngineException {
		loadCoreStreamLazily();
		if (cachedTreeV0 != null) {
			// it is safe to reuse the tocReaderV0 as the close is empty.
			return new TOCReaderV0(cachedTreeV0);
		}
		try {
			return new TOCReader(archive, loader);
		} catch (IOException ex) {
			throw new EngineException(MessageConstants.FAILED_TO_LOAD_TOC_TREE_EXCEPTION, ex);
		}
	}

	public void setEngineCacheEntry(LinkedEntry<ReportDocumentReader> entry) {
		this.engineCacheEntry = entry;
	}

	public String getSystemId() {
		return systemId;
	}

	public List<String> getDocumentErrors() {
		RunStatusReader statusReader = new RunStatusReader(this);
		try {
			return statusReader.getGenerationErrors();
		} finally {
			statusReader.close();
		}
	}
}
