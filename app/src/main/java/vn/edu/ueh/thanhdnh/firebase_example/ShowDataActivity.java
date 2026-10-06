package vn.edu.ueh.thanhdnh.firebase_example;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShowDataActivity extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView recyclerView;
    List<Article> articles = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        //users.add(new User("default", "000"));

        recyclerView = findViewById(R.id.reclyclerview);
        ArticleViewAdapter adapter = new ArticleViewAdapter(this, articles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        /*db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful()){
              users.clear();
              for(QueryDocumentSnapshot q : task.getResult()){
                Map<String, Object> data = q.getData();
                User user = new User((String)data.get("name"), (String)data.get("phone"));
                users.add(user);
              }
              adapter.update(users);
              adapter.notifyDataSetChanged();
            }
          }
        });*/
      // Đọc collection "articles", sắp xếp theo id, tự cập nhật khi dữ liệu đổi (kể cả lượt xem)
      db.collection("articles").orderBy("id").addSnapshotListener(new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
          if (snapshots != null) {
            articles.clear();
            for (QueryDocumentSnapshot q : snapshots) {
              Map<String, Object> data = q.getData();
              // Firestore lưu số nguyên kiểu Long nên đổi về int
              Article article = new Article(((Long) data.get("id")).intValue(),
                (String) data.get("username"), (String) data.get("email"),
                (String) data.get("desc"), (String) data.get("avatar_url"),
                (String) data.get("hobby"));
              // Bài chưa ai xem thì chưa có field views, coi như 0
              if (data.get("views") != null)
                article.setViews((Long) data.get("views"));
              articles.add(article);
            }
            adapter.update(articles);
            adapter.notifyDataSetChanged();
          }
        }
      });
    }
}
