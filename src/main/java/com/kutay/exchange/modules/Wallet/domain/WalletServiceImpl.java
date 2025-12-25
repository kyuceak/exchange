package com.kutay.exchange.modules.Wallet.domain;

import com.kutay.exchange.modules.Wallet.web.DTO.WalletRequest;
import com.kutay.exchange.modules.Wallet.web.DTO.WalletResponse;
import com.kutay.exchange.modules.Wallet.mapper.WalletMapper;
import com.kutay.exchange.modules.Wallet.domain.model.WalletEntity;
import com.kutay.exchange.modules.Wallet.domain.model.enums.WalletStatus;
import com.kutay.exchange.modules.Wallet.infrastructure.persistence.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper = new WalletMapper();

    public List<WalletResponse> getAllWallets() {

        List<WalletEntity> wallets = walletRepository.findAll();

        return walletMapper.convertToDtoList(wallets);
    }

    public WalletResponse getWallet(Long walletId) {

        Optional<WalletEntity> result = walletRepository.findById(walletId);

        WalletEntity wallet = null;

        if (result.isPresent()) {
            wallet = result.get();
        } else {
            throw new RuntimeException("user not found");
        }

        return walletMapper.convertToDTO(wallet);
    }

    public List<WalletResponse> getWalletsForCustomer(Long customerId) {

        List<WalletEntity> customerWallets = walletRepository.findAllByCustomerId(customerId);

        return walletMapper.convertToDtoList(customerWallets);
    }

    public WalletResponse createWallet(WalletRequest dto) {
        WalletEntity walletEntity = new WalletEntity(dto.customerId(), dto.walletType());

        WalletEntity db_wallet = walletRepository.save(walletEntity);

        return walletMapper.convertToDTO(db_wallet);
    }

    public void deleteWallet(Long walletId) {
        walletRepository.deleteById(walletId);
    }

    public void validateWalletIsActive(Long walletId) {

        WalletEntity wallet = walletRepository.findById(walletId).orElseThrow(() ->
                new EntityNotFoundException("Wallet not found " + walletId));


        if (wallet.getWalletStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Wallet is not active " + walletId);
        }
    }
}
