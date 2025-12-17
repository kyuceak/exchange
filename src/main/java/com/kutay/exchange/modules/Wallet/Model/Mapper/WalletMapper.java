package com.kutay.exchange.modules.Wallet.Model.Mapper;

import com.kutay.exchange.modules.Wallet.DTO.WalletRequest;
import com.kutay.exchange.modules.Wallet.DTO.WalletResponse;
import com.kutay.exchange.modules.Wallet.Model.WalletEntity;
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
                entity.getCreated_at()
        );

        return dto;
    }
}
