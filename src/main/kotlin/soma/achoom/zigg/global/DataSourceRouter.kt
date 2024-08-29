package soma.achoom.zigg.global

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

open class DataSourceRouter : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any {
        val readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly()
        return if (readOnly) "read" else "write"
    }
}