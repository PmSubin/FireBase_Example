package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  // File JSON bài viết của bài Article trước
  private static final String JSON_URL = "https://raw.githubusercontent.com/thanhdnh/json/main/products.json";

  FirebaseFirestore db;
  Button btAdd, btShow, btLoad;
  EditText etId, etTitle, etImage, etDescription;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    FirebaseApp.initializeApp(this);
    db = FirebaseFirestore.getInstance();
    btAdd = findViewById(R.id.btAdd);
    btShow = findViewById(R.id.btShow);
    btLoad = findViewById(R.id.btLoad);
    etId = findViewById(R.id.etId);
    etTitle = findViewById(R.id.etTitle);
    etImage = findViewById(R.id.etImage);
    etDescription = findViewById(R.id.etDescription);
    btAdd.setOnClickListener(this);
    btShow.setOnClickListener(this);
    btLoad.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.btAdd) {
      String id = etId.getText().toString().trim();
      String title = etTitle.getText().toString().trim();
      if (id.isEmpty() || title.isEmpty()) {
        Toast.makeText(this, "Nhập ít nhất ID và Title", Toast.LENGTH_SHORT).show();
        return;
      }
      Article article = new Article(Integer.parseInt(id), title,
        etImage.getText().toString().trim(), etDescription.getText().toString().trim());
      // Code cũ của thầy lưu User: db.collection("users").add(new User(name, phone));
      // Giờ lưu Article vào collection "articles", lấy ID bài viết làm tên document
      db.collection("articles").document(id).set(article);
      etId.setText("");
      etTitle.setText("");
      etImage.setText("");
      etDescription.setText("");
      Toast.makeText(this, "Đã lưu bài viết vào Firestore", Toast.LENGTH_SHORT).show();
    } else if (view.getId() == R.id.btShow) {
      Intent intent = new Intent(getBaseContext(), ShowDataActivity.class);
      startActivity(intent);
    } else if (view.getId() == R.id.btLoad) {
      loadArticlesFromJson();
    }
  }

  // Tải file JSON bài viết (giống bài Article trước) rồi lưu tất cả vào Firestore
  private void loadArticlesFromJson() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(JSON_URL).build();
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(@NonNull Call call, @NonNull IOException e) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Tải JSON thất bại", Toast.LENGTH_SHORT).show());
      }

      @Override
      public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        if (!response.isSuccessful() || response.body() == null) {
          runOnUiThread(() -> Toast.makeText(MainActivity.this, "Tải JSON thất bại", Toast.LENGTH_SHORT).show());
          return;
        }
        String json = response.body().string();
        ArticleList list = new Gson().fromJson(json, ArticleList.class);
        for (Article article : list.getArticles()) {
          db.collection("articles").document(String.valueOf(article.getArticle_id())).set(article);
        }
        int count = list.getArticles().size();
        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Đã lưu " + count + " bài viết vào Firestore", Toast.LENGTH_SHORT).show());
      }
    });
  }
}
