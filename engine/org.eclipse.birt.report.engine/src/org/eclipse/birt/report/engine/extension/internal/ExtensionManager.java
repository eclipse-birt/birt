/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.extension.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.DataExtractionFormatInfo;
import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ExecutorManager;
import org.eclipse.birt.report.engine.executor.ExtendedGenerateExecutor;
import org.eclipse.birt.report.engine.extension.IDataExtractionExtension;
import org.eclipse.birt.report.engine.extension.IExtendedItemFactory;
import org.eclipse.birt.report.engine.extension.IReportEventHandler;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemGeneration;
import org.eclipse.birt.report.engine.extension.IReportItemPreparation;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IReportItemQuery;

/**
 * Manages engine extensions. Currently, engine supports 4 types of extensions:
 * emitters, extended items Query, Generation, Presentation time extensions
 */
public class ExtensionManager {

	protected static Logger logger = Logger.getLogger(ExtensionManager.class.getName());

	public final static String EXTENSION_POINT_EMITTERS = "org.eclipse.birt.report.engine.emitters"; //$NON-NLS-1$
	public final static String EXTENSION_POINT_GENERATION = "org.eclipse.birt.report.engine.reportitemGeneration"; //$NON-NLS-1$
	public final static String EXTENSION_POINT_PRESENTATION = "org.eclipse.birt.report.engine.reportitemPresentation"; //$NON-NLS-1$
	public final static String EXTENSION_POINT_QUERY = "org.eclipse.birt.report.engine.reportitemQuery"; //$NON-NLS-1$
	public final static String EXTENSION_POINT_EVENTHANDLER = "org.eclipse.birt.report.engine.reportEventHandler"; //$NON-NLS-1$
	public final static String EXTENSION_POINT_PREPARATION = "org.eclipse.birt.report.engine.reportItemPreparation"; //$NON-NLS-1$
	public final static String EXTENSION_POINT_DATAEXTRACTION = "org.eclipse.birt.report.engine.dataExtraction"; //$NON-NLS-1$
	public final static String EXTENSION_POINT_EXTENDED_ITEM_FACTORY = "org.eclipse.birt.report.engine.extendedItemFactory"; //$NON-NLS-1$
	public final static String emitterFormatPattern = "[$_a-zA-Z][.$_a-zA-Z0-9]*";

	/**
	 * the singleton isntance
	 */
	static protected ExtensionManager sm_instance;

	/**
	 * stores references to all the generation extensions
	 */
	protected HashMap generationExtensions = new HashMap();

	/**
	 * stores references to all the presentation extensions
	 */
	protected HashMap presentationExtensions = new HashMap();

	/**
	 * reference to all the query extesions.
	 */
	protected HashMap queryExtensions = new HashMap();

	/*
	 * stores references to extended event handlers
	 */
	protected HashMap eventHandlerExtensions = new HashMap();

	/*
	 * references to prepare extentions
	 */
	protected HashMap preparationExtensions = new HashMap();

	/**
	 * extended item factory
	 */
	protected HashMap factories = new HashMap();

	/**
	 * emitterId to emitter info mapping
	 */
	protected HashMap<String, EmitterInfo> emitters = new HashMap<String, EmitterInfo>();

	/**
	 * output format to MIMEType mapping
	 */
	protected HashMap<String, String> format2MIMEType = new HashMap<String, String>();

	/**
	 * format to the default emitter id mapping
	 */
	private static HashMap<String, String> format2DefaultEmitterID = new HashMap<String, String>();
	static {
		format2DefaultEmitterID.put("doc", "org.eclipse.birt.report.engine.emitter.word");
		format2DefaultEmitterID.put("docx", "org.eclipse.birt.report.engine.emitter.docx");
		format2DefaultEmitterID.put("html", "org.eclipse.birt.report.engine.emitter.html");
		format2DefaultEmitterID.put("odp", "org.eclipse.birt.report.engine.emitter.odp");
		format2DefaultEmitterID.put("ods", "org.eclipse.birt.report.engine.emitter.prototype.ods");
		format2DefaultEmitterID.put("odt", "org.eclipse.birt.report.engine.emitter.odt");
		format2DefaultEmitterID.put("pdf", "org.eclipse.birt.report.engine.emitter.pdf");
		format2DefaultEmitterID.put("postscript", "org.eclipse.birt.report.engine.emitter.postscript");
		format2DefaultEmitterID.put("ppt", "org.eclipse.birt.report.engine.emitter.ppt");
		format2DefaultEmitterID.put("pptx", "org.eclipse.birt.report.engine.emitter.pptx");
		format2DefaultEmitterID.put("xls", "org.eclipse.birt.report.engine.emitter.prototype.excel");
	}

