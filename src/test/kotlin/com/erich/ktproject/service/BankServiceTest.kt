package com.erich.ktproject.service

import com.erich.ktproject.datasource.BankDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BankServiceTest {

 private val dataSource: BankDataSource = mockk<BankDataSource>(relaxed = true)
 private val bankService = BankService(dataSource)

 @Test
  fun `should be able to retrieve banks`() {
     // given
//    every { dataSource.retrieveBanks() } returns emptyList()

     // when
     val banks = bankService.getBanks()
     // then
     verify(exactly = 1) { dataSource.retrieveBanks() }
//     assertEquals(3, banks.size)
  }

}