package es.apryso.aprysobarcodeserver.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import es.apryso.aprysobarcodeserver.entity.Session;

public interface SessionRepository extends Repository<Session, Integer> {
	
	public Session save(Session session);
	public Session findById(Long sessionId);
	public void delete(Session session);
	
	@Query("SELECT s FROM Session s WHERE s.timestamp=:timestamp AND s.device.deviceId=:deviceId")
	public Session findSession(@Param(value="timestamp") Date timestamp, @Param(value="deviceId") String deviceId);
	
	public List<Session> findAllByOrderByTimestampDesc(Pageable page);
	
	@Query("SELECT COUNT(s.id) FROM Session s")
	public Integer getSessionCount();
		
}
