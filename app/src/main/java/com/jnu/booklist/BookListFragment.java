package com.jnu.booklist;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.booklist.data.BookItem;
import com.jnu.booklist.data.DataBank;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookListFragment extends Fragment {
    public static final int RESULT_CODE_ADD_DATA = 996;
    private List<BookItem> bookItems;
    private BookListFragment.MyRecyclerViewAdapter recyclerViewAdapter;

    ActivityResultLauncher<Intent> launcherAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            int resultCode = result.getResultCode();
            if (resultCode == RESULT_CODE_ADD_DATA) {
                if (null == data) return;
                String name = data.getStringExtra("name");
                int position = data.getIntExtra("position", bookItems.size());
                bookItems.add(position, new BookItem(name, R.drawable.book_no_name));
                dataBank.saveData();
                recyclerViewAdapter.notifyItemInserted(position);

            }
        }
    });

    ActivityResultLauncher<Intent> launcherEdit = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            int resultCode = result.getResultCode();
            if (resultCode == RESULT_CODE_ADD_DATA) {
                if (null == data) return;
                String name = data.getStringExtra("name");
                int position = data.getIntExtra("position", bookItems.size());
                bookItems.get(position).setName(name);
                dataBank.saveData();
                recyclerViewAdapter.notifyItemInserted(position);
            }
        }
    });
    private DataBank dataBank;

    public BookListFragment() {
        // Required empty public constructor
    }


    public static BookListFragment newInstance() {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);
        initData();

        FloatingActionButton fabAdd = rootView.findViewById(R.id.floating_action_button_add);
        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this.getContext(), EditBookActivity.class);
            intent.putExtra("position", bookItems.size());
            launcherAdd.launch(intent);
        });

        RecyclerView mainRecyclerView = rootView.findViewById(R.id.recycle_view_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mainRecyclerView.setLayoutManager(layoutManager);

        recyclerViewAdapter = new BookListFragment.MyRecyclerViewAdapter(bookItems);
        mainRecyclerView.setAdapter(recyclerViewAdapter);

        return rootView;
    }

    public void initData() {
        dataBank = new DataBank(this.getContext());
        bookItems = dataBank.loadData();
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<BookListFragment.MyRecyclerViewAdapter.MyViewHolder> {
        private List<BookItem> bookItems;

        public MyRecyclerViewAdapter(List<BookItem> bookItems) {
            this.bookItems = bookItems;
        }

        @NonNull
        @Override
        public BookListFragment.MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_item_holder, parent, false);

            return new BookListFragment.MyRecyclerViewAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BookListFragment.MyRecyclerViewAdapter.MyViewHolder holder, int position) {
            holder.getImageView().setImageResource(bookItems.get(position).getPictureId());
            holder.getTextViewName().setText(bookItems.get(position).getName());

        }

        @Override
        public int getItemCount() {
            return bookItems.size();
        }

        private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
            public static final int CONTEXT_MENU_ID_ADD = 1;
            public static final int CONTEXT_MENU_ID_UPDATE = CONTEXT_MENU_ID_ADD + 1;
            public static final int CONTEXT_MENU_ID_DELETE = CONTEXT_MENU_ID_ADD + 2;

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
                int position = getAdapterPosition();
                MenuItem menuItemAdd = contextMenu.add(Menu.NONE, CONTEXT_MENU_ID_ADD, CONTEXT_MENU_ID_ADD, BookListFragment.this.getResources().getString(R.string.string_menu_add));
                MenuItem menuItemEdit = contextMenu.add(Menu.NONE, CONTEXT_MENU_ID_UPDATE, CONTEXT_MENU_ID_UPDATE, BookListFragment.this.getResources().getString(R.string.string_menu_edit));
                MenuItem menuItemDelete = contextMenu.add(Menu.NONE, CONTEXT_MENU_ID_DELETE, CONTEXT_MENU_ID_DELETE, BookListFragment.this.getResources().getString(R.string.string_menu_delete));

                menuItemAdd.setOnMenuItemClickListener(this);
                menuItemEdit.setOnMenuItemClickListener(this);
                menuItemDelete.setOnMenuItemClickListener(this);

            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int position = getAdapterPosition();
                Intent intent;
                switch (menuItem.getItemId()) {
                    case CONTEXT_MENU_ID_ADD:
                        intent = new Intent(BookListFragment.this.getContext(), EditBookActivity.class);
                        intent.putExtra("position", position);
                        launcherAdd.launch(intent);
                        break;
                    case CONTEXT_MENU_ID_UPDATE:
                        intent = new Intent(BookListFragment.this.getContext(), EditBookActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("name", bookItems.get(position).getName());
                        launcherEdit.launch(intent);
                        break;
                    case CONTEXT_MENU_ID_DELETE:
                        AlertDialog.Builder alertDB = new AlertDialog.Builder(BookListFragment.this.getContext());
                        alertDB.setPositiveButton(BookListFragment.this.getResources().getString(R.string.string_confirmation), (dialogInterface, i) -> {
                            bookItems.remove(position);
                            dataBank.saveData();
                            BookListFragment.MyRecyclerViewAdapter.this.notifyItemRemoved(position);
                        });
                        alertDB.setNegativeButton(BookListFragment.this.getResources().getString(R.string.string_cancel), (dialogInterface, i) -> {

                        });
                        alertDB.setMessage(BookListFragment.this.getResources().getString(R.string.string_confirm_delete) + bookItems.get(position).getName() + "？");
                        alertDB.setTitle(BookListFragment.this.getResources().getString(R.string.hint)).show();
                        break;

                }

                return false;
            }
        }
    }
}