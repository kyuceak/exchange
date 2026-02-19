package com.kutay.exchange.modules.customer.domain;

import com.kutay.exchange.modules.customer.web.dto.CustomerRequest;
import com.kutay.exchange.modules.customer.api.CustomerFacade;

import com.kutay.exchange.modules.customer.web.dto.CustomerResponse;
import com.kutay.exchange.modules.wallet.api.WalletAccountSpec;
import com.kutay.exchange.modules.wallet.api.WalletFacade;
import com.kutay.exchange.modules.wallet.domain.model.enums.WalletType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerFacadeImpl implements CustomerFacade {
    private final CustomerServiceImpl customerService;
    private final WalletFacade walletFacade;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        CustomerResponse customer = customerService.createCustomer(request);
        walletFacade.createWallet(new WalletAccountSpec(customer.id(), WalletType.SPOT));
        return customer;
    }

    @Override
    public CustomerResponse readUser(Long customerId) {
        return customerService.readUser(customerId);
    }

    @Override
    public List<CustomerResponse> readUsers() {
        return customerService.readUsers();
    }

    @Override
    public boolean existsById(Long id) {
        return customerService.existsById(id);
    }
}