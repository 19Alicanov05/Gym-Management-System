package org.example.gymmanagementsystem.servicegit

import org.example.gymmanagementsystem.dao.entity.TrainerEntity
import org.example.gymmanagementsystem.dao.repository.TrainerRepository
import org.example.gymmanagementsystem.exceptions.NotFoundException
import org.example.gymmanagementsystem.mapper.TrainerMapper
import org.example.gymmanagementsystem.model.TrainerDto
import org.example.gymmanagementsystem.service.impl.TrainerServiceImpl
import spock.lang.Specification

class TrainerServiceTest extends Specification {
    private TrainerRepository trainerRepository
    private TrainerMapper trainerMapper
    private TrainerServiceImpl trainerService
    private EnhanceRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    void setup() {
        trainerRepository = Mock()
        trainerMapper = Mock()
        trainerService = new TrainerServiceImpl(trainerRepository, trainerMapper)
    }

    def "GetAllTrainers returns list of TrainerDto"() {
        given: "a list of trainers returned from the repository"
        def trainerDtoList = [random.nextObject(TrainerDto)]
        def trainerEntityList = [random.nextObject(TrainerEntity)]

        when: "the getAllTrainers method is called"
        def result = trainerService.getAllTrainers()

        then: "the repository and mapper are called once each"
        1 * trainerRepository.findAll() >> trainerEntityList
        1 * trainerMapper.toDtoList(trainerEntityList) >> trainerDtoList

        result == trainerDtoList
    }

    def "getTrainerById - success"() {
        given:
        def id = random.nextInt()
        def trainerEntity = random.nextObject(TrainerEntity)
        def trainerDto = random.nextObject(TrainerDto)

        when:
        def result = trainerService.getTrainerById(id)

        then:
        1 * trainerRepository.findById(id) >> Optional.of(trainerEntity)
        1 * trainerMapper.toDto(trainerEntity) >> trainerDto

        result == trainerDto
    }

    def "getTrainerById - NotFoundException"() {
        given:
        def id = random.nextInt()

        when:
        def result = trainerService.getTrainerById(id)

        then:
        1 * trainerRepository.findById(id) >> Optional.empty()
        0 * trainerMapper.toDto(_)

        def exception = thrown(NotFoundException)
        exception.message == "Trainer not found"

        result == null
    }
}
