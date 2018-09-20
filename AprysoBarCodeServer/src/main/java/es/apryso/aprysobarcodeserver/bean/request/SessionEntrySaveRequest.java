package es.apryso.aprysobarcodeserver.bean.request;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionEntrySaveRequest {

	private String manufacturer;
	private String model;
	private String deviceId;
	
	private Long sessionTimestamp;
	
	private String barcodeFormat;
	private String content;
	private Long entryTimestamp;
	private Integer numberOfItems;
	
	
	@Override
	public String toString() {

		return "manufacturer: " + this.manufacturer + " " +
				"model: " + this.model + " " +
				"deviceId: " + this.deviceId + " " +
				"sessionTimestamp: " + new Date(this.sessionTimestamp).toString() + " " + 
				"barcodeFormat: " + this.barcodeFormat + " " +
				"content: " + this.content + " " +
				"numberOfItems: " + this.numberOfItems + " " + 
				"sessionEntryTimestamp: " + new Date(this.entryTimestamp).toString(); 
	}
}
