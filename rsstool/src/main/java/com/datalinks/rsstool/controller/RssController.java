package com.datalinks.rsstool.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.datalinks.rsstool.model.xml.Item;
import com.datalinks.rsstool.model.RssFile;
import com.datalinks.rsstool.model.RssFileModel;
import com.datalinks.rsstool.model.xml.Channel;
import com.datalinks.rsstool.model.xml.Image;
import com.datalinks.rsstool.model.xml.Rss;
import com.datalinks.rsstool.util.RssUtil;



@Controller
@RequestMapping("/")
public class RssController implements RssTool_Variables{
		
	public final static Logger LOGGER = Logger.getLogger(RssController.class.getName());

	/*
	 * LOGIN page
	 */
	@RequestMapping(value = "login")
	public ModelAndView goLoginPage() {
		return new ModelAndView("login");
	}

	
	/*
	 * DELETE RSS FIlE
	 */
	@RequestMapping(value = "deleterssfileConfirm", method = RequestMethod.POST)
	public String deleteRssFileConfirm(
			@ModelAttribute("deleteModel") RssFileModel deleteModel) {
		if (deleteModel.isDoDelete()) {
			File file = new File(RssUtil.getProp(INSTALL_DIRECTORY) + deleteModel.getFileName());
			file.delete();
		}
		return "redirect:/rssfile";
	}

	/*
	 * EDIT RSS FIlE; CANCEL action
	 * 
	 */
	@RequestMapping(params = "cancel", value = "editFile", method = RequestMethod.POST)
	public String editRssFileCancel(@ModelAttribute("editModel") RssFileModel editModel) {
		return "redirect:/rssfile";
	}
	
	/*
	 * EDIT RSS FIlE;
	 * Actually we delete and recreate the file
	 */
	@RequestMapping(params = "save", value = "editFile", method = RequestMethod.POST )
	public String editRssFile(@ModelAttribute("editModel") RssFileModel editModel  ) {
		LOGGER.info("editing file now....");
		try {
			createRssFile(editModel, false, getLoggedInUser());
		} catch (JAXBException e) {
			LOGGER.severe("Exception creating the jaxb file in editRssFile, exception: "+e.getMessage());
		}

		return "redirect:/rssfile";
	}

	/*
	 * EDIT RSS FIlE; adding Item
	 * Actually we delete and recreate the file
	 */
	@RequestMapping(params = "add_item", value = "editFile", method = RequestMethod.POST)
	public ModelAndView editRssFileAddItem(@ModelAttribute("editModel") RssFileModel editModel) {
		LOGGER.info("editing file now....");
		ModelMap model = null;

		try {
			model = createSelectedRssModel(editModel.getFileName());
			createRssFile(editModel, true, getLoggedInUser());
		} catch (JAXBException e) {
			LOGGER.severe("Exception creating the jaxb file in editRssFileAddItem, exception: "+e.getMessage());
		}
		return new ModelAndView("editrssfile", model);
	}
	

	/*
	 * SHOW RSS File
	 */
	@RequestMapping(value = "rssfile")
	public ModelAndView selectedRssFile(HttpServletRequest request) {

		String fileName = request.getParameter("rssFilez");

		// TRUE = Delete or FALSE = Edit
		if (Boolean.valueOf(request.getParameter("doDelete"))) {
			ModelMap model = new ModelMap();
			model.addAttribute("selectedFileName", fileName);
			model.addAttribute("deleteModel", new RssFileModel());
			return new ModelAndView("deleterssfile", model);
		} else {
			try {
				ModelMap model = createSelectedRssModel(fileName);
				return new ModelAndView("editrssfile", model);
			} catch (JAXBException e) {
				LOGGER.severe("Exception creating the jaxb file in selectedRssFile, exception: "+e.getMessage());
			}
		}
		//	This should not happen
		return null;
	}

	private ModelMap createSelectedRssModel(String fileName) throws JAXBException{
		ModelMap model = new ModelMap();
		Rss rssFile = getRssFile(fileName,getLoggedInUser());
		model.addAttribute("selectedItems", rssFile.getChannel().getItem());
		RssFileModel rsMod = new RssFileModel();
		rsMod.setRssItems(rssFile.getChannel().getItem());
		rsMod.setChannel(rssFile.getChannel());
		rsMod.setFileName(fileName);
		model.addAttribute("editModel", rsMod);

		return model;
	}
	
	/*
	 * SHOW RSS File
	 */
	@RequestMapping(value = "rssfile", method = RequestMethod.GET)
	public ModelAndView showForm(
			@ModelAttribute("rssFileModel") RssFileModel rssFileModel) {

		ModelAndView modelAndView = new ModelAndView("rssfile");
		modelAndView.addObject("rssFilez", getRssFilez(getLoggedInUser()));
		return modelAndView;
	}

