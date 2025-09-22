package com.challengue.client.infrastructure.output.repository.mapper;

import com.challengue.client.domain.Client;
import com.challengue.client.domain.Person;
import com.challengue.client.infrastructure.output.repository.entity.ClientEntity;
import com.challengue.client.infrastructure.output.repository.entity.PersonEntity;
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

  @Mapping(target = "id", source = "personId")
  PersonEntity toPersonEntity(Person person);

  @InheritInverseConfiguration
  Person toPerson(PersonEntity personEntity);

  @Mapping(target = "clientId", source = "clientEntity.clientId")
  @Mapping(target = "personId", source = "personEntity.id")
  Client toClient(ClientEntity clientEntity, PersonEntity personEntity);
}
