package com.challenge.client.infrastructure.output.repository.mapper;

import com.challenge.client.domain.Client;
import com.challenge.client.domain.Person;
import com.challenge.client.infrastructure.output.repository.entity.ClientEntity;
import com.challenge.client.infrastructure.output.repository.entity.PersonEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ClientEntityMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "clientId", source = "clientId")
  @Mapping(target = "password", source = "password")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "personId", source = "personId")
  Client toClient(ClientEntity clientEntity);

  ClientEntity toClientEntity(Client client);

  PersonEntity toPersonEntity(Person person);

  @InheritInverseConfiguration
  Person toPerson(PersonEntity personEntity);

  @Mapping(target = "clientId", source = "clientEntity.clientId")
  @Mapping(target = "personId", source = "personEntity.personId")
  Client toClient(ClientEntity clientEntity, PersonEntity personEntity);
}
