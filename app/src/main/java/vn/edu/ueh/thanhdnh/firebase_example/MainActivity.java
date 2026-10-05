package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  FirebaseFirestore db;
  Button btAdd, btShow;
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
    etId = findViewById(R.id.etId);
    etTitle = findViewById(R.id.etTitle);
    etImage = findViewById(R.id.etImage);
    etDescription = findViewById(R.id.etDescription);
    btAdd.setOnClickListener(this);
    btShow.setOnClickListener(this);
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
    }
  }
}
