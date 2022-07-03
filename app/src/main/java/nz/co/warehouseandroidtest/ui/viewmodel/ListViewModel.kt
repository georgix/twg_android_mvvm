package nz.co.warehouseandroidtest.ui.viewmodel

import android.provider.MediaStore
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.data.model.SearchResult
import nz.co.warehouseandroidtest.data.model.User
import nz.co.warehouseandroidtest.domain.ListUseCase
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val listUseCase: ListUseCase) : ViewModel() {
    val user = MediatorLiveData<Result<User?>>()
    val products = MediatorLiveData<Result<SearchResult?>>()

    fun getUserId() {
        viewModelScope.launch {
            user.addSource(listUseCase.getUserId()) {
                user.value = it
            }
        }
    }

    fun getSearchProducts(query: Map<String, String>) {
        viewModelScope.launch {
            products.addSource(listUseCase.getSearchProducts(query)) {
                products.value = it
            }
        }
    }
}