package com.jnu.booklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        Intent intent=getIntent();
        int position=intent.getIntExtra("position",0);

        EditText editTextName=findViewById(R.id.edit_text_name);
        String name=intent.getStringExtra("name");
        if(null!=name){
            editTextName.setText(name);
        }

        Button buttonConfirm=this.findViewById(R.id.button_confirm);
        buttonConfirm.setOnClickListener(view -> {
            Intent intent1 =new Intent();
            intent1.putExtra("position",position);
            intent1.putExtra("name",editTextName.getText().toString());
            setResult(BookListFragment.RESULT_CODE_ADD_DATA, intent1);
            EditBookActivity.this.finish();
        });

        Button buttonCancel=this.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(view -> {
                EditBookActivity.this.finish();
        });
    }
}
