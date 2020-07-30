package com.example.whatscookin.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatscookin.databinding.ItemTagBinding;

import org.json.JSONArray;
import org.json.JSONException;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder>{

    private Context context;
    private JSONArray tags;
    private ItemTagBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public TagAdapter(Context context, JSONArray tags) {
        this.context = context;
        this.tags = tags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemTagBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tag = "";
        try {
            tag = tags.getString(position);
            Log.i("tag", tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.bind(tag);
    }

    @Override
    public int getItemCount() {
        return tags.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTag = binding.tvTag;

        }

        public void bind(String tag) {
            tvTag.setText(tag);
            if (tag.isEmpty()) {
                Log.i("tag", "I'm here" + tag);
                tvTag.setVisibility(View.GONE);
            }
        }
    }
}
