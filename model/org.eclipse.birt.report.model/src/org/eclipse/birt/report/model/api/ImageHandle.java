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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.impl.ActionHelper;

/**
 * Represents an image report item. The image can come from a number of sources:
 * <ul>
 * <li>embedded image in the report design
 * <li>image file
 * <li>image presented by URL
 * <li>expression which returns the image contents
 * </ul>
 * <p>
 * The image has the following properties:
 * <ul>
 * <li>An optional hyperlink for this image.
 * <li>An optional help text for the image.
 * <li>An optional text message to display in place of the image in a web
 * browser.
 * </ul>
 * 
 * The image item can be sized to the image (in which case the height and width
 * attributes are ignored), or the image can be sized or clipped to fit the
 * item. Images are always scaled as percentage proportionately.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.ImageItem
 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
 */

public class ImageHandle extends ReportItemHandle implements IImageItemModel {

	/**
	 * Constructs a image handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public ImageHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the image scale. The scale factor for the image given as a
	 * percentage.
	 * 
	 * @return the scale of this image.
	 */

	public double getScale() {
		return getFloatProperty(IImageItemModel.SCALE_PROP);
	}

	/**
	 * Returns the image size. The size must be the internal name that is one the
	 * following options defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <p>
	 * <ul>
	 * <li><code>IMAGE_SIZE_SIZE_TO_IMAGE</code>
	 * <li><code>IMAGE_SIZE_SCALE_TO_ITEM</code>
	 * <li><code>IMAGE_SIZE_CLIP</code>
	 * </ul>
	 * 
	 * @return the internal value of the image size.
	 */

	public String getSize() {
		return getStringProperty(IImageItemModel.SIZE_PROP);
	}

	/**
	 * Returns the alternate text of this image.
	 * 
	 * @return the alternate text of the image item.
	 */
	@Deprecated
	public String getAltText() {
		ExpressionHandle expr = getAltTextExpression();
		if (expr != null) {
			return expr.getStringExpression();
		}
		return null;
	}

	/**
	 * Sets the the alternate text of this image.
	 * 
	 * @param altText the alternate text
	 * @throws SemanticException
	 */
	@Deprecated
	public void setAltText(String altText) throws SemanticException {
		setProperty(IReportItemModel.ALTTEXT_PROP, new Expression(altText, ExpressionType.CONSTANT));
	}

	/**
	 * Returns the image source type. This is one of the following options defined
	 * in <code>DesignChoiceConstants</code>:
	 * <p>
	 * <ul>
	 * <li><code>IMAGE_REF_TYPE_NONE</code>
	 * <li><code>IMAGE_REF_TYPE_URL</code>
	 * <li><code>IMAGE_REF_TYPE_FILE</code>
	 * <li><code>IMAGE_REF_TYPE_EXPR</code>
	 * <li><code>IMAGE_REF_TYPE_EMBED</code>
	 * </ul>
	 * 
	 * @return the image source type.
	 * 
	 */

	public String getSource() {
		return getStringProperty(IImageItemModel.SOURCE_PROP);
	}

	/**
	 * Sets the image source type. This is one of the following options defined in
	 * <code>DesignChoiceConstants</code>:
	 * <p>
	 * <ul>
	 * <li><code>IMAGE_REF_TYPE_NONE</code>
	 * <li><code>IMAGE_REF_TYPE_URL</code>
	 * <li><code>IMAGE_REF_TYPE_FILE</code>
	 * <li><code>IMAGE_REF_TYPE_EXPR</code>
	 * <li><code>IMAGE_REF_TYPE_EMBED</code>
	 * </ul>
	 * 
	 * @param source the image source type.
	 * @throws SemanticException if the <code>source</code> is not one of the above.
	 * 
	 */

	public void setSource(String source) throws SemanticException {
		setProperty(IImageItemModel.SOURCE_PROP, source);
	}

