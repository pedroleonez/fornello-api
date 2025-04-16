package pedroleonez.fornello.api.mappers;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pedroleonez.fornello.api.dtos.output.user.RecoveryRoleDto;
import pedroleonez.fornello.api.dtos.output.user.RecoveryUserDto;
import pedroleonez.fornello.api.entities.Role;
import pedroleonez.fornello.api.entities.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(qualifiedByName = "mapRoleListToRoleDtoList", target = "roles")
    @Named("mapUserToUserDto")
    RecoveryUserDto mapUserToUserDto(User user);

    @Named("mapRoleListToRoleDtoList")
    @IterableMapping(qualifiedByName = "mapRoleToRoleDto")
    List<RecoveryRoleDto> mapRoleListToRoleDtoList(List<Role> roles);

    @Named("mapRoleToRoleDto")
    RecoveryRoleDto mapRoleToRoleDto(Role role);

}
