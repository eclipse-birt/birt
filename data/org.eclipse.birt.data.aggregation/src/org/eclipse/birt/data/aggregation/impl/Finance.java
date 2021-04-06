/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.aggregation.impl;

import java.lang.reflect.Array;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.MathUtil;
import org.eclipse.birt.data.aggregation.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Finance.
 * 
 * JRP provides a class that provides a set of static methods that provide a
 * wide range of financial function. Class Finance provides a set of static
 * financial functions
 * 
 * @version $Revision: 1.3 $ $Date: 2008/04/24 09:33:20 $
 */
public class Finance {

	/**
	 * The application cannot create an instance of this class
	 */
	private Finance() {

	}

	/**
	 * @param Cost    is the initial cost of the asset.
	 * @param salvage is the value at the end of the depreciation (sometimes called
	 *                the salvage value of the asset).
	 * @param life    is the number of periods over which the asset is being
	 *                depreciated (sometimes called the useful life of the asset).
	 * @param period  is the period for which you want to calculate the
	 *                depreciation. Period must use the same units as life.
	 * 
	 * @return the depreciation of an asset for a given , single period using the
	 *         double-declning balance method Remarks
	 * 
	 *         The double-declining balance method computes depreciation at an
	 *         accelerated rate. Depreciation is highest in the first period and
	 *         decreases in successive periods. DDB uses the following formula to
	 *         calculate depreciation for a period: ((cost-salvage) - total
	 *         depreciation from prior periods) * (factor/life)
	 * 
	 *         Change factor if you do not want to use the double-declining balance
	 *         method. Use the VDB function if you want to switch to the
	 *         straight-line depreciation method when depreciation is greater than
	 *         the declining balance calculation.
	 */
	public static double ddb(double cost, double salvage, double life, int period) throws DataException {

		double rate;
		double prior = 0; /* Depreciation in prior period */
		double basis; /* The basis for this cycle, the underpreciated value */
		double depr = 0; /* The depreciation we calculate for current cycle */

		if (life <= 0 || salvage < 0 || cost <= 0 || period <= 0 || Double.isNaN(life) || Double.isNaN(salvage)
				|| Double.isNaN(cost) || Double.isNaN(period)) {
			throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "DDB")); //$NON-NLS-1$
		}

		rate = (1 / life) * 2;

		for (int x = 0; x < period; x++) {
			basis = cost - prior;
			depr = Math.min(basis - salvage, basis * rate);
			prior += depr;
		}
		return depr;
	}

	/**
	 * @param cost    is the initial cost of the asset.
	 * @param salvage is the value at the end of the depreciation (sometimes called
	 *                the salvage value of the asset).
	 * @param life    is the number of periods over which the asset is depreciated
	 *                (sometimes called the useful life of the asset).
	 * @return the straight-line depreciation of an asset for a single period
	 */
	public static double sln(double cost, double salvage, double life) throws DataException {
		if (life <= 0 || salvage < 0 || cost <= 0) {
			throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "SLN")); //$NON-NLS-1$

		}
		return ((cost - salvage) / life);
	}

	/**
	 * @param cost    is the initial cost of the asset.
	 * @param salvage is the value at the end of the depreciation (sometimes called
	 *                the salvage value of the asset).
	 * @param life    is the number of periods over which the asset is depreciated
	 *                (sometimes called the useful life of the asset).
	 * @param period  is the period and must use the same units as life.
	 * @return sum-of-years'-digits deprciation of an asset for a specified period
	 *         Remark
	 * 
	 *         SYD is calculated as follows:
	 *         SYD=(cost-salvage)*(life-per+1)*2/(life*(life+1))
	 * 
	 */
	public static double syd(double cost, double salvage, double life, int period) throws DataException {

		int denom = 0;
		int i;
		double rate;
		double depr = 0; /* The depreciation we calculate for current cycle */

		if (life <= 0 || salvage < 0 || cost <= 0 || period <= 0) {
			throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "SYD")); //$NON-NLS-1$
		}

		/* Compute denominator */
		for (i = 1; i <= life; i++)
			denom += i;

		for (int x = 1; x <= period; x++) {
			rate = (life - x + 1) / denom;
			depr = Math.min(cost - salvage, (cost - salvage) * rate);
		}

		return depr;
	}

	/**
	 * @param rate is the interest rate per period.
	 * @param nPer is the total number of payment periods in an annuity.
	 * @param pmt  is the payment made each period; it cannot change over the life
	 *             of the annuity. Typically, pmt contains principal and interest
	 *             but no other fees or taxes. If pmt is omitted, you must include
	 *             the pv argument.
	 * @param pv   is the present value, or the lump-sum amount that a series of
	 *             future payments is worth right now. If pv is omitted, it is
	 *             assumed to be 0 (zero), and you must include the pmt argument.
	 * @param due  is the number 0 or 1 and indicates when payments are due. If type
	 *             is omitted, it is assumed to be 0.
	 * @return the future value of an annuity based on periodic , constant
	 *         payments,and on an unvarying interest rate Make sure that you are
	 *         consistent about the units you use for specifying rate and nper.
	 * 
	 *         Remarks Make sure that you are consistent about the units you use for
	 *         specifying rate and nper. If you make monthly payments on a four-year
	 *         loan at 12 percent annual interest, use 12%/12 for rate and 4*12 for
	 *         nper. If you make annual payments on the same loan, use 12% for rate
	 *         and 4 for nper. For all the arguments, cash you pay out, such as
	 *         deposits to savings, is represented by negative numbers; cash you
	 *         receive, such as dividend checks, is represented by positive numbers.
	 * 
	 * 
	 */
	public static double fv(double rate, int nPer, double pmt, double pv, int due) throws DataException {

		double fv = 0;
		int start = 0;
		int end = 0;
		if (rate < 0 || nPer < 0 || (due != 1 && due != 0)) {
			throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "fv")); //$NON-NLS-1$
		}

		switch (due) {
		case 1:
			start = 1;
			end = nPer;
			break;
		case 0:
			start = 0;
			end = nPer - 1;
			break;
		default:

			throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "fv")); //$NON-NLS-1$
		}

		for (int i = start; i <= end; i++)
			fv += Math.pow((1 + rate), (double) i);

		fv = fv * pmt;

		if (pv != 0) {
			fv += pv * Math.pow((1 + rate), nPer);
		}
		return (0 - fv);
	}

	/**
	 * @param rate is the interest rate per period.
	 * @param nPer is the total number of payment periods in an annuity.
	 * @param pv   is the present value, or the lump-sum amount that a series of
	 *             future payments is worth right now. If pv is omitted, it is
	 *             assumed to be 0 (zero), and you must include the pmt argument.
	 * @param due  is the number 0 or 1 and indicates when payments are due. If type
	 *             is omitted, it is assumed to be 0.
	 * @return the payment for an annuity, based on periodic , constant payments,
	 *         and on an unvarying interest rate.
	 * 
	 *         The payment returned by PMT includes principal and interest but no
	 *         taxes, reserve payments, or fees sometimes associated with loans.
	 *         Make sure that you are consistent about the units you use for
	 *         specifying rate and nper. If you make monthly payments on a four-year
	 *         loan at an annual interest rate of 12 percent, use 12%/12 for rate
	 *         and 4*12 for nper. If you make annual payments on the same loan, use
	 *         12 percent for rate and 4 for nper.
	 * 
	 */
	public static double pmt(double rate, int nper, double pv, double fv, int due) throws DataException {
		int start_pv = 0;
		int end_pv = 0;
		int start_fv = 0;
		int end_fv = 0;
		int t; // Time period
		double pmt = 0;
		double df_pv = 0; // Discount factor to apply against present value
		double df_fv = 0; // Discount factor to apply against future value

		switch (due) {
		case 0:// each payment is made at the end
			start_pv = 1;
			end_pv = nper;
			start_fv = 0;
			end_fv = nper - 1;
			break;
		case 1:// each payment is made at the beginning
			start_pv = 0;
			end_pv = nper - 1;
			start_fv = 1;
			end_fv = nper;
			break;
		default:
			throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "pmt")); //$NON-NLS-1$
		}

		/*
		 * Calculate the discount factor that gives you payment from present value
		 */
		for (t = start_pv; t <= end_pv; t++) {
			double curr = Math.pow(1 + rate, t);
			df_pv += 1 / curr;
		}

		/*
		 * Calculate the discount factor that gives you payment from future value
		 */
		for (t = start_fv; t <= end_fv; t++) {
			double curr = Math.pow((1 + rate), t);
			df_fv += curr;
		}

		pmt = pv / df_pv + fv / df_fv;
		return -pmt;
	}

	/**
	 * @param rate is the interest rate per period.
	 * @param nPer is the total number of payment periods in an annuity.
	 * @param pv   is the present value, or the lump-sum amount that a series of
	 *             future payments is worth right now. If pv is omitted, it is
	 *             assumed to be 0 (zero), and you must include the pmt argument.
	 * @param due  is the number 0 or 1 and indicates when payments are due. If type
	 *             is omitted, it is assumed to be 0.
	 * @param per  is the period for which you want to find the interest and must be
	 *             in the range 1 to nper.
	 * @param fv   is the future value, or a cash balance you want to attain after
	 *             the last payment is made. If fv is omitted, it is assumed to be 0
	 *             (the future value of a loan, for example, is 0).
	 * @return the interest payment for a given period of an annuity, based on
	 *         periodic , constant payments, and on an unvarying interest rate
	 * 
	 *         Remarks Make sure that you are consistent about the units you use for
	 *         specifying rate and nper. If you make monthly payments on a four-year
	 *         loan at 12 percent annual interest, use 12%/12 for rate and 4*12 for
	 *         nper. If you make annual payments on the same loan, use 12% for rate
	 *         and 4 for nper. For all the arguments, cash you pay out, such as
	 *         deposits to savings, is represented by negative numbers; cash you
	 *         receive, such as dividend checks, is represented by positive numbers.
	 */
	public static double ipmt(double rate, int per, int nPer, double pv, double fv, int due) throws DataException {
		double pmt;
		double ipmt = 0;
		double principal;
		double ppmt = 0;
		if (rate < 0 || nPer < 0 || per > nPer || (due != 1 && due != 0)) {

			throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "IPMT")); //$NON-NLS-1$
		}
		pmt = Math.abs(Finance.pmt(rate, nPer, pv, fv, due));
		principal = Math.abs(pv);

		for (int curper = 1; curper <= per; curper++) {
			if (curper != 1 || due == 0)
				ipmt = rate * principal;
			ppmt = pmt - ipmt;
			principal = principal - ppmt;
		}

		if (pv > 0 || (pv == 0 && fv > 0)) {
			ipmt *= -1;
		}
		return ipmt;

	}

	/**
	 * @param rate is the interest rate per period.
	 * @param nPer is the total number of payment periods in an annuity.
	 * @param pv   is the present value, or the lump-sum amount that a series of
	 *             future payments is worth right now. If pv is omitted, it is
	 *             assumed to be 0 (zero), and you must include the pmt argument.
	 * @param due  is the number 0 or 1 and indicates when payments are due. If type
	 *             is omitted, it is assumed to be 0.
	 * @param per  is the period for which you want to find the interest and must be
	 *             in the range 1 to nper.
	 * @param fv   is the future value, or a cash balance you want to attain after
	 *             the last payment is made. If fv is omitted, it is assumed to be 0
	 *             (the future value of a loan, for example, is 0).
	 * @return the principal payment for a given period of an annuity, based on
	 *         periodic, constant payments, and on an unvarying interest rate
	 * 
	 *         Remark Make sure that you are consistent about the units you use for
	 *         specifying rate and nper. If you make monthly payments on a four-year
	 *         loan at 12 percent annual interest, use 12%/12 for rate and 4*12 for
	 *         nper. If you make annual payments on the same loan, use 12% for rate
	 *         and 4 for nper.
	 * 
	 */
	public static double ppmt(double rate, int per, int nPer, double pv, double fv, int due) throws DataException {
		double pmt;
		double ipmt = 0;
		double principal;
		double ppmt = 0;
		if (rate < 0 || nPer < 0 || per > nPer || (due != 1 && due != 0)) {
			throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "PPMT")); //$NON-NLS-1$
		}
		pmt = Math.abs(Finance.pmt(rate, nPer, pv, fv, due));
		principal = Math.abs(pv);

		for (int curper = 1; curper <= per; curper++) {
			if (curper != 1 || due == 0)
				ipmt = rate * principal;
			ppmt = pmt - ipmt;
			principal = principal - ppmt;
		}

		if (pv > 0 || (pv == 0 && fv > 0)) {
			ppmt *= -1;
		}
		return ppmt;

	}

	/**
	 * *
	 * 
	 * @param rate is the interest rate per period.
	 * @param pv   is the present value, or the lump-sum amount that a series of
	 *             future payments is worth right now. If pv is omitted, it is
	 *             assumed to be 0 (zero), and you must include the pmt argument.
	 * @param due  is the number 0 or 1 and indicates when payments are due. If type
	 *             is omitted, it is assumed to be 0.
	 * @param fv   is the future value, or a cash balance you want to attain after
	 *             the last payment is made. If fv is omitted, it is assumed to be 0
	 *             (the future value of a loan, for example, is 0).
	 * @param pmt  is the payment made each period; it cannot change over the life
	 *             of the annuity. Typically, pmt contains principal and interest
	 *             but no other fees or taxes.
	 * @return the number of periods for an annuity based on periodic , constant
	 *         payments, and on an unvarying interest rate
	 */
	public static int nPer(double rate, double pmt, double pv, double fv, int due) throws DataException {

		if (rate < 0 || pmt == 0 || (due != 1 && due != 0) || ((Math.abs(pv) * rate) >= Math.abs(pmt))
				|| ((Math.abs(fv) * rate) >= Math.abs(pmt))) {
			throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "NPER")); //$NON-NLS-1$
		}

		int FvPeriod = 0;
		int PvPeriod = 0;
		int nPer = 0;
		double currentvalue = 0;

		if (pv != 0) {

			if (pv * pmt < 0) // Loan
			{
				currentvalue = Math.abs(pv);
				if (due == 1)
					currentvalue -= currentvalue * rate;

				while (currentvalue > 0) {
					currentvalue += currentvalue * rate;
					currentvalue -= Math.abs(pmt);
					PvPeriod++;
				}
			} else
			// Saving
			{
				currentvalue = Math.abs(pmt);
				if (due == 1)
					pv = pv * (1.0 + rate);

				while (currentvalue < Math.abs(pv)) {
					currentvalue += currentvalue * rate;
					currentvalue += Math.abs(pmt);
					PvPeriod--;
				}

			}
		}

		if (fv != 0) {
			if (fv * pmt < 0) // Saving
			{
				currentvalue = Math.abs(pmt);
				if (due == 0)
					currentvalue = 0;

				while (currentvalue < Math.abs(fv)) {
					currentvalue += currentvalue * rate;
					currentvalue += Math.abs(pmt);
					FvPeriod++;
				}
			} else
			// Loan
			{
				FvPeriod = 1;
				currentvalue = Math.abs(fv);
				if (due == 1)
					currentvalue -= currentvalue * rate;

				while (currentvalue > 0) {
					currentvalue += currentvalue * rate;
					currentvalue -= Math.abs(pmt);
					FvPeriod--;
				}
			}
		}
		nPer = PvPeriod + FvPeriod;
		return nPer;
	}

	/**
	 * @param rate is the interest rate per period.
	 * @param due  is the number 0 or 1 and indicates when payments are due. If type
	 *             is omitted, it is assumed to be 0.
	 * @param fv   is the future value, or a cash balance you want to attain after
	 *             the last payment is made. If fv is omitted, it is assumed to be 0
	 *             (the future value of a loan, for example, is 0).
	 * @param pmt  is the payment made each period; it cannot change over the life
	 *             of the annuity. Typically, pmt contains principal and interest
	 *             but no other fees or taxes.
	 * @param nPer is the total number of payment periods in an annuity. For
	 *             example, if you get a four-year car loan and make monthly
	 *             payments, your loan has 4*12 (or 48) periods. You would enter 48
	 *             into the formula for nper.
	 * @return the present value of an annuity based on periodic, constant payments
	 *         to be paid in the future,and on an unvarying interest rate
	 * 
	 *         Remarks Make sure that you are consistent about the units you use for
	 *         specifying rate and nper. If you make monthly payments on a four-year
	 *         loan at 12 percent annual interest, use 12%/12 for rate and 4*12 for
	 *         nper. If you make annual payments on the same loan, use 12% for rate
	 *         and 4 for nper. The following functions apply to annuities:
	 */
	public static double pv(double rate, int nPer, double pmt, double fv, int due) throws DataException {

		double denom;
		double num;
		double pv = 0;
		if (nPer < 0 || (due != 1 && due != 0)) {
			throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "pv")); //$NON-NLS-1$
		}

		if (rate < 0) {
			pv = -fv - (pmt * nPer);
		} else {
			denom = Math.pow((1 + rate), (double) nPer);
			num = -fv - pmt * (1 + (rate * due)) * (denom - 1) / rate;
			pv = num / denom;
		}

		return pv;
	}

	/**
	 * @param rate     is the interest rate per period.
	 * @param due      is the number 0 or 1 and indicates when payments are due. If
	 *                 type is omitted, it is assumed to be 0.
	 * @param fv       is the future value, or a cash balance you want to attain
	 *                 after the last payment is made. If fv is omitted, it is
	 *                 assumed to be 0 (the future value of a loan, for example, is
	 *                 0).
	 * @param nPer     is the total number of payment periods in an annuity. For
	 *                 example, if you get a four-year car loan and make monthly
	 *                 payments, your loan has 4*12 (or 48) periods. You would enter
	 *                 48 into the formula for nper.
	 * @param true_pmt
	 * @param loanamt
	 * @param incr
	 * @param attempt  is the number we have failed
	 * @param found    is only a tag
	 * @return The function is the basis of rate()
	 */
	static private double solvePmt(int nPer, double true_pmt, double loanamt, double fv, int due, double rate,
			double incr, int attempt, boolean found) {
		try {
			double tolerance;
			double diff1;
			double diff2;

			tolerance = .0000001; /*
									 * this will provide precision past the 3rd decimal
									 */

			if (attempt > 1000) {
				found = false;
				return -1;
			}

			diff1 = true_pmt - pmt(rate + incr, nPer, loanamt, fv, due);

			if (Math.abs(diff1) <= tolerance) {
				found = true;
				return (rate + incr);
			} else {
				diff2 = true_pmt - pmt(rate, nPer, loanamt, fv, due);
				if (Math.abs(diff2) <= tolerance) {
					found = true;
					return (rate);
				}
				if (diff1 * diff2 < 0)
					incr /= 10;
				else
					rate += incr;
				return (solvePmt(nPer, true_pmt, loanamt, fv, due, rate, incr, ++attempt, found));
			}
		} catch (Exception e) {
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * @param due   is the number 0 or 1 and indicates when payments are due. If
	 *              type is omitted, it is assumed to be 0.
	 * @param fv    is the future value, or a cash balance you want to attain after
	 *              the last payment is made. If fv is omitted, it is assumed to be
	 *              0 (the future value of a loan, for example, is 0).
	 * @param pmt   is the payment made each period; it cannot change over the life
	 *              of the annuity. Typically, pmt contains principal and interest
	 *              but no other fees or taxes.
	 * @param nPer  is the total number of payment periods in an annuity. For
	 *              example, if you get a four-year car loan and make monthly
	 *              payments, your loan has 4*12 (or 48) periods. You would enter 48
	 *              into the formula for nper.
	 * @param pv    is the present value - the total amount that a series of future
	 *              payments is worth now.
	 * @param guess is your guess for what the rate will be. If you omit guess, it
	 *              is assumed to be 10 percent. If RATE does not converge, try
	 *              different values for guess. RATE usually converges if guess is
	 *              between 0 and 1.
	 * @return the interest rate per period for an annuity
	 * 
	 *         Remark
	 * 
	 *         Make sure that you are consistent about the units you use for
	 *         specifying guess and nper. If you make monthly payments on a
	 *         four-year loan at 12 percent annual interest, use 12%/12 for guess
	 *         and 4*12 for nper. If you make annual payments on the same loan, use
	 *         12% for guess and 4 for nper.
	 * 
	 */
	public static double rate(int nPer, double pmt, double pv, double fv, int due, double guess) throws DataException {
		try {
			int i;
			double tmp;
			double curr_guess;
			double incr = .1;
			boolean found = true;

			double result = 0;

			if (nPer <= 0 || (due != 1 && due != 0)) {
				if (nPer <= 0) {
					throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "rate")); //$NON-NLS-1$
				}

				else {
					throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "rate")); //$NON-NLS-1$
				}
			}

			/*
			 * Try successive guesses at interest rate, 1st with positive increment, then
			 * negative
			 */

			i = 5;
			curr_guess = guess;
			for (i--; i > 0; i--) {
				incr = .1;
				tmp = curr_guess;
				try {
					result = solvePmt(nPer, pmt, pv, fv, due, tmp, incr, 0, found);
				} catch (ArrayIndexOutOfBoundsException e) {
					throw DataException.wrap(new AggrException(ResourceConstants.BAD_PARAM_COUNT, e));
				}
				if (result != -1)
					return result;

				curr_guess = curr_guess / 2;
			}

			i = 4;
			curr_guess = guess * 2;
			for (i--; i > 0; i--) { // Scale the guess up and try again.
				incr = .1;
				tmp = curr_guess;

				result = solvePmt(nPer, pmt, pv, fv, due, tmp, incr, 0, found);
				if (result != -1)
					return result;

				curr_guess = curr_guess * 2;
			}

			i = 5;
			curr_guess = guess;
			for (i--; i > 0; i--) { // Scale the guess down and try again.
				incr = -.1;
				tmp = curr_guess;

				result = solvePmt(nPer, pmt, pv, fv, due, tmp, incr, 0, found);
				if (result != -1)
					return result;

				curr_guess = curr_guess / 2;
			}

			i = 4;
			curr_guess = guess * 2;
			for (i--; i > 0; i--) { // Scale the guess up and try again.
				incr = -.1;
				tmp = curr_guess;

				result = solvePmt(nPer, pmt, pv, fv, due, tmp, incr, 0, found);
				if (result != -1)
					return result;

				curr_guess = curr_guess * 2;
			}
		} catch (ClassCastException e) {
			throw DataException.wrap(new AggrException(ResourceConstants.BAD_PARAM_TYPE, e));
		}

		throw DataException.wrap(new AggrException(ResourceConstants.RESET_RATE));
		// return result;

	}

	/**
	 * @param denom is the denominator
	 * @param num   is the numerator
	 * @return the percentage of two numbers
	 */
	public static double percent(double denom, double num) {

		if (num == 0)
			return 0;
		assert (denom != 0d);
		return (num / denom) * 100;
	}

	/**
	 * @param arptr array of Doubles that specifies the name of an existing array of
	 *              cash flow values. Rule for <casharray>: Array must contain at
	 *              least one positive value (receipt) and one negative value
	 *              (payment)
	 * @param rate  is the rate of discount over the length of one period.
	 * @return the net present value of a varying series of periodic cash flows,
	 *         both positive and negative, at a given interest rate
	 * 
	 *         Remarks
	 * 
	 *         The NPV investment begins one period before the date of the value1
	 *         cash flow and ends with the last cash flow in the list. The NPV
	 *         calculation is based on future cash flows. If your first cash flow
	 *         occurs at the beginning of the first period, the first value must be
	 *         added to the NPV result, not included in the values arguments. For
	 *         more information, see the examples below. If n is the number of cash
	 *         flows in the list of values, the formula for NPV is:
	 * 
	 * 
	 *         NPV is similar to the PV function (present value). The primary
	 *         difference between PV and NPV is that PV allows cash flows to begin
	 *         either at the end or at the beginning of the period. Unlike the
	 *         variable NPV cash flow values, PV cash flows must be constant
	 *         throughout the investment. For information about annuities and
	 *         financial functions, see PV. NPV is also related to the IRR function
	 *         (internal rate of return). IRR is the rate for which NPV equals zero:
	 *         NPV(IRR(...), ...) = 0.
	 * @throws BirtException
	 */
	public static Number npv(Object[] arptr, double rate) throws BirtException {

		long arsize = Array.getLength(arptr);
		Number npv = 0;
		Object dval = 0;

		for (int i = 1; i <= arsize; i++) {
			try {
				dval = Array.get(arptr, i - 1);
			} catch (Exception e) {
				throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "npv")); //$NON-NLS-1$
			}
			npv = MathUtil.add(npv, MathUtil.divide(dval, Math.pow((1 + rate), (double) i)));
		}

		return npv;
	}

	/**
	 * @param cash    array of Doubles that specifies the name of an existing array
	 *                of cash flow values. Rule for <casharray>: Array must contain
	 *                at least one positive value (receipt) and one negative value
	 *                (payment)
	 * @param intrate
	 * @param inc
	 * @param attempt
	 * @return the internal rate of return for a series of periodic cash flows,
	 *         payments and receipts, in an existing array
	 * @throws BirtException
	 */
	private static double calcIrr(Object[] cash, double intrate, double inc, int attempt) throws BirtException {

		double tolerance;
		Number npv1;
		Number npv2;

		tolerance = 0.0000001; /*
								 * this will provide precision past the 3rd decimal
								 */

		// If it cannot determine a result after 1000 iterations, the function
		// fails
		if (attempt > 1000) {
			return -1;
		}

		npv1 = npv(cash, intrate + inc);
		// printf("Int Rate %1.3f generates delta=%1.3f\n",(*intrate +
		// *inc),npv1);

		if (MathUtil.compare(MathUtil.abs(npv1), tolerance) <= 0)
			return (intrate + inc);
		else {
			npv2 = npv(cash, intrate);
			if (MathUtil.compareTo0(MathUtil.multiply(npv2, npv1)) < 0)
				inc /= 10;
			else
				intrate += inc;
			return (calcIrr(cash, intrate, inc, ++attempt));
		}
	}

	/**
	 * Calculate internal rate of return (IRR) using cash flows that occur at
	 * regular intervals, such as monthly or annually. The internal rate of return
	 * is the interest rate received for an investment consisting of payments and
	 * receipts that occur at regular intervals.
	 *
	 * Method: Newton-Raphson technique. Formula: sum(cashFlow(i) / (1 + IRR)^i)
	 *
	 * @param cashFlows       Cash flow values. Must contain at least one negative
	 *                        value (cash paid) and one positive value (cash
	 *                        received).
	 * @param estimatedResult Optional guess as start value (default: 0.1 = 10%; if
	 *                        value is negative: 0.5). As the formula to calculate
	 *                        IRRs can have multiple solutions, an estimated result
	 *                        (guess) can help find the result we are looking for.
	 * @return Internal rate of return (0.25 = 25%) or Double.NaN if IRR not
	 *         computable.
	 * @throws BirtException
	 * 
	 */
	static public double irr(final Object[] cashFlows, final double estimatedResult) throws BirtException {

		if (cashFlows == null || cashFlows.length < 2) {
			throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "irr")); //$NON-NLS-1$
		}
		int cashFlowsCount = cashFlows.length;
		// check if business startup costs is not zero:
		if (MathUtil.compareTo0(cashFlows[0]) != 0) {
			Number sumCashFlows = 0.0;
			// check if at least 1 positive and 1 negative cash flow exists:
			int numOfNegativeCashFlows = 0;
			int numOfPositiveCashFlows = 0;
			for (int i = 0; i < cashFlowsCount; i++) {
				sumCashFlows = MathUtil.add(sumCashFlows, cashFlows[i]);
				if (MathUtil.compareTo0(cashFlows[i]) > 0) {
					numOfPositiveCashFlows++;
				} else if (MathUtil.compareTo0(cashFlows[i]) < 0) {
					numOfNegativeCashFlows++;
				}
			}

			// at least 1 negative and 1 positive cash flow available?
			if (numOfNegativeCashFlows > 0 && numOfPositiveCashFlows > 0) {
				// set estimated result:
				double irrGuess = 0.1; // default: 10%
				if (!Double.isNaN(estimatedResult)) {
					if (estimatedResult >= 0) {
						irrGuess = estimatedResult;
					} else {
						irrGuess = 0.5;
					}
				} else {
					throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "irr")); //$NON-NLS-1$
				}
				// initialize first IRR with estimated result:
				double irr;
				if (MathUtil.compareTo0(sumCashFlows) < 0) { // sum of cash flows negative?
					irr = -irrGuess;
				} else { // sum of cash flows not negative
					irr = irrGuess;
				}

				// iteration:
				// the smaller the distance, the smaller the interpolation
				// error
				final double minDistance = 1E-15;

				// business startup costs
				final Object cashFlowStart = cashFlows[0];
				final int maxIteration = 50;
				boolean highValueGap = false;
				Number cashValue = 0.0;
				for (int i = 0; i <= maxIteration; i++) {
					// calculate cash value with current irr
					cashValue = MathUtil.toNumber(cashFlowStart); // initialized with startup
					// costs

					// for each cash flow
					for (int j = 1; j < cashFlowsCount; j++) {
						cashValue = MathUtil.add(cashValue, MathUtil.divide(cashFlows[j], Math.pow(1.0 + irr, j)));
					}

					// cash value is close to zero
					if (MathUtil.compare(MathUtil.abs(cashValue), 1E-7) <= 0) {
						return irr;
					}

					// adjust irr for next iteration:
					// cash value > 0 => next irr > current irr
					if (MathUtil.compare(cashValue, 0.0) > 0) {
						if (highValueGap) {
							irrGuess /= 2;
						}

						irr += irrGuess;

						if (highValueGap) {
							irrGuess -= minDistance;
							highValueGap = false;
						}

					} else {// cash value < 0 => next irr < current irr
						irrGuess /= 2;
						irr -= irrGuess;
						highValueGap = true;
					}

					// estimated result too small to continue => end
					// calculation
					if (irrGuess <= minDistance && MathUtil.compare(MathUtil.abs(cashValue), 1E-7) <= 0) {
						return irr;
					}
				}
			}
		}
		return Double.NaN; // $NON-NLS-1$
	}

	/**
	 * @param arptr
	 * @param rate
	 * @return Positive NPV for MIRR function
	 * @throws BirtException
	 */
	private static Number calcPNPV(Object[] arptr, long arsize, double rate) throws BirtException {
		Number npv = 0;
		for (int i = 1; i <= arsize; i++) {
			Object dval = Array.get(arptr, i - 1);

			if (MathUtil.compareTo0(dval) >= 0)
				npv = MathUtil.add(npv, MathUtil.divide(dval, Math.pow((1 + rate), (double) i)));
		}
		return npv;
	}

	/**
	 * @param arptr
	 * @param rate
	 * @return Negative NPV for MIRR function
	 * @throws BirtException
	 */
	private static Number calcNNPV(Object[] arptr, long arsize, double rate) throws BirtException {
		Number npv = 0;
		for (int i = 1; i <= arsize; i++) {
			Object dval = Array.get(arptr, i - 1);
			if (MathUtil.compareTo0(dval) < 0)
				npv = MathUtil.add(npv, MathUtil.divide(dval, Math.pow((1 + rate), (double) i)));
		}
		return npv;
	}

	/**
	 * @param arptr array of Doubles that specifies the name of an existing array of
	 *              cash flow values
	 * @param frate is the interest rate you pay on the money used in the cash
	 *              flows.
	 * @param rrate is the interest rate you receive on the cash flows as you
	 *              reinvest them.
	 * @return the modified internal rate of return for a series of periodic cash
	 *         flows (payments and receipts) in an existing array
	 * 
	 *         Remarks
	 * 
	 *         MIRR uses the order of values to interpret the order of cash flows.
	 *         Be sure to enter your payment and income values in the sequence you
	 *         want and with the correct signs (positive values for cash received,
	 *         negative values for cash paid). If n is the number of cash flows in
	 *         values, frate is the finance_rate, and rrate is the reinvest_rate.
	 * @throws BirtException
	 * 
	 * 
	 */
	public static double mirr(Object[] arptr, double frate, double rrate) throws BirtException {
		long arsize = 0;
		if (arptr != null) {
			arsize = arptr.length;
		}

		if (arptr == null || frate < 0 || rrate < 0) {
			throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "mirr")); //$NON-NLS-1$
		}

		Number PNpv, NNpv;
		long n = arsize;
		double sign = 1.0;

		PNpv = calcPNPV(arptr, n, rrate);
		NNpv = calcNNPV(arptr, n, frate);

		if (MathUtil.compareTo0(NNpv) == 0) {
			throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "mirr")); //$NON-NLS-1$
		}
		Number divider = MathUtil.multiply(MathUtil.negate(PNpv), Math.pow((1.0 + rrate), (double) n));
		Number tmp = MathUtil.divide(divider, MathUtil.multiply(NNpv, 1.0 + frate));

		if (MathUtil.compareTo0(tmp) < 0) {
			sign = -1.0;
		}

		return (sign * (Math.pow(MathUtil.abs(tmp).doubleValue(), (1.0 / (n - 1))) - 1.0));
	}
}
