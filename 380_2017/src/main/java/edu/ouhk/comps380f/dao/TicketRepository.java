package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Ticket;
import java.util.List;

public interface TicketRepository {
    public void create(Ticket ticket);
    public List<Ticket> findAll();
    public Ticket findById(int id);
    public int maxId();
    public List<Ticket> findByCategories(String type);
    public void deleteById(int id);
    public void update(Ticket ticket);

}

