/* 
 * (c) 2010 Alessandro Colantonio
 * <mailto:colanton@mat.uniroma3.it>
 * <http://ricerca.mat.uniroma3.it/users/colanton>
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 


package it.uniroma3.mat.extendedset.wrappers.matrix;

/**
 * A class for representing a single transaction-item relationship. This class
 * is mainly used in {@link PairSet} to iterate over the cells of a
 * binary matrix.
 * 
 * @author Alessandro Colantonio
 * @version $Id: //Actuate/Project-Branches/1584-Remove-Need-4-PreBuild-Cubes/JRP/source/plugins/com.actuate.birt.data.linkeddatamodel/src/it/uniroma3/mat/extendedset/wrappers/matrix/Pair.java#1 $
 * 
 * @param <T>
 *            transaction type
 * @param <I>
 *            item type
 * @see PairSet
 */
public class Pair<T, I> implements java.io.Serializable {
	/** generated ID */
	private static final long serialVersionUID = 328985131584539749L;

	/** the transaction */
	public final T transaction;
	
	/** the item */
	public final I item;

	/**
	 * Creates a new transaction-item pair
	 * 
	 * @param transaction
	 * @param item
	 */
	public Pair(T transaction, I item) {
		this.transaction = transaction;
		this.item = item;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		// 524287 * i = (i << 19) - i, where 524287 is prime.
		// This hash function avoids transactions and items to overlap,
		// since "item" can often stay in 32 - 19 = 13 bits. Therefore, it is
		// better than multiplying by 31.
		final int hi = item.hashCode();
		final int ht = transaction.hashCode();
		return (hi << 19) - hi + ht;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (!(obj instanceof Pair<?, ?>))
			return false;
		@SuppressWarnings("unchecked")
		Pair<T, I> other = (Pair<T, I>) obj;
		return transaction.equals(other.transaction) && item.equals(other.item);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(" + transaction + ", " + item + ")";
	}
}
