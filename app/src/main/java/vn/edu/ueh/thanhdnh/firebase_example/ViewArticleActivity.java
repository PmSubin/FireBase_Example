package vn.edu.ueh.thanhdnh.firebase_example;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

// Màn chi tiết (lấy từ bài UserProfile trước): mở lên là tăng 1 lượt xem trên Firestore
public class ViewArticleActivity extends AppCompatActivity {
  FirebaseFirestore db;
  ImageView iv_detail;
  TextView tv_detail_title, tv_detail_description, tv_detail_email, tv_detail_hobby, tv_detail_views;
  Button btn_back;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_article);

    iv_detail = findViewById(R.id.iv_detail);
    tv_detail_title = findViewById(R.id.tv_detail_title);
    tv_detail_description = findViewById(R.id.tv_detail_description);
    tv_detail_email = findViewById(R.id.tv_detail_email);
    tv_detail_hobby = findViewById(R.id.tv_detail_hobby);
    tv_detail_views = findViewById(R.id.tv_detail_views);
    btn_back = findViewById(R.id.btn_back);

    // id gửi từ danh sách, cũng chính là tên document trên Firestore
    String id = getIntent().getStringExtra("id");
    db = FirebaseFirestore.getInstance();

    // Tăng lượt xem lên 1 (chưa có field views thì Firestore tự tạo từ 0)
    db.collection("articles").document(id).update("views", FieldValue.increment(1));

    // Đọc document đó và hiện lên màn hình, lượt xem đổi là tự cập nhật
    db.collection("articles").document(id).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
      @Override
      public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
        if (doc == null || !doc.exists())
          return;
        tv_detail_title.setText(doc.getString("username"));
        tv_detail_email.setText("Email: " + doc.getString("email"));
        tv_detail_hobby.setText("Hobby: " + doc.getString("hobby"));
        tv_detail_description.setText(doc.getString("desc"));
        Long views = doc.getLong("views");
        tv_detail_views.setText("Lượt xem: " + (views == null ? 0 : views));
        String image = doc.getString("avatar_url");
        if (image != null && !image.isEmpty())
          Picasso.get().load(image).resize(400, 500).centerCrop().into(iv_detail);
      }
    });

    // Nút Back: đóng màn chi tiết, quay về danh sách
    btn_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }
}
