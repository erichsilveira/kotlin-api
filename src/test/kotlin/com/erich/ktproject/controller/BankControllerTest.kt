package com.erich.ktproject.controller

import com.erich.ktproject.model.Bank
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class BankControllerTest @Autowired constructor (
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    val basePath = "/api/banks"

    @Nested
    @DisplayName("GET /banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks {
        @Test
        fun `should return all banks`() {
            // when/then
            mockMvc.get(basePath)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType("application/json") }
                    jsonPath("$[0].accountNumber") { value("1234") }
                }
        }
    }

    @Nested
    @DisplayName("GET /banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return the bank with given account number`() {
            // given
            val accountNumber = 1234

            // when/then
            mockMvc.get("$basePath/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType("application/json") }
                    jsonPath("$.trust") { value("3.14") }
                }
        }

        @Test
        fun `it should return not found if the account number does not exist`() {
            // given
            val accountNumber = "dummy_val"

            // when/then
            mockMvc.get("/api/banks/$accountNumber")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("POST /banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostBank {
        @Test
        fun `should create a new bank`() {
            // given
            val newBank = Bank("12345", 3.14, 17)

            // when/then
            mockMvc.post(basePath) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType("application/json")
                        json(objectMapper.writeValueAsString(newBank))

                    }
                }

            mockMvc.get("$basePath/${newBank.accountNumber}")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType("application/json")
                        json(objectMapper.writeValueAsString(newBank))
                    }
                }
        }

        @Test
        fun `should return BAD REQUEST if given account number already exists`() {
            // given
            val newBank = Bank("1234", 3.14, 17)

            // when/then
            mockMvc.post(basePath) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    @DisplayName("PATCH /banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchBank {
        @Test
        fun `should updated an existent bank`() {
            // given
            val updatedBank = Bank("1234", 3.14, 17)

            // when/then
            mockMvc.patch(basePath) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedBank)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType("application/json")
                        json(objectMapper.writeValueAsString(updatedBank))
                    }
                }

            mockMvc.get("$basePath/${updatedBank.accountNumber}")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType("application/json")
                        json(objectMapper.writeValueAsString(updatedBank))
                    }
                }
        }

        @Test
        fun `should return NOT FOUND if no bank with given account number exists`() {
            // given
            val invalidBank = Bank("bla", 3.14, 17)

            // when/then
            mockMvc.patch(basePath) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("DELETE /banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteBank {
        @Test
        @DirtiesContext
        fun `should delete the bank with the given account number`() {
            // given
            val accountNumber = "toBeDeleted"
            val newBank = Bank(accountNumber, 3.14, 17)

            mockMvc.post(basePath) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }

            // when/then
            mockMvc.delete("$basePath/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNoContent() }
                }

            mockMvc.get("$basePath/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }

        @Test
        fun `should return NOT FOUND if no bank with given account number exists`() {
            // given
            val invalidAccountNumber = "bla"

            // when/then
            mockMvc.delete("$basePath/$invalidAccountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }
}