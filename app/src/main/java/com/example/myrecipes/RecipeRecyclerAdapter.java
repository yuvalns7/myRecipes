package com.example.myrecipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;

import com.example.myrecipes.databinding.FragmentAddRecipeBinding;
import com.example.myrecipes.databinding.FragmentRecipesListBinding;
import com.example.myrecipes.databinding.RecipeRowBinding;
import com.example.myrecipes.model.recipe.Recipe;
import com.example.myrecipes.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

class RecipeViewHolder extends RecyclerView.ViewHolder{

    RecipeRowBinding binding;
    List<Recipe> data;
    ImageView avatarImage;
    public RecipeViewHolder(@NonNull View itemView, RecipeRecyclerAdapter.OnItemClickListener listener,
                            List<Recipe> data) {
        super(itemView);
        this.binding = RecipeRowBinding.bind(itemView);;
        this.data = data;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                listener.onItemClick(pos);
            }
        });
    }

    public void bind(Recipe recipe, int pos) {
        binding.recipeRowNameTv.setText(recipe.getName());
        binding.recipeRowCategoryTv.setText(recipe.getCategory());
        binding.recipeRowUserTv.setText(recipe.getUsername());
        if (StringUtils.isBlank(recipe.getImgUrl())) {
            Picasso.get().load(recipe.getImgUrl()).placeholder(R.drawable.chef_avatar).into(binding.recipeRowAvatarImg);
        }else{
            binding.recipeRowAvatarImg.setImageResource(R.drawable.chef_avatar);
        }
    }
}

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder>{
    OnItemClickListener listener;
    public static interface OnItemClickListener{
        void onItemClick(int pos);
    }
    LayoutInflater inflater;
    List<Recipe> data;

    public void setData(List<Recipe> data){
        this.data = data;
        notifyDataSetChanged();
    }
    public RecipeRecyclerAdapter(LayoutInflater inflater, List<Recipe> data){
        this.inflater = inflater;
        this.data = data;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipe_row,parent,false);
        return new RecipeViewHolder(view,listener, data);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = data.get(position);
        holder.bind(recipe,position);
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }
}