	/**
	 * Returns the image URI if the image source type is
	 * <code>IMAGE_REF_TYPE_URL</code> or <code>IMAGE_REF_TYPE_FILE</code>.
	 * 
	 * @return the image URI if the image source type is
	 *         <code>IMAGE_REF_TYPE_URL</code> or <code>IMAGE_REF_TYPE_FILE</code>.
	 *         Otherwise, return <code>null</code>.
	 */

	public String getURI() {
		if (DesignChoiceConstants.IMAGE_REF_TYPE_URL.equalsIgnoreCase(getSource())
				|| DesignChoiceConstants.IMAGE_REF_TYPE_FILE.equalsIgnoreCase(getSource())) {
			return getStringProperty(IImageItemModel.URI_PROP);
		}
		return null;
	}

	/**
	 * Returns the image URI if the image source type is
	 * <code>IMAGE_REF_TYPE_URL</code> or <code>IMAGE_REF_TYPE_FILE</code>.
	 * 
	 * @return the image URI if the image source type is
	 *         <code>IMAGE_REF_TYPE_URL</code> , Otherwise, return
	 *         <code>null</code>.
	 */

	public String getURL() {
		if (DesignChoiceConstants.IMAGE_REF_TYPE_URL.equalsIgnoreCase(getSource()))
			return getStringProperty(IImageItemModel.URI_PROP);

		return null;
	}

	/**
	 * Returns the image URI if the image source type is
	 * <code>IMAGE_REF_TYPE_URL</code> or <code>IMAGE_REF_TYPE_FILE</code>.
	 * 
	 * @return the image URI if the image source type is
	 *         <code>IMAGE_REF_TYPE_FILE</code>. Otherwise, return
	 *         <code>null</code>.
	 */

	public String getFile() {
		if (DesignChoiceConstants.IMAGE_REF_TYPE_FILE.equalsIgnoreCase(getSource()))
			return getStringProperty(IImageItemModel.URI_PROP);

		return null;
	}

	/**
	 * Returns the type expression of the image item if the image source type is
	 * <code>IMAGE_REF_TYPE_EXPR</code>.
	 * 
	 * @return the type expression, if the image source type is
	 *         <code>IMAGE_REF_TYPE_EXPR</code>. Otherwise, return
	 *         <code>null</code>.
	 * 
	 */

	public String getTypeExpression() {
		if (DesignChoiceConstants.IMAGE_REF_TYPE_EXPR.equalsIgnoreCase(getSource())) {
			return getStringProperty(IImageItemModel.TYPE_EXPR_PROP);
		}
		return null;

	}

	/**
	 * Returns the value expression of the image if the image source type is
	 * <code>IMAGE_REF_TYPE_EXPR</code>.
	 * 
	 * @return the value expression, if the image source type is
	 *         <code>IMAGE_REF_TYPE_EXPR</code>. Otherwise, return
	 *         <code>null</code>.
	 */

	public String getValueExpression() {
		if (DesignChoiceConstants.IMAGE_REF_TYPE_EXPR.equalsIgnoreCase(getSource())) {
			return getStringProperty(IImageItemModel.VALUE_EXPR_PROP);
		}
		return null;

	}

	/**
	 * Returns the embedded image name that this image refers, if the image source
	 * type is <code>IMAGE_REF_TYPE_EMBED</code>. This is not the same as
	 * {@link DesignElementHandle#getName}of this image item.
	 * 
	 * @return the embedded image name, if the image source type is
	 *         <code>IMAGE_REF_TYPE_EMBED</code>. Otherwise, return
	 *         <code>null</code>.
	 */

	public String getImageName() {
		if (!DesignChoiceConstants.IMAGE_REF_TYPE_EMBED
				.equalsIgnoreCase(getStringProperty(IImageItemModel.SOURCE_PROP)))
			return null;

		return getStringProperty(IImageItemModel.IMAGE_NAME_PROP);

	}

