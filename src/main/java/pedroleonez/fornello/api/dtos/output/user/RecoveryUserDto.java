package pedroleonez.fornello.api.dtos.output.user;

import java.util.List;

public record RecoveryUserDto(

        Long id,

        String email,

        List<RecoveryRoleDto> roles

) {
}
