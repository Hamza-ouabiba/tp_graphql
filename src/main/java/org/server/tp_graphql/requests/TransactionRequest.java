package org.server.tp_graphql.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.tp_graphql.enums.TypeTransaction;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
        private Long compteId;
        private Double montant;
        private TypeTransaction type;
        private Date date;
}
