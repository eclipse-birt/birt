/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute.impl;

import java.util.Locale;

import org.eclipse.birt.chart.internal.model.Fraction;
import org.eclipse.birt.chart.internal.model.FractionApproximator;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.ibm.icu.util.ULocale;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Fraction Number Format Specifier</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FractionNumberFormatSpecifierImpl#isPrecise
 * <em>Precise</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FractionNumberFormatSpecifierImpl#getFractionDigits
 * <em>Fraction Digits</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FractionNumberFormatSpecifierImpl#getNumerator
 * <em>Numerator</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FractionNumberFormatSpecifierImpl#getPrefix
 * <em>Prefix</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FractionNumberFormatSpecifierImpl#getSuffix
 * <em>Suffix</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FractionNumberFormatSpecifierImpl#getDelimiter
 * <em>Delimiter</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FractionNumberFormatSpecifierImpl extends FormatSpecifierImpl implements FractionNumberFormatSpecifier {

	/**
	 * The default value of the '{@link #isPrecise() <em>Precise</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isPrecise()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PRECISE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPrecise() <em>Precise</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isPrecise()
	 * @generated
	 * @ordered
	 */
	protected boolean precise = PRECISE_EDEFAULT;

	/**
	 * This is true if the Precise attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean preciseESet;

	/**
	 * The default value of the '{@link #getFractionDigits() <em>Fraction
	 * Digits</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFractionDigits()
	 * @generated
	 * @ordered
	 */
	protected static final int FRACTION_DIGITS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getFractionDigits() <em>Fraction
	 * Digits</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFractionDigits()
	 * @generated
	 * @ordered
	 */
	protected int fractionDigits = FRACTION_DIGITS_EDEFAULT;

	/**
	 * This is true if the Fraction Digits attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean fractionDigitsESet;

	/**
	 * The default value of the '{@link #getNumerator() <em>Numerator</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getNumerator()
	 * @generated
	 * @ordered
	 */
	protected static final double NUMERATOR_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getNumerator() <em>Numerator</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getNumerator()
	 * @generated
	 * @ordered
	 */
	protected double numerator = NUMERATOR_EDEFAULT;

	/**
	 * This is true if the Numerator attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean numeratorESet;

	/**
	 * The default value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPrefix()
	 * @generated
	 * @ordered
	 */
	protected static final String PREFIX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPrefix()
	 * @generated
	 * @ordered
	 */
	protected String prefix = PREFIX_EDEFAULT;

	/**
	 * The default value of the '{@link #getSuffix() <em>Suffix</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSuffix()
	 * @generated
	 * @ordered
	 */
	protected static final String SUFFIX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSuffix() <em>Suffix</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSuffix()
	 * @generated
	 * @ordered
	 */
	protected String suffix = SUFFIX_EDEFAULT;

	/**
	 * The default value of the '{@link #getDelimiter() <em>Delimiter</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDelimiter()
	 * @generated
	 * @ordered
	 */
	protected static final String DELIMITER_EDEFAULT = "/"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getDelimiter() <em>Delimiter</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDelimiter()
	 * @generated
	 * @ordered
	 */
	protected String delimiter = DELIMITER_EDEFAULT;

	/**
	 * This is true if the Delimiter attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean delimiterESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected FractionNumberFormatSpecifierImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.FRACTION_NUMBER_FORMAT_SPECIFIER;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isPrecise() {
		return precise;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setPrecise(boolean newPrecise) {
		boolean oldPrecise = precise;
		precise = newPrecise;
		boolean oldPreciseESet = preciseESet;
		preciseESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__PRECISE, oldPrecise, precise, !oldPreciseESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetPrecise() {
		boolean oldPrecise = precise;
		boolean oldPreciseESet = preciseESet;
		precise = PRECISE_EDEFAULT;
		preciseESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__PRECISE, oldPrecise, PRECISE_EDEFAULT,
					oldPreciseESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetPrecise() {
		return preciseESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getFractionDigits() {
		return fractionDigits;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setFractionDigits(int newFractionDigits) {
		int oldFractionDigits = fractionDigits;
		fractionDigits = newFractionDigits;
		boolean oldFractionDigitsESet = fractionDigitsESet;
		fractionDigitsESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS, oldFractionDigits,
					fractionDigits, !oldFractionDigitsESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetFractionDigits() {
		int oldFractionDigits = fractionDigits;
		boolean oldFractionDigitsESet = fractionDigitsESet;
		fractionDigits = FRACTION_DIGITS_EDEFAULT;
		fractionDigitsESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS, oldFractionDigits,
					FRACTION_DIGITS_EDEFAULT, oldFractionDigitsESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetFractionDigits() {
		return fractionDigitsESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getNumerator() {
		return numerator;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setNumerator(double newNumerator) {
		double oldNumerator = numerator;
		numerator = newNumerator;
		boolean oldNumeratorESet = numeratorESet;
		numeratorESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__NUMERATOR, oldNumerator, numerator,
					!oldNumeratorESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetNumerator() {
		double oldNumerator = numerator;
		boolean oldNumeratorESet = numeratorESet;
		numerator = NUMERATOR_EDEFAULT;
		numeratorESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__NUMERATOR, oldNumerator, NUMERATOR_EDEFAULT,
					oldNumeratorESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetNumerator() {
		return numeratorESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setPrefix(String newPrefix) {
		String oldPrefix = prefix;
		prefix = newPrefix;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__PREFIX, oldPrefix, prefix));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSuffix(String newSuffix) {
		String oldSuffix = suffix;
		suffix = newSuffix;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__SUFFIX, oldSuffix, suffix));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDelimiter(String newDelimiter) {
		String oldDelimiter = delimiter;
		delimiter = newDelimiter;
		boolean oldDelimiterESet = delimiterESet;
		delimiterESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__DELIMITER, oldDelimiter, delimiter,
					!oldDelimiterESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetDelimiter() {
		String oldDelimiter = delimiter;
		boolean oldDelimiterESet = delimiterESet;
		delimiter = DELIMITER_EDEFAULT;
		delimiterESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__DELIMITER, oldDelimiter, DELIMITER_EDEFAULT,
					oldDelimiterESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetDelimiter() {
		return delimiterESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__PRECISE:
			return isPrecise();
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS:
			return getFractionDigits();
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__NUMERATOR:
			return getNumerator();
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__PREFIX:
			return getPrefix();
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__SUFFIX:
			return getSuffix();
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__DELIMITER:
			return getDelimiter();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__PRECISE:
			setPrecise((Boolean) newValue);
			return;
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS:
			setFractionDigits((Integer) newValue);
			return;
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__NUMERATOR:
			setNumerator((Double) newValue);
			return;
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__PREFIX:
			setPrefix((String) newValue);
			return;
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__SUFFIX:
			setSuffix((String) newValue);
			return;
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__DELIMITER:
			setDelimiter((String) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__PRECISE:
			unsetPrecise();
			return;
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS:
			unsetFractionDigits();
			return;
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__NUMERATOR:
			unsetNumerator();
			return;
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__PREFIX:
			setPrefix(PREFIX_EDEFAULT);
			return;
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__SUFFIX:
			setSuffix(SUFFIX_EDEFAULT);
			return;
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__DELIMITER:
			unsetDelimiter();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__PRECISE:
			return isSetPrecise();
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS:
			return isSetFractionDigits();
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__NUMERATOR:
			return isSetNumerator();
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__PREFIX:
			return PREFIX_EDEFAULT == null ? prefix != null : !PREFIX_EDEFAULT.equals(prefix);
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__SUFFIX:
			return SUFFIX_EDEFAULT == null ? suffix != null : !SUFFIX_EDEFAULT.equals(suffix);
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER__DELIMITER:
			return isSetDelimiter();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (precise: "); //$NON-NLS-1$
		if (preciseESet)
			result.append(precise);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", fractionDigits: "); //$NON-NLS-1$
		if (fractionDigitsESet)
			result.append(fractionDigits);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", numerator: "); //$NON-NLS-1$
		if (numeratorESet)
			result.append(numerator);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", prefix: "); //$NON-NLS-1$
		result.append(prefix);
		result.append(", suffix: "); //$NON-NLS-1$
		result.append(suffix);
		result.append(", delimiter: "); //$NON-NLS-1$
		if (delimiterESet)
			result.append(delimiter);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience methods provided to create an initialized NumberFormatSpecifier
	 * instance
	 * 
	 * NOTE: Manually written
	 * 
	 * @return this instance
	 */
	public static FractionNumberFormatSpecifier create() {
		final FractionNumberFormatSpecifier nfs = AttributeFactory.eINSTANCE.createFractionNumberFormatSpecifier();
		nfs.setPrecise(true);
		nfs.setFractionDigits(3);
		return nfs;
	}

	@SuppressWarnings("deprecation")
	public String format(double dValue, Locale lo) {
		return format(dValue, ULocale.forLocale(lo));
	}

	public String format(double dValue, ULocale lo) {
		// Convert the decimal value to the fraction
		Fraction fraction = null;
		if (isPrecise()) {
			fraction = FractionApproximator.getExactFraction(dValue);
		} else if (getNumerator() > 0) {
			fraction = FractionApproximator.getFractionWithNumerator(dValue, (int) getNumerator());
		}
		// Using max digits for denominator to calculate
		else {
			fraction = FractionApproximator.getFractionWithMaxDigits(dValue, getFractionDigits());
		}

		// Generate the formatted string
		final StringBuffer sb = new StringBuffer();
		if (getPrefix() != null) {
			sb.append(getPrefix());
		}
		sb.append(fraction.toString(getDelimiter()));
		if (getSuffix() != null) {
			sb.append(getSuffix());
		}
		return sb.toString();
	}

	/**
	 * @generated
	 */
	public FractionNumberFormatSpecifier copyInstance() {
		FractionNumberFormatSpecifierImpl dest = new FractionNumberFormatSpecifierImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(FractionNumberFormatSpecifier src) {

		super.set(src);

		// attributes

		precise = src.isPrecise();

		preciseESet = src.isSetPrecise();

		fractionDigits = src.getFractionDigits();

		fractionDigitsESet = src.isSetFractionDigits();

		numerator = src.getNumerator();

		numeratorESet = src.isSetNumerator();

		prefix = src.getPrefix();

		suffix = src.getSuffix();

		delimiter = src.getDelimiter();

		delimiterESet = src.isSetDelimiter();

	}

} // FractionNumberFormatSpecifierImpl