	/*
	 * CREATE RSS File
	 */
	@RequestMapping(value = "/createRssFile", method = RequestMethod.GET)
	public ModelAndView createRssFile() {
		return new ModelAndView("createrssfile", "command", new RssFile());
	}

	
	/*
	 * CREATE RSS File
	 */
	@RequestMapping(value = "/createRssFile", method = RequestMethod.POST)
	public String rssfile(@ModelAttribute("rssfile") RssFile rssFile,
			ModelMap model, HttpServletRequest request) {
		try {
			createDummyRssFile(rssFile.getFileName(),getLoggedInUser(),request);
		} catch (JAXBException e) {
			LOGGER.severe("Exception creating the jaxb file in createRssFile, exception: "+e.getMessage());
		}
		return "redirect:/rssfile";
	}

	/*
	 * 
	 * Private methods go here
	 * 
	 */
	
	
	/*
	 * 
	 * Get Logged in User
	 * 
	 */
	private String getLoggedInUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName();
	    return name;
	}
	
	/*
	 * GET aLL RSS Files
	 * 
	 */
	private List<RssFile> getRssFilez(String username) {
		List<RssFile> result = new ArrayList<RssFile>();

		// Read complete directory with rss files (if any available)
		String fileName;
		File folder = new File(RssUtil.getProp(INSTALL_DIRECTORY)+username);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile() && listOfFiles[i].canRead()) {
				fileName = listOfFiles[i].getName();
				if (fileName.endsWith(".xml")) {
					RssFile tmp = new RssFile();
					tmp.setFileName(fileName);
					result.add(tmp);
				}
			}
		}
		return result;
	}

	/*
	 * 	Get an existing RSS File
	 * 
	 */
	private Rss getRssFile(String fileName, String username) throws JAXBException {

		File file = new File(RssUtil.getProp(INSTALL_DIRECTORY) + username+File.separator+fileName);

		JAXBContext jaxbContext = JAXBContext.newInstance(Rss.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Rss rssXmlFile = (Rss) jaxbUnmarshaller.unmarshal(file);
		Channel channel = rssXmlFile.getChannel();

		Image plaatje = channel.getImage();
		if (plaatje != null)
			LOGGER.info("plaatje: " + plaatje.getTitle());

		return rssXmlFile;
	}

	/*
	 * 	Delete and create the New RSS File
	 * 
	 */
	private void createRssFile(RssFileModel editFileModel, boolean addItem, String username) throws JAXBException {
		String fileName = editFileModel.getFileName();
		File file_del = new File(RssUtil.getProp(INSTALL_DIRECTORY) +username+File.separator+ fileName);
		
		//	Only create the new File is delete of the OLD has succeeded
		if(file_del.delete()){
			File file_new = new File(RssUtil.getProp(INSTALL_DIRECTORY) +username+File.separator+ fileName);
			
			JAXBContext jaxbContext_new = JAXBContext.newInstance(Rss.class);
			Marshaller jaxbMarshaller = jaxbContext_new.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			Rss rssX = new Rss();
			rssX.setVersion(RSS_VERSION);
			
			Channel channelX = editFileModel.getChannel();
			List<Item> itemXList = editFileModel.getRssItems();			
			if(addItem){
				Item itemX = new Item();
				itemX.setTitle("edit title here");
				itemX.setLink("edit link here");
				itemX.setDescription("edit description here");
				itemXList.add(itemX);
			}
			channelX.setItem(itemXList);
			rssX.setChannel(channelX);
			jaxbMarshaller.marshal(rssX, file_new);
		}
		
	
	}
	
	/*
	 * Creates the initial dummy file
	 */
	private void createDummyRssFile(String fileNaam, String username, HttpServletRequest request) throws JAXBException {
		
		//	Filename should end with .xml
		if(!fileNaam.endsWith(".xml") )
			fileNaam = fileNaam+".xml";
		
		File file_new = new File(RssUtil.getProp(INSTALL_DIRECTORY) +username+File.separator+ fileNaam);
		JAXBContext jaxbContext_new = JAXBContext.newInstance(Rss.class);
		Marshaller jaxbMarshaller = jaxbContext_new.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		Rss rssX = new Rss();
		rssX.setVersion(RSS_VERSION);
		
		Channel channelX = new Channel();
		channelX.setTitle("edit title");
		String link = request.getLocalName()+":"+request.getLocalPort()+"/rss/"+username+"/"+fileNaam;
		channelX.setLink(link);
		channelX.setDescription("edit description");
		Item itemX = new Item();
		itemX.setTitle("e.g. google");
		itemX.setLink("e.g. http://www.google.com");
		itemX.setDescription("e.g. great search engine");

		List<Item> itemXList = new ArrayList<Item>();
		itemXList.add(itemX);

		channelX.setItem(itemXList);
		rssX.setChannel(channelX);
		jaxbMarshaller.marshal(rssX, file_new);
	}

}
