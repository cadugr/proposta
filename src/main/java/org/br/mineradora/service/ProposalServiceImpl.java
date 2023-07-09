package org.br.mineradora.service;

import org.br.mineradora.dto.ProposalDTO;
import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.entity.ProposalEntity;
import org.br.mineradora.message.KafkaEvent;
import org.br.mineradora.repository.ProposalRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Date;

@ApplicationScoped
public class ProposalServiceImpl implements ProposalService {

    @Inject
    private ProposalRepository proposalRepository;

    @Inject
    private KafkaEvent kafkaMessages;

    @Override
    public ProposalDetailsDTO findFullProposal(Long id) {
        ProposalEntity proposal = proposalRepository.findById(id);
        return ProposalDetailsDTO.builder()
                .proposalId(proposal.getId())
                .customer(proposal.getCustomer())
                .priceTonne(proposal.getPriceTonne())
                .tonnes(proposal.getTonnes())
                .country(proposal.getCountry())
                .proposalValidityDays(proposal.getProposalValidityDays())
                .build();
    }

    @Override
    @Transactional
    public void createNewProposal(ProposalDetailsDTO proposalDetailsDTO) {
        ProposalDTO proposal = buildAndSaveNewProposal(proposalDetailsDTO);
        System.out.println(proposal);
        kafkaMessages.sendNewKafkaEvent(proposal);
    }

    @Override
    @Transactional
    public void removalProposal(Long id) {
        proposalRepository.deleteById(id);
    }

    @Transactional
    private ProposalDTO buildAndSaveNewProposal(ProposalDetailsDTO proposalDetailsDTO) {
        try {
            ProposalEntity proposalEntity = new ProposalEntity();
            proposalEntity.setCreated(new Date());
            proposalEntity.setCustomer(proposalDetailsDTO.getCustomer());
            proposalEntity.setPriceTonne(proposalDetailsDTO.getPriceTonne());
            proposalEntity.setTonnes(proposalDetailsDTO.getTonnes());
            proposalEntity.setCountry(proposalDetailsDTO.getCountry());
            proposalEntity.setProposalValidityDays(proposalDetailsDTO.getProposalValidityDays());

            proposalRepository.persist(proposalEntity);

            return ProposalDTO.builder()
                    .proposalId(proposalEntity.getId())
                    .priceTonne(proposalEntity.getPriceTonne())
                    .customer(proposalEntity.getCustomer())
                    .build();
        } catch(Exception ex) {
            ex.getStackTrace();
            throw new RuntimeException();
        }
    }
}