	protected Map dataExtractionFormats = new HashMap();

	/**
	 * HTML pagination.
	 */
	public static final String PAGE_BREAK_PAGINATION = "page-break-pagination";

	/**
	 * Pdf pagination.
	 */
	public static final String PAPER_SIZE_PAGINATION = "paper-size-pagination";

	/**
	 * No pagination.
	 */
	public static final String NO_PAGINATION = "no-pagination";

	/**
	 * whether emitter need to output the display:none or process it in layout
	 * engine. true: output display:none in emitter and do not process it in layout
	 * engine. false: process it in layout engine, not output it in emitter.
	 */
	public static final Boolean DEFAULT_OUTPUT_DISPLAY_NONE = Boolean.FALSE;

	/**
	 * emitter default supported image formats.
	 */
	public static final String DEFAULT_SUPPORTED_IMAGE_FORMATS = "PNG;GIF;JPG;BMP";

	/**
	 * Emitter needn't output result set data defaultly.
	 */
	public static final boolean DEFAULT_NEED_OUTPUT_RESULTSET = false;

	/**
	 * Dummy constructor
	 */
	ExtensionManager() {
		loadGenerationExtensionDefns();
		loadPresentationExtensionDefns();
		loadQueryExtensionDefns();
		loadEmitterExtensionDefns();
		loadEventHandlerExtensionDefns();
		loadPreparationExtensionDefns();
		loadDataExtractionExtensions();
		loadExtendedItems();
	}

	/**
	 * create the static instance. It is a separate function so that getInstance do
	 * not need to be synchronized
	 */
	private synchronized static void createInstance() {
		if (sm_instance == null)
			sm_instance = new ExtensionManager();
	}

	/**
	 * @return the single instance for the extension manager
	 */
	static public ExtensionManager getInstance() {
		if (sm_instance == null)
			createInstance();

		return sm_instance;
	}

	/**
	 * @param itemType
	 * @return an object that is used for generation time extended item processing
	 */
	public IReportItemExecutor createReportItemExecutor(ExecutorManager manager, String itemType) {
		IConfigurationElement config = (IConfigurationElement) generationExtensions.get(itemType);
		if (config != null) {
			Object object = createObject(config, "class"); //$NON-NLS-1$
			if (object instanceof IReportItemExecutor) {
				return (IReportItemExecutor) object;
			} else if (object instanceof IReportItemGeneration) {
				return new ExtendedGenerateExecutor(manager, (IReportItemGeneration) object);
			}
			logger.log(Level.WARNING, "Create Report Item Executor fail, Config not exist class: {0}", //$NON-NLS-1$
					config.getName());
		}
		// provide an default extendedGenerationExecutor if the extended
		// item do not implement IReportItemGeneration or
		// IReportItemExecutor interfaces. see bug196779
		return new ExtendedGenerateExecutor(manager, null);
	}

	/**
	 * @param itemType the type of the extended item, i.e., "chart"
	 * @return an object that is used for presentation time extended item processing
	 */
	public IReportItemPresentation createPresentationItem(String itemType) {
		IConfigurationElement config = (IConfigurationElement) presentationExtensions.get(itemType);
		if (config != null) {
			Object object = createObject(config, "class"); //$NON-NLS-1$
			if (object instanceof IReportItemPresentation) {
				return (IReportItemPresentation) object;
			}
		}
		return null;
	}

	/**
	 * @param itemType the type of the extended item, i.e., "chart"
	 * @return an object that is used for query preparation time extended item
	 *         processing
	 */
	public IReportItemQuery createQueryItem(String itemType) {
		IConfigurationElement config = (IConfigurationElement) queryExtensions.get(itemType);
		if (config != null) {
			Object object = createObject(config, "class"); //$NON-NLS-1$
			if (object instanceof IReportItemQuery) {
				return (IReportItemQuery) object;
			}
		}
		return null;
	}

