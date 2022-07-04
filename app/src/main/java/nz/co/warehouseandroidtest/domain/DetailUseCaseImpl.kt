package nz.co.warehouseandroidtest.domain

import androidx.lifecycle.LiveData
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.data.model.ProductDetail
import nz.co.warehouseandroidtest.data.model.SearchResult
import nz.co.warehouseandroidtest.data.model.User
import nz.co.warehouseandroidtest.data.repository.PriceRepository
import nz.co.warehouseandroidtest.data.repository.SearchRepository
import nz.co.warehouseandroidtest.data.repository.UserRepository
import javax.inject.Inject

class DetailUseCaseImpl @Inject
    constructor(private val priceRepository: PriceRepository) : DetailUseCase {

    override fun getProductDetail(query: Map<String, String>): LiveData<Result<ProductDetail?>> {
        return priceRepository.getProductDetail(query)
    }
}