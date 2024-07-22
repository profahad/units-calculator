package com.fahad.common_data.repository

import com.fahad.common_data.service.CommonDataService
import com.fahad.common_domain.repository.CommonRepository
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val dataService: CommonDataService,
) : CommonRepository {
}
