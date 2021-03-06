package es.apryso.aprysobarcodeserver.bean.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSessionsResponse extends BaseResponse {
	
	private Integer pos;
	private Integer total_count;
	
	private List<SessionItemResponse> data;

}
