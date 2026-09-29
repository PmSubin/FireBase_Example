package vn.edu.ueh.thanhdnh.firebase_example;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Giữ các view của 1 dòng bài viết (giống UserViewHolder của thầy)
public class ArticleViewHolder extends RecyclerView.ViewHolder {
  private ImageView imgArticle;
  private TextView txtTitle, txtDescription;
  private ArticleViewAdapter adapter;

  public ArticleViewHolder(@NonNull View itemView, ArticleViewAdapter adapter) {
    super(itemView);
    imgArticle = itemView.findViewById(R.id.img_article);
    txtTitle = itemView.findViewById(R.id.txt_title);
    txtDescription = itemView.findViewById(R.id.txt_description);
    this.adapter = adapter;
  }

  public ImageView getImgArticle() {
    return imgArticle;
  }

  public void setImgArticle(ImageView imgArticle) {
    this.imgArticle = imgArticle;
  }

  public TextView getTxtTitle() {
    return txtTitle;
  }

  public void setTxtTitle(TextView txtTitle) {
    this.txtTitle = txtTitle;
  }

  public TextView getTxtDescription() {
    return txtDescription;
  }

  public void setTxtDescription(TextView txtDescription) {
    this.txtDescription = txtDescription;
  }
}
