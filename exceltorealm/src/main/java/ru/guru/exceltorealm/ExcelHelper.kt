package ru.guru.exceltorealm

import android.content.Context
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType

class ExcelHelper {
    fun getBooks(context: Context): ArrayList<Book>{
        val result: ArrayList<Book> = ArrayList()
        val files = context.assets.list("")
        for (file in files!!){
            if (file.contains(".xls")){
                val mBook = Book(file.replace(".xls", ""))
                val mFile = context.assets.open(file)
                for (sheet in HSSFWorkbook(mFile)){
                    val mSheet = Sheet(sheet.sheetName)
                    for (row in sheet){
                        val mRow = Row()
                        for (cell in row){
                                if (cell != null) {
                                    mRow.cells.add(getCellValue(cell).toString())
                                }
                             }
                        mSheet.rows.add(mRow)
                    }
                    mBook.sheets.add(mSheet)
                }
                mFile.close()
                result.add(mBook)
            } }
        return result
    }

    private fun getCellValue(cell: Cell): Any? {
        when (cell.cellType) {
            CellType.STRING -> return cell.stringCellValue
            CellType.BLANK -> return ""
            CellType.BOOLEAN -> return cell.booleanCellValue
            CellType.ERROR -> return cell.errorCellValue
            CellType.FORMULA ->  return cell.cellFormula
            CellType.NUMERIC -> return cell.numericCellValue
            else -> {}
        }
        return null
    }

    data class Book(
        val title: String,
        var sheets: ArrayList<Sheet> = ArrayList()
    )

    data class Sheet(
        val title: String,
        var rows: ArrayList<Row> = ArrayList(),
    )

    data class Row(
        var cells: ArrayList<String> = ArrayList()
    )

}