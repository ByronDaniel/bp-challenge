package com.challengue.client.infrastructure.output.repository.impl;

import com.challengue.client.infrastructure.output.repository.ClientPersonRepository;
import com.challengue.client.infrastructure.output.repository.entity.ClientEntity;
import com.challengue.client.infrastructure.output.repository.entity.PersonEntity;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Repository
@RequiredArgsConstructor
public class ClientPersonRepositoryImpl implements ClientPersonRepository {

  private static final String BASE_SELECT = """
      SELECT c.id, c.password, c.status,
             p.id AS personId, p.name, p.gender, p.age,
             p.identification, p.address, p.phone
      FROM clients c
      JOIN persons p ON c.personId = p.id
      """;

  private final DatabaseClient client;

  @Override
  public Flux<Tuple2<ClientEntity, PersonEntity>> findAllWithPerson() {
    return client.sql(BASE_SELECT)
        .map(this::mapRow)
        .all();
  }

  @Override
  public Mono<Tuple2<ClientEntity, PersonEntity>> findByIdWithPerson(Integer id) {
    return client.sql(BASE_SELECT + " WHERE c.id = :id")
        .bind("id", id)
        .map(this::mapRow)
        .one();
  }

  private Tuple2<ClientEntity, PersonEntity> mapRow(Row row, RowMetadata meta) {
    ClientEntity clientEntity = new ClientEntity();
    clientEntity.setClientId(row.get("id", Integer.class));
    clientEntity.setPassword(row.get("password", String.class));
    clientEntity.setStatus(row.get("status", Boolean.class));
    clientEntity.setPersonId(row.get("personId", Integer.class));

    PersonEntity personEntity = new PersonEntity();
    personEntity.setId(row.get("personId", Integer.class));
    personEntity.setName(row.get("name", String.class));
    personEntity.setGender(row.get("gender", String.class));
    personEntity.setAge(row.get("age", Integer.class));
    personEntity.setIdentification(row.get("identification", String.class));
    personEntity.setAddress(row.get("address", String.class));
    personEntity.setPhone(row.get("phone", String.class));

    return Tuples.of(clientEntity, personEntity);
  }
}
