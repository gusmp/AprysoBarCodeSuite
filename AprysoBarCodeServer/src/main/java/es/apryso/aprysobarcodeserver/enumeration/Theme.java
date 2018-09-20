package es.apryso.aprysobarcodeserver.enumeration;

public enum Theme {
	AIR("skins/air.css"), AIRCOMPACT("skins/aircompact.css"), CLOUDS(
			"skins/clouds.css"), COMPACT("skins/compact.css"), CONTRAST(
			"skins/contrast.css"), FLAT("skins/flat.css"), GLAMOUR(
			"skins/glamour.css"), LIGHT("skins/light.css"), METRO(
			"skins/metro.css"), TERRACE("skins/terrace.css"), TOUCH(
			"skins/touch.css"), WEB("skins/web.css"), WEBIX("webix.css");

	Theme(String css) {
		cssFile = css;
	}

	private String cssFile;

	public String getCssFile() {
		return cssFile;
	}

}
