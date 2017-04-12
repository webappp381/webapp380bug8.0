
package edu.ouhk.comps380f.controller;

import edu.ouhk.comps380f.model.Attachment;
import edu.ouhk.comps380f.model.ReplyTicket;
import edu.ouhk.comps380f.model.Ticket;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import edu.ouhk.comps380f.dao.AttachmentRepository;
import edu.ouhk.comps380f.dao.ReplyTicketRepository;
import edu.ouhk.comps380f.dao.TicketRepository;
import edu.ouhk.comps380f.view.DownloadingView;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("reply")


public class ReplyTicketController {
     @Autowired
     ReplyTicketRepository ReplyTicketRepo;
     
    @Autowired
    TicketRepository ticketRepo;
    
    @Autowired
    AttachmentRepository attachmentRepo; 
     
     private volatile long REPLYTICKET_ID_SEQUENCE = 1;
   
       
     @RequestMapping(value = "{ticketId}", method = RequestMethod.GET)
    public ModelAndView replyView(@PathVariable("ticketId") int ticketId) {
        //Ticket ticket = this.ticketDatabase.get(ticketId);

        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject("ticketId", ticketId);
       return new ModelAndView("reply", "replyForm",new ReplyForm());
    }
    
       public static class ReplyForm {

        private int refticketId;
        private String replybody;

        private List<MultipartFile> replyattachments;

        public int getRefticketId() {
            return refticketId;
        }

        public void setRefticketId(int refticketId) {
            this.refticketId = refticketId;
        }
      
      
        public String getReplybody() {
            return replybody;
        }

        public void setReplybody(String replybody) {
            this.replybody = replybody;
        }

        public List<MultipartFile> getReplyattachments() {
            return replyattachments;
        }

        public void setReplyattachments(List<MultipartFile> replyattachments) {
            this.replyattachments = replyattachments;
        }
    }
       
        @RequestMapping(value = "{ticketId}", method = RequestMethod.POST)
    public View reply(Principal principal,ReplyForm form,@PathVariable("ticketId") int ticketId) throws IOException {
        String attachmentname ="";
        ReplyTicket replyTicket = new ReplyTicket();
        if(!ReplyTicketRepo.findAll().isEmpty()){
                replyTicket.setId(ReplyTicketRepo.maxId()+1);
            }else{
                replyTicket.setId(1);
            }
        
        replyTicket.setReplyName(principal.getName());
        replyTicket.setRefTicketid(ticketId);
        replyTicket.setReplybody(form.getReplybody());
        int reply_id = replyTicket.getId();
        int referenceTicketId = replyTicket.getRefTicketid();
        Attachment attachment = new Attachment();
        for (MultipartFile filePart : form.getReplyattachments()) {
            
            attachment.setRid(reply_id);
            attachment.setId(referenceTicketId);
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            attachmentname = attachment.getName();
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                replyTicket.addAttachment(attachment);
            }
            if (!attachmentname.equals("")) {
                attachmentRepo.createReplyAttachment(replyTicket.getAttachment(attachmentname));}
        }
        System.out.println("ticket id:"+attachment.getId() + "reply_id" + attachment.getRid());
        //TicketController.replyTicketDatabase.put(replyTicket.getId(), replyTicket);
        ReplyTicketRepo.create(replyTicket);
        //Ticket ticket = TicketController.ticketRepo.get(ticketId);
        
        //ticket.setReplyId(replyTicket.getId());
        
        //TicketController.ticketRepo.replace(ticketId,ticket);
        
        return new RedirectView("/ticket/view/" + ticketId, true);
    }
    
     @RequestMapping(
            value = "/{replyId}/attachment/{attachment:.+}",
            method = RequestMethod.GET
    )
    public View download(@PathVariable("replyId") int replyId,
            @PathVariable("attachment") String name) {
        ReplyTicket replyTicket = this.ReplyTicketRepo.findById(replyId);
        Attachment attachment= attachmentRepo.findByName(name);
        if (replyTicket != null) {
            
            if (attachment != null) {
                return new DownloadingView(attachment.getName(),
                        attachment.getMimeContentType(), attachment.getContents());
            }
        }
        return new RedirectView("/ticket/list", true);
    }
    
    private synchronized long getNextReplyTicketId() {
        return this.REPLYTICKET_ID_SEQUENCE++;
    }
    
     @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public View deleteTicket(@PathVariable("id") int reId) {
        if (this.ReplyTicketRepo.findById(reId) != null) {
            this.ReplyTicketRepo.deleteById(reId);
        }
        return new RedirectView("/ticket/list", true);
    }
}
