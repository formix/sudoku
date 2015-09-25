package org.formix.sudoku;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "com.girfid.sudoku.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key, Object... args) {
		try {
			if (args.length == 0) {
				return RESOURCE_BUNDLE.getString(key);
			} else {
				return String.format(RESOURCE_BUNDLE.getString(key), args);
			}
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
