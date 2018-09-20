package es.apryso.aprysobarcodeserver.bean.response;

import java.text.SimpleDateFormat;

import lombok.Getter;
import es.apryso.aprysobarcodeserver.entity.Session;
import es.apryso.aprysobarcodeserver.entity.SessionEntry;

@Getter
public class SessionItemResponse {
	
	private Long sessionId;
	private String timestamp;
	private String model;
	private String manufacturer;
	private Integer numberOfReads;
	private Integer numberOfReadItems;
	
	public SessionItemResponse(Session session) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
		
		this.sessionId = session.getId();
		this.timestamp = sdf.format(session.getTimestamp());
		this.model = session.getDevice().getModel();
		this.manufacturer = session.getDevice().getManufacturer();
		
		this.numberOfReads = 0;
		this.numberOfReadItems = 0;
		for (SessionEntry se: session.getEntryList()) {
			this.numberOfReads++;
			this.numberOfReadItems += se.getNumberOfItems();
		}
		
	}

}
