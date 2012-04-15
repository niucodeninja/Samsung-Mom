/*
 * Copyright (c) 2012 Jorge Osorio <niucodeninja@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any
 * person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice
 * shall be included in all copies or substantial portions of
 * the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.niucodeninja;

import org.apache.commons.validator.EmailValidator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

@SuppressWarnings("deprecation")
public class Validator {

	/**
	 * Verifica una direcci—n de correo electr—nico
	 * 
	 * @param email
	 * @return true o false
	 */
	public static boolean isValidEmail(String email) {
		EmailValidator emailValidator = EmailValidator.getInstance();
		if (emailValidator.isValid(email)) {
			return true;
		}
		return false;
	}

	/**
	 * Validar un nœmero telefonico
	 * 
	 * @param phone_number
	 * @param country
	 * @return
	 */
	public static boolean isValidPhoneNumber(String phone_number, String country) {
		try {
			PhoneNumberUtil.getInstance().parse(phone_number, country);
			return true;
		} catch (NumberParseException e) {
			return false;
		}
	}

	/**
	 * Validar un nombre
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isValidName(String name) {
		if (name.length() >= 2) {
			if (name.matches("[A-Z][a-zA-Z]*")) {
				return true;
			}
		}
		return false;
	}
}
