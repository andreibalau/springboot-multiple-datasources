package com.test.multipledatasources;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.test.multipledatasources.model.animal.Animal;
import com.test.multipledatasources.model.forest.Forest;
import com.test.multipledatasources.repository.animal.AnimalRepository;
import com.test.multipledatasources.repository.forest.ForestRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
class MultipleDataSourcesApplicationTests {

    /*
     * We will be using mysql databases we configured in our properties file for our tests Make sure your datasource
     * connections are correct otherwise the test will fail
     */
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private ForestRepository forestRepository;
    private static Animal animal;
    private static Forest forest;

    @BeforeAll
    public static void initializeDataObjects() {
        animal = new Animal();
        animal.setSpecies("Dog");
        animal.setName("Roso");
        animal.setName("Pitbul");
        forest = new Forest();
        forest.setLocation("Brasov");
    }

    @Test
    public void shouldSaveAnimalToAnimalDB() {
        Animal savedAnimal = animalRepository.save(animal);
        Optional<Animal> animalFromDb = animalRepository.findById(savedAnimal.getId());
        assertTrue(animalFromDb.isPresent());
    }

    @Test
    public void shouldSaveForestToForestDB() {
        Forest savedForestHolder = forestRepository.save(forest);
        Optional<Forest> forestHolderFromDb = forestRepository.findById(savedForestHolder.getId());
        assertTrue(forestHolderFromDb.isPresent());
    }
}
