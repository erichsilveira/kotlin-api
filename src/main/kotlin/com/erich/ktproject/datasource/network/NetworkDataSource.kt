package com.erich.ktproject.datasource.network

import com.erich.ktproject.datasource.BankDataSource
import com.erich.ktproject.datasource.network.dto.BankList
import com.erich.ktproject.model.Bank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.io.IOException

@Repository("network")
class NetworkDataSource(
    @Autowired private val restTemplate: RestTemplate
) : BankDataSource {
    override fun retrieveBanks(): Collection<Bank> {
        val  response = restTemplate.getForEntity<BankList>("http://someHost/banks")
        return response.body?.results ?: throw IOException("Could not fetch banks from the network")
    }

    override fun retrieveBank(accountNumber: String): Bank {
        TODO("Not yet implemented")
    }

    override fun createBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun updateBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun deleteBank(accountNumber: String) {
        TODO("Not yet implemented")
    }

}