	/**
	 * Returns the embedded image handle that this image refers, if the image source
	 * type is <code>IMAGE_REF_TYPE_EMBED</code>.
	 * 
	 * @return the embedded image handle, if the image source type is
	 *         <code>IMAGE_REF_TYPE_EMBED</code> and the referred embedded image is
	 *         found. Otherwise, return <code>null</code>.
	 */

	public EmbeddedImageHandle getEmbeddedImage() {
		if (!DesignChoiceConstants.IMAGE_REF_TYPE_EMBED.equals(getStringProperty(IImageItemModel.SOURCE_PROP)))
			return null;

		StructRefValue imageRef = (StructRefValue) getElement().getProperty(getModule(),
				IImageItemModel.IMAGE_NAME_PROP);
		if (imageRef == null)
			return null;

		// the structure is resolve, then find the owner module and construct
		// the embedded image handle

		if (imageRef.isResolved()) {
			EmbeddedImage image = (EmbeddedImage) imageRef.getTargetStructure();
			DesignElement owner = getElement();
			while (owner != null) {
				if (owner.getLocalProperty(module, IImageItemModel.IMAGE_NAME_PROP) == imageRef) {
					Module targetModule = null;

					// if find the image in this parent or virtual parent, then
					// getRoot must not be null

					if (owner != getElement())
						targetModule = owner.getRoot();

					// if find the image in element itself, the element maybe
					// not in any tree

					else {
						if (imageRef.getLibraryNamespace() != null)
							targetModule = getModule().getLibraryWithNamespace(imageRef.getLibraryNamespace());
						else
							targetModule = getModule();

					}

					assert targetModule != null;

					// find the position of the image in the target module and
					// construct the handle

					List images = targetModule.getListProperty(targetModule, IModuleModel.IMAGES_PROP);
					if (images == null || images.isEmpty())
						continue;

					int posn = images.indexOf(image);
					PropertyHandle propHandle = targetModule.getHandle(targetModule)
							.getPropertyHandle(IModuleModel.IMAGES_PROP);
					assert posn != -1;
					EmbeddedImageHandle imageHandle = new EmbeddedImageHandle(propHandle, posn);
					return imageHandle;
				}

				// recursively find the image and construct the handle if it has
				// parent or virtual parent

				owner = owner.isVirtualElement() ? owner.getVirtualParent() : owner.getExtendsElement();
			}
		}

		return null;
	}

	/**
	 * Sets the embedded image name that this image refers, if the image source type
	 * is <code>IMAGE_REF_TYPE_EMBED</code>. The reference type is automatically set
	 * in this method. This is not the same as
	 * {@link DesignElementHandle#setName(String )}.
	 * 
	 * @param name the embedded image name
	 * @throws SemanticException if the property is locked.
	 */

	public void setImageName(String name) throws SemanticException {
		ActivityStack as = module.getActivityStack();
		try {

			as.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_MESSAGE,
					new String[] { IMAGE_NAME_PROP }));

