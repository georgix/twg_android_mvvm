package nz.co.warehouseandroidtest.data.model

data class ProductDetail (
    var MachineID: String,
    var Action: String,
    var ScanBarcode: String,
    var ScanID: String,
    var UserDescription: String,
    var Product: Product,
    var ProdQAT: String,
    var ScanDateTime: String,
    var Found: String,
    var UserID: String,
    var Branch: String,
)