	public boolean getAllRows(String itemType) {
		IConfigurationElement config = (IConfigurationElement) queryExtensions.get(itemType);
		if (config != null) {
			return Boolean.valueOf(config.getAttribute("getAllRows")).booleanValue();
		}
		return false;
	}

	/**
	 * @param format the format that the extension point supports
	 * @return an emitter
	 */
	public IContentEmitter createEmitter(String id) {
		IConfigurationElement config = null;
		if (id != null) {
			EmitterInfo emitterInfo = getEmitter(id);
			if (emitterInfo != null) {
				config = emitterInfo.getEmitter();
			}
		}
		if (config != null) {
			Object object = createObject(config, "class"); //$NON-NLS-1$
			if (object instanceof IContentEmitter) {
				return (IContentEmitter) object;
			}
		}
		return null;
	}

	/**
	 * Creates a data extraction extension according to its extension id.
	 * 
	 * @param id the extension id of a data extraction extension.
	 * @return a data extraction extension.
	 */
	public IDataExtractionExtension createDataExtractionExtensionById(String id) {
		if (id == null) {
			return null;
		}
		DataExtractionFormatInfo info = (DataExtractionFormatInfo) dataExtractionFormats.get(id);
		IConfigurationElement config = info.getDataExtractionExtension();
		if (config != null) {
			Object object = createObject(config, "class"); //$NON-NLS-1$
			if (object instanceof IDataExtractionExtension) {
				return (IDataExtractionExtension) object;
			}
		}
		return null;
	}

	/**
	 * Creates a data extraction extension according to its format.
	 * 
	 * @param format the format id of a data extraction extension.
	 * @return a data extraction extension.
	 */
	public IDataExtractionExtension createDataExtractionExtensionByFormat(String format) {
		if (format == null) {
			return null;
		}
		IConfigurationElement config = null;
		Iterator extensions = dataExtractionFormats.values().iterator();
		while (extensions.hasNext()) {
			DataExtractionFormatInfo info = (DataExtractionFormatInfo) extensions.next();
			if (format.equals(info.getFormat())) {
				config = info.getDataExtractionExtension();
				break;
			}
		}
		if (config != null) {
			Object object = createObject(config, "class"); //$NON-NLS-1$
			if (object instanceof IDataExtractionExtension) {
				return (IDataExtractionExtension) object;
			}
		}
		return null;
	}

	/**
	 * @param itemType the type of the extended item, i.e., "chart"
	 * @return an object that extended items use to handle java event
	 */
	public IReportEventHandler createEventHandler(String itemType) {
		IConfigurationElement config = (IConfigurationElement) eventHandlerExtensions.get(itemType);
		if (config != null) {
			Object object = createObject(config, "class"); //$NON-NLS-1$
			if (object instanceof IReportEventHandler) {
				return (IReportEventHandler) object;
			}
		}
		return null;
	}

	/**
	 * @param itemType the type of the extended item, i.e., "chart"
	 * @return an object that extended items use
	 */
	public IReportItemPreparation createPreparationItem(String itemType) {
		IConfigurationElement config = (IConfigurationElement) preparationExtensions.get(itemType);
		if (config != null) {
			Object object = createObject(config, "class");
			if (object instanceof IReportItemPreparation) {
				return (IReportItemPreparation) object;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param itemType itemType the type of the extended item, i.e., "chart"
	 * @return
	 */
	public IExtendedItemFactory createExtendedItemFactory(String itemType) {
		IConfigurationElement config = (IConfigurationElement) factories.get(itemType);
		if (config != null) {
			Object object = createObject(config, "class");
			if (object instanceof IExtendedItemFactory) {
				return (IExtendedItemFactory) object;
			}
		}
		return null;
	}

	/**
	 * @return all the emitter extensions
	 */
	public Collection getSupportedFormat() {
		return format2MIMEType.keySet();
	}

	/**
	 * return all emitter informations
	 * 
	 * @return all emitter informations
	 */
	public EmitterInfo[] getEmitterInfo() {
		EmitterInfo[] infos = new EmitterInfo[emitters.size()];
		Object[] keys = emitters.keySet().toArray();
		for (int index = 0, length = keys.length; index < length; index++) {
			infos[index] = (EmitterInfo) emitters.get(keys[index].toString());
		}
		return infos;
	}

	/**
	 * @param config
	 * @param property
	 * @return
	 */
	protected Object createObject(IConfigurationElement config, String property) {
		try {
			String value = config.getAttribute(property);
			if (value != null) {
				Object object = config.createExecutableExtension(property);
				if (object != null)
					return object;
			}
		} catch (FrameworkException ex) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, "Can not instantiate class {0} with property {1}.", //$NON-NLS-1$
						new String[] { config.getAttribute("class"), property }); //$NON-NLS-1$
			}
		}
		return null;
	}

