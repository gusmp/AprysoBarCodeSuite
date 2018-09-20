package es.apryso.aprysobarcodeserver.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.apryso.aprysobarcodeserver.bean.request.SessionEntrySaveRequest;
import es.apryso.aprysobarcodeserver.bean.response.SaveEntryResponse;
import es.apryso.aprysobarcodeserver.bean.response.StatusResponse;
import es.apryso.aprysobarcodeserver.service.SessionService;

@RestController
@RequestMapping(value="device")
@Slf4j
public class DeviceController {
	
	@Autowired
	private SessionService sessionService;
	
	// http://localhost:8081/aprysobarcodeserver/device/status
	
	@GetMapping(value = "/status", produces = "application/json")
	public StatusResponse status() {

		StatusResponse response = new StatusResponse();
		
		try {
			sessionService.getSessionCount();
			response.setStatus("OK");
		} catch (Exception exc) {
			response.setStatus("FAIL");
		}
		
		return response;
	}
	
	@PostMapping(value="/save", produces = "application/json")
	public SaveEntryResponse save(@RequestBody SessionEntrySaveRequest sessionEntrySaveRequest) {

		System.out.println(sessionEntrySaveRequest.toString());
		SaveEntryResponse response = new SaveEntryResponse();
		try {
			sessionService.save(sessionEntrySaveRequest);
			response.setStatus("OK");
		} catch(Exception exc) {
			response.setStatus("FAIL");
			log.error("Error saving request: " + exc.toString());
			log.error("Request: " + sessionEntrySaveRequest.toString());
		}
		return response;
	}
}
