package com.example.foodorderingapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderingapp.Repositories.CategoryRepository
import com.example.foodorderingapp.Repositories.FoodItemRepository
import com.example.foodorderingapp.Response.CustomResponse
import com.example.foodorderingapp.models.Category
import com.example.foodorderingapp.models.FoodItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val foodItemRepository: FoodItemRepository
) : ViewModel() {

    val categoryList: LiveData<CustomResponse<List<Category>>>
        get() = categoryRepository.categoriesList

    val foodItemList: LiveData<CustomResponse<List<FoodItem>>>
        get() = foodItemRepository.foodItemList

    var category : Category? = null

//    val errorMessage:MutableStateFlow<String?> = MutableStateFlow(null)
//    val successMessage:MutableStateFlow<String?> = MutableStateFlow(null)


    init {
        viewModelScope.launch {
            categoryRepository.getCategoriesList()
            foodItemRepository.getFoodItemList()
        }
    }



    fun getFoodItemsByCategory(category: Category): MutableList<FoodItem> {
        val filteredItems = mutableListOf<FoodItem>()
        filteredItems.addAll(foodItemList.value?.data?.filter { it.categoryId == category.id }?: emptyList())
        return filteredItems

    }


    fun setFoodItem(foodItem: FoodItem){
        foodItemRepository.createFoodItem(foodItem){ success, exception ->
            if(success){
//                successMessage.value = "Category Added Successfully"
            }
            else{
//                errorMessage.value = exception?.message
            }

        }


    }

    fun stopObservingData(){
        categoryRepository.stopObservingData()
        foodItemRepository.stopObservingData()
    }

    override fun onCleared() {
        super.onCleared()
        categoryRepository.stopObservingData()
        foodItemRepository.stopObservingData()
    }


}