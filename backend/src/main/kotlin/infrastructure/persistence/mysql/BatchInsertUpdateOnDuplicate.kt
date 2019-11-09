package infrastructure.persistence.mysql

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager

class BatchInsertUpdateOnDuplicate(table: Table, val onDupUpdate: List<Column<*>>): BatchInsertStatement(table, false) {
    override fun prepareSQL(transaction: Transaction): String {
        val onUpdateSQL = if(onDupUpdate.isNotEmpty()) {
            " ON DUPLICATE KEY UPDATE " + onDupUpdate.joinToString { "${transaction.identity(it)}=VALUES(${transaction.identity(it)})" }
        } else ""
        return super.prepareSQL(transaction) + onUpdateSQL
    }
}

fun <T: Table, E> T.batchInsertOnDuplicateKeyUpdate(data: List<E>, onDupUpdateColumns: List<Column<*>>, body: T.(BatchInsertUpdateOnDuplicate, E) -> Unit): List<Int> {
    return data.takeIf { it.isNotEmpty() }?.let {
        val insert = BatchInsertUpdateOnDuplicate(this, onDupUpdateColumns)
        data.forEach {
            insert.addBatch()
            body(insert, it)
        }
        TransactionManager.current().exec(insert)
        columns.firstOrNull { it.columnType.isAutoInc }?.let { idCol ->
            insert.generatedKey?.mapNotNull {
                val value = it[idCol]
                when (value) {
                    is Long -> value.toInt()
                    is Int -> value
                    null -> null
                    else -> error("can't find primary key of type Int or Long; map['$idCol']='$value' (where map='$it')")
                }
            }
        }
    }.orEmpty()
}
