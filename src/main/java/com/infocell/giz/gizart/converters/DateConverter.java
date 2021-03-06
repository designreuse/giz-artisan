package com.infocell.giz.gizart.converters;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.core.convert.converter.Converter;

public class DateConverter implements Converter<String, LocalDate> {

	@Override
	public LocalDate convert(String strDate) {

		try {
			System.out.println("inside date converter");
			LocalDate dt = LocalDate.parse(strDate, DateTimeFormat.forPattern("dd-MM-yyyy"));
			final DateTimeZone fromTimeZone = DateTimeZone.forID("Africa/Lagos");
			LocalDate ld = new LocalDate(dt, fromTimeZone);

			return ld;

		} catch (Exception e) {
			return null;

		}
	}

}
