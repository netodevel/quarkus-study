package br.com.netodevel.demo.service;

import br.com.netodevel.demo.entity.Person;
import br.com.netodevel.demo.exceptions.AppException;
import br.com.netodevel.demo.repository.PersonRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PersonService {

    private final PersonRepository personRepository;
    private final Validator validator;

    @Inject
    public PersonService(PersonRepository repository, Validator validator) {
        this.personRepository = repository;
        this.validator = validator;
    }

    public List<Person> listAll() {
        return personRepository.findOrderByName();
    }

    @Transactional
    public Person create(Person person) {
        var violations = validator.validate(person);
        if (violations.isEmpty()) {
            personRepository.persist(person);
            return personRepository.findById(person.id);
        }

        String allViolations = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        throw new AppException(400, allViolations);
    }

    public Person findById(Long id) {
        return personRepository.findByIdOptional(id).orElseThrow(()-> {
            throw new AppException(204, "person_id ".concat(String.valueOf(id)).concat(" not found"));
        });
    }

    @Transactional
    public Person updatePerson(Long id, Person personToUpdate) {
        var person = this.findById(id);

        if (personToUpdate.name != null && !personToUpdate.name.isBlank()) {
            person.name = personToUpdate.name;
        }
        if (personToUpdate.birth != null) {
            person.birth = personToUpdate.birth;
        }

        personRepository.persist(person);
        return person;
    }

    @Transactional
    public String deletePerson(long id) {
        this.findById(id);
        personRepository.deleteById(id);
        return "deleted";
    }

}
