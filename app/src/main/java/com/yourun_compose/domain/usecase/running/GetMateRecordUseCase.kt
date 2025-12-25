package com.yourun_compose.domain.usecase.running

import com.yourun_compose.data.repository.RunningRepository
import javax.inject.Inject

class GetMateRecordUseCase @Inject constructor(
    private val repository: RunningRepository
) {
    suspend operator fun invoke(mateId: Long): Result<Int> {
        return repository.getMateRunningRecord(mateId)
    }
}