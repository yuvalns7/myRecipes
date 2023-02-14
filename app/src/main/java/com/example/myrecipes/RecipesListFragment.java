package com.example.myrecipes;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myrecipes.databinding.FragmentRecipesListBinding;
import com.example.myrecipes.model.recipe.Recipe;
import com.example.myrecipes.model.recipe.RecipeModel;

public class RecipesListFragment extends Fragment {
    FragmentRecipesListBinding binding;
    RecipeRecyclerAdapter adapter;
    RecipesListFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecipesListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(RecipesListFragmentViewModel.class);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeRecyclerAdapter(getLayoutInflater(),viewModel.getData().getValue());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecipeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG", "Row was clicked " + pos);
                Recipe recipe = viewModel.getData().getValue().get(pos);
                Navigation.findNavController(view).navigate(RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipe));
            }
        });

        binding.progressBar.setVisibility(View.GONE);

        viewModel.getData().observe(getViewLifecycleOwner(),list->{
            adapter.setData(list);
            if (adapter.getItemCount() == 0){
                binding.noRecipesImg.setVisibility(View.VISIBLE);
            } else {
                binding.noRecipesImg.setVisibility(View.GONE);
            }
        });

        RecipeModel.instance().EventListLoadingState.observe(getViewLifecycleOwner(), status->{
            binding.swipeRefresh.setRefreshing(status == RecipeModel.LoadingState.LOADING);
        });

        binding.swipeRefresh.setOnRefreshListener(()->{
            reloadData();
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(RecipesListFragmentViewModel.class);
    }

    void reloadData(){
        RecipeModel.instance().refreshAllRecipes();
    }
}