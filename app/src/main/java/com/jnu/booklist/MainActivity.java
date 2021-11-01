package com.jnu.booklist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public static class BookListMainActivity extends AppCompatActivity {

        private List<com.jnu.booklist.BookItem> bookItems;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_book_list_main);

            initData();

            RecyclerView mainRecyclerView=findViewById(R.id.recycle_view_books);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mainRecyclerView.setLayoutManager(layoutManager);
            mainRecyclerView.setAdapter(new MyRecyclerViewAdapter(bookItems));
        }

        public void initData(){
            bookItems=new ArrayList<com.jnu.booklist.BookItem>();
            bookItems.add(new com.jnu.booklist.BookItem("软件项目管理案例教程（第4版）",R.drawable.book_2));
            bookItems.add(new com.jnu.booklist.BookItem("创新工程实践",R.drawable.book_no_name));
            bookItems.add(new com.jnu.booklist.BookItem("信息安全数学基础（第2版）",R.drawable.book_1));


        }


        private class MyRecyclerViewAdapter extends RecyclerView.Adapter {
            private final List<com.jnu.booklist.BookItem> bookItems;

            public MyRecyclerViewAdapter(List<BookItem> bookItems) {
                this.bookItems=bookItems;
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_item_holder, parent, false);

                return new MyViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder Holder, int position) {
                MyViewHolder holder=(MyViewHolder) Holder;

                holder.getImageView().setImageResource(bookItems.get(position).getPictureId());
                holder.getTextViewName().setText(bookItems.get(position).getName());

            }

            @Override
            public int getItemCount() {
                return bookItems.size();
            }

            private class MyViewHolder extends RecyclerView.ViewHolder {
                private final ImageView imageView;
                private final TextView textViewName;

                public MyViewHolder(View view) {
                    super(view);

                    this.imageView=view.findViewById(R.id.imageView_book_item);
                    this.textViewName=view.findViewById(R.id.textView_book_item_name);
                }

                public ImageView getImageView() {
                    return imageView;
                }

                public TextView getTextViewName() {
                    return textViewName;
                }
            }
        }
    }
}