			setProperty(IImageItemModel.SOURCE_PROP, DesignChoiceConstants.IMAGE_REF_TYPE_EMBED);
			setProperty(IImageItemModel.IMAGE_NAME_PROP, name);
		} catch (SemanticException e) {
			as.rollback();
			throw e;
		}
		as.commit();
	}

	/**
	 * Sets the image uri property. The source type is
	 * <code>IMAGE_REF_TYPE_URL</code>, and will automatically set in this method.
	 * 
	 * @param url the url to be set.
	 * @throws SemanticException if the property is locked.
	 */

	public void setURL(String url) throws SemanticException {
		String source = DesignChoiceConstants.IMAGE_REF_TYPE_URL;

		setURIProperty(url, source);
	}

	/**
	 * Sets the image uri property. The source type is
	 * <code>IMAGE_REF_TYPE_URL</code>, and will automatically set in this method.
	 * 
	 * @param uri the uri to be set.
	 * @throws SemanticException if the property is locked.
	 * @deprecated should use {@link #setFile(String)} or {@link #setURL(String)}.
	 */

	public void setURI(String uri) throws SemanticException {
		setURL(uri);
	}

	/**
	 * Sets the image uri property. The source type is
	 * <code>IMAGE_REF_TYPE_FILE</code>, and will automatically set in this method.
	 * 
	 * @param file the file to be set.
	 * @throws SemanticException if the property is locked.
	 */

	public void setFile(String file) throws SemanticException {
		String source = DesignChoiceConstants.IMAGE_REF_TYPE_FILE;

		setURIProperty(file, source);
	}

	/**
	 * Sets the image uri property. The source type is
	 * <code>IMAGE_REF_TYPE_FILE</code> or <code>IMAGE_REF_TYPE_FILE</code>.
	 * 
	 * @param prop   uri property
	 * @param source image reference property
	 * @throws SemanticException
	 */

	private void setURIProperty(Object prop, String source) throws SemanticException {
		ActivityStack as = module.getActivityStack();
		try {
			as.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_MESSAGE,
					new String[] { URI_PROP }));

			setProperty(IImageItemModel.SOURCE_PROP, source);
			setProperty(IImageItemModel.URI_PROP, prop);
		} catch (SemanticException e) {
			as.rollback();
			throw e;
		}
		as.commit();
	}

	/**
	 * Sets the image scale property. The scale factor for the image given as a
	 * percentage. The default is 100%.
	 * 
	 * @param scale the scale value to be set.
	 * @throws SemanticException if the property is locked.
	 */

	public void setScale(double scale) throws SemanticException {
		setFloatProperty(IImageItemModel.SCALE_PROP, scale);
	}

	/**
	 * Sets the image size property. The input value is one of the followings
	 * defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <p>
	 * <ul>
	 * <li><code>IMAGE_SIZE_SIZE_TO_IMAGE</code>
	 * <li><code>IMAGE_SIZE_SCALE_TO_ITEM</code>
	 * <li><code>IMAGE_SIZE_CLIP</code>
	 * </ul>
	 * 
	 * @param size the size value to be set.
	 * @throws SemanticException if the input size is not one of the above, or if
	 *                           the property is locked.
	 */

	public void setSize(String size) throws SemanticException {
		setStringProperty(IImageItemModel.SIZE_PROP, size);

	}

	/**
	 * Sets the type expression value. The source type is automatically set to
	 * <code>IMAGE_REF_TYPE_EXPR</code>.
	 * 
	 * @param value the type expression value.
	 * @throws SemanticException if the property is locked.
	 */

	public void setTypeExpression(String value) throws SemanticException {
		ActivityStack as = module.getActivityStack();
		try {

			as.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_MESSAGE,
					new String[] { TYPE_EXPR_PROP }));

			setProperty(IImageItemModel.SOURCE_PROP, DesignChoiceConstants.IMAGE_REF_TYPE_EXPR);
			setProperty(IImageItemModel.TYPE_EXPR_PROP, value);
		} catch (SemanticException e) {
			as.rollback();
			throw e;
		}
		as.commit();
	}

	/**
	 * Sets the value expression value. The source type is automatically set to
	 * <code>IMAGE_REF_TYPE_EXPR</code>.
	 * 
	 * @param value the value expression.
	 * @throws SemanticException if the property is locked.
	 */

	public void setValueExpression(String value) throws SemanticException {
		ActivityStack as = module.getActivityStack();
		try {

			as.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_MESSAGE,
					new String[] { VALUE_EXPR_PROP }));

			setProperty(IImageItemModel.SOURCE_PROP, DesignChoiceConstants.IMAGE_REF_TYPE_EXPR);
			setProperty(IImageItemModel.VALUE_EXPR_PROP, value);
		} catch (SemanticException e) {
			as.rollback();
			throw e;
		}
		as.commit();
	}

	/**
	 * Returns a handle to work with the action property, action is a structure that
	 * defines a hyperlink.
	 * 
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the image.
	 * @see ActionHandle
	 */

	public ActionHandle getActionHandle() {
		return new ActionHelper(this, ACTION_PROP).getActionHandle();
	}

	/**
	 * Set an action on the image.
	 * 
	 * @param action new action to be set on the image, it represents a bookmark
	 *               link, hyper-link, and drill through etc.
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the image.
	 * 
	 * @throws SemanticException if member of the action is not valid.
	 */

	public ActionHandle setAction(Action action) throws SemanticException {
		return new ActionHelper(this, ACTION_PROP).setAction(action);
	}

	/**
	 * Returns the iterator for action defined on this image item.
	 * 
	 * @return the iterator for <code>Action</code> structure list defined on this
	 *         image item
	 */

	public Iterator<ActionHandle> actionsIterator() {
		return new ActionHelper(this, ACTION_PROP).actionsIterator();
	}

	/**
	 * Returns the help text of this image item.
	 * 
	 * @return the help text
	 */

	public String getHelpText() {
		return getStringProperty(IImageItemModel.HELP_TEXT_PROP);
	}

	/**
	 * Sets the help text of this image item.
	 * 
	 * @param helpText the help text
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setHelpText(String helpText) throws SemanticException {
		setStringProperty(IImageItemModel.HELP_TEXT_PROP, helpText);
	}

	/**
	 * Returns the resource key of the help text of this image item.
	 * 
	 * @return the resource key of the help text
	 */

	public String getHelpTextKey() {
		return getStringProperty(IImageItemModel.HELP_TEXT_ID_PROP);
	}

	/**
	 * Sets the resource key of help text of this image item.
	 * 
	 * @param helpTextKey the help text
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setHelpTextKey(String helpTextKey) throws SemanticException {
		setStringProperty(IImageItemModel.HELP_TEXT_ID_PROP, helpTextKey);
	}

	/**
	 * Gets the fit to container property value of this image item.
	 * 
	 * @return the fit to container of this image item.
	 */
	public boolean fitToContainer() {
		return getBooleanProperty(FIT_TO_CONTAINER_PROP);
	}

	/**
	 * Sets the fit to container property value of this image item.
	 * 
	 * @param fitToContainer the value of fit to container.
	 * @throws SemanticException if the property is locked.
	 */
	public void setFitToContainer(boolean fitToContainer) throws SemanticException {
		setProperty(FIT_TO_CONTAINER_PROP, Boolean.valueOf(fitToContainer));
	}

	/**
	 * Sets the image uri property by an expression. The source type is
	 * <code>IMAGE_REF_TYPE_URL</code>, and will automatically set in this method.
	 * 
	 * @param expr the expression to be set.
	 * @throws SemanticException if the property is locked.
	 */

	public void setURL(Expression expr) throws SemanticException {
		setURIProperty(expr, DesignChoiceConstants.IMAGE_REF_TYPE_URL);
	}

	/**
	 * Sets the image uri property by an expression. The source type is
	 * <code>IMAGE_REF_TYPE_FILE</code>, and will automatically set in this method.
	 * 
	 * @param expr the expression to be set.
	 * @throws SemanticException if the property is locked.
	 */

	public void setFile(Expression expr) throws SemanticException {
		setURIProperty(expr, DesignChoiceConstants.IMAGE_REF_TYPE_FILE);
	}

	/**
	 * Gets the flag which indicates whether the image scales proportionally or not.
	 *
	 * @return true if the image scale proportionally, othewise false
	 */
	public boolean isProportionalScale() {
		return getBooleanProperty(PROPORTIONAL_SCALE_PROP);
	}

	/**
	 * Sets the flag which indicates whether the image scales proportionally or not.
	 *
	 * @param proportionalScale the new flag to set
	 * @throws SemanticException
	 */
	public void setProportionalScale(boolean proportionalScale) throws SemanticException {
		setBooleanProperty(PROPORTIONAL_SCALE_PROP, proportionalScale);
	}

}