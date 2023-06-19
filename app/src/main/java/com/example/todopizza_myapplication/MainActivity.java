package com.example.todopizza_myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    int emotion[] = {R.drawable.gpimento, R.drawable.ham, R.drawable.mushroom, R.drawable.olive, R.drawable.onion, R.drawable.rpimento, R.drawable.tomato};
    private static final String TAG = "MainActivity";

    Fragment mainFragment;
    EditText inputToDo;
    Context context;
    TextView today;

    String imagePath;
    CheckBox checkBox;
    ImageView gpimentoToppings;
    Spinner emotion_Spinner;
    public static NoteDatabase noteDatabase = null;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Log.d("mytag","실행중");
        mainFragment = new MainFragment();
        today = findViewById(R.id.today);
        emotion_Spinner = findViewById(R.id.emotion_spinner);
        emotion_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                imagePath = getImagePath(i);
                imagePath = getSelectedImagePath();
                Log.d("mytag",""+imagePath); //imagePath 아이콘 뭔지 가져오기
                NoteAdapter.myimg = imagePath;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

            private String getImagePath(int i) {
                String[] imagePaths = {"gpimento", "ham", "mushroom","olive","onion","rpimento","tomato"};
                return imagePaths[i];
            }
            private String getSelectedImagePath() {
                // 이미지 스피너에서 선택된 이미지의 경로를 반환하는 로직 작성
                // 예시: 선택된 이미지의 경로를 스피너의 선택 인덱스를 통해 가져오는 방식
                int position = emotion_Spinner.getSelectedItemPosition(); // 스피너에서 선택된 인덱스
                return getImagePath(position); // 선택된 이미지의 경로 반환 (getImagePath() 메소드는 이전에 작성한 메소드를 사용)
            }
        });

        long now = System.currentTimeMillis(); //오늘 날짜 가져오는 로직
        Date date = new Date(now);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String time = mFormat.format(date);
        today.setText(time);
        EmotionCustomAdapter EmotioncustomAdapter = new EmotionCustomAdapter(getApplicationContext(), emotion);
        emotion_Spinner.setAdapter(EmotioncustomAdapter);
        //getSupportFragmentManager 을 이용하여 이전에 만들었던 **FrameLayout**에 `fragment_main.xml`이 추가
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                saveToDo();

                Toast.makeText(getApplicationContext(),"추가되었습니다.",Toast.LENGTH_SHORT).show();

            }
        });
        openDatabase();

    }

    private void saveToDo(){
        inputToDo = findViewById(R.id.inputToDo);
        //EditText에 적힌 글을 가져오기
        String todo = inputToDo.getText().toString();
        String toping = imagePath;

        //테이블에 값을 추가하는 sql구문 insert...
        String sqlSave = "insert into " + NoteDatabase.TABLE_NOTE + " (TODO, TOPPING) values (" +
                "'" + todo + "', '" + toping +"')"; //할 일 입력값 넣기
//        String sqlSave = "insert into " + NoteDatabase.TABLE_NOTE + " (TODO, TOPPING) values (" +
//                "'" + todo + "')";
        //sql문 실행
        NoteDatabase database = NoteDatabase.getInstance(context);
        database.execSQL(sqlSave);

        //저장과 동시에 EditText 안의 글 초기화
        inputToDo.setText("");
    }



    public void openDatabase() {
        // open database
        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }

        noteDatabase = NoteDatabase.getInstance(this);
        boolean isOpen = noteDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){}

    @Override
    public void onNothingSelected(AdapterView<?> parent){}


}