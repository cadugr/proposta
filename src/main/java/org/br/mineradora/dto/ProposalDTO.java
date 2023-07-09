package org.br.mineradora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@ToString
public class ProposalDTO {

    private Long proposalId;
    private String customer;
    private BigDecimal priceTonne;
}
