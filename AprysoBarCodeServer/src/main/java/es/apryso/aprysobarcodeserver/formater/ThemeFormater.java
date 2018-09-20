package es.apryso.aprysobarcodeserver.formater;

import java.text.ParseException;
import java.util.Locale;
import es.apryso.aprysobarcodeserver.enumeration.Theme;
import org.springframework.format.Formatter;

public class ThemeFormater implements Formatter<Theme> {

	@Override
	public String print(Theme theme, Locale arg1) {
		
		if (theme == null) {
			return Theme.WEBIX.name();
		}
		
		return theme.name();

	}

	@Override
	public Theme parse(String theme, Locale arg1) throws ParseException {

		if (theme == null) return Theme.WEBIX;
		else return Theme.valueOf(theme);
	}
	
	

}
