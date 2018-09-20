package es.apryso.aprysobarcodeserver.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.apryso.aprysobarcodeserver.entity.Session;
import es.apryso.aprysobarcodeserver.entity.SessionEntry;
import es.apryso.aprysobarcodeserver.repository.SessionEntryRepository;
import es.apryso.aprysobarcodeserver.repository.SessionRepository;

@Service
public class SessionEntryService {
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	private SessionEntryRepository sessionEntryRepository;
	
	
	@Transactional(readOnly=true)
	public List<SessionEntry> findSessionEntries(Long sessionId, Pageable page) {
		
		Session session = sessionRepository.findById(sessionId);
		if (session != null) {
			return sessionEntryRepository.findBySessionOrderByTimestampDesc(session,page);
		} else {
			return new ArrayList<SessionEntry>();
		}
	}
	
	@Transactional(readOnly=true)
	public Integer getSessionEntryCount(Long sessionId) {
		
		Session session = sessionRepository.findById(sessionId);
		if (session != null) {
			return sessionEntryRepository.getSessionEntryCount(session);
		} else {
			return 0;
		}
	}
}
