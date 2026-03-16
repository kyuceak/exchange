package com.kutay.exchange.modules.wallet.application;

import com.kutay.exchange.modules.ledger.api.LedgerFacade;
import com.kutay.exchange.modules.ledger.api.dto.LedgerAccountSpec;
import com.kutay.exchange.modules.wallet.api.WalletAccountSpec;
import com.kutay.exchange.modules.wallet.domain.model.WalletAsset;
import com.kutay.exchange.modules.wallet.domain.service.WalletAssetService;
import com.kutay.exchange.modules.wallet.domain.service.WalletService;
import com.kutay.exchange.modules.wallet.web.dto.WalletResponse;
import com.kutay.exchange.modules.wallet.mapper.WalletMapper;
import com.kutay.exchange.modules.wallet.domain.model.Wallet;
import com.kutay.exchange.modules.wallet.domain.model.enums.WalletStatus;
import com.kutay.exchange.modules.wallet.infrastructure.persistence.WalletRepository;
import com.kutay.exchange.shared.contracts.Asset;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletCoreService {
    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final WalletAssetService walletAssetService;
    private final WalletMapper walletMapper = new WalletMapper();
    private static final Set<Asset> DEFAULT_ASSETS = Set.of(
            Asset.BTC,
            Asset.ETH,
            Asset.USDT
    );
    private final LedgerFacade ledgerFacade;

    // create wallet (with register)
    // create walletAssets (bootstrap default when wallet creation)
    // create walletAsset ( for future added assets)

    @Transactional
    public void createWallet(WalletAccountSpec spec) {

        Wallet wallet = walletService.createWalletIfNotExists(spec);

        bootstrapDefaultAssets(wallet);

        log.info("Created wallet with default assets: id={}, customerId={}, type ={}", wallet.getId(), wallet.getCustomerId(), wallet.getWalletType());
    }

    private void bootstrapDefaultAssets(Wallet wallet) {

        for (Asset asset : DEFAULT_ASSETS) {
            walletAssetService.createWalletAssetIfNotExists(wallet, asset);

            ledgerFacade.createUserAccount(new LedgerAccountSpec(wallet.getId(), asset));
        }
    }

    @Transactional
    public UUID addWalletAsset(UUID walletId, Asset asset) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new EntityNotFoundException("wallet not found"));

        WalletAsset walletAsset = walletAssetService.createWalletAssetIfNotExists(wallet, asset);
        ledgerFacade.createUserAccount(new LedgerAccountSpec(wallet.getId(), asset));

        return walletAsset.getId();
    }


    public List<WalletResponse> getAllWallets() {

        List<Wallet> wallets = walletRepository.findAll();

        return walletMapper.convertToDtoList(wallets);
    }

    public WalletResponse getWallet(UUID walletId) {

        Optional<Wallet> result = walletRepository.findById(walletId);

        Wallet wallet = null;

        if (result.isPresent()) {
            wallet = result.get();
        } else {
            throw new EntityNotFoundException("wallet not found");
        }

        return walletMapper.convertToDTO(wallet);
    }

    public List<WalletResponse> getWalletsForCustomer(UUID customerId) {

        List<Wallet> customerWallets = walletRepository.findAllByCustomerId(customerId);

        return walletMapper.convertToDtoList(customerWallets);
    }


    public void deleteWallet(UUID walletId) {
        walletRepository.deleteById(walletId);
    }

    public void validateWalletIsActive(UUID walletId) {

        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() ->
                new EntityNotFoundException("Wallet not found " + walletId));


        if (wallet.getWalletStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Wallet is not active " + walletId);
        }
    }
}
