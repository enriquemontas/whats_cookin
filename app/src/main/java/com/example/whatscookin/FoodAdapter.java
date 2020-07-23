package com.example.whatscookin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatscookin.activities.FoodDetailActivity;
import com.example.whatscookin.activities.MainActivity;
import com.example.whatscookin.databinding.ItemFoodBinding;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{

    Context context;
    List<Food> foodList;
    ItemFoodBinding binding;

    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemFoodBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void addAll(List<Food> foodList) {
        this.foodList.addAll(foodList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivFood;

        public ViewHolder(@NonNull View itemView) {
            super(binding.getRoot());
            ivFood = binding.ivFood;
            itemView.setOnClickListener(this);
        }

        public void bind(Food food) {
            ParseFile image = food.getImage();
            Glide.with(context).load(image.getUrl()).into(ivFood);
            if (food.getCurrentQuantity() <= food.getOriginalQuantity() / 2){
                ivFood.setBackgroundColor(0xFFF498AD);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Food food = foodList.get(position);
                Intent intent = new Intent(context, FoodDetailActivity.class);
                intent.putExtra(Food.class.getSimpleName(), Parcels.wrap(food));
                context.startActivity(intent);
            }
        }
    }
}
