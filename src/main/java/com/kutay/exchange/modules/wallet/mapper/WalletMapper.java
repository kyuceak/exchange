package com.kutay.exchange.modules.wallet.mapper;

import com.kutay.exchange.modules.wallet.api.WalletAccountSpec;
import com.kutay.exchange.modules.wallet.web.dto.WalletResponse;
import com.kutay.exchange.modules.wallet.domain.model.Wallet;
import com.kutay.exchange.shared.Mapper.BaseMapper;

public class WalletMapper extends BaseMapper<Wallet, WalletAccountSpec, WalletResponse> {
    @Override
    public Wallet convertToEntity(WalletAccountSpec dto, Object... args) {
        return null;
    }

    @Override
    public WalletResponse convertToDTO(Wallet entity, Object... args) {

        if (entity == null) {
            return null;
        }

        WalletResponse dto = new WalletResponse(
                entity.getId(),
                entity.getWalletType(),
                entity.getWalletStatus(),
                entity.getCreatedAt()
        );

        return dto;
    }
}
