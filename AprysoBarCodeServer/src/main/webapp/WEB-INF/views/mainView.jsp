<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags"  prefix="spring" %>
<!DOCTYPE html>
<html>
    <head>
        
		<c:url var="root"  value="/" />
		
		<link rel="stylesheet" href="${root}${themeUrl}"  type="text/css" charset="utf-8">
		
		<script src="<c:url value="/resources/js/webix/webix.js"/>" type="text/javascript" charset="utf-8"></script>
			

		<style>
		
			.myhover{ background: #AEDCF7; }
			
		</style>
		
		
    </head>
    <body>
        <script type="text/javascript" charset="utf-8">
            webix.ready(function() {
			
                function showEntries(sessionId) {
                	
                	webix.ui({
						view:"popup",
						id:"entriesWindow",
						modal:true,
						position:"center",
						height: screen.height/2,
						width: screen.width - (screen.width/4),
						body: {
							rows: [
								{ 
									view:"datatable", 
									id: "entriesTable",
									url: '<c:url value="/sessionEntry/getEntries"/>/'+sessionId,
									pager: "entriesPager",
									resizeColumn:true,
									scrollX:true,
									columns: [
										{ id:"sessionEntryId", hidden:true },
										{ id:"timestamp",	fillspace:2, header: {text:'<spring:message code="mainView.entriesWindows.header.timestamp"/>', css:{"text-align":"center"} },  css:{'text-align':'center'} },
										{ id:"content",     fillspace:3, header: {text:'<spring:message code="mainView.entriesWindows.header.content"/>', css:{"text-align":"center"} },  css:{'text-align':'center'} },
										{ id:"format",      fillspace:1, header: {text:'<spring:message code="mainView.entriesWindows.header.format"/>', css:{"text-align":"center"} },  css:{'text-align':'center'} },
										{ id:"numberOfItems",fillspace:true, header: {text:'<spring:message code="mainView.entriesWindows.header.numberOfItems"/>', css:{"text-align":"center"} },  css:{'text-align':'center'}},
									],
									on: { 
										onBeforeLoad:function(){
					                        this.showOverlay('<spring:message code="mainView.entriesWindows.overlay.loading"/>');
					                    },
					                    onAfterLoad:function(){
					                    	 if (this.count() == 0) {
												this.showOverlay('<spring:message code="mainView.entriesWindows.overlay.noData"/>');
					                    	 } else {
					                        	this.hideOverlay();
					                    	 }
					                    }
									}
								},
								{
									view:"pager",
									template:" {common.prev()} {common.pages()} {common.next()}",
									id: "entriesPager",
									size:20,
									group:3
								},
								{ view:"button", label:'<spring:message code="mainView.entriesWindows.button.cancel"/>', click:"$$('entriesWindow').close();" }
							]
						}
					}).show();
                }
            	
            	
            	
            	/* 
				   Latest Webix v5.3 uses Font Awesome v4.7.0
				   https://fontawesome.com/v4.7.0/icons/
				   https://fontawesome.com/icons?d=gallery 
				*/
				webix.ui({
					rows: [
						{
							view: "template", template: '<spring:message code="mainView.title"/>', type:"header"
						},
						{ 
							view:"toolbar", id:"toolbar", height: 70, elements:[
								{
									view: "combo", id: "themeCombo", width: 300, label: '<spring:message code="mainView.toolbar.theme"/>',
									value: '${currentTheme}', labelWidth: 50,
									options: [ "AIR", "AIRCOMPACT", "CLOUDS", "COMPACT", "CONTRAST", "FLAT", "GLAMOUR", "LIGHT", "METRO", "TERRACE", "TOUCH", "WEB", "WEBIX" ],
									on: {
										onChange: function(newTheme, oldTheme){
											webix.send( '<c:url value="/"/>'+newTheme);
										}
									}
								},
								{},
								{
									view:"button", type:"iconButtonTop", icon:"android", 
									click: function() { window.open('<c:url value="/apk/index.html"/>', '_blank'); }, 
									id: "androidBt", label:'<spring:message code="mainView.toolbar.android"/>', width: 110
								},
								{
									view:"button", type:"iconButtonTop", icon:"history", 
									click: function() { window.location.href =this.config.href; }, 
									href: '<c:url value="/"/>',
									id: "reloadBt", label:'<spring:message code="mainView.toolbar.reload"/>', width: 110
								},
								{
									view:"button", type:"iconButtonTop", icon:"minus-circle", 
									click: function() { window.location.href =this.config.href; }, 
									href: '<c:url value="/logout"/>',
									id: "logoutBt", label:'<spring:message code="mainView.toolbar.logout"/>', width: 110
								}
							]
						},
						{
							cols: [
								{
							    	view: "datatable",
									id: "sessionTable",
									url: '<c:url value="/getSessions"/>',
									pager: "pager",
									resizeColumn:true,
									scrollX:false,
									hover:"myhover",
									columns: [
										{ id:"sessionId", hidden:true },
										{ id:"timestamp",  fillspace:2, header: {text:'<spring:message code="mainView.sessionList.header.timestamp"/>', css:{"text-align":"center"}}, css: { "text-align":"center" }},
										{ id:"model",  fillspace:2, header: {text:'<spring:message code="mainView.sessionList.header.model"/>', css:{"text-align":"center"}}, css: { "text-align":"center" }},
										{ id:"manufacturer",  fillspace:2, header: {text:'<spring:message code="mainView.sessionList.header.manufacturer"/>', css:{"text-align":"center"}}, css: { "text-align":"center" }},
										{ id:"numberOfReads",  fillspace:1, header: {text:'<spring:message code="mainView.sessionList.header.numberOfReads"/>', css:{"text-align":"center"}}, css: { "text-align":"center" }},
										{ id:"numberOfReadItems",  fillspace:1, header: {text:'<spring:message code="mainView.sessionList.header.numberOfReadItems"/>', css:{"text-align":"center"}}, css: { "text-align":"center" }},
										{ id:"showEntries", fillspace:1, header:"",
								        	template: function(sessionInfo) {
									        	return "<span class='webix_icon fa-search-plus' style='color: #666'></span>";     
									        },
								        	css:{'text-align':'center'} },
								        { id:"delete",         fillspace:true, header:"", template:"{common.trashIcon()}",                                       css:{'text-align':'center'} },
									],
									on: {},
									onClick: {
										"fa-search-plus" : function(e, id, trg) {
								        	
								        	session= $$("sessionTable").data.getItem(id);
								        	showEntries(session.sessionId);
								        },
										"fa-trash": function(e, id, trg) {
								        	
								        	webix.confirm({
								        		
								        	    title:'<spring:message code="mainView.delete.title"/>',
								        	    ok:"Yes", 
								        	    cancel:"No",
								        	    text: '<spring:message code="mainView.delete.text"/>',
								        	    callback:function(result) {

								        	    	if (result == true) {
								        	    		
								        	    		session = $$("sessionTable").data.getItem(id);
														
														webix.ajax().post('<c:url value="/deleteSession"/>/'+session.sessionId, function(text, data, XmlHttpRequest){
														    if (data.json().status == "server") {
														    	session = $$("sessionTable").data.remove(id);
														    	
														    	if ($$("sessionTable").data.count() == 0) {
															    	$$("sessionTable").showOverlay('<spring:message code="mainView.sessionList.overlay.noData"/>');
														    	}
														    	webix.message('<spring:message code="mainView.delete.success"/>');
														    	
														    } else {
														    	webix.message("ERROR",'<spring:message code="mainView.delete.fail"/>');
														    }
														});
								        	    	}
								        	   }
								        	});
								        },
									},
									onDblClick: {
										"webix_cell": function(e, id, trg) {
											
											session= $$("sessionTable").data.getItem(id);
								        	showEntries(session.sessionId);
										}
									}
								}
							]
						},
						{
							view:"pager",
							template:" {common.prev()} {common.pages()} {common.next()}",
							id: "pager",
							size:20,
							group:3
						}
					]
				});
			});
		</script>
	</body>
</html>
