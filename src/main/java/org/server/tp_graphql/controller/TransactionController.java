package org.server.tp_graphql.controller;

import lombok.AllArgsConstructor;
import org.server.tp_graphql.classes.Compte;
import org.server.tp_graphql.classes.Transaction;
import org.server.tp_graphql.enums.TypeTransaction;
import org.server.tp_graphql.repositories.CompteRepository;
import org.server.tp_graphql.repositories.TransactionRepository;
import org.server.tp_graphql.requests.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {
    @Autowired
    private CompteRepository compteRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @MutationMapping
    public Transaction addTransaction(@Argument TransactionRequest transactionRequest) {
        Compte compte = compteRepository.findById(transactionRequest.getCompteId())
                .orElseThrow(() -> new RuntimeException("Compte not found"));

        Transaction transaction = new Transaction();

        if(transactionRequest.getType() == TypeTransaction.DEPOT) {
            compte.setSolde(transactionRequest.getMontant() + compte.getSolde());
        } else {
            if(compte.getSolde() - transactionRequest.getMontant() > 0) {
                compte.setSolde(compte.getSolde() - transactionRequest.getMontant());
            } else throw new RuntimeException("Solde insuffisant");
        }


        compteRepository.save(compte);
        transaction.setMontant(transactionRequest.getMontant());
        transaction.setDate(new Date());
        transaction.setType(transactionRequest.getType());
        transaction.setCompte(compte);
        transactionRepository.save(transaction);
        return transaction;
    }
    @QueryMapping
    public List<Transaction> compteTransactions(@Argument Long id) {
        Compte compte = compteRepository.findById(id).orElseThrow(() -> new RuntimeException("Compte not found"));
        return transactionRepository.findByCompte(compte);
    }

    @QueryMapping
    public List<Transaction> allTransactions() {
        return transactionRepository.findAll();
    }
    @QueryMapping
    public Map<String, Object> transactionStats() {
        long count = transactionRepository.count();
        Double sumDepots = transactionRepository.sumDepots();
        Double sumRetraits = transactionRepository.sumRetraits();

        return Map.of(
                "count", count,
                "sumDepots", sumDepots != null ? sumDepots : 0.0,
                "sumRetraits", (sumRetraits != null) ? sumRetraits : 0.0
        );
    }

}
