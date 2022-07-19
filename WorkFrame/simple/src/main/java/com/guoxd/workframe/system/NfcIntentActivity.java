package com.guoxd.workframe.system;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.guoxd.workframe.R;

public class NfcIntentActivity extends AppCompatActivity {
    NfcAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base_recycler);
        initRecycler();
        initNFC();
    }

    private void initRecycler() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
    }

    private void initNFC() {
    }

    class NfcBean extends RecyclerView.ViewHolder{
        public NfcBean(View item){
            super(item);
        }
    }
    class NfcAdapter extends RecyclerView.Adapter<NfcBean>{
        @NonNull
        @Override
        public NfcBean onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull NfcBean holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
