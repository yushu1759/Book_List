package com.jnu.booklist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    public static final int RESULT_CODE_ADD_DATA = 996;
    public static final int REQUEST_CODE_ADD = 123;
    public static final int REQUEST_CODE_EDIT = REQUEST_CODE_ADD+1;
    private List<BookItem> bookItems;
    private MyRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE_ADD){
            if(resultCode== RESULT_CODE_ADD_DATA) {
                String name=data.getStringExtra("name");
                int position=data.getIntExtra("position",bookItems.size());
                bookItems.add(position,new BookItem(name,R.drawable.book_no_name));
                recyclerViewAdapter.notifyItemInserted(position);
            }
        }
        if(requestCode==REQUEST_CODE_EDIT) {
            if (resultCode == RESULT_CODE_ADD_DATA) {
                String name = data.getStringExtra("name");
                int position = data.getIntExtra("position", bookItems.size());
                bookItems.get(position).setName(name);
                recyclerViewAdapter.notifyItemChanged(position);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        initData();

        RecyclerView mainRecyclerView = findViewById(R.id.recycle_view_books);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new MyRecyclerViewAdapter(bookItems);
        mainRecyclerView.setAdapter(recyclerViewAdapter);
    }

    public void initData() {
        bookItems = new ArrayList<BookItem>();
        bookItems.add(new BookItem("软件项目管理案例教程（第4版）", R.drawable.book_2));
        bookItems.add(new BookItem("创新工程实践", R.drawable.book_no_name));
        bookItems.add(new BookItem("信息安全数学基础（第2版）", R.drawable.book_1));


    }


    private class MyRecyclerViewAdapter extends RecyclerView.Adapter {
        private List<BookItem> bookItems;

        public MyRecyclerViewAdapter(List<BookItem> bookItems) {
            this.bookItems = bookItems;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_item_holder, parent, false);

            return new MyRecyclerViewAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder Holder, int position) {
            MyRecyclerViewAdapter.MyViewHolder holder = (MyRecyclerViewAdapter.MyViewHolder) Holder;

            holder.getImageView().setImageResource(bookItems.get(position).getPictureId());
            holder.getTextViewName().setText(bookItems.get(position).getName());

        }

        @Override
        public int getItemCount() {
            return bookItems.size();
        }

        private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
            public static final int CONTEXT_MENU_ID_ADD = 1;
            public static final int CONTEXT_MENU_ID_UPDATE = CONTEXT_MENU_ID_ADD+1;
            public static final int CONTEXT_MENU_ID_DELETE = CONTEXT_MENU_ID_ADD+2;
            private final ImageView imageView;
            private final TextView textViewName;

            public MyViewHolder(View itemview) {
                super(itemview);

                this.imageView = itemview.findViewById(R.id.image_view_book_cover);
                this.textViewName = itemview.findViewById(R.id.text_view_book_title);

                itemview.setOnCreateContextMenuListener(this);
            }

            public ImageView getImageView() {
                return imageView;
            }

            public TextView getTextViewName() {
                return textViewName;
            }


            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo menuInfo) {
                int position=getAdapterPosition();
                MenuItem menuItemAdd=contextMenu.add(Menu.NONE,CONTEXT_MENU_ID_ADD,CONTEXT_MENU_ID_ADD,"Add"+position);
                MenuItem menuItemEdit=contextMenu.add(Menu.NONE,CONTEXT_MENU_ID_UPDATE,CONTEXT_MENU_ID_UPDATE,"Edit"+position);
                MenuItem menuItemDelete=contextMenu.add(Menu.NONE,CONTEXT_MENU_ID_DELETE,CONTEXT_MENU_ID_DELETE,"Delete");

                menuItemAdd.setOnMenuItemClickListener(this);
                menuItemEdit.setOnMenuItemClickListener(this);
                menuItemDelete.setOnMenuItemClickListener(this);

            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int position=getAdapterPosition();
                Intent intent;
                switch(menuItem.getItemId())
                {
                    case CONTEXT_MENU_ID_ADD:
                        intent=new Intent(BookListMainActivity.this,EditBookActivity.class);
                        intent.putExtra("position",position);
                        BookListMainActivity.this.startActivityForResult(intent, REQUEST_CODE_ADD);
                        break;
                    case CONTEXT_MENU_ID_UPDATE:
                        intent=new Intent(BookListMainActivity.this,EditBookActivity.class);
                        intent.putExtra("position",position);
                        intent.putExtra("name",bookItems.get(position).getName());
                        BookListMainActivity.this.startActivityForResult(intent, REQUEST_CODE_EDIT);
                        break;
                    case CONTEXT_MENU_ID_DELETE:
                        bookItems.remove(position);
                        MyRecyclerViewAdapter.this.notifyItemRemoved(position);
                        break;

                }

                Toast.makeText(BookListMainActivity.this,"点击了"+menuItem.getItemId(),Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }
}
