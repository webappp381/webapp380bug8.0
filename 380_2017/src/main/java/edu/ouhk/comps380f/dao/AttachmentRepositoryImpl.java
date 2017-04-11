package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Attachment;
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
public class AttachmentRepositoryImpl implements AttachmentRepository {

  private DataSource dataSource;
  private JdbcOperations jdbcOp;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
    this.jdbcOp = new JdbcTemplate(this.dataSource);
  }
  private static final String SQL_INSERT_ATTACHMENT
          = "insert into attachment (id,attachmentname,mimecontenttype,contents) values (?,?,?,?)";

  @Override
  public void create(Attachment attach) {
    jdbcOp.update(SQL_INSERT_ATTACHMENT,
                attach.getId(),
                attach.getName(),
                attach.getMimeContentType(),               
                attach.getContents());
  }
  

    private static final String SQL_DELETE_ATTACHMENT
          = "delete from attachment where attachmentname = ?";

  @Override
  public void deleteByName(String name) {
    jdbcOp.update(SQL_DELETE_ATTACHMENT, name);

  }
  
  private static final String SQL_FIND_ATTACHMENT
          = "select * from attachment";

  @Override
  public List<Attachment> findAll() {
      List<Attachment> attachments = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcOp.queryForList(SQL_FIND_ATTACHMENT);

        for (Map<String, Object> row : rows) {
            Attachment attachment = new Attachment();
            int id = (int)row.get("id");
            String attachmentname = (String)row.get("attachmentname");
            String mimeContentType = (String)row.get("mimecontenttype");
            byte[] contents = (byte[])row.get("contents");
            attachment.setId(id);                  
            attachment.setName(attachmentname);
            attachment.setMimeContentType(mimeContentType);
            attachment.setContents(contents);
            attachments.add(attachment);
        }
        return attachments;
  }
}
