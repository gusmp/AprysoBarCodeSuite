package es.apryso.aprysobarcodeserver.bean.response;

import java.text.SimpleDateFormat;

import lombok.Getter;
import es.apryso.aprysobarcodeserver.entity.SessionEntry;

@Getter
public class SessionEntryItemResponse {
	
	private Long sessionEntryId;
	private String content;
	private String format;
	private int numberOfItems;
	private String timestamp;
	
	public SessionEntryItemResponse(SessionEntry sessionEntry) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
		
		this.sessionEntryId = sessionEntry.getId();
		
		this.content = sessionEntry.getContent();
		this.format = sessionEntry.getFormat();
		this.numberOfItems = sessionEntry.getNumberOfItems();
		this.timestamp =  sdf.format(sessionEntry.getTimestamp());
	}

}
