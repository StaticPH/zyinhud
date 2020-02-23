package com.zyin.zyinhud.util;

public interface IAdvancingEnum<E extends Enum<E> & IAdvancingEnum<E>> {
	/**
	 * The ordinal of this enumeration constant (its position
	 * in the enum declaration, where the initial constant is assigned
	 * an ordinal of zero).
	 */
	int ordinal(); //Automatically will be implemented by enum classes, and doesn't require a manual implementation

	/**
	 * Returns the elements of this enum class or null if this
	 * somehow does not represent an enum type.
	 *
	 * @return an array containing the values comprising the enum class
	 * represented by this Class object in the order they're declared,
	 * or null if this somehow does not represent an enum type
	 */
	@SuppressWarnings("unchecked")
	default E[] getValues() {
		//Note: getEnumConstants will be null if IAdvancingEnum is used implemented by something other than an enum; though that shouldn't be allowed
//		if (!(this.getClass().isEnum())){ return null; }
		return (E[]) getClass().getEnumConstants();
	}

	/**
	 * Get the next enumeration constant declared.
	 * If the current constant is the last constant declared, return the first constant.
	 *
	 * @return The next enumeration constant declared
	 */
	default E next() {
		int ordinal = ordinal();
		E[] values = getValues();
		return ordinal() < values.length - 1
		       ? values[ordinal() + 1]
		       : values[0];
	}

	/**
	 * Get the previous enumeration constant declared.
	 * If the current constant is the first constant declared, return the last constant.
	 *
	 * @return The previous enumeration constant declared
	 */
	default E prev() {
		int ordinal = ordinal();
		E[] values = getValues();
		return ordinal() > 0
		       ? values[ordinal() - 1]
		       : values[values.length - 1];
	}

	/**
	 * Convenience method that returns the next or previous element according to a boolean parameter
	 *
	 * @param forward whether to get next or previous element
	 * @return The next element if the <tt>boolean</tt> parameter <tt>forward</tt> is true,
	 * or the previous element if <tt>forward</tt> is false.
	 */
	default E getNextOrPrevious(boolean forward) {
		return forward ? next() : prev();
	}

	/**
	 * Get the enumeration constant declared after <tt>element</tt>.
	 * If <tt>element</tt>> is the last constant declared, return the first constant.
	 *
	 * @param element the element to get the element after
	 * @return The next enumeration constant declared after <tt>element</tt>
	 */
	default E getElementAfter(E element) {
		return element.next();
	}

	/**
	 * Get the enumeration constant declared before <tt>element</tt>.
	 * If <tt>element</tt>> is the first constant declared, return the last constant.
	 *
	 * @param element the element to get the element before
	 * @return The previous enumeration constant declared before <tt>element</tt>
	 */
	default E getElementBefore(E element) {
		return element.prev();
	}
}
