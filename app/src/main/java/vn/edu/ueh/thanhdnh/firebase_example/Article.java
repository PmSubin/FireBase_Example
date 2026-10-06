package vn.edu.ueh.thanhdnh.firebase_example;

// Lớp Article: nội dung giống bài UserProfile trước (users.json), thêm số lượt xem
public class Article {
  private int id;
  private String username;
  private String email;
  private String desc;
  private String avatar_url;
  private String hobby;
  private long views;

  public Article(int id, String username, String email, String desc, String avatar_url, String hobby) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.desc = desc;
    this.avatar_url = avatar_url;
    this.hobby = hobby;
    this.views = 0;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getAvatar_url() {
    return avatar_url;
  }

  public void setAvatar_url(String avatar_url) {
    this.avatar_url = avatar_url;
  }

  public String getHobby() {
    return hobby;
  }

  public void setHobby(String hobby) {
    this.hobby = hobby;
  }

  public long getViews() {
    return views;
  }

  public void setViews(long views) {
    this.views = views;
  }
}
