package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.ReplyTicket;
import java.util.List;

public interface ReplyTicketRepository {
    public void create(ReplyTicket ticket);
    public List<ReplyTicket> findAll();
    public List<ReplyTicket> findParts(int ticketId);
    public ReplyTicket findById(int id);
    public int maxId();
    public void deleteById(int id );
}
