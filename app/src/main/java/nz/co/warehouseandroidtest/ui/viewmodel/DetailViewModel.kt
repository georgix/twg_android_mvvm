package nz.co.warehouseandroidtest.ui.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.data.model.ProductDetail
import nz.co.warehouseandroidtest.domain.DetailUseCase
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val detailUseCase: DetailUseCase) : ViewModel() {
    val productDetail = MediatorLiveData<Result<ProductDetail?>>()

    fun getSearchProducts(query: Map<String, String>) {
        viewModelScope.launch {
            productDetail.addSource(detailUseCase.getProductDetail(query)) {
                productDetail.value = it
            }
        }
    }
}