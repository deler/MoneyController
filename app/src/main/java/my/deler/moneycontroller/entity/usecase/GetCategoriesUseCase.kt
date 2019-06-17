package my.deler.moneycontroller.entity.usecase

import my.deler.moneycontroller.data.repository.SheetsRepository
import my.deler.moneycontroller.entity.entities.CategoryItemEntity
import my.deler.moneycontroller.entity.entities.CategoryType
import my.deler.moneycontroller.entity.entities.toEntity
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val sheetsRepository: SheetsRepository) :
    UseCase<Unit, Map<CategoryType, List<CategoryItemEntity>>>() {

    override suspend fun execute(params: Unit?): Map<CategoryType, List<CategoryItemEntity>> {
        val categories = sheetsRepository.getCategories()
        return categories.asSequence()
            .map { it.toEntity() }
            .filter { it.category != CategoryType.UNKNOWN }
            .groupBy { it.category }
    }

}