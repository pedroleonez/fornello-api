package pedroleonez.fornello.api.dtos.input.order;

import jakarta.validation.constraints.NotBlank;

public record UpdateStatusOrderDto(

        @NotBlank(message = "{not.blank.message}")
        String status

) {
}
