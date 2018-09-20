package es.apryso.aprysobarcodeserver.service;

import java.util.Date;
import java.util.List;

//import lombok.extern.slf4j.Slf4j;






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.apryso.aprysobarcodeserver.bean.request.SessionEntrySaveRequest;
import es.apryso.aprysobarcodeserver.entity.Device;
import es.apryso.aprysobarcodeserver.entity.Session;
import es.apryso.aprysobarcodeserver.entity.SessionEntry;
import es.apryso.aprysobarcodeserver.repository.SessionEntryRepository;
import es.apryso.aprysobarcodeserver.repository.SessionRepository;

@Service
//@Slf4j
public class SessionService {
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	private SessionEntryRepository sessionEntryRepository;
	
	@Transactional(readOnly=false)
	public Session save(Session session) {
		
		return sessionRepository.save(session);
	}
	
	@Transactional(readOnly=false)
	public Session save(SessionEntrySaveRequest sessionEntrySaveRequest) {
		
		Date sessionTimestamp = new Date(sessionEntrySaveRequest.getSessionTimestamp());
		Session session = sessionRepository.findSession(sessionTimestamp, sessionEntrySaveRequest.getDeviceId());
		
		if (session == null) {
			
			session = new Session();
			session.setTimestamp(sessionTimestamp);
			
			Device device = new Device();
			device.setDeviceId(sessionEntrySaveRequest.getDeviceId());
			device.setManufacturer(sessionEntrySaveRequest.getManufacturer());
			device.setModel(sessionEntrySaveRequest.getModel());
			device.setSession(session);
			
			session.setDevice(device);
		}
		
		Date sessionEntryStimestamp = new Date(sessionEntrySaveRequest.getEntryTimestamp());
		SessionEntry sessionEntry = sessionEntryRepository.findSessionEntry(sessionEntryStimestamp,sessionTimestamp, sessionEntrySaveRequest.getDeviceId());
		
		if (sessionEntry == null) {
			
			sessionEntry = new SessionEntry();
			sessionEntry.setContent(sessionEntrySaveRequest.getContent());
			sessionEntry.setFormat(sessionEntrySaveRequest.getBarcodeFormat());
			sessionEntry.setNumberOfItems(sessionEntrySaveRequest.getNumberOfItems());
			sessionEntry.setSession(session);
			sessionEntry.setTimestamp(sessionEntryStimestamp);
			session.getEntryList().add(sessionEntry);
		}
		
		return sessionRepository.save(session);

	}
	
	
	@Transactional(readOnly=false)
	public void delete(Long sessionId) {
		
		Session session = sessionRepository.findById(sessionId);
		if ( session != null) sessionRepository.delete(session);
	}
	
	@Transactional(readOnly=true)
	public List<Session> findSessions(Pageable page) {
		
		return sessionRepository.findAllByOrderByTimestampDesc(page);
		
	}
	
	@Transactional(readOnly=true)
	public Integer getSessionCount() {
		
		return sessionRepository.getSessionCount();

	} 
	

}
