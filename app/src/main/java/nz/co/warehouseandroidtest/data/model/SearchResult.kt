package nz.co.warehouseandroidtest.data.model

data class SearchResult (
    var HitCount: String,
    var Results: List<SearchResultItem?>,
    var SearchID: String,
    var ProdQAT: String,
    var Found: String,
)