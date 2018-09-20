package es.apryso.aprysobarcodeserver.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.apryso.aprysobarcodeserver.bean.response.GetSessionEntriesResponse;
import es.apryso.aprysobarcodeserver.bean.response.SessionEntryItemResponse;
import es.apryso.aprysobarcodeserver.entity.SessionEntry;
import es.apryso.aprysobarcodeserver.service.SessionEntryService;


@Controller
@RequestMapping(value="/sessionEntry")
public class SessionEntryController {

	private final int PAGE_SIZE = 20;

	
	@Autowired
	private SessionEntryService sessionEntryService;
	
	
	
	@RequestMapping("/getEntries/{sessionId}")
	@ResponseBody
	public GetSessionEntriesResponse getSessions(@PathVariable Long sessionId,
			@RequestParam(required=false,name="start") Integer pageStart,
			@RequestParam(required=false) Integer count,
			@RequestParam(required=false) Boolean continueParam) {
		
		GetSessionEntriesResponse response = new GetSessionEntriesResponse();
		List<SessionEntryItemResponse> responseSessionItemlist = new ArrayList<SessionEntryItemResponse>();
		List<SessionEntry> sessionEntryList;
		
		if (pageStart == null) {
			
			sessionEntryList = sessionEntryService.findSessionEntries(sessionId,PageRequest.of(0, PAGE_SIZE));
			for(SessionEntry se : sessionEntryList) {
				responseSessionItemlist.add(new SessionEntryItemResponse(se));
			} 

			response.setPos(0);
			response.setTotal_count(sessionEntryService.getSessionEntryCount(sessionId));
			
		} else {
			
			sessionEntryList = sessionEntryService.findSessionEntries(sessionId,PageRequest.of(pageStart / PAGE_SIZE, PAGE_SIZE));
			for(SessionEntry se : sessionEntryList) {
				responseSessionItemlist.add(new SessionEntryItemResponse(se));
			}
			
			response.setPos(pageStart);
			response.setTotal_count(null);
			
		}
		
		response.setData(responseSessionItemlist);
		
		return response;
	}

}
