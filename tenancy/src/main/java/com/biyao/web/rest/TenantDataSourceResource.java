package com.biyao.web.rest;

import com.biyao.service.TenantDataSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/multi-tenancy")
@ConditionalOnProperty(prefix = "spring.jpa.properties.hibernate", name = {"multiTenancy", "tenant_identifier_resolver", "multi_tenant_connection_provider"})
public class TenantDataSourceResource {
    private static final Logger log = LoggerFactory.getLogger(TenantDataSourceResource.class);

    @Autowired
    private TenantDataSourceService tenantDataSourceService;

    @PostMapping("/tenant/data-source")
    public ResponseEntity<Void> addDataSource() {
        tenantDataSourceService.initRemoteDataSourceSync();
        return ResponseEntity.ok().build();
    }

}