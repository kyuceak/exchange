package com.kutay.exchange.modules.Wallet.mapper;

import com.kutay.exchange.modules.Wallet.web.DTO.WalletRequest;
import com.kutay.exchange.modules.Wallet.web.DTO.WalletResponse;
import com.kutay.exchange.modules.Wallet.domain.model.Wallet;
import com.kutay.exchange.shared.Mapper.BaseMapper;

public class WalletMapper extends BaseMapper<Wallet, WalletRequest, WalletResponse> {
    @Override
    public Wallet convertToEntity(WalletRequest dto, Object... args) {
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