	/**
	 * load report item generation extension definitions
	 */
	protected void loadGenerationExtensionDefns() {
		IExtension[] exts = getExtensions(EXTENSION_POINT_GENERATION);
		if (exts == null)
			return;

		for (int i = 0; i < exts.length; i++) {
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++) {
				String itemName = configs[j].getAttribute("name"); //$NON-NLS-1$
				generationExtensions.put(itemName, configs[j]);
				logger.log(Level.FINE, "Load generation extension: {0}", itemName); //$NON-NLS-1$
			}
		}
	}

	/**
	 * load report item presentation extension definitions
	 */
	protected void loadPresentationExtensionDefns() {
		IExtension[] exts = getExtensions(EXTENSION_POINT_PRESENTATION);
		if (exts == null) {
			return;
		}
		for (int i = 0; i < exts.length; i++) {
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++) {
				String itemName = configs[j].getAttribute("name"); //$NON-NLS-1$
				presentationExtensions.put(itemName, configs[j]);
				logger.log(Level.FINE, "Load prsentation extension: {0}", itemName); //$NON-NLS-1$
			}
		}
	}

	/**
	 * load report item query extension definitions
	 */
	protected void loadQueryExtensionDefns() {
		IExtension[] exts = getExtensions(EXTENSION_POINT_QUERY);
		if (exts == null) {
			return;
		}
		for (int i = 0; i < exts.length; i++) {
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++) {
				String itemName = configs[j].getAttribute("name"); //$NON-NLS-1$
				queryExtensions.put(itemName, configs[j]);
				logger.log(Level.FINE, "Load query extension: {0}", itemName); //$NON-NLS-1$
			}
		}
	}

	/**
	 * load report item emitters extension definitions
	 */
	protected void loadEmitterExtensionDefns() {
		IExtension[] exts = getExtensions(EXTENSION_POINT_EMITTERS);
		if (exts == null) {
			return;
		}
		for (int i = 0; i < exts.length; i++)
		// loop at emitters level, i.e., fo or html
		{
			String namespace = exts[i].getNamespace();
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++) // loop at emitter level
			{
				String id = configs[j].getAttribute("id"); //$NON-NLS-1$
				// check if we need to override the existing emitterInfo with
				// same id.
				String overridePriority = configs[j].getAttribute("overridePriority");
				int priority = 0;
				if (null != overridePriority) {
					priority = Integer.valueOf(overridePriority).intValue();
				}
				String format = configs[j].getAttribute("format"); //$NON-NLS-1$
				String mimeType = configs[j].getAttribute("mimeType"); //$NON-NLS-1$
				String pagination = configs[j].getAttribute("pagination");
				String supportedImageFormats = configs[j].getAttribute("supportedImageFormats");
				if (pagination == null) {
					pagination = PAGE_BREAK_PAGINATION;
				}
				String icon = configs[j].getAttribute("icon");
				Boolean outDisplayNone = Boolean.valueOf(configs[j].getAttribute("outputDisplayNone"));
				String fileExtension = configs[j].getAttribute("fileExtension");
				Boolean isHidden = Boolean.valueOf(configs[j].getAttribute("isHidden"));
				boolean needOutputResultSet = Boolean.valueOf(configs[j].getAttribute("needOutputResultSet"));
				String isFormatDeprecated = configs[j].getAttribute("isFormatDeprecated");
				boolean deprecated = false;
				if (null != isFormatDeprecated) {
					deprecated = Boolean.valueOf(isFormatDeprecated).booleanValue();
				}
				if (!Pattern.matches(emitterFormatPattern, format)) {
					logger.log(Level.SEVERE, "\"" + format
							+ "\" is an invalid format. A valid format must begin with a letter, the dollar sign \"$\", or the underscore character \"_\". Subsequent characters may be letters, digits, dollar signs, or underscore characters.");
				} else {
					EmitterInfo emitterInfo = new EmitterInfo(format, id, pagination, mimeType, icon, namespace,
							fileExtension, outDisplayNone, isHidden, supportedImageFormats, needOutputResultSet,
							configs[j]);
					emitterInfo.setOverridePriority(priority);
					emitterInfo.setFormatDeprecated(deprecated);

					EmitterInfo existedInfo = (EmitterInfo) emitters.get(id);
					if (existedInfo != null) {
						if (existedInfo.getOverridePriority() < priority) {
							emitters.remove(id);
							format2MIMEType.put(format, emitterInfo.getMimeType());
							emitters.put(id, emitterInfo);
						}
					} else {
						format2MIMEType.put(format, emitterInfo.getMimeType());
						emitters.put(id, emitterInfo);
					}
					logger.log(Level.FINE, "Load {0} emitter {1}", new String[] { format, id }); //$NON-NLS-1$
				}
			}
		}
	}

	/*
	 * load extended event handler definitions
	 */
	protected void loadEventHandlerExtensionDefns() {
		IExtension[] exts = getExtensions(EXTENSION_POINT_EVENTHANDLER);
		if (exts == null) {
			return;
		}
		for (int i = 0; i < exts.length; i++) {
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++) {
				String itemName = configs[j].getAttribute("name"); //$NON-NLS-1$
				eventHandlerExtensions.put(itemName, configs[j]);
				logger.log(Level.FINE, "Load reportEventHandler extension: {0}", itemName); //$NON-NLS-1$
			}
		}
	}

	/**
	 * load prepare extensions
	 */
	protected void loadPreparationExtensionDefns() {
		IExtension[] exts = getExtensions(EXTENSION_POINT_PREPARATION);
		if (exts == null) {
			return;
		}
		for (int i = 0; i < exts.length; i++) {
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++) {
				String itemName = configs[j].getAttribute("name"); //$NON-NLS-1$
				preparationExtensions.put(itemName, configs[j]);
				logger.log(Level.FINE, "Load reportItemPrepare extension: {0}", itemName); //$NON-NLS-1$
			}
		}
	}

	/**
	 * load extended item factories
	 */
	protected void loadExtendedItems() {
		IExtension[] exts = getExtensions(EXTENSION_POINT_EXTENDED_ITEM_FACTORY);
		if (exts == null) {
			return;
		}
		for (int i = 0; i < exts.length; i++) {
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++) {
				String itemName = configs[j].getAttribute("name");
				factories.put(itemName, configs[j]);
				logger.log(Level.FINE, "Load extendedItem extension: {0}", itemName);
			}
		}
	}

	private IExtension[] getExtensions(String extensionPoint) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extPoint = registry.getExtensionPoint(extensionPoint);
		if (extPoint == null)
			return null;

		IExtension[] exts = extPoint.getExtensions();
		logger.log(Level.FINE, "Start load extension point: {0}", extensionPoint); //$NON-NLS-1$
		return exts;
	}

	/**
	 * Loads data extraction extension.
	 */
	public void loadDataExtractionExtensions() {
		IExtension[] exts = getExtensions(EXTENSION_POINT_DATAEXTRACTION);
		if (exts == null) {
			return;
		}
		for (int i = 0; i < exts.length; i++) {
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++) {
				String id = configs[j].getAttribute("id"); //$NON-NLS-1$
				String format = configs[j].getAttribute("format"); //$NON-NLS-1$
				String mimeType = configs[j].getAttribute("mimeType"); //$NON-NLS-1$
				String name = configs[j].getAttribute("name");
				Boolean isHidden = Boolean.valueOf(configs[j].getAttribute("isHidden"));
				DataExtractionFormatInfo info = new DataExtractionFormatInfo(id, format, mimeType, name, isHidden,
						configs[j]);
				dataExtractionFormats.put(id, info);
				logger.log(Level.FINE, "Load data extraction extension: {0}", id); //$NON-NLS-1$
			}
		}
	}

	/**
	 * @param format the output format
	 * @return the mime type for the specific format
	 */
	public String getMIMEType(String format) {
		if (format != null) {
			format = format.toLowerCase();
		}
		if (format2MIMEType.containsKey(format)) {
			return format2MIMEType.get(format);
		}
		return null;
	}

	/**
	 * @param emitterId emitterId
	 * @return the pagination for the specified emitter ID.
	 */
	public String getPagination(String emitterId) {
		if (emitterId != null) {
			EmitterInfo emitterInfo = emitters.get(emitterId);
			if (emitterInfo != null) {
				return emitterInfo.getPagination();
			}
		}
		return PAGE_BREAK_PAGINATION;
	}

	public Boolean getOutputDisplayNone(String emitterId) {
		if (emitterId != null) {
			EmitterInfo emitterInfo = emitters.get(emitterId);
			if (emitterInfo != null) {
				return emitterInfo.getOutputDisplayNone();
			}
		}
		return DEFAULT_OUTPUT_DISPLAY_NONE;
	}

	public String getSupportedImageFormats(String emitterId) {
		if (emitterId != null) {
			EmitterInfo emitterInfo = (EmitterInfo) emitters.get(emitterId);
			if (emitterInfo != null) {
				String supportedImageFormats = emitterInfo.getSupportedImageFormats();
				if (null != supportedImageFormats) {
					return supportedImageFormats;
				}
			}
		}
		return DEFAULT_SUPPORTED_IMAGE_FORMATS;
	}

	public boolean needOutputResultSet(String emitterId) {
		EmitterInfo emitterInfo = getEmitter(emitterId);
		return emitterInfo == null ? DEFAULT_NEED_OUTPUT_RESULTSET : emitterInfo.needOutputResultSet();
	}

	private EmitterInfo getEmitter(String emitterId) {
		if (emitterId != null) {
			return emitters.get(emitterId);
		}
		return null;
	}

	public DataExtractionFormatInfo[] getDataExtractionExtensionInfo() {
		int length = dataExtractionFormats.size();
		DataExtractionFormatInfo[] result = new DataExtractionFormatInfo[length];
		Iterator iterator = dataExtractionFormats.values().iterator();
		for (int i = 0; i < length; i++) {
			result[i] = (DataExtractionFormatInfo) iterator.next();
		}
		return result;
	}

	public boolean isValidEmitterID(String id) {
		boolean isValidEmitterID = false;
		if (id != null && emitters.containsKey(id)) {
			isValidEmitterID = true;
		}
		return isValidEmitterID;
	}

	public boolean isSupportedFormat(String format) {
		boolean supported = false;
		Collection supportedFormats = getSupportedFormat();
		Iterator iter = supportedFormats.iterator();
		while (iter.hasNext()) {
			String supportedFormat = (String) iter.next();
			if (supportedFormat != null && supportedFormat.equalsIgnoreCase(format)) {
				supported = true;
				break;
			}
		}
		return supported;
	}

	public String getSupportedFormat(String format) {
		Collection supportedFormats = getSupportedFormat();
		Iterator iter = supportedFormats.iterator();
		while (iter.hasNext()) {
			String supportedFormat = (String) iter.next();
			if (supportedFormat != null && supportedFormat.equalsIgnoreCase(format)) {
				return supportedFormat;
			}
		}
		return null;
	}

	public String getFormat(String emitterId) {
		EmitterInfo emitterInfo = emitters.get(emitterId);
		if (emitterInfo != null) {
			return emitterInfo.getFormat();
		}
		return null;
	}

	public String getEmitterID(String format) {
		String emitterId = format2DefaultEmitterID.get(format);
		if (emitterId == null) {
			for (String id : emitters.keySet()) {
				EmitterInfo emitterInfo = (EmitterInfo) emitters.get(id);
				if (format.equalsIgnoreCase(emitterInfo.getFormat())) {
					emitterId = emitterInfo.getID();
					break;
				}
			}
		}
		return emitterId;
	}

}
