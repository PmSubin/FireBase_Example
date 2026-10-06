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
  // File dữ liệu của bài UserProfile trước (repo PhotoApplab8 của anh)
  private static final String JSON_URL = "https://raw.githubusercontent.com/PmSubin/PhotoApplab8/master/users.json";

  FirebaseFirestore db;
  Button btAdd, btShow, btLoad;
  EditText etId, etUsername, etEmail, etHobby, etAvatar, etDesc;

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
    etUsername = findViewById(R.id.etUsername);
    etEmail = findViewById(R.id.etEmail);
    etHobby = findViewById(R.id.etHobby);
    etAvatar = findViewById(R.id.etAvatar);
    etDesc = findViewById(R.id.etDesc);
    btAdd.setOnClickListener(this);
    btShow.setOnClickListener(this);
    btLoad.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.btAdd) {
      String id = etId.getText().toString().trim();
      String username = etUsername.getText().toString().trim();
      if (id.isEmpty() || username.isEmpty()) {
        Toast.makeText(this, "Nhập ít nhất ID và Username", Toast.LENGTH_SHORT).show();
        return;
      }
      Article article = new Article(Integer.parseInt(id), username,
        etEmail.getText().toString().trim(), etDesc.getText().toString().trim(),
        etAvatar.getText().toString().trim(), etHobby.getText().toString().trim());
      // Code cũ của thầy lưu User: db.collection("users").add(new User(name, phone));
      // Giờ lưu Article vào collection "articles", lấy ID làm tên document
      db.collection("articles").document(id).set(article);
      etId.setText("");
      etUsername.setText("");
      etEmail.setText("");
      etHobby.setText("");
      etAvatar.setText("");
      etDesc.setText("");
      Toast.makeText(this, "Đã lưu vào Firestore", Toast.LENGTH_SHORT).show();
    } else if (view.getId() == R.id.btShow) {
      Intent intent = new Intent(getBaseContext(), ShowDataActivity.class);
      startActivity(intent);
    } else if (view.getId() == R.id.btLoad) {
      loadFromJson();
    }
  }

  // Tải users.json của bài trước rồi lưu tất cả lên Firestore (id làm tên document)
  private void loadFromJson() {
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
        for (Article article : list.getUsers()) {
          db.collection("articles").document(String.valueOf(article.getId())).set(article);
        }
        int count = list.getUsers().size();
        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Đã lưu " + count + " người vào Firestore", Toast.LENGTH_SHORT).show());
      }
    });
  }
}
