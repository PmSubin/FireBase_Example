package vn.edu.ueh.thanhdnh.firebase_example;

import java.util.ArrayList;

// Hộp chứa danh sách đọc từ file users.json của bài UserProfile trước (key "users")
public class ArticleList {
  private ArrayList<Article> users;

  public ArticleList(ArrayList<Article> users) {
    this.users = users;
  }

  public ArrayList<Article> getUsers() {
    return users;
  }

  public void setUsers(ArrayList<Article> users) {
    this.users = users;
  }
}
