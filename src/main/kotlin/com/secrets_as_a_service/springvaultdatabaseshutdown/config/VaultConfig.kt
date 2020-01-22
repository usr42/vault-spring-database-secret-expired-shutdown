package com.secrets_as_a_service.springvaultdatabaseshutdown.config

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.vault.core.lease.SecretLeaseContainer
import org.springframework.vault.core.lease.domain.RequestedSecret.Mode.RENEW
import org.springframework.vault.core.lease.event.SecretLeaseEvent
import org.springframework.vault.core.lease.event.SecretLeaseExpiredEvent
import javax.annotation.PostConstruct

// tag::autowire[]
@Configuration
class VaultConfig(
        private val leaseContainer: SecretLeaseContainer,
        @Value("\${spring.cloud.vault.database.role}")
        private val databaseRole: String,
        private val applicationContext: ConfigurableApplicationContext
) {
    // end::autowire[]

    // tag::postconstruct[]
    @PostConstruct
    private fun configureShutdownWhenLeaseExpires() {
        val vaultCredsPath = "database/creds/$databaseRole" // <1>
        leaseContainer.addLeaseListener { event -> // <2>
            if (event.path == vaultCredsPath) { // <3>
                log.info { "Lease change for DB: ($event) : (${event.lease})" }
                if (event.isLeaseExpired && event.mode == RENEW) { // <3>
                    log.error { "Database lease expired. Shutting down." }
                    applicationContext.close() // <4>
                }
            }
        }
    }
    // end::postconstruct[]

    // tag::extensions[]
    private val SecretLeaseEvent.path get() = source.path
    private val SecretLeaseEvent.isLeaseExpired get() = this is SecretLeaseExpiredEvent
    private val SecretLeaseEvent.mode get() = source.mode
    // end::extensions[]

    companion object {
        private val log = KotlinLogging.logger { }
    }
}
