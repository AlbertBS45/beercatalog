package com.catalog.beercatalog.security;

import com.catalog.beercatalog.entity.Beer;
import com.catalog.beercatalog.entity.Provider;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean isItOwnManufacturer(Beer beer) {
        
        Boolean isAdmin = this.authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
        Boolean isManufacturer = this.authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_MANUFACTURER"));
        
        // Verify if auth user has the role Admin
        if (isAdmin) {
            return true;
        }
        
        // Verify if authorized user has role Manufacturer
        if (isManufacturer) {

            // Obtain manufacturer id from beer
            Long beerManufacturerId = beer.getManufacturer().getId();
            this.isFullyAuthenticated()
            // Obtain manufacturer id from authenticated user
            Provider authenticatedProvider = (Provider) this.authentication.getPrincipal();
            Long authUserManufacturerId = authenticatedProvider.getManufacturer().getId();

            //Proceed to check if is it own beer manufacturer
            if (beerManufacturerId != null && beerManufacturerId == authUserManufacturerId) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

}
