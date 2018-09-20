package es.apryso.aprysobarcodeserver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.apryso.aprysobarcodeserver.bean.response.DeleteSessionResponse;
import es.apryso.aprysobarcodeserver.bean.response.GetSessionsResponse;
import es.apryso.aprysobarcodeserver.bean.response.SessionItemResponse;
import es.apryso.aprysobarcodeserver.entity.Session;
import es.apryso.aprysobarcodeserver.enumeration.Theme;
import es.apryso.aprysobarcodeserver.service.SessionService;


@Controller
public class MainController {

	private final int PAGE_SIZE = 20;

	
	@Autowired
	private SessionService sessionService;
	
	
	
	@RequestMapping(value = {"/", "/{theme}"})
	public String mainView(@PathVariable Optional<Theme> theme, Model model) {
		

		if (theme.isPresent() == false) {
			model.addAttribute("themeUrl", "resources/js/webix/" + Theme.AIR.getCssFile());
			model.addAttribute("currentTheme",Theme.AIR.name());
		}
		else {
			switch(theme.get()) {
				case AIR:
				case AIRCOMPACT:
				case CLOUDS:
				case COMPACT:
				case CONTRAST:
				case FLAT:
				case GLAMOUR:
				case LIGHT:
				case METRO:
				case TERRACE:
				case TOUCH:
				case WEB:
					model.addAttribute("themeUrl", "resources/js/webix/" + theme.get().getCssFile());
					model.addAttribute("currentTheme",theme.get().name());
					break;
				default:
					model.addAttribute("themeUrl", "resources/js/webix/" + Theme.WEBIX.getCssFile() );
					model.addAttribute("currentTheme",Theme.WEBIX.name());
			}
		}

		
		return "mainView";
	}
	
	@RequestMapping("/getSessions")
	@ResponseBody
	public GetSessionsResponse getSessions(@RequestParam(required=false,name="start") Integer pageStart,
			@RequestParam(required=false) Integer count,
			@RequestParam(required=false) Boolean continueParam) {
		
		GetSessionsResponse response = new GetSessionsResponse();
		List<SessionItemResponse> responseSessionItemlist = new ArrayList<SessionItemResponse>();
		List<Session> sessionList;
		
		if (pageStart == null) {
			
			sessionList = sessionService.findSessions(PageRequest.of(0, PAGE_SIZE));
			for(Session s : sessionList) {
				responseSessionItemlist.add(new SessionItemResponse(s));
			} 

			response.setPos(0);
			response.setTotal_count(sessionService.getSessionCount());
			
		} else {
			
			sessionList = sessionService.findSessions(PageRequest.of(pageStart / PAGE_SIZE, PAGE_SIZE));
			for(Session s : sessionList) {
				responseSessionItemlist.add(new SessionItemResponse(s));
			}
			
			response.setPos(pageStart);
			response.setTotal_count(null);
			
		}
		
		response.setData(responseSessionItemlist);
		
		return response;
	}
	
	@RequestMapping("/deleteSession/{sessionId}")
	@ResponseBody
	public DeleteSessionResponse deleteSession(@PathVariable Long sessionId) {
		
		sessionService.delete(sessionId);
		
		DeleteSessionResponse response = new DeleteSessionResponse();
		response.setStatus("server");
		return response;
		
	}
	

	
	

}
