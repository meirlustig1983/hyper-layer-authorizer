package com.hyperlayer.hyperlayerauthorizer.services;

import com.hyperlayer.hyperlayerauthorizer.dta.DbMerchant;
import com.hyperlayer.hyperlayerauthorizer.dto.Merchant;
import com.hyperlayer.hyperlayerauthorizer.facade.DataFacade;
import com.hyperlayer.hyperlayerauthorizer.mappers.MerchantMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MerchantService {

    private final DataFacade dataFacade;
    private final MerchantMapper merchantMapper;

    public MerchantService(DataFacade dataFacade, MerchantMapper merchantMapper) {
        this.dataFacade = dataFacade;
        this.merchantMapper = merchantMapper;
    }

    public List<Merchant> findAll() {
        List<Merchant> data = dataFacade.getMerchants().stream().map(merchantMapper::mapToMerchant).toList();
        log.info("get all merchants data. size: {}", data.size());
        return data;
    }

    public Merchant findById(String merchantId) {
        log.info("get merchant data. merchantId: {}", merchantId);
        Optional<DbMerchant> result = dataFacade.getMerchantById(new ObjectId(merchantId));
        return result.map(merchantMapper::mapToMerchant).orElse(null);
    }

    public Merchant save(Merchant merchant) {
        DbMerchant dbMerchant = merchantMapper.mapToDbMerchant(merchant);
        dbMerchant.setCreatedDate(LocalDateTime.now());
        Optional<DbMerchant> result = dataFacade.saveMerchant(dbMerchant);
        log.info("save new merchant data. merchantId: {}",
                result.<Object>map(DbMerchant::getMerchantId).orElse(null));
        return result.map(merchantMapper::mapToMerchant).orElse(null);
    }

    public void deleteById(String merchantId) {
        log.info("delete merchant data. merchantId: {}", merchantId);
        dataFacade.deleteMerchantById(new ObjectId(merchantId));
    }
}