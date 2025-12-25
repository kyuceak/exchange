package com.kutay.exchange.modules.Wallet.mapper;

import com.kutay.exchange.modules.Wallet.web.DTO.WalletRequest;
import com.kutay.exchange.modules.Wallet.web.DTO.WalletResponse;
import com.kutay.exchange.modules.Wallet.domain.model.WalletEntity;
import com.kutay.exchange.shared.Mapper.BaseMapper;

public class WalletMapper extends BaseMapper<WalletEntity, WalletRequest, WalletResponse> {
    @Override
    public WalletEntity convertToEntity(WalletRequest dto, Object... args) {
        return null;
    }

    @Override
    public WalletResponse convertToDTO(WalletEntity entity, Object... args) {

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
