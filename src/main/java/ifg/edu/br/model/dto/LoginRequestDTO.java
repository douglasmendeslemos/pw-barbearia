package ifg.edu.br.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @Email
    @NotBlank(message = "email nao vazio")
    String email,
    @NotBlank
    String senha
) {
}
