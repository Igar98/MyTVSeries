package com.example.monolith.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.monolith.domain.AppUser;
import com.example.monolith.web.model.AppUserDto;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUserDto appUserToAppUserDto(AppUser appUser);
    
    @Mapping(target = "ratings", ignore = true)
    AppUser appUserDtoToAppUser(AppUserDto appUserDto);
}
