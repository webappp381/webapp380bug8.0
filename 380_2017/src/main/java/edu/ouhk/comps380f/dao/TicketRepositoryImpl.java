package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.EmptyTicket;
import edu.ouhk.comps380f.model.Ticket;
import edu.ouhk.comps380f.model.TicketUser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TicketRepositoryImpl implements TicketRepository {

    private DataSource dataSource;
    private JdbcOperations jdbcOp;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcOp = new JdbcTemplate(this.dataSource);
    }

    private static final class TicketRowMapper implements RowMapper<Ticket> {

        @Override
        public Ticket mapRow(ResultSet rs, int i) throws SQLException {
            Ticket ticket = new Ticket();
            ticket.setId(rs.getInt("id"));
            ticket.setCustomerName(rs.getString("customername"));
            ticket.setSubject(rs.getString("subject"));
            ticket.setBody(rs.getString("body"));
            ticket.setCategories(rs.getString("categories"));
            return ticket;
        }
    }
    
       private static final class EmptyTicketRowMapper implements RowMapper<EmptyTicket> {

        @Override
        public EmptyTicket mapRow(ResultSet rs, int i) throws SQLException {
            EmptyTicket ticket = new EmptyTicket();
            ticket.setEmpty(rs.getInt("empty"));
            return ticket;
        }
    }

//    private static final String SQL_INSERT_TICKET
//            = "insert into ticket (id,customername,subject,body,categories) values (1,'p','f','d','lab')";
        private static final String SQL_INSERT_TICKET
            = "insert into ticket (customername,subject,body,categories) values (?,?,?,?)";
   // private static final String SQL_INSERT_ROLE
     //       = "insert into user_roles (username, role) values (?, ?)";

    public void create(Ticket ticket) {
        jdbcOp.update(SQL_INSERT_TICKET,
                ticket.getCustomerName(),
                ticket.getSubject(),               
                ticket.getBody(),
                ticket.getCategories());
//        for (String role : user.getRoles()) {
//            jdbcOp.update(SQL_INSERT_ROLE,
//                    user.getUsername(),
//                    role);             
    }


    private static final String SQL_SELECT_ALL_TICKET
            = "select id, customername, subject, body, categories from ticket";
  //  private static final String SQL_SELECT_ROLES
    //        = "select username, role from user_roles where username = ?";

    @Override
    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcOp.queryForList(SQL_SELECT_ALL_TICKET);

        for (Map<String, Object> row : rows) {
            Ticket ticket = new Ticket();
            
            int id = (int)row.get("id");
            ticket.setId(id);       
            
            String customername = (String)row.get("customername");
            ticket.setCustomerName(customername);  
            
            String subject = (String)row.get("subject");
            ticket.setSubject(subject);
            
            String body = (String)row.get("body");
            ticket.setBody(body);
            
            String categories = (String)row.get("categories");
            ticket.setCategories(categories);
            
            tickets.add(ticket);
        }
        return tickets;
    }
    private static final String SQL_SELECT_TICKET
            = "select * from ticket where id = ?";

    @Override
    public Ticket findById(int id) {
        Ticket ticket = jdbcOp.queryForObject(SQL_SELECT_TICKET, new TicketRowMapper(), id);
        return ticket;
    }
    
    private static final String SQL_EMPTY
            = "select count(id) as empty from ticket";

    @Override
    public int checkempty() {
        EmptyTicket total = jdbcOp.queryForObject(SQL_EMPTY, new EmptyTicketRowMapper());
        int emptytable=total.getEmpty();
        if (emptytable!=0)
            return 1;
        else
            return 0;
    }
    
    private static final String SQL_SELECT_MAX
            = "select * from ticket where id = (select MAX(id) from ticket)";
    @Override
    public int maxId() {
        Ticket idobject = jdbcOp.queryForObject(SQL_SELECT_MAX, new TicketRowMapper());
        int id=(int)idobject.getId();
        return id;
    }
    private static final String SQL_SELECT_LAB
            = "select * from ticket where categories = 'lab'";
    private static final String SQL_SELECT_LECTURE
            = "select * from ticket where categories = 'lecture'";
    private static final String SQL_SELECT_OTHER
            = "select * from ticket where categories = 'other'";
    @Override
    public List<Ticket> findByCategories(String type) {
        List<Map<String, Object>> rows=null;
        List<Ticket> tickets2 = new ArrayList<>();
        if(type=="lab"){
          rows = jdbcOp.queryForList(SQL_SELECT_LAB);
        }
        if(type=="lecture"){
          rows = jdbcOp.queryForList(SQL_SELECT_LECTURE);
        }
        if(type=="other"){
          rows = jdbcOp.queryForList(SQL_SELECT_OTHER);
        }
        
        for (Map<String, Object> row : rows) {
            Ticket ticket = new Ticket();
            
            int id = (int)row.get("id");
            ticket.setId(id);       
            
            String customername = (String)row.get("customername");
            ticket.setCustomerName(customername);  
            
            String subject = (String)row.get("subject");
            ticket.setSubject(subject);
            
            String body = (String)row.get("body");
            ticket.setBody(body);
            
            String categories = (String)row.get("categories");
            ticket.setCategories(categories);
            
            tickets2.add(ticket);
        }
        return tickets2;
    }
//    private static final String SQL_DELETE_USER
//            = "delete from users where username = ?";
//    private static final String SQL_DELETE_ROLES
//            = "delete from user_roles where username = ?";
//
//    @Override
//    public void deleteByUsername(String username) {
//        jdbcOp.update(SQL_DELETE_ROLES, username);
//        jdbcOp.update(SQL_DELETE_USER, username);
//    }

    private static final String SQL_DELETE_TICKET
            = "delete  from ticket where id = ?";
    

    

    @Override
    public void deleteById(int id) {
        jdbcOp.update(SQL_DELETE_TICKET, id);
        
    }
    
    private static final String SQL_UPDATE_TICKET
            = "update ticket set subject = ?, body = ? where id = ?";
    
    @Override
    public void update(Ticket ticket) {
        jdbcOp.update(SQL_UPDATE_TICKET, ticket.getSubject(), ticket.getBody(), ticket.getId());
    }

}
