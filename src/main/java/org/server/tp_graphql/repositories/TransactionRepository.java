package org.server.tp_graphql.repositories;

import org.server.tp_graphql.classes.Compte;
import org.server.tp_graphql.classes.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByCompte(Compte compte);
    @Query("SELECT SUM(t.montant) FROM Transaction t where t.type='DEPOT'")
    Double sumDepots();
    @Query("SELECT SUM(t.montant) FROM Transaction t where t.type='RETRAIT'")
    Double sumRetraits();
}
