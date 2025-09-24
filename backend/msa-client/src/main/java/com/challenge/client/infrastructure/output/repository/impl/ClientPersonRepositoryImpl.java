package com.challenge.client.infrastructure.output.repository.impl;

import com.challenge.client.infrastructure.output.repository.ClientPersonRepository;
import com.challenge.client.infrastructure.output.repository.entity.ClientEntity;
import com.challenge.client.infrastructure.output.repository.entity.PersonEntity;
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
      SELECT c.client_id, c.password, c.status,
             p.person_id, p.name, p.gender, p.age,
             p.identification, p.address, p.phone
      FROM clients c
      JOIN persons p ON c.person_id = p.person_id
      WHERE c.status = 1
      """;

  private final DatabaseClient client;

  @Override
  public Flux<Tuple2<ClientEntity, PersonEntity>> findAll() {
    return client.sql(BASE_SELECT)
        .map(this::mapRow)
        .all();
  }

  @Override
  public Mono<Tuple2<ClientEntity, PersonEntity>> findByIdentification(String identification) {
    return client.sql(BASE_SELECT + " AND p.identification = :identification")
        .bind("identification", identification)
        .map(this::mapRow)
        .one();
  }

  private Tuple2<ClientEntity, PersonEntity> mapRow(Row row, RowMetadata meta) {
    ClientEntity clientEntity = new ClientEntity();
    clientEntity.setClientId(row.get("client_id", Integer.class));
    clientEntity.setPassword(row.get("password", String.class));
    clientEntity.setStatus(row.get("status", Boolean.class));

    PersonEntity personEntity = new PersonEntity();
    personEntity.setPersonId(row.get("person_id", Integer.class));
    personEntity.setName(row.get("name", String.class));
    personEntity.setGender(row.get("gender", String.class));
    personEntity.setAge(row.get("age", Integer.class));
    personEntity.setIdentification(row.get("identification", String.class));
    personEntity.setAddress(row.get("address", String.class));
    personEntity.setPhone(row.get("phone", String.class));

    clientEntity.setPersonId(personEntity.getPersonId());

    return Tuples.of(clientEntity, personEntity);
  }
}
