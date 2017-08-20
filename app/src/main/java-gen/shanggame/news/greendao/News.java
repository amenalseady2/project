package shanggame.news.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table NEWS.
 */
public class News {

    private Long id;
    /** Not-null value. */
    private String newsid;
    private String newscontent;
    private String newstypeid;
    private java.util.Date date;

    public News() {
    }

    public News(Long id) {
        this.id = id;
    }

    public News(Long id, String newsid, String newscontent, String newstypeid, java.util.Date date) {
        this.id = id;
        this.newsid = newsid;
        this.newscontent = newscontent;
        this.newstypeid = newstypeid;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getNewsid() {
        return newsid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getNewscontent() {
        return newscontent;
    }

    public void setNewscontent(String newscontent) {
        this.newscontent = newscontent;
    }

    public String getNewstypeid() {
        return newstypeid;
    }

    public void setNewstypeid(String newstypeid) {
        this.newstypeid = newstypeid;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

}