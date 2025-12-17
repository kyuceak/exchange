package com.kutay.exchange.modules.Wallet.Service;

import com.kutay.exchange.modules.Wallet.DTO.WalletRequest;
import com.kutay.exchange.modules.Wallet.DTO.WalletResponse;
import com.kutay.exchange.modules.Wallet.Model.Mapper.WalletMapper;
import com.kutay.exchange.modules.Wallet.Model.WalletEntity;
import com.kutay.exchange.modules.Wallet.Model.enums.WalletStatus;
import com.kutay.exchange.modules.Wallet.Repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper = new WalletMapper();

    @Override
    public List<WalletResponse> getAllWallets() {

        List<WalletEntity> wallets = walletRepository.findAll();

        return walletMapper.convertToDtoList(wallets);
    }

    @Override
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

    @Override
    public List<WalletResponse> getWalletsForCustomer(Long customerId) {

        List<WalletEntity> customerWallets = walletRepository.findAllByCustomerId(customerId);

        return walletMapper.convertToDtoList(customerWallets);
    }

    @Override
    public WalletResponse createWallet(WalletRequest dto) {
        WalletEntity walletEntity = new WalletEntity(dto.customerId(), dto.walletType());

        WalletEntity db_wallet = walletRepository.save(walletEntity);

        return walletMapper.convertToDTO(db_wallet);
    }

    @Override
    public void deleteWallet(Long walletId) {
        walletRepository.deleteById(walletId);
    }

    @Override
    public void validateWalletIsActive(Long walletId) {

        WalletEntity wallet = walletRepository.findById(walletId).orElseThrow(() ->
                new EntityNotFoundException("Wallet not found " + walletId));


        if (wallet.getWalletStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Wallet is not active " + walletId);
        }
    }
}
