package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

// Đổ danh sách bài viết vào RecyclerView (làm theo mẫu UserViewAdapter của thầy)
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
    holder.getTxtTitle().setText(currentarticle.getId() + ". " + currentarticle.getUsername());
    holder.getTxtDescription().setText(currentarticle.getDesc());
    holder.getTxtViews().setText("Lượt xem: " + currentarticle.getViews());
    // Dùng Picasso tải avatar từ link (giống bài trước)
    String image = currentarticle.getAvatar_url();
    if (image != null && !image.isEmpty())
      Picasso.get().load(image).resize(300, 400).centerCrop().into(holder.getImgArticle());
    else
      holder.getImgArticle().setImageDrawable(null);

    // Bấm vào 1 dòng: mở màn chi tiết, gửi kèm id (cũng là tên document)
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ViewArticleActivity.class);
        intent.putExtra("id", String.valueOf(currentarticle.getId()));
        v.getContext().startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount() {
    return articles.size();
  }
}
