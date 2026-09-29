package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

// Đổ danh sách bài viết vào RecyclerView (giống UserViewAdapter của thầy)
public class ArticleViewAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
  private LayoutInflater mInflater;
  private List<Article> articles;

  public ArticleViewAdapter(Context context, List<Article> articles) {
    this.mInflater = LayoutInflater.from(context);
    this.articles = articles;
  }

  public void update(List<Article> articles){
    this.articles = articles;
  }

  @NonNull
  @Override
  public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View customView = mInflater.inflate(R.layout.article_list, parent, false);
    return new ArticleViewHolder(customView, this);
  }

  @Override
  public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
    Article currentarticle = articles.get(position);
    holder.getTxtTitle().setText(currentarticle.getArticle_id() + ". " + currentarticle.getArticle_title());
    // File JSON ghi xuống dòng là "\n" dạng chữ nên đổi lại thành xuống dòng thật
    String description = currentarticle.getArticle_description();
    if (description != null)
      description = description.replace("\\n", "\n");
    holder.getTxtDescription().setText(description);
    // Dùng Picasso tải ảnh từ link (giống bài Article trước)
    String image = currentarticle.getArticle_image();
    if (image != null && !image.isEmpty())
      Picasso.get().load(image).resize(300, 400).centerCrop().into(holder.getImgArticle());
    else
      holder.getImgArticle().setImageDrawable(null);
  }

  @Override
  public int getItemCount() {
    return articles.size();
  }
}
