package com.zyin.zyinhud.util;

import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Localization {
	/**
	 * Gets the local text of your translation based on the given key.
	 * This will look through your mod's translation file that was previously registered.
	 *
	 * @param key E.x.: tile.block.name
	 * @return The translated string or the default English (en_US) translation if none was found.
	 */
	@Nonnull
	public static String get(String key) {
		return get(key, (Object) null);
	}

	/**
	 * Gets the local text of your translation based on the given key.
	 * This will look through your mod's translation file that was previously registered.
	 *
	 * @param key  E.x.: tile.block.name
	 * @param args args parameters for {@code String.format}
	 * @return The translated string or the default English (en_US) translation if none was found.
	 */
	@Nonnull
	public static String get(String key, Object... args) {
		return I18n.format(key, args);
	}

	/**
	 * <p>As necessary, changes the first character of each whitespace delimited "word" in the
	 * <tt>String</tt> parameter <tt>str</tt> to its Unicode titlecase equivalent (normally
	 * equivalent to the uppercase form of the character), if one exists.<br>
	 *
	 * Does not modify any other characters in the <tt>String</tt></p><br>
	 *
	 * <p>This serves to replace the use of the single-parameter <tt>capitalize</tt> method from the deprecated
	 * {@link org.apache.commons.lang3.text.WordUtils} class</p>
	 *
	 * @param str The string to convert
	 * @return <tt>null</tt> if the <tt>String</tt> parameter is <tt>null</tt>;
	 *         otherwise, the converted <tt>String</tt>, with the first character of each "word" in Unicode titlecase.
	 */
	// This suppression is just to make the static analysis shut up about linking to a deprecated API in a Javadoc
	@SuppressWarnings("deprecation")
	@Nullable
	public static String toTitleCase(String str) {
		if (str == null || str.trim().isEmpty()) { return str; }

		final char[] old = str.toCharArray();
		old[0] = Character.toTitleCase(old[0]);
		for (int i = 1; i < old.length; i++) {
			if (Character.isWhitespace(old[i - 1])) {
				old[i] = Character.toTitleCase(old[i]);
			}
		}
		return new String(old);
	}
}