package com.example.whatscookin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatscookin.models.Food;
import com.example.whatscookin.activities.FoodDetailActivity;
import com.example.whatscookin.databinding.ItemFoodBinding;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

/**
 * Adapter for home fragment
 * binds food images to spots in grid view and outlines items with under 1/2 original quantity
 */
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{

    private Context context;
    private List<Food> foodList;
    private ItemFoodBinding binding;

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
        final Food food = foodList.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivFood;

        public ViewHolder(@NonNull View itemView) {
            super(binding.getRoot());
            ivFood = binding.ivFood;

            // taping an icon brings a detailed view
            // holding an icon removes it
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    remove();
                    return true;
                }
            });
        }

        private void remove() {
            final int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                final Food food = foodList.get(position);
                food.deleteEventually();
                foodList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, foodList.size());

            }
        }

        public void bind(Food food) {
            final ParseFile image = food.getImage();
            Glide.with(context).load(image.getUrl()).into(ivFood);
            if ((food.getCurrentQuantity() * 2) <= food.getOriginalQuantity()){
                ivFood.setBackgroundColor(0xFFF498AD);
            }
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Food food = foodList.get(position);
                Intent intent = new Intent(context, FoodDetailActivity.class);
                intent.putExtra(Food.class.getSimpleName(), Parcels.wrap(food));
                context.startActivity(intent);
            }
        }
    }
}
