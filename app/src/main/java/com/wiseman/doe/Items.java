package com.wiseman.doe;

/**
 * Created by Wiseman on 2017-05-12.
 */

public class Items {
    private int messageId;
    private String message;
    private String subject;
    private String link;
    private String author;
    private String date;
    private String filename;
    private String urgent;
    private String attach;
    public Items(int messageId,String  subject,
                 String date,String message,
                 String  attach,String urgent,
                 String author,String link  ,
                 String filename)
    {
        this.messageId=messageId;
        this.message = message;
        this.subject = subject;
        this.link = link;
        this.author = author;
        this.date = date;
        this.filename =filename;
        this.urgent =urgent;
        this.setAttach(attach);
    }

    public int getMessageId()
    {
        return messageId;
    }
    public void setMessageId(int messageId)
    {
        this.messageId = messageId;
    }
    public String getMessage()
    {
        return message;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }
    public String getSubject()
    {
        return subject;
    }
    public void setSubject(String subject)
    {
        this.subject = subject;
    }
    public String getLink()
    {
        return link;
    }
    public void setLink(String link)
    {
        this.link = link;
    }
    public String getAuthor()
    {
        return author;
    }
    public void setAuthor(String author)
    {
        this.author = author;
    }
    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date = date;
    }
    public String getFilename()
    {
        return filename;
    }
    public void setFilename(String filename)
    {
        this.filename = filename;
    }
    public String getUrgent()
    {
        return urgent;
    }
    public void setUrgent(String urgent)
    {
        this.urgent = urgent;
    }
    public String getAttach() {
        return attach;
    }
    public void setAttach(String attach) {
        this.attach = attach;
    }
}
