package es.apryso.aprysobarcodeserver.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="device")
@Getter
@Setter
public class Device {
	
	@Id
	@GeneratedValue
	@Column(nullable = false)
	private Long id;
	
	private String model;
	
	private String manufacturer;
	
	private String deviceId;
	
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sessionId") 
	private Session session;

}
