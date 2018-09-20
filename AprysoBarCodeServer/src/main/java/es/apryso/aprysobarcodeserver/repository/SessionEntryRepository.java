package es.apryso.aprysobarcodeserver.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import es.apryso.aprysobarcodeserver.entity.Session;
import es.apryso.aprysobarcodeserver.entity.SessionEntry;

public interface SessionEntryRepository extends Repository<SessionEntry, Integer> {
	
	public SessionEntry save(SessionEntry sessionEntry);
	public SessionEntry findById(SessionEntry sessionEntryId);
	public void delete(SessionEntry sessionEntry);
	
	@Query("SELECT se FROM SessionEntry se WHERE se.timestamp=:entry_timestamp AND se.session.timestamp=:session_timestamp AND se.session.device.deviceId=:deviceId")
	public SessionEntry findSessionEntry(@Param(value="entry_timestamp") Date entryTimestamp, 
			@Param(value="session_timestamp") Date sessionTimestamp,
			@Param(value="deviceId") String deviceId);
	
	
	public List<SessionEntry> findBySessionOrderByTimestampDesc(Session session,Pageable page);
	
	@Query("SELECT COUNT(se.id) FROM SessionEntry se WHERE se.session=:session")
	public Integer getSessionEntryCount(@Param(value="session") Session session);

	
}
