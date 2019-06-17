package my.deler.moneycontroller.entity.usecase

import my.deler.moneycontroller.data.repository.SheetsRepository
import my.deler.moneycontroller.entity.entities.IncomingEntity
import my.deler.moneycontroller.entity.entities.toData
import javax.inject.Inject


class AddIncomingUseCase @Inject constructor(private val sheetsRepository: SheetsRepository) :
    UseCase<IncomingEntity, Unit>() {

    override suspend fun execute(params: IncomingEntity?) {
        params?.let {
            sheetsRepository.addIncoming(it.toData())
        }
    }
}