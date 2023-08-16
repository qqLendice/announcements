package com.example.servingwebcontent;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {

	@Autowired
    private final AnnouncementDAO announcementDAO; 

    @Autowired
    public AnnouncementController(AnnouncementDAO announcementDAO) {
        this.announcementDAO = announcementDAO;
    }

//    @GetMapping
//    public String listAnnouncements(Model model) {
//        List<Announcement> announcements = announcementDAO.getAllAnnouncements(); 
//        model.addAttribute("announcements", announcements);
//        return "announcement/list";
//    }
    
    @GetMapping
    public String listAnnouncements(@PageableDefault(size = 6) Pageable pageable, Model model) {
        Page<Announcement> announcementPage = announcementDAO.getAllAnnouncements(pageable);
        
        model.addAttribute("page", announcementPage);
        return "announcement/list";
    }
    

    @GetMapping("/{id}/edit")
    public String editAnnouncementForm(@PathVariable Long id, Model model) {
        Announcement announcement = getAnnouncementById(id);
        model.addAttribute("announcement", announcement);
        return "announcement/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateAnnouncement(@PathVariable Long id, @ModelAttribute Announcement updatedAnnouncement) {
        updateAnnouncementById(id, updatedAnnouncement);
        return "redirect:/announcements";
    }
    
    @GetMapping("/{id}/delete")
    public String deleteAnnouncement(@PathVariable Long id) {
    	announcementDAO.deleteAnnouncementById(id);
        return "redirect:/announcements";
    }
    
    @GetMapping("/new")
    public String createAnnouncementForm(Model model) {
        Announcement newAnnouncement = new Announcement();
        model.addAttribute("announcement", newAnnouncement);
        return "announcement/new"; 
    }

    @PostMapping("/new")
    public String createAnnouncement(@ModelAttribute Announcement newAnnouncement) {
        announcementDAO.save(newAnnouncement); 
        return "redirect:/announcements";
    }
    
    

    private Announcement getAnnouncementById(Long id) {
        return announcementDAO.getById(id);
    }

    private void updateAnnouncementById(Long id, Announcement updatedAnnouncement) {
        Announcement existingAnnouncement = announcementDAO.getById(id);
        if (existingAnnouncement != null) {
            existingAnnouncement.setTitle(updatedAnnouncement.getTitle());
            existingAnnouncement.setPublishDate(updatedAnnouncement.getPublishDate());
            existingAnnouncement.setEndDate(updatedAnnouncement.getEndDate());
            existingAnnouncement.setPublisher(updatedAnnouncement.getPublisher());
            existingAnnouncement.setContent(updatedAnnouncement.getContent());
            announcementDAO.save(existingAnnouncement);
        }
    }

}
