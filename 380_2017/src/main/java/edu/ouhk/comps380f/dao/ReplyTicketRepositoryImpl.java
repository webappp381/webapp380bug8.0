package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.ReplyTicket;
import edu.ouhk.comps380f.model.Ticket;
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
public class ReplyTicketRepositoryImpl implements ReplyTicketRepository {

    private DataSource dataSource;
    private JdbcOperations jdbcOp;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcOp = new JdbcTemplate(this.dataSource);
    }

    private static final class ReplyTicketRowMapper implements RowMapper<ReplyTicket> {

        @Override
        public ReplyTicket mapRow(ResultSet rs, int i) throws SQLException {
            ReplyTicket replyticket = new ReplyTicket();
            replyticket.setId(rs.getInt("id"));
            replyticket.setRefTicketid(rs.getInt("refId"));
            replyticket.setReplybody(rs.getString("body"));
            return replyticket;
        }
    }

//    private static final String SQL_INSERT_TICKET
//            = "insert into ticket (id,customername,subject,body,categories) values (1,'p','f','d','lab')";
        private static final String SQL_INSERT_REPLYTICKET
            = "insert into Reply (replyname,refid,body) values (?,?,?)";
   // private static final String SQL_INSERT_ROLE
     //       = "insert into user_roles (username, role) values (?, ?)";

    public void create(ReplyTicket ticket) {
        jdbcOp.update(SQL_INSERT_REPLYTICKET,
                ticket.getReplyName(),
                ticket.getRefTicketid(),
                ticket.getReplybody());   
    }


    private static final String SQL_SELECT_ALL_REPLYTICKET
            = "select id,replyname,refid,body from REPLY";
  //  private static final String SQL_SELECT_ROLES
    //        = "select username, role from user_roles where username = ?";

    @Override
    public List<ReplyTicket> findAll() {
        List<ReplyTicket> replyTickets = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcOp.queryForList(SQL_SELECT_ALL_REPLYTICKET);
        
        for (Map<String, Object> row : rows) {
            ReplyTicket replyTicket = new ReplyTicket();
            int id = (int)row.get("ID");
            replyTicket.setId(id);       
            
            String replyname = (String)row.get("REPLYNAME");
            replyTicket.setReplyName(replyname);
            replyTickets.add(replyTicket);
            
            int refId = (int)row.get("REFID");
            replyTicket.setRefTicketid(refId);       
                             
            String replyBody = (String)row.get("BODY");
            replyTicket.setReplybody(replyBody);
            replyTickets.add(replyTicket);

        }
        return replyTickets;
    }
    private static final String SQL_SELECT_PART_REPLYTICKET = "select id,replyname, refId, body from reply WHERE refId = ?";
     @Override
    public List<ReplyTicket> findParts(int ticketId) {
        List<ReplyTicket> replyTickets = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcOp.queryForList(SQL_SELECT_PART_REPLYTICKET,ticketId);

        for (Map<String, Object> row : rows) {
            ReplyTicket replyTicket = new ReplyTicket();
            
            int id = (int)row.get("id");
            replyTicket.setId(id);       
            
            String replyname = (String)row.get("REPLYNAME");
            replyTicket.setReplyName(replyname);
            
            
            int refId = (int)row.get("refId");
            replyTicket.setRefTicketid(refId);       
                             
            String replyBody = (String)row.get("body");
            replyTicket.setReplybody(replyBody);
            
            replyTickets.add(replyTicket);
        }
        return replyTickets;
    }
    
    private static final String SQL_SELECT_REPLYTICKET
            = "select * from REPLY where id = ?";

    @Override
    public ReplyTicket findById(int id) {
        ReplyTicket ticket = jdbcOp.queryForObject(SQL_SELECT_REPLYTICKET, new ReplyTicketRowMapper(), id);
        return ticket;
    }
    
    
    private static final String SQL_SELECT_MAX
            = "select * from reply where id = (select MAX(id) from reply)";
    @Override
    public int maxId() {
        ReplyTicket idobject = jdbcOp.queryForObject(SQL_SELECT_MAX, new ReplyTicketRowMapper());
        int id=(int)idobject.getId();
        return id;
    }

    private static final String SQL_DELETE_TICKET
            = "delete from REPLY where id = ?";
    
    @Override
    public void deleteById(int id) {
       
        jdbcOp.update(SQL_DELETE_TICKET, id);
    }

}
