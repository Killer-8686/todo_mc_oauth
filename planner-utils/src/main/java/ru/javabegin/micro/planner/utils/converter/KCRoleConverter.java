package ru.javabegin.micro.planner.utils.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

// Конвертер для данных из JWT  в роли spring security
public class KCRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        //Получаем доступ к разделу JWT
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

        // Если раздел JSON не найдет, то нет ролей
        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>(); // пустая коллекция - нет ролей
        }

        // для того, чтобы spring контейнер понимал роли из jwt -
        // нужно их преобразовать в коллекцию GrantedAuthority
        Collection<GrantedAuthority> returnValue = new ArrayList<>();

        for (String roleName : (List<String>) realmAccess.get("roles")) {
            returnValue.add(new SimpleGrantedAuthority("ROLE" + roleName));
        }


        return returnValue;
    }
}
