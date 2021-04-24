package org.acme.getting.started.service;

import org.acme.getting.started.entity.Person;
import org.acme.getting.started.exceptions.AppException;
import org.acme.getting.started.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    PersonService personService;
    PersonRepository personRepository;
    Validator validator;

    @BeforeEach
    public void setUp() {
        personRepository = mock(PersonRepository.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        personService = new PersonService(personRepository, validator);
    }

    @Test
    @DisplayName("should execute query order by name")
    public void shouldInvokeFind() {
        personService.listAll();
        Mockito.verify(personRepository, times(1)).findOrderByName();
    }

    @Test
    @DisplayName("given a person valid then should save person")
    public void shouldSavePerson() {
        var personValid = new Person();
        personValid.name = "netodevel";

        personService.create(personValid);
        verify(personRepository, times(1)).persist(eq(personValid));
    }

    @Test
    @DisplayName("given a person invalid should return IllegalArgumentException")
    public void shouldReturnException() {
        var exception = assertThrows(AppException.class, () -> {
            var personInvalid = new Person();
            personService.create(personInvalid);
        });
        assertEquals("name my not be blank", exception.getMessage());
    }

    @Test
    @DisplayName("give a data person should update a person")
    public void shouldUpdatedPerson() {
        var personToUpdate = new Person();
        personToUpdate.name = "xpto";

        var personFromId = new Person();
        personFromId.name = "first name";
        Mockito.when(personRepository.findById(1L)).thenReturn(personFromId);

        personService.updatePerson(1L, personToUpdate);
        Mockito.verify(personRepository, times(1)).persist(any(Person.class));
    }

    @Test
    @DisplayName("given a invalid id to update should return exception")
    public void shouldReturnExceptionWhenUpdate() {
        var personToUpdate = new Person();
        personToUpdate.name = "xpto";

        var personFromId = new Person();
        personFromId.name = "first name";
        Mockito.when(personRepository.findById(1L)).thenReturn(personFromId);

        Assertions.assertThrows(AppException.class, () -> {
            personService.updatePerson(2L, personToUpdate);
        });
    }

    @Test
    @DisplayName("given a person_id should delete a person")
    public void shouldDeletePerson() {
        var personFromId = new Person();
        personFromId.name = "first name";
        Mockito.when(personRepository.findById(1L)).thenReturn(personFromId);

        personService.deletePerson(1L);
        Mockito.verify(personRepository, times(1)).deleteById(eq(1L));
    }

    @Test
    @DisplayName("given a person_id invalid should return excpetion")
    public void notShouldDeletePerson() {
        Assertions.assertThrows(AppException.class, () -> {
            personService.deletePerson(101010L);
        });
    